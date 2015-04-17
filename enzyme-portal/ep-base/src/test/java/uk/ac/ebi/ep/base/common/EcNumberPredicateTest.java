/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.base.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.ep.data.domain.EnzymePortalEcNumbers;
import uk.ac.ebi.ep.data.domain.UniprotEntry;

/**
 *
 * @author joseph
 */
public class EcNumberPredicateTest {

    private EcNumberPredicate instance = null;

    @Before
    public void setUp() {
        List<Integer> ecList = new ArrayList<>();
        ecList.add(1);

        instance = new EcNumberPredicate(ecList);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of evaluate method, of class EcNumberPredicate.
     */
    @Test
    public void testEvaluate() {

        UniprotEntry obj = new UniprotEntry("P1234");
        obj.setDbentryId(2345L);

        EnzymePortalEcNumbers ecNumber = new EnzymePortalEcNumbers(BigDecimal.TEN);
        ecNumber.setEcNumber("1.1.1.1");
        ecNumber.setUniprotAccession(obj);
        ecNumber.setEcFamily(1);

        EnzymePortalEcNumbers ecNumber2 = new EnzymePortalEcNumbers(BigDecimal.ONE);
        ecNumber2.setEcNumber("6.3.4.6");
        ecNumber2.setUniprotAccession(obj);
        ecNumber2.setEcFamily(6);

        obj.getEnzymePortalEcNumbersSet().add(ecNumber);
        obj.getEnzymePortalEcNumbersSet().add(ecNumber2);

        boolean expResult = true;
        boolean result = instance.evaluate(obj);
        assertEquals(expResult, result);
        assertTrue(result);

    }

}
