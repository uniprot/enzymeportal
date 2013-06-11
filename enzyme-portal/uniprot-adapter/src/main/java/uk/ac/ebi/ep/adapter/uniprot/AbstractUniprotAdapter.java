package uk.ac.ebi.ep.adapter.uniprot;

import java.sql.Connection;

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

	public void setConfig(UniprotConfig config) {
		this.config = config;
	}

    public void setMmConnection(Connection mmConnection) {
        this.mmConnection = mmConnection;
    }

}
