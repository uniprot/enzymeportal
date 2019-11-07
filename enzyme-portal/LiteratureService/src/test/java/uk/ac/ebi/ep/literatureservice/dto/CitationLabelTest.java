package uk.ac.ebi.ep.literatureservice.dto;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author joseph
 */
public class CitationLabelTest {

    /**
     * Test of getDisplayText method, of class CitationLabel.
     */
    @Test
    public void testGetDisplayText() {
        Assert.assertEquals("Diseases", CitationLabel.DISEASES.getDisplayText());

    }

    /**
     * Test of forDatabase method, of class CitationLabel.
     */
    @Test
    public void testForDatabase() {
        CitationLabel l = CitationLabel.forDatabase("UNIPROT");
        Assert.assertEquals(CitationLabel.ENZYME, l);
    }

}
