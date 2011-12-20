package uk.ac.ebi.ep.adapter.reactome;

public interface ReactomeConfigMBean {

	public void setUseProxy(boolean useProxy);
	
	public boolean getUseProxy();
	
	public void setTimeout(int msec);
	
	public int getTimeout();

}
