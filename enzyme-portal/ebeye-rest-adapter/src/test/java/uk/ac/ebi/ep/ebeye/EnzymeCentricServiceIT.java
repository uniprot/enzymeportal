package uk.ac.ebi.ep.ebeye;

import uk.ac.ebi.ep.ebeye.config.EbeyeConfig;

import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;

/**
 * Tests the behaviour of the {@link EnzymeCentricService}.
 *
 * @author joseph
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {EbeyeConfig.class})
public class EnzymeCentricServiceIT {
    @Autowired
    private EnzymeCentricService enzymeCentricService;

    @Test
    public void query_with_limit_of_5_sent_to_ebeyeSearch_returns_at_most_5_accessions() throws Exception {
        String query = "kinase";
        int limit = 5;

        List<String> actualAccs = enzymeCentricService.queryEbeyeForEcNumbers(query, limit);
        assertThat(actualAccs, hasSize(lessThanOrEqualTo(limit)));
    }

    @Test
    public void query_with_limit_of_800_sent_to_ebeyeSearch_returns_at_most_5_accessions() throws Exception {
        String query = "kinase";
        int limit = 800;

        List<String> actualAccs = enzymeCentricService.queryEbeyeForEcNumbers(query, limit);
        assertThat(actualAccs, hasSize(lessThanOrEqualTo(limit)));
    }
}