package uk.ac.ebi.ep.adapter.chebi;

import java.io.FileInputStream;
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
    private static final String SS_URL = "ep.chebi.ss.url";
    private static final String SS_PRINTERFRIENDY =
            "ep.chebi.ss.printerfriendly";
    private static final String SS_CALLBACK_URL = "ep.chebi.ss.callback.url";
    private static final String SS_DATASOURCE = "ep.chebi.ss.datasource";
    private static final String SS_SPECIALDATASET =
            "ep.chebi.ss.special.dataset";
    
	int maxThreads = 10;
	
	int maxRetrievedMolecules = 3;
	
	StarsCategory searchStars = StarsCategory.ALL;
	
	int timeout = 30000;
	
	protected String compoundBaseUrl =
	        "http://www.ebi.ac.uk/chebi/searchId.do?chebiId=";

	protected String compoundImgBaseUrl =
	        "http://www.ebi.ac.uk/chebi/displayImage.do?"
	        + "defaultImage=true&imageIndex=0&chebiId=";
	
	protected String ssUrl =
	        "http://www.ebi.ac.uk/chebi/advancedSearchForward.do";
	
	protected boolean ssPrinterFriendly = true;
	
	protected String ssCallbackUrl =
	        "http://www.ebi.ac.uk/enzymeportal/search%3F"
	        + "searchparams.type%3DCOMPOUND%26searchparams.text%3D*";
	
	protected String ssDatasource = "EnzymePortal";
	
	protected String ssSpecialDataset = "EnzymePortal";

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
    
    public String getSsUrl() {
        return ssUrl;
    }

    public void setSsUrl(String ssUrl) {
        this.ssUrl = ssUrl;
    }

    public boolean isSsPrinterFriendly() {
        return ssPrinterFriendly;
    }

    public void setSsPrinterFriendly(boolean ssPrinterFriendly) {
        this.ssPrinterFriendly = ssPrinterFriendly;
    }

    public String getSsCallbackUrl() {
        return ssCallbackUrl;
    }

    public void setSsCallbackUrl(String ssCallbackUrl) {
        this.ssCallbackUrl = ssCallbackUrl;
    }

    public String getSsDatasource() {
        return ssDatasource;
    }

    public void setSsDatasource(String ssDatasource) {
        this.ssDatasource = ssDatasource;
    }

    public String getSsSpecialDataset() {
        return ssSpecialDataset;
    }

    public void setSsSpecialDataset(String ssSpecialDataset) {
        this.ssSpecialDataset = ssSpecialDataset;
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
     *  <li><code>ep.chebi.ss.url</code>: the URL for the chebi structure
     *      search.</li>
     *  <li><code>ep.chebi.ss.printerfriendly</code>: is the web interface of
     *      the structure search printer friendly (i.e. without headers, ready
     *      to embed in another web page)? Normally set to
     *      <code>true</code>.</li>
     *  <li><code>ep.chebi.ss.special.dataset</code>: the configuration of the
     *      structure search at the ChEBI side. Normally set to
     *      <code>EnzymePortal</code>.</li>
     *  <li><code>ep.chebi.ss.data.source</code>: the source of cross references
     *      in ChEBI for which results will be filtered. Normally set to
     *      <code>EnzymePortal</code>.</li>
     *  <li><code>ep.chebi.ss.callback.url</code>: the URL called from a result
     *      from the ChEBI structure search. It includes an asterisk which will
     *      be replaced by the ChEBI ID.</li>
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
        config.setSsUrl(props.getProperty(SS_URL, config.getSsUrl()));
        config.setSsPrinterFriendly(Boolean.valueOf(
                props.getProperty(SS_PRINTERFRIENDY,
                        String.valueOf(config.isSsPrinterFriendly())
                )));
        config.setSsSpecialDataset(props.getProperty(SS_SPECIALDATASET,
                config.getSsSpecialDataset()));
        config.setSsDatasource(props.getProperty(SS_DATASOURCE,
                config.getSsDatasource()));
        config.setSsCallbackUrl(props.getProperty(SS_CALLBACK_URL,
                config.getSsCallbackUrl()));
        return config;
    }
}
