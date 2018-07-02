/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.base.search;

import java.util.ArrayList;
import java.util.List;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import uk.ac.ebi.ep.data.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.data.enzyme.model.ProteinStructure;
import uk.ac.ebi.ep.data.search.model.Disease;
import uk.ac.ebi.ep.enzymeservices.chebi.IChebiAdapter;
import uk.ac.ebi.ep.enzymeservices.intenz.IntenzAdapter;

/**
 *
 * @author joseph
 */
public class EnzymeRetrieverTest extends BaseTest {

    private static final String uniprotAccession = "Q07973";

    /**
     * Test of getChebiAdapter method, of class EnzymeRetriever.
     */
    @Test
    public void testGetChebiAdapter() {
        System.out.println("getChebiAdapter");
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
        EnzymeModel expResult = new EnzymeModel();
        expResult.setAccession(uniprotAccession);
        EnzymeModel result = enzymeRetriever.getEnzyme(uniprotAccession);
        assertEquals(expResult.getAccession(), result.getAccession());
        assertNotNull(result.getName());

    }

    /**
     * Test of getProteinStructure method, of class EnzymeR
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetProteinStructure() throws Exception {

        String accession = "Q09128";
        List<ProteinStructure> proteinstructure = new ArrayList<>();
        ProteinStructure structure = new ProteinStructure();
        structure.setId("3k9v");
        structure.setName("Crystal structure of rat mitochondrial P450 24A1 S57D in complex with CHAPS");
        structure.setDescription("Crystal structure of rat mitochondrial P450 24A1 S57D in complex with CHAPS");

 
        ProteinStructure structure2 = new ProteinStructure();
        structure2.setId("4k9v");
        structure2.setName("Crystal structure of rat mitochondrial P450 24A1 S57D in complex with CHAPS");
        structure2.setDescription("Crystal structure of rat mitochondrial P450 24A1 S57D in complex with CHAPS");

        
        
        proteinstructure.add(structure);
         proteinstructure.add(structure2);

        EnzymeModel expResult = new EnzymeModel();
        expResult.setAccession(accession);
        expResult.setName("1,25-dihydroxyvitamin D(3) 24-hydroxylase, mitochondrial");
        expResult.setProteinstructure(proteinstructure);
        expResult.setProteinName("1,25-dihydroxyvitamin D(3) 24-hydroxylase, mitochondrial");
        expResult.setScientificName("Rattus norvegicus");
        expResult.setCommonName("Rat");

        EnzymeModel result = enzymeRetriever.getProteinStructure(accession);
        
        assertEquals(expResult.getProteinstructure().size(), result.getProteinstructure().size());
        assertNotNull(result);

    }

    /**
     * Test of getDiseases method, of class EnzymeRetriever.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetDiseases() throws Exception {

        EnzymeModel expResult = new EnzymeModel();

        Disease disease = new Disease("D006934", "hypercalcemia");

        List<Disease> diseases = new ArrayList<>();
        diseases.add(disease);

        expResult.setDisease(diseases);

        EnzymeModel result = enzymeRetriever.getDiseases(uniprotAccession);
        assertEquals(expResult.getDisease().stream().findFirst().get(), result.getDisease().stream().findAny().get());

    }

    @Test
    public void testGetLiterature() throws Exception {

        String uniprotAccession1 = "O76074";
        int limit = 10;
        EnzymeModel result = enzymeRetriever.getLiterature(uniprotAccession1, limit);

        assertNotNull(result.getLiterature().size());

        assertNotNull(result);
        assertThat(result.getLiterature(), hasSize(greaterThan(1)));
        assertThat(result.getLiterature(), hasSize(lessThanOrEqualTo(limit)));

    }

    /**
     * Test of getReactionsPathways method, of class EnzymeRetriever.
     *
     * @throws java.lang.Exception
     */
//    @Test
//    public void testGetReactionsPathways() throws Exception {
//        EnzymeModel expResult = new EnzymeModel();
//
//        List<ReactionPathway> reactionPathway = new ArrayList<>();
//
//        List<EnzymeReaction> reactions = new ArrayList<>();
//        EnzymeReaction reaction = new EnzymeReaction("RHEA:24967", "calcitriol + H(+) + NADPH + O2 <=> calcitetrol + H2O + NADP(+)");
//        reactions.add(reaction);
//
//        List<Pathway> pathways = new ArrayList<>();
//        Pathway pathway = new Pathway("REACT_23767 ", "cGMP effects");
//
//        pathways.add(pathway);
//
//        expResult.setPathways(pathways);
//
//        ReactionPathway rp = new ReactionPathway();
//        rp.setReactions(reactions);
//        rp.setPathways(pathways);
//
//        reactionPathway.add(rp);
//
//        expResult.setReactionpathway(reactionPathway);
//
//        EnzymeModel result = enzymeRetriever.getReactionsPathways("Q8CHX6");
//        assertNotNull(result);
//        assertThat(result.getPathways(), hasSize(greaterThan(0)));
//    }

}
