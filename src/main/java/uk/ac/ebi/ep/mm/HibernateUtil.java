package uk.ac.ebi.ep.mm;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    public static SessionFactory getSessionFactory() {
        // Create the SessionFactory from hibernate.cfg.xml
        return getSessionFactory(null);
    }

    public static SessionFactory getSessionFactory(String cfgFile){
        try {
	    	return cfgFile == null?
	    			new Configuration().configure().buildSessionFactory():
	    			new Configuration().configure(cfgFile).buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
}
