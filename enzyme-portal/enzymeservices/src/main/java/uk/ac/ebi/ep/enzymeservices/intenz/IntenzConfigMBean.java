package uk.ac.ebi.ep.enzymeservices.intenz;

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

	/**
	 * Sets the base URL for a concrete EC number in the IntEnz website (the
	 * complete URL is built just adding the EC number at the end).
	 * @param ecBaseUrl the base URL.
	 * @since 1.0.6
	 */
    public abstract void setEcBaseUrl(String ecBaseUrl);

    /**
     * @since 1.0.6
     */
    public abstract String getEcBaseUrl();
}
