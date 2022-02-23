package modelo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.DropDB;
import org.basex.core.cmd.InfoDB;
import org.basex.core.cmd.Open;
import org.basex.core.cmd.XQuery;
import org.basex.io.serial.Serializer;
import org.basex.query.QueryException;
import org.basex.query.QueryProcessor;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.item.Item;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

public class BaseXManager implements AccesoADatos {

	Context context;
	private final String nombreDB = "BD_Actores_XML";
	private final String ruta = "Ficheros/actores.xml";

	public BaseXManager() {

		// Database context.
		context = new Context();

		try {
			abrirDB(nombreDB);
		} catch (BaseXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void cerrarContext() {

		// Close the database context
		context.close();

	}

	public void crearDB(String nombre, String rutaFicheroXML) throws BaseXException {

		// Habría que añadir algo para ver si está ya creada y no crearla

		System.out.println("=== Creando BD ===");

		new CreateDB(nombre, rutaFicheroXML).execute(context);

	}

	public void abrirDB(String nombre) throws BaseXException {

		System.out.println("=== Abrir BD ===");

		new Open(nombre).execute(context);

	}

	public void infoDB() throws BaseXException {

		System.out.println("\n* Información de la DB abierta:");

		System.out.print(new InfoDB().execute(context));

	}

	public void borrarDB(String nombre) throws BaseXException {

		new DropDB(nombre).execute(context);

	}

	private void query(final String query) throws BaseXException {
		new XQuery(query).execute(context);

	}

	@Override
	public HashMap<Integer, Actor> leerInfo() throws Exception {
		HashMap<Integer, Actor> hmAuxiliar = new HashMap<Integer, Actor>();

		// String query ="for $actor in //Actor return data($actor)";
		String query = "/StarWars";

		String datosConsulta = new XQuery(query).execute(context);

		// System.out.println(datosConsulta);

		InputStream fichero = new ByteArrayInputStream(datosConsulta.getBytes());

		try {
			SAXBuilder saxBuilder = new SAXBuilder();
			Document document = saxBuilder.build(fichero);
			Element classElement = document.getRootElement();

			List<Element> actorList = classElement.getChildren();

			for (int temp = 0; temp < actorList.size(); temp++) {
				Element actor = actorList.get(temp);

				Actor objetoActor = new Actor();

				int idActor = Integer.parseInt(actor.getAttributeValue("id"));

				objetoActor.setNombre(actor.getChild("Nombre").getText());
				objetoActor.setApellido(actor.getChild("Apellido").getText());
				objetoActor.setPersonaje(actor.getChild("Personaje").getText());
				objetoActor.setId(idActor);

				hmAuxiliar.put(idActor, objetoActor);

			}

		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		return hmAuxiliar;
	}

	@Override
	public void AddOne(Actor actorInsertado) throws BaseXException {

		Element actor = new Element("Actor");

		Attribute attr = new Attribute("id", Integer.toString(actorInsertado.getId()));
		actor.setAttribute(attr);
		Element nombre = new Element("Nombre");
		nombre.setText(actorInsertado.getNombre());
		Element apellido = new Element("Apellido");
		apellido.setText(actorInsertado.getApellido());
		Element personaje = new Element("Personaje");
		personaje.setText(actorInsertado.getPersonaje());

		actor.addContent(nombre);
		actor.addContent(apellido);
		actor.addContent(personaje);

		// Formateamos como string y lo añadimos a la query de inserción
		XMLOutputter xmlOut = new XMLOutputter();
		String formateado = xmlOut.outputString(actor);

		String queryInsert = "insert node " + formateado + " into /StarWars ";

		// Ejecutamos la query (IMPORTANTE: la base de datos tiene que estar cerrada en
		// BaseX, porque se bloquea)

		query(queryInsert);

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

		boolean borrado = false;
		try {
			if (findOneById(indice)==null) {
				borrado=false;
			}else
				borrado=true;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			Element actor = new Element("Actor");

			Attribute attr = new Attribute("id", Integer.toString(indice));

			String queryDelete = "delete node //Actor[@id=" + indice + "]";

			String datosConsulta = new XQuery(queryDelete).execute(context);

			System.out.println("DATOS DEVUELTOS" + datosConsulta);

			// Ejecutamos la query (IMPORTANTE: la base de datos tiene que estar cerrada en
			// BaseX, porque se bloquea)
			borrado = true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return borrado;
	}

	@Override
	public boolean ChangeOne(int indice, Actor actorModificar) throws Exception {
		
		Element actor = new Element("Actor");

		Attribute attr = new Attribute("id", String.valueOf(indice));
		actor.setAttribute(attr);
		Element nombre = new Element("Nombre");
		nombre.setText(actorModificar.getNombre());
		Element apellido = new Element("Apellido");
		apellido.setText(actorModificar.getApellido());
		Element personaje = new Element("Personaje");
		personaje.setText(actorModificar.getPersonaje());

		actor.addContent(nombre);
		actor.addContent(apellido);
		actor.addContent(personaje);

		// Formateamos como string y lo añadimos a la query de inserción
		XMLOutputter xmlOut = new XMLOutputter();
		String formateado = xmlOut.outputString(actor);

		String queryInsert = "insert node " + formateado + " into /StarWars ";

		// Ejecutamos la query (IMPORTANTE: la base de datos tiene que estar cerrada en
		// BaseX, porque se bloquea)

		query(queryInsert);
		return false;
	}

	@Override
	public Actor findOneById(int indice) throws Exception {
		
		Actor objetoActor = null;
		String query = "//Actor[@id="+indice+"]";
		
		String datosConsulta = new XQuery(query).execute(context);
		
		if (datosConsulta.length()!=0) {
		
			InputStream fichero = new ByteArrayInputStream(datosConsulta.getBytes());
		
		
			objetoActor = new Actor();
		
			try {
				SAXBuilder saxBuilder = new SAXBuilder();
				Document document = saxBuilder.build(fichero);
				Element eActor = document.getRootElement();
				int idActor = indice;

				objetoActor.setNombre(eActor.getChild("Nombre").getText());
				objetoActor.setApellido(eActor.getChild("Apellido").getText());
				objetoActor.setPersonaje(eActor.getChild("Personaje").getText());
				objetoActor.setId(idActor);

				

			
			} catch (Exception e) {
				System.out.println(e);
			 e.printStackTrace();
			}
		}
		
		return objetoActor;
	}

	@Override
	public HashMap<Integer, Actor> findBytext(String texto) {
		HashMap<Integer, Actor> hmAuxiliar = new HashMap<Integer, Actor>();
		//Funcionaria el metodo usado en el XML Manager, intentar hacerlo con BaseX
		String miTexto = "%"+texto+"%";
		String [] patron = {"nombre", "apellido", "personaje"};
		
		//String queryNombre = "<respuesta>{//Actor[nombre="+miTexto+"]}</respuesta>";
		//String queryApellido = "<respuesta>{//Actor[apellido="+miTexto+"]}</respuesta>";
		//String queryPersonaje = "<respuesta>{//Actor[personaje="+miTexto+"]}</respuesta>";
		try {
			for (int i=0;i<3;i++) {
				String query = "<respuesta>{//Actor["+patron[i]+"="+miTexto+"]}</respuesta>";
				//String datosConsultaNom = new XQuery(queryNombre).execute(context);
				//String datosConsultaApe = new XQuery(queryApellido).execute(context);
				String datosConsulta=  new XQuery(query).execute(context);
				InputStream fichero = new ByteArrayInputStream(datosConsulta.getBytes());
				SAXBuilder saxBuilder = new SAXBuilder();
				Document document = saxBuilder.build(fichero);
				Element classElement = document.getRootElement();

				List<Element> actorList = classElement.getChildren();

				for (int temp = 0; temp < actorList.size(); temp++) {
					Element actor = actorList.get(temp);

					Actor objetoActor = new Actor();

					int idActor = Integer.parseInt(actor.getAttributeValue("id"));

					objetoActor.setNombre(actor.getChild("Nombre").getText());
					objetoActor.setApellido(actor.getChild("Apellido").getText());
					objetoActor.setPersonaje(actor.getChild("Personaje").getText());
					objetoActor.setId(idActor);

					hmAuxiliar.put(idActor, objetoActor);

				}
			}	
		} catch (BaseXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return hmAuxiliar;	
		}
	

	@Override
	public int devolverId(HashMap<Integer, Actor> miHM) {
		// TODO Auto-generated method stub
		int indiceMax = 1;
		for (Map.Entry<Integer, Actor> entry : miHM.entrySet()) {
			if (entry.getKey() > indiceMax) {
				indiceMax = entry.getKey();
			}
		}
		return indiceMax + 1;
	}
}
