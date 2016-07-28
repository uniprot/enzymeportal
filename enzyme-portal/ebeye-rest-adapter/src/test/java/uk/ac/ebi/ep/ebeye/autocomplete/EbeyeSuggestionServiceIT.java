
package uk.ac.ebi.ep.ebeye.autocomplete;

import java.util.List;
import java.util.stream.Collectors;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.StringStartsWith.startsWith;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ep.ebeye.EbeyeSuggestionService;
import uk.ac.ebi.ep.ebeye.config.EbeyeConfig;

/**
 * Runs integration tests on the {@link EbeyeSuggestionService} class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {EbeyeConfig.class})
public class EbeyeSuggestionServiceIT {
    @Autowired
    private EbeyeSuggestionService ebeyeSuggestionService;

    @Test
    public void partial_term_phos_sent_to_Ebeye_autocomplete_search_returns_valid_suggestions() {
        String searchTerm = "phos";

        List<Suggestion> result =
                ebeyeSuggestionService.autocompleteSearch(searchTerm).stream().sorted().collect(Collectors.toList());

        assertThat(result, hasSize(greaterThan(0)));

        result.forEach(suggestion -> assertThat(suggestion.getSuggestedKeyword(), startsWith(searchTerm)));
    }
}