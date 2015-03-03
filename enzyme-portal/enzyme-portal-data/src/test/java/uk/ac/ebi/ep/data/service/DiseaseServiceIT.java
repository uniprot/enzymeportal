/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.service;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import uk.ac.ebi.ep.data.domain.EnzymePortalDisease;
import uk.ac.ebi.ep.data.search.model.Disease;

/**
 *
 * @author joseph
 */
@Ignore
public class DiseaseServiceIT {
    

    
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
     * Test of addDisease method, of class DiseaseService.
     */
    @Test
    public void testAddDisease_EnzymePortalDisease() {
        System.out.println("addDisease");
        EnzymePortalDisease d = null;
        DiseaseService instance = new DiseaseService();
        EnzymePortalDisease expResult = null;
        EnzymePortalDisease result = instance.addDisease(d);
        assertEquals(expResult, result);

    }

    /**
     * Test of addDisease method, of class DiseaseService.
     */
    @Test
    public void testAddDisease_List() {
        System.out.println("addDisease");
        List<EnzymePortalDisease> d = null;
        DiseaseService instance = new DiseaseService();
        List<EnzymePortalDisease> expResult = null;
        List<EnzymePortalDisease> result = instance.addDisease(d);
        assertEquals(expResult, result);
    
    }

    /**
     * Test of findById method, of class DiseaseService.
     */
    @Test
    public void testFindById() {
        System.out.println("findById");
        Long id = null;
        DiseaseService instance = new DiseaseService();
        EnzymePortalDisease expResult = null;
        EnzymePortalDisease result = instance.findById(id);
        assertEquals(expResult, result);
   
    }

    /**
     * Test of findDiseases method, of class DiseaseService.
     */
    @Test
    public void testFindDiseases() {
        System.out.println("findDiseases");
        DiseaseService instance = new DiseaseService();
        List<EnzymePortalDisease> expResult = null;
        List<EnzymePortalDisease> result = instance.findDiseases();
        assertEquals(expResult, result);
 
    }

    /**
     * Test of findDiseasesByNamePrefix method, of class DiseaseService.
     */
    @Test
    public void testFindDiseasesByNamePrefix() {
        System.out.println("findDiseasesByNamePrefix");
        List<String> name_prefixes = null;
        DiseaseService instance = new DiseaseService();
        List<EnzymePortalDisease> expResult = null;
        List<EnzymePortalDisease> result = instance.findDiseasesByNamePrefix(name_prefixes);
        assertEquals(expResult, result);
   
    }

    /**
     * Test of findDiseasesByAccessions method, of class DiseaseService.
     */
    @Test
    public void testFindDiseasesByAccessions() {
        System.out.println("findDiseasesByAccessions");
        List<String> accessions = null;
        DiseaseService instance = new DiseaseService();
        List<EnzymePortalDisease> expResult = null;
        List<EnzymePortalDisease> result = instance.findDiseasesByAccessions(accessions);
        assertEquals(expResult, result);
 
    }

    /**
     * Test of findDiseasesByAccession method, of class DiseaseService.
     */
    @Test
    public void testFindDiseasesByAccession() {
        System.out.println("findDiseasesByAccession");
        String accession = "";
        DiseaseService instance = new DiseaseService();
        List<Disease> expResult = null;
        List<Disease> result = instance.findDiseasesByAccession(accession);
        assertEquals(expResult, result);

    }

    /**
     * Test of findDiseasesLike method, of class DiseaseService.
     */
    @Test
    public void testFindDiseasesLike() {
        System.out.println("findDiseasesLike");
        String diseaseName = "";
        DiseaseService instance = new DiseaseService();
        List<Disease> expResult = null;
        List<Disease> result = instance.findDiseasesLike(diseaseName);
        assertEquals(expResult, result);

    }
    
}
