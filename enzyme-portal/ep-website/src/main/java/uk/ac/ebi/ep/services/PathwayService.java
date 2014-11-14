package uk.ac.ebi.ep.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.ep.data.service.EnzymePortalService;
import uk.ac.ebi.ep.ebeye.EbeyeService;

/**
* Created by xwatkins on 03/11/14.
*/
@RestController
public class PathwayService {

    @Autowired
    protected EnzymePortalService enzymePortalService;
    @Autowired
    protected EbeyeService ebeyeService;

//    @RequestMapping("/service/pathways")
//    public List<String> getPathways(@RequestParam(value="name", required = true) String name) {
//        if(name != null) {
//            name = String.format("%%%s%%", name);
//            return enzymePortalService.findPathwaysByName(name);
//        } else {
//            return new ArrayList();
//        }
//    }

}
