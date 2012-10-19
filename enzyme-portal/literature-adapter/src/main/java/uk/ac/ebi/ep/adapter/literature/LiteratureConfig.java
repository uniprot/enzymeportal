package uk.ac.ebi.ep.adapter.literature;

public class LiteratureConfig implements LiteratureConfigMBean {

	private int maxThreads = 4;
	
	private int citexploreClientPoolSize = 8;
	
	/* (non-Javadoc)
	 * @see uk.ac.ebi.ep.adapter.literature.LiteratureConfigMBean#getMaxThreads()
	 */
	public int getMaxThreads() {
		return maxThreads;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ebi.ep.adapter.literature.LiteratureConfigMBean#setMaxThreads(int)
	 */
	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}

	public int getCitexploreClientPoolSize() {
		return citexploreClientPoolSize;
	}

	/**
	 * {@inheritDoc}
	 * <br>
	 * <b>Please note that this implementation does not notify of the change
	 * yet. This setting is only used at pool creation time.</b>
	 */
	public void setCitexploreClientPoolSize(int size) {
		this.citexploreClientPoolSize = size;
	}
	
}
