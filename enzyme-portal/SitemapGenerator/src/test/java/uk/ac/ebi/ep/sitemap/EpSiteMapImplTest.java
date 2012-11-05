/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.sitemap;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import static org.junit.Assert.assertTrue;
import org.junit.*;

/**
 *
 * @author joseph
 */
public class EpSiteMapImplTest {

    private EpSiteMapImpl instance = null;
    private String fileDirectory = System.getProperty("user.home");
    private String filename = "SiteMapTest";

    public EpSiteMapImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        instance = new EpSiteMapImpl("ep-mm-db-enzdev");
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of generateSitemap method, of class EpSiteMapImpl.
     */
    @Test
    public void testGenerateSitemap_3args() throws Exception {
        System.out.println("generateSitemap");

        String filename_prefix = filename;
        Collection<String> inputData = new ArrayList<String>();
        for (int x = 0; x < 5000; x++) {

            inputData.add("Q8YE58");
            inputData.add("A8ADR3");
            inputData.add("B3R112");
        }

        File output = new File(fileDirectory);

        instance.generateSitemap(inputData, output, filename_prefix,false);


        System.out.println("Does File exists : " + output.exists());
        assertTrue(output.exists());
        //System.out.println("Passed!");



    }


}
