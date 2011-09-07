/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.uniprot.adapter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author hongcao
 */
public class TransformerTest {

    public TransformerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }


    /**
     * Test of parseTextForMolecules method, of class Transformer.
     */
    @Test
    public void testParseTextForMolecules() {
        System.out.println("parseTextForMolecules");
        //String text = "Completely inhibited by ADP and ADP-glucose, and partially inhibited by ATP and NADH";
        String text = "Activated by threonine and tyrosine phosphorylation by either of two dual specificity kinases, MAP2K3 or MAP2K6, and potentially also MAP2K4. Inhibited by dual specificity phosphatases, such as DUSP1. Specifically inhibited by the binding of pyridinyl-imidazole compounds, which are cytokine-suppressive anti-inflammatory drugs (CSAID). Isoform Mxi2 is 100-fold less sensitive to these agents than the other isoforms and is not inhibited by DUSP1";
        //Transformer.parseTextForActivators(text);
        String[] sentences = text.split("\\.");
        for (String sentence: sentences) {
            if (sentence.contains("Activated by") || sentence.contains("activated by")) {
                Transformer.parseTextForActivators(sentence.trim());
            }
            if (sentence.contains("Inhibited by") || sentence.contains("inhibited by")) {
                Transformer.parseTextForInhibitors(sentence.trim());
            }
        }

        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
        
    }

}