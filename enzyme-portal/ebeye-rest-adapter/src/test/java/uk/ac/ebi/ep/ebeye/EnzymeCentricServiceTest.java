package uk.ac.ebi.ep.ebeye;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.ebeye.config.EbeyeIndexProps;
import uk.ac.ebi.ep.ebeye.model.enzyme.EnzymeEntry;
import uk.ac.ebi.ep.ebeye.model.enzyme.EnzymeFields;
import uk.ac.ebi.ep.ebeye.model.enzyme.EnzymeSearchResult;
import uk.ac.ebi.ep.ebeye.model.enzyme.Facet;
import uk.ac.ebi.ep.ebeye.model.enzyme.FacetValue;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class EnzymeCentricServiceTest {

    private static final int MAX_ENTRIES_IN_RESPONSE = 4;
    private static final String SERVER_URL = "http://www.myserver.com/ebeye";

    private MockRestServiceServer syncRestServerMock;
    private Function<String, EnzymeEntry> entryCreator;
    private Function<String, Facet> facetCreator;

    private static final int FACET_COUNT = 20;

    private EnzymeCentricService enzymeCentricService;

    @Before
    public void setUp() {
        EbeyeIndexProps serverUrl = new EbeyeIndexProps();
        serverUrl.setEnzymeCentricSearchUrl(SERVER_URL);
        serverUrl.setMaxEbiSearchLimit(MAX_ENTRIES_IN_RESPONSE);

        RestTemplate restTemplate = new RestTemplate();

        syncRestServerMock = MockRestServiceServer.createServer(restTemplate);
        enzymeCentricService = new EnzymeCentricService(restTemplate, serverUrl);

        entryCreator = createEntry();
        facetCreator = createFacet();

    }

    private String buildQueryUrl(String endpoint, String query, int facetCount, String facets, int startPage, int pageSize) {
        
        String ebeyeQueryUrl = "%s?query=%s&facetcount=%d&facets:TAXONOMY&start=%d&size=%d&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,enzyme_family,alt_names,intenz_cofactors&sort=_relevance&reverse=true&format=json";

        if (!StringUtils.isEmpty(facets) && StringUtils.hasText(facets)) {
            //ebeyeQueryUrl = "%s?query=%s&facetcount=%d&facets=%s&start=%d&size=%d&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,compound_name,disease_name,enzyme_family&format=json";
            ebeyeQueryUrl = "%s?query=%s&facetcount=%d&facets=%s&start=%d&size=%d&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,enzyme_family,alt_names,intenz_cofactors&sort=_relevance&reverse=true&format=json";
            return String.format(ebeyeQueryUrl, endpoint, query, facetCount, facets, startPage, pageSize);
        }

//        String ebeyeQueryUrl = "%s?query=%s&facetcount=%d&facets:TAXONOMY&start=%d&size=%d&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,enzyme_family&sort=_relevance&reverse=true&format=json";
//
//        if (!StringUtils.isEmpty(facets) && StringUtils.hasText(facets)) {
//            ebeyeQueryUrl = "%s?query=%s&facetcount=%d&facets=%s&start=%d&size=%d&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,enzyme_family&sort=_relevance&reverse=true&format=json";
//            return String.format(ebeyeQueryUrl, endpoint, query, facetCount, facets, startPage, pageSize);
//        }
        return String.format(ebeyeQueryUrl, endpoint, query, facetCount, startPage, pageSize);
    }

    @Test
    public void query_for_search_result_with_startPage_and_pageSize_and_facetCount_with_NO_limit() throws IOException {

        int limit = 800;
        int startPage = 0;
        int pageSize = 10;
        int facetCount = FACET_COUNT;
        String facetsList = "";
        String query = "query";

        List<EnzymeEntry> entries = createEntries(limit, entryCreator);
        List<Facet> facets = createFacets(facetCount, facetCreator);

        String queryUrl = buildQueryUrl(SERVER_URL, query, facetCount, facetsList, startPage, pageSize);

        EnzymeSearchResult resultSet = createSearchResult(entries, facets);

        mockSuccessfulServerResponse(syncRestServerMock, queryUrl, resultSet);

        EnzymeSearchResult searchResult = enzymeCentricService.getQuerySearchResult(query, startPage, pageSize, facetsList, facetCount);
        List<FacetValue> facetValue = searchResult.getFacets().stream().findAny().get().getFacetValues();

        assertThat(searchResult.getEntries(), hasSize(limit));
        assertThat(searchResult.getFacets(), hasSize(facetCount));
        assertNotNull(searchResult);
        assertThat(searchResult.getEntries(), hasSize(lessThanOrEqualTo(searchResult.getHitCount())));
        assertThat(searchResult.getFacets(), hasSize(greaterThanOrEqualTo(facetCount)));
        assertThat(facetValue, hasSize(facetCount));
        syncRestServerMock.verify();

    }

    @Test
    public void query_for_search_result_with_startPage_and_pageSize_and_facetCount_with_limit() throws IOException {

        int limit = 800;
        int startPage = 0;
        int pageSize = 10;
        int facetCount = FACET_COUNT;
        String facetsList = "";
        String query = "query";

        List<EnzymeEntry> entries = createEntries(limit, entryCreator);
        List<Facet> facets = createFacets(facetCount, facetCreator);

        String queryUrl = buildQueryUrl(SERVER_URL, query, facetCount, facetsList, startPage, pageSize);

        EnzymeSearchResult resultSet = createSearchResult(entries, facets, limit);

        mockSuccessfulServerResponse(syncRestServerMock, queryUrl, resultSet);

        EnzymeSearchResult searchResult = enzymeCentricService.getQuerySearchResult(query, startPage, pageSize, facetsList, facetCount);

        assertNotNull(searchResult);
        assertThat(searchResult.getEntries(), hasSize(lessThanOrEqualTo(searchResult.getHitCount())));
        assertThat(searchResult.getEntries(), hasSize(limit));
        assertThat(searchResult.getFacets(), hasSize(facetCount));
        syncRestServerMock.verify();

    }

    private List<EnzymeEntry> createEntries(int num, Function< String, EnzymeEntry> entryCreator) {
        return IntStream.range(0, num)
                .mapToObj(String::valueOf)
                .map(id -> entryCreator.apply(id))
                .collect(Collectors.toList());
    }

    private List<Facet> createFacets(int num, Function<String, Facet> facetCreator) {
        return IntStream.range(0, num)
                .mapToObj(String::valueOf)
                .map(id -> facetCreator.apply(id))
                .collect(Collectors.toList());
    }

    private EnzymeSearchResult createSearchResult(List<EnzymeEntry> entries, List<Facet> facets, int hitCount) {
        EnzymeSearchResult result = new EnzymeSearchResult();

        result.setEntries(entries);
        result.setHitCount(hitCount);
        result.setFacets(facets);

        return result;
    }

    private EnzymeSearchResult createSearchResult(List<EnzymeEntry> entries, List<Facet> facets) {
        return createSearchResult(entries, facets, entries.size());
    }

    private void mockSuccessfulServerResponse(MockRestServiceServer serverMock, String requestUrl,
            EnzymeSearchResult searchResult)
            throws IOException {

        serverMock.expect(requestTo(requestUrl)).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(convertToJson(searchResult), MediaType.APPLICATION_JSON));
    }

    private String convertToJson(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    private FacetValue buildFacetValue(int x) {
        FacetValue facetValue = new FacetValue();
        facetValue.setCount(x);
        facetValue.setLabel("common_name_" + x);
        facetValue.setValue("human_" + x);
        return facetValue;
    }

    private List<FacetValue> createFacetValue() {
        return IntStream.range(0, FACET_COUNT)
                .mapToObj(String::valueOf)
                .map(facetValue -> buildFacetValue(FACET_COUNT))
                .collect(Collectors.toList());
    }

    private Function<String, Facet> createFacet() {
        List<String> name = new ArrayList<>();
        name.add("title");
        EnzymeFields fields = new EnzymeFields();
        fields.setName(name);
        List<String> sciName = new ArrayList<>();
        sciName.add("homo sapien");
        fields.setScientificName(sciName);

        List<String> family = new ArrayList<>();
        family.add("Ligase");
        fields.setEnzymeFamily(family);

        String id = "1.1.1.";
        Facet facet = new Facet();
        facet.setId("TAXONOMY");
        facet.setLabel("someLabel");
        facet.setFacetValues(createFacetValue());
        return new FunctionFacetImpl(facet);
    }

    private static class FunctionFacetImpl implements Function<String, Facet> {

        private final Facet f;

        public FunctionFacetImpl(Facet ff) {
            this.f = ff;
        }

        @Override
        public Facet apply(String e) {
            return f;
        }
    }

    private Function<String, EnzymeEntry> createEntry() {
        List<String> name = new ArrayList<>();
        name.add("title");
        EnzymeFields fields = new EnzymeFields();
        fields.setName(name);
        List<String> sciName = new ArrayList<>();
        sciName.add("homo sapien");
        fields.setScientificName(sciName);

        List<String> family = new ArrayList<>();
        family.add("Ligase");
        fields.setEnzymeFamily(family);

        String id = "1.1.1.";
        EnzymeEntry entry = new EnzymeEntry(id, "enzymeportal", fields);
        entry.setEnzymeName("enzyme_name_" + id);

        return new FunctionImpl(entry);
    }

    private static class FunctionImpl implements Function<String, EnzymeEntry> {

        private final EnzymeEntry entry;

        public FunctionImpl(EnzymeEntry entry) {
            this.entry = entry;
        }

        @Override
        public EnzymeEntry apply(String e) {
            return entry;
        }
    }

    /**
     * Test of getQuerySearchResult method, of class EnzymeCentricService.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testGetSearchResult() throws IOException {
        int limit = 800;
        int startPage = 0;
        int pageSize = 10;
        int facetCount = FACET_COUNT;
        String facetsList = "";
        String query = "query";

        List<EnzymeEntry> entries = createEntries(limit, entryCreator);
        List<Facet> facets = createFacets(facetCount, facetCreator);

        String queryUrl = buildQueryUrl(SERVER_URL, query, facetCount, facetsList, startPage, pageSize);

        EnzymeSearchResult resultSet = createSearchResult(entries, facets, limit);

        mockSuccessfulServerResponse(syncRestServerMock, queryUrl, resultSet);

        EnzymeSearchResult searchResult = enzymeCentricService.getQuerySearchResult(query, startPage, pageSize, facetsList, facetCount);

        assertNotNull(searchResult);
        assertThat(searchResult.getEntries(), hasSize(lessThanOrEqualTo(searchResult.getHitCount())));
        assertThat(searchResult.getEntries(), hasSize(limit));
        assertThat(searchResult.getFacets(), hasSize(facetCount));
        syncRestServerMock.verify();
    }

    /**
     * Test of findEbiSearchResultsByOmimId method, of class
     * EnzymeCentricService.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testFindEbiSearchResultsByOmimId() throws IOException {

        int limit = 800;
        int startPage = 0;
        int pageSize = 10;
        int facetCount = FACET_COUNT;
        String facetsList = "";
        String omimId = "12345";
        String query = "OMIM:" + omimId;

        List<EnzymeEntry> entries = createEntries(limit, entryCreator);
        List<Facet> facets = createFacets(facetCount, facetCreator);

        String queryUrl = buildQueryUrl(SERVER_URL, query, facetCount, facetsList, startPage, pageSize);

        EnzymeSearchResult resultSet = createSearchResult(entries, facets, limit);

        mockSuccessfulServerResponse(syncRestServerMock, queryUrl, resultSet);

        EnzymeSearchResult searchResult = enzymeCentricService.findEbiSearchResultsByOmimId(omimId, startPage, pageSize, facetsList, facetCount);

        assertNotNull(searchResult);
        assertThat(searchResult.getEntries(), hasSize(lessThanOrEqualTo(searchResult.getHitCount())));
        assertThat(searchResult.getEntries(), hasSize(limit));
        assertThat(searchResult.getFacets(), hasSize(facetCount));
        syncRestServerMock.verify();
    }

    /**
     * Test of findEbiSearchResultsByPathwayId method, of class
     * EnzymeCentricService.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testFindEbiSearchResultsByPathwayId() throws IOException {
        int limit = 800;
        int startPage = 0;
        int pageSize = 10;
        int facetCount = FACET_COUNT;
        String facetsList = "";
        String pathwayId = "R-GGA-189451";
        String query = "REACTOME:" + pathwayId;

        List<EnzymeEntry> entries = createEntries(limit, entryCreator);
        List<Facet> facets = createFacets(facetCount, facetCreator);

        String queryUrl = buildQueryUrl(SERVER_URL, query, facetCount, facetsList, startPage, pageSize);

        EnzymeSearchResult resultSet = createSearchResult(entries, facets, limit);

        mockSuccessfulServerResponse(syncRestServerMock, queryUrl, resultSet);

        EnzymeSearchResult searchResult = enzymeCentricService.findEbiSearchResultsByPathwayId(pathwayId, startPage, pageSize, facetsList, facetCount);

        assertNotNull(searchResult);
        assertThat(searchResult.getEntries(), hasSize(lessThanOrEqualTo(searchResult.getHitCount())));
        assertThat(searchResult.getEntries(), hasSize(limit));
        assertThat(searchResult.getFacets(), hasSize(facetCount));
        syncRestServerMock.verify();
    }

    /**
     * Test of findEbiSearchResultsByEC method, of class EnzymeCentricService.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testFindEbiSearchResultsByEC() throws IOException {
        int limit = 800;
        int startPage = 0;
        int pageSize = 10;
        int facetCount = FACET_COUNT;
        String facetsList = "";
        String ec = "1.1.1.1";
        String query = "INTENZ:" + ec;

        List<EnzymeEntry> entries = createEntries(limit, entryCreator);
        List<Facet> facets = createFacets(facetCount, facetCreator);

        String queryUrl = buildQueryUrl(SERVER_URL, query, facetCount, facetsList, startPage, pageSize);

        EnzymeSearchResult resultSet = createSearchResult(entries, facets, limit);

        mockSuccessfulServerResponse(syncRestServerMock, queryUrl, resultSet);

        EnzymeSearchResult searchResult = enzymeCentricService.findEbiSearchResultsByEC(ec, startPage, pageSize, facetsList, facetCount);

        assertNotNull(searchResult);
        assertThat(searchResult.getEntries(), hasSize(lessThanOrEqualTo(searchResult.getHitCount())));
        assertThat(searchResult.getEntries(), hasSize(limit));
        assertThat(searchResult.getFacets(), hasSize(facetCount));
        syncRestServerMock.verify();
    }

    /**
     * Test of findEbiSearchResultsByTaxId method, of class
     * EnzymeCentricService.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testFindEbiSearchResultsByTaxId() throws IOException {
        int limit = 800;
        int startPage = 0;
        int pageSize = 10;
        int facetCount = FACET_COUNT;
        String facetsList = "";
        String taxId = "9606";
        String query = "TAXONOMY:" + taxId;

        List<EnzymeEntry> entries = createEntries(limit, entryCreator);
        List<Facet> facets = createFacets(facetCount, facetCreator);

        String queryUrl = buildQueryUrl(SERVER_URL, query, facetCount, facetsList, startPage, pageSize);

        EnzymeSearchResult resultSet = createSearchResult(entries, facets, limit);

        mockSuccessfulServerResponse(syncRestServerMock, queryUrl, resultSet);

        EnzymeSearchResult searchResult = enzymeCentricService.findEbiSearchResultsByTaxId(taxId, startPage, pageSize, facetsList, facetCount);

        assertNotNull(searchResult);
        assertThat(searchResult.getEntries(), hasSize(lessThanOrEqualTo(searchResult.getHitCount())));
        assertThat(searchResult.getEntries(), hasSize(limit));
        assertThat(searchResult.getFacets(), hasSize(facetCount));
        syncRestServerMock.verify();
    }

}
