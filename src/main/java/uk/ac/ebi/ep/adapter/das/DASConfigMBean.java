package uk.ac.ebi.ep.adapter.das;

public interface DASConfigMBean {

	public abstract int getClientPoolSize();

	/**
	 * Sets the size of the pool of clients used to retrieve data from a DAS
	 * server.
	 * @param dasClientPoolSize the new pool size.
	 */
	/*
	 * This method won't be exposed via JMX until its implementation notifies
	 * the pool.
	public abstract void setClientPoolSize(int dasClientPoolSize);
	 */

}