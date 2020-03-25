package uk.ac.ebi.ep.dataservice.dto;

import java.math.BigInteger;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 *
 * @author joseph
 */
public class ProteinDataTest {

    private ProteinData getProteinData() {
        ProteinData data = new ProteinData();
        data.setAccession("Acc12345");
        data.setCommonName("Human");
        data.setEntryType(Short.valueOf("0"));
        data.setExpEvidenceFlag(BigInteger.ONE);
        data.setSequenceLength(20);
        data.setFunction("function info");
        data.setFunctionLength(BigInteger.TEN);
        data.setProteinGroupId("ENZP1234");
        data.setProteinName("Classic Protein");
        data.setScientificName("Homo Sapien");
        data.setTaxId(9605L);
        data.setSynonymNames("protein synonym");

        return data;
    }

    /**
     * Test of getCommonName method, of class ProteinData.
     */
    @Test
    public void testGetCommonName() {

        ProteinData proteinData = getProteinData();
        assertNotNull(proteinData.getCommonName());
    }

    /**
     * Test of getExpEvidence method, of class ProteinData.
     */
    @Test
    public void testGetExpEvidence() {
        ProteinData proteinData = getProteinData();
        Assertions.assertTrue(proteinData.getExpEvidence());

    }

    /**
     * Test of getSpecies method, of class ProteinData.
     */
    @Test
    public void testGetSpecies() {
        ProteinData proteinData = getProteinData();
        assertNotNull(proteinData.getSpecies());
    }

    /**
     * Test of getSynonym method, of class ProteinData.
     */
    @Test
    public void testGetSynonym() {
        ProteinData proteinData = getProteinData();
        assertNotNull(proteinData.getSynonymNames());
        assertThat(proteinData.getSynonym(), hasSize(greaterThanOrEqualTo(1)));

    }

    /**
     * Test of getAccession method, of class ProteinData.
     */
    @Test
    public void testGetAccession() {
        ProteinData proteinData = getProteinData();
        assertNotNull(proteinData.getAccession());
    }

    /**
     * Test of getTaxId method, of class ProteinData.
     */
    @Test
    public void testGetTaxId() {
        ProteinData proteinData = getProteinData();
        assertNotNull(proteinData.getTaxId());
    }

    /**
     * Test of getProteinName method, of class ProteinData.
     */
    @Test
    public void testGetProteinName() {
        ProteinData proteinData = getProteinData();
        assertNotNull(proteinData.getProteinName());
    }

    /**
     * Test of getScientificName method, of class ProteinData.
     */
    @Test
    public void testGetScientificName() {
        ProteinData proteinData = getProteinData();
        assertNotNull(proteinData.getScientificName());
    }

    /**
     * Test of getSequenceLength method, of class ProteinData.
     */
    @Test
    public void testGetSequenceLength() {
        ProteinData proteinData = getProteinData();
        assertNotNull(proteinData.getSequenceLength());
    }

    /**
     * Test of getFunction method, of class ProteinData.
     */
    @Test
    public void testGetFunction() {
        ProteinData proteinData = getProteinData();
        assertNotNull(proteinData.getFunction());
    }

    /**
     * Test of getEntryType method, of class ProteinData.
     */
    @Test
    public void testGetEntryType() {
        ProteinData proteinData = getProteinData();
        assertNotNull(proteinData.getEntryType());
    }

    /**
     * Test of getFunctionLength method, of class ProteinData.
     */
    @Test
    public void testGetFunctionLength() {
        ProteinData proteinData = getProteinData();
        assertNotNull(proteinData.getFunctionLength());
    }

    /**
     * Test of getSynonymNames method, of class ProteinData.
     */
    @Test
    public void testGetSynonymNames() {
        ProteinData proteinData = getProteinData();
        assertNotNull(proteinData.getSynonymNames());
    }

    /**
     * Test of getExpEvidenceFlag method, of class ProteinData.
     */
    @Test
    public void testGetExpEvidenceFlag() {
        ProteinData proteinData = getProteinData();
        assertNotNull(proteinData.getExpEvidenceFlag());
    }

    /**
     * Test of getProteinGroupId method, of class ProteinData.
     */
    @Test
    public void testGetProteinGroupId() {
        ProteinData proteinData = getProteinData();
        assertNotNull(proteinData.getProteinGroupId());
    }

}
