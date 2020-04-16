package uk.ac.ebi.ep.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;
import uk.ac.ebi.ep.indexservice.model.autocomplete.Autocomplete;
import uk.ac.ebi.ep.indexservice.model.autocomplete.Suggestion;
import uk.ac.ebi.ep.indexservice.service.SuggestionService;

/**
 *
 * @author joseph
 */
@Controller
public class AutocompleteSearchController {

    private final SuggestionService suggestionService;

    @Autowired
    public AutocompleteSearchController(SuggestionService suggestionService) {
        this.suggestionService = suggestionService;
    }

    @ResponseBody
    @GetMapping("/service/search")
    public List<Suggestion> autocompleteSearchJsonResonse(@RequestParam(value = "name", required = true) String name) {
        if (name != null && name.length() >= 3) {
            String keyword = name.toLowerCase();
            if (keyword.contains(":")) {

                return suggest(keyword);
            }

            return findSuggestions(keyword)
                    .stream()
                    .distinct()
                    .collect(Collectors.toList());

        }

        return new ArrayList<>();

    }

    @ResponseBody
    @PostMapping("/service/auto")
    public Mono<Autocomplete> autocompleteSearchReactiveJsonResonse(@RequestParam(value = "name", required = true) String name) {

        if (name != null && name.length() >= 3) {
            String keyword = name.toLowerCase();
            if (keyword.contains(":")) {

                return specialCaseAutoComplete(keyword);
            }
            return autocompleteSearch(keyword);

        }

        return Mono.empty();
    }

    private Mono<Autocomplete> autocompleteSearch(String keyword) {
        String cleanedKeyword = Jsoup.clean(keyword, Whitelist.basic());
        return suggestionService.autocompleteSearch(cleanedKeyword.trim());
    }

    private Mono<Autocomplete> specialCaseAutoComplete(String keyword) {

        keyword = "\"" + keyword.toLowerCase() + "\"";
        return autocompleteSearch(keyword);

    }

    private List<Suggestion> findSuggestions(String keyword) {
        String cleanedKeyword = Jsoup.clean(keyword, Whitelist.basic());
        return suggestionService.findSuggestions(cleanedKeyword.trim());
    }

    private List<Suggestion> suggest(String keyword) {

        keyword = "\"" + keyword.toLowerCase() + "\"";
        return findSuggestions(keyword)
                .stream()
                .distinct()
                .map(s -> s.getSuggestedKeyword()
                .replace(" ", ":"))
                .map(r -> new Suggestion(r))
                .collect(Collectors.toList());

    }

}
