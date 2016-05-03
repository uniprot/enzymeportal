package uk.ac.ebi.ep.ebeye.autocomplete;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * @author joseph
 */
public class EbeyeAutocompleteTest {
    /**
     * Test of getSuggestions method, of class EbeyeAutocomplete.
     */
    @Test
    public void testGetSuggestions() {

        EbeyeAutocomplete instance = new EbeyeAutocomplete();
        Suggestion suggestion = new Suggestion("phos");
        instance.getSuggestions().add(suggestion);

        List<Suggestion> expResult = new ArrayList<>();
        expResult.add(suggestion);
        List<Suggestion> result = instance.getSuggestions();

        assertEquals(expResult, result);
    }
}