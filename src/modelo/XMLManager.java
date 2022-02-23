package modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.SAXException;

public class XMLManager implements AccesoADatos {

	private File archivo;

	public XMLManager() {
		// archivo = new File("Ficheros/actores.xml");
		try (InputStream input = new FileInputStream("config.properties")) {

			Properties prop = new Properties();

			// load a properties file
			prop.load(input);

			// get the property value and print it out
			String ruta = prop.getProperty("xml.url");

			archivo = new File(ruta);

		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public HashMap<Integer, Actor> leerInfo() throws Exception {
		// TODO Auto-generated method stub
		HashMap<Integer, Actor> hmAuxiliar = new HashMap<Integer, Actor>();

		try {

			SAXBuilder saxBuilder = new SAXBuilder();
			Document document = saxBuilder.build(archivo);
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
	public void AddOne(Actor actorInsertado) throws Exception {
		// TODO Auto-generated method stub
		try {

			SAXBuilder saxBuilder = new SAXBuilder();
			Document document = saxBuilder.build(archivo);
			Element raiz = document.getRootElement();
			/*
			 * Creamos nodo actor y sus hijos (nombre, apellido y personaje) Como son
			 * elementos simples añadimos nodo de texto con valor
			 */

			Element actor = new Element("Actor");
			raiz.addContent(actor);
			Element nombre = new Element("Nombre");
			nombre.setText(actorInsertado.getNombre());
			Element apellido = new Element("Apellido");
			apellido.setText(actorInsertado.getApellido());
			Element personaje = new Element("Personaje");
			personaje.setText(actorInsertado.getPersonaje());

			/*
			 * Añadimos hijos al elemento empleado
			 */

			actor.addContent(nombre);
			actor.addContent(apellido);
			actor.addContent(personaje);

			/*
			 * Creamos y añadimos atributo
			 */

			Attribute attr = new Attribute("id", Integer.toString(actorInsertado.getId()));
			actor.setAttribute(attr);

			/*
			 * Volvemos a escribir el fichero
			 */

			Format f = Format.getPrettyFormat(); // Formato de visualización perfecto de xml
			// Format f = Format.getCompactFormat (); // El formato de visualización
			// compacto de xml
			f.setEncoding("gbk");
			f.setOmitDeclaration(false);

			// Genera un archivo xml mediante transmisión; escribe un árbol DOM desde la
			// memoria al disco duro.
			XMLOutputter xmlOut = new XMLOutputter(f);
			xmlOut.output(raiz, new FileOutputStream(archivo));

		} catch (Exception e) {
			System.out.println(e);
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
	public void DeleteAll() {
		try {
			Element raiz = new Element("StarWars");
			Document doc = new Document(raiz);
			XMLOutputter xml = new XMLOutputter();
			xml.setFormat(Format.getPrettyFormat());
			xml.output(doc, new FileWriter(archivo));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean DeleteOne(int indice) {
		HashMap<Integer, Actor> miHM = new HashMap<Integer, Actor>();

		try {
			miHM = this.leerInfo();
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
		int indiceMax = 1;
		for (Map.Entry<Integer, Actor> entry : miHM.entrySet()) {
			if (entry.getKey() > indiceMax) {
				indiceMax = entry.getKey();
			}
		}
		return indiceMax + 1;
	}

}
