package uk.ac.ebi.ep.core.search;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import uk.ac.ebi.ep.config.Domain;
import uk.ac.ebi.ep.config.EnzymeRelatedDomains;
import uk.ac.ebi.ep.config.ResultField;
import uk.ac.ebi.ep.core.search.IEnzymeFinder.UniprotImplementation;
import uk.ac.ebi.ep.core.search.IEnzymeFinder.UniprotSource;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class Config implements ConfigMBean {

    private static final Logger LOGGER = Logger.getLogger(ConfigMBean.class);

	/* <moveThisTo_EbeyeConfig spaghetti="true"> */
	@Deprecated
    public static List<Domain> domainList;
    @Deprecated
    public static final String configFile = "/config.xml";
	/* </moveThisTo_EbeyeConfig> */

    protected int resultsPerPage = 10;
    
    protected int maxPages = 1;
    
    protected UniprotSource finderUniprotSource = UniprotSource.EBEYE;
    
    protected UniprotSource retrieverUniprotSource = UniprotSource.UNIPROT;
    
    protected UniprotImplementation uniprotImplementation =
    		UniprotImplementation.WS;
    
    protected int searchCacheSize = 50;

    protected int maxMoleculesPerGroup = 3;
    
    protected String structureSearchUrl =
            "http://www.ebi.ac.uk/chebi/advancedSearchForward.do?"
            + "printerFriendlyView=true&datasourceQuery[0].value=EnzymePortal"
            + "&specialDataset=EnzymePortal&callbackUrl=/enzymeportal/search%3F"
            + "searchparams.type%3DCOMPOUND%26searchparams.text%3D*";
    
    /**
     * The JNDI name for the mega-mapper data source.
     */
    protected String mmDatasource = "jdbc/ep/mm";
    
	public void initIt() throws Exception {
	  LOGGER.debug("Init method after properties are set : ");
          loadCacheData();
	}

	public void cleanUp() throws Exception {
	  LOGGER.debug("Spring Container is destroy! Customer clean up");
          
	}

    /**
     * @deprecated this method loads EB-Eye fields from a XML file included in
     *      ep-domain. Use ebeye-adapter's Domains enum instead.
     */
    @Deprecated
    public void loadCacheData() {
        try {
            EnzymeRelatedDomains enzymeRelatedDomains =
                    unmarshalConfigFile(Config.class.getResourceAsStream(configFile));
            domainList = enzymeRelatedDomains.getDomain();
            //enzymesInUniprot = unmarshalCacheFile(Config.class.getResourceAsStream(""));
        } catch (IOException ex) {
            //COnfig file not found
        } catch (JAXBException ex) {
            //Config file provided can be unmarshalled
        }
    }

/*
    public static File marshalResultToXml(ResultCollection resultCollection, File f) throws JAXBException
            {
            JAXBContext jaxbContext = JAXBContext.newInstance("uk.ac.ebi.ep.ebeye.result");
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://ebi.ac.uk/ep/ebeye/result ebeyeResult.xsd");
            marshaller.marshal(resultCollection, f);
        return f;
    }
*/

    /**
     * @deprecated this method is only used by the deprecated
     *      {@link #loadCacheData()}
     */
    @Deprecated
    public static EnzymeRelatedDomains unmarshalConfigFile(InputStream inFile)
            throws IOException, JAXBException {
        EnzymeRelatedDomains enzymeRelatedDomains = null;
        JAXBContext jaxbContext = JAXBContext.newInstance("uk.ac.ebi.ep.config");
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        JAXBElement<?> jaxbElement = (JAXBElement<?>) unmarshaller.unmarshal(inFile);
        enzymeRelatedDomains = (EnzymeRelatedDomains) jaxbElement.getValue();
        return enzymeRelatedDomains;
    }

    /**
     * @deprecated use ebeye-adapter's Domains enum
     */
     @Deprecated
    public static Domain getDomain(String domainId) {
        Iterator<Domain> it = Config.domainList.iterator();
        Domain domain = null;
        while (it.hasNext()) {
            domain = (Domain) it.next();
            if (domainId.equals(domain.getId())) {
                break;
            }
        }
        return domain;
    }

    /**
     * @deprecated This method is not used, as for every search in EB-Eye we
     *      retrieve either acc field (uniprot domain), or UNIPROT pseudo-field
     *      or id field (to get from it the acc of any uniprot references).
     */
     @Deprecated
    public static List<String> getResultFields(Domain domain) {
        List<ResultField> fieldList = domain.getResultFieldList().getResultField();
        Iterator<ResultField> it = fieldList.iterator();
        List<String> resultRefFields = new ArrayList<String>();
        while (it.hasNext()) {
            ResultField field = (ResultField) it.next();
            resultRefFields.add(field.getId());
        }
        return resultRefFields;
    }

	public int getResultsPerPage() {
		return resultsPerPage;
	}

	public void setResultsPerPage(int resultsPerPage) {
		this.resultsPerPage = resultsPerPage;
	}

	public int getMaxPages() {
		return maxPages;
	}

	public void setMaxPages(int maxPages) {
		this.maxPages = maxPages;
	}

	public String getFinderUniprotSource() {
		return finderUniprotSource.name();
	}

	public void setFinderUniprotSource(String finderUniprotSource) {
		this.finderUniprotSource = UniprotSource.valueOf(finderUniprotSource);
	}

	public String getRetrieverUniprotSource() {
		return retrieverUniprotSource.name();
	}

	public void setRetrieverUniprotSource(String retrieverUniprotSource) {
		this.retrieverUniprotSource = UniprotSource.valueOf(retrieverUniprotSource);
	}

	public String getUniprotImplementation() {
		return uniprotImplementation.name();
	}

	public void setUniprotImplementation(String imp) {
		this.uniprotImplementation = UniprotImplementation.valueOf(imp);
	}

	/**
	 * {@inheritDoc}
	 * <br>
	 * This implementation understands size as the number of search terms,
	 * not their related search results.
	 */
	public void setSearchCacheSize(int size) {
		searchCacheSize = size;
	}

	public int getSearchCacheSize() {
		return searchCacheSize;
	}

	public String getMmDatasource() {
		return mmDatasource;
	}

	public void setMmDatasource(String mmDatasource) {
		this.mmDatasource = mmDatasource;
	}

    public int getMaxMoleculesPerGroup() {
        return maxMoleculesPerGroup;
    }

    public void setMaxMoleculesPerGroup(int maxMoleculesPerGroup) {
        this.maxMoleculesPerGroup = maxMoleculesPerGroup;
    }

    public String getStructureSearchUrl() {
        return structureSearchUrl;
    }

    public void setStructureSearchUrl(String structureSearchUrl) {
        this.structureSearchUrl = structureSearchUrl;
    }
    
}
