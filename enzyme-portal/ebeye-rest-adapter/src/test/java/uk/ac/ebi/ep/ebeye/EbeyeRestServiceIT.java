package uk.ac.ebi.ep.ebeye;

import java.util.List;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import rx.Observable;
import uk.ac.ebi.ep.ebeye.config.EbeyeConfig;
import uk.ac.ebi.ep.ebeye.protein.model.Protein;

/**
 * Tests the behaviour of the {@link EbeyeRestService}.
 *
 * @author joseph
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {EbeyeConfig.class})
public class EbeyeRestServiceIT {

    @Autowired
    private EbeyeRestService ebeyeRestService;

    @Test
    public void query_with_limit_of_5_sent_to_ebeyeSearch_returns_at_most_5_accessions() throws Exception {
        String query = "kinase";
        int limit = 5;

        List<String> actualAccs = ebeyeRestService.queryForUniqueAccessions(query, limit);
        assertThat(actualAccs, hasSize(lessThanOrEqualTo(limit)));
    }

    @Test
    public void query_with_limit_of_800_sent_to_ebeyeSearch_returns_at_most_5_accessions() throws Exception {
        String query = "kinase";
        int limit = 800;

        List<String> actualAccs = ebeyeRestService.queryForUniqueAccessions(query, limit);
        assertThat(actualAccs, hasSize(lessThanOrEqualTo(limit)));
    }

    /**
     * Test of queryForUniqueAccessions method, of class EbeyeRestService.
     */
    @Test
    public void testQuery_with_ec_And_searchTerm_And_limit_of_800_ForUniqueAccessions() {

        String ec = "1.1.1.1";
        String searchTerm = "alcohol";
        int limit = 100;

        List<String> actualAccs = ebeyeRestService.queryForUniqueAccessions(ec, searchTerm, limit);
        assertThat(actualAccs, hasSize(lessThanOrEqualTo(limit)));

    }

    /**
     * Test of queryForUniqueProteins method, of class EbeyeRestService.
     */
    @Test
    public void testQuery_with_ec_and_limit_of_800_ForUniqueProteins() {

        String ec = "6.1.1.1";
        int limit = 10;

        List<Protein> actualUniqueProteins = ebeyeRestService.queryForUniqueProteins(ec, limit);
        assertThat(actualUniqueProteins, hasSize(lessThanOrEqualTo(limit)));
    }

    /**
     * Test of queryForUniqueProteins method, of class EbeyeRestService.
     */
    @Test
    public void testQuery_with_ec_and_searchTerm_and_limit_of_100_ForUniqueProteins_3args() {

        String ec = "1.1.1.1";
        String searchTerm = "alcohol";
        int limit = 100;

        List<Protein> actualUniqueProteins = ebeyeRestService.queryForUniqueProteins(ec, searchTerm, limit);
        assertThat(actualUniqueProteins, hasSize(lessThanOrEqualTo(limit)));

    }

    /**
     * Test of searchForUniqueProteins method, of class EbeyeRestService.
     */
    @Test
    public void testSearchForUniqueProteins() {

        String query = "mtor";
        int limit = 4;

        List<Protein> actualUniqueProteins = ebeyeRestService.searchForUniqueProteins(query, limit);
        assertThat(actualUniqueProteins, hasSize(lessThanOrEqualTo(limit)));

    }

    /**
     * Test of queryForUniqueProteins method, of class EbeyeRestService.
     */
    @Test
    public void testQuery_For_Observable_UniqueProteins() {

        String query = "kinase";

        Observable<Protein> result = ebeyeRestService.queryForUniqueProteins(query);
        List<Protein> actualUniqueProteins = result.toList().toBlocking().single();

        assertNotNull(result);

        assertThat(actualUniqueProteins, hasSize(greaterThan(1)));

    }
}
