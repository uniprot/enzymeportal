package uk.ac.ebi.ep.web.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.ac.ebi.ep.dataservice.dto.Cofactor;
import uk.ac.ebi.ep.dataservice.entities.EnzymePortalCofactor;
import uk.ac.ebi.ep.dataservice.service.EnzymePortalCofactorService;

/**
 *
 * @author joseph
 */
@Controller
public class BrowseCofactorController {

    private static final String COFACTORS = "/browse-cofactors";

    private static final String BROWSE_COFACTORS = "/browse/cofactors";

    private static final String FIND_COFACTORS_BY_NAME = "/service/cofactors";

    private final EnzymePortalCofactorService enzymePortalCofactorService;

    @Autowired
    public BrowseCofactorController(EnzymePortalCofactorService enzymePortalCofactorService) {
        this.enzymePortalCofactorService = enzymePortalCofactorService;
    }

    @GetMapping(value = BROWSE_COFACTORS)
    public String showCofactors(Model model) {

        List<EnzymePortalCofactor> cofactors = enzymePortalCofactorService.cofactors();

        model.addAttribute("cofactors", cofactors);

        return COFACTORS;

    }

    @ResponseBody
    @GetMapping(value = FIND_COFACTORS_BY_NAME)
    public List<Cofactor> getCofactorsByNameLike(@RequestParam(value = "name", required = true) String name) {
        if (name != null && name.length() >= 2) {
            return enzymePortalCofactorService.findCofactorsLike(name);
        } else {
            return new ArrayList<>();
        }
    }
}
