package uk.ac.ebi.ep.adapter.bioportal;

/**
 * Configuration object for
 * <a href="http://data.bioontology.org/documentation">BioPortal</a> proxies.
 * This bean should be used as a singleton.
 * <br>
 * Configurable properties (via JMX):
 * <ul>
 *  <li><b>apiKey *</b>: the API key <b>required</b> to make requests to the
 *      BioPortalweb service.</li>
 * 	<li><b>searchUrl *</b>: URL used to <a
 *      href="http://data.bioontology.org/documentation#nav_search">search</a>
 * 		BioPortal. It <i>must</i> include the following placeholders to be
 * 		replaced with values for the query parameters:
 * 		<ul>
 * 			<li>{0}: ontology names: a comma-separated list of ontology names
 *              (as enumerated in {@link BioportalOntology})</li>
 * 			<li>{1}: query</li>
 * 		    <li>{2}: api key</li>
 * 		</ul>
 * 		If any of these placeholders are missing, the URL should have them
 * 		hardcoded.<br/>
 * 	</li>
 * 	<li><b>getUrl *</b>: URL used to
 * 		<a href="http://data.bioontology.org/documentation#Class">get</a>
 * 		classes (concepts) from BioPortal. It <i>must</i> include the following
 * 		placeholders to be replaced with values for the query parameters:</li>
 * 		<ul>
 * 			<li>{0}: ontology name: the name of an ontology, as enumerated in
 *              {@link BioportalOntology}.</li>
 * 			<li>{1}: class id: the URI of the searched class (concept).</li>
 * 		    <li>{2}: apiKey</li>
 * 		</ul>
 * 	<li><b>useProxy</b>: (not used) use proxy to connect to BioPortal? Defaults
 * 		to <code>true</code>.</li>
 * 	<li><b>timeout</b>: timeout in milliseconds for the connections to
 * 		BiopPortal.</li>
 * </ul>
 * * Please note that both <code>searchUrl</code> and <code>getUrl</code> should
 * include a parameter <code>apikey</code>, obtained after registering to the
 * service (free).
 * @author rafa
 *
 */
public class BioportalConfig implements BioportalConfigMBean {

	/* Default one, just in case it is not configured somewhere else */
	private String searchUrl =
			"http://data.bioontology.org/search?ontologies={0}&q={1}&apikey={2}&exact_match=true";

	private String getUrl =
			"http://data.bioontology.org/ontologies/{0}/classes/{1}?apikey={2}";

	private String apiKey;

	private boolean useProxy = true;

	private int timeout = 30000;

	public String getSearchUrl() {
		return searchUrl;
	}

	public void setSearchUrl(String searchUrl) {
		this.searchUrl = searchUrl;
	}

	public String getGetUrl() {
		return getUrl;
	}

	public void setGetUrl(String getUrl) {
		this.getUrl = getUrl;
	}

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
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
