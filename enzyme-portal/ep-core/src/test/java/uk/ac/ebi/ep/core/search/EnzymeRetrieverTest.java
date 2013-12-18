/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.core.search;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import uk.ac.ebi.ep.adapter.chebi.ChebiConfig;
import uk.ac.ebi.ep.adapter.reactome.ReactomeConfig;
import uk.ac.ebi.ep.adapter.uniprot.UniprotConfig;
import uk.ac.ebi.ep.core.search.IEnzymeFinder.UniprotImplementation;
import uk.ac.ebi.ep.core.search.IEnzymeFinder.UniprotSource;
import uk.ac.ebi.ep.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.enzyme.model.EnzymeReaction;
import uk.ac.ebi.ep.enzyme.model.ReactionPathway;

/**
 *
 * @author hongcao
 */
@Ignore
public class EnzymeRetrieverTest {
    private EnzymeRetriever instance;
     String uniprotAccession ="Q9NP56";// "Q08499";
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
        ReactomeConfig reactomeConfig = new ReactomeConfig();
        reactomeConfig.setTimeout(30000);
        reactomeConfig.setUseProxy(false);
        reactomeConfig.setWsBaseUrl("http://www.reactome.org:8080/ReactomeRESTfulAPI/RESTfulWS/queryById/");
        instance.getReactomeAdapter().setConfig(reactomeConfig);
        ChebiConfig chebiConfig = new ChebiConfig();
        instance.getChebiAdapter().setConfig(chebiConfig);
    }

    @After
    public void tearDown() {
    }
    
    /**
     * Test of getEnzyme method, of class EnzymeRetriever.
     */
//    @Test
//    public void testGetMolecules() throws Exception {
//        System.out.print("testGetMolecules");        
//        EnzymeModel result = instance.getMolecules(uniprotAccession);
//        List<Molecule> drugMols = result.getMolecule().getDrugs();
//        assertNotNull(drugMols);
//        System.out.println("testGetMolecules: passed!");
//    }

    @Test
    public void testGetReactionsPathways() throws Exception {
        System.out.print("testGetReactionsPathways");
        EnzymeModel result = instance.getReactionsPathways(uniprotAccession);        
        assertNotNull(result);
        System.out.println("testGetReactionsPathways: passed!");
    }
    
    @Test
    public void testGetPathwaysFromRheaXref() throws Exception {
    	ReactionPathway rp = new ReactionPathway();
    	// RHEA:13068, for P13569:
    	EnzymeReaction er = new EnzymeReaction();
    	er.getXrefs().addAll(Arrays.asList(new String[]{
    			"REACT_111034.1","REACT_111067.1","REACT_111156.1",
    			"REACT_111162.1","REACT_111164.1","REACT_111189.1",
    			"REACT_120947.1","REACT_121056.1","REACT_13622.1",
    			"REACT_13627.1","REACT_13681.2","REACT_15300.3","REACT_15322.2",
    			"REACT_22342.2","REACT_24963.1","REACT_25071.1","REACT_25155.1",
    			"REACT_25173.1","REACT_25268.1","REACT_25287.1","REACT_25301.1"
		}));
    	rp.setReaction(er);
    	List<ReactionPathway> rps =
    			instance.getPathwaysFromRheaXref(Collections.singletonList(rp));
    	assertTrue(rps.get(0).getPathways().size() > 0);
    	assertTrue(rps.get(0).getPathways().size() < 42);
    }
}