/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.biomart.adapter;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URLConnection;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import uk.ac.ebi.ep.enzyme.model.ReactionPathway;

/**
 *
 * @author hongcao
 */
public class BiomartAdapterTest {
    public static final String reactionStableId = "REACT_533";
    public static final String uniprotAccession = "Q08499";
    private BiomartAdapter instance;
    //public static final String[] expPathwayResults = {"REACT_567","REACT_2243","REACT_1725","REACT_734","REACT_152","REACT_383","REACT_21300"};
    public static final String[] expPathwayResults = {"REACT_567","REACT_2243"};
    public static final String[] expReactionResults = {"REACT_1257","REACT_19387"};

    public BiomartAdapterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        instance = new BiomartAdapter();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getPathwaysByReactionId method, of class BiomartAdapter.
     */
    @Test
    @Ignore("BioMart server returns error for this ID")
    public void testGetPathwaysByReactionId() throws Exception {
        List<String> results = instance.getPathwaysByReactionId(reactionStableId);
        assertArrayEquals(expPathwayResults, results.toArray());
    }
    
    @Test
    @Ignore("BioMart Server returns nothing for this accession")
    public void testGetPathwaysByUniprotAccession() throws Exception {
    	List<String> results = instance.getPathwaysByUniprotAccession("P30613");
    	assertNotNull(results);
    	assertTrue(results.size() > 0);
    }

    /**
     * Test of sendRequest method, of class BiomartAdapter.
     */
    @Test
    public void testSendRequest() throws Exception {
        String baseUrl = Transformer.getMessageTemplate("baseUrl");
        Object[] objs = {reactionStableId};
        String pwQuery = Transformer.getMessageTemplate("getPathwaysByReactionIdQuery", objs);
        Object[] objs1 = {pwQuery};
        String query = Transformer.getMessageTemplate("queryTpl", objs1);
        URLConnection uCon = instance.sendRequest(baseUrl, query);
        assertNotNull(uCon);
    }


    @Test
    public void testGetReactionsByUniprotAccession() throws Exception {
        List<ReactionPathway> results = instance.getReactionsByUniprotAccession(uniprotAccession);
        assertNotNull(results);
    }
}