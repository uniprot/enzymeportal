package uk.ac.ebi.ep.ebeye.service;

import java.net.URI;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import uk.ac.ebi.ep.ebeye.ProteinGroupService;
import uk.ac.ebi.ep.ebeye.config.EbeyeConfig;
import uk.ac.ebi.ep.ebeye.config.EbeyeIndexProps;
import uk.ac.ebi.ep.ebeye.model.proteinGroup.ProteinGroupEntry;
import uk.ac.ebi.ep.ebeye.model.proteinGroup.ProteinGroupSearchResult;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {EbeyeConfig.class})
public class QueryServiceIT {

    private QueryService<ProteinGroupSearchResult, ProteinGroupEntry> queryService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private EbeyeIndexProps ebeyeIndexProps;
    final String query = "kinase";
    private String endpoint;
    private String queryTemplate;

    @Before
    public void setUp() {
        queryService = new ProteinGroupService(restTemplate, ebeyeIndexProps);
        endpoint = ebeyeIndexProps.getProteinGroupSearchUrl();
        queryTemplate = "%s?query=%s&size=%d&start=%d&fields=primary_accession&reverse=true&format=json";

    }

    /**
     * Test of executeFirstQuery method, of class QueryService.
     */
    @Test
    public void testExecuteFirstQuery() {

        int resultSize = 20;

        ProteinGroupSearchResult result = queryService.executeFirstQuery(query, queryTemplate, resultSize, endpoint, ProteinGroupSearchResult.class);
        assertNotNull(result);
        assertThat(result.getEntries(), hasSize(resultSize));

    }

    /**
     * Test of executeQueryRequest method, of class QueryService.
     */
    @Test
    public void testExecuteQueryRequest() {
        int resultSize = 20;

        int start = 0;
        int end = 2;

        Observable<URI> reqUrlObs = generateUrlRequests(query, queryTemplate, resultSize, endpoint, start, end);

        assertNotNull(reqUrlObs);
        assertThat(reqUrlObs.toList().toBlocking().single(), hasSize(end));
        Observable<ProteinGroupSearchResult> result = queryService.executeQueryRequest(reqUrlObs, ProteinGroupSearchResult.class);

        assertNotNull(result);
        assertThat(result.toList().toBlocking().single(), hasSize(greaterThanOrEqualTo(1)));

    }

    /**
     * Test of generateUrlRequests method, of class QueryService.
     */
    @Test
    public void testGenerateUrlRequests() {

        int resultSize = 20;
        int start = 0;
        int end = 5;

        Observable<URI> result = generateUrlRequests(query, queryTemplate, resultSize, endpoint, start, end);

        assertNotNull(result);
        assertThat(result.toList().toBlocking().single(), hasSize(greaterThanOrEqualTo(1)));
        assertThat(result.toList().toBlocking().single(), hasSize(end));

    }

    private Observable<URI> generateUrlRequests(String query, String queryTemplate, int resultSize, String endpoint, int start, int end) {
        return queryService.generateUrlRequests(query, queryTemplate, resultSize, endpoint, start, end);
    }

}
