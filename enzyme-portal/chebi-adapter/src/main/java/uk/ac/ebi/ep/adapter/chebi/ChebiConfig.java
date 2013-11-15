package uk.ac.ebi.ep.adapter.chebi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import uk.ac.ebi.chebi.webapps.chebiWS.model.StarsCategory;

public class ChebiConfig implements ChebiConfigMBean {

    private static final Logger LOGGER = Logger.getLogger(ChebiConfig.class);
    
    private static final String MAX_THREADS = "ep.chebi.threads.max";
    private static final String MAX_RESULTS = "ep.chebi.search.max";
    private static final String SEARCH_STARS = "ep.chebi.search.stars";
    private static final String TIMEOUT = "ep.chebi.ws.timeout";
    private static final String COMPOUND_URL = "ep.chebi.compound.base.url";
    private static final String IMG_URL = "ep.chebi.compound.img.base.url";
    
	int maxThreads = 10;
	
	int maxRetrievedMolecules = 3;
	
	StarsCategory searchStars = StarsCategory.ALL;
	
	int timeout = 30000;
	
	protected String compoundBaseUrl =
	        "http://www.ebi.ac.uk/chebi/searchId.do?chebiId=";

	protected String compoundImgBaseUrl =
	        "http://www.ebi.ac.uk/chebi/displayImage.do?defaultImage=true&imageIndex=0&chebiId=";
	
    public int getMaxThreads() {
		return maxThreads;
	}

	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}

	public String getSearchStars() {
		return searchStars.name();
	}

	public void setSearchStars(String searchStars) {
		this.searchStars = StarsCategory.valueOf(searchStars);
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getMaxRetrievedMolecules() {
		return maxRetrievedMolecules;
	}

	public void setMaxRetrievedMolecules(int maxRetrievedMolecules) {
		this.maxRetrievedMolecules = maxRetrievedMolecules;
	}

    public String getCompoundBaseUrl() {
        return compoundBaseUrl;
    }

    public void setCompoundBaseUrl(String compoundBaseUrl) {
        this.compoundBaseUrl = compoundBaseUrl;
    }

    public String getCompoundImgBaseUrl() {
        return compoundImgBaseUrl;
    }

    public void setCompoundImgBaseUrl(String compoundImgBaseUrl) {
        this.compoundImgBaseUrl = compoundImgBaseUrl;
    }
	
    /**
     * Reads the configuration for the ChEBI proxy from a properties file
     * <code>chebi-adapter.properties</code> in the classpath.
     * @return a configuration for the ChEBI proxy with the properties values
     *      from the file, or the default ones for those missing.
     * @throws IOException in case of problems reading from file.
     * @since 1.0.4
     * @see #readFromInputStream(InputStream) Properties' keys.
     */
    public static ChebiConfig readFromFile() throws IOException{
        return readFromInputStream(ChebiConfig.class.getClassLoader()
                .getResourceAsStream("chebi-adapter.properties"));
    }
    
    /**
     * Reads the configuration from a file.
     * @param filePath the path to a file with the configuration properties.
     *      Searched first in the classpath, then if not found in the current
     *      working directory.
     * @return a configuration for the ChEBI proxy with the properties values
     *      from the file, or the default ones for those missing.
     * @throws IOException in case of problems reading from file.
     * @since 1.0.4
     * @see #readFromInputStream(InputStream) Properties' keys.
     */
    public static ChebiConfig readFromFile(String filePath) throws IOException {
        InputStream is = null;
        try {
            is = ChebiConfig.class.getClassLoader()
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
     *  <li><code>ep.chebi.threads.max</code>: (integer) maximum number of
     *      threads started to retrieve data from the web service.</li>
     *  <li><code>ep.chebi.search.max</code>: (integer) maximum number of
     *      compounds retrieved from the web service.</li>
     *  <li><code>ep.chebi.search.stars</code>: (string) curation status
     *      of results retrieved from ChEBI (see {@link StarsCategory} enum).
     *      </li>
     *  <li><code>ep.chebi.ws.timeout</code>: (integer) maximum time (ms) to get
     *      a response from the web service.</li>
     *  <li><code>ep.chebi.compound.base.url</code>: (string) base URL for ChEBI
     *      compounds (complete URL is built by appending the ChEBI ID at the
     *      end).</li>
     *  <li><code>ep.chebi.compound.img.base.url</code>: (string) base URL for
     *      compound images (complete URL is built by appending the ChEBI ID at
     *      the end).</li>
     * </ul>
     * @param is an input stream.
     * @return a configuration for the ChEBI proxy with the properties values
     *      from the file, or the default ones for those missing.
     * @throws IOException in case of problem reading the properties from file.
     * @since 1.0.4
     */
    protected static ChebiConfig readFromInputStream(InputStream is)
    throws IOException{
        Properties props = new Properties();
        props.load(is);
        ChebiConfig config = new ChebiConfig();
        try {
            config.setMaxThreads(Integer.valueOf(
                    props.getProperty(MAX_THREADS)));
        } catch (NumberFormatException e){
            LOGGER.error("Using default for invalid value " + MAX_THREADS
                    + "=" + props.getProperty(MAX_THREADS));
        }
        try {
            config.setMaxRetrievedMolecules(Integer.valueOf(
                    props.getProperty(MAX_RESULTS)));
        } catch (NumberFormatException e){
            LOGGER.error("Using default for invalid value "
                    + MAX_RESULTS
                    + "=" + props.getProperty(MAX_RESULTS));
        }
        try {
            config.setSearchStars(props.getProperty(SEARCH_STARS));
        } catch (Exception e){
            LOGGER.error("Using default for invalid value " + SEARCH_STARS
                    + "=" + props.getProperty(SEARCH_STARS));
        }
        try {
            config.setTimeout(Integer.valueOf(props.getProperty(TIMEOUT)));
        } catch (NumberFormatException e){
            LOGGER.error("Using default for invalid value " + TIMEOUT
                    + "=" + props.getProperty(TIMEOUT));
        }
        config.setCompoundBaseUrl(props.getProperty(COMPOUND_URL,
                config.getCompoundBaseUrl()));
        config.setCompoundImgBaseUrl(props.getProperty(IMG_URL,
                config.getCompoundImgBaseUrl()));
        return config;
    }
}
