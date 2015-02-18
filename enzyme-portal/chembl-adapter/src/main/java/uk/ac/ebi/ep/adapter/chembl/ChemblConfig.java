package uk.ac.ebi.ep.adapter.chembl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author rafa
 * @since 1.0.0
 */
public class ChemblConfig implements ChemblConfigMBean {

    private static final Logger LOGGER = Logger.getLogger(ChemblConfig.class);

    private static final String MIN_ASSAYS =
            "ep.chembl.bioactivities.assays.min";
    private static final String MIN_FUNCTIONAL =
            "ep.chembl.bioactivities.assays.functional.min";
    private static final String MIN_CONF4 =
            "ep.chembl.bioactivities.assays.confidence4.min";
    private static final String MIN_CONF9 =
            "ep.chembl.bioactivities.assays.confidence9.min";
    private static final String WS_URL = "ep.chembl.ws.url.base";
    private static final String COMPOUND_URL =
            "ep.chembl.compound.url.base";
    private static final String IMG_URL = 
            "ep.chembl.compound.img.url.base";

    private int minAssays = 5;

    private double minFunc = 0.5;

    private double minConf4 = 0.0;

    private double minConf9 = 0.5;

    private String wsBaseUrl = "https://www.ebi.ac.uk/chemblws";
    
    private String compoundBaseUrl =
            "https://www.ebi.ac.uk/chembldb/compound/inspect/";
    
    private String compoundImgBaseUrl =
            "https://www.ebi.ac.uk/chembldb/compound/displayimage/";

    public int getMinAssays() {
        return minAssays;
    }

    public void setMinAssays(int minAssays) {
        this.minAssays = minAssays;
    }

    public double getMinFunc() {
        return minFunc;
    }

    public void setMinFunc(double minFunc) {
        this.minFunc = minFunc;
    }

    public double getMinConf4() {
        return minConf4;
    }

    public void setMinConf4(double minConf4) {
        this.minConf4 = minConf4;
    }

    public double getMinConf9() {
        return minConf9;
    }

    public void setMinConf9(double minConf9) {
        this.minConf9 = minConf9;
    }

    public String getWsBaseUrl() {
        return wsBaseUrl;
    }

    public void setWsBaseUrl(String wsBaseUrl) {
        this.wsBaseUrl = wsBaseUrl;
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
     * Reads the configuration for the ChEMBL proxy from a properties file
     * <code>chembl-adapter.properties</code> in the classpath.
     * @return A configuration object with its fields set to the values from
     *      the properties file (or the default values, if missing there).
     * @throws IOException in case of trouble loading the properties.
     * @see #readFromInputStream(InputStream) Properties' keys.
     */
    public static ChemblConfig readFromFile() throws IOException {
        return readFromInputStream(ChemblConfig.class.getClassLoader()
                .getResourceAsStream("chembl-adapter.properties"));
    }

    /**
     * Reads configuration values from a file.
     * @param filePath the absolute path of the configuration file.
     *      Searched first in the classpath, then if not found in the current
     *      working directory.
     * @return a configuration object.
     * @throws IOException in case of trouble finding the file or loading the
     *      properties.
     * @see #readFromInputStream(InputStream) Properties' keys.
     */
    public static ChemblConfig readFromFile(String filePath)
    throws IOException {
        InputStream is = null;
        try {
            is = ChemblConfig.class.getClassLoader().getResourceAsStream(filePath);
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
     * Reads properties from an input stream. If any property is not found, the
     * default value is used. The properties' keys are:
     * <ul>
     *  <li><code>ep.chembl.bioactivities.assays.min</code>: (integer)
     *      minimum number of assays required to {@link
     *      ChemblBioactivities#filter(int, double, double, double) filter} a
     *      bioactivity.</li>
     *  <li><code>ep.chembl.bioactivities.assays.functional.min</code>:
     *      (double) minimum fraction of <a
     *      href="https://www.ebi.ac.uk/chembldb/index.php/faq#faq41">functional
     *      </a> assays (<i>"data measuring the biological effect of a
     *      compound"</i>) required to {@link
     *      ChemblBioactivities#filter(int, double, double, double) filter} a
     *      bioactivity.</li>
     *  <li><code>ep.chembl.bioactivities.assays.confidence4.min</code>:
     *      (double) minimum fraction of assays with a confidence 4
     *      (<i>"Multiple homologous protein targets may be assigned"</i>) or
     *      higher required to {@link ChemblBioactivities#filter(int, double,
     *      double, double) filter} a bioactivity.</li>
     *  <li><code>ep.chembl.bioactivities.assays.confidence9.min</code>:
     *      (double) minimum fraction of assays with a confidence 9 (<i>"Direct
     *      single protein target assigned"</i>) or higher required to {@link
     *      ChemblBioactivities#filter(int, double, double, double) filter} a
     *      bioactivity.</li>
     *  <li><code>ep.chembl.ws.url.base</code>: the base URL for ChEMBL
     *      web services.</li>
     *  <li><code>ep.chembl.compound.url.base</code>: the base URL for
     *      the public view of a ChEMBL compound.</li>
     *  <li><code>ep.chembl.compound.img.url.base</code>: the base URL
     *      for the image of a ChEMBL compound.</li>
     * </ul>
     * For confidence levels, see also the table CONFIDENCE_SCORE_LOOKUP in
     * ChEMBL schema.
     * @param is the input stream.
     * @return a ChEMBL configuration with properties from the input stream,
     *      or default values for properties not found.
     * @throws IOException in case of problem loading the properties.
     */
    protected static ChemblConfig readFromInputStream(InputStream is)
    throws IOException {
        Properties props = new Properties();
        props.load(is);
        ChemblConfig config = new ChemblConfig();
        try {
            config.setMinAssays(Integer.valueOf(props.getProperty(MIN_ASSAYS)));
        } catch (NumberFormatException e){
            LOGGER.error("Using default for invalid value "
                    + MIN_ASSAYS + "=" + props.getProperty(MIN_ASSAYS));
        }
        try {
            config.setMinFunc(Double.valueOf(props.getProperty(MIN_FUNCTIONAL)));
        } catch (NumberFormatException e){
            LOGGER.error("Using default for invalid value "
                    + MIN_FUNCTIONAL + "=" + props.getProperty(MIN_FUNCTIONAL));
        }
        try {
            config.setMinConf4(Double.valueOf(props.getProperty(MIN_CONF4)));
        } catch (NumberFormatException e){
            LOGGER.error("Using default for invalid value "
                    + MIN_CONF4 + "=" + props.getProperty(MIN_CONF4));
        }
        try {
            config.setMinConf9(Double.valueOf(props.getProperty(MIN_CONF9)));
        } catch (NumberFormatException e){
            LOGGER.error("Using default for invalid value "
                    + MIN_CONF9 + "=" + props.getProperty(MIN_CONF9));
        }
        config.setWsBaseUrl(props.getProperty(WS_URL, config.wsBaseUrl));
        config.setCompoundBaseUrl(
                props.getProperty(COMPOUND_URL, config.compoundBaseUrl));
        config.setCompoundImgBaseUrl(
                props.getProperty(IMG_URL, config.compoundImgBaseUrl));
        return config;
    }
}
