/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.sitemap;

import org.junit.experimental.categories.Category;
import java.util.ArrayList;
import java.io.File;
import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 
 * @author joseph
 */
public class SiteMapImplTest {

    private SiteMapImpl instance = null;//new SiteMapImpl("ep-mm-db-enzdev");

    public SiteMapImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {

        instance = new SiteMapImpl("ep-mm-db-enzdev");
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of generateSitemap method, of class SiteMapImpl.
     */
    @Test
    public void testGenerateSitemap_Collection_File() throws Exception {
        System.out.println("generateSitemap colection input data");
        Collection<String> inputData = new ArrayList<String>();
        inputData.add("Q8YE58");
        inputData.add("A8ADR3");
        inputData.add("B3R112");

        String fileDirectory = System.getProperty("user.home");
        String filename = "EnzymePortalSiteMapTest";
        String output = String.format("%s/%s.xml", fileDirectory, filename);

        File outputData = new File(output);

        instance.generateSitemap(inputData, outputData);
        System.out.println("Does File exists : " + outputData.exists());
        assertTrue(outputData.exists());
        System.out.println("Passed!");
        outputData.delete();


    }
}
