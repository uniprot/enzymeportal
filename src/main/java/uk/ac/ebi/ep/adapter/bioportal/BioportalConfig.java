package uk.ac.ebi.ep.adapter.bioportal;

public class BioportalConfig implements BioportalConfigMBean {

	/* Default one, just in case it is not configured somewhere else */
	private String searchUrl =
			"http://rest.bioontology.org/bioportal/search/?ontologyids={0}&query={1}&isexactmatch={2}&apikey=9f19fdf6-82d0-4335-97a1-f71d3ce156f6";
	
	private String getUrl =
			"http://rest.bioontology.org/bioportal/concepts/{0}?conceptid={1}&light=1&apikey=9f19fdf6-82d0-4335-97a1-f71d3ce156f6";

	private boolean useProxy = false;

	private int timeout = 30000;

	public String getSearchUrl() {
		return searchUrl;
	}

	public void setSearchUrl(String efoSearchUrl) {
		this.searchUrl = efoSearchUrl;
	}

	public String getGetUrl() {
		return getUrl;
	}

	public void setGetUrl(String getUrl) {
		this.getUrl = getUrl;
	}

	public boolean getUseProxy() {
		return useProxy;
	}

	public void setUseProxy(boolean useProxy) {
		this.useProxy = useProxy;
	}
	
	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}
