package uk.ac.ebi.ep.ebeye.service;

import java.util.List;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
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
import uk.ac.ebi.ep.ebeye.XCentricSetup;
import uk.ac.ebi.ep.ebeye.config.EbeyeConfig;
import uk.ac.ebi.ep.ebeye.config.EbeyeIndexProps;
import uk.ac.ebi.ep.ebeye.model.proteinGroup.ProteinGroupEntry;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {EbeyeConfig.class})
public class ProteinQueryServiceImplIT extends XCentricSetup {

    private ProteinQueryServiceImpl proteinQueryService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private EbeyeIndexProps ebeyeIndexProps;

    @Before
    public void setUp() {
        proteinQueryService = new ProteinGroupService(restTemplate, ebeyeIndexProps);
    }

    /**
     * Test of executeQuery method, of class ProteinQueryServiceImpl.
     */
    @Test
    public void testExecuteQuery() {

        String query = "kinase";

        Observable<ProteinGroupEntry> result = proteinQueryService.executeQuery(query);

        assertNotNull(result);
        assertThat(result.toList().toBlocking().single(), hasSize(greaterThanOrEqualTo(1)));

    }

    /**
     * Test of queryForUniquePrimaryAccessions method, of class
     * ProteinQueryServiceImpl.
     */
    @Test
    public void testQueryForUniquePrimaryAccessions_String_int() {

        String query = "kinase";
        int limit = 10;

        List<String> result = proteinQueryService.queryForUniquePrimaryAccessions(query, limit);

        assertNotNull(result);
        assertThat(result, hasSize(lessThanOrEqualTo(limit)));
        assertThat(result, hasSize(limit));

    }

    /**
     * Test of queryForUniquePrimaryAccessions method, of class
     * ProteinQueryServiceImpl.
     */
    @Test
    public void testQueryForUniquePrimaryAccessions_String() {

        String query = "kinase";
        Observable<String> uniquePrimaryAccessions = proteinQueryService.queryForUniquePrimaryAccessions(query);

        assertNotNull(uniquePrimaryAccessions);
        assertThat(uniquePrimaryAccessions.toList().toBlocking().single(), hasSize(greaterThanOrEqualTo(1)));

    }

}
