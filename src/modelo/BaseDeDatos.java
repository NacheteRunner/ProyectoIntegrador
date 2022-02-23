package modelo;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class BaseDeDatos implements AccesoADatos {

	private String sURL;
	private String user;
	private String pass;
	private Connection con;

	public BaseDeDatos() {

		conectarBase();

	}

	public void conectarBase()  {
		
		// La idea de controlar las excepciones seria de hacerlo en el 
		// controlador, aqui lanzar un throws Exception y quitar el 
		// try catch, lanzarla hacia arriba y en el controlador pedirle
		// a la vista que muestre un mensaje, una excepcion de la base
		// de datos no es una excepcion recuperable, si no hay base de
		// datos no podemos hacer nada, no esta bien luego imprimir el
		// menu o lo que sea, no es lo mismo que si pides una opción
		// al usuario y te mete algo no númerico eso es recuperable

		// String sURL = "jdbc:mysql://localhost:3307/prueba";
		try {

			Properties prop = new Properties();
			prop.load(new FileReader("config.properties"));

			// get the property value and print it out
			sURL = prop.getProperty("db.path");
			user = prop.getProperty("db.user");
			pass = prop.getProperty("db.pass");

			con = DriverManager.getConnection(sURL, user, pass);
			
		} catch (IOException ex) {
			ex.printStackTrace();
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public HashMap<Integer, Actor> leerInfo() throws Exception {

		HashMap<Integer, Actor> miHM = new HashMap<>();

		try {
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM actores");
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Actor actor = new Actor();
				actor.setId(rs.getInt("id"));
				actor.setNombre(rs.getString("Nombre"));
				actor.setApellido(rs.getString("Apellido"));
				actor.setPersonaje(rs.getString("Personaje"));
				miHM.put(actor.getId(), actor);
			}
		} catch (SQLException sqle) {
			System.out.println("Error en la ejecución:" + sqle.getErrorCode() + " " + sqle.getMessage());
		}
		return miHM;
	}

	@Override
	public void AddOne(Actor actor) throws Exception {
		
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement("INSERT INTO actores (nombre, apellido, personaje) VALUES (?,?,?)");
			stmt.setString(1, actor.getNombre());
			stmt.setString(2, actor.getApellido());
			stmt.setString(3, actor.getPersonaje());
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void AddAll(HashMap<Integer, Actor> miHM) {
		
		this.DeleteAll();
		for (Map.Entry<Integer, Actor> entry : miHM.entrySet()) {
			try {
				Actor actor = new Actor();
				actor = entry.getValue();
				AddOne(actor);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void DeleteAll() {   // Metodo que usamos antes de AddAll
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement("TRUNCATE TABLE actores");
			stmt.executeUpdate();
			// System.out.println("Todos los registros han sido eliminados");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}

	}

	@Override
	public boolean DeleteOne(int indice) {

		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement("DELETE FROM actores WHERE ID= "+indice);
			if (stmt.executeUpdate()==0) {
				return false;
			}			
			return true;
		} catch (SQLException e) {
			
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public boolean ChangeOne( int indice, Actor actor) {
		
		try {
			PreparedStatement stmt = con.prepareStatement(
					"UPDATE actores SET " + "Nombre = ?" + ",Apellido = ?" + ",Personaje = ?" + "WHERE ID= "+indice);
			stmt.setString(1, actor.getNombre());
			stmt.setString(2, actor.getApellido());
			stmt.setString(3, actor.getPersonaje());
			if (stmt.executeUpdate()==0) {
				return false;
			}			
			return true;

		} catch (SQLException e) {
			
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public Actor findOneById(int indice) throws Exception {
		PreparedStatement stmt = con.prepareStatement("SELECT * FROM actores WHERE ID= "+indice);
		ResultSet rs = stmt.executeQuery();
		Actor a = new Actor();
		if (!rs.next()) {			
			return null;   // El constructor vacio de actor nos devolveria un actor con id=0
						   // No puedo devolver a pq a no es null, tendria todos los campos null menos el id a 0
		}else {			
			a.setId(rs.getInt("id"));
			a.setNombre(rs.getString("Nombre"));
			a.setApellido(rs.getString("Apellido"));
			a.setPersonaje(rs.getString("Personaje"));
			return a;
		}
	}

	@Override
	public HashMap<Integer, Actor> findBytext(String texto) {
		// TODO Auto-generated method stub
		String miTexto = "%"+texto+"%";
		HashMap<Integer,Actor> hmAuxiliar = new HashMap<>();
		try {
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM actores WHERE nombre LIKE ? OR apellido  LIKE ? OR personaje LIKE ?") ;
			stmt.setString(1, miTexto);
			stmt.setString(2, miTexto);
			stmt.setString(3, miTexto);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				// System.out.print (rs.getString("Nombre"));
				Actor a = new Actor();
				a.setId(rs.getInt("id"));
				a.setNombre(rs.getString("Nombre"));
				a.setApellido(rs.getString("Apellido"));
				a.setPersonaje(rs.getString("Personaje"));
				hmAuxiliar.put(a.getId(), a);	
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return hmAuxiliar;
		

		
	}

	@Override
	public int devolverId(HashMap<Integer, Actor> miHM) {
		// TODO Auto-generated method stub
		int indiceMax = 1000;
		for (Map.Entry<Integer, Actor> entry : miHM.entrySet()) {
			if (entry.getKey() > indiceMax) {
				indiceMax = entry.getKey();
			}
		}
		return indiceMax + 1;
	}

}
