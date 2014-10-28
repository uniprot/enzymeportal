/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import uk.ac.ebi.biobabel.util.collections.ChemicalNameComparator;
import uk.ac.ebi.ep.base.search.EnzymeFinder;
import uk.ac.ebi.ep.data.domain.EnzymePortalDisease;
import uk.ac.ebi.ep.data.search.model.SearchModel;
import uk.ac.ebi.ep.data.search.model.SearchParams;
import uk.ac.ebi.ep.data.search.model.SearchResults;

/**
 * This controller is for browse Enzymes By disease
 *
 * @author joseph
 */
@Controller
public class BrowseController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(BrowseController.class);
    private static final String BROWSE = "/browse";
    private static final String BROWSE_DISEASE = "/browse/disease";
    private static final String SEARCH_DISEASE = "/search/disease";
    private static final String RESULT = "/search_result_disease";

    private List<EnzymePortalDisease> diseaseList = new ArrayList<>();

    @RequestMapping(value = BROWSE_DISEASE, method = RequestMethod.GET)
    public String showDiseases(Model model) {
        EnzymeFinder finder = new EnzymeFinder(enzymePortalService, ebeyeService);

       
        diseaseList = finder.findDiseases();
     

        SearchModel searchModelForm = searchform();
        model.addAttribute("searchModel", searchModelForm);
        model.addAttribute("diseaseList", diseaseList);

        return BROWSE;
    }

    @RequestMapping(value = BROWSE_DISEASE + "/{startsWith}", method = RequestMethod.GET)
    public String showDiseasesLike(@PathVariable(value = "startsWith") String startsWith, Model model) {

        Set<EnzymePortalDisease> selectedDiseases = new TreeSet<>(SORT_DISEASES);

        for (EnzymePortalDisease disease : diseaseList) {
            if (disease.getName().startsWith(startsWith.toLowerCase())) {
                selectedDiseases.add(disease);
            }
            if (startsWithDigit(disease.getName())) {
                String current = disease.getName().replaceAll("(-)?\\d+(\\-\\d*)?", "").trim();
                if (current.startsWith(startsWith.toLowerCase())) {
                    selectedDiseases.add(disease);
                }
            }
        }

        SearchModel searchModelForm = searchform();
        model.addAttribute("searchModel", searchModelForm);

        model.addAttribute("alldiseaseList", selectedDiseases);
        model.addAttribute("startsWith", startsWith.toUpperCase());

        return BROWSE;
    }

    @RequestMapping(value = SEARCH_DISEASE, method = RequestMethod.GET)
    public String showResults(@ModelAttribute("searchModel") SearchModel searchModel,
            @RequestParam(value = "entryid", required = false) String entryID, @RequestParam(value = "entryname", required = false) String entryName,
            Model model, HttpSession session, HttpServletRequest request) {

        model.addAttribute("entryid", entryID);
        model.addAttribute("entryname", entryName);

        return computeResult(searchModel, entryID,entryName, model, session, request);
    }

    @RequestMapping(value = SEARCH_DISEASE, method = RequestMethod.POST)
    public String getSearchResults(@ModelAttribute("searchModel") SearchModel searchModel,
            @RequestParam(value = "entryid", required = false) String entryID, @RequestParam(value = "entryname", required = false) String entryName,
            Model model, HttpSession session, HttpServletRequest request) {

        model.addAttribute("entryid", entryID);
        model.addAttribute("entryname", entryName);
        return computeResult(searchModel, entryID,entryName, model, session, request);

    }

    private String computeResult(@ModelAttribute("searchModel") SearchModel searchModel,
            @RequestParam(value = "entryid", required = false) String entryID,@RequestParam(value = "entryname", required = false) String entryName,
            Model model, HttpSession session, HttpServletRequest request) {

        String view = "error";

        Map<String, SearchResults> prevSearches
                = getPreviousSearches(session.getServletContext());
        String searchKey = getSearchKey(searchModel.getSearchparams());

        SearchResults results = prevSearches.get(searchKey);

        if (results == null) {
            // New search:
            clearHistory(session);
            results = findEnzymesByDisease(entryID,entryName);

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

    private SearchResults findEnzymesByDisease(String diseaseId, String diseaseName) {

        SearchResults results = null;
        EnzymeFinder finder = new EnzymeFinder(enzymePortalService, ebeyeService);

        
        List<String> accessions = enzymePortalService.findAccessionsByDisease(diseaseId);
        

        if (accessions != null ) {



            SearchParams searchParams = new SearchParams();
            searchParams.setText(diseaseName);
            searchParams.setType(SearchParams.SearchType.KEYWORD);
            searchParams.setStart(0);
            searchParams.setPrevioustext(diseaseName);

            finder.setSearchParams(searchParams);

            if (accessions.size() > 0) {

                results = finder.computeEnzymeSummariesByAccessions(accessions);
              

            } else if (accessions.isEmpty()) {
                //if not found at mm, search via ebeye using the enzyme name as keyword
                return getEnzymes(finder, searchParams);
            }
        }

        return results;
    }

    private SearchResults getEnzymes(EnzymeFinder finder, SearchParams searchParams) {

        SearchResults results = finder.getEnzymes(searchParams);

        return results;
    }

    private boolean startsWithDigit(String data) {
        return Character.isDigit(data.charAt(0));
    }
    private static final Comparator<String> NAME_COMPARATOR = new ChemicalNameComparator();
    static final Comparator<EnzymePortalDisease> SORT_DISEASES = new Comparator<EnzymePortalDisease>() {
        @Override
        public int compare(EnzymePortalDisease d1, EnzymePortalDisease d2) {

            if (d1.getName() == null && d2.getName() == null) {

                return NAME_COMPARATOR.compare(d1.getName(), d2.getName());
            }
            int compare = NAME_COMPARATOR.compare(d1.getName(), d2.getName());

            return ((compare == 0) ? NAME_COMPARATOR.compare(d1.getName(), d2.getName()) : compare);

        }
    };
}
