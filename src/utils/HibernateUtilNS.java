package utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtilNS {

	private SessionFactory sessionFactory;
    
    public HibernateUtilNS(){

        try {
            // Create the SessionFactory from standard (hibernate.cfg.xml) config file.
        	sessionFactory = new Configuration().configure().buildSessionFactory();
                     
        } catch (Throwable ex) {
            // Log the exception. 
            System.err.println("Initial SessionFactory creation failed." + ex);
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    	
    }
    
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
