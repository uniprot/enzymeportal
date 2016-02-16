/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import uk.ac.ebi.ep.ebeye.search.EbeyeSearchResult;
import uk.ac.ebi.ep.ebeye.search.Entry;

/**
 *
 * @author joseph
 */
public class EbeyeSearchResultTest extends AbstractEbeyeTest {

    /**
     * Test of getUniProtDomains method, of class EbeyeSearchResult.
     */
    @Test
    public void testGetEntries() {
        logger.info("getEntries");
        EbeyeSearchResult instance = new EbeyeSearchResult();

        Entry entry1 = new Entry();
        Entry entry2 = new Entry();
        List<Entry> expResult = new ArrayList<>();
        expResult.add(entry1);
        expResult.add(entry2);

        instance.getEntries().add(entry1);
        instance.getEntries().add(entry2);

        List<Entry> result = instance.getEntries();
        assertEquals(expResult.size(), result.size());

    }

}
