package uk.ac.ebi.ep.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.ac.ebi.ep.dataservice.dto.Pathway;
import uk.ac.ebi.ep.dataservice.dto.PathwayView;
import uk.ac.ebi.ep.dataservice.service.EnzymePortalPathwayService;

/**
 *
 * @author joseph
 */
@Slf4j
@Controller
public class BrowsePathwaysController {

    private static final String PATHWAYS = "/browse-pathways";

    private static final String BROWSE_PATHWAYS = "/browse/pathways";

    private static final String FIND_PATHWAYS_BY_NAME = "/service/pathways";

    private final EnzymePortalPathwayService enzymePortalPathwayService;

    @Autowired
    public BrowsePathwaysController(EnzymePortalPathwayService enzymePortalPathwayService) {
        this.enzymePortalPathwayService = enzymePortalPathwayService;
    }

    @GetMapping(value = BROWSE_PATHWAYS)
    public String showPathways(Model model) {

        List<PathwayView> pathwayList = enzymePortalPathwayService.findPathways()
                .stream()
                .distinct()
                .collect(Collectors.toList());

        model.addAttribute("pathwayList", pathwayList);

        return PATHWAYS;
    }

    /**
     * Note : to access the name & id use pathwayName and pathwayId respectively
     *
     * @param name pathway name
     * @return pathways
     */
    @ResponseBody
    @GetMapping(value = FIND_PATHWAYS_BY_NAME)
    public List<Pathway> getPathwaysByName(@RequestParam(value = "name", required = true) String name) {
        if (name != null && name.length() >= 3) {

            return enzymePortalPathwayService.findPathwaysWithNamesLike(name)
                    .stream()
                    .distinct()
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }


}
