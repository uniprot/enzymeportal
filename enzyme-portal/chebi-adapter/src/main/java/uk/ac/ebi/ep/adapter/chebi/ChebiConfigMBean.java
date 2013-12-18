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

	/**
	 * Sets the maximum number of molecules retrieved for a group (i.e. drugs,
	 * activators, inhibitors, bioactive compounds).
	 * @param maxRetrievedMolecules
	 */
	public abstract void setMaxRetrievedMolecules(int maxRetrievedMolecules);

	public abstract int getMaxRetrievedMolecules();

	/**
	 * Sets the base public URL of a ChEBI entity (the complete URL is built
	 * just adding the CHEBI ID at the end).
	 * @param compoundBaseUrl the base URL
	 * @since 1.0.4
	 */
    public abstract void setCompoundBaseUrl(String compoundBaseUrl);

    /**
     * @since 1.0.4
     */
    public abstract String getCompoundBaseUrl();

    /**
     * Sets the base URL for a compound image (the complete URL is built just
     * adding the CHEBI ID at the end).
     * @param compoundImgBaseUrl the base URL.
     * @since 1.0.4
     */
    public abstract void setCompoundImgBaseUrl(String compoundImgBaseUrl);

    /**
     * @since 1.0.4
     */
    public abstract String getCompoundImgBaseUrl();


}
