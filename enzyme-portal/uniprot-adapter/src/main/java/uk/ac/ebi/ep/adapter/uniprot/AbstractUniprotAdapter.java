package uk.ac.ebi.ep.adapter.uniprot;

import java.sql.Connection;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

/**
 * Intermediate implementation with configuration.
 * @author rafa
 *
 */
public abstract class AbstractUniprotAdapter implements IUniprotAdapter {

	private static final Logger LOGGER =
	        Logger.getLogger(AbstractUniprotAdapter.class);

	protected UniprotConfig config;

    /**
     * A database connection to the mega-map.
     */
	protected Connection mmConnection;

    public UniprotConfig getConfig() {
		return config;
	}
    
	public void setConfig(UniprotConfig config) {
		this.config = config;
	}

    public void setMmDatasource(String mmDatasource) {
        try {
            // Get the JNDI context where we are getting mm connections from:
            Context jndiContext = (Context)
                    new InitialContext().lookup("java:/comp/env");
            DataSource mmDs = (DataSource) jndiContext.lookup(mmDatasource);
            mmConnection = mmDs.getConnection();
        } catch (Exception e) {
            LOGGER.error("Unable to set mm data source", e);
        }
    }

    /**
     * Gets the connection to the mega-map (there is only one per instance of
     * this class).
     * @return a database connection to the mega-map, or <code>null</code> if
     *      there was any problem with the JNDI context or the database, or if
     *      the datasource is not configured.
     */
    protected Connection getMmConnection(){
        return mmConnection;
    }

}
