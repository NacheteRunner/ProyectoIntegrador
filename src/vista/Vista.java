package vista;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import controlador.Controlador;
import modelo.Actor;

public class Vista {
	
	Controlador miControlador;
	Auxiliar auxiliar;
	Scanner entrada;
	
	public Vista(Controlador controlador) {
		this.entrada = new Scanner(System.in);
		this.miControlador = controlador;
		this.auxiliar = new Auxiliar(entrada);
	}


	public int pintarMenuManager(String texto, int actual) {
		
		int opcion = 0;
		System.out.println();
		System.out.println(texto);
		//ifs
		if (actual!=1) {
			System.out.println("1. TXT");
		}
		if (actual!=2) {
			System.out.println("2. XML");
		}
		if (actual!=3) {
			System.out.println("3. BBDD");
		}
		if (actual!=4) {
			System.out.println("4. HIBERNATE");
		}
		if (actual!=5) {
			System.out.println("5. MATTISE");
		}
		if (actual!=6) {
			System.out.println("6. MONGO");
		}
		if (actual!=7) {
			System.out.println("7. BASE_X");
		}
		if (actual!=8) {
			System.out.println("8. JSON/PHP");
		}
		
		opcion = auxiliar.leerEntero("Selecciona una opción");
		return opcion;
	}

	public int pintarMenu() {
		// TODO Auto-generated method stub
		int opcion = 0;
		System.out.println();
		System.out.println("¿Con qué opción quieres trabajar?");
		System.out.println("1. TXT"); 
		System.out.println("2. XML"); 
		System.out.println("3. BD"); 
		System.out.println("4. HIBERNATE");
		System.out.println("5. MATTISE");
		System.out.println("6. MONGO");
		System.out.println("7. BASE_X");
		System.out.println("8. JSON/PHP");

		
		opcion = auxiliar.leerEntero("Selecciona una opción: ");
		return opcion;
	}
	
	public int pintarSubMenu() {
		int opcion = 0;
		System.out.println("¿Qué quieres hacer?");
		System.out.println("0. Salir");
		System.out.println("1. Leer todos los registros");
		System.out.println("2. Insertar registro");
		System.out.println("3. Exportar");
		System.out.println("4. Borrar registro");
		System.out.println("5. Modificar registro");
		System.out.println("6. Buscar un registro");
		System.out.println("7. Busqueda por texto");
		System.out.println("8. Importar");
		System.out.println();
		opcion = auxiliar.leerEntero("Selecciona una opción");
		System.out.println();
		
		return opcion;
	}
	
	public int pintarSubMenu2() {
		int opcion =0;
		System.out.println("A que formato quieres exportar: ");
		System.out.println("0. Salir");
		System.out.println("1. A XML");
		System.out.println("2. A TXT");
		System.out.println("3. A Base De Datos");
		System.out.println("4. Hibernate");
		System.out.println("5. Matisse");
		System.out.println("6. Mongo");
		System.out.println("7. Base_X");
		System.out.println("7. JSON/PHP");

		opcion = auxiliar.leerEntero("Selecciona una opción");
		return opcion;
	}
	
	public void pintarDatos(HashMap<Integer, Actor> hmPintar) {
		for (Map.Entry<Integer, Actor> entry : hmPintar.entrySet()) {
			System.out.println("-----------------");
		    System.out.println(entry.getValue());
		    System.out.println("-----------------");
		}
		System.out.println();
	}
	
	public void pintarMensaje(String mensaje) {
		System.out.println(mensaje);
		System.out.println();
	}
	
	public Actor pedirDatosActor() {
		Actor actor = new Actor("", "", "");
		
		System.out.println("Escribe el nombre");
		String nombre = entrada.nextLine();
		System.out.println("Escribe el apellido");
		String apellido = entrada.nextLine();
		System.out.println("Escribe el personaje");
		String personaje = entrada.nextLine(); 
		
		// Lo mejor sería controlarlo dentro del modelo. Por ahora se queda así
		actor.setNombre(nombre);
		actor.setApellido(apellido);
		actor.setPersonaje(personaje);
		return actor;
		
	}
	
	public int pedirIndice() {
		System.out.println("Introduce el indice del Actor a modificar/borrar/buscar");
		int indice = -1;
		try {
			indice=entrada.nextInt();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		entrada.nextLine();
		return indice;
	}
	
	public String pedirTextoABuscar() {
		System.out.println("Introduce la palabra a buscar");
		String texto = entrada.next();
		entrada.nextLine();
		return texto;
	}


	

}
