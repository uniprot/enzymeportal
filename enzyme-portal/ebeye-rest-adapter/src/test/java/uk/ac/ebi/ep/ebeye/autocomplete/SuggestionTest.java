/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye.autocomplete;

import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author joseph
 */
public class SuggestionTest {

    private Suggestion instance;

    @Before
    public void setUp() {
        instance = new Suggestion();

    }

    /**
     * Test of getSuggestion method, of class Suggestion.
     */
    @Test
    public void testGetSuggestion() {
        String expResult = null;
        String result = instance.getSuggestedKeyword();
        assertEquals(expResult, result);

    }

    /**
     * Test of toString method, of class Suggestion.
     */
    @Test
    public void testToString() {

        String expResult = null;
        String result = instance.toString();
        assertEquals(expResult, result);

    }

    /**
     * Test of hashCode method, of class Suggestion.
     */
    @Test
    public void testHashCode() {

        int expResult = 0;
        int result = instance.hashCode();
        assertNotEquals(expResult, result);

    }

    /**
     * Test of equals method, of class Suggestion.
     */
    @Test
    public void testEquals() {
        Object obj = new ArrayList<>();

        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);

    }

}
