package uk.ac.ebi.ep.adapter.uniprot;

public interface UniprotConfigMBean {

	/**
	 * @return the timeout in seconds for requests to UniProt.
	 */
	public int getTimeout();
	
	/**
	 * Sets the timeout for requests to UniProt.
	 * @param timeout the timeout in seconds
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

}
