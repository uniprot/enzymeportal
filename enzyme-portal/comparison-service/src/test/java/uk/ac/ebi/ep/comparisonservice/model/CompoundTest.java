package uk.ac.ebi.ep.comparisonservice.model;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import uk.ac.ebi.ep.dataservice.common.CompoundRole;

/**
 *
 * @author joseph
 */
public class CompoundTest {

    private Compound getCompound() {
        return new Compound("CHEBI:18440", "Mg(3+)", "CHEBI", "https://www.ebi.ac.uk/chebi/searchId.do?chebiId=CHEBI:18420", CompoundRole.COFACTOR.getName());

    }

    /**
     * Test of getId method, of class Compound.
     */
    @Test
    public void testGetId() {
        Compound compound = getCompound();
        assertNotNull(compound.getId());
    }

    /**
     * Test of getName method, of class Compound.
     */
    @Test
    public void testGetName() {
        Compound compound = getCompound();
        assertNotNull(compound.getName());
    }

    /**
     * Test of getSource method, of class Compound.
     */
    @Test
    public void testGetSource() {
        Compound compound = getCompound();
        assertNotNull(compound.getSource());
    }

    /**
     * Test of getUrl method, of class Compound.
     */
    @Test
    public void testGetUrl() {
        Compound compound = getCompound();
        assertNotNull(compound.getUrl());
    }

    /**
     * Test of getRole method, of class Compound.
     */
    @Test
    public void testGetRole() {
        Compound compound = getCompound();
        assertNotNull(compound.getRole());
    }

    /**
     * Test of toString method, of class Compound.
     */
    @Test
    public void testToString() {
        Compound compound = getCompound();
        assertNotNull(compound.toString());
    }

}
