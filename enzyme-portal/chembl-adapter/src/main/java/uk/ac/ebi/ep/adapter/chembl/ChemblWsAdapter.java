package uk.ac.ebi.ep.adapter.chembl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import uk.ac.ebi.biobabel.util.xml.XPathSAXHandler;

/**
 * @author rafa
 * @since 1.0.0
 */
public class ChemblWsAdapter implements IChemblAdapter {

    private static final Logger LOGGER =
            Logger.getLogger(ChemblWsAdapter.class);

    private enum SearchBy { targets, compounds }

    private static final String COMPOUND_PATH = "/compounds/{0}";
    private static final String PREFERRED_COMPOUND_NAME_XPATH =
            "//compound/preferredCompoundName";

    private static final String BIOACTIVITIES_PATH = "/{0}/{1}/bioactivities";
    private static final String TARGET_ID_XPATH =
            "//list/bioactivity/target__chemblid";
    private static final String COMPOUND_ID_XPATH =
            "//list/bioactivity/ingredient__cmpd__chemblid";
    private static final String CONFIDENCE_XPATH =
            "//list/bioactivity/target__confidence";
    private static final String ASSAY_TYPE_XPATH =
            "//list/bioactivity/assay__type";
    
    private static final String UP_TARGETS_PATH = "/targets/uniprot/{0}";
    private static final String UP_TARGET_ID_XPATH = "//target/chemblId";
    private static final String UP_TARGET_BIOACT_COUNT =
            "//target/bioactivityCount";

    private ChemblConfig config;

    /**
     * Constructor with default configuration.
     */
    public ChemblWsAdapter(){
        this(new ChemblConfig());
    }

    /**
     * Constructor with custom configuration.
     * @param config a custom configuration.
     */
    public ChemblWsAdapter(ChemblConfig config){
        // Override default:
        this.config = config;
    }

    public ChemblConfig getConfig() {
        return config;
    }

    public void setConfig(ChemblConfig config) {
        this.config = config;
    }

    protected ChemblBioactivities getBioactivities(String chemblId,
            SearchBy searchBy)
    throws ChemblAdapterException {
        ChemblBioactivities bioactivities = null;
        try {
            final String theUrl = MessageFormat.format(
                    config.getWsBaseUrl() + BIOACTIVITIES_PATH,
                    searchBy.name(), chemblId);
            String[] xpaths = { TARGET_ID_XPATH, COMPOUND_ID_XPATH,
                    CONFIDENCE_XPATH, ASSAY_TYPE_XPATH };
            Map<String, Collection<String>> results =
                    getResults(theUrl, xpaths);
            final Collection<String> targetIds = results.get(TARGET_ID_XPATH);
            if (targetIds != null){
                bioactivities = new ChemblBioactivities();
                Iterator<String> confIter =
                        results.get(CONFIDENCE_XPATH).iterator();
                Iterator<String> asstIter =
                        results.get(ASSAY_TYPE_XPATH).iterator();
                // We get compounds for targets and vice-versa:
                switch (searchBy){
                    case compounds:
                        for (String targetId : targetIds) {
                            bioactivities.addBioactivity(targetId,
                                    Integer.parseInt(confIter.next()),
                                    asstIter.next());
                        }
                        break;
                    case targets:
                        for (String compId : results.get(COMPOUND_ID_XPATH)){
                            bioactivities.addBioactivity(compId,
                                    Integer.parseInt(confIter.next()),
                                    asstIter.next());
                        }
                        break;
                }
            }
        } catch (Exception e) {
            throw new ChemblAdapterException(chemblId, e);
        }
        return bioactivities;
    }

    public ChemblBioactivities getTargetBioactivities(String targetId)
    throws ChemblAdapterException {
        return getBioactivities(targetId, SearchBy.targets);
    }

    public ChemblBioactivities getCompoundBioactivities(String compoundId)
    throws ChemblAdapterException {
        return getBioactivities(compoundId, SearchBy.compounds);
    }

    public String getPreferredName(String compoundId)
    throws ChemblAdapterException {
        String name = null;
        try {
            final String theUrl = MessageFormat.format(
                    config.getWsBaseUrl() + COMPOUND_PATH, compoundId);
            Map<String, Collection<String>> results =
                    getResults(theUrl, PREFERRED_COMPOUND_NAME_XPATH);
            Collection<String> names =
                    results.get(PREFERRED_COMPOUND_NAME_XPATH);
            if (names != null) name = names.iterator().next(); // only one
        } catch (Exception e) {
            throw new ChemblAdapterException(compoundId, e);
        }
        return name;
    }

    public List<String> getTargets(String uniprotAcc)
    throws ChemblAdapterException {
        List<String> targetIds = null;
        String theUrl = MessageFormat.format(
                config.getWsBaseUrl() + UP_TARGETS_PATH, uniprotAcc);
        try {
            Map<String, Collection<String>> results =
                    getResults(theUrl, UP_TARGET_ID_XPATH);
            targetIds = (List<String>) results.get(UP_TARGET_ID_XPATH);
        } catch (Exception e) {
            throw new ChemblAdapterException(uniprotAcc, e);
        }
        return targetIds;
    }
    
    /**
     * Connects to the web service and retrieves the interesting values.
     * @param theUrl the complete URL of the requested resource.
     * @param xpaths the interesting XPaths.
     * @return a map of XPaths to retrieved values.
     * @throws SAXException in case of problem creating the XML reader or
     *      parsing the response.
     * @throws IOException in case of problem establishing the connection or
     *      parsing the response.
     * @since 1.0.2
     */
    private Map<String, Collection<String>> getResults(String theUrl,
            String... xpaths) throws SAXException, IOException{
        InputStream is = null;
        try {
            XMLReader xr = XMLReaderFactory.createXMLReader();
            XPathSAXHandler handler = new XPathSAXHandler(xpaths);
            xr.setContentHandler(handler);
            URL url = new URL(theUrl);
            URLConnection urlCon = url.openConnection();
            urlCon.setRequestProperty("Accept", "application/xml");
            is = urlCon.getInputStream();
            InputSource inputSource = new InputSource(is);
            xr.parse(inputSource);
            return handler.getResults();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error("Unable to close input stream", e);
                }
            }
        }
    }

}
