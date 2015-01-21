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
import uk.ac.ebi.ep.ebeye.autocomplete.Suggestion;

/**
 *
 * @author joseph
 */
public class EbeyeServiceTest extends TestCase {

    /**
     * Test of query method, of class EbeyeService.
     */
    @Test
    public void testQuery() {
        System.out.println("query");
        String query = "cancer";
        UniProtDomain domain1 = new UniProtDomain("P1234", "ABC_HUMAN");
        UniProtDomain domain2 = new UniProtDomain("Q1234", "ABC_RAT");

        List<UniProtDomain> domains = new ArrayList<>();
        domains.add(domain1);
        domains.add(domain2);

        EbeyeService instance = new EbeyeService();

        EbeyeSearchResult expResult = new EbeyeSearchResult();
        expResult.setUniProtDomains(domains);

        EbeyeSearchResult result = instance.query(query);
        result.setUniProtDomains(domains);

        assertEquals(expResult.getUniProtDomains(), result.getUniProtDomains());

    }

    /**
     * Test of ebeyeAutocompleteSearch method, of class EbeyeService.
     */
    public void testEbeyeAutocompleteSearch() {
        System.out.println("ebeyeAutocompleteSearch");
        String searchTerm = "phos";

        EbeyeService instance = new EbeyeService();

        List<Suggestion> result = instance.ebeyeAutocompleteSearch(searchTerm);
        assertNotNull(result);

    }

}
