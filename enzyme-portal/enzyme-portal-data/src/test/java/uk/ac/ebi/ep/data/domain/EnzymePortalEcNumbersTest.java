/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.domain;

import java.math.BigDecimal;
import java.sql.SQLException;
import static junit.framework.TestCase.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.ep.data.service.AbstractDataTest;

/**
 *
 * @author joseph
 */
public class EnzymePortalEcNumbersTest extends AbstractDataTest {

    private EnzymePortalEcNumbers instance = new EnzymePortalEcNumbers(BigDecimal.ONE);

    @Before
    @Override
    public void setUp() {
        instance = new EnzymePortalEcNumbers(BigDecimal.ONE);
        instance.setEcNumber("1.1.1.1");
        instance.setEcFamily(1);

    }

    @After
    @Override
    public void tearDown() throws SQLException {
        dataSource.getConnection().close();

    }

    /**
     * Test of getEcInternalId method, of class EnzymePortalEcNumbers.
     */
    @Test
    public void testGetEcInternalId() {
        LOGGER.info("getEcInternalId");

        BigDecimal expResult = BigDecimal.ONE;
        BigDecimal result = instance.getEcInternalId();
        assertEquals(expResult, result);

    }

    /**
     * Test of getEcNumber method, of class EnzymePortalEcNumbers.
     */
    @Test
    public void testGetEcNumber() {
        LOGGER.info("getEcNumber");

        String expResult = "1.1.1.1";
        String result = instance.getEcNumber();
        assertEquals(expResult, result);

    }

    /**
     * Test of toString method, of class EnzymePortalEcNumbers.
     */
    @Test
    public void testToString() {
        LOGGER.info("toString");

        String expResult = "1.1.1.1";
        String result = instance.toString();
        assertEquals(expResult, result);

    }

    /**
     * Test of compareTo method, of class EnzymePortalEcNumbers.
     */
    @Test
    public void testCompareTo() {
        LOGGER.info("compareTo");
        EnzymePortalEcNumbers ec = new EnzymePortalEcNumbers(BigDecimal.ZERO);
        ec.setEcNumber("2.1.1.1");
        ec.setEcFamily(2);

        int expResult = -100000;//not equal as ec is not same
        int result = instance.compareTo(ec);
        assertNotSame(expResult, result);

    }

    /**
     * Test of getFamily method, of class EnzymePortalEcNumbers.
     */
    @Test
    public void testGetFamily() {
        LOGGER.info("getFamily");

        String expResult = "Oxidoreductases";
        String result = instance.getFamily();
        assertEquals(expResult, result);

    }

    /**
     * Test of getEc method, of class EnzymePortalEcNumbers.
     */
    @Test
    public void testGetEc() {
        LOGGER.info("getEc");

        int expResult = 1;
        int result = instance.getEc();
        assertEquals(expResult, result);

    }

}
