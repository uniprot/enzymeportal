/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.search.model;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

/**
 *
 * @author joseph
 */
public class EcNumberTest {

    protected static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(EcNumberTest.class);
    private EcNumber instance = null;

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        instance = new EcNumber(1, false);
       

    }

    /**
     * Test of isSelected method, of class EcNumber.
     */
    @Test
    public void testIsSelected() {
        LOGGER.info("isSelected");

        boolean expResult = false;
        boolean result = instance.isSelected();
        assertEquals(expResult, result);

    }

    /**
     * Test of setSelected method, of class EcNumber.
     */
    @Test
    public void testSetSelected() {
        LOGGER.info("setSelected");
        boolean selected = true;

        instance.setSelected(selected);
        Assert.assertTrue(instance.isSelected());
    }

    /**
     * Test of getEc method, of class EcNumber.
     */
    @Test
    public void testGetEc() {
        LOGGER.info("getEc");

        Integer expResult = 1;
        Integer result = instance.getEc();
        assertEquals(expResult, result);

    }

    /**
     * Test of setEc method, of class EcNumber.
     */
    @Test
    public void testSetEc() {
        LOGGER.info("setEc");
        int ec = 2;

        instance.setEc(ec);
        int theEc = instance.getEc();
        TestCase.assertTrue(ec==theEc);

    }

    /**
     * Test of getFamily method, of class EcNumber.
     */
    @Test
    public void testGetFamily() {
        LOGGER.info("getFamily");

        String expResult = "Oxidoreductases";
        String result = instance.getFamily();
        assertEquals(expResult, result);

    }

    /**
     * Test of setFamily method, of class EcNumber.
     */
    @Test
    public void testSetFamily() {
        LOGGER.info("setFamily");
        String family = "Ligases";
        EcNumber obj = new EcNumber();
        obj.setEc(6);
        obj.setFamily(family);
        String theFamily = obj.getFamily();
        TestCase.assertTrue(family.equalsIgnoreCase(theFamily));

    }

    /**
     * Test of hashCode method, of class EcNumber.
     */
    @Test
    public void testHashCode() {
        LOGGER.info("hashCode");
        EcNumber obj = new EcNumber(1, false);
        int expResult = obj.hashCode();
        int result = instance.hashCode();
        assertEquals(expResult, result);
        Assert.assertTrue(expResult == result);

    }

    /**
     * Test of equals method, of class EcNumber.
     */
    @Test
    public void testEquals() {
        LOGGER.info("equals");
        Object obj = new EcNumber(1, false);

        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);

    }

    /**
     * Test of getFamilies method, of class EcNumber.
     */
    @Test
    public void testGetFamilies() {
        LOGGER.info("getFamilies");
     
        List<String> expResult = new ArrayList<>();
        expResult.add("Oxidoreductases");
        List<String> result = instance.getFamilies();
    
        assertEquals(expResult, result);

    }

    /**
     * Test of toString method, of class EcNumber.
     */
    @Test
    public void testToString() {
        LOGGER.info("toString");
      
        String expResult = "EcNumber{ec=1, family=Oxidoreductases, families=[Oxidoreductases]}";
        String result = instance.toString();
     
        assertEquals(expResult, result);
  
    }



}
