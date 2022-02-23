package modelo;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import utils.HibernateUtilNS;

public class HibernateManager implements AccesoADatos {

		Session session;
		
		public HibernateManager() {
			
			Logger.getLogger("org.hibernate").setLevel(Level.OFF);
			
			HibernateUtilNS util = new HibernateUtilNS();
			
			session = util.getSessionFactory().openSession();
			
			
		}

		@Override
		public HashMap<Integer, Actor> leerInfo() throws Exception {
			
			HashMap<Integer, Actor> hmAuxiliar = new HashMap<Integer, Actor>();
			
			TypedQuery<Actor> q= session.createQuery("select a from Actor a");
	        
	    	List results = q.getResultList();
	        
	        Iterator actorIterator= results.iterator();
	        
	        

	        while (actorIterator.hasNext()){
	            
	        	Actor actor = (Actor)actorIterator.next();
	        	
	        	hmAuxiliar.put(actor.getId(), actor);
	            
	        }
			return hmAuxiliar;
		}

		@Override
		public void AddOne(Actor actor) throws Exception {   
	        
	        session.beginTransaction();	        
	        session.save(actor);
	        session.getTransaction().commit();
	       
	    }
			
		

		@Override
		public void AddAll(HashMap<Integer, Actor> miHM) {
				session.clear();
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
			
	        session.beginTransaction();    
	        session.createSQLQuery("truncate table actores").executeUpdate();
	        session.getTransaction().commit();
			
		}

		@Override
		public boolean DeleteOne(int indice) {
			   
	        // Obtener un objeto a traves de su id
	        Actor actor = (Actor) session.get(Actor.class, indice);
	        if (actor==null) {
	        	return false;
	        }else {
	        	session.beginTransaction();
	        	session.delete(actor);
	        	session.getTransaction().commit();
	        }	        
			return true;
		}

		@Override
		public boolean ChangeOne(int indice, Actor nuevoActor) throws Exception {
	        session.beginTransaction();
	        session.update(nuevoActor);
	        session.getTransaction().commit(); 
			return true;
		}

		@Override
		public Actor findOneById(int indice) throws Exception {
			
			Actor a = (Actor) session.get(Actor.class, indice);
			
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

		@Override
		public int devolverId(HashMap<Integer, Actor> miHM) {
			// TODO Auto-generated method stub
			return 0;
		}
		
		
}
