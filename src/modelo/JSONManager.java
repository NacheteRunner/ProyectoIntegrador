package modelo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import auxiliares.ApiRequests;

public class JSONManager implements AccesoADatos {

	ApiRequests encargadoPeticiones;
	private String SERVER_PATH, GET_PLAYER, SET_PLAYER, DELETE_PLAYER, UPDATE_PLAYER; // Datos de la conexion

	public JSONManager() {
		encargadoPeticiones = new ApiRequests();

		SERVER_PATH = "http://localhost/AccesoADatos/ActoresJSONServer/";
		GET_PLAYER = "leeActores.php";
		SET_PLAYER = "escribirActor.php";
		DELETE_PLAYER = "borrarActor.php";
		UPDATE_PLAYER = "actualizarActor.php";
	}

	@Override
	public HashMap<Integer, Actor> leerInfo() throws Exception {
		HashMap<Integer, Actor> auxhm = this.leerAux("");

		return auxhm;
	}

	public HashMap<Integer, Actor> leerAux(String buscado) throws Exception {
		HashMap<Integer, Actor> auxhm = new HashMap<Integer, Actor>();

		try {

			System.out.println("---------- Leemos datos de JSON --------------------");

			System.out.println("Lanzamos peticion JSON para actores");

			String url = SERVER_PATH + GET_PLAYER; // Sacadas de configuracion

			try {
				Integer.parseInt(buscado);
				url += "?id_actor=" + buscado;
			} catch (NumberFormatException excepcion) {

				if (buscado.length() > 0) {
					url += "?texto=" + buscado;
				}

			}
			

			System.out.println("La url a la que lanzamos la petición es " + url); // Traza para pruebas

			String response = encargadoPeticiones.getRequest(url);

			System.out.println(response); // Traza para pruebas
//
//			System.exit(-1);

			// Parseamos la respuesta y la convertimos en un JSONObject
			JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

			if (respuesta == null) { // Si hay algún error de parseo (json
										// incorrecto porque hay algún caracter
										// raro, etc.) la respuesta será null
				System.out.println("El json recibido no es correcto. Finaliza la ejecución");
				System.exit(-1);
			} else { // El JSON recibido es correcto
				// Sera "ok" si todo ha ido bien o "error" si hay algún problema
				String estado = (String) respuesta.get("estado");
				// Si ok, obtenemos array de jugadores para recorrer y generar hashmap
				if (estado.equals("ok")) {
					JSONArray array = (JSONArray) respuesta.get("actores");

					if (array.size() > 0) {

						// Declaramos variables
						Actor nuevoActor;
						int id;
						String nombre;
						String apellido;
						String personaje;
						int equipo;

						for (int i = 0; i < array.size(); i++) {
							JSONObject row = (JSONObject) array.get(i);
							id = Integer.parseInt(row.get("id").toString());
							nombre = row.get("nombre").toString();
							apellido = row.get("apellido").toString();
							personaje = row.get("personaje").toString();

							nuevoActor = new Actor();
							nuevoActor.setNombre(nombre);
							nuevoActor.setId(id);
							nuevoActor.setPersonaje(personaje);
							nuevoActor.setApellido(apellido);

							auxhm.put(id, nuevoActor);
						}

						System.out.println("Acceso JSON Remoto - Leidos datos correctamente y generado hashmap");
						System.out.println();

					} else { // El array de jugadores está vacío
						System.out.println("Acceso JSON Remoto - No hay datos que tratar");
						System.out.println();
					}

				} else { // Hemos recibido el json pero en el estado se nos
							// indica que ha habido algún error

					System.out.println("Ha ocurrido un error en la busqueda de datos");
					System.out.println("Error: " + (String) respuesta.get("error"));
					System.out.println("Consulta: " + (String) respuesta.get("query"));

					System.exit(-1);

				}
			}

		} catch (Exception e) {
			System.out.println("Ha ocurrido un error en la busqueda de datos");

			e.printStackTrace();

			System.exit(-1);
		}

		return auxhm;
	}

	@Override
	public void AddOne(Actor actor) throws Exception {
		try {
			JSONObject objActor = new JSONObject();
			JSONObject objPeticion = new JSONObject();

			objActor.put("nombre", actor.getNombre());
			objActor.put("apellido", actor.getApellido());
			objActor.put("personaje", actor.getPersonaje());

			// Tenemos el jugador como objeto JSON. Lo añadimos a una peticion
			// Lo transformamos a string y llamamos al
			// encargado de peticiones para que lo envie al PHP

			objPeticion.put("peticion", "add");
			objPeticion.put("ActorAnnadir", objActor);

			String json = objPeticion.toJSONString();

			System.out.println("Lanzamos peticion JSON para almacenar un jugador");

			String url = SERVER_PATH + SET_PLAYER;

			System.out.println("La url a la que lanzamos la petición es " + url);
			System.out.println("El json que enviamos es: ");
			System.out.println(json);
			// System.exit(-1);

			String response = encargadoPeticiones.postRequest(url, json);

			System.out.println("El json que recibimos es: ");

			System.out.println(response); // Traza para pruebas
			// System.exit(-1);

			// Parseamos la respuesta y la convertimos en un JSONObject

			JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

			if (respuesta == null) { // Si hay algún error de parseo (json
										// incorrecto porque hay algún caracter
										// raro, etc.) la respuesta será null
				System.out.println("El json recibido no es correcto. Finaliza la ejecución");
				System.exit(-1);
			} else { // El JSON recibido es correcto

				// Sera "ok" si todo ha ido bien o "error" si hay algún problema
				String estado = (String) respuesta.get("estado");
				if (estado.equals("ok")) {

					System.out.println("Almacenado jugador enviado por JSON Remoto");

				} else { // Hemos recibido el json pero en el estado se nos
							// indica que ha habido algún error

					System.out.println("Acceso JSON REMOTO - Error al almacenar los datos");
					System.out.println("Error: " + (String) respuesta.get("error"));
					System.out.println("Consulta: " + (String) respuesta.get("query"));

					System.exit(-1);

				}
			}
		} catch (Exception e) {
			System.out.println(
					"Excepcion desconocida. Traza de error comentada en el método 'annadirJugador' de la clase JSON REMOTO");
			// e.printStackTrace();
			System.out.println("Fin ejecución");
			System.exit(-1);
		}

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
		try {
			JSONObject objActor = new JSONObject();
			JSONObject objPeticion = new JSONObject();

			objPeticion.put("id", indice);

			objPeticion.put("peticion", "delete");

			String json = objPeticion.toJSONString();

			// System.out.println("Lanzamos peticion JSON para borrar un actor");

			String url = SERVER_PATH + DELETE_PLAYER;

			// System.out.println("La url a la que lanzamos la petición es " + url);
			// System.out.println("El json que enviamos es: ");
			// System.out.println(json);
			// System.exit(-1);

			String response = encargadoPeticiones.postRequest(url, json);

			// System.out.println("El json que recibimos es: ");

			// System.out.println(response); // Traza para pruebas

			// System.exit(-1);

			// Parseamos la respuesta y la convertimos en un JSONObject

			JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

			if (respuesta == null) { // Si hay algún error de parseo (json
				// incorrecto porque hay algún caracter
				// raro, etc.) la respuesta será null
				System.out.println("El json recibido no es correcto. Finaliza la ejecución");
				System.exit(-1);

			} else { // El JSON recibido es correcto

				// Sera "ok" si todo ha ido bien o "error" si hay algún problema
				String estado = (String) respuesta.get("estado");
				String mensaje = (String) respuesta.get("mensaje");
				if (estado.equals("ok")) {

					// System.out.println("Actor borrado por JSON Remoto");

				} else { // Hemos recibido el json pero en el estado se nos
					// indica que ha habido algún error

					// System.out.println("Acceso JSON REMOTO - Error borrar el actor");
					// System.out.println("Error: " + (String) respuesta.get("error"));
					// System.out.println("Consulta: " + (String) respuesta.get("query"));
					if (mensaje.equals("No existe id"))
						return false;

				}
			}
		} catch (Exception e) {
			System.out.println(
					"Excepcion desconocida. Traza de error comentada en el método 'annadirJugador' de la clase JSON REMOTO");
			// e.printStackTrace();
			System.out.println("Fin ejecución");
			System.exit(-1);
		}
		return true;
	}

	@Override
	public boolean ChangeOne(int indice, Actor actor) throws Exception {
		try {
			JSONObject objActor = new JSONObject();
			JSONObject objPeticion = new JSONObject();
			objActor.put("id", indice);
			objActor.put("nombre", actor.getNombre());
			objActor.put("apellido", actor.getApellido());
			objActor.put("personaje", actor.getPersonaje());

			// Tenemos el jugador como objeto JSON. Lo añadimos a una peticion
			// Lo transformamos a string y llamamos al
			// encargado de peticiones para que lo envie al PHP

			objPeticion.put("peticion", "actualizar");
			objPeticion.put("ActorActualizar", objActor);

			String json = objPeticion.toJSONString();

			System.out.println("Lanzamos peticion JSON para almacenar un jugador");

			String url = SERVER_PATH + UPDATE_PLAYER;

			System.out.println("La url a la que lanzamos la petición es " + url);
			System.out.println("El json que enviamos es: ");
			System.out.println(json);
			// System.exit(-1);

			String response = encargadoPeticiones.postRequest(url, json);

			System.out.println("El json que recibimos es: ");

			System.out.println(response); // Traza para pruebas
			// System.exit(-1);

			// Parseamos la respuesta y la convertimos en un JSONObject

			JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

			if (respuesta == null) { // Si hay algún error de parseo (json
										// incorrecto porque hay algún caracter
										// raro, etc.) la respuesta será null
				System.out.println("El json recibido no es correcto. Finaliza la ejecución");
				System.exit(-1);
			} else { // El JSON recibido es correcto

				// Sera "ok" si todo ha ido bien o "error" si hay algún problema
				String estado = (String) respuesta.get("estado");
				if (estado.equals("ok")) {

					System.out.println("Actualizado actor enviado por JSON Remoto");

				} else { // Hemos recibido el json pero en el estado se nos
							// indica que ha habido algún error

					System.out.println("Acceso JSON REMOTO - Error al almacenar los datos");
					System.out.println("Error: " + (String) respuesta.get("error"));
					System.out.println("Consulta: " + (String) respuesta.get("query"));

					System.exit(-1);

				}
			}
		} catch (Exception e) {
			System.out.println(
					"Excepcion desconocida. Traza de error comentada en el método 'annadirJugador' de la clase JSON REMOTO");
			// e.printStackTrace();
			System.out.println("Fin ejecución");
			System.exit(-1);
		}
		return true;
	}

	@Override
	public Actor findOneById(int indice) throws Exception {
		// TODO Auto-generated method stub

		HashMap<Integer, Actor> auxhm = this.leerAux(String.valueOf(indice));
		if (auxhm.size() == 0) {
			return null;
		} else {
			Actor actor;
			actor = auxhm.get(indice);
			return actor;
		}

	}

	@Override
	public HashMap<Integer, Actor> findBytext(String texto) {
		HashMap<Integer, Actor> auxhm = new HashMap<>();
		try {
			auxhm = this.leerAux(texto);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return auxhm;
	}

	@Override
	public int devolverId(HashMap<Integer, Actor> miHM) {
		// TODO Auto-generated method stub
		return 0;
	}

}
