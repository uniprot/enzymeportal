package uk.ac.ebi.ep.core.compare;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringComparisonTest {

    StringComparison comparison;

    @Test
    public void testIsDifferent() {
        comparison = new StringComparison("a", "b");
        assertTrue(comparison.isDifferent());
        
        comparison = new StringComparison(null, "b");
        assertTrue(comparison.isDifferent());
        
        comparison = new StringComparison("a", null);
        assertTrue(comparison.isDifferent());
        
        comparison = new StringComparison("ab", "ab");
        assertFalse(comparison.isDifferent());
    }

}
