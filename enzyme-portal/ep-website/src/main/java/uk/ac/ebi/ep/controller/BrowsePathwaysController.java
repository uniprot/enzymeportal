/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.ac.ebi.ep.base.search.EnzymeFinder;
import uk.ac.ebi.ep.data.domain.EnzymePortalPathways;
import uk.ac.ebi.ep.data.enzyme.model.Pathway;
import uk.ac.ebi.ep.data.search.model.SearchModel;
import uk.ac.ebi.ep.data.search.model.SearchParams;
import uk.ac.ebi.ep.data.search.model.SearchResults;

/**
 *
 * @author joseph
 */
@Controller
public class BrowsePathwaysController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(BrowsePathwaysController.class);

    private static final String PATHWAYS = "/pathways";

    private static final String BROWSE_PATHWAYS = "/browse/pathways";

    private static final String SEARCH_PATHWAYS = "/search-pathways";
    private static final String RESULT = "/search";

    private static final String FIND_PATHWAYS_BY_NAME = "/service/pathways";

    private List<EnzymePortalPathways> pathwayList = new ArrayList<>();

    @RequestMapping(value = BROWSE_PATHWAYS, method = RequestMethod.GET)
    public String showPathways(Model model) {
        EnzymeFinder finder = new EnzymeFinder(enzymePortalService, ebeyeRestService);

        pathwayList = finder.findAllPathways().stream().distinct().collect(Collectors.toList());
        String msg = String.format("Number of pathways found : %s", pathwayList.size());
        LOGGER.debug(msg);

        SearchModel searchModelForm = searchform();
        model.addAttribute("searchModel", searchModelForm);
        model.addAttribute("pathwayList", pathwayList);

        return PATHWAYS;
    }

    @RequestMapping(value = SEARCH_PATHWAYS, method = RequestMethod.GET)
    public String showResults(@ModelAttribute("searchModel") SearchModel searchModel,
            @RequestParam(value = "entryid", required = false) String entryID, @RequestParam(value = "entryname", required = false) String entryName,
            Model model, HttpSession session, HttpServletRequest request) {

        model.addAttribute("entryid", entryID);
        model.addAttribute("entryname", entryName);

        return computeResult(searchModel, entryID, entryName, model, session, request);
    }

    @RequestMapping(value = SEARCH_PATHWAYS, method = RequestMethod.POST)
    public String getSearchResults(@ModelAttribute("searchModel") SearchModel searchModel,
            @RequestParam(value = "entryid", required = false) String entryID, @RequestParam(value = "entryname", required = false) String entryName,
            Model model, HttpSession session, HttpServletRequest request) {

        model.addAttribute("entryid", entryID);
        model.addAttribute("entryname", entryName);
        return computeResult(searchModel, entryID, entryName, model, session, request);

    }

    private String computeResult(@ModelAttribute("searchModel") SearchModel searchModel,
            @RequestParam(value = "entryid", required = false) String entryID, @RequestParam(value = "entryname", required = false) String entryName,
            Model model, HttpSession session, HttpServletRequest request) {

        String view = "error";

        Map<String, SearchResults> prevSearches
                = getPreviousSearches(session.getServletContext());
        String searchKey = getSearchKey(searchModel.getSearchparams());

        SearchResults results = prevSearches.get(searchKey);

        if (results == null) {
            // New search:
            clearHistory(session);
            results = findEnzymesByPathway(entryID, entryName);

        }

        if (results != null) {
            cacheSearch(session.getServletContext(), searchKey, results);
            setLastSummaries(session, results.getSummaryentries());
            searchModel.setSearchresults(results);
            applyFilters(searchModel, request);
             model.addAttribute("searchConfig", searchConfig);
            model.addAttribute("searchModel", searchModel);
            model.addAttribute("pagination", getPagination(searchModel));
            clearHistory(session);
            addToHistory(session, searchModel.getSearchparams().getType(),
                    searchKey);
            view = RESULT;
        }

        return view;
    }

    private SearchResults findEnzymesByPathway(String pathwayId, String pathwayName) {

        SearchResults results = null;
        EnzymeFinder finder = new EnzymeFinder(enzymePortalService, ebeyeRestService);

        SearchParams searchParams = new SearchParams();
        searchParams.setText(pathwayName);
        searchParams.setType(SearchParams.SearchType.KEYWORD);
        searchParams.setStart(0);
        searchParams.setPrevioustext(pathwayName);

        finder.setSearchParams(searchParams);

    
       
            results = finder.computeEnzymeSummariesByPathwayId(pathwayId);
        

        if (results == null) {

            return getEnzymes(finder, searchParams);
        }

        return results;
    }

    private SearchResults getEnzymes(EnzymeFinder finder, SearchParams searchParams) {

        SearchResults results = finder.getEnzymes(searchParams);

        return results;
    }

    /**Note : to access the name & id use pathwayName and pathwayId respectively
     * 
     * @param name pathway name
     * @return pathways
     */
    @ResponseBody
    @RequestMapping(value = FIND_PATHWAYS_BY_NAME, method = RequestMethod.GET)
    public List<Pathway> getPathwaysByName(@RequestParam(value = "name", required = true) String name) {
        if (name != null && name.length()>=3) {
            name = String.format("%%%s%%", name).toLowerCase();
            return enzymePortalService.findPathwaysByName(name).stream().distinct().collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

}
