package uk.ac.ebi.ep.data.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import uk.ac.ebi.ep.data.domain.EnzymePortalCompound;
import uk.ac.ebi.ep.data.domain.EnzymePortalDisease;
import uk.ac.ebi.ep.data.domain.EnzymePortalEcNumbers;
import uk.ac.ebi.ep.data.domain.EnzymePortalPathways;
import uk.ac.ebi.ep.data.domain.EnzymePortalReaction;
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
public class EnzymePortalServiceIT extends AbstractDataTest {

    private static final String ACCESSION = "Q64441";
    private static final String UNIPROT_ACCESSION = "O76074";
    private static final Pageable PAGEABLE = new PageRequest(0, 500, Sort.Direction.ASC, "function", "entryType");

    @Override
    protected void tearDown() throws Exception {
        dataSource.getConnection().close();
    }

    /**
     * Test of findByAccession method, of class EnzymePortalService.
     */
    @Test
    public void testFindByAccession() {
        LOGGER.info("findByAccession");

        UniprotEntry expResult = new UniprotEntry(ACCESSION);
        expResult.setCommonName("Mouse");
        expResult.setScientificName("Mus musculus");
        expResult.setProteinName("MOCK-1,25-dihydroxyvitamin D(3) 24-hydroxylase, mitochondrial");
        expResult.setName("CP24A_MOUSE");

        UniprotEntry result = enzymePortalService.findByAccession(ACCESSION);
        assertEquals(expResult, result);

    }

    /**
     * Test of findAllUniprotAccessions method, of class EnzymePortalService.
     */
    @Test
    public void testFindAllUniprotAccessions() {
        LOGGER.info("findAllUniprotAccessions");

        int expResult = 17;
        List<String> result = enzymePortalService.findAllUniprotAccessions();
        assertEquals(expResult, result.size());

    }

    /**
     * Test of findAllDiseases method, of class EnzymePortalService.
     */
    @Test
    public void testFindAllDiseases() {
        LOGGER.info("findAllDiseases");

        int expResult = 3;
        List<EnzymePortalDisease> result = enzymePortalService.findAllDiseases();

        assertEquals(expResult, result.size());

    }

    /**
     * Test of findCompoundsByUniprotAccession method, of class
     * EnzymePortalService.
     */
    @Test
    public void testFindCompoundsByUniprotAccession() {
        LOGGER.info("findCompoundsByUniprotAccession");

        int expResult = 1;
        List<EnzymePortalCompound> result = enzymePortalService.findCompoundsByUniprotAccession(UNIPROT_ACCESSION);
        assertEquals(expResult, result.size());

    }

    /**
     * Test of findCompoundsByUniprotName method, of class EnzymePortalService.
     */
    @Test
    public void testFindCompoundsByUniprotName() {
        LOGGER.info("findCompoundsByUniprotName");
        String uniprotName = "PDE5A";

        int expResult = 1;
        List<EnzymePortalCompound> result = enzymePortalService.findCompoundsByUniprotName(uniprotName);

        assertEquals(expResult, result.size());

    }

    /**
     * Test of findCompoundsByNamePrefix method, of class EnzymePortalService.
     */
    @Test
    public void testFindCompoundsByNamePrefix() {
        LOGGER.info("findCompoundsByNamePrefix");
        List<String> namePrefixes = new ArrayList<>();
        namePrefixes.add("PDE5A");
        namePrefixes.add("CP24A");
        namePrefixes.add("FAKE_NAME_PREFIX");

        int expResult = 1;
        List<EnzymePortalCompound> result = enzymePortalService.findCompoundsByNamePrefix(namePrefixes);

        assertEquals(expResult, result.size());

    }

    /**
     * Test of findEnzymesByAccession method, of class EnzymePortalService.
     */
    @Test
    public void testFindEnzymesByAccession() {
        LOGGER.info("findEnzymesByAccession");

        int expResult = 1;
        List<UniprotEntry> result = enzymePortalService.findEnzymesByAccession(UNIPROT_ACCESSION);

        assertEquals(expResult, result.size());

    }

    /**
     * Test of findEnzymesByAccessions method, of class EnzymePortalService.
     */
    @Test
    public void testFindEnzymesByAccessions_List() {
        LOGGER.info("findEnzymesByAccessions");
        List<String> accessions = new ArrayList<>();
        accessions.add("Q60991");
        accessions.add("Q63688");
        accessions.add("Q0III2");
        accessions.add("Q64441");
        accessions.add("fakeAccession");

        //int expResult = 4;
        List<UniprotEntry> result = enzymePortalService.findEnzymesByAccessions(accessions);

        assertTrue(result.size() > 1);

    }

    /**
     * Test of findEnzymeSummariesByNamePrefixes method, of class
     * EnzymePortalService.
     */
    /**
     * Test of findDiseasesByAccession method, of class EnzymePortalService.
     */
    @Test
    public void testFindDiseasesByAccession() {
        LOGGER.info("findDiseasesByAccession");
        String dAccession = "O75881";

        String expResult = "spastic paraplegia hereditary";
        List<Disease> result = enzymePortalService.findDiseasesByAccession(dAccession);
        assertEquals(expResult, result.stream().findAny().get().getName());
        assertEquals(1, result.size());

    }

    /**
     * Test of findDiseasesByNamePrefix method, of class EnzymePortalService.
     */
    @Test
    public void testFindDiseasesByNamePrefix() {
        LOGGER.info("findDiseasesByNamePrefix");
        List<String> namePrefixes = new ArrayList<>();
        namePrefixes.add("CP7B1");
        namePrefixes.add("CP8B1");
        namePrefixes.add("MBTP2");

        int expResult = 2;
        List<EnzymePortalDisease> result = enzymePortalService.findDiseasesByNamePrefix(namePrefixes);
        assertEquals(expResult, result.size());

    }

    /**
     * Test of findPDBcodesByAccession method, of class EnzymePortalService.
     */
    @Test
    public void testFindPDBcodesByAccession() {
        LOGGER.info("findPDBcodesByAccession");
        String pAccession = "Q09128";

        int expResult = 2;
        List<UniprotXref> result = enzymePortalService.findPDBcodesByAccession(pAccession);

        assertEquals(expResult, result.size());

    }

    /**
     * Test of findPathwaysByAccession method, of class EnzymePortalService.
     */
    @Test
    public void testFindPathwaysByAccession() {
        LOGGER.info("findPathwaysByAccession");
        String acceSSion = "O75881";

        int expResult = 6;
        List<Pathway> result = enzymePortalService.findPathwaysByAccession(acceSSion);

        assertEquals(expResult, result.size());

    }

    /**
     * Test of filterEnzymesInAccessions method, of class EnzymePortalService.
     */
    @Test
    public void testFilterEnzymesInAccessions() {
        LOGGER.info("filterEnzymesInAccessions");
        List<String> accessions = new ArrayList<>();
        accessions.add("Q60991");
        accessions.add("Q63688");
        accessions.add("Q0III2");
        accessions.add("Q64441");
        accessions.add("NotAnEnzyme");

        List<String> result = enzymePortalService.filterEnzymesInAccessions(accessions);
        assertTrue(result.size() > 0);

    }

    /**
     * Test of findRelatedProteinsByNamePrefix method, of class
     * EnzymePortalService.
     */
    @Test
    public void testFindRelatedProteinsByNamePrefix() {
        LOGGER.info("findRelatedProteinsByNamePrefix");
        String nameprefix = "PDE5A";

        RelatedProteins expResult = new RelatedProteins();
        expResult.setRelProtInternalId(BigDecimal.valueOf(853983));
        expResult.setNamePrefix(nameprefix);

        RelatedProteins result = enzymePortalService.findRelatedProteinsByNamePrefix(nameprefix);
        assertEquals(expResult, result);

    }

    /**
     * Test of findRelatedProteinsByNamePrefixes method, of class
     * EnzymePortalService.
     */
    @Test
    public void testFindRelatedProteinsByNamePrefixes() {
        LOGGER.info("findRelatedProteinsByNamePrefixes");
        List<String> namePrefixes = new ArrayList<>();
        namePrefixes.add("CP24A");
        namePrefixes.add("FAKEGENE");
        namePrefixes.add("PDE5A");

        int expResult = 2;
        List<RelatedProteins> result = enzymePortalService.findRelatedProteinsByNamePrefixes(namePrefixes);
        assertEquals(expResult, result.size());

    }

    /**
     * Test of findEnzymesByCompound method, of class EnzymePortalService.
     */
    @Test
    public void testFindEnzymesByCompound() {
        LOGGER.info("findEnzymesByCompound");
        String compoundId = "CHEMBL192";

        int expResult = 1;
        List<String> result = enzymePortalService.findEnzymesByCompound(compoundId);

        assertEquals(expResult, result.size());

    }

    /**
     * Test of findDiseases method, of class EnzymePortalService.
     */
    @Test
    public void testFindDiseases() {
        LOGGER.info("findDiseases");

        int expResult = 3;
        List<EnzymePortalDisease> result = enzymePortalService.findDiseases();

        assertEquals(expResult, result.size());

    }

    /**
     * Test of findAccessionsByEc method, of class EnzymePortalService.
     */
    @Test
    public void testFindAccessionsByEc() {
        LOGGER.info("findAccessionsByEc");
        String ecNumber = "1.14.13.126";

        List<String> expResult = new LinkedList<>();
//        expResult.add("Q07973");
//        expResult.add("Q09128");
        expResult.add("Q64441");
        List<String> result = enzymePortalService.findAccessionsByEc(ecNumber);

        assertEquals(expResult, result);

    }

    /**
     * Test of findByEcNumbersByAccession method, of class EnzymePortalService.
     */
    @Test
    public void testFindByEcNumbersByAccession() {
        LOGGER.info("findByEcNumbersByAccession");

        int expResult = 1;
        List<EnzymePortalEcNumbers> result = enzymePortalService.findByEcNumbersByAccession(ACCESSION).stream().distinct().collect(Collectors.toList());

        assertEquals(expResult, result.size());

    }

    /**
     * Test of findReactions method, of class EnzymePortalService.
     */
    @Test
    public void testFindReactions() {
        LOGGER.info("findReactions");

        int expResult = 8;
        List<EnzymePortalReaction> result = enzymePortalService.findReactions();

        assertEquals(expResult, result.size());

    }

    /**
     * Test of findPathways method, of class EnzymePortalService.
     */
    @Test
    public void testFindPathways() {
        LOGGER.info("findPathways");

        int expResult = 10;
        List<EnzymePortalPathways> result = enzymePortalService.findPathways();
        assertEquals(expResult, result.size());

    }

    /**
     * Test of findAccessionsByReactionId method, of class EnzymePortalService.
     */
    @Test
    public void testFindAccessionsByReactionId() {
        LOGGER.info("findAccessionsByReactionId");
        String reactionId = "RHEA:24979";

        List<String> expResult = new ArrayList<>();
        expResult.add("Q07973");

        List<String> result = enzymePortalService.findAccessionsByReactionId(reactionId);

        assertEquals(expResult, result);
        assertEquals(expResult.size(), result.size());

    }

    /**
     * Test of findReactionsByAccession method, of class EnzymePortalService.
     */
    @Test
    public void testFindReactionsByAccession() {
        LOGGER.info("findReactionsByAccession");
        String rAccession = "Q07973";

        int expResult = 6;
        List<EnzymeReaction> result = enzymePortalService.findReactionsByAccession(rAccession);
        assertEquals(expResult, result.size());

    }

    /**
     * Test of findAccessionsByPathwayId method, of class EnzymePortalService.
     */
    @Test
    public void testFindAccessionsByPathwayId() {
        LOGGER.info("findAccessionsByPathwayId");
        String pathwayId = "REACT_147797";

        int expResult = 1;
        List<String> result = enzymePortalService.findAccessionsByPathwayId(pathwayId);
        assertEquals(expResult, result.size());

    }

    /**
     * Test of findAccessionsByTaxId method, of class EnzymePortalService.
     */
    @Test
    public void testFindAccessionsByTaxId() {
        LOGGER.info("findAccessionsByTaxId");
        Long taxId = 9606L;

        int expResult = 5;
        List<String> result = enzymePortalService.findAccessionsByTaxId(taxId);
        assertEquals(expResult, result.size());

    }

    /**
     * Test of getCountForOrganisms method, of class EnzymePortalService.
     */
    @Test
    public void testGetCountForOrganisms() {
        LOGGER.info("getCountForOrganisms");
        List<Long> taxids = new ArrayList<>();
        taxids.add(9606L);
        taxids.add(9606L);
        taxids.add(9606L);
        taxids.add(10090L);
        taxids.add(10090L);

        int expectedResult = 2;
        List<Taxonomy> result = enzymePortalService.getCountForOrganisms(taxids);

        assertNotNull(result);
        assertThat(result, hasSize(lessThan(Integer.MAX_VALUE)));
        assertThat(result, hasSize(greaterThan(0)));
        assertThat(result, hasSize(expectedResult));

        long numEnzymeForHuman = 4;

        result
                .stream()
                .filter(taxonomy -> taxonomy.getTaxId() == 9606)
                .forEach(taxonomy -> {
                    assertTrue(taxonomy.getNumEnzymes() == numEnzymeForHuman);
                });

    }

    /**
     * Test of findEnzymesByTaxId method, of class EnzymePortalService.
     */
    @Test
    public void testFindEnzymesByTaxId() {
        LOGGER.info("findEnzymesByTaxId");
        Long taxId = 10090L;

        int expResult = 3;
        List<UniprotEntry> result = enzymePortalService.findEnzymesByTaxId(taxId);

        assertEquals(expResult, result.size());

    }

    /**
     * Test of findEnzymesByTaxonomy method, of class EnzymePortalService.
     */
    @Test
    public void testFindEnzymesByTaxonomy() {
        LOGGER.info("findEnzymesByTaxonomy");
        Long taxId = 10116L;

        int expResult = 2;
        Page<UniprotEntry> result = enzymePortalService.findEnzymesByTaxonomy(taxId, PAGEABLE);
        assertEquals(expResult, result.getContent().size());

    }

    /**
     * Test of findEnzymesByAccessions method, of class EnzymePortalService.
     */
    @Test
    public void testFindEnzymesByAccessions_List_Pageable() {
        LOGGER.info("findEnzymesByAccessions");
        List<String> accessions = new ArrayList<>();
        accessions.add("Q60991");
        accessions.add("Q63688");
        accessions.add("Q0III2");
        accessions.add("Q64441");
        accessions.add("fakeAccession");

        int expResult = 4;
        Page<UniprotEntry> result = enzymePortalService.findEnzymesByAccessions(accessions, PAGEABLE);
        assertEquals(expResult, result.getContent().size());
        assertEquals(4, result.getTotalElements());
        assertEquals(1, result.getTotalPages());

    }

    /**
     * Test of findEnzymesByEcNumbers method, of class EnzymePortalService.
     */
    @Test
    public void testFindEnzymesByEcNumbers() {
        LOGGER.info("findEnzymesByEcNumbers");
        List<String> ecNumbers = new ArrayList<>();
        ecNumbers.add("3.4.24.85");
        ecNumbers.add("1.14.13.126");

        int expResult = 6;
        Page<UniprotEntry> result = enzymePortalService.findEnzymesByEcNumbers(ecNumbers, PAGEABLE);

        assertEquals(expResult, result.getContent().size());

    }

    /**
     * Test of findEnzymesByNamePrefixes method, of class EnzymePortalService.
     */
    @Test
    public void testFindEnzymesByNamePrefixes_List_Pageable() {
        LOGGER.info("findEnzymesByNamePrefixes");
        List<String> namePrefixes = new ArrayList<>();
        namePrefixes.add("CP24A");
        namePrefixes.add("FAKEGENE");
        namePrefixes.add("PDE5A");

        int expResult = 4;
        Page<UniprotEntry> result = enzymePortalService.findEnzymesByNamePrefixes(namePrefixes, PAGEABLE);
        assertEquals(expResult, result.getContent().size());

    }

    /**
     * Test of findEnzymesByNamePrefixes method, of class EnzymePortalService.
     */
    @Test
    public void testFindEnzymesByNamePrefixes_List() {
        LOGGER.info("findEnzymesByNamePrefixes");
        List<String> namePrefixes = new ArrayList<>();
        namePrefixes.add("CP24A");
        namePrefixes.add("FAKEGENE");
        namePrefixes.add("PDE5A");
        int expResult = 4;
        List<UniprotEntry> result = enzymePortalService.findEnzymesByNamePrefixes(namePrefixes);

        assertEquals(expResult, result.size());

    }

    /**
     * Test of findEnzymesByPathwayId method, of class EnzymePortalService.
     */
    @Test
    public void testFindEnzymesByPathwayId() {
        LOGGER.info("findEnzymesByPathwayId");
        String pathwayId = "REACT_11054";

        int expResult = 1;
        List<UniprotEntry> result = enzymePortalService.findEnzymesByPathwayId(pathwayId);

        assertEquals(expResult, result.size());

    }

    /**
     * Test of findEnzymesByEc method, of class EnzymePortalService.
     */
    @Test
    public void testFindEnzymesByEc() {
        LOGGER.info("findEnzymesByEc");
        String ec = "1.14.13.126";

        int expResult = 1;
        List<UniprotEntry> result = enzymePortalService.findEnzymesByEc(ec);

        assertEquals(expResult, result.size());

    }

    /**
     * Test of findPathwaysByName method, of class EnzymePortalService.
     */
    @Test
    public void testFindPathwaysByName() {
        LOGGER.info("findPathwaysByName");
        String pathwayName = "Orphan transporters";

        int expResult = 1;
        List<Pathway> result = enzymePortalService.findPathwaysByName(pathwayName.trim());
        assertEquals(expResult, result.size());

    }

    /**
     * Test of findEnzymesByMeshId method, of class EnzymePortalService.
     */
    @Test
    public void testFindEnzymesByMeshId() {
        LOGGER.info("findEnzymesByMeshId");
        String omimId = "143880";

        int expResult = 1;
        List<UniprotEntry> result = enzymePortalService.findEnzymesByMeshId(omimId);

        assertEquals(expResult, result.size());

    }

    /**
     * Test of findDiseasesLike method, of class EnzymePortalService.
     */
    @Test
    public void testFindDiseasesLike() {
        LOGGER.info("findDiseasesLike");
        String diseaseName = "immunodeficiency";

        int expResult = 1;
        List<Disease> result = enzymePortalService.findDiseasesLike(diseaseName);

        assertEquals(expResult, result.size());

    }

    /**
     * Test of findDiseasesByTaxId method, of class EnzymePortalService.
     */
    @Test
    public void testFindDiseasesByTaxId() {
        LOGGER.info("findDiseasesByTaxId");
        Long taxId = 9606L;

        int expResult = 3;
        List<Disease> result = enzymePortalService.findDiseasesByTaxId(taxId);

        assertEquals(expResult, result.size());

    }

    /**
     * Test of findSpeciesByTaxId method, of class EnzymePortalService.
     */
    @Test
    public void testFindSpeciesByTaxId() {
        LOGGER.info("findSpeciesByTaxId");
        Long taxId = 9606L;

        int expResult = 1;
        List<Species> result = enzymePortalService.findSpeciesByTaxId(taxId);

        assertEquals(expResult, result.size());

    }

    /**
     * Test of findCompoundsByTaxId method, of class EnzymePortalService.
     */
    @Test
    public void testFindCompoundsByTaxId() {
        LOGGER.info("findCompoundsByTaxId");
        Long taxId = 9606L;

        int expResult = 4;
        List<Compound> result = enzymePortalService.findCompoundsByTaxId(taxId);

        assertEquals(expResult, result.size());

    }

    /**
     * Test of filterBySpecieAndCompoundsAndDiseases method, of class
     * EnzymePortalService.
     */
    @Test
    public void testFilterBySpecieAndCompoundsAndDiseases() {
        LOGGER.info("filterBySpecieAndCompoundsAndDiseases");
        Long taxId = 9606L;
        List<String> compoudIds = new ArrayList<>();
        compoudIds.add("CHEMBL192");
        List<String> diseaseNames = new ArrayList<>();
        diseaseNames.add("spastic paraplegia hereditary");

        Page<UniprotEntry> result = enzymePortalService.filterBySpecieAndCompoundsAndDiseases(taxId, compoudIds, diseaseNames, PAGEABLE);
        assertTrue(result.getTotalPages() == 0);

    }

    /**
     * Test of filterBySpecieAndCompounds method, of class EnzymePortalService.
     */
    @Test
    public void testFilterBySpecieAndCompounds() {
        LOGGER.info("filterBySpecieAndCompounds");
        Long taxId = 9606L;
        List<String> compoudId = new ArrayList<>();
        compoudId.add("CHEMBL192");

        int expResult = 1;
        Page<UniprotEntry> result = enzymePortalService.filterBySpecieAndCompounds(taxId, compoudId, PAGEABLE);

        assertEquals(expResult, result.getContent().size());

    }

    /**
     * Test of filterBySpecieAndDiseases method, of class EnzymePortalService.
     */
    @Test
    public void testFilterBySpecieAndDiseases() {
        LOGGER.info("filterBySpecieAndDiseases");
        Long taxId = 9606L;
        List<String> omimNUmbers = new ArrayList<>();
        omimNUmbers.add("270800");

        int expResult = 1;
        Page<UniprotEntry> result = enzymePortalService.filterBySpecieAndDiseases(taxId, omimNUmbers, PAGEABLE);

        assertEquals(expResult, result.getContent().size());

    }

    /**
     * Test of filterBySpecie method, of class EnzymePortalService.
     */
    @Test
    public void testFilterBySpecie() {
        LOGGER.info("filterBySpecie");
        Long taxId = 9606L;

        int expResult = 5;
        Page<UniprotEntry> result = enzymePortalService.filterBySpecie(taxId, PAGEABLE);

        assertEquals(expResult, result.getContent().size());

    }

    /**
     * Test of findCatalyticActivitiesByAccession method, of class
     * EnzymePortalService.
     */
    @Test
    public void testFindCatalyticActivitiesByAccession() {
        LOGGER.info("findCatalyticActivitiesByAccession");

        int resultSize = 2;
        List<String> expResult = new LinkedList<>();
        expResult.add("Calcidiol + NADPH + O(2) = secalciferol + NADP(+) + H(2)O");
        expResult.add("Calcitriol + NADPH + O(2) = calcitetrol + NADP(+) + H(2)O");

        List<String> result = enzymePortalService.findCatalyticActivitiesByAccession(ACCESSION);

        assertEquals(expResult, result);
        assertEquals(resultSize, result.size());

    }

    /**
     * Test of findEnzymeFamiliesByTaxId method, of class EnzymePortalService.
     */
    @Test
    public void testFindEnzymeFamiliesByTaxId() {
        LOGGER.info("findEnzymeFamiliesByTaxId");
        Long taxId = 10090L;

        List<EcNumber> ecList = new ArrayList<>();

        EcNumber ecNumber = new EcNumber(3);
        ecNumber.getFamilies().add("Hydrolases");
        ecList.add(ecNumber);
        EcNumber ecNumber2 = new EcNumber(1);
        ecNumber2.getFamilies().add("Oxidoreductases");
        ecList.add(ecNumber2);

        List<EcNumber> result = enzymePortalService.findEnzymeFamiliesByTaxId(taxId);

        assertEquals(ecList.size(), result.size());

    }

}
