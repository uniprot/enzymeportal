/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.sitemap;

import java.io.File;
import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.*;
import javax.xml.bind.Marshaller;
import org.apache.log4j.Logger;
import org.sitemaps.ObjectFactory;
import uk.ac.ebi.ep.exception.EnzymePortalException;
import uk.ac.ebi.ep.mm.MmDatabase;
import uk.ac.ebi.ep.sitemap.advanced.W3CDateFormat.Pattern;
import uk.ac.ebi.ep.sitemap.advanced.*;

/**
 * This class is the implementation of the ISiteMap. it uses the Resources from
 * the super class to retrieve relevant data from the database to generate a
 * siteMap.
 *
 * @author joseph
 */
public class EpSiteMapImpl extends SiteMapResources<File> {

    private final Logger LOGGER = Logger.getLogger(EpSiteMapImpl.class);
    private static final String SITEMAP_INDEX = "sitemap_index.xml";
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
     *
     * @param dbConfig the database configuration filename
     */
    public EpSiteMapImpl(String dbConfig) {
        super(dbConfig);


    }

    /**
     * this method takes an input to generate a siteMap
     *
     * @param inputData collection of data to be used in generating the siteMap
     * @param output the siteMap generated
     * @param testMode this is to determine is we are testing or generating the
     * real siteMap. this is to enable deleting generated files from local
     * machine
     * @throws EnzymePortalException while generating or writing the siteMap
     */
    public void generateSitemap(Collection<?> inputData, File output, String filename_prefix, boolean testMode) throws SiteMapException {
        WebSitemapGenerator sitemapGenerator;
        WebSitemapUrl url = null;
        SitemapIndexGenerator indexGenerator;

        // Use DAY pattern (2012-07-13), Greenwich Mean Time timezone
        W3CDateFormat dateFormat = new W3CDateFormat(Pattern.DAY);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        try {
            indexGenerator = new SitemapIndexGenerator(ENZYME_TAB, output);
            sitemapGenerator = WebSitemapGenerator.builder(ENZYME_PORTAL_URL, output).fileNamePrefix(filename_prefix).gzip(true).dateFormat(dateFormat).build();
            // sitemapGenerator = WebSitemapGenerator.builder(ENZYME_PORTAL_URL, output).fileNamePrefix(FILENAME_PREFIX).gzip(true).dateFormat(dateFormat).build();
            for (Object input : inputData) {

                for (String entry_tab : tabs) {

                    url = new WebSitemapUrl.Options(MessageFormat.format(entry_tab, new Object[]{input})).lastMod(new Date()).priority(1.0).changeFreq(ChangeFreq.HOURLY).build();
                    sitemapGenerator.addUrl(url);
                }



            }
            sitemapGenerator.write();
            sitemapGenerator.writeSitemapsWithIndex(SITEMAP_INDEX);

            if (testMode) {
                sitemapGenerator.deleteGeneratedSiteMap();
            }

        } catch (MalformedURLException ex) {
            LOGGER.fatal("MalformedURLException ", ex);
        }



    }

    /**
     * overloaded method to generated siteMap from the specified database
     *
     * @param fileLocation the directory where the generated siteMap will be
     * saved
     * @param filename the filename of the generated siteMap
     * @throws SiteMapException if the siteMap cannot be generated.
     */
    public void generateSitemap(String fileDirectory, String filename, boolean testMode) throws SiteMapException {

        checkWriteableDirectory(fileDirectory);
        File outputStream = new File(fileDirectory);
        List<String> input = getAccessions(MmDatabase.UniProt);
        this.generateSitemap(input, outputStream, filename, testMode);

    }

    private List<String> getAccessions(MmDatabase database) {
        List<String> accessions = null;
        if (accessions == null) {
            accessions = new ArrayList<String>();
        }
        accessions = this.getMegaMapper().getAllUniProtAccessions(database);

        return accessions;
    }
}
