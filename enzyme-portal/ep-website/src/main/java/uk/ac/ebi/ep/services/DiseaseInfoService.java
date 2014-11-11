package uk.ac.ebi.ep.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.ep.data.service.EnzymePortalService;
import uk.ac.ebi.ep.ebeye.EbeyeService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xwatkins on 10/11/14.
 */
@RestController
public class DiseaseInfoService {

    @Autowired
    protected EnzymePortalService enzymePortalService;
    @Autowired
    protected EbeyeService ebeyeService;

    @RequestMapping("/service/diseases")
    public List<String> getDiseases(@RequestParam(value="name", required = true) String name) {
        if(name != null) {
            name = String.format("%%%s%%", name);
            return enzymePortalService.findDiseasesByName(name);
        } else {
            return new ArrayList();
        }
    }

}
