package uk.ac.ebi.ep.dataservice.dto;

import java.math.BigInteger;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 *
 * @author joseph
 */
public class TaxonomyTest {

    private Taxonomy getTaxonomy() {

        return new Taxonomy(9605L, "Homo Sapien", "Human", BigInteger.TEN);

    }

    /**
     * Test of getTaxId method, of class Taxonomy.
     */
    @Test
    public void testGetTaxId() {
        Taxonomy taxonomy = getTaxonomy();
        assertNotNull(taxonomy.getTaxId());
    }

    /**
     * Test of getScientificName method, of class Taxonomy.
     */
    @Test
    public void testGetScientificName() {
        Taxonomy taxonomy = getTaxonomy();
        assertNotNull(taxonomy.getScientificName());
    }

    /**
     * Test of getCommonName method, of class Taxonomy.
     */
    @Test
    public void testGetCommonName() {
        Taxonomy taxonomy = getTaxonomy();
        assertNotNull(taxonomy.getCommonName());
    }

    /**
     * Test of getNumEnzymes method, of class Taxonomy.
     */
    @Test
    public void testGetNumEnzymes() {
        Taxonomy taxonomy = getTaxonomy();
        assertNotNull(taxonomy.getNumEnzymes());
    }

}
