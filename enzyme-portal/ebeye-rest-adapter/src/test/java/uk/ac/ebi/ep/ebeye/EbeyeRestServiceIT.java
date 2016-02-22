package uk.ac.ebi.ep.ebeye;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.ac.ebi.ep.ebeye.autocomplete.Suggestion;
import uk.ac.ebi.ep.ebeye.config.EbeyeConfig;

/**
 * Tests the behaviour of the {@link EbeyeRestService}.
 *
 * @author joseph
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {EbeyeConfig.class})
public class EbeyeRestServiceIT {
    private static final String QUERY = "kinase";

    @Autowired
    private EbeyeRestService ebeyeRestService;

    @Test
    public void partial_term_phos_sent_to_Ebeye_autocomplete_search_returns_valid_suggestions() throws Exception {
        String searchTerm = "phos";

        List<Suggestion> result =
                ebeyeRestService.ebeyeAutocompleteSearch(searchTerm).stream().sorted().collect(Collectors.toList());

        assertThat(result, hasSize(greaterThan(0)));

        result.forEach(suggestion -> assertThat(suggestion.getSuggestedKeyword(), startsWith(searchTerm)));
    }

    @Test
    public void query_with_limit_of_5_sent_to_ebeyeSearch_returns_at_most_5_accessions() throws Exception {
        int limit = 5;

        List<String> actualAccs = ebeyeRestService.queryEbeyeForUniqueAccessions(QUERY, limit);
        assertThat(actualAccs, hasSize(lessThanOrEqualTo(limit)));
    }

    @Test
    public void query_with_no_limit_sent_to_ebeyeSearch_returns_at_most_5_accession() throws Exception {
        int limit = EbeyeRestService.NO_RESULT_LIMIT;

        List<String> actualAccs = ebeyeRestService.queryEbeyeForUniqueAccessions(QUERY, limit);
        assertThat(actualAccs, hasSize(greaterThan(0)));
    }
}