package uk.ac.ebi.ep.adapter.intenz;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;

public class IntenzConfig implements IntenzConfigMBean {

    private static final Logger LOGGER = Logger.getLogger(IntenzConfig.class);
    
    private static final String TIMEOUT = "ep.intenz.ws.timeout";
    private static final String XML_URL = "ep.intenz.ws.xml.url";
    private static final String EC_URL = "ep.intenz.ec.url";
    
	private int timeout = 30000;
	
	private String intenzXmlUrl =
	        "http://www.ebi.ac.uk/intenz/ws/EC/{0}.{1}.{2}.{3}.xml";
	
	private String ecBaseUrl =
	        "http://www.ebi.ac.uk/intenz/query?cmd=SearchEC&ec=";
	
	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getIntenzXmlUrl() {
		return intenzXmlUrl;
	}

	public void setIntenzXmlUrl(String intenzXmlUrl) {
		this.intenzXmlUrl = intenzXmlUrl;
	}

    public String getEcBaseUrl() {
        return ecBaseUrl;
    }

    public void setEcBaseUrl(String ecBaseUrl) {
        this.ecBaseUrl = ecBaseUrl;
    }

    /**
     * Reads the configuration for the IntEnz proxy from a properties file
     * <code>intenz-adapter.properties</code> in the classpath.     
     * @return a configuration for the IntEnz proxy with the properties values
     *      from the file, or the default ones for those missing.
     * @throws IOException in case of problems reading from file.
     * @since 1.0.6
     * @see #readFromInputStream(InputStream) Properties' keys.
     */
    public static IntenzConfig readFromFile() throws IOException{
        return readFromInputStream(IntenzConfig.class.getClassLoader()
                .getResourceAsStream("intenz-adapter.properties"));
    }
    
    /**
     * Reads the configuration from a file.
     * @param filePath the path to a file with the configuration properties.
     *      Searched first in the classpath, then if not found in the current
     *      working directory.
     * @return a configuration for the IntEnz proxy with the properties values
     *      from the file, or the default ones for those missing.
     * @throws IOException in case of problems reading from file.
     * @since 1.0.6
     * @see #readFromInputStream(InputStream) Properties' keys.
     */
    public static IntenzConfig readFromFile(String filePath) throws IOException{
        InputStream is = null;
        try {
            is = IntenzConfig.class.getClassLoader().getResourceAsStream(filePath);
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
     *  <li><code>ep.intenz.ws.timeout</code>: (integer) maximum time
     *      (ms) to get a response from the web service.</li>
     *  <li><code>ep.intenz.ws.xml.url</code>: (string) URL template
     *      to retrieve IntEnz entries as IntEnzXML from the web service. It
     *      should have the placeholders {0}, {1}, {2} and {3} for ec1, ec2, ec3
     *      and ec4 respectively.</li>
     *  <li><code>ep.intenz.ec.url</code>: (string) base URL for EC
     *      numbers in the IntEnz website (the complete URL is built just adding
     *      the EC number at the end).</li>
     * </ul>
     * @param is an input stream.
     * @return a configuration for the IntEnz proxy with the properties values
     *      from the file, or the default ones for those missing.
     * @throws IOException in case of problem reading the properties from file.
     * @since 1.0.6
     */
    protected static IntenzConfig readFromInputStream(InputStream is)
    throws IOException{
        Properties props = new Properties();
        props.load(is);
        IntenzConfig config = new IntenzConfig();
        try {
            config.setTimeout(Integer.valueOf(props.getProperty(TIMEOUT)));
        } catch (NumberFormatException e){
            LOGGER.error("Using default for invalid value " + TIMEOUT
                    + "=" + props.getProperty(TIMEOUT));
        }
        config.setIntenzXmlUrl(props.getProperty(XML_URL,
                config.getIntenzXmlUrl()));
        config.setEcBaseUrl(props.getProperty(EC_URL, config.getEcBaseUrl()));
        return config;
    }
}
