/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.adapter.chebi;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.chebi.webapps.chebiWS.model.Entity;
import uk.ac.ebi.ep.adapter.chebi.ChebiAdapter;
import static org.junit.Assert.*;

/**
 *
 * @author hongcao
 */
public class ChebiAdapterTest {
    private ChebiAdapter instance;
    public ChebiAdapterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
       instance = new ChebiAdapter();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of main method, of class ChebiAdapter.
     */
    @Test
    public void testQueryChebiNameForId() throws Exception {
        System.out.print("testQueryChebiNameForId");
        String name = "sildenafil";
        String expResult = "CHEBI:9139";
        String result = instance.queryChebiNameForId(name);
        assertEquals(expResult, result);
        System.out.println(": passed!");
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testQueryChebiAllNamesForEntities() throws Exception {
        System.out.print("testQueryChebiAllNamesForEntities");
        //"ADP-glucose" can only be tested in Chebi release in October, because
        //the current release does not have this synonym
        String[] expectedResultsForInhibitors = {"ADP", "phosphorylation","cGMP", "phosphatidic acid"};
        //ADP->CHEBI:16761, ATP->CHEBI:15422, Sildenafil->CHEBI:9139,null->phosphorylation, cGMP->CHEBI:16356
        //String[] expectedResults = {"ADP",
        Set<String> uniqueNames = new LinkedHashSet<String>(Arrays.asList(expectedResultsForInhibitors));
        Map<String,Entity> result = instance.queryChebiAllNamesForEntities(uniqueNames);
        String expChebiId = "CHEBI:16761";
        Entity entity = result.get("ADP");
        String actualChebiId = entity.getChebiId();
        //exact chebi name
        assertEquals(expChebiId, actualChebiId);

        //not found in chebi
        String expEntity = null;
        entity = result.get("phosphorylation");
        //actualChebiId = entity.getChebiId();
        assertEquals(expEntity, entity);

        //found in synonyms
        expChebiId = "CHEBI:16356";
        entity = result.get("cGMP");
        actualChebiId = entity.getChebiId();
        assertEquals(expChebiId, actualChebiId);
        System.out.println(": passed!");

        expChebiId = "CHEBI:16337";
        entity = result.get("phosphatidic acid");
        actualChebiId = entity.getChebiId();
        assertEquals(expChebiId, actualChebiId);
        System.out.println(": passed!");

    }
}