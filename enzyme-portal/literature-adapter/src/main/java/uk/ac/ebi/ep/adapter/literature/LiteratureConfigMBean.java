package uk.ac.ebi.ep.adapter.literature;

public interface LiteratureConfigMBean {

	public abstract int getMaxThreads();

	/**
	 * Sets the maximum number of threads to be used to retrieve citations.
	 * @param maxThreads
	 */
	public abstract void setMaxThreads(int maxThreads);

	public abstract int getCitexploreClientPoolSize();
	
	/**
	 * Sets the size of the pool with CiteXplore clients.
	 * @param size
	 */
	/* This method won't be exposed via JMX until its implementation notifies
	 * the pool.
	public abstract void setCitexploreClientPoolSize(int size);
	*/

}