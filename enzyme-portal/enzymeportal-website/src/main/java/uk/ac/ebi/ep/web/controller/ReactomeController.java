package uk.ac.ebi.ep.web.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;
import uk.ac.ebi.ep.dataservice.service.DataService;
import uk.ac.ebi.ep.enzymeservice.reactome.service.ReactomeService;
import uk.ac.ebi.ep.enzymeservice.reactome.view.PathWay;

/**
 * Ajax workaround for slow reactome services.
 *
 * @author joseph
 *
 */
@Slf4j
@Controller
public class ReactomeController {

    private final ReactomeService reactomeService;
    private final DataService dataService;

    @Autowired
    public ReactomeController(DataService dataService, ReactomeService reactomeService) {
        this.dataService = dataService;
        this.reactomeService = reactomeService;
    }

    @ResponseBody
    @GetMapping(value = "/ajax/reactome/{accession}")
    public Mono<List<PathWay>> getPathways(@PathVariable String accession) {
        //40 pathways found for this entry P30876
        List<String> pathwayIds = dataService.findPathwayIdsByAccession(accession);

        return reactomeService.findPathwaysByIds(pathwayIds);

    }

    @GetMapping(value = "/ajax/reactomeModel/{reactomePathwayId}")
    public String getPathwayModel(Model model, @PathVariable String reactomePathwayId) {

        PathWay pathway = reactomeService.findPathwayById(reactomePathwayId)
                .block();

        model.addAttribute("pathway", pathway);
        return "pathway";
    }


}
