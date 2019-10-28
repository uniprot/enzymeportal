package uk.ac.ebi.ep.web.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.ac.ebi.ep.dataservice.dto.Metabolite;
import uk.ac.ebi.ep.dataservice.dto.MetaboliteView;
import uk.ac.ebi.ep.dataservice.service.EnzymePortalMetaboliteService;

/**
 *
 * @author joseph
 */
@Controller
public class BrowseMetaboliteController {

    private static final String METABOLITES = "/browse-metabolites";

    private static final String BROWSE_METABOLITES = "/browse/metabolites";

    private static final String FIND_METABOLITES_BY_NAME = "/service/metabolites";
    private final EnzymePortalMetaboliteService enzymePortalMetaboliteService;

    @Autowired
    public BrowseMetaboliteController(EnzymePortalMetaboliteService enzymePortalMetaboliteService) {
        this.enzymePortalMetaboliteService = enzymePortalMetaboliteService;
    }

    @GetMapping(value = BROWSE_METABOLITES)
    public String showMetabolites(Model model) {

        List<MetaboliteView> metabolites = enzymePortalMetaboliteService.findMetabolites();

        model.addAttribute("metabolites", metabolites);

        return METABOLITES;

    }

    @ResponseBody
    @GetMapping(value = FIND_METABOLITES_BY_NAME)
    public List<Metabolite> getMetabolitesByNameLike(@RequestParam(value = "name", required = true) String name) {
        if (name != null && name.length() >= 2) {
            return enzymePortalMetaboliteService.findMetaboliteNameLike(name);
        } else {
            return new ArrayList<>();
        }
    }

}
