/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 * @author joseph
 */
public class EbeyeSearchResultTest extends TestCase {

    /**
     * Test of getUniProtDomains method, of class EbeyeSearchResult.
     */
    @Test
    public void testGetEntries() {
        System.out.println("getEntries");
        EbeyeSearchResult instance = new EbeyeSearchResult();

        Entry domain1 = new Entry();
        Entry domain2 = new Entry();
        List<Entry> expResult = new ArrayList<>();
        expResult.add(domain1);
        expResult.add(domain2);

        instance.getEntries().add(domain1);
        instance.getEntries().add(domain2);

        List<Entry> result = instance.getEntries();
        assertEquals(expResult.size(), result.size());

    }

}
