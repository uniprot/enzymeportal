package uk.ac.ebi.ep.mm.app;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author joseph
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();
    
    private static SessionFactory buildSessionFactory(){
        try {
            // Create the SessionFactory from standard (hibernate.cfg.xml) 
            // config file.
                   // return new Configuration().configure().buildSessionFactory(); 
            return new Configuration().configure("uk/ac/ebi/ep/mm/app/hibernate1.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            // Log the exception. 
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    

}
