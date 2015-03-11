/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.ebi.ep.data.domain.UniprotEntry;

/**
 *
 * @author joseph
 */
public class UniprotEntryServiceTestIT extends AbstractDataTest {

    @Autowired
    protected UniprotEntryService uniprotEntryService;
    private static final String ACCESSION = "Q64441";

    @Override
    protected void tearDown() throws Exception {
        dataSource.getConnection().close();
    }

    /**
     * Test of findByAccession method, of class UniprotEntryService.
     */
    @Test
    public void testFindByAccession() {
        LOGGER.info("findByAccession");

        String geneName = "CP24A_MOUSE";
        String scientificName = "Mus musculus";
        Optional<UniprotEntry> result = uniprotEntryService.findByAccession(ACCESSION);

        assertEquals(geneName, result.get().getName());
        assertTrue(result.get().getScientificName().equalsIgnoreCase(scientificName));

    }

    /**
     * Test of findByUniProtAccession method, of class UniprotEntryService.
     */
    @Test
    public void testFindByUniProtAccession() {
        LOGGER.info("findByUniProtAccession");

        String commonName = "Mouse";
        String proteinName = "MOCK-1,25-dihydroxyvitamin D(3) 24-hydroxylase, mitochondrial";
        UniprotEntry result = uniprotEntryService.findByUniProtAccession(ACCESSION);

        assertEquals(commonName, result.getCommonName());
        assertEquals(proteinName, result.getProteinName());

    }

    /**
     * Test of findAllUniprotAccessions method, of class UniprotEntryService.
     */
    @Test
    public void testFindAllUniprotAccessions() {
        LOGGER.info("findAllUniprotAccessions");

        int expResult = 11;
        List<String> result = uniprotEntryService.findAllUniprotAccessions();
        assertEquals(expResult, result.size());

    }

    /**
     * Test of findUniprotEntries method, of class UniprotEntryService.
     */
    @Test
    public void testFindUniprotEntries() {
        LOGGER.info("findUniprotEntries");

        int expResult = 11;

        List<UniprotEntry> result = uniprotEntryService.findUniprotEntries();
        assertEquals(expResult, result.size());

    }

    /**
     * Test of findByNamePrefixes method, of class UniprotEntryService.
     */
    @Test
    public void testFindByNamePrefixes() {
        LOGGER.info("findByNamePrefixes");
        List<String> namePrefixes = new ArrayList<>();
        namePrefixes.add("CP24A");
        namePrefixes.add("FAKEGENE");
        namePrefixes.add("CP7B1");

        int expResult = 6;
        List<UniprotEntry> result = uniprotEntryService.findByNamePrefixes(namePrefixes);
        assertEquals(expResult, result.size());

    }

    /**
     * Test of findEnzymesByAccessions method, of class UniprotEntryService.
     */
    @Test
    public void testFindEnzymesByAccessions() {
        LOGGER.info("findEnzymesByAccessions");
        List<String> accessions = new ArrayList<>();
        accessions.add("Q60991");
        accessions.add("Q63688");
        accessions.add("Q0III2");
        accessions.add("Q64441");
        accessions.add("fakeAccession");

        int expResult = 4;
        List<UniprotEntry> result = uniprotEntryService.findEnzymesByAccessions(accessions);

        assertEquals(expResult, result.size());

    }

}
