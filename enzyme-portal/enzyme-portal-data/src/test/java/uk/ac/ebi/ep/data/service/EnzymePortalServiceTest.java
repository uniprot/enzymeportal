/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.service;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.ac.ebi.ep.data.domain.EnzymePortalCompound;
import uk.ac.ebi.ep.data.domain.EnzymePortalDisease;
import uk.ac.ebi.ep.data.domain.EnzymePortalEcNumbers;
import uk.ac.ebi.ep.data.domain.EnzymePortalPathways;
import uk.ac.ebi.ep.data.domain.EnzymePortalReaction;
import uk.ac.ebi.ep.data.domain.EnzymePortalSummary;
import uk.ac.ebi.ep.data.domain.RelatedProteins;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.domain.UniprotXref;
import uk.ac.ebi.ep.data.enzyme.model.EnzymeReaction;
import uk.ac.ebi.ep.data.enzyme.model.Pathway;
import uk.ac.ebi.ep.data.search.model.Compound;
import uk.ac.ebi.ep.data.search.model.Disease;
import uk.ac.ebi.ep.data.search.model.EcNumber;
import uk.ac.ebi.ep.data.search.model.Species;
import uk.ac.ebi.ep.data.search.model.Taxonomy;

/**
 *
 * @author joseph
 */
@Ignore
public class EnzymePortalServiceTest extends AbstractDataTest {
    
    public EnzymePortalServiceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of findByAccession method, of class EnzymePortalService.
     */
    @Test
    public void testFindByAccession() {
        System.out.println("findByAccession");
        String accession = "";
        EnzymePortalService instance = new EnzymePortalService();
        UniprotEntry expResult = null;
        UniprotEntry result = instance.findByAccession(accession);
        assertEquals(expResult, result);

    }

    /**
     * Test of findEnzymeSummaryByAccession method, of class EnzymePortalService.
     */
    @Test
    public void testFindEnzymeSummaryByAccession() {
        System.out.println("findEnzymeSummaryByAccession");
        String accession = "";
        EnzymePortalService instance = new EnzymePortalService();
        EnzymePortalSummary expResult = null;
        EnzymePortalSummary result = instance.findEnzymeSummaryByAccession(accession);
        assertEquals(expResult, result);

    }

    /**
     * Test of findAllUniprotAccessions method, of class EnzymePortalService.
     */
    @Test
    public void testFindAllUniprotAccessions() {
        System.out.println("findAllUniprotAccessions");
        EnzymePortalService instance = new EnzymePortalService();
        List<String> expResult = null;
        List<String> result = instance.findAllUniprotAccessions();
        assertEquals(expResult, result);

    }

    /**
     * Test of findAllDiseases method, of class EnzymePortalService.
     */
    @Test
    public void testFindAllDiseases() {
        System.out.println("findAllDiseases");
        EnzymePortalService instance = new EnzymePortalService();
        List<EnzymePortalDisease> expResult = null;
        List<EnzymePortalDisease> result = instance.findAllDiseases();
        assertEquals(expResult, result);

    }

    /**
     * Test of findCompoundsByUniprotAccession method, of class EnzymePortalService.
     */
    @Test
    public void testFindCompoundsByUniprotAccession() {
        System.out.println("findCompoundsByUniprotAccession");
        String accession = "";
        EnzymePortalService instance = new EnzymePortalService();
        List<EnzymePortalCompound> expResult = null;
        List<EnzymePortalCompound> result = instance.findCompoundsByUniprotAccession(accession);
        assertEquals(expResult, result);

    }

    /**
     * Test of findCompoundsByUniprotName method, of class EnzymePortalService.
     */
    @Test
    public void testFindCompoundsByUniprotName() {
        System.out.println("findCompoundsByUniprotName");
        String uniprotName = "";
        EnzymePortalService instance = new EnzymePortalService();
        List<EnzymePortalCompound> expResult = null;
        List<EnzymePortalCompound> result = instance.findCompoundsByUniprotName(uniprotName);
        assertEquals(expResult, result);

    }

    /**
     * Test of findCompoundsByNamePrefix method, of class EnzymePortalService.
     */
    @Test
    public void testFindCompoundsByNamePrefix() {
        System.out.println("findCompoundsByNamePrefix");
        List<String> namePrefixes = null;
        EnzymePortalService instance = new EnzymePortalService();
        List<EnzymePortalCompound> expResult = null;
        List<EnzymePortalCompound> result = instance.findCompoundsByNamePrefix(namePrefixes);
        assertEquals(expResult, result);

    }

    /**
     * Test of findEnzymesByAccession method, of class EnzymePortalService.
     */
    @Test
    public void testFindEnzymesByAccession() {
        System.out.println("findEnzymesByAccession");
        String accession = "";
        EnzymePortalService instance = new EnzymePortalService();
        List<UniprotEntry> expResult = null;
        List<UniprotEntry> result = instance.findEnzymesByAccession(accession);
        assertEquals(expResult, result);

    }

    /**
     * Test of findEnzymesByAccessions method, of class EnzymePortalService.
     */
    @Test
    public void testFindEnzymesByAccessions_List() {
        System.out.println("findEnzymesByAccessions");
        List<String> accessions = null;
        EnzymePortalService instance = new EnzymePortalService();
        List<UniprotEntry> expResult = null;
        List<UniprotEntry> result = instance.findEnzymesByAccessions(accessions);
        assertEquals(expResult, result);
 
    }

    /**
     * Test of findEnzymeSummariesByNamePrefixes method, of class EnzymePortalService.
     */
    @Test
    public void testFindEnzymeSummariesByNamePrefixes() {
        System.out.println("findEnzymeSummariesByNamePrefixes");
        List<String> namePrefixes = null;
        EnzymePortalService instance = new EnzymePortalService();
        List<EnzymePortalSummary> expResult = null;
        List<EnzymePortalSummary> result = instance.findEnzymeSummariesByNamePrefixes(namePrefixes);
        assertEquals(expResult, result);
 
    }

    /**
     * Test of findEnzymeSumariesByAccessions method, of class EnzymePortalService.
     */
    @Test
    public void testFindEnzymeSumariesByAccessions_List() {
        System.out.println("findEnzymeSumariesByAccessions");
        List<String> accessions = null;
        EnzymePortalService instance = new EnzymePortalService();
        List<EnzymePortalSummary> expResult = null;
        List<EnzymePortalSummary> result = instance.findEnzymeSumariesByAccessions(accessions);
        assertEquals(expResult, result);
   
    }

    /**
     * Test of findEnzymeSumariesByAccession method, of class EnzymePortalService.
     */
    @Test
    public void testFindEnzymeSumariesByAccession() {
        System.out.println("findEnzymeSumariesByAccession");
        String accession = "";
        EnzymePortalService instance = new EnzymePortalService();
        List<EnzymePortalSummary> expResult = null;
        List<EnzymePortalSummary> result = instance.findEnzymeSumariesByAccession(accession);
        assertEquals(expResult, result);

    }

    /**
     * Test of findCatalyticActivitiesByAccession method, of class EnzymePortalService.
     */
    @Test
    public void testFindCatalyticActivitiesByAccession() {
        System.out.println("findCatalyticActivitiesByAccession");
        String accession = "";
        EnzymePortalService instance = new EnzymePortalService();
        List<String> expResult = null;
        List<String> result = instance.findCatalyticActivitiesByAccession(accession);

    }

    /**
     * Test of findEnzymeSumariesByAccessions method, of class EnzymePortalService.
     */
    @Test
    public void testFindEnzymeSumariesByAccessions_List_Pageable() {
        System.out.println("findEnzymeSumariesByAccessions");
        List<String> accessions = null;
        Pageable pageable = null;
        EnzymePortalService instance = new EnzymePortalService();
        Page<EnzymePortalSummary> expResult = null;
        Page<EnzymePortalSummary> result = instance.findEnzymeSumariesByAccessions(accessions, pageable);
        assertEquals(expResult, result);

    }

    /**
     * Test of findDiseasesByAccession method, of class EnzymePortalService.
     */
    @Test
    public void testFindDiseasesByAccession() {
        System.out.println("findDiseasesByAccession");
        String accession = "";
        EnzymePortalService instance = new EnzymePortalService();
        List<Disease> expResult = null;
        List<Disease> result = instance.findDiseasesByAccession(accession);
        assertEquals(expResult, result);

    }

    /**
     * Test of findDiseasesByNamePrefix method, of class EnzymePortalService.
     */
    @Test
    public void testFindDiseasesByNamePrefix() {
        System.out.println("findDiseasesByNamePrefix");
        List<String> namePrefixes = null;
        EnzymePortalService instance = new EnzymePortalService();
        List<EnzymePortalDisease> expResult = null;
        List<EnzymePortalDisease> result = instance.findDiseasesByNamePrefix(namePrefixes);
        assertEquals(expResult, result);

    }

    /**
     * Test of findPDBcodesByAccession method, of class EnzymePortalService.
     */
    @Test
    public void testFindPDBcodesByAccession() {
        System.out.println("findPDBcodesByAccession");
        String accession = "";
        EnzymePortalService instance = new EnzymePortalService();
        List<UniprotXref> expResult = null;
        List<UniprotXref> result = instance.findPDBcodesByAccession(accession);
        assertEquals(expResult, result);

    }

    /**
     * Test of findPathwaysByAccession method, of class EnzymePortalService.
     */
    @Test
    public void testFindPathwaysByAccession() {
        System.out.println("findPathwaysByAccession");
        String accession = "";
        EnzymePortalService instance = new EnzymePortalService();
        List<Pathway> expResult = null;
        List<Pathway> result = instance.findPathwaysByAccession(accession);
        assertEquals(expResult, result);

    }

    /**
     * Test of filterEnzymesInAccessions method, of class EnzymePortalService.
     */
    @Test
    public void testFilterEnzymesInAccessions() {
        System.out.println("filterEnzymesInAccessions");
        List<String> accessions = null;
        EnzymePortalService instance = new EnzymePortalService();
        List<String> expResult = null;
        List<String> result = instance.filterEnzymesInAccessions(accessions);
        assertEquals(expResult, result);

    }

    /**
     * Test of findRelatedProteinsByNamePrefix method, of class EnzymePortalService.
     */
    @Test
    public void testFindRelatedProteinsByNamePrefix() {
        System.out.println("findRelatedProteinsByNamePrefix");
        String nameprefix = "";
        EnzymePortalService instance = new EnzymePortalService();
        RelatedProteins expResult = null;
        RelatedProteins result = instance.findRelatedProteinsByNamePrefix(nameprefix);
        assertEquals(expResult, result);

    }

    /**
     * Test of findRelatedProteinsByNamePrefixes method, of class EnzymePortalService.
     */
    @Test
    public void testFindRelatedProteinsByNamePrefixes() {
        System.out.println("findRelatedProteinsByNamePrefixes");
        List<String> nameprefixes = null;
        EnzymePortalService instance = new EnzymePortalService();
        List<RelatedProteins> expResult = null;
        List<RelatedProteins> result = instance.findRelatedProteinsByNamePrefixes(nameprefixes);
        assertEquals(expResult, result);

    }

    /**
     * Test of findEnzymesByCompound method, of class EnzymePortalService.
     */
    @Test
    public void testFindEnzymesByCompound() {
        System.out.println("findEnzymesByCompound");
        String compoundId = "";
        EnzymePortalService instance = new EnzymePortalService();
        List<String> expResult = null;
        List<String> result = instance.findEnzymesByCompound(compoundId);
        assertEquals(expResult, result);

    }

    /**
     * Test of findDiseases method, of class EnzymePortalService.
     */
    @Test
    public void testFindDiseases() {
        System.out.println("findDiseases");
        EnzymePortalService instance = new EnzymePortalService();
        List<EnzymePortalDisease> expResult = null;
        List<EnzymePortalDisease> result = instance.findDiseases();
        assertEquals(expResult, result);
  
    }

    /**
     * Test of findAccessionsByEc method, of class EnzymePortalService.
     */
    @Test
    public void testFindAccessionsByEc() {
        System.out.println("findAccessionsByEc");
        String ecNumber = "";
        EnzymePortalService instance = new EnzymePortalService();
        List<String> expResult = null;
        List<String> result = instance.findAccessionsByEc(ecNumber);
        assertEquals(expResult, result);

    }

    /**
     * Test of findByEcNumbersByAccession method, of class EnzymePortalService.
     */
    @Test
    public void testFindByEcNumbersByAccession() {
        System.out.println("findByEcNumbersByAccession");
        String accession = "";
        EnzymePortalService instance = new EnzymePortalService();
        List<EnzymePortalEcNumbers> expResult = null;
        List<EnzymePortalEcNumbers> result = instance.findByEcNumbersByAccession(accession);
        assertEquals(expResult, result);

    }

    /**
     * Test of findReactions method, of class EnzymePortalService.
     */
    @Test
    public void testFindReactions() {
        System.out.println("findReactions");
        EnzymePortalService instance = new EnzymePortalService();
        List<EnzymePortalReaction> expResult = null;
        List<EnzymePortalReaction> result = instance.findReactions();
        assertEquals(expResult, result);

    }

    /**
     * Test of findPathways method, of class EnzymePortalService.
     */
    @Test
    public void testFindPathways() {
        System.out.println("findPathways");
        EnzymePortalService instance = new EnzymePortalService();
        List<EnzymePortalPathways> expResult = null;
        List<EnzymePortalPathways> result = instance.findPathways();
        assertEquals(expResult, result);

    }

    /**
     * Test of findAccessionsByReactionId method, of class EnzymePortalService.
     */
    @Test
    public void testFindAccessionsByReactionId() {
        System.out.println("findAccessionsByReactionId");
        String reactionId = "";
        EnzymePortalService instance = new EnzymePortalService();
        List<String> expResult = null;
        List<String> result = instance.findAccessionsByReactionId(reactionId);
        assertEquals(expResult, result);

    }

    /**
     * Test of findReactionsByAccession method, of class EnzymePortalService.
     */
    @Test
    public void testFindReactionsByAccession() {
        System.out.println("findReactionsByAccession");
        String accession = "";
        EnzymePortalService instance = new EnzymePortalService();
        List<EnzymeReaction> expResult = null;
        List<EnzymeReaction> result = instance.findReactionsByAccession(accession);
        assertEquals(expResult, result);

    }

    /**
     * Test of findAccessionsByPathwayId method, of class EnzymePortalService.
     */
    @Test
    public void testFindAccessionsByPathwayId() {
        System.out.println("findAccessionsByPathwayId");
        String pathwayId = "";
        EnzymePortalService instance = new EnzymePortalService();
        List<String> expResult = null;
        List<String> result = instance.findAccessionsByPathwayId(pathwayId);
        assertEquals(expResult, result);

    }

    /**
     * Test of findAccessionsByTaxId method, of class EnzymePortalService.
     */
    @Test
    public void testFindAccessionsByTaxId() {
        System.out.println("findAccessionsByTaxId");
        Long taxId = null;
        EnzymePortalService instance = new EnzymePortalService();
        List<String> expResult = null;
        List<String> result = instance.findAccessionsByTaxId(taxId);
        assertEquals(expResult, result);
 
    }

    /**
     * Test of getCountForOrganisms method, of class EnzymePortalService.
     */
    @Test
    public void testGetCountForOrganisms() {
        System.out.println("getCountForOrganisms");
        List<Long> taxids = null;
        EnzymePortalService instance = new EnzymePortalService();
        List<Taxonomy> expResult = null;
        List<Taxonomy> result = instance.getCountForOrganisms(taxids);
        assertEquals(expResult, result);

    }

    /**
     * Test of findEnzymesByTaxId method, of class EnzymePortalService.
     */
    @Test
    public void testFindEnzymesByTaxId() {
        System.out.println("findEnzymesByTaxId");
        Long taxId = null;
        EnzymePortalService instance = new EnzymePortalService();
        List<UniprotEntry> expResult = null;
        List<UniprotEntry> result = instance.findEnzymesByTaxId(taxId);
        assertEquals(expResult, result);

    }

    /**
     * Test of findEnzymesByTaxonomy method, of class EnzymePortalService.
     */
    @Test
    public void testFindEnzymesByTaxonomy() {
        System.out.println("findEnzymesByTaxonomy");
        Long taxId = null;
        Pageable pageable = null;
        EnzymePortalService instance = new EnzymePortalService();
        Page<UniprotEntry> expResult = null;
        Page<UniprotEntry> result = instance.findEnzymesByTaxonomy(taxId, pageable);
        assertEquals(expResult, result);

    }

    /**
     * Test of findEnzymesByAccessions method, of class EnzymePortalService.
     */
    @Test
    public void testFindEnzymesByAccessions_List_Pageable() {
        System.out.println("findEnzymesByAccessions");
        List<String> accessions = null;
        Pageable pageable = null;
        EnzymePortalService instance = new EnzymePortalService();
        Page<UniprotEntry> expResult = null;
        Page<UniprotEntry> result = instance.findEnzymesByAccessions(accessions, pageable);
        assertEquals(expResult, result);

    }

    /**
     * Test of findEnzymesByEcNumbers method, of class EnzymePortalService.
     */
    @Test
    public void testFindEnzymesByEcNumbers() {
        System.out.println("findEnzymesByEcNumbers");
        List<String> ecNumbers = null;
        Pageable pageable = null;
        EnzymePortalService instance = new EnzymePortalService();
        Page<UniprotEntry> expResult = null;
        Page<UniprotEntry> result = instance.findEnzymesByEcNumbers(ecNumbers, pageable);
        assertEquals(expResult, result);

    }

    /**
     * Test of findEnzymesByNamePrefixes method, of class EnzymePortalService.
     */
    @Test
    public void testFindEnzymesByNamePrefixes_List_Pageable() {
        System.out.println("findEnzymesByNamePrefixes");
        List<String> namePrefixes = null;
        Pageable pageable = null;
        EnzymePortalService instance = new EnzymePortalService();
        Page<UniprotEntry> expResult = null;
        Page<UniprotEntry> result = instance.findEnzymesByNamePrefixes(namePrefixes, pageable);
        assertEquals(expResult, result);

    }

    /**
     * Test of findEnzymesByNamePrefixes method, of class EnzymePortalService.
     */
    @Test
    public void testFindEnzymesByNamePrefixes_List() {
        System.out.println("findEnzymesByNamePrefixes");
        List<String> namePrefixes = null;
        EnzymePortalService instance = new EnzymePortalService();
        List<UniprotEntry> expResult = null;
        List<UniprotEntry> result = instance.findEnzymesByNamePrefixes(namePrefixes);
        assertEquals(expResult, result);

    }

    /**
     * Test of findEnzymesByPathwayId method, of class EnzymePortalService.
     */
    @Test
    public void testFindEnzymesByPathwayId() {
        System.out.println("findEnzymesByPathwayId");
        String pathwayId = "";
        EnzymePortalService instance = new EnzymePortalService();
        List<UniprotEntry> expResult = null;
        List<UniprotEntry> result = instance.findEnzymesByPathwayId(pathwayId);
        assertEquals(expResult, result);

    }

    /**
     * Test of findEnzymesByEc method, of class EnzymePortalService.
     */
    @Test
    public void testFindEnzymesByEc() {
        System.out.println("findEnzymesByEc");
        String ec = "";
        EnzymePortalService instance = new EnzymePortalService();
        List<UniprotEntry> expResult = null;
        List<UniprotEntry> result = instance.findEnzymesByEc(ec);
        assertEquals(expResult, result);

    }

    /**
     * Test of findPathwaysByName method, of class EnzymePortalService.
     */
    @Test
    public void testFindPathwaysByName() {
        System.out.println("findPathwaysByName");
        String pathwayName = "";
        EnzymePortalService instance = new EnzymePortalService();
        List<Pathway> expResult = null;
        List<Pathway> result = instance.findPathwaysByName(pathwayName);
        assertEquals(expResult, result);

    }

    /**
     * Test of findEnzymesByMeshId method, of class EnzymePortalService.
     */
    @Test
    public void testFindEnzymesByMeshId() {
        System.out.println("findEnzymesByMeshId");
        String meshId = "";
        EnzymePortalService instance = new EnzymePortalService();
        List<UniprotEntry> expResult = null;
        List<UniprotEntry> result = instance.findEnzymesByMeshId(meshId);
        assertEquals(expResult, result);

    }

    /**
     * Test of findDiseasesLike method, of class EnzymePortalService.
     */
    @Test
    public void testFindDiseasesLike() {
        System.out.println("findDiseasesLike");
        String diseaseName = "";
        EnzymePortalService instance = new EnzymePortalService();
        List<Disease> expResult = null;
        List<Disease> result = instance.findDiseasesLike(diseaseName);
        assertEquals(expResult, result);

    }

    /**
     * Test of findDiseasesByTaxId method, of class EnzymePortalService.
     */
    @Test
    public void testFindDiseasesByTaxId() {
        System.out.println("findDiseasesByTaxId");
        Long taxId = null;
        EnzymePortalService instance = new EnzymePortalService();
        List<Disease> expResult = null;
        List<Disease> result = instance.findDiseasesByTaxId(taxId);
        assertEquals(expResult, result);

    }

    /**
     * Test of findSpeciesByTaxId method, of class EnzymePortalService.
     */
    @Test
    public void testFindSpeciesByTaxId() {
        System.out.println("findSpeciesByTaxId");
        Long taxId = null;
        EnzymePortalService instance = new EnzymePortalService();
        List<Species> expResult = null;
        List<Species> result = instance.findSpeciesByTaxId(taxId);
        assertEquals(expResult, result);

    }

    /**
     * Test of findCompoundsByTaxId method, of class EnzymePortalService.
     */
    @Test
    public void testFindCompoundsByTaxId() {
        System.out.println("findCompoundsByTaxId");
        Long taxId = null;
        EnzymePortalService instance = new EnzymePortalService();
        List<Compound> expResult = null;
        List<Compound> result = instance.findCompoundsByTaxId(taxId);
        assertEquals(expResult, result);

    }

    /**
     * Test of filterBySpecieAndCompoundsAndDiseases method, of class EnzymePortalService.
     */
    @Test
    public void testFilterBySpecieAndCompoundsAndDiseases() {
        System.out.println("filterBySpecieAndCompoundsAndDiseases");
        Long taxId = null;
        List<String> compoudNames = null;
        List<String> diseaseNames = null;
        Pageable pageable = null;
        EnzymePortalService instance = new EnzymePortalService();
        Page<UniprotEntry> expResult = null;
        Page<UniprotEntry> result = instance.filterBySpecieAndCompoundsAndDiseases(taxId, compoudNames, diseaseNames, pageable);
        assertEquals(expResult, result);
  
    }

    /**
     * Test of filterBySpecieAndCompounds method, of class EnzymePortalService.
     */
    @Test
    public void testFilterBySpecieAndCompounds() {
        System.out.println("filterBySpecieAndCompounds");
        Long taxId = null;
        List<String> compoudNames = null;
        Pageable pageable = null;
        EnzymePortalService instance = new EnzymePortalService();
        Page<UniprotEntry> expResult = null;
        Page<UniprotEntry> result = instance.filterBySpecieAndCompounds(taxId, compoudNames, pageable);
        assertEquals(expResult, result);
  
    }

    /**
     * Test of filterBySpecieAndDiseases method, of class EnzymePortalService.
     */
    @Test
    public void testFilterBySpecieAndDiseases() {
        System.out.println("filterBySpecieAndDiseases");
        Long taxId = null;
        List<String> diseaseNames = null;
        Pageable pageable = null;
        EnzymePortalService instance = new EnzymePortalService();
        Page<UniprotEntry> expResult = null;
        Page<UniprotEntry> result = instance.filterBySpecieAndDiseases(taxId, diseaseNames, pageable);
        assertEquals(expResult, result);

    }

    /**
     * Test of filterBySpecie method, of class EnzymePortalService.
     */
    @Test
    public void testFilterBySpecie() {
        System.out.println("filterBySpecie");
        Long taxId = null;
        Pageable pageable = null;
        EnzymePortalService instance = new EnzymePortalService();
        Page<UniprotEntry> expResult = null;
        Page<UniprotEntry> result = instance.filterBySpecie(taxId, pageable);
        assertEquals(expResult, result);

    }

    /**
     * Test of findEnzymeFamiliesByTaxId method, of class EnzymePortalService.
     */
    @Test
    public void testFindEnzymeFamiliesByTaxId() {
        System.out.println("findEnzymeFamiliesByTaxId");
        Long taxId = null;
        EnzymePortalService instance = new EnzymePortalService();
        List<EcNumber> expResult = null;
        List<EcNumber> result = instance.findEnzymeFamiliesByTaxId(taxId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
