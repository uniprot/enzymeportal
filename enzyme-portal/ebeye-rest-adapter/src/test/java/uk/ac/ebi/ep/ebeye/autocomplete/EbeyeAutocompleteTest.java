/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye.autocomplete;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author joseph
 */
public class EbeyeAutocompleteTest {

    /**
     * Test of getSuggestions method, of class EbeyeAutocomplete.
     */
    @Test
    public void testGetSuggestions() {

        EbeyeAutocomplete instance = new EbeyeAutocomplete();

        List<Suggestion> expResult = new ArrayList<>();
        List<Suggestion> result = instance.getSuggestions();

        assertEquals(expResult, result);

    }

}
