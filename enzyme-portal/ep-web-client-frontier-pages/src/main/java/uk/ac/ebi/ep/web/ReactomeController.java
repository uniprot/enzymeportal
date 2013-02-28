package uk.ac.ebi.ep.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import uk.ac.ebi.ep.adapter.reactome.ReactomeConfig;
import uk.ac.ebi.ep.adapter.reactome.ReactomeWsCallable;
import uk.ac.ebi.ep.enzyme.model.Pathway;

/**
 * Ajax workaround for slow reactome services.
 * @author rafa
 *
 */
@Controller
public class ReactomeController {

    @Autowired
    private ReactomeConfig reactomeConfig;

    @RequestMapping(value="/ajax/reactome/{reactomePathwayId}")
    protected String getPathway(Model model,
    		@PathVariable String reactomePathwayId)
	throws Exception{
    	ReactomeWsCallable callable =
    			new ReactomeWsCallable(reactomeConfig, reactomePathwayId);
        Pathway pathway = callable.call();     
        model.addAttribute("pathway", pathway);
        return "pathway";
    }

}
