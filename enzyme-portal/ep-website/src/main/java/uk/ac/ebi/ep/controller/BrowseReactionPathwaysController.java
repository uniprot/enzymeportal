/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import uk.ac.ebi.ep.base.search.EnzymeFinder;
import uk.ac.ebi.ep.data.domain.EnzymePortalReaction;
import uk.ac.ebi.ep.data.search.model.SearchModel;
import uk.ac.ebi.ep.data.search.model.SearchParams;
import uk.ac.ebi.ep.data.search.model.SearchResults;

/**
 *
 * @author joseph
 */
@Controller
@Deprecated
public class BrowseReactionPathwaysController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(BrowseReactionPathwaysController.class);

    private static final String REACTIONS = "/reactions";
   

    private static final String BROWSE_REACTIONS = "/browse/reactions";
   

   private static final String SEARCH_REACTIONS = "/search/reactions";

   
   
    private static final String RESULT = "/search"; 
    
    private List<EnzymePortalReaction> reactionList = new ArrayList<>();
  

    @RequestMapping(value = BROWSE_REACTIONS, method = RequestMethod.GET)
    public String showReactions(Model model) {
        EnzymeFinder finder = new EnzymeFinder(enzymePortalService, ebeyeService);

        reactionList = finder.findAllReactions();

        SearchModel searchModelForm = searchform();
        model.addAttribute("searchModel", searchModelForm);
        model.addAttribute("reactionList", reactionList);

        return REACTIONS;
    }


    
   
    
       @RequestMapping(value = SEARCH_REACTIONS, method = RequestMethod.GET)
    public String showResults(@ModelAttribute("searchModel") SearchModel searchModel,
            @RequestParam(value = "entryid", required = false) String entryID, @RequestParam(value = "entryname", required = false) String entryName,
            Model model, HttpSession session, HttpServletRequest request) {

        model.addAttribute("entryid", entryID);
        model.addAttribute("entryname", entryName);

        return computeResult(searchModel, entryID, entryName, model, session, request);
    }

    @RequestMapping(value = SEARCH_REACTIONS, method = RequestMethod.POST)
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
            results = findEnzymesByReaction(entryID, entryName);

        }

        if (results != null) {
            cacheSearch(session.getServletContext(), searchKey, results);
            setLastSummaries(session, results.getSummaryentries());
            searchModel.setSearchresults(results);
            applyFilters(searchModel, request);
            model.addAttribute("searchModel", searchModel);
            model.addAttribute("pagination", getPagination(searchModel));
            clearHistory(session);
            addToHistory(session, searchModel.getSearchparams().getType(),
                    searchKey);
            view = RESULT;
        }

        return view;
    } 
    
       private SearchResults findEnzymesByReaction(String reactionId, String reactionName) {

        SearchResults results = null;
        EnzymeFinder finder = new EnzymeFinder(enzymePortalService, ebeyeService);

        SearchParams searchParams = new SearchParams();
        searchParams.setText(reactionName);
        searchParams.setType(SearchParams.SearchType.KEYWORD);
        searchParams.setStart(0);
        searchParams.setPrevioustext(reactionName);

        finder.setSearchParams(searchParams);

        List<String> accessions = enzymePortalService.findAccessionsByReactionId(reactionId);

        if (!accessions.isEmpty()) {
            //results = finder.computeEnzymeSummariesByAccessions(accessions);
        }

        if (results == null) {

            return getEnzymes(finder, searchParams);
        }

        return results;
    }

    private SearchResults getEnzymes(EnzymeFinder finder, SearchParams searchParams) {

        SearchResults results = finder.getEnzymes(searchParams);

        return results;
    }

}
