package uk.ac.ebi.ep.adapter.chembl;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;

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
        InputStream is = null;
        try {
            XMLReader xr = XMLReaderFactory.createXMLReader();
            XPathSAXHandler handler = new XPathSAXHandler(
                    TARGET_ID_XPATH, COMPOUND_ID_XPATH,
                    CONFIDENCE_XPATH, ASSAY_TYPE_XPATH);
            xr.setContentHandler(handler);
            URL url = new URL(MessageFormat.format(
                    config.getWsBaseUrl() + BIOACTIVITIES_PATH,
                    searchBy.name(), chemblId));
            URLConnection urlCon = url.openConnection();
            urlCon.setRequestProperty("Accept", "application/xml");
            is = urlCon.getInputStream();
            InputSource inputSource = new InputSource(is);
            xr.parse(inputSource);
            final Collection<String> targetIds =
                    handler.getResults().get(TARGET_ID_XPATH);
            final Collection<String> compoundIds =
                    handler.getResults().get(COMPOUND_ID_XPATH);
            final Collection<String> confidences =
                    handler.getResults().get(CONFIDENCE_XPATH);
            final Collection<String> assayTypes =
                    handler.getResults().get(ASSAY_TYPE_XPATH);
            if (targetIds != null){
                bioactivities = new ChemblBioactivities();
                Iterator<String> confIter = confidences.iterator();
                Iterator<String> asstIter = assayTypes.iterator();
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
                        Iterator<String> compIter = compoundIds.iterator();
                        for (String compoundId : compoundIds) {
                            bioactivities.addBioactivity(compoundId,
                                    Integer.parseInt(confIter.next()),
                                    asstIter.next());
                        }
                        break;
                }
            }
        } catch (MalformedURLException e) {
            throw new ChemblAdapterException(chemblId, e);
        } catch (IOException e) {
            throw new ChemblAdapterException(chemblId, e);
        } catch (SAXException e) {
            throw new ChemblAdapterException(chemblId, e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error(e);
                }
            }
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
        InputStream is = null;
        try {
            XMLReader xr = XMLReaderFactory.createXMLReader();
            XPathSAXHandler handler = new XPathSAXHandler(
                    PREFERRED_COMPOUND_NAME_XPATH);
            xr.setContentHandler(handler);
            URL url = new URL(MessageFormat.format(
                    config.getWsBaseUrl() + COMPOUND_PATH, compoundId));
            URLConnection urlCon = url.openConnection();
            urlCon.setRequestProperty("Accept", "application/xml");
            is = urlCon.getInputStream();
            InputSource inputSource = new InputSource(is);
            xr.parse(inputSource);
            Collection<String> names =
                    handler.getResults().get(PREFERRED_COMPOUND_NAME_XPATH);
            if (names != null) name = names.iterator().next(); // only one
        } catch (MalformedURLException e) {
            throw new ChemblAdapterException(compoundId, e);
        } catch (IOException e) {
            throw new ChemblAdapterException(compoundId, e);
        } catch (SAXException e) {
            throw new ChemblAdapterException(compoundId, e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error(e);
                }
            }
        }
        return name;
    }

}
