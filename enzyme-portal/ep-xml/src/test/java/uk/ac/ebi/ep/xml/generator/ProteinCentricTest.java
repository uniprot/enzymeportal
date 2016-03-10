/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.xml.generator;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Ignore
public class ProteinCentricTest {
    
    public ProteinCentricTest() {
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
     * Test of generateXmL method, of class ProteinCentric.
     */
    @Test
    public void testGenerateXmL() throws Exception {
        System.out.println("generateXmL");
        ProteinCentric instance = null;
        instance.generateXmL();

    }

    /**
     * Test of validateXML method, of class ProteinCentric.
     */
    @Test
    public void testValidateXML() {
        System.out.println("validateXML");
        ProteinCentric instance = null;
        instance.validateXML();

    }
    
}
