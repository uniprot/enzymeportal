package uk.ac.ebi.ep.dataservice.dto;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 *
 * @author joseph
 */
public class SpeciesTest {

    private Species getSpecies() {

        return new Species("Homo Sapien", "Human", 9706L);

    }

    /**
     * Test of compareTo method, of class Species.
     */
    @Test
    public void testCompareTo() {
        Species species = getSpecies();
        Species sp = new Species("Mus cus", "Mouse", 5806L);
        int compare = species.compareTo(sp);
        Assertions.assertNotEquals(1, compare);

    }

    /**
     * Test of getScientificname method, of class Species.
     */
    @Test
    public void testGetScientificname() {
        Species species = getSpecies();
        assertNotNull(species.getScientificname());
    }

    /**
     * Test of getCommonname method, of class Species.
     */
    @Test
    public void testGetCommonname() {
        Species species = getSpecies();
        assertNotNull(species.getCommonname());
    }

    /**
     * Test of isSelected method, of class Species.
     */
    @Test
    public void testIsSelected() {
        Species species = getSpecies();
        Assertions.assertFalse(species.isSelected());

    }

    /**
     * Test of getTaxId method, of class Species.
     */
    @Test
    public void testGetTaxId() {
        Species species = getSpecies();
        assertNotNull(species.getTaxId());
    }

    /**
     * Test of toString method, of class Species.
     */
    @Test
    public void testToString() {
        Species species = getSpecies();
        assertNotNull(species.toString());
        Assertions.assertEquals("Species(scientificname=Homo Sapien, commonname=Human, taxId=9706)", species.toString());

    }

}
