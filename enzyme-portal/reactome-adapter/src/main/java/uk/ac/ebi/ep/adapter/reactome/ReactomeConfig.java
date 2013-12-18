package uk.ac.ebi.ep.adapter.reactome;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ReactomeConfig implements ReactomeConfigMBean {

    private static final Logger LOGGER = Logger.getLogger(ReactomeConfig.class);
    
    private static final String USE_PROXY = "ep.reactome.proxy.use";
    private static final String TIMEOUT = "ep.reactome.ws.timeout";
    private static final String WS_BASE_URL = "ep.reactome.ws.base.url";
    private static final String EVENT_BASE_URL = "ep.reactome.event.base.url";
    
	/**
	 * Use a proxy for requests to Reactome?
	 */
	private boolean useProxy = true;
	
	/**
	 * Timeout for requests to Reactome, in milliseconds.
	 */
	private int timeout = 30000;
	
	private String wsBaseUrl = 
			"http://www.reactome.org:8080/ReactomeRESTfulAPI/RESTfulWS/queryById/";
	
	private String eventBaseUrl =
	        "http://www.reactome.org/cgi-bin/link?SOURCE=Reactome&ID=";
	
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

	public void setWsBaseUrl(String wsBaseUrl) {
		this.wsBaseUrl = wsBaseUrl;
	}

	public String getWsBaseUrl() {
		return wsBaseUrl;
	}

    public String getEventBaseUrl() {
        return eventBaseUrl;
    }

    public void setEventBaseUrl(String eventBaseUrl) {
        this.eventBaseUrl = eventBaseUrl;
    }
    
    /**
     * Reads the configuration for the Reactome proxy from a properties file
     * <code>reactome-adapter.properties</code> in the classpath.
     * @return a configuration for the Reactome proxy with the properties values
     *      from the file, or the default ones for those missing.
     * @throws IOException in case of problems reading from file.
     * @since 1.0.3
     * @see #readFromInputStream(InputStream) Properties' keys.
     */
    public static ReactomeConfig readFromFile() throws IOException{
        return readFromInputStream(ReactomeConfig.class.getClassLoader()
                .getResourceAsStream("reactome-adapter.properties"));
    }
    
    /**
     * Reads the configuration from a file.
     * @param filePath the path to a file with the configuration properties.
     *      Searched first in the classpath, then if not found in the current
     *      working directory.
     * @return a configuration for the Reactome proxy with the properties values
     *      from the file, or the default ones for those missing.
     * @throws IOException in case of problems reading from file.
     * @since 1.0.3
     * @see #readFromInputStream(InputStream) Properties' keys.
     */
    public static ReactomeConfig readFromFile(String filePath)
    throws IOException {
        InputStream is = null;
        try {
            is = ReactomeConfig.class.getClassLoader()
                    .getResourceAsStream(filePath);
            if (is == null){
                LOGGER.info(filePath + " not found in the classpath,"
                        + " trying the working directory...");
                is = new FileInputStream(filePath);
            }
            return readFromInputStream(is);
        } finally {
            if (is != null) try {
                is.close();
            } catch (IOException e) {
                LOGGER.error("Unable to close input stream", e);
            }
        }
    }
    
    /**
     * Reads the configuration from an input stream. The properties' keys are:
     * <ul>
     *  <li><code>ep.reactome.proxy.use</code>: (boolean) use a proxy for
     *      requests to reactome web service?</li>
     *  <li><code>ep.reactome.ws.timeout</code>: (integer) maximum time (ms) to
     *      get a response from the web service.</li>
     *  <li><code>ep.reactome.ws.base.url</code>: (string) base URL for the
     *      reactome web service.</li>
     *  <li><code>ep.reactome.event.base.url</code>: (string) base URL for
     *      events in the Reactome web site (complete URL is built by appending
     *      the event stable ID at the end).</li>
     * </ul>
     * @param is an input stream.
     * @return a configuration for the Reactome proxy with the properties values
     *      from the file, or the default ones for those missing.
     * @throws IOException in case of problem reading the properties from file.
     * @since 1.0.3
     */
    protected static ReactomeConfig readFromInputStream(InputStream is)
    throws IOException{
        Properties props = new Properties();
        props.load(is);
        ReactomeConfig config = new ReactomeConfig();
        try {
            config.setUseProxy(Boolean.valueOf(props.getProperty(USE_PROXY)));
        } catch (Exception e){
            LOGGER.error("Using default for invalid value " + USE_PROXY
                    + "=" + props.getProperty(USE_PROXY));
        }
        try {
            config.setTimeout(Integer.valueOf(props.getProperty(TIMEOUT)));
        } catch (NumberFormatException e){
            LOGGER.error("Using default for invalid value " + TIMEOUT
                    + "=" + props.getProperty(TIMEOUT));
        }
        config.setWsBaseUrl(props.getProperty(WS_BASE_URL,
                config.getWsBaseUrl()));
        config.setEventBaseUrl(props.getProperty(EVENT_BASE_URL,
                config.getEventBaseUrl()));
        return config;
    }

}
