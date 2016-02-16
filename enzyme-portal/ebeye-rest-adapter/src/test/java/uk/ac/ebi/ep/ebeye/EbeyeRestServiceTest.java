/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import uk.ac.ebi.ep.ebeye.autocomplete.EbeyeAutocomplete;
import uk.ac.ebi.ep.ebeye.autocomplete.Suggestion;
import uk.ac.ebi.ep.ebeye.search.EbeyeSearchResult;
import uk.ac.ebi.ep.ebeye.search.Entry;

/**
 *
 * @author joseph
 */
public class EbeyeRestServiceTest extends AbstractEbeyeTest {

    @Autowired
    private EbeyeRestService ebeyeRestService;
    
    private static final  String query = "sildenafil";
    private static final String ebeyeJsonFile = "ebeye.json";

    /**
     * Test of ebeyeAutocompleteSearch method, of class EbeyeRestService.
     */
    @Test
    public void testEbeyeAutocompleteSearch() {
        try {
            logger.info("ebeyeAutocompleteSearch");

            String searchTerm = "phos";

            String url = ebeyeIndexUrl.getDefaultSearchIndexUrl() + "/autocomplete?term=" + searchTerm + "&format=json";

            String filename = "suggestions.json";
            String json = getJsonFile(filename);
            
            mockRestServer.expect(requestTo(url)).andExpect(method(HttpMethod.GET))
                    .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

            EbeyeAutocomplete aut = restTemplate.getForObject(url.trim(), EbeyeAutocomplete.class);
            List<Suggestion> expResult = aut.getSuggestions().stream().sorted().collect(Collectors.toList());

            List<Suggestion> result = ebeyeRestService.ebeyeAutocompleteSearch(searchTerm).stream().sorted().collect(Collectors.toList());
            Suggestion suggestion = expResult.stream().sorted().findAny().get();

            mockRestServer.verify();

            assertThat(result, hasItem(suggestion));

        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }

    }

    /**
     * Test of queryEbeyeForAccessions method, of class EbeyeRestService.
     */
    @Test
    public void testQueryEbeyeForAccessions_String() {
        try {
            logger.info("queryEbeyeForAccessions");

            

            String url = ebeyeIndexUrl.getDefaultSearchIndexUrl() + "?format=json&size=100&query=";

            
            String json = getJsonFile(ebeyeJsonFile);

            mockRestServer.expect(requestTo(url)).andExpect(method(HttpMethod.GET))
                    .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

            EbeyeSearchResult searchResult = restTemplate.getForObject(url.trim(), EbeyeSearchResult.class);

           
            Set<String> accessions = new LinkedHashSet<>();

            for (Entry entry : searchResult.getEntries()) {
                accessions.add(entry.getUniprotAccession());
            }

            List<String> expResult = accessions.stream().distinct().collect(Collectors.toList());

            String accession = expResult.stream().findAny().get();

            List<String> result = ebeyeRestService.queryEbeyeForAccessions(query);

            mockRestServer.verify();

            assertThat(result.stream().findAny().get(), containsString(accession));

            assertTrue(result.size() > 0);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }

    }

    /**
     * Test of queryEbeyeForAccessions method, of class EbeyeRestService.
     */
    @Test
    public void testQueryEbeyeForAccessions_String_boolean() {
        logger.info("queryEbeyeForAccessions paginate:false");

        try {

            boolean paginate = false;

            String url = ebeyeIndexUrl.getDefaultSearchIndexUrl() + "?format=json&size=100&query=";

            String json = getJsonFile(ebeyeJsonFile);

            mockRestServer.expect(requestTo(url)).andExpect(method(HttpMethod.GET))
                    .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

            EbeyeSearchResult searchResult = restTemplate.getForObject(url.trim(), EbeyeSearchResult.class);

               Set<String> accessions = new LinkedHashSet<>();

            for (Entry entry : searchResult.getEntries()) {
                accessions.add(entry.getUniprotAccession());
            }

            List<String> expResult = accessions.stream().distinct().collect(Collectors.toList());

            String accession = expResult.stream().findAny().get();

            List<String> result = ebeyeRestService.queryEbeyeForAccessions(query, paginate);

            mockRestServer.verify();

            assertThat(result.stream().findAny().get(), containsString(accession));

            assertTrue(result.size() > 0);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }

    }

    /**
     * Test of queryEbeyeForAccessions method, of class EbeyeRestService.
     */
    @Test
    public void testQueryEbeyeForAccessions_3args() {
        logger.info("queryEbeyeForAccessions paginate :true:limit:yes");

        try {

            int limit = 2;
            boolean paginate = true;
   
            String url = ebeyeIndexUrl.getDefaultSearchIndexUrl() + "?format=json&size=100&query=";

            String json = getJsonFile(ebeyeJsonFile);

            mockRestServer.expect(requestTo(url)).andExpect(method(HttpMethod.GET))
                    .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

            EbeyeSearchResult searchResult = restTemplate.getForObject(url.trim(), EbeyeSearchResult.class);

            Set<String> accessions = new LinkedHashSet<>();

            for (Entry entry : searchResult.getEntries()) {
                accessions.add(entry.getUniprotAccession());
            }

            List<String> expResult = accessions.stream().distinct().collect(Collectors.toList());

            String accession = expResult.stream().findAny().get();

            List<String> result = ebeyeRestService.queryEbeyeForAccessions(query, paginate, limit);
            
            mockRestServer.verify();

            assertThat(result.stream().findAny().get(), containsString(accession));

            assertTrue(result.size() > 0);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }

    }

}
