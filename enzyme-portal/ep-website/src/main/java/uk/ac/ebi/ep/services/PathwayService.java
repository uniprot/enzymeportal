package uk.ac.ebi.ep.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.ep.base.search.EnzymeFinder;
import uk.ac.ebi.ep.controller.AbstractController;
import uk.ac.ebi.ep.data.domain.EnzymePortalPathways;
import uk.ac.ebi.ep.data.service.EnzymePortalService;
import uk.ac.ebi.ep.ebeye.EbeyeService;

import java.util.ArrayList;
import java.util.List;

/**
* Created by xwatkins on 03/11/14.
*/
@RestController
public class PathwayService {

    @Autowired
    protected EnzymePortalService enzymePortalService;
    @Autowired
    protected EbeyeService ebeyeService;

    @RequestMapping("/service/pathways")
    public List<String> getPathways(@RequestParam(value="name", required = true) String name) {
        if(name != null && name.length() > 4) {
            name = String.format("%%%s%%", name);
            return enzymePortalService.findPathwaysByName(name);
        } else {
            return new ArrayList();
        }
    }

}
