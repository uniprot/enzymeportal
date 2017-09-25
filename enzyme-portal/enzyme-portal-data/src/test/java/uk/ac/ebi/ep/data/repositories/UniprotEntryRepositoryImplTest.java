package uk.ac.ebi.ep.data.repositories;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.After;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.search.model.EnzymeSummary;
import uk.ac.ebi.ep.data.search.model.Species;
import uk.ac.ebi.ep.data.service.AbstractDataTest;

/**
 *
 * @author joseph
 */
public class UniprotEntryRepositoryImplTest extends AbstractDataTest {

    @PersistenceContext
    private EntityManager entityManager;

    @After
    @Override
    public void tearDown() throws SQLException {
        dataSource.getConnection().close();
        entityManager.close();
    }

    /**
     * Test of setEntityManager method, of class UniprotEntryRepositoryImpl.
     */
//    @Test
//    public void testSetEntityManager() {
//        LOGGER.info("setEntityManager");
//
//        instance.setEntityManager(entityManager);
//        EntityManager manager = instance.getEntityManager();
//        assertNotNull(manager);
//
//    }

    /**
     * Test of findEnzymesByNamePrefixes method, of class
     * UniprotEntryRepositoryImpl.
     */
    @Test
    public void testFindEnzymesByNamePrefixes() {
        LOGGER.info("findEnzymesByNamePrefixes");

        List<String> namePrefixes = new ArrayList<>();
        namePrefixes.add("CP24A");
        namePrefixes.add("FAKEGENE");
        namePrefixes.add("CP7B1");

        int expResult = 6;

        List<UniprotEntry> result = uniprotEntryRepository.findEnzymesByNamePrefixes(namePrefixes);
        assertEquals(expResult, result.size());

    }

    /**
     * Test of findEnzymesByAccessions method, of class
     * UniprotEntryRepositoryImpl.
     */
//    @Test
//    public void testFindEnzymesByAccessions_List() {
//        LOGGER.info("findEnzymesByAccessions");
//        List<String> accessions = new ArrayList<>();
//        accessions.add("Q60991");
//        accessions.add("M636T8");
//        accessions.add("Q0III2");
//        accessions.add("Q64441");
//        accessions.add("PK5671");
//        accessions.add("fakeAccession");
//
//        int expResult = 3;
//
//        List<UniprotEntry> result = uniprotEntryRepository.findEnzymesByAccessions(accessions);
//        assertEquals(expResult, result.size());
//
//    }

    /**
     * Test of findEnzymeByNamePrefix method, of class
     * UniprotEntryRepositoryImpl.
     */
//    @Test
//    public void testFindEnzymeByNamePrefix() {
//        LOGGER.info("findEnzymeByNamePrefix");
//        String namePrefix = "CP24A";
//
//        int expResult = 2;
//        List<UniprotEntry> result = uniprotEntryRepository.findEnzymeByNamePrefix(namePrefix);
//        assertEquals(expResult, result.size());
//        assertTrue(result.size() > 0);
//
//    }

    /**
     * Test of filterEnzymesInAccessions method, of class
     * UniprotEntryRepositoryImpl.
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

        List<String> result = uniprotEntryRepository.filterEnzymesFromAccessions(accessions).stream().distinct().collect(Collectors.toList());
        assertTrue(result.size() > 1);

    }

    /**
     * Test of findEnzymesByAccession method, of class
     * UniprotEntryRepositoryImpl.
     */
//    @Test
//    public void testFindEnzymesByAccession() {
//        LOGGER.info("findEnzymesByAccession");
//        String accession = "Q64441";
//
//        int expResult = 1;
//        List<UniprotEntry> result = uniprotEntryRepository.findEnzymesByAccession(accession).stream().distinct().collect(Collectors.toList());
//
//        assertEquals(expResult, result.size());
//
//    }

    /**
     * Test of getCountForOrganisms method, of class UniprotEntryRepositoryImpl.
     */
//    @Test
//    public void testGetCountForOrganisms() {
//        LOGGER.info("getCountForOrganisms");
//        List<Long> taxids = new ArrayList<>();
//        taxids.add(9606L);
//        taxids.add(10116L);
//        taxids.add(7955L);
//        taxids.add(9913L);
//        taxids.add(10090L);
//
//        int expResult = 4;
//        List<Taxonomy> result = uniprotEntryRepository.getCountForOrganisms(taxids);
//        assertEquals(expResult, result.size());
//
//    }

    /**
     * Test of findAccessionsByTaxId method, of class
     * UniprotEntryRepositoryImpl.
     */
    @Test
    public void testFindAccessionsByTaxId() {
        LOGGER.info("findAccessionsByTaxId");
        Long taxId = 9606L;

        List<String> expResult = new LinkedList<>();

        expResult.add("O43462");
        expResult.add("O75881");
        expResult.add("Q07973");
        expResult.add("P08183");
        expResult.add("O76074");

        List<String> result = uniprotEntryRepository.findAccessionsByTaxId(taxId);
        assertEquals(expResult, result);
        assertEquals(expResult.size(), result.size());

    }

    /**
     * Test of findEnzymesByTaxId method, of class UniprotEntryRepositoryImpl.
     */
    @Test
    public void testFindEnzymesByTaxId() {
        LOGGER.info("findEnzymesByTaxId");
        Long taxId = 9606L;

        int expResult = 5;
        List<UniprotEntry> result = uniprotEntryRepository.findEnzymesByTaxId(taxId);

        assertEquals(expResult, result.size());

    }

    /**
     * Test of findEnzymesByAccessions method, of class
     * UniprotEntryRepositoryImpl.
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

        Pageable pageable = new PageRequest(0, 500, Sort.Direction.ASC, "function", "entryType");

        int expResult = 4;
        Page<EnzymeSummary> result = uniprotEntryRepository.findEnzymesByAccessions(accessions, pageable);

        assertEquals(expResult, result.getContent().size());
        assertEquals(4, result.getTotalElements());
        assertEquals(1, result.getTotalPages());

    }

    /**
     * Test of findSpeciesByTaxId method, of class UniprotEntryRepositoryImpl.
     */
    @Test
    public void testFindSpeciesByTaxId() {
        LOGGER.info("findSpeciesByTaxId");
        Long taxId = 9606L;

        Species species = new Species("Homo sapiens", "Human", taxId);

        List<Species> expResult = new ArrayList<>();
        expResult.add(species);

        List<Species> result = uniprotEntryRepository.findSpeciesByTaxId(taxId);

        assertEquals(expResult, result);
        assertEquals(expResult.size(), result.size());

    }

    /**
     * Test of findSpeciesByScientificName method, of class
     * UniprotEntryRepositoryImpl.
     */
    @Test
    public void testFindSpeciesByScientificName() {
        LOGGER.info("findSpeciesByScientificName");
        String sName = "Homo sapiens";

        Species species = new Species("Homo sapiens", "Human", 9606L);
        List<Species> expResult = new ArrayList<>();
        expResult.add(species);
        List<Species> result = uniprotEntryRepository.findSpeciesByScientificName(sName);
        assertEquals(expResult, result);

    }

    /**
     * Test of findEnzymesByMeshId method, of class UniprotEntryRepositoryImpl.
     */
    @Test
    public void testFindEnzymesByMeshId() {
        LOGGER.info("findEnzymesByMeshId");
        String omimId = "143880";

        int expResult = 1;
        List<UniprotEntry> result = uniprotEntryRepository.findEnzymesByMeshId(omimId);

        assertEquals(expResult, result.size());

    }

    /**
     * Test of findEnzymesByPathwayId method, of class
     * UniprotEntryRepositoryImpl.
     */
    @Test
    public void testFindEnzymesByPathwayId() {
        LOGGER.info("findEnzymesByPathwayId");
        String pathwayId = "REACT_147797";

        int expResult = 1;
        List<UniprotEntry> result = uniprotEntryRepository.findEnzymesByPathwayId(pathwayId);
        assertEquals(expResult, result.size());

    }

}
