package controlador;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Properties;

import modelo.*;
import vista.*;

public class Controlador {

	private AccesoADatos manager, managerDestino;
	private Vista miVista;
	private HashMap<Integer, Actor> miHM;
	private File fichero;
	private Auxiliar auxiliar;
	

	public Controlador() {
		manager = null;
		managerDestino = null;
		miVista = new Vista(this);
		
	}

	public void ejecucion() throws Exception {
		
		boolean salir = false;
		int opcion;
		opcion = miVista.pintarMenuManager("¿Con qué opción quieres trabajar?",0);
		//opcion = miVista.pintarMenuManager("¿Con qué opción quieres trabajar?",0);
		
		manager = getManager(opcion);
		if (manager==null) {
			miVista.pintarMensaje("Opción no válida o no implementada");
			System.exit(-1);
		}
		
		while (!salir) {
			int opcion2 = miVista.pintarSubMenu();
			switch (opcion2) {
			case 0:
				salir = true;
				System.out.println("Que la fuerza te acompañe");
				break;
			case 1:
				miHM = manager.leerInfo();
				miVista.pintarDatos(miHM);
				break;
			case 2:
				Actor nuevoActor = miVista.pedirDatosActor();
				miHM = manager.leerInfo();
				nuevoActor.setId(manager.devolverId(miHM));
				manager.AddOne(nuevoActor);
				miVista.pintarMensaje("Nuevo actor añadido");
				break;
			case 3:
				boolean salir2 = false;
				while (!salir2) {
					//opcion3 = miVista.pintarMenuManager("A quienb quieress exporrtar",opcion);
					//managerDestino = getMAnager(opcion3);
					int opcion3 = miVista.pintarMenuManager("A que formato quieres exportar: ", opcion);
					managerDestino = getManager(opcion3);
					if (managerDestino==null) {
						miVista.pintarMensaje("Opción no válida o no implementada");
					}else {
						miHM = manager.leerInfo();
						managerDestino.AddAll(miHM);
						salir2 = true;
					}
				}

				break;
			case 4:
				// Borrar uno
				int indice=miVista.pedirIndice();				
					if (!manager.DeleteOne(indice)) {
						miVista.pintarMensaje("No existe ningún registro con ese Id asociado" );
					}else
						miVista.pintarMensaje("Se ha borrado el registro con id: "+indice);
				
				break;
			
			case 5:
				// TODO modificar registro
				indice=miVista.pedirIndice();	
				Actor a = manager.findOneById(indice);
				if (a==null) {
					miVista.pintarMensaje("No existe ningún registro con ese Id asociado");

				}else {
					// Tengo que usar el metodo replace que he creado
					
					a.replace(miVista.pedirDatosActor());
					
					manager.ChangeOne(indice, a);			
					miVista.pintarMensaje("Se ha modificado el actor: "+a.toString());
				}	
				break;
			
			case 6:
				//buscar uno
				indice=miVista.pedirIndice();						
				Actor b = manager.findOneById(indice); 
				if (b == null) {
					miVista.pintarMensaje("No existe ningún registro con ese Id asociado");
				}else {
					System.out.println(b.toString());
					System.out.println();
				}
				break;
			
				
			case 7:
				// buscar plus
				String texto = miVista.pedirTextoABuscar();
				
				miHM = manager.findBytext(texto);
				if (miHM.isEmpty()) miVista.pintarMensaje("La busqueda no arroja ningún resultado");
				miVista.pintarDatos(miHM);
				                
				break;

			case 8:
				boolean salir3 = false;
				while (!salir3) {
					//opcion3 = miVista.pintarMenuManager("A quienb quieress exporrtar",opcion);
					//managerDestino = getMAnager(opcion3);
					int opcion3 = miVista.pintarMenuManager("De que formato quieres importar: ", opcion);
					managerDestino = getManager(opcion3);
					if (managerDestino==null) {
						miVista.pintarMensaje("Opción no válida o no implementada");
					}else {
						miHM = managerDestino.leerInfo();
						manager.AddAll(miHM);
						salir3 = true;
					}
				}
				break;				
			default:
				miVista.pintarMensaje("Opción no válida o no implementada");
				break;
			}
		}

	}

	public AccesoADatos getManager(int opcion) {
		
		AccesoADatos auxiliar = null;
		
		switch (opcion) {
		case 1:
			auxiliar = new FileManager();
			break;
		case 2:
			auxiliar = new XMLManager();
			break;
		case 3:
			auxiliar = new BaseDeDatos();
			break;
		case 4:
			auxiliar = new HibernateManager();
			break;
		case 5:
			auxiliar = new MattiseManager();
			break;
		case 6:
			auxiliar = new MongoManager();
			break;
		case 7:
			auxiliar = new BaseXManager();
			break;
		case 8:
			auxiliar = new JSONManager();
			break;
		/*
		 * default: miVista.pintarMensaje("Opción no válida o no implementada"); break;
		 */
		}
		
		return auxiliar;
		
	}
	
}
