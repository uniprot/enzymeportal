/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.core.search;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.ebi.ep.adapter.uniprot.UniprotConfig;
import uk.ac.ebi.ep.core.search.IEnzymeFinder.UniprotImplementation;
import uk.ac.ebi.ep.core.search.IEnzymeFinder.UniprotSource;
import uk.ac.ebi.ep.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.enzyme.model.Molecule;

/**
 *
 * @author hongcao
 */
public class EnzymeRetrieverTest {
    private EnzymeRetriever instance;
    String uniprotAccession = "Q08499";
    public EnzymeRetrieverTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    	Config config = new Config();
    	config.setFinderUniprotSource(UniprotSource.EBEYE.name());
    	config.setRetrieverUniprotSource(UniprotSource.UNIPROT.name());
    	config.setResultsPerPage(10);
    	config.setMaxPages(1);
    	config.setUniprotImplementation(UniprotImplementation.JAPI.name());
        instance = new EnzymeRetriever(config);
        UniprotConfig uniprotConfig = new UniprotConfig();
        uniprotConfig.setReviewed(false);
        uniprotConfig.setTimeout(30);
        instance.uniprotAdapter.setConfig(uniprotConfig);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getEnzyme method, of class EnzymeRetriever.
     */
    @Test
    public void testGetMolecules() throws Exception {
        System.out.print("testGetMolecules");        
        EnzymeModel expResult = null;
        EnzymeModel result = instance.getMolecules(uniprotAccession);
        List<Molecule> drugMols = result.getMolecule().getDrugs();
        assertNotNull(drugMols);
        System.out.println("testGetMolecules: passed!");
    }

    @Test
    public void testGetReactionsPathways() throws Exception {
        System.out.print("testGetReactionsPathways");
        EnzymeModel expResult = null;
        EnzymeModel result = instance.getReactionsPathways(uniprotAccession);        
        assertNotNull(result);
        System.out.println("testGetReactionsPathways: passed!");
    }
}