package modelo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.matisse.MtDatabase;
import com.matisse.MtObjectIterator;
import com.matisse.MtPackageObjectFactory;





public class MattiseManager implements AccesoADatos {
	
	MtDatabase db; 

	
	public MattiseManager() {
		
		String hostname = "localhost";
		String dbname = "prueba";		
		db = new MtDatabase(hostname, dbname, new MtPackageObjectFactory("", "modelo"));
		//db.open();  // Estoy abriendo y cerrando base de datos de momento
		// Se puede dejar abierta pero tendria que cerrarla antes de salir, en el controlador
		// y tendria que hacerlo para todos los accesos a Datos, managers.


	}
	@Override
	public HashMap<Integer, Actor> leerInfo() throws Exception {
		HashMap<Integer, Actor> hmAuxiliar = new HashMap<Integer, Actor>();
		try {
			db.open();
			db.startTransaction();
			// Me hace falta empezar una transacción poruqe me falla, me da una excepción
			// como si estuviera modificando algo
			// Crea una instancia de Statement
			Statement stmt = db.createStatement();
			// Asigna una consulta OQL. Esta consulta lo que hace es utilizar REF() para
			// obtener el objeto
			// directamente en vez de obtener valores concretos (que tambiÃ©n podrÃ­a ser).
			String commandText = "SELECT REF(a) from modelo.actorMattise a;";
			// Ejecuta la consulta y obtiene un ResultSet
			ResultSet rset = stmt.executeQuery(commandText);
			actorMattise act_mt;
			Actor a;
			// Lee rset uno a uno.
			int idAux = 1; // para darle un id
			while (rset.next()) {
				// Obtiene los objetos Autor.
				act_mt = (actorMattise) rset.getObject(1);
				//a = convertFromMatisse(act_mt, idAux++);
				a = convertFromMatisse(act_mt);
				hmAuxiliar.put(a.getId(), a);
				
			}
			// Cierra las conexiones
			rset.close();
			stmt.close();
			db.commit();
			db.close();
		} catch (SQLException e) {
			System.out.println("SQLException:  " + e.getMessage());
		}
		return hmAuxiliar;
	}

	@Override
	public void AddOne(Actor actor) throws Exception {
		db.open();
		db.startTransaction();
		actorMattise am = convertToMatisse(actor);
		
		db.commit();
		db.close();
		
	}

	@Override
	public void AddAll(HashMap<Integer, Actor> miHM) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DeleteAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean DeleteOne(int indice) {
		int nActores = 0;
	
		db.open();
		db.startTransaction();
		nActores = (int) actorMattise.getInstanceNumber(db);
		MtObjectIterator<actorMattise> iter = actorMattise.<actorMattise>instanceIterator(db);

		// System.out.println("recorro el iterador de uno en uno y cambio cuando encuentro 'nombre'");
		while (iter.hasNext()) {
			actorMattise[] actores = iter.next(nActores);
			for (int i = 0; i < actores.length; i++) {
				// Busca una autor con nombre 'nombre'
				if (actores[i].getId()==indice) {
					actores[i].remove();
				}

			}
			break;
		}
		iter.close();
		// materializa los cambios y cierra la BD
		db.commit();
		db.close();
		return true;
	}

	@Override
	public boolean ChangeOne(int indice, Actor actor) throws Exception {
		int nActores = 0;
		
		db.open();
		db.startTransaction();
		nActores = (int) actorMattise.getInstanceNumber(db);
		MtObjectIterator<actorMattise> iter = actorMattise.<actorMattise>instanceIterator(db);

		//System.out.println("recorro el iterador de uno en uno y cambio cuando encuentro 'nombre'");
		while (iter.hasNext()) {
			actorMattise[] actores = iter.next(nActores);
			actorMattise am = convertToMatisse(actor);
			for (int i = 0; i < actores.length; i++) {
				
				if (actores[i].getId()==indice) {
					actores[i] = am;
				}

			}
			break;
		}
		iter.close();
		// materializa los cambios y cierra la BD
		db.commit();
		db.close();
		return true;
	}

	@Override
	public Actor findOneById(int indice) throws Exception {
		int nActores = 0;
		Actor actor = null;
		db.open();
		db.startTransaction();
		nActores = (int) actorMattise.getInstanceNumber(db);
		MtObjectIterator<actorMattise> iter = actorMattise.<actorMattise>instanceIterator(db);

		// System.out.println("recorro el iterador de uno en uno y cambio cuando encuentro 'nombre'");
		while (iter.hasNext()) {
			
			actorMattise[] actores = iter.next(nActores);
			
			for (int i = 0; i < actores.length; i++) {
				
				if (actores[i].getId()==indice) {
					actor = new Actor();
					actor = convertFromMatisse(actores[i]);
					
				}

			}
			break;
		}
		iter.close();
		// materializa los cambios y cierra la BD
		db.commit();
		db.close();
		return actor;
	}

	@Override
	public HashMap<Integer, Actor> findBytext(String texto) {
		int nActores = 0;
		Actor actor = null;
		HashMap<Integer, Actor> hmAuxiliar = new HashMap<Integer, Actor>();
		db.open();
		db.startTransaction();
		nActores = (int) actorMattise.getInstanceNumber(db);
		MtObjectIterator<actorMattise> iter = actorMattise.<actorMattise>instanceIterator(db);

		// System.out.println("recorro el iterador de uno en uno y cambio cuando encuentro 'nombre'");
		while (iter.hasNext()) {
			
			actorMattise[] actores = iter.next(nActores);
			
			for (int i = 0; i < actores.length; i++) {
				
				if (actores[i].getNombre().contains(texto)
						||actores[i].getApellidos().contains(texto)
						||actores[i].getPersonaje().contains(texto)) {
					actor = new Actor();
					actor = convertFromMatisse(actores[i]);
					hmAuxiliar.put(actor.getId(), actor);
				}

			}
			break;
		}
		iter.close();
		// materializa los cambios y cierra la BD
		db.commit();
		db.close();
		return hmAuxiliar;
	}

	@Override
	public int devolverId(HashMap<Integer, Actor> miHM) {
		// TODO Auto-generated method stub
		int indiceMax = 0;
		for (Map.Entry<Integer, Actor> entry : miHM.entrySet()) {
			if (entry.getKey() > indiceMax) {
				indiceMax = entry.getKey();
			}
		}
		return indiceMax + 1;
	}
	
	private actorMattise convertToMatisse (Actor actor) {		
		actorMattise act_mt = new actorMattise(db);
		act_mt.setNombre(actor.getNombre());
		act_mt.setApellidos(actor.getApellido());
		act_mt.setPersonaje(actor.getPersonaje());	
		act_mt.setId(actor.getId());
		return act_mt;	
	}
	
	private Actor convertFromMatisse (actorMattise act_mt, int auxiliar) {
		Actor actor = new Actor();
		actor.setNombre(act_mt.getNombre());
		actor.setApellido(act_mt.getApellidos());
		actor.setPersonaje(act_mt.getPersonaje());
		actor.setId(auxiliar);
		return actor;
	}
	
	private Actor convertFromMatisse (actorMattise act_mt) {
		Actor actor = new Actor();
		actor.setNombre(act_mt.getNombre());
		actor.setApellido(act_mt.getApellidos());
		actor.setPersonaje(act_mt.getPersonaje());
		actor.setId(act_mt.getId());
		return actor;
	}
}
