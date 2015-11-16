/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.sitemap;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import javax.transaction.Transactional;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import uk.ac.ebi.ep.data.service.UniprotEntryService;
import uk.ac.ebi.ep.sitemap.generator.EnzymePortalSiteMap;
import uk.ac.ebi.ep.sitemap.generator.SiteMapGenerator;

/**
 *
 * @author joseph
 */
//@Ignore
@Transactional
public class EpSiteMapImplTest {

    private SiteMapGenerator instance = null;
    private final String fileDirectory = System.getProperty("user.home");
    private final String filename = "SiteMapTest";
    
    @Before
    public void setUp() {

        instance = new EnzymePortalSiteMap(uniprotEntryService());
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of generateSitemap method, of class EpSiteMapImpl.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGenerateSitemap_3args() throws Exception {
        System.out.println("generateSitemap");

        String filename_prefix = filename;
        Collection<String> inputData = new ArrayList<>();
        for (int x = 0; x < 5000; x++) {

            inputData.add("Q8YE58");
            inputData.add("A8ADR3");
            inputData.add("B3R112");
        }

        File output = new File(fileDirectory);

        instance.generateSitemap(inputData, output, filename_prefix, true);
        assertTrue(output.exists());

    }
    
    @Bean
    public UniprotEntryService uniprotEntryService(){
        return new UniprotEntryService();
    }

}
