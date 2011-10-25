package uk.ac.ebi.ep.adapter.uniprot;

public class UniprotConfig implements UniprotConfigMBean {

	private int timeout;
	
	private boolean reviewed;
	
	private boolean useProxy;
	
	private int maxTermsPerQuery;
	
	/**
	 * http://www.uniprot.org/uniprot/?format=tab&sort=score&query={0}&columns={1}
	 */
	private String wsUrl;
	
	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public boolean isReviewed() {
		return reviewed;
	}

	public void setReviewed(boolean reviewed) {
		this.reviewed = reviewed;
	}

	public String getWsUrl() {
		return wsUrl;
	}

	public void setWsUrl(String wsUrl) {
		this.wsUrl = wsUrl;
	}

	public void setUseProxy(boolean useProxy) {
		this.useProxy = useProxy;
	}

	public boolean getUseProxy() {
		return useProxy;
	}

	public void setMaxTermsPerQuery(int max) {
		this.maxTermsPerQuery = max;
	}

	public int getMaxTermsPerQuery() {
		return maxTermsPerQuery;
	}

}
