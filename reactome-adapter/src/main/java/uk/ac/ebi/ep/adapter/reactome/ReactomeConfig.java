package uk.ac.ebi.ep.adapter.reactome;

public class ReactomeConfig implements ReactomeConfigMBean {

	/**
	 * Use a proxy for requests to Reactome?
	 */
	private boolean useProxy;
	
	/**
	 * Timeout for requests to Reactome, in milliseconds.
	 */
	private int timeout;
	
	public void setUseProxy(boolean useProxy) {
		this.useProxy = useProxy;
	}

	public boolean getUseProxy() {
		return useProxy;
	}

	public void setTimeout(int msec) {
		this.timeout = msec;
	}

	public int getTimeout() {
		return timeout;
	}

}
