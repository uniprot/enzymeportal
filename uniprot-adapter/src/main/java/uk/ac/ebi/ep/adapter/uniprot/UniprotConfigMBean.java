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

}
