/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.core.utility;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author joseph
 */
public final class MegaMapperJdbcConnection  {
    
    private static final String dataSourceName = "jdbc/ep/mm";
    public static final MegaMapperJdbcConnection INSTANCE = new MegaMapperJdbcConnection();
    private Context context;
    
    private Connection connection = null;

    private MegaMapperJdbcConnection()  {
        try {
            // Obtain our environment naming context
            Context initContext = new InitialContext();
             context = (Context) initContext.lookup("java:/comp/env");
        } catch (NamingException ex) {
            Logger.getLogger(MegaMapperJdbcConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Connection getConnection(String dsName) throws NamingException, SQLException{
        // Look up our data source
        DataSource dataSource = (DataSource)context.lookup(dsName);
        // Allocate and use a connection from the pool
        Connection conn =  dataSource.getConnection();
        if(conn != null){
            System.out.println(" Connected !!!!");
        }
        return conn;
    }

    public Connection getConnection() {
        if(connection == null){
            try {
                connection = getConnection(dataSourceName);
            } catch (NamingException ex) {
                Logger.getLogger(MegaMapperJdbcConnection.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(MegaMapperJdbcConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return connection;
    }

    public static MegaMapperJdbcConnection getINSTANCE() {
        return INSTANCE;
    }
    
    
    	

    
}
