package uk.ac.ebi.ep.adapter.uniprot;

/**
 * Intermediate implementation with configuration.
 * @author rafa
 *
 */
public abstract class AbstractUniprotAdapter implements IUniprotAdapter {
	
	protected UniprotConfig config;

    public UniprotConfig getConfig() {
		return config;
	}
    
	public void setConfig(UniprotConfig config) {
		this.config = config;
	}

}
