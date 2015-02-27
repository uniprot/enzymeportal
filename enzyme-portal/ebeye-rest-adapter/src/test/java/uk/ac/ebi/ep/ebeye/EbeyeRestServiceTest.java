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
public class EbeyeRestServiceTest extends EbeyeBaseTest {

    @Autowired
    private EbeyeRestService ebeyeRestService;
    
    private final  String query = "sildenafil";
    private final String ebeyeJsonFile = "ebeye.json";

    /**
     * Test of ebeyeAutocompleteSearch method, of class EbeyeRestService.
     */
    @Test
    public void testEbeyeAutocompleteSearch() {
        try {
            LOGGER.info("ebeyeAutocompleteSearch");

            String searchTerm = "phos";

            String url = ebeyeIndexUrl.getDefaultSearchIndexUrl() + "/autocomplete?term=" + searchTerm + "&format=json";

            String filename = "suggestions.json";
            String json = getJsonFile(filename);

            mockRestServer.expect(requestTo(url)).andExpect(method(HttpMethod.GET))
                    .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

            EbeyeAutocomplete expResult = restTemplate.getForObject(url.trim(), EbeyeAutocomplete.class);

            List<Suggestion> result = ebeyeRestService.ebeyeAutocompleteSearch(searchTerm);

            String suggestion = expResult.getSuggestions().stream().findAny().get().getSuggestedKeyword();

            mockRestServer.verify();

            assertThat(result.stream().findAny().get().getSuggestedKeyword(), containsString(suggestion));

            assertEquals(expResult.getSuggestions(), result);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

    }

    /**
     * Test of queryEbeyeForAccessions method, of class EbeyeRestService.
     */
    @Test
    public void testQueryEbeyeForAccessions_String() {
        try {
            LOGGER.info("queryEbeyeForAccessions");

            

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

            assertEquals(expResult, result);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

    }

    /**
     * Test of queryEbeyeForAccessions method, of class EbeyeRestService.
     */
    @Test
    public void testQueryEbeyeForAccessions_String_boolean() {
        LOGGER.info("queryEbeyeForAccessions paginate:false");

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

            assertEquals(expResult, result);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

    }

    /**
     * Test of queryEbeyeForAccessions method, of class EbeyeRestService.
     */
    @Test
    public void testQueryEbeyeForAccessions_3args() {
        LOGGER.info("queryEbeyeForAccessions paginate :true:limit:yes");

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

            assertEquals(expResult, result);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

    }

}
