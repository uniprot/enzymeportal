package uk.ac.ebi.ep.adapter.chebi;

import uk.ac.ebi.chebi.webapps.chebiWS.model.SearchCategory;

/**
 * Configuration bean for ChEBI proxies.
 * @author rafa
 *
 */
public interface ChebiConfigMBean {

	/**
	 * Sets the timeout for web services calls.
	 * @param timeout
	 */
	public abstract void setTimeout(int timeout);

	public abstract int getTimeout();

	/**
	 * Sets the number of stars for searches.
	 * @param searchStars 
	 * @see SearchCategory
	 */
	public abstract void setSearchStars(String searchStars);

	public abstract String getSearchStars();

	/**
	 * Sets the maximum number of threads to be used by ona
	 * {@link ChebiAdapter}.
	 * @param maxThreads
	 */
	public abstract void setMaxThreads(int maxThreads);

	public abstract int getMaxThreads();


}
