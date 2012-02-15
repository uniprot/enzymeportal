/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.core.search;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import uk.ac.ebi.ep.mm.MegaJdbcMapper;
import uk.ac.ebi.ep.mm.MegaMapper;

/**
 *This class creates the JDBC connection required by the megaMapper
 * @author joseph
 */
public final class MegaMapperConnection {

    private static final Logger LOGGER = Logger.getLogger(MegaMapperConnection.class);
    // public static final MegaMapperConnection INSTANCE = new MegaMapperConnection();
    private Context context;
      /** The connection used by the mega-mapper. */
    private Connection connection = null;
    private String mmDatasource;
    /** A mega-mapper to get xrefs from. */
    private MegaMapper megaMapper = null;

    public MegaMapperConnection(final String mmDatasource) {
        this.mmDatasource = mmDatasource;
        try {
            // Obtain our environment naming context
            Context initContext = new InitialContext();
            context = (Context) initContext.lookup("java:/comp/env");
        } catch (NamingException ex) {
            LOGGER.error("Required environment naming context not found: " + ex);
        }
    }

    private Connection getConnection(String dsName) throws NamingException, SQLException {
        // Look up our data source
        DataSource dataSource = (DataSource) context.lookup(dsName);
        // Allocate and use a connection from the pool
        Connection conn = dataSource.getConnection();
        return conn;
    }

    /**
     * 
     * @return the created connection
     */
    public final Connection getConnection() {
        if (connection == null) {
            try {
                connection = getConnection(mmDatasource);
            } catch (NamingException ex) {
                LOGGER.error("Data source not found: " + mmDatasource, ex);
            } catch (SQLException ex) {
                LOGGER.error("Unable to establish connection to " + mmDatasource, ex);
            }
        }

        return connection;
    }

    /**
     * 
     * @return an instance of the MegaMapper
     */
    public final MegaMapper getMegaMapper() {
        final Connection conn = this.getConnection();
        try {
            if (megaMapper == null) {
                megaMapper = new MegaJdbcMapper(conn);
            }
        } catch (IOException ex) {
            LOGGER.error("MegaMapper could not be created " + ex);
        }
        return megaMapper;
    }

    /**
     * close all resources used. megaMapper & JDBC connection
     */
    public void closeMegaMapperConnection() {
        if (megaMapper != null) {
            try {
                megaMapper.closeMap();
            } catch (IOException e) {
                LOGGER.error("Unable to close mega-mapper", e);
            }
        }
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                LOGGER.error("Unable to close connection", e);
            }
        }
    }

//    @Override
//    public void finalize() throws Throwable {
//        super.finalize();
//        closeMegaMapperConnection();
//        
//
//    }
}
