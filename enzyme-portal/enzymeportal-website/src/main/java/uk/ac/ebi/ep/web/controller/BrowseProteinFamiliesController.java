package uk.ac.ebi.ep.web.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.ac.ebi.ep.dataservice.dto.ProteinFamily;
import uk.ac.ebi.ep.dataservice.dto.ProteinFamilyView;
import uk.ac.ebi.ep.dataservice.service.EnzymePortalFamilyNameService;

/**
 *
 * @author Joseph
 */
@Controller
public class BrowseProteinFamiliesController {

    private static final String FAMILIES = "/browse-families";

    private static final String BROWSE_FAMILIES = "/browse/families";

    private static final String FIND_FAMILIES_BY_NAME = "/service/families";

    private final EnzymePortalFamilyNameService enzymePortalFamilyNameService;

    @Autowired
    public BrowseProteinFamiliesController(EnzymePortalFamilyNameService enzymePortalFamilyNameService) {
        this.enzymePortalFamilyNameService = enzymePortalFamilyNameService;
    }

    @GetMapping(value = BROWSE_FAMILIES)
    public String showProteinFamilies(Model model) {

        List<ProteinFamilyView> uniprotFamilies = enzymePortalFamilyNameService.findProteinFamilies();

        model.addAttribute("uniprotFamilies", uniprotFamilies);

        return FAMILIES;
    }

    @ResponseBody
    @GetMapping(value = FIND_FAMILIES_BY_NAME)
    public List<ProteinFamily> getProteinFamiliesByNameLike(@RequestParam(value = "name", required = true) String name) {
        if (name != null && name.length() >= 3) {
            return enzymePortalFamilyNameService.findProteinFamiliesWithNamesLike(name);
        } else {
            return new ArrayList<>();
        }
    }
}
