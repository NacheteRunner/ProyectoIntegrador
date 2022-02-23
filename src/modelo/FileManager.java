package modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;



import modelo.Actor;

public class FileManager implements AccesoADatos {

	private File archivo;

	public FileManager() {
		// archivo = new File("Ficheros/actores");
		try (InputStream input = new FileInputStream("config.properties")) {

			Properties prop = new Properties();

			// load a properties file
			prop.load(input);

			// get the property value and print it out

			archivo = new File(prop.getProperty("file.url"));

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public HashMap<Integer, Actor> leerInfo() throws Exception {
		// TODO Auto-generated method stub
		HashMap<Integer, Actor> hmAuxiliar = new HashMap<Integer, Actor>();
		String cadena = null;
		FileReader fr = null;
		BufferedReader br = null;
		String[] datosActor = null;
		try {
			fr = new FileReader(archivo);
			br = new BufferedReader(fr);
			while ((cadena = br.readLine()) != null) {
				datosActor = cadena.split("_");
				Actor objetoActor = new Actor();
				// System.out.println(Arrays.toString(datosActor));
				objetoActor.setId(Integer.parseInt(datosActor[0]));
				objetoActor.setNombre(datosActor[1]);
				objetoActor.setApellido(datosActor[2]);
				objetoActor.setPersonaje(datosActor[3]);

				hmAuxiliar.put(objetoActor.getId(), objetoActor);
			}
		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (null != br)
					br.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return hmAuxiliar;
	}

	@Override
	public void AddOne(Actor actor) throws Exception {
		// TODO Auto-generated method stub
		FileWriter fl = null;
		PrintWriter pw = null;
		try {
			fl = new FileWriter(archivo, true);
			pw = new PrintWriter(fl);
			pw.print(actor.getId() + "_");
			pw.print(actor.getNombre() + "_");
			pw.print(actor.getApellido() + "_");
			pw.println(actor.getPersonaje() + "_");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fl)
					fl.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
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
	public void DeleteAll() {
		FileWriter fl = null;

		try {
			fl = new FileWriter(archivo, false);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fl)
					fl.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}



	public boolean DeleteOne(int indice) {
		HashMap<Integer, Actor> miHM = new HashMap<Integer, Actor>();
		
		try {
		miHM  = this.leerInfo();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Actor a = miHM.remove(indice);
		AddAll(miHM);

		if (a == null) {
			return false;
		} else {
			return true;
		}

	}

	@Override
	public boolean ChangeOne(int indice, Actor nuevoActor) throws Exception {
		HashMap<Integer, Actor> hmAuxiliar = this.leerInfo();
		nuevoActor.setId(indice);
		Actor a = hmAuxiliar.put(indice, nuevoActor);
		
		if (a == null) {
			return false;
		} else {
			AddAll(hmAuxiliar);
			return true;
		}
		
	}

	@Override
	public Actor findOneById( int indice) throws Exception {
		HashMap<Integer, Actor> hmAuxiliar = this.leerInfo();
		
		Actor a = hmAuxiliar.get(indice);
		// return a??
		if(a == null) {
			return null;
		}else 
			return a;
	}

	@Override
	public HashMap<Integer, Actor> findBytext(String texto) {
		HashMap<Integer, Actor> miHM = new HashMap<Integer, Actor>();
		try {
			miHM = leerInfo();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashMap<Integer, Actor> hmAuxiliar = new HashMap<Integer, Actor>();
		Actor actor = new Actor();
		String textoEnMinus = texto.toLowerCase();
		for (Map.Entry<Integer, Actor> entry : miHM.entrySet()) {
			actor = entry.getValue();
			if ((actor.getNombre().toLowerCase().contains(textoEnMinus))
					|| (actor.getApellido().toLowerCase().contains(textoEnMinus))
					|| actor.getPersonaje().toLowerCase().contains(textoEnMinus)) {
				hmAuxiliar.put(actor.getId(), actor);
			}
		}
		return hmAuxiliar;
	}

	public int devolverId(HashMap<Integer, Actor> miHM) {
		int indiceMax = 1000;
		for (Map.Entry<Integer, Actor> entry : miHM.entrySet()) {
			if (entry.getKey() > indiceMax) {
				indiceMax = entry.getKey();
			}
		}
		return indiceMax + 1;
	}

	

}
