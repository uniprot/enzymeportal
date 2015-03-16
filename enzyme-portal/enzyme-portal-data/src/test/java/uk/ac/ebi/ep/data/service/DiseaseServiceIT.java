/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Test;
import uk.ac.ebi.ep.data.domain.EnzymePortalDisease;
import uk.ac.ebi.ep.data.search.model.Disease;

/**
 *
 * @author joseph
 */
public class DiseaseServiceIT extends AbstractDataTest {

    @After
    @Override
    public void tearDown() throws SQLException {
        dataSource.getConnection().close();
    }

    /**
     * Test of findById method, of class DiseaseService.
     */
    @Test
    public void testFindById() {
        LOGGER.info("findById");
        Long id = 872L;

        String expResult = "x-linked combined immunodeficiency diseases";
        EnzymePortalDisease result = diseaseService.findById(id);
        assertEquals(expResult, result.getDiseaseName());

    }

    /**
     * Test of findDiseases method, of class DiseaseService.
     */
    @Test
    public void testFindDiseases() {
        LOGGER.info("findDiseases");

        int expResult = 3;
        List<EnzymePortalDisease> result = diseaseService.findDiseases();
        assertEquals(expResult, result.size());

    }

    /**
     * Test of findDiseasesByNamePrefix method, of class DiseaseService.
     */
    @Test
    public void testFindDiseasesByNamePrefix() {
        LOGGER.info("findDiseasesByNamePrefix");
        List<String> namePrefixes = new ArrayList<>();
        namePrefixes.add("CP7B1");
        namePrefixes.add("CP8B1");
        namePrefixes.add("MBTP2");

        int expResult = 2;
        List<EnzymePortalDisease> result = diseaseService.findDiseasesByNamePrefix(namePrefixes);
        assertEquals(expResult, result.size());

    }

    /**
     * Test of findDiseasesByAccessions method, of class DiseaseService.
     */
    @Test
    public void testFindDiseasesByAccessions() {
        LOGGER.info("findDiseasesByAccessions");
        List<String> accessions = new ArrayList<>();
        accessions.add("Q07973");
        accessions.add("O75881");
        accessions.add("PKK123_deleted");
        accessions.add("FakeAccession");

        int expResult = 2;
        List<EnzymePortalDisease> result = diseaseService.findDiseasesByAccessions(accessions);
        assertEquals(expResult, result.size());

    }

    /**
     * Test of findDiseasesByAccession method, of class DiseaseService.
     */
    @Test
    public void testFindDiseasesByAccession() {
        LOGGER.info("findDiseasesByAccession");
        String accession = "O75881";

        String expResult = "spastic paraplegia hereditary";
        List<Disease> result = diseaseService.findDiseasesByAccession(accession);
        assertEquals(expResult, result.stream().findAny().get().getName());

    }

    /**
     * Test of findDiseasesLike method, of class DiseaseService.
     */
    @Test
    public void testFindDiseasesLike() {
        LOGGER.info("findDiseasesLike");
        String diseaseName = "paraplegia";
        String expResult = "spastic paraplegia hereditary";
        List<Disease> result = diseaseService.findDiseasesLike(diseaseName);
        assertEquals(expResult, result.stream().findAny().get().getName());

    }

}
