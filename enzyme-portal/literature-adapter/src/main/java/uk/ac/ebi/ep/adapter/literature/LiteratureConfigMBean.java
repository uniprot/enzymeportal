package uk.ac.ebi.ep.adapter.literature;

public interface LiteratureConfigMBean {

	public abstract int getMaxThreads();

	/**
	 * Sets the maximum number of threads to be used to retrieve citations.
	 * @param maxThreads
	 */
	public abstract void setMaxThreads(int maxThreads);

}