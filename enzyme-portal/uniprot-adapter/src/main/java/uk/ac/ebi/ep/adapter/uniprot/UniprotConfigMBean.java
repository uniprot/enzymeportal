package uk.ac.ebi.ep.adapter.uniprot;

public interface UniprotConfigMBean {

	/**
	 * @return the timeout in miliseconds for requests to UniProt web service.
	 */
	public int getTimeout();
	
	/**
	 * Sets the timeout for requests to UniProt web service.
	 * @param timeout the timeout in miliseconds
	 */
	public void setTimeout(int timeout);

	/**
	 * @return Are we considering only reviewed entries (Swiss-Prot)?
	 */
	public abstract boolean isReviewed();

	/**
	 * Sets the flag to consider only reviewed entries (Swiss-Prot).
	 * @param reviewed consider only reviewed entries (Swiss-Prot)?
	 */
	public abstract void setReviewed(boolean reviewed);

	public abstract void setWsUrl(String wsUrl);

	public abstract String getWsUrl();
	
	/**
	 * Sets the use of a proxy to get web service requests.
	 * @param useProxy Use a proxy?
	 */
	public abstract void setUseProxy(boolean useProxy);
	
	public abstract boolean getUseProxy();
	
	/**
	 * Sets the maximum number of terms (accessions, IDs) that can be used
	 * in a single query as OR'ed terms.
	 * @param max the maximum number of terms per query.
	 */
	public abstract void setMaxTermsPerQuery(int max);
	
	public abstract int getMaxTermsPerQuery();

	/**
	 * Sets the maximum number of threads to run concurrently in order to
	 * retrieve data from UniProt.
	 * @param max
	 */
	public abstract void setMaxThreads(int max);
	
	public abstract int getMaxThreads();
}
