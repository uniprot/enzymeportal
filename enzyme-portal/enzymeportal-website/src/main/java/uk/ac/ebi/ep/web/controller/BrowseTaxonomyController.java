package uk.ac.ebi.ep.web.controller;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.ac.ebi.ep.dataservice.dto.Taxonomy;
import uk.ac.ebi.ep.dataservice.service.EnzymePortalTaxonomyService;

/**
 *
 * @author joseph
 */
@Controller
@Slf4j
public class BrowseTaxonomyController {

    private static final String BROWSE_TAXONOMY_PAGE = "/browse-taxonomy";
    private static final String BROWSE_TAXONOMY = "/browse/taxonomy";

    private static final String GET_COUNT_FOR_ORGANISMS = "/service/taxonomy-service";
    private static final String TAXONOMY_AUTOCOMPLETE_SERVICE = "/service/taxonomy-autocomplete-service";
    private final EnzymePortalTaxonomyService enzymePortalTaxonomyService;

    @Autowired
    public BrowseTaxonomyController(EnzymePortalTaxonomyService enzymePortalTaxonomyService) {
        this.enzymePortalTaxonomyService = enzymePortalTaxonomyService;
    }

    @GetMapping(value = BROWSE_TAXONOMY)
    public String showOrganisms(Model model) {

        return BROWSE_TAXONOMY_PAGE;
    }

    @ResponseBody
    @GetMapping(value = GET_COUNT_FOR_ORGANISMS)
    public List<Taxonomy> getCountForOrganisms() {

        List<Taxonomy> organisms = enzymePortalTaxonomyService.getModelOrganisms();

        return organisms;
    }

    @ResponseBody
    @GetMapping(value = TAXONOMY_AUTOCOMPLETE_SERVICE)
    public List<Taxonomy> getOrganismsByNameLike(@RequestParam(value = "name", required = true) String name) {
        if (name != null && name.length() >= 3) {
            return enzymePortalTaxonomyService.findOrganismsWithNameLike(name);
        } else {
            return new ArrayList<>();
        }
    }

}
