package uk.ac.ebi.ep.adapter.bioportal;

public interface BioportalConfigMBean {

	/**
	 * Sets the Bioportal URL used for searches. It can contain the following
	 * placeholders (see MessageFormat javadocs):
	 * <ul>
	 * 	<li>{0} for the ontology ID (ex. 1136 for EFO)</li>
	 * 	<li>{1} for the query</li>
	 *  <li>{2} set to 1 for exact matches only, 0 otherwise</li>
	 * </ul>
	 * @param efoSearchUrl the Bioportal URL for searches.
	 */
	public void setSearchUrl(String efoSearchUrl);

	public String getSearchUrl();

	/**
	 * Sets the Bioportal URL used to get individual concepts. It can contain
	 * the following placeholders (see MessageFormat javadocs):
	 * <ul>
	 * 	<li>{0} for the ontology version ID (ex. 46765 for EFO 2.20)</li>
	 * 	<li>{1} for the concept ID, including the ontology prefix (ex. 'efo:'
	 * 		for EFO).</li>
	 *  <li>{2} set to 1 for exact matches only, 0 otherwise</li>
	 * </ul>
	 * @return
	 */
	public abstract void setGetUrl(String getUrl);

	public abstract String getGetUrl();

	public abstract void setUseProxy(boolean useProxy);

	public abstract boolean getUseProxy();

	/**
	 * Sets the timeout for connections to BioPortal web services.
	 * @param timeout the timeout in milliseconds.
	 */
	public abstract void setTimeout(int timeout);

	public abstract int getTimeout();
	
}
