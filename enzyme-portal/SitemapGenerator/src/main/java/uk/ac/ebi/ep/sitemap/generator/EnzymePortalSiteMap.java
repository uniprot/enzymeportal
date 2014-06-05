/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.sitemap.generator;

import java.io.File;
import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.xml.bind.Marshaller;
import org.apache.log4j.Logger;
import org.sitemaps.ObjectFactory;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ep.data.service.UniprotEntryService;
import uk.ac.ebi.ep.sitemap.advanced.ChangeFreq;
import uk.ac.ebi.ep.sitemap.advanced.W3CDateFormat;
import uk.ac.ebi.ep.sitemap.advanced.WebSitemapGenerator;
import uk.ac.ebi.ep.sitemap.advanced.WebSitemapUrl;

/**
 *
 * @author joseph
 */
@Service
public class EnzymePortalSiteMap extends SiteMapGenerator<File> {
    
    private final Logger LOGGER = Logger.getLogger(EnzymePortalSiteMap.class);
    private static final String SITEMAP_INDEX = "sitemap_index.xml";
    private static final String SITEMAP_LOCATION = "http://www.ebi.ac.uk/enzymeportal/SiteMapServlet?sitemaps=";
    private static final String ENZYME_PORTAL_URL ="http://www.ebi.ac.uk/enzymeportal"; 
    private static final String ENZYME_TAB = String.format("%s/%s", ENZYME_PORTAL_URL, "search/{0}/enzyme");
    private static final String PROTEIN_STRUCTURE_TAB = String.format("%s/%s", ENZYME_PORTAL_URL, "search/{0}/proteinStructure");
    private static final String REACTION_PATHWAYS_TAB = String.format("%s/%s", ENZYME_PORTAL_URL, "search/{0}/reactionsPathways");
    private static final String MOLECULES_TAB = String.format("%s/%s", ENZYME_PORTAL_URL, "search/{0}/molecules");
    private static final String DISEASE_TAB = String.format("%s/%s", ENZYME_PORTAL_URL, "search/{0}/diseaseDrugs");
    private static final String LITERATURE_TAB = String.format("%s/%s", ENZYME_PORTAL_URL, "search/{0}/literature");
    private ObjectFactory objectFactory;
    private Marshaller marshaller;
    private final String CHANGE_FREQ = "weekly";
    private static final String[] tabs = {ENZYME_TAB, PROTEIN_STRUCTURE_TAB, REACTION_PATHWAYS_TAB, MOLECULES_TAB, DISEASE_TAB, LITERATURE_TAB};

     //@Autowired
    private final UniprotEntryService uniprotEntryService;
    
    /**
     * 
     *
     * @param uniprotEntryService
     */

    public EnzymePortalSiteMap(UniprotEntryService uniprotEntryService) {
        this.uniprotEntryService = uniprotEntryService;
    }
    
    

    /**
     * this method takes an input to generate a siteMap
     *
     * @param inputData collection of data to be used in generating the siteMap
     * @param output the siteMap generated
     * @param filename_prefix
     * @param testMode this is to determine is we are testing or generating the
     * real siteMap. this is to enable deleting generated files from local
     * machine
     * @throws uk.ac.ebi.ep.sitemap.generator.SiteMapException
     */
    @Override
    public void generateSitemap(Collection<?> inputData, File output, String filename_prefix, boolean testMode) throws SiteMapException {
        WebSitemapGenerator sitemapGenerator;
        WebSitemapUrl url = null;
        //SitemapIndexGenerator indexGenerator;

        // Use DAY pattern (2012-07-13), Greenwich Mean Time timezone
        W3CDateFormat dateFormat = new W3CDateFormat(W3CDateFormat.Pattern.DAY);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        try {
            //indexGenerator = new SitemapIndexGenerator(SITEMAP_LOCATION, output);
            //sitemapGenerator = WebSitemapGenerator.builder(SITEMAP_LOCATION, output).allowMultipleSitemaps(true).fileNamePrefix(filename_prefix).gzip(false).dateFormat(dateFormat).build();
             sitemapGenerator = WebSitemapGenerator.builder(SITEMAP_LOCATION, output).fileNamePrefix(filename_prefix).gzip(false).dateFormat(dateFormat).build();
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
     * @param fileDirectory
     * @param testMode
     * @param filename the filename of the generated siteMap
     * @throws SiteMapException if the siteMap cannot be generated.
     */
    @Override
    public void generateSitemap(String fileDirectory, String filename, boolean testMode) throws SiteMapException {

        checkWriteableDirectory(fileDirectory);
        File outputStream = new File(fileDirectory);
        List<String> input = getAccessions();
        this.generateSitemap(input, outputStream, filename, testMode);

    }

    private List<String> getAccessions() {

        return uniprotEntryService.findAllUniprotAccessions();
    }
}
