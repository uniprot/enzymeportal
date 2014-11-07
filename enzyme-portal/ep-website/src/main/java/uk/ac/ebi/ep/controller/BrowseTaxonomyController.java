/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.search.model.SearchModel;
import uk.ac.ebi.ep.data.search.model.Taxonomy;

/**
 *
 * @author joseph
 */
@Controller
public class BrowseTaxonomyController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(BrowseTaxonomyController.class);
    private static final String ORGANISMS = "/organisms";
    private static final String BROWSE_TAXONOMY = "/browse/taxonomy";

    private static final String SEARCH_TAXONOMY = "/search/organisms";

    private static final String RESULT = "/tsearch";
    private static final String SEARCH_BY_TAX_ID = "/taxonomy";

    private static final int SEARCH_PAGESIZE = 10;

    @RequestMapping(value = BROWSE_TAXONOMY, method = RequestMethod.GET)
    public String showPathways(Model model) {
        //EnzymeFinder finder = new EnzymeFinder(enzymePortalService, ebeyeService);

        List<Taxonomy> organisms = enzymePortalService.findModelOrganisms();

        SearchModel searchModelForm = searchform();
        model.addAttribute("searchModel", searchModelForm);
        model.addAttribute("organisms", organisms);

        return ORGANISMS;
    }

    @RequestMapping(value = SEARCH_BY_TAX_ID, method = RequestMethod.GET)
    public String searchByTaxId(@ModelAttribute("searchModel") SearchModel searchModel,
            @RequestParam(value = "entryid", required = false) Long entryID, @RequestParam(value = "entryname", required = false) String entryName,
            Model model, HttpSession session, HttpServletRequest request, Pageable pageable, RedirectAttributes attributes) {

        pageable = new PageRequest(0, SEARCH_PAGESIZE);

        Page<UniprotEntry> page = this.enzymePortalService.findEnzymesByTaxonomy(entryID, pageable);

        List<UniprotEntry> result = page.getContent();

        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, page.getTotalPages());

        model.addAttribute("page", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);

        model.addAttribute("organismName", entryName);
        model.addAttribute("taxId", entryID);

        model.addAttribute("summaryEntries", result);

        return RESULT;
    }

    @RequestMapping(value = SEARCH_BY_TAX_ID + "/page={pageNumber}", method = RequestMethod.GET)
    public String searchByTaxIdPaginated(@PathVariable Integer pageNumber, @ModelAttribute("searchModel") SearchModel searchModel,
            @RequestParam(value = "entryid", required = false) Long entryID, @RequestParam(value = "entryname", required = false) String entryName,
            Model model, HttpSession session, HttpServletRequest request, RedirectAttributes attributes) {

        Pageable pageable = new PageRequest(pageNumber - 1, SEARCH_PAGESIZE);

        Page<UniprotEntry> page = this.enzymePortalService.findEnzymesByTaxonomy(entryID, pageable);

        List<UniprotEntry> result = page.getContent();

        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, page.getTotalPages());

        model.addAttribute("page", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);

        model.addAttribute("organismName", entryName);
        model.addAttribute("taxId", entryID);

        model.addAttribute("summaryEntries", result);

        return RESULT;

    }

    @RequestMapping(value = SEARCH_TAXONOMY, method = RequestMethod.POST)
    public String SearchByTaxId(@ModelAttribute("searchModel") SearchModel searchModel,
            @RequestParam(value = "entryid", required = false) Long entryID, @RequestParam(value = "entryname", required = false) String entryName,
            Model model, HttpSession session, HttpServletRequest request, Pageable pageable, RedirectAttributes attributes) {

        return searchByTaxId(searchModel, entryID, entryName, model, session, request, pageable, attributes);

    }

}
