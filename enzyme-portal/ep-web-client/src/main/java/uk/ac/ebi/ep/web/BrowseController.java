/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.web;

import java.util.ArrayList;
import java.util.Collection;
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
import uk.ac.ebi.ep.core.search.EnzymeFinder;
import uk.ac.ebi.ep.mm.CustomXRef;
import uk.ac.ebi.ep.mm.Entry;
import uk.ac.ebi.ep.search.exception.EnzymeFinderException;
import uk.ac.ebi.ep.search.model.SearchModel;
import uk.ac.ebi.ep.search.model.SearchParams;
import uk.ac.ebi.ep.search.model.SearchResults;

/**
 * This controller is for browse Enzymes By disease and Ec classification
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
    private Set<uk.ac.ebi.ep.search.model.Disease> diseaseList = new TreeSet<>();

    private SearchResults findEnzymesByEntry(String entry_id) {

        SearchResults results = null;
        EnzymeFinder finder = new EnzymeFinder(searchConfig);
        finder.getUniprotAdapter().setConfig(uniprotConfig);
        finder.getIntenzAdapter().setConfig(intenzConfig);
        finder.getEbeyeAdapter().setConfig(ebeyeConfig);


        Entry entry = finder.findEntryById(entry_id);//get the entry id (disease or ec)


        List<String> ids = new ArrayList<>();
        Collection<CustomXRef> xrefResult = null;
        //int total = 0;
        if (entry != null) {

            xrefResult = finder.getXrefs(entry);
            for (CustomXRef ref : xrefResult) {
                ids = ref.getIdList();
                //total = ref.getResult_count();


            }
        }



        SearchParams searchParams = new SearchParams();
        searchParams.setText(entry.getEntryName());
        searchParams.setType(SearchParams.SearchType.KEYWORD);
        searchParams.setStart(0);
        searchParams.setPrevioustext(entry.getEntryName());

        finder.setSearchParams(searchParams);

        if (ids.size() > 0) {

            results = finder.computeEnzymeSummary(ids, new SearchResults());

        } else if (ids.isEmpty()) {
            //if not found at mm, search via ebeye using the enzyme name as keyword
            return getEnzymes(finder, searchParams);
        }

        return results;
    }

    private SearchResults getEnzymes(EnzymeFinder finder, SearchParams searchParams) {
        SearchResults results = null;

        try {
            results = finder.getEnzymes(searchParams);
        } catch (EnzymeFinderException ex) {

            LOGGER.fatal("ERROR while searching for enzymes", ex);
        }
        return results;
    }

    private String computeResult(@ModelAttribute("searchModel") SearchModel searchModel,
            @RequestParam(value = "entryid", required = false) String entryID,
            Model model, HttpSession session, HttpServletRequest request) {

        String view = "error";

        Map<String, SearchResults> prevSearches =
                getPreviousSearches(session.getServletContext());
        String searchKey = getSearchKey(searchModel.getSearchparams());

        SearchResults results = prevSearches.get(searchKey);

        if (results == null) {
            // New search:
            clearHistory(session);
            results = findEnzymesByEntry(entryID);

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

    @RequestMapping(value = BROWSE_DISEASE, method = RequestMethod.GET)
    public String showDiseases(Model model) {
        EnzymeFinder finder = new EnzymeFinder(searchConfig);

        diseaseList = finder.findAllDiseases();

        SearchModel searchModelForm = searchform();
        model.addAttribute("searchModel", searchModelForm);
        model.addAttribute("diseaseList", diseaseList);

        return BROWSE;
    }

    @RequestMapping(value = BROWSE_DISEASE + "/{startsWith}", method = RequestMethod.GET)
    public String showDiseasesLike(@PathVariable(value = "startsWith") String startsWith, Model model) {

        Set<uk.ac.ebi.ep.search.model.Disease> selectedDiseases = new TreeSet<>(SORT_DISEASES);

        for (uk.ac.ebi.ep.search.model.Disease disease : diseaseList) {
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

        return computeResult(searchModel, entryID, model, session, request);
    }

    @RequestMapping(value = SEARCH_DISEASE, method = RequestMethod.POST)
    public String getSearchResults(@ModelAttribute("searchModel") SearchModel searchModel,
            @RequestParam(value = "entryid", required = false) String entryID, @RequestParam(value = "entryname", required = false) String entryName,
            Model model, HttpSession session, HttpServletRequest request) {

        model.addAttribute("entryid", entryID);
        model.addAttribute("entryname", entryName);
        return computeResult(searchModel, entryID, model, session, request);

    }

    private boolean startsWithDigit(String data) {
        return Character.isDigit(data.charAt(0));
    }
    private static final Comparator<String> NAME_COMPARATOR = new ChemicalNameComparator();
    static final Comparator<uk.ac.ebi.ep.search.model.Disease> SORT_DISEASES = new Comparator<uk.ac.ebi.ep.search.model.Disease>() {
        @Override
        public int compare(uk.ac.ebi.ep.search.model.Disease d1, uk.ac.ebi.ep.search.model.Disease d2) {

            if (d1.getName() == null && d2.getName() == null) {

                return NAME_COMPARATOR.compare(d1.getName(), d2.getName());
            }
            int compare = NAME_COMPARATOR.compare(d1.getName(), d2.getName());

            return ((compare == 0) ? NAME_COMPARATOR.compare(d1.getName(), d2.getName()) : compare);

        }
    };
}
