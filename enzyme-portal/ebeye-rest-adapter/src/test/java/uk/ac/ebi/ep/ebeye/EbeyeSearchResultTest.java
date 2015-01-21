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
    public void testGetUniProtDomains() {
        System.out.println("getUniProtDomains");
        EbeyeSearchResult instance = new EbeyeSearchResult();

        UniProtDomain domain1 = new UniProtDomain();
        UniProtDomain domain2 = new UniProtDomain();
        List<UniProtDomain> expResult = new ArrayList<>();
        expResult.add(domain1);
        expResult.add(domain2);

        instance.getUniProtDomains().add(domain1);
        instance.getUniProtDomains().add(domain2);

        List<UniProtDomain> result = instance.getUniProtDomains();
        assertEquals(expResult.size(), result.size());

    }

}
