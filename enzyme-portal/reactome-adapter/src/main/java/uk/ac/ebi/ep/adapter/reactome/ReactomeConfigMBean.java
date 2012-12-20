package uk.ac.ebi.ep.adapter.reactome;

public interface ReactomeConfigMBean {

	public void setUseProxy(boolean useProxy);
	
	public boolean getUseProxy();
	
	public void setTimeout(int msec);
	
	public int getTimeout();
	
	/**
	 * Sets the base URL (excluding method name) for Reactome RESTful web
	 * services.
	 * @param wsBaseUrl
	 */
	public void setWsBaseUrl(String wsBaseUrl);
	
	public String getWsBaseUrl();

}
