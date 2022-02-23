package modelo;

import java.util.HashMap;


public interface AccesoADatos {
	public HashMap<Integer,Actor> leerInfo() throws Exception;
	public void AddOne(Actor actor) throws Exception;
	public void AddAll (HashMap<Integer, Actor> miHM);
	public void DeleteAll();
	//SUSANA BBDD
	public boolean DeleteOne(int indice);
	//SUSANA
	public boolean ChangeOne(int indice, Actor actor) throws Exception;
	// el buscar uno devuelvo un hashmap por si hay registros duplicados, lo habia hecho devolviendo un actor.
	public Actor findOneById (int indice) throws Exception;
	public HashMap<Integer, Actor> findBytext(String texto);
	public int devolverId(HashMap<Integer, Actor> miHM);
	
}
