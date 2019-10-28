package uk.ac.ebi.ep.web.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.ac.ebi.ep.dataservice.dto.Disease;
import uk.ac.ebi.ep.dataservice.dto.DiseaseView;
import uk.ac.ebi.ep.dataservice.service.DiseaseService;

/**
 * This controller is for browse Enzymes By disease
 *
 * @author joseph
 */
@Controller
public class BrowseDiseasesController {

    private static final String BROWSE = "/browse-diseases";
    private static final String BROWSE_DISEASE = "/browse/disease";

    private static final String FIND_DISEASES_BY_NAME = "/service/diseases";

    private final DiseaseService diseaseService;

    @Autowired
    public BrowseDiseasesController(DiseaseService diseaseService) {
        this.diseaseService = diseaseService;
    }

    @RequestMapping(value = BROWSE_DISEASE, method = RequestMethod.GET)
    public String showDiseases(Model model) {

        List<DiseaseView> diseases = diseaseService.findDiseases();
        model.addAttribute("diseaseList", diseases);

        return BROWSE;
    }

    /**
     * Note: to access name and id, use diseaseName & meshId respectively
     *
     * @param name diseaseName
     * @return Diseases
     */
    @ResponseBody
    @RequestMapping(value = FIND_DISEASES_BY_NAME, method = RequestMethod.GET)
    public List<Disease> getDiseases(@RequestParam(value = "name", required = true) String name) {
        if (name != null && name.length() >= 3) {
            return diseaseService.findDiseasesNameLike(name);
        } else {
            return new ArrayList<>();
        }
    }

}
