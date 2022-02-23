package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.bson.Document;
import org.bson.codecs.PatternCodec;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MongoManager implements AccesoADatos{
	
	MongoCollection miColeccion;
	
	public MongoManager() {
		
		MongoClient mongoClient = new MongoClient("localhost", 27017);

		MongoDatabase database = mongoClient.getDatabase("prueba");

		miColeccion = database.getCollection("actores");
	}

	@Override
	public HashMap<Integer, Actor> leerInfo() throws Exception {
		HashMap<Integer, Actor> miHM = new HashMap<>();
		
		MongoCursor resultado = miColeccion.find().iterator();

		System.out.println("\n VER DATOS \n");		
		
		while (resultado.hasNext()) {

			Document doc = (Document) resultado.next();
			Actor actor = new Actor();
			actor.setId(doc.getInteger("id"));
			actor.setNombre(doc.getString("nombre"));
			actor.setApellido(doc.getString("apellidos"));
			actor.setPersonaje(doc.getString("personaje"));
			miHM.put(actor.getId(), actor);
			

		}
		return miHM;
	}

	@Override
	public void AddOne(Actor actor) throws Exception {
		Document nuevo = new Document();
		
		nuevo.put("id", actor.getId());
		nuevo.put("nombre", actor.getNombre());
		nuevo.put("apellidos", actor.getApellido());
		nuevo.put("personaje", actor.getPersonaje());

		miColeccion.insertOne(nuevo);
		
	}

	@Override
	public void AddAll(HashMap<Integer, Actor> miHM) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DeleteAll() {
		
		
	}

	@Override
	public boolean DeleteOne(int indice) {
		
		Document searchQuery = new Document();
		searchQuery.put("id", indice);
		MongoCursor resultado = miColeccion.find(searchQuery).iterator();

		while (resultado.hasNext()) { // Si resultado me devuelve algo lo borro, si no no existe.

			Document doc = (Document) resultado.next();
			miColeccion.deleteOne(doc);
			return true;
			
		}	
		return false;
		
	}

	@Override
	public boolean ChangeOne(int indice, Actor actor) throws Exception {
		
		Document searchQuery = new Document();
		searchQuery.put("id", indice);
		
		Document nuevo = new Document();
		nuevo.put("id", actor.getId());
		nuevo.put("nombre", actor.getNombre());
		nuevo.put("apellidos", actor.getApellido());
		nuevo.put("personaje", actor.getPersonaje());
		// No me funcionaba con updateOne, se hace asi cuando quieres modificar algun campo
		// Aqui como estamos cambiando el objeto entero lo hago con replaceOne
		// Document set = new Document();
		
		//set.put("$set", nuevo);

		// miColeccion.updateOne(searchQuery, set);
		
		miColeccion.replaceOne(searchQuery, nuevo);
		
		return true;
	}

	@Override
	public Actor findOneById(int indice) throws Exception {
		
		Document searchQuery = new Document();
		searchQuery.put("id", indice);
		Actor a = null;
		MongoCursor resultado = miColeccion.find(searchQuery).iterator();

		while (resultado.hasNext()) {
			// Se supone que solo voy a recibir un actor
			a = new Actor();
			Document doc = (Document) resultado.next();
			a.setId(indice);
			a.setNombre(doc.getString("nombre"));
			a.setApellido(doc.getString("apellidos"));
			a.setPersonaje(doc.getString("personaje"));
			
			

		}
		return a;
	}

	@Override
	public HashMap<Integer, Actor> findBytext(String texto) {
		Pattern pt = Pattern.compile(texto);
		HashMap<Integer, Actor> miHM = new HashMap<Integer, Actor>();
		Actor a;
		Document busca1 = new Document();
		busca1.put("nombre", pt.CASE_INSENSITIVE);
		Document busca2 = new Document();
		busca2.put("apellidos", pt.CASE_INSENSITIVE);
		Document busca3 = new Document();
		busca3.put("personaje", pt.CASE_INSENSITIVE);
		// Creamos una lista para la busqueda y añadimos los 3 documentos de la busqueda
		BasicDBList or = new BasicDBList();
		
		or.add(busca1);
		or.add(busca2);
		or.add(busca3);
		
		Document query = new Document ("$or", or);
		
		MongoCursor resultado = miColeccion.find(query).iterator();
		
		while(resultado.hasNext()) {
			
			a = new Actor();
			Document doc = (Document) resultado.next();		
			a.setId(doc.getInteger("id"));
			a.setNombre(doc.getString("nombre"));
			a.setApellido(doc.getString("apellidos"));
			a.setPersonaje(doc.getString("personaje"));
			miHM.put(a.getId(), a);
		}
		
		return miHM;
	}

	@Override
	public int devolverId(HashMap<Integer, Actor> miHM) {
		int indiceMax = 1;
		for (Map.Entry<Integer, Actor> entry : miHM.entrySet()) {
			if (entry.getKey() > indiceMax) {
				indiceMax = entry.getKey();
			}
		}
		return indiceMax + 1;
	}

}
