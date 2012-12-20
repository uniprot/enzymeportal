package uk.ac.ebi.ep.adapter.intenz;

/**
 * MBean interface for configuring a intenz proxy.
 * @author rafa
 *
 */
public interface IntenzConfigMBean {

	public int getTimeout();
	
	/**
	 * Sets the timeout of requests to IntEnz.
	 * @param timeout in milliseconds.
	 */
	public void setTimeout(int timeout);
}
