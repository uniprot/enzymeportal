/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.base.search;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import uk.ac.ebi.ep.data.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.enzymeservices.chebi.IChebiAdapter;
import uk.ac.ebi.ep.enzymeservices.intenz.IntenzAdapter;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class EnzymeRetrieverIT extends BaseTest {

    private static final String uniprotAccession ="Q07973";

    /**
     * Test of getChebiAdapter method, of class EnzymeRetriever.
     */
    @Test
    public void testGetChebiAdapter() {
        IChebiAdapter result = enzymeRetriever.getChebiAdapter();
        assertNotNull(result);
    }

    /**
     * Test of getIntenzAdapter method, of class EnzymeRetriever.
     */
    @Test
    public void testGetIntenzAdapter() {

        IntenzAdapter result = enzymeRetriever.getIntenzAdapter();
        assertNotNull(result);

    }

    /**
     * Test of getEnzyme method, of class EnzymeRetriever.
     */
    @Test
    public void testGetEnzyme() {
        EnzymeModel result = enzymeRetriever.getEnzyme(uniprotAccession);
        assertNotNull(result);
        assertNotNull(result.getEnzyme());

    }

    /**
     * Test of getProteinStructure method, of class EnzymeRetriever.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetProteinStructure() throws Exception {
        int expectedResult = 2;
        String accession = "Q09128";
        EnzymeModel result = enzymeRetriever.getProteinStructure(accession);
        assertNotNull(result);
        assertNotNull(result.getProteinstructure());

        assertThat(result.getProteinstructure(), hasSize(greaterThanOrEqualTo(expectedResult)));

    }

    /**
     * Test of getDiseases method, of class EnzymeRetriever.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetDiseases() throws Exception {
        int expectedResult = 1;
        EnzymeModel result = enzymeRetriever.getDiseases(uniprotAccession);
        assertNotNull(result);
        assertNotNull(result.getDisease());

        assertThat(result.getDisease(), hasSize(greaterThanOrEqualTo(expectedResult)));

        assertThat(result.getDisease(), hasSize(lessThanOrEqualTo(expectedResult)));
        assertThat(result.getDisease(), hasSize(expectedResult));
        
    }

    /**
     * Test of getLiterature method, of class EnzymeRetriever.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetLiterature() throws Exception {

        int limit = 50;

        EnzymeModel result = enzymeRetriever.getLiterature(uniprotAccession, limit);
        assertNotNull(result);
        assertNotNull(result.getLiterature());

        assertThat(result.getLiterature(), hasSize(greaterThanOrEqualTo(limit)));
    }

    /**
     * Test of getWholeModel method, of class EnzymeRetriever.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetWholeModel() throws Exception {

        EnzymeModel result = enzymeRetriever.getWholeModel(uniprotAccession);
        assertNotNull(result);
    }

    /**
     * Test of getMolecules method, of class EnzymeRetriever.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetMolecules() throws Exception {

        EnzymeModel result = enzymeRetriever.getMolecules(uniprotAccession);
        assertNotNull(result);
        assertNotNull(result.getMolecule());

    }

    /**
     * Test of getReactionsPathways method, of class EnzymeRetriever.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetReactionsPathways() throws Exception {
        int expectedResult = 7;
        EnzymeModel result = enzymeRetriever.getReactionsPathways(uniprotAccession);
        assertNotNull(result);
        assertNotNull(result.getReactionpathway());

        assertThat(result.getReactionpathway(), hasSize(greaterThanOrEqualTo(expectedResult)));
    }

}
