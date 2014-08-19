package uk.ac.ebi.ep.core.compare;

import java.util.Arrays;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ListComparisonTest {

    ListComparison comparison;
    
    @Test
    public void testIsDifferent() {
        comparison = new ListComparison(
                Arrays.asList(new String[]{ "1", "2" }),
                Arrays.asList(new String[]{ "3", "4" }));
        assertTrue(comparison.isDifferent());

        comparison = new ListComparison(
                Arrays.asList(new String[]{ "1", "2" }),
                Arrays.asList(new String[]{ "1", "2", "3" }));
        assertTrue(comparison.isDifferent());

        comparison = new ListComparison(
                Arrays.asList(new String[]{ "1", "2" }),
                Arrays.asList(new String[]{ }));
        assertTrue(comparison.isDifferent());

        comparison = new ListComparison(
                Arrays.asList(new String[]{ "1", "2" }),
                null);
        assertTrue(comparison.isDifferent());

        comparison = new ListComparison(
                Arrays.asList(new String[]{ }),
                Arrays.asList(new String[]{ "1", "2" }));
        assertTrue(comparison.isDifferent());

        comparison = new ListComparison(
                null,
                Arrays.asList(new String[]{ "1", "2" }));
        assertTrue(comparison.isDifferent());

        /* not different: */
        
        comparison = new ListComparison(
                Arrays.asList(new String[]{ "1", "2" }),
                Arrays.asList(new String[]{ "1", "2" }));
        assertFalse(comparison.isDifferent());

        comparison = new ListComparison(
                Arrays.asList(new String[]{ "1", "2" }),
                Arrays.asList(new String[]{ "2", "1" }));
        assertFalse(comparison.isDifferent());
    }

}
