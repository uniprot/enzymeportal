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

	public abstract String getIntenzXmlUrl();

	/**
	 * Sets the format string of URLs to retrieve IntEnz entries as IntEnzXML.
	 * It should have the placeholders {0}, {1}, {2} and {3} for ec1, ec2, ec3
	 * and ec4 respectively.
	 * @param intenzXmlUrl
	 */
	public abstract void setIntenzXmlUrl(String intenzXmlUrl);
}
