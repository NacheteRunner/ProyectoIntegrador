package modelo;

public class Actor {
	
	    static int id_clase = 1000;
	    
	    private int id;
	    private String nombre;
		private String apellido;
		private String personaje;
		
		
		public Actor() {
			super();
			
		}

		public Actor(String nombre, String apellido, String personaje) {
			super();
			this.nombre = nombre;
			this.apellido = apellido;
			this.personaje = personaje;
			id = id_clase++;
		}

		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		public String getApellido() {
			return apellido;
		}

		public void setApellido(String apellido) {
			this.apellido = apellido;
		}

		public String getPersonaje() {
			return personaje;
		}

		public void setPersonaje(String personaje) {
			this.personaje = personaje;
		}

		

		public void setId(int id) {
			this.id = id;
		}

		public int getId() {
			
			return this.id;
		}
		// Método que me hacia falta en HIBERNATE porque me daba un problema
		// de persistencia de datos, existian 2 objetos en memoria con el mismo
		// id y distintos datos, al pedir el actor reemplazo uno por otro.
		public void replace(Actor nuevo) {
			this.nombre = nuevo.nombre;
			this.apellido = nuevo.apellido;
			this.personaje = nuevo.personaje;	
		}

		@Override
		public String toString() {
			return "Actor [id=" + id + ", nombre=" + nombre + ", apellido=" + apellido + ", personaje=" + personaje
					+ "]";
		}

		
		
		

	}



