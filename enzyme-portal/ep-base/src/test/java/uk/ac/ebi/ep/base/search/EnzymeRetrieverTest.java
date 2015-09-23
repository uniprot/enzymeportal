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
import org.junit.Test;
import uk.ac.ebi.ep.adapter.literature.ILiteratureAdapter;
import uk.ac.ebi.ep.data.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.data.enzyme.model.EnzymeReaction;
import uk.ac.ebi.ep.data.enzyme.model.Pathway;
import uk.ac.ebi.ep.data.enzyme.model.ProteinStructure;
import uk.ac.ebi.ep.data.enzyme.model.ReactionPathway;
import uk.ac.ebi.ep.data.search.model.Disease;
import uk.ac.ebi.ep.enzymeservices.chebi.IChebiAdapter;
import uk.ac.ebi.ep.enzymeservices.intenz.IntenzAdapter;

/**
 *
 * @author joseph
 */
//@Ignore
public class EnzymeRetrieverTest extends BaseTest {

    private static final String uniprotAccession = "Q07973";



    /**
     * Test of getChebiAdapter method, of class EnzymeRetriever.
     */
    @Test
    public void testGetChebiAdapter() {
        System.out.println("getChebiAdapter");
             IChebiAdapter result = instance.getChebiAdapter();
        assertNotNull(result);
    }

    /**
     * Test of getLiteratureAdapter method, of class EnzymeRetriever.
     */
    @Test
    public void testGetLiteratureAdapter() {
        System.out.println("getLiteratureAdapter");
        EnzymeRetriever instance = new EnzymeRetriever(service, ebeyeService);
        ILiteratureAdapter result = instance.getLiteratureAdapter();
        assertNotNull(result);
    }

    /**
     * Test of getIntenzAdapter method, of class EnzymeRetriever.
     */
    @Test
    public void testGetIntenzAdapter() {
        System.out.println("getIntenzAdapter");
        EnzymeRetriever instance = new EnzymeRetriever(service, ebeyeService);
        IntenzAdapter result = instance.getIntenzAdapter();
        assertNotNull(result);
    }

    /**
     * Test of getEnzyme method, of class EnzymeRetriever.
     */
    @Test
    public void testGetEnzyme() {
        System.out.println("getEnzyme");

        EnzymeModel expResult = new EnzymeModel();
        expResult.setAccession(uniprotAccession);

        EnzymeRetriever instance = new EnzymeRetriever(service, ebeyeService);
        EnzymeModel result = instance.getEnzyme(uniprotAccession);
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
        System.out.println("getProteinStructure");

        String accession = "Q09128";
        List<ProteinStructure> proteinstructure = new ArrayList<>();
        ProteinStructure structure = new ProteinStructure();
        structure.setId("3k9v");
        structure.setName("Crystal structure of rat mitochondrial P450 24A1 S57D in complex with CHAPS");
        structure.setDescription("Crystal structure of rat mitochondrial P450 24A1 S57D in complex with CHAPS");

        proteinstructure.add(structure);

        EnzymeModel expResult = new EnzymeModel();
        expResult.setAccession(accession);
        expResult.setName("1,25-dihydroxyvitamin D(3) 24-hydroxylase, mitochondrial");
        expResult.setProteinstructure(proteinstructure);
        expResult.setProteinName("1,25-dihydroxyvitamin D(3) 24-hydroxylase, mitochondrial");
        expResult.setScientificName("Rattus norvegicus");
        expResult.setCommonName("Rat");

        EnzymeRetriever instance = new EnzymeRetriever(service, ebeyeService);
        EnzymeModel result = instance.getProteinStructure(accession);

        //result.setProteinstructure(proteinstructure);
        assertEquals(expResult, result);
        assertNotNull(result);
       
        //assertEquals(expResult.getProteinstructure().stream().findAny().get().getId(), result.getProteinstructure().stream().findAny().get().getId());

    }

    /**
     * Test of getDiseases method, of class EnzymeRetriever.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetDiseases() throws Exception {
        System.out.println("getDiseases");
        //String uniprotAccession = "P49768";

        EnzymeModel expResult = new EnzymeModel();

        Disease disease = new Disease("D006934", "hypercalcemia");

        List<Disease> diseases = new ArrayList<>();
        diseases.add(disease);

        expResult.setDisease(diseases);

        EnzymeRetriever instance = new EnzymeRetriever(service, ebeyeService);
        EnzymeModel result = instance.getDiseases(uniprotAccession);
        assertEquals(expResult.getDisease().stream().findFirst().get(), result.getDisease().stream().findAny().get());

    }
//
//    /**
//     * Test of getLiterature method, of class EnzymeRetriever.
//     *
//     * @throws java.lang.Exception
//     */
//    @Test
//    public void testGetLiterature() throws Exception {
//        System.out.println("getLiterature");
//
//        String uniprotAccession = "O76074";
//        EnzymeRetriever instance = new EnzymeRetriever(service, ebeyeService);
//        instance.getLiteratureAdapter().setConfig(literatureConfig());
//
//        EnzymeModel result = instance.getLiterature(uniprotAccession);
//
//        assertNotNull(result.getLiterature().size());
//
//    }
//
//    /**
//     * Test of getWholeModel method, of class EnzymeRetriever.
//     *
//     * @throws java.lang.Exception
//     */
//    @Test
//    public void testGetWholeModel() throws Exception {
//        System.out.println("getWholeModel");
//        String acc = "O76074";
//        EnzymeRetriever instance = new EnzymeRetriever(service, ebeyeService);
//
//        instance.getIntenzAdapter().setConfig(intenzConfig());
//        instance.getReactomeAdapter().setConfig(reactomeConfig());
//        instance.getChebiAdapter().setConfig(chebiConfig());
//
//        EnzymeModel result = instance.getWholeModel(acc);
//        assertNotNull(result);
//
//    }
//
//    /**
//     * Test of getMolecules method, of class EnzymeRetriever.
//     *
//     * @throws java.lang.Exception
//     */
//    @Test
//    public void testGetMolecules() throws Exception {
//        System.out.println("getMolecules");
//        String uniprotAccession = "O76074";
//        EnzymeRetriever instance = new EnzymeRetriever(service, ebeyeService);
//        instance.getChebiAdapter().setConfig(chebiConfig());
//
//        EnzymeModel expResult = new EnzymeModel();
//
//        ChemicalEntity chemicalEntity = new ChemicalEntity();
//
//        CountableMolecules molecules = new CountableMolecules();
//        molecules.setTotalFound(1);
//
//        chemicalEntity.setInhibitors(molecules);
//
//        expResult.setMolecule(chemicalEntity);
//
//        EnzymeModel result = instance.getMolecules(uniprotAccession);
//
//        assertEquals(expResult.getMolecule().getInhibitors().getTotalFound(), result.getMolecule().getInhibitors().getTotalFound());
//
//    }

    /**
     * Test of getReactionsPathways method, of class EnzymeRetriever.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetReactionsPathways() throws Exception {
        System.out.println("getReactionsPathways");
        //String uniprotAccession = "O76074";
        EnzymeRetriever instance = new EnzymeRetriever(service, ebeyeService);

        EnzymeModel expResult = new EnzymeModel();

        List<ReactionPathway> reactionPathway = new ArrayList<>();

        List<EnzymeReaction> reactions = new ArrayList<>();
        EnzymeReaction reaction = new EnzymeReaction("RHEA:24967", "calcitriol + H(+) + NADPH + O2 <=> calcitetrol + H2O + NADP(+)");
        reactions.add(reaction);

        List<Pathway> pathways = new ArrayList<>();
        Pathway pathway = new Pathway("REACT_23767 ", "cGMP effects");

        pathways.add(pathway);

        expResult.setPathways(pathways);

        ReactionPathway rp = new ReactionPathway();
        rp.setReactions(reactions);
        rp.setPathways(pathways);

        reactionPathway.add(rp);

        expResult.setReactionpathway(reactionPathway);

        EnzymeModel result = instance.getReactionsPathways("P63086");
        
        //assertEquals(expResult.getPathways().stream().findAny().get().getPathwayId().trim(), result.getPathways().stream().findAny().get().getPathwayId().trim());
    }


}
