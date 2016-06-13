package uk.ac.ebi.ep.ebeye;

import uk.ac.ebi.ep.ebeye.autocomplete.EbeyeAutocomplete;
import uk.ac.ebi.ep.ebeye.autocomplete.Suggestion;
import uk.ac.ebi.ep.ebeye.config.EbeyeIndexProps;
import uk.ac.ebi.ep.ebeye.utils.Preconditions;

import java.util.List;
import org.springframework.web.client.RestTemplate;

/**
 * Queries the Ebeye search REST server for suggestions based on client queries.
 *
 * @author Ricardo Antunes
 */
public class EbeyeSuggestionService {
    private final EbeyeIndexProps ebeyeIndexProps;
    private final RestTemplate restTemplate;

    public EbeyeSuggestionService(EbeyeIndexProps ebeyeIndexProps, RestTemplate restTemplate) {
        Preconditions.checkArgument(restTemplate != null, "'restTemplate' must not be null");
        Preconditions.checkArgument(ebeyeIndexProps != null, "'ebeyeIndexProps' must not be null");

        this.ebeyeIndexProps = ebeyeIndexProps;
        this.restTemplate = restTemplate;
    }

    /**
     * Generates an RPC call to the EBeye suggestion service in order to produce
     * a list of suggestions for the client to use.
     *
     * @param searchTerm the partial term that will be used by the EBeye service
     * to generate suggestions
     * @return suggestions a list of suggestions generated from the EBeye
     * service
     */
    public List<Suggestion> autocompleteSearch(String searchTerm) {
        String url = ebeyeIndexProps.getEbeyeSearchUrl() + "/autocomplete?term=" + searchTerm + "&format=json";

        EbeyeAutocomplete autocompleteResult = restTemplate.getForObject(url, EbeyeAutocomplete.class);

        return autocompleteResult.getSuggestions();
    }
}
