package uk.ac.ebi.ep.indexservice.service;

import java.util.List;
import reactor.core.publisher.Mono;
import uk.ac.ebi.ep.indexservice.model.autocomplete.Autocomplete;
import uk.ac.ebi.ep.indexservice.model.autocomplete.Suggestion;

/**
 *
 * @author Joseph
 */
public interface SuggestionService {

    List<Suggestion> findSuggestions(String searchTerm);

    List<Suggestion> getSuggestions(String searchTerm);

    Mono<Autocomplete> autocomplete(String searchTerm);

    Mono<Autocomplete> autocompleteSearch(String searchTerm);
}
