/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.sitemap;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.apache.log4j.Logger;
import org.sitemaps.ObjectFactory;
import org.sitemaps.Url;
import org.sitemaps.Urlset;
import org.xml.sax.SAXException;
import uk.ac.ebi.ep.exception.*;
import uk.ac.ebi.ep.mm.MmDatabase;

/**
 *This class is the implementation of the ISiteMap. it uses the Resources from the 
 * super class to retrieve relevant data from the database to generate a siteMap.
 * @author joseph
 */
public class SiteMapImpl extends SiteMapResources<File> {

    private final Logger LOGGER = Logger.getLogger(SiteMapImpl.class);
    private static final String ENZYME_PORTAL_URL = "http://www.ebi.ac.uk/enzymeportal";
    private static final String ENZYME_TAB = String.format("%s/%s", ENZYME_PORTAL_URL, "search/{0}/enzyme");
    private static final String PROTEIN_STRUCTURE_TAB = String.format("%s/%s", ENZYME_PORTAL_URL, "search/{0}/proteinStructure");
    private static final String REACTION_PATHWAYS_TAB = String.format("%s/%s", ENZYME_PORTAL_URL, "search/{0}/reactionsPathways");
    private static final String MOLECULES_TAB = String.format("%s/%s", ENZYME_PORTAL_URL, "search/{0}/molecules");
    private static final String DISEASE_TAB = String.format("%s/%s", ENZYME_PORTAL_URL, "search/{0}/diseaseDrugs");
    private static final String LITERATURE_TAB = String.format("%s/%s", ENZYME_PORTAL_URL, "search/{0}/literature");
    private ObjectFactory objectFactory;
    private Marshaller marshaller;
    private final String CHANGE_FREQ = "weekly";
    private static String[] tabs = {ENZYME_TAB, PROTEIN_STRUCTURE_TAB, REACTION_PATHWAYS_TAB, MOLECULES_TAB, DISEASE_TAB, LITERATURE_TAB};

    /**
     * constructing this class Object using the following parameters.
     * @param dbConfig the database configuration filename
     */
    public SiteMapImpl(String dbConfig) {
        super(dbConfig);
        init();

    }

    private void init() {
        try {
            JAXBContext context = JAXBContext.newInstance("org.sitemaps");
            marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            Schema sitemapXsd = SchemaFactory.newInstance(
                    XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(
                    SiteMapImpl.class.getClassLoader().getResource("sitemap.xsd"));
            marshaller.setSchema(sitemapXsd);
            objectFactory = new ObjectFactory();
        } catch (SAXException ex) {
            LOGGER.fatal("SAXException while parsing the sitemap schema", ex);
        } catch (JAXBException ex) {
            LOGGER.fatal("JAXBException while marshalling the sitemap schema", ex);
        }
    }

    /**
     * this method takes an input to generate a siteMap
     * @param inputData collection of data to be used in generating the siteMap
     * @param output  the siteMap generated
     * @throws EnzymePortalException while generating or writing the siteMap
     */
    public void generateSitemap(Collection<?> inputData, File output) throws SiteMapException {

        Urlset urlset = objectFactory.createUrlset();
        Url enzymePortalUrl = objectFactory.createUrl();
        enzymePortalUrl.setLoc(ENZYME_PORTAL_URL);
        enzymePortalUrl.setLastmod(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        enzymePortalUrl.setChangefreq(CHANGE_FREQ);
        enzymePortalUrl.setPriority(new BigDecimal(0.8).setScale(1, BigDecimal.ROUND_DOWN));
        urlset.getUrl().add(enzymePortalUrl);

        for (Object input : inputData) {
            for (String entry_tab : tabs) {
                Url pageUrl = objectFactory.createUrl();
                pageUrl.setLoc(MessageFormat.format(entry_tab,
                        new Object[]{input}));

                pageUrl.setChangefreq(CHANGE_FREQ);
                urlset.getUrl().add(pageUrl);
            }
        }
        try {
            marshaller.marshal(urlset, output);
            makeGzip(output);
        } catch (Exception ex) {
            throw new SiteMapException("JAXBException while marshalling the output", ex, Severity.SYSTEM_AFFECTING);
        }
    }

    /**
     * overloaded method to generated siteMap from the specified database
     * @param fileLocation the directory where the generated siteMap will be saved
     * @param filename the filename of the generated siteMap
     * @throws SiteMapException if the siteMap cannot be generated.
     */
    public void generateSitemap(String fileDirectory, String filename) throws SiteMapException {
        File outputStream = null;
        try {
            outputStream = exportFile(fileDirectory, filename);
            List<String> input = getAccessions(MmDatabase.UniProt);
            this.generateSitemap(input, outputStream);
        } catch (FileNotFoundException ex) {
            LOGGER.error(String.format("This File %s and/or %s cannot be found", fileDirectory, filename), ex);
        } catch (IOException ex) {

            LOGGER.error(String.format("Error in creacting these Files %s and/or %s ", fileDirectory, filename), ex);
        } finally {
            outputStream.delete();

        }
    }
   
    private List<String> getAccessions(MmDatabase database) {
        List<String> accessions = null;
        if (accessions == null) {
            accessions = new ArrayList<String>();
        }
        accessions = this.getMegaMapper().getAllUniProtAccessions(database);

        return accessions;
    }

    /**
     * this method is for exporting the generated siteMap
     * @param fileDirectory the directory where the siteMap will be saved
     * @param filename the name of the siteMap file
     * @return OutputStream
     * @throws FileNotFoundException if file cannot be found in the directory
     * @throws IOException if siteMap cannot be exported to the directory
     */
    @Override
    public File exportFile(String fileDirectory, String filename) throws FileNotFoundException, IOException {
        try {

            checkWriteableDirectory(fileDirectory);
        } catch (SiteMapException ex) {
            LOGGER.warn(String.format("This directory %s is not Writeable", fileDirectory), ex);
        }
        String output = String.format("%s/%s.xml", fileDirectory, filename);
        File filestream = new File(output);
        return filestream;
    }
}
