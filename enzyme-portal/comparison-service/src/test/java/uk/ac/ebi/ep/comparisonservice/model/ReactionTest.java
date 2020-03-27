package uk.ac.ebi.ep.comparisonservice.model;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 *
 * @author joseph
 */
public class ReactionTest {

    private Reaction getReaction() {
        return new Reaction("RHEA:46608", "ATP + L-threonyl-[protein] = ADP + H(+) + O-phospho-L-threonyl-[protein]");
    }

    /**
     * Test of getId method, of class Reaction.
     */
    @Test
    public void testGetId() {
        Reaction result = getReaction();
        assertNotNull(result.getId());
    }

    /**
     * Test of getName method, of class Reaction.
     */
    @Test
    public void testGetName() {
        Reaction result = getReaction();
        assertNotNull(result.getName());
    }

    /**
     * Test of toString method, of class Reaction.
     */
    @Test
    public void testToString() {
        Reaction result = getReaction();
        assertNotNull(result.toString());
    }

}
