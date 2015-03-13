/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import java.sql.SQLException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.search.model.EnzymeSummary;
import uk.ac.ebi.ep.data.search.model.Species;
import uk.ac.ebi.ep.data.search.model.Taxonomy;
import uk.ac.ebi.ep.data.service.AbstractDataTest;

/**
 *
 * @author joseph
 */
@Ignore
public class UniprotEntryRepositoryImplTest extends AbstractDataTest {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @After
    @Override
    public void tearDown() throws SQLException {
         dataSource.getConnection().close();
    }

    /**
     * Test of setEntityManager method, of class UniprotEntryRepositoryImpl.
     */
    @Test
    public void testSetEntityManager() {
        System.out.println("setEntityManager");
        System.out.println("the manager "+ entityManager.getEntityManagerFactory().toString());
        EntityManager entityManager = null;
        UniprotEntryRepositoryImpl instance = new UniprotEntryRepositoryImpl();
        instance.setEntityManager(entityManager);

    }

    /**
     * Test of findEnzymesByNamePrefixes method, of class UniprotEntryRepositoryImpl.
     */
    @Test
    public void testFindEnzymesByNamePrefixes() {
        System.out.println("findEnzymesByNamePrefixes");
        List<String> name_prefixes = null;
        UniprotEntryRepositoryImpl instance = new UniprotEntryRepositoryImpl();
        List<UniprotEntry> expResult = null;
        List<UniprotEntry> result = instance.findEnzymesByNamePrefixes(name_prefixes);
        assertEquals(expResult, result);

    }

    /**
     * Test of findEnzymesByAccessions method, of class UniprotEntryRepositoryImpl.
     */
    @Test
    public void testFindEnzymesByAccessions_List() {
        System.out.println("findEnzymesByAccessions");
        List<String> accessions = null;
        UniprotEntryRepositoryImpl instance = new UniprotEntryRepositoryImpl();
        List<UniprotEntry> expResult = null;
        List<UniprotEntry> result = instance.findEnzymesByAccessions(accessions);
        assertEquals(expResult, result);

    }

    /**
     * Test of findEnzymeByNamePrefix method, of class UniprotEntryRepositoryImpl.
     */
    @Test
    public void testFindEnzymeByNamePrefix() {
        System.out.println("findEnzymeByNamePrefix");
        String namePrefix = "";
        UniprotEntryRepositoryImpl instance = new UniprotEntryRepositoryImpl();
        List<UniprotEntry> expResult = null;
        List<UniprotEntry> result = instance.findEnzymeByNamePrefix(namePrefix);
        assertEquals(expResult, result);

    }

    /**
     * Test of filterEnzymesInAccessions method, of class UniprotEntryRepositoryImpl.
     */
    @Test
    public void testFilterEnzymesInAccessions() {
        System.out.println("filterEnzymesInAccessions");
        List<String> accessions = null;
        UniprotEntryRepositoryImpl instance = new UniprotEntryRepositoryImpl();
        List<String> expResult = null;
        List<String> result = instance.filterEnzymesInAccessions(accessions);
        assertEquals(expResult, result);

    }

    /**
     * Test of findEnzymesByAccession method, of class UniprotEntryRepositoryImpl.
     */
    @Test
    public void testFindEnzymesByAccession() {
        System.out.println("findEnzymesByAccession");
        String accession = "";
        UniprotEntryRepositoryImpl instance = new UniprotEntryRepositoryImpl();
        List<UniprotEntry> expResult = null;
        List<UniprotEntry> result = instance.findEnzymesByAccession(accession);
        assertEquals(expResult, result);

    }

    /**
     * Test of getCountForOrganisms method, of class UniprotEntryRepositoryImpl.
     */
    @Test
    public void testGetCountForOrganisms() {
        System.out.println("getCountForOrganisms");
        List<Long> taxids = null;
        UniprotEntryRepositoryImpl instance = new UniprotEntryRepositoryImpl();
        List<Taxonomy> expResult = null;
        List<Taxonomy> result = instance.getCountForOrganisms(taxids);
        assertEquals(expResult, result);

    }

    /**
     * Test of findAccessionsByTaxId method, of class UniprotEntryRepositoryImpl.
     */
    @Test
    public void testFindAccessionsByTaxId() {
        System.out.println("findAccessionsByTaxId");
        Long taxId = null;
        UniprotEntryRepositoryImpl instance = new UniprotEntryRepositoryImpl();
        List<String> expResult = null;
        List<String> result = instance.findAccessionsByTaxId(taxId);
        assertEquals(expResult, result);

    }

    /**
     * Test of findEnzymesByTaxId method, of class UniprotEntryRepositoryImpl.
     */
    @Test
    public void testFindEnzymesByTaxId() {
        System.out.println("findEnzymesByTaxId");
        Long taxId = null;
        UniprotEntryRepositoryImpl instance = new UniprotEntryRepositoryImpl();
        List<UniprotEntry> expResult = null;
        List<UniprotEntry> result = instance.findEnzymesByTaxId(taxId);
        assertEquals(expResult, result);

    }

    /**
     * Test of findEnzymesByAccessions method, of class UniprotEntryRepositoryImpl.
     */
    @Test
    public void testFindEnzymesByAccessions_List_Pageable() {
        System.out.println("findEnzymesByAccessions");
        List<String> accessions = null;
        Pageable pageable = null;
        UniprotEntryRepositoryImpl instance = new UniprotEntryRepositoryImpl();
        Page<EnzymeSummary> expResult = null;
        Page<EnzymeSummary> result = instance.findEnzymesByAccessions(accessions, pageable);
        assertEquals(expResult, result);

    }

    /**
     * Test of findSpeciesByTaxId method, of class UniprotEntryRepositoryImpl.
     */
    @Test
    public void testFindSpeciesByTaxId() {
        System.out.println("findSpeciesByTaxId");
        Long taxId = null;
        UniprotEntryRepositoryImpl instance = new UniprotEntryRepositoryImpl();
        List<Species> expResult = null;
        List<Species> result = instance.findSpeciesByTaxId(taxId);
        assertEquals(expResult, result);

    }

    /**
     * Test of findSpeciesByScientificName method, of class UniprotEntryRepositoryImpl.
     */
    @Test
    public void testFindSpeciesByScientificName() {
        System.out.println("findSpeciesByScientificName");
        String sName = "";
        UniprotEntryRepositoryImpl instance = new UniprotEntryRepositoryImpl();
        List<Species> expResult = null;
        List<Species> result = instance.findSpeciesByScientificName(sName);
        assertEquals(expResult, result);
  
    }

    /**
     * Test of findEnzymesByMeshId method, of class UniprotEntryRepositoryImpl.
     */
    @Test
    public void testFindEnzymesByMeshId() {
        System.out.println("findEnzymesByMeshId");
        String meshId = "";
        UniprotEntryRepositoryImpl instance = new UniprotEntryRepositoryImpl();
        List<UniprotEntry> expResult = null;
        List<UniprotEntry> result = instance.findEnzymesByMeshId(meshId);
        assertEquals(expResult, result);
 
    }

    /**
     * Test of findEnzymesByPathwayId method, of class UniprotEntryRepositoryImpl.
     */
    @Test
    public void testFindEnzymesByPathwayId() {
        System.out.println("findEnzymesByPathwayId");
        String pathwayId = "";
        UniprotEntryRepositoryImpl instance = new UniprotEntryRepositoryImpl();
        List<UniprotEntry> expResult = null;
        List<UniprotEntry> result = instance.findEnzymesByPathwayId(pathwayId);
        assertEquals(expResult, result);
 
    }
    
}
