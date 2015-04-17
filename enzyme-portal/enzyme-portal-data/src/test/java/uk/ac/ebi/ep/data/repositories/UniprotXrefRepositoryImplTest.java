/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import java.util.List;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Ignore;
import org.junit.Test;
import uk.ac.ebi.ep.data.domain.UniprotXref;

/**
 *
 * @author joseph
 */
@Ignore
public class UniprotXrefRepositoryImplTest {
    
    public UniprotXrefRepositoryImplTest() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of findPDBcodesByAccession method, of class UniprotXrefRepositoryImpl.
     */
    @Test
    public void testFindPDBcodesByAccession() {
        System.out.println("findPDBcodesByAccession");
        String accession = "";
        UniprotXrefRepositoryImpl instance = new UniprotXrefRepositoryImpl();
        List<UniprotXref> expResult = null;
        List<UniprotXref> result = instance.findPDBcodesByAccession(accession);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findPdbCodesWithNoNames method, of class UniprotXrefRepositoryImpl.
     */
    @Test
    public void testFindPdbCodesWithNoNames() {
        System.out.println("findPdbCodesWithNoNames");
        UniprotXrefRepositoryImpl instance = new UniprotXrefRepositoryImpl();
        List<String> expResult = null;
        List<String> result = instance.findPdbCodesWithNoNames();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findPdbById method, of class UniprotXrefRepositoryImpl.
     */
    @Test
    public void testFindPdbById() {
        System.out.println("findPdbById");
        String pdbId = "";
        UniprotXrefRepositoryImpl instance = new UniprotXrefRepositoryImpl();
        UniprotXref expResult = null;
        UniprotXref result = instance.findPdbById(pdbId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findPDBcodes method, of class UniprotXrefRepositoryImpl.
     */
    @Test
    public void testFindPDBcodes() {
        System.out.println("findPDBcodes");
        UniprotXrefRepositoryImpl instance = new UniprotXrefRepositoryImpl();
        List<UniprotXref> expResult = null;
        List<UniprotXref> result = instance.findPDBcodes();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
