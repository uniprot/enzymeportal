package uk.ac.ebi.ep.adapter.bioportal;

import uk.ac.ebi.ep.adapter.bioportal.BioportalWsAdapter.BioportalOntology;

/**
 * Configuration object for
 * <a href="http://www.bioontology.org/wiki/index.php/NCBO_REST_services">BioPortal</a>
 * proxies. This bean should be used as a singleton.
 * <br>
 * Configurable properties (via JMX):
 * <ul>
 * 	<li><b>searchUrl *</b>: URL used to
 * 		<a href="http://rest.bioontology.org/bioportal/search">search</a>
 * 		BioPortal. It can include the following placeholders to be replaced with
 * 		values for the query parameters:
 * 		<ul>
 * 			<li>{0}: ontologyids (see {@link BioportalOntology#getId()})</li>
 * 			<li>{1}: query</li>
 * 			<li>{2}: isexactmatch (<code>1</code> for true, <code>0</code> for
 * 				false)</li>
 * 		</ul>
 * 		If any of these placeholders are missing, the URL should have them
 * 		hardcoded.
 * 	</li>
 * 	<li><b>getUrl *</b>: URL used to
 * 		<a href="http://rest.bioontology.org/bioportal/concepts">get</a>
 * 		concepts from BioPortal. It <i>must</i> include the following
 * 		placeholders to be replaced with values for the query parameters:</li>
 * 		<ul>
 * 			<li>{0}: ontologyversionid</li>
 * 			<li>{1}: conceptid</li>
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
			"http://rest.bioontology.org/bioportal/search/?ontologyids={0}&query={1}&isexactmatch={2}&apikey=9f19fdf6-82d0-4335-97a1-f71d3ce156f6";
	
	private String getUrl =
			"http://rest.bioontology.org/bioportal/concepts/{0}?conceptid={1}&light=1&apikey=9f19fdf6-82d0-4335-97a1-f71d3ce156f6";

	private boolean useProxy = true;

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
