package vista;



import java.util.Scanner;



public class Auxiliar {

	Scanner entrada;

	public Auxiliar(Scanner entrada) {
		
		this.entrada = entrada;
	}

	public int leerEntero(String peticion) {
		
		int varOpcion = 0;
		boolean salir = false;
		while (!salir) {
			try {
				System.out.println(peticion);
				varOpcion = Integer.parseInt(entrada.nextLine());
				salir = true;
			} catch (NumberFormatException e) {
				
				System.out.println("No es un número entero");
			}
		}

		return varOpcion;
	}
	
	
	


}
