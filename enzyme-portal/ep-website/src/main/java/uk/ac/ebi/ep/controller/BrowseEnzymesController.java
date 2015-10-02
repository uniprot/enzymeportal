/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.ebi.ep.base.search.EnzymeFinder;
import uk.ac.ebi.ep.data.common.CommonSpecies;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.search.model.Compound;
import uk.ac.ebi.ep.data.search.model.Disease;
import uk.ac.ebi.ep.data.search.model.EcNumber;
import uk.ac.ebi.ep.data.search.model.SearchFilters;
import uk.ac.ebi.ep.data.search.model.SearchModel;
import uk.ac.ebi.ep.data.search.model.SearchParams;
import uk.ac.ebi.ep.data.search.model.SearchResults;
import uk.ac.ebi.ep.data.search.model.Species;
import uk.ac.ebi.ep.enzymes.EnzymeEntry;
import uk.ac.ebi.ep.enzymes.EnzymeSubSubclass;
import uk.ac.ebi.ep.enzymes.EnzymeSubclass;
import uk.ac.ebi.ep.enzymes.IntenzEnzyme;

/**
 *
 * @author joseph
 */
@Controller
public class BrowseEnzymesController extends AbstractController {

    //concrete jsp's
    private static final String BROWSE_ENZYMES = "/browse_enzymes";
    private static final String EC = "/ec";
    //private static final String RESULT = "/search_result_ec";
    private static final String RESULT = "/esearch";
    //abtract url
    private static final String BROWSE_ENZYME_CLASSIFICATION = "/browse/enzymes";
    private static final String BROWSE_EC = "/browse/enzyme";

//    private static final String SEARCH_BY_EC = "/ecnumber";
//     private static final String SEARCH_EC_NUMBER = "/search/ecnumber";
    private static final String FILTER_BY_FACETS = "/search-enzymes/filter";

    private static final String SEARCH_ENZYMES = "/search-enzymes";
    private static final String EC_NUMBER = "ec";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String SUBCLASSES = "subclasses";
    private static final String SUBSUBCLASSES = "subsubclasses";
    private static final String ENTRIES = "entries";
    private static final String INTENZ_URL = "http://www.ebi.ac.uk/intenz/ws/EC";
    private static final String ROOT = "ROOT";
    private static final String SUBCLASS = "SUBCLASS";
    private static final String SUBSUBCLASS = "SUBSUBCLASS";
    private static final String selectedEc = "selectedEc";

    private static final int SEARCH_PAGESIZE = 10;

    private SearchResults findEnzymesByEc(String ec) {

        SearchResults results = null;
        EnzymeFinder finder = new EnzymeFinder(enzymePortalService, ebeyeRestService);

        SearchParams searchParams = new SearchParams();
        searchParams.setText(ec);//use the ec number here. note ebeye is indexing ep data for ec to be searchable
        searchParams.setType(SearchParams.SearchType.KEYWORD);
        searchParams.setStart(0);
        searchParams.setPrevioustext(ec);//use ec here

        finder.setSearchParams(searchParams);

        results = finder.computeEnzymeSummariesByEc(ec);

        if (results == null) {

            return getEnzymes(finder, searchParams);
        }

        return results;
    }

    private SearchResults getEnzymes(EnzymeFinder finder, SearchParams searchParams) {

        SearchResults results = finder.getEnzymes(searchParams);

        return results;
    }

    private String computeResult(@ModelAttribute("searchModel") SearchModel searchModel,
            @RequestParam(value = "entryid", required = false) String entryID, @RequestParam(value = "entryname", required = false) String entryname,
            Model model, HttpSession session, HttpServletRequest request) {

        String view = "error";

        Map<String, SearchResults> prevSearches
                = getPreviousSearches(session.getServletContext());
        String searchKey = getSearchKey(searchModel.getSearchparams());

        SearchResults results = prevSearches.get(searchKey);

        if (results == null) {
            // New search:
            clearHistory(session);
            results = findEnzymesByEc(entryID);

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
            request.setAttribute("searchTerm", searchModel.getSearchparams().getText());
            view = RESULT;
        }

        return view;
    }

    @ModelAttribute("searchModel")
    public SearchModel searchform(String text) {
        SearchModel searchModelForm = new SearchModel();
        SearchParams searchParams = new SearchParams();
        searchParams.setStart(0);
        searchParams.setText(text);
        searchParams.setType(SearchParams.SearchType.KEYWORD);
        searchModelForm.setSearchparams(searchParams);
        return searchModelForm;
    }

    @RequestMapping(value = BROWSE_ENZYME_CLASSIFICATION, method = RequestMethod.GET)
    public String browseEc(Model model, HttpSession session) {
        clearSelectedEc(session);

        SearchModel searchModelForm = searchform();
        model.addAttribute("searchModel", searchModelForm);
        model.addAttribute(BROWSE_VIDEO, BROWSE_VIDEO);
        return BROWSE_ENZYMES;
    }

    @RequestMapping(value = BROWSE_EC + "/{ec}/{ecname}", method = RequestMethod.GET)
    public String showStaticEc(@ModelAttribute("searchModel") SearchModel searchModel,
            @PathVariable("ec") String ec, @PathVariable("ecname") String ecname,
            Model model, HttpSession session, HttpServletRequest request) throws MalformedURLException, IOException {
        clearSelectedEc(session);
        browseEc(model, session, ecname, null, null, null, ec);
        return EC;

    }

    @RequestMapping(value = BROWSE_EC, method = RequestMethod.GET)
    public String browseEcTree(@ModelAttribute("searchModel") SearchModel searchModel,
            @RequestParam(value = "ec", required = false) String ec, @RequestParam(value = "ecname", required = false) String ecname,
            @RequestParam(value = "subecname", required = false) String subecname,
            @RequestParam(value = "subsubecname", required = false) String subsubecname,
            @RequestParam(value = "entryecname", required = false) String entryecname, Model model, HttpSession session, HttpServletRequest request, Pageable pageable, RedirectAttributes attributes) throws MalformedURLException, IOException {
        model.addAttribute(BROWSE_VIDEO, BROWSE_VIDEO);
        if (ec != null && ec.length() >= 7) {
            model.addAttribute("entryid", ec);
            searchModel = searchform(ec);
            //return computeResult(searchModel, ec, entryecname, model, session, request);
            return searchByEcNumber(searchModel, ec, ecname, subecname, subsubecname, entryecname, model, request, session, pageable, attributes);

        } else {
            browseEc(model, session, ecname, subecname, subsubecname, entryecname, ec);
        }

        return EC;
    }

//    @RequestMapping(value = SEARCH_ENZYMES, method = RequestMethod.GET)
//    public String showEnzymeByEC(@ModelAttribute("searchModel") SearchModel searchModel,
//            @RequestParam(value = "ec", required = false) String ec, @RequestParam(value = "ecname", required = false) String ecname,
//            @RequestParam(value = "subecname", required = false) String subecname,
//            @RequestParam(value = "subsubecname", required = false) String subsubecname,
//            @RequestParam(value = "entryecname", required = false) String entryecname, Model model, HttpSession session, HttpServletRequest request) throws MalformedURLException, IOException {
//
//        model.addAttribute("entryid", ec);
//        model.addAttribute("entryname", entryecname);
//        model.addAttribute(BROWSE_VIDEO, BROWSE_VIDEO);
//        SearchModel searchModelForm = new SearchModel();
//        SearchParams searchParams = new SearchParams();
//        searchParams.setStart(0);
//        searchParams.setText(ec);
//        searchModelForm.setSearchparams(searchParams);
//        model.addAttribute("searchModel", searchModelForm);
//        request.setAttribute("searchTerm", searchModel.getSearchparams().getText());
//        return computeResult(searchModel, ec, entryecname, model, session, request);
//
//    }
//    @RequestMapping(value = SEARCH_ENZYMES, method = RequestMethod.POST)
//    public String searchEnzymesByEcPost(@ModelAttribute("searchModel") SearchModel searchModel,
//            @RequestParam(value = "ec", required = false) String ec, @RequestParam(value = "ecname", required = false) String ecname,
//            @RequestParam(value = "subecname", required = false) String subecname,
//            @RequestParam(value = "subsubecname", required = false) String subsubecname,
//            @RequestParam(value = "entryecname", required = false) String entryecname, Model model, HttpSession session, HttpServletRequest request) throws MalformedURLException, IOException {
//
//        model.addAttribute("entryid", ec);
//        model.addAttribute("entryname", entryecname);
//
//        return computeResult(searchModel, ec, entryecname, model, session, request);
//
//    }
    private void browseEc(Model model, HttpSession session, String ecname, String sub_ecname, String subsub_ecname, String entry_ecname, String ec) throws MalformedURLException, IOException {

        String intenz_url = String.format("%s/%s.json", INTENZ_URL, ec);
        URL url = new URL(intenz_url);
        try (InputStream is = url.openStream();
                JsonReader rdr = Json.createReader(is)) {

            computeJsonData(rdr, model, session, ecname, sub_ecname, subsub_ecname, entry_ecname, ec);
        }
    }

    /**
     * This method keeps track of the selected enzymes in their hierarchy for
     * the browse enzyme
     *
     * @param session
     * @param s the selected enzyme
     * @param type the position in the hierarchy
     */
    private void addToSelectedEc(HttpSession session, IntenzEnzyme s, String type) {
        @SuppressWarnings("unchecked")
        LinkedList<IntenzEnzyme> history = (LinkedList<IntenzEnzyme>) session.getAttribute(selectedEc);

        if (history == null) {

            history = new LinkedList<>();
            session.setAttribute(selectedEc, history);
        }

        if (!history.isEmpty() && history.contains(s)) {

            if (type.equalsIgnoreCase(ROOT) && history.size() == 2) {
                history.removeLast();

            }
            if (type.equalsIgnoreCase(ROOT) && history.size() == 3) {
                history.removeLast();
                history.removeLast();
                //history.remove(history.size()-1);//same as above

            }
            if (type.equalsIgnoreCase(SUBCLASS) && history.size() == 2) {
                history.removeLast();
                history.add(s);

            }
            if (type.equalsIgnoreCase(SUBCLASS) && history.size() == 3) {
                history.removeLast();

            }

        } else if ((history.isEmpty() || !history.contains(s)) && (history.size() < 3)) {
            history.add(s);

        }
    }

    private void clearSelectedEc(HttpSession session) {
        @SuppressWarnings("unchecked")
        LinkedList<IntenzEnzyme> history = (LinkedList<IntenzEnzyme>) session.getAttribute(selectedEc);
        if (history == null) {
            //history = new ArrayList<String>();
            history = new LinkedList<>();
            session.setAttribute(selectedEc, history);
        } else {
            history.clear();
        }
    }

    private void computeJsonData(JsonReader jsonReader, Model model, HttpSession session, String... ecname) {
        JsonObject jsonObject = jsonReader.readObject();

        IntenzEnzyme root = new IntenzEnzyme();

        String ec = jsonObject.getString(EC_NUMBER);
        //String name = jsonObject.getString(NAME);
        String description = null;

        if (jsonObject.containsKey(DESCRIPTION)) {
            description = jsonObject.getString(DESCRIPTION);

            root.setDescription(description);
        }
        root.setEc(ec);
        root.setName(ecname[0]);
        root.setSubclassName(ecname[1]);
        root.setSubsubclassName(ecname[2]);
        root.setEntryName(ecname[3]);

        //compute the childObject
        if (jsonObject.containsKey(SUBCLASSES)) {

            JsonArray jsonArray = jsonObject.getJsonArray(SUBCLASSES);

            for (JsonObject childObject : jsonArray.getValuesAs(JsonObject.class)) {
                String _ec = null;
                String _name = null;
                String _desc = null;
                _ec = childObject.getString(EC_NUMBER);
                _name = childObject.getString(NAME);

                EnzymeSubclass subclass = new EnzymeSubclass();

                if (childObject.containsKey(DESCRIPTION)) {
                    _desc = childObject.getString(DESCRIPTION);
                    subclass.setDescription(_desc);
                }

                subclass.setEc(_ec);
                subclass.setName(_name);
                root.getChildren().add(subclass);

            }
            addToSelectedEc(session, root, ROOT);
            model.addAttribute("json", root);
        }
        if (jsonObject.containsKey(SUBSUBCLASSES)) {

            JsonArray jsonArray = jsonObject.getJsonArray(SUBSUBCLASSES);

            for (JsonObject childObject : jsonArray.getValuesAs(JsonObject.class)) {
                String _ec = null;
                String _name = null;
                String _desc = null;
                _ec = childObject.getString(EC_NUMBER);
                _name = childObject.getString(NAME);

                EnzymeSubSubclass subsubclass = new EnzymeSubSubclass();

                if (childObject.containsKey(DESCRIPTION)) {
                    _desc = childObject.getString(DESCRIPTION);

                    subsubclass.setDescription(_desc);
                }

                subsubclass.setEc(_ec);
                subsubclass.setName(_name);

                root.getSubSubclasses().add(subsubclass);

            }

            model.addAttribute("json", root);
            addToSelectedEc(session, root, SUBCLASS);
        }
        if (jsonObject.containsKey(ENTRIES)) {

            JsonArray jsonArray = jsonObject.getJsonArray(ENTRIES);

            for (JsonObject childObject : jsonArray.getValuesAs(JsonObject.class)) {
                String _ec = null;
                String _name = null;
                String _desc = null;
                _ec = childObject.getString(EC_NUMBER);
                _name = childObject.getString(NAME);

                EnzymeEntry entries = new EnzymeEntry();
                if (childObject.containsKey(DESCRIPTION)) {
                    _desc = childObject.getString(DESCRIPTION);

                    entries.setDescription(_desc);
                }

                entries.setEc(_ec);
                entries.setName(_name);
                root.setEc(ecname[4]);
                root.getEntries().add(entries);

            }

            model.addAttribute("json", root);
            addToSelectedEc(session, root, SUBSUBCLASS);
        }

    }

    ///enhancement to how we query and show search by ec numbers
//    
//        private String computeResultXX(@ModelAttribute("searchModel") SearchModel searchModel,
//            @RequestParam(value = "entryid", required = false) String entryID, @RequestParam(value = "entryname", required = false) String entryname,
//            Model model, HttpSession session, HttpServletRequest request) {
    @RequestMapping(value = SEARCH_ENZYMES, method = RequestMethod.GET)
    public String searchByEcNumber(@ModelAttribute("searchModel") SearchModel searchModel,
            @RequestParam(value = "ec", required = false) String ec, @RequestParam(value = "ecname", required = false) String ecname,
            @RequestParam(value = "subecname", required = false) String subecname,
            @RequestParam(value = "subsubecname", required = false) String subsubecname,
            @RequestParam(value = "entryecname", required = false) String entryecname,
            Model model, HttpServletRequest request, HttpSession session, Pageable pageable, RedirectAttributes attributes) {

        //String view = RESULT;// "error";
//        Map<String, SearchResults> prevSearches
//                = getPreviousSearches(session.getServletContext());
//        String searchKey = getSearchKey(searchModel.getSearchparams());
//
//        SearchResults results = prevSearches.get(searchKey);
//
//        if (results == null) {
//            // New search:
//            clearHistory(session);
//            results = findEnzymesByEc(entryID);
//
//        }
//
//        if (results != null) {
//            cacheSearch(session.getServletContext(), searchKey, results);
//            setLastSummaries(session, results.getSummaryentries());
//            searchModel.setSearchresults(results);
//            applyFilters(searchModel, request);
//            model.addAttribute("searchConfig", searchConfig);
//            model.addAttribute("searchModel", searchModel);
//            model.addAttribute("pagination", getPagination(searchModel));
//            clearHistory(session);
//            addToHistory(session, searchModel.getSearchparams().getType(),
//                    searchKey);
//            request.setAttribute("searchTerm", searchModel.getSearchparams().getText());
//            view = RESULT;
//        }
        pageable = new PageRequest(0, SEARCH_PAGESIZE, Sort.Direction.ASC, "entryType", "function");

        Page<UniprotEntry> page = this.enzymePortalService.findEnzymesByEcNumber(ec, pageable);

        List<Species> species = enzymePortalService.findSpeciesByEcNumber(ec);
        List<Compound> compouds = enzymePortalService.findCompoundsByEcNumber(ec);
        List<Disease> diseases = enzymePortalService.findDiseasesByEcNumber(ec);

        List<EcNumber> enzymeFamilies = enzymePortalService.findEnzymeFamiliesByEcNumber(ec);
        System.out.println("EC " + ec);
        System.out.println("ECNAME " + entryecname);
        SearchParams searchParams = searchModel.getSearchparams();
        searchParams.setStart(0);
        searchParams.setType(SearchParams.SearchType.KEYWORD);
        searchParams.setText(ec);
        searchParams.setSize(SEARCH_PAGESIZE);
        searchModel.setSearchparams(searchParams);

        List<UniprotEntry> result = page.getContent();

        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, page.getTotalPages());

        model.addAttribute("page", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);

        model.addAttribute("ecname", entryecname);
        model.addAttribute("ec", ec);

        // model.addAttribute("summaryEntries", result);
        SearchResults searchResults = new SearchResults();

        searchResults.setTotalfound(page.getTotalElements());
        SearchFilters filters = new SearchFilters();
        Set<Species> speciesFilter = species.stream().collect(Collectors.toSet());
        List<Species> speciesFacets = applySpeciesFilter(speciesFilter);
        filters.setSpecies(speciesFacets);
        filters.setCompounds(compouds);
        filters.setEcNumbers(enzymeFamilies);
        filters.setDiseases(diseases);

        searchResults.setSearchfilters(filters);
        searchResults.setSummaryentries(result);

        searchModel.setSearchresults(searchResults);


        String searchKey = getSearchKey(searchModel.getSearchparams());

        cacheSearch(session.getServletContext(), searchKey, searchResults);
        setLastSummaries(session, searchResults.getSummaryentries());
        clearHistory(session);
        System.out.println("SEARCH MODEL TYPE " + searchModel.getSearchparams().getType() + " my key " + searchKey);
        addToHistory(session, searchModel.getSearchparams().getType(),
                searchKey);
        model.addAttribute("searchModel", searchModel);
        model.addAttribute("searchConfig", searchConfig);

        model.addAttribute("searchFilter", filters);
        model.addAttribute(BROWSE_VIDEO, BROWSE_VIDEO);
        request.setAttribute("searchTerm", searchModel.getSearchparams().getText());

        return RESULT;
    }


    @RequestMapping(value = SEARCH_ENZYMES + "/page={pageNumber}", method = RequestMethod.GET)
    public String searchByEcNumberPaginated(@PathVariable Integer pageNumber, @ModelAttribute("searchModel") SearchModel searchModel,
            @RequestParam(value = "ec", required = true) String ec, @RequestParam(value = "ecname", required = false) String ecname,
            @RequestParam(value = "subecname", required = false) String subecname,
            @RequestParam(value = "subsubecname", required = false) String subsubecname,
            @RequestParam(value = "entryecname", required = false) String entryecname,
            Model model, HttpServletRequest request, HttpSession session, RedirectAttributes attributes) {


        if (pageNumber < 1) {
            pageNumber = 1;
        }

        Pageable pageable = new PageRequest(pageNumber - 1, SEARCH_PAGESIZE, Sort.Direction.ASC, "entryType", "function");

        Page<UniprotEntry> page = this.enzymePortalService.findEnzymesByEcNumber(ec, pageable);

        List<Species> species = enzymePortalService.findSpeciesByEcNumber(ec);
        List<Compound> compouds = enzymePortalService.findCompoundsByEcNumber(ec);
        List<Disease> diseases = enzymePortalService.findDiseasesByEcNumber(ec);

        List<EcNumber> enzymeFamilies = enzymePortalService.findEnzymeFamiliesByEcNumber(ec);

        SearchParams searchParams = searchModel.getSearchparams();
        searchParams.setStart(0);
        searchParams.setType(SearchParams.SearchType.KEYWORD);
        searchParams.setText(ec);
        searchParams.setSize(SEARCH_PAGESIZE);
        searchModel.setSearchparams(searchParams);

        List<UniprotEntry> result = page.getContent();

        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, page.getTotalPages());

        model.addAttribute("page", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);

        model.addAttribute("ecname", entryecname);
        model.addAttribute("ec", ec);

        // model.addAttribute("summaryEntries", result);
        SearchResults searchResults = new SearchResults();

        searchResults.setTotalfound(page.getTotalElements());
        SearchFilters filters = new SearchFilters();
        Set<Species> speciesFilter = species.stream().collect(Collectors.toSet());
        List<Species> speciesFacets = applySpeciesFilter(speciesFilter);
        filters.setSpecies(speciesFacets);
        filters.setCompounds(compouds);
        filters.setEcNumbers(enzymeFamilies);
        filters.setDiseases(diseases);

        searchResults.setSearchfilters(filters);
        searchResults.setSummaryentries(result);

        searchModel.setSearchresults(searchResults);

        String searchKey = getSearchKey(searchModel.getSearchparams());

        cacheSearch(session.getServletContext(), searchKey, searchResults);
        setLastSummaries(session, searchResults.getSummaryentries());
        clearHistory(session);

        addToHistory(session, searchModel.getSearchparams().getType(),
                searchKey);
        model.addAttribute("searchModel", searchModel);
        model.addAttribute("searchConfig", searchConfig);

        model.addAttribute("searchFilter", filters);
        model.addAttribute(BROWSE_VIDEO, BROWSE_VIDEO);
        request.setAttribute("searchTerm", searchModel.getSearchparams().getText());

        return RESULT;
    }

    @RequestMapping(value = FILTER_BY_FACETS, method = RequestMethod.POST)
    public String filterByFacetsPost(@ModelAttribute("searchModel") SearchModel searchModel,
            @RequestParam(value = "ec", required = true) String ec, @RequestParam(value = "ecname", required = false) String ecname,
            Model model, HttpServletRequest request, HttpSession session, RedirectAttributes attributes) {
        return filterByFacets(searchModel, ec, ecname, model, request, session, attributes);
    }

    @RequestMapping(value = FILTER_BY_FACETS, method = RequestMethod.GET)
    public String filterByFacets(@ModelAttribute("searchModel") SearchModel searchModel,
            @RequestParam(value = "ec", required = true) String ec, @RequestParam(value = "ecname", required = false) String ecname,
            Model model, HttpServletRequest request, HttpSession session, RedirectAttributes attributes) {

        List<Species> species = enzymePortalService.findSpeciesByEcNumber(ec);
        List<Compound> compouds = enzymePortalService.findCompoundsByEcNumber(ec);
        List<Disease> diseases = enzymePortalService.findDiseasesByEcNumber(ec);

        List<EcNumber> enzymeFamilies = enzymePortalService.findEnzymeFamiliesByEcNumber(ec);

        SearchFilters filters = new SearchFilters();
        filters.setSpecies(species);
        filters.setCompounds(compouds);
        filters.setDiseases(diseases);
        filters.setEcNumbers(enzymeFamilies);

        SearchParams searchParams = searchModel.getSearchparams();
        searchParams.setText(ec);
        searchParams.setSize(SEARCH_PAGESIZE);
        searchParams.setType(SearchParams.SearchType.KEYWORD);
        searchModel.setSearchparams(searchParams);

        SearchResults searchResults = new SearchResults();

        searchResults.setSearchfilters(filters);
        searchModel.setSearchresults(searchResults);

        SearchParams searchParameters = searchModel.getSearchparams();

        String compound_autocompleteFilter = request.getParameter("searchparams.compounds");
        String specie_autocompleteFilter = request.getParameter("_ctempList_selected");
        String diseases_autocompleteFilter = request.getParameter("_DtempList_selected");

        // Filter:
        List<String> specieFilter = searchParameters.getSpecies();
        List<String> compoundFilter = searchParameters.getCompounds();
        List<String> diseaseFilter = searchParameters.getDiseases();
        List<Integer> ecFilter = searchParameters.getEcFamilies();

//        System.out.println("EC IN FILTER " + ec + " eclist " + ecFilter);
//
//        System.out.println("SP " + specieFilter + " CP " + compoundFilter + " DS " + diseaseFilter);
//
//        System.out.println("AUTO sp " + specie_autocompleteFilter + " CP " + compound_autocompleteFilter + " dis " + diseases_autocompleteFilter);

        //remove empty string in the filter to avoid unsual behavior of the filter facets
        if (specieFilter.contains("")) {
            specieFilter.remove("");

        }
        if (compoundFilter.contains("")) {
            compoundFilter.remove("");

        }
        if (diseaseFilter.contains("")) {
            diseaseFilter.remove("");
        }

        //to ensure that the seleted item is used in species filter, add the selected to the list. this is a workaround. different JS were used for auto complete and normal filter
        if ((specie_autocompleteFilter != null && StringUtils.hasLength(specie_autocompleteFilter) == true) && StringUtils.isEmpty(compound_autocompleteFilter) && StringUtils.isEmpty(diseases_autocompleteFilter)) {
            specieFilter.add(specie_autocompleteFilter);

        }

        if ((diseases_autocompleteFilter != null && StringUtils.hasLength(diseases_autocompleteFilter) == true) && StringUtils.isEmpty(compound_autocompleteFilter) && StringUtils.isEmpty(specie_autocompleteFilter)) {
            diseaseFilter.add(diseases_autocompleteFilter);

        }

//both from auto complete and normal selection. selected items are displayed on top the list and returns back to the orignial list when not selected.
        //SearchResults searchResults = resultSet;
        List<Species> defaultSpeciesList = searchResults.getSearchfilters().getSpecies();
        resetSelectedSpecies(defaultSpeciesList);

        searchParameters.getSpecies().stream().forEach((selectedItems) -> {
            defaultSpeciesList.stream().filter((theSpecies) -> (selectedItems.equals(theSpecies.getScientificname()))).forEach((theSpecies) -> {
                theSpecies.setSelected(true);
            });
        });

        List<Compound> defaultCompoundList = searchResults.getSearchfilters().getCompounds();
        resetSelectedCompounds(defaultCompoundList);

        searchParameters.getCompounds().stream().forEach((SelectedCompounds) -> {
            defaultCompoundList.stream().filter((theCompound) -> (SelectedCompounds.equals(theCompound.getName()))).forEach((theCompound) -> {
                theCompound.setSelected(true);
            });
        });

        List<Disease> defaultDiseaseList = searchResults.getSearchfilters().getDiseases();
        resetSelectedDisease(defaultDiseaseList);

        searchParameters.getDiseases().stream().forEach((selectedDisease) -> {
            defaultDiseaseList.stream().filter((disease) -> (selectedDisease.equals(disease.getName()))).forEach((disease) -> {
                disease.setSelected(true);
            });
        });

        List<EcNumber> defaultEcNumberList = searchResults.getSearchfilters().getEcNumbers();

        resetSelectedEcNumber(defaultEcNumberList);

        searchParameters.getEcFamilies().stream().forEach((selectedEcFamily) -> {
            defaultEcNumberList.stream().filter((ecn) -> (selectedEcFamily.equals(ecn.getEc()))).forEach((ecn) -> {
                ecn.setSelected(true);
            });
        });

        Pageable pageable = new PageRequest(0, SEARCH_PAGESIZE, Sort.Direction.ASC, "entryType", "function");
        Page<UniprotEntry> page = new PageImpl<>(new ArrayList<>(), pageable, 0);

        //methods
        //ec only
        if (specieFilter.isEmpty() && compoundFilter.isEmpty() && diseaseFilter.isEmpty() && !StringUtils.isEmpty(ec)) {
            page = enzymePortalService.filterByEc(ec, pageable);
            System.out.println("EC ONLY");

        }

        //specie only
        if (!specieFilter.isEmpty() && compoundFilter.isEmpty() && diseaseFilter.isEmpty()) {
            page = enzymePortalService.filterByEcAndSpecies(ec, specieFilter, pageable);
            System.out.println("species only");

        }

        // compounds only
        if (!compoundFilter.isEmpty() && specieFilter.isEmpty() && diseaseFilter.isEmpty()) {
            page = enzymePortalService.filterByEcAndCompounds(ec, compoundFilter, pageable);
            System.out.println("compound only");

        }
        // disease only
        if (specieFilter.isEmpty() && compoundFilter.isEmpty() && !diseaseFilter.isEmpty()) {
            page = enzymePortalService.filterByEcAndDiseases(ec, diseaseFilter, pageable);
            System.out.println("disease only");

        }

        //species, compound and ec
        if (!specieFilter.isEmpty() && !compoundFilter.isEmpty() && diseaseFilter.isEmpty()) {
            page = enzymePortalService.filterByEcAndSpeciesAndCompound(ec, specieFilter, compoundFilter, pageable);
        }

        //species, disease and ec
        if (!specieFilter.isEmpty() && compoundFilter.isEmpty() && !diseaseFilter.isEmpty()) {
            page = enzymePortalService.filterByEcAndSpeciesAndDiseases(ec, specieFilter, diseaseFilter, pageable);
        }

        //species,compounds, disease and ec
        if (!specieFilter.isEmpty() && !compoundFilter.isEmpty() && !diseaseFilter.isEmpty()) {
            page = enzymePortalService.filterByEcAndSpeciesAndCompoundAndDiseases(ec, specieFilter, compoundFilter, diseaseFilter, pageable);
        }

        //compound, diseases and ec
        if (specieFilter.isEmpty() && !compoundFilter.isEmpty() && !diseaseFilter.isEmpty()) {
            page = enzymePortalService.filterByEcAndCompoundAndDiseases(ec, compoundFilter, diseaseFilter, pageable);
        }


        List<UniprotEntry> result = page.getContent();

        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, page.getTotalPages());

        model.addAttribute("page", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);

        model.addAttribute("ecname", ecname);
        model.addAttribute("ec", ec);

        model.addAttribute("summaryEntries", result);

        searchResults.setTotalfound(page.getTotalElements());
        searchResults.setSearchfilters(filters);
        searchResults.setSummaryentries(result);
        searchModel.setSearchresults(searchResults);
        model.addAttribute("searchModel", searchModel);
        model.addAttribute("searchConfig", searchConfig);

        String searchKey = getSearchKey(searchModel.getSearchparams());

        cacheSearch(session.getServletContext(), searchKey, searchResults);
        setLastSummaries(session, searchResults.getSummaryentries());

        clearHistory(session);
        addToHistory(session, searchModel.getSearchparams().getType(),
                searchKey);
        model.addAttribute("searchFilter", filters);
        model.addAttribute(BROWSE_VIDEO, BROWSE_VIDEO);
        request.setAttribute("searchTerm", searchModel.getSearchparams().getText());
        model.addAttribute("filtering", true);

        return RESULT;
      
    }

    @RequestMapping(value = FILTER_BY_FACETS + "/page={pageNumber}", method = RequestMethod.POST)
    public String filterByFacetsPaginated(@PathVariable Integer pageNumber, @ModelAttribute("searchModel") SearchModel searchModel,
            @RequestParam(value = "ec", required = true) String ec, @RequestParam(value = "ecname", required = false) String ecname,
            Model model, HttpServletRequest request, HttpSession session, RedirectAttributes attributes) {

        if (pageNumber < 1) {
            pageNumber = 1;
        }

        Pageable pageable = new PageRequest(pageNumber - 1, SEARCH_PAGESIZE, Sort.Direction.ASC, "entryType", "function");

        Page<UniprotEntry> page = new PageImpl<>(new ArrayList<>(), pageable, 0);

        List<Species> species = enzymePortalService.findSpeciesByEcNumber(ec);
        List<Compound> compouds = enzymePortalService.findCompoundsByEcNumber(ec);
        List<Disease> diseases = enzymePortalService.findDiseasesByEcNumber(ec);

        List<EcNumber> enzymeFamilies = enzymePortalService.findEnzymeFamiliesByEcNumber(ec);

        SearchFilters filters = new SearchFilters();
        filters.setSpecies(species);
        filters.setCompounds(compouds);
        filters.setDiseases(diseases);
        filters.setEcNumbers(enzymeFamilies);

        SearchParams searchParams = searchModel.getSearchparams();
        searchParams.setText(ec);
        searchParams.setSize(SEARCH_PAGESIZE);
        searchParams.setType(SearchParams.SearchType.KEYWORD);
        searchModel.setSearchparams(searchParams);

        SearchResults searchResults = new SearchResults();

        searchResults.setSearchfilters(filters);
        searchModel.setSearchresults(searchResults);

        SearchParams searchParameters = searchModel.getSearchparams();

        String compound_autocompleteFilter = request.getParameter("searchparams.compounds");
        String specie_autocompleteFilter = request.getParameter("_ctempList_selected");
        String diseases_autocompleteFilter = request.getParameter("_DtempList_selected");

        // Filter:
        List<String> specieFilter = searchParameters.getSpecies();
        List<String> compoundFilter = searchParameters.getCompounds();
        List<String> diseaseFilter = searchParameters.getDiseases();
        List<Integer> ecFilter = searchParameters.getEcFamilies();

//        System.out.println("EC IN FILTER " + ec + " eclist " + ecFilter);
//
//        System.out.println("SP " + specieFilter + " CP " + compoundFilter + " DS " + diseaseFilter);
//
//        System.out.println("AUTO sp " + specie_autocompleteFilter + " CP " + compound_autocompleteFilter + " dis " + diseases_autocompleteFilter);

        //remove empty string in the filter to avoid unsual behavior of the filter facets
        if (specieFilter.contains("")) {
            specieFilter.remove("");

        }
        if (compoundFilter.contains("")) {
            compoundFilter.remove("");

        }
        if (diseaseFilter.contains("")) {
            diseaseFilter.remove("");
        }

        //to ensure that the seleted item is used in species filter, add the selected to the list. this is a workaround. different JS were used for auto complete and normal filter
        if ((specie_autocompleteFilter != null && StringUtils.hasLength(specie_autocompleteFilter) == true) && StringUtils.isEmpty(compound_autocompleteFilter) && StringUtils.isEmpty(diseases_autocompleteFilter)) {
            specieFilter.add(specie_autocompleteFilter);

        }

        if ((diseases_autocompleteFilter != null && StringUtils.hasLength(diseases_autocompleteFilter) == true) && StringUtils.isEmpty(compound_autocompleteFilter) && StringUtils.isEmpty(specie_autocompleteFilter)) {
            diseaseFilter.add(diseases_autocompleteFilter);

        }

//both from auto complete and normal selection. selected items are displayed on top the list and returns back to the orignial list when not selected.
        //SearchResults searchResults = resultSet;
        List<Species> defaultSpeciesList = searchResults.getSearchfilters().getSpecies();
        resetSelectedSpecies(defaultSpeciesList);

        searchParameters.getSpecies().stream().forEach((selectedItems) -> {
            defaultSpeciesList.stream().filter((theSpecies) -> (selectedItems.equals(theSpecies.getScientificname()))).forEach((theSpecies) -> {
                theSpecies.setSelected(true);
            });
        });

        List<Compound> defaultCompoundList = searchResults.getSearchfilters().getCompounds();
        resetSelectedCompounds(defaultCompoundList);

        searchParameters.getCompounds().stream().forEach((SelectedCompounds) -> {
            defaultCompoundList.stream().filter((theCompound) -> (SelectedCompounds.equals(theCompound.getName()))).forEach((theCompound) -> {
                theCompound.setSelected(true);
            });
        });

        List<Disease> defaultDiseaseList = searchResults.getSearchfilters().getDiseases();
        resetSelectedDisease(defaultDiseaseList);

        searchParameters.getDiseases().stream().forEach((selectedDisease) -> {
            defaultDiseaseList.stream().filter((disease) -> (selectedDisease.equals(disease.getName()))).forEach((disease) -> {
                disease.setSelected(true);
            });
        });

        List<EcNumber> defaultEcNumberList = searchResults.getSearchfilters().getEcNumbers();

        resetSelectedEcNumber(defaultEcNumberList);

        searchParameters.getEcFamilies().stream().forEach((selectedEcFamily) -> {
            defaultEcNumberList.stream().filter((ecn) -> (selectedEcFamily.equals(ecn.getEc()))).forEach((ecn) -> {
                ecn.setSelected(true);
            });
        });

        //methods
        //ec only
        if (specieFilter.isEmpty() && compoundFilter.isEmpty() && diseaseFilter.isEmpty() && !StringUtils.isEmpty(ec)) {
            page = enzymePortalService.filterByEc(ec, pageable);
          

        }

        //specie only
        if (!specieFilter.isEmpty() && compoundFilter.isEmpty() && diseaseFilter.isEmpty()) {
            page = enzymePortalService.filterByEcAndSpecies(ec, specieFilter, pageable);
      

        }

        // compounds only
        if (!compoundFilter.isEmpty() && specieFilter.isEmpty() && diseaseFilter.isEmpty()) {
            page = enzymePortalService.filterByEcAndCompounds(ec, compoundFilter, pageable);
          

        }
        // disease only
        if (specieFilter.isEmpty() && compoundFilter.isEmpty() && !diseaseFilter.isEmpty()) {
            page = enzymePortalService.filterByEcAndDiseases(ec, diseaseFilter, pageable);
          

        }

        //species, compound and ec
        if (!specieFilter.isEmpty() && !compoundFilter.isEmpty() && diseaseFilter.isEmpty()) {
            page = enzymePortalService.filterByEcAndSpeciesAndCompound(ec, specieFilter, compoundFilter, pageable);
        }

        //species, disease and ec
        if (!specieFilter.isEmpty() && compoundFilter.isEmpty() && !diseaseFilter.isEmpty()) {
            page = enzymePortalService.filterByEcAndSpeciesAndDiseases(ec, specieFilter, diseaseFilter, pageable);
        }

        //species,compounds, disease and ec
        if (!specieFilter.isEmpty() && !compoundFilter.isEmpty() && !diseaseFilter.isEmpty()) {
            page = enzymePortalService.filterByEcAndSpeciesAndCompoundAndDiseases(ec, specieFilter, compoundFilter, diseaseFilter, pageable);
        }

        //compound, diseases and ec
        if (specieFilter.isEmpty() && !compoundFilter.isEmpty() && !diseaseFilter.isEmpty()) {
            page = enzymePortalService.filterByEcAndCompoundAndDiseases(ec, compoundFilter, diseaseFilter, pageable);
        }


        List<UniprotEntry> result = page.getContent();

        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, page.getTotalPages());

        model.addAttribute("page", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);

        model.addAttribute("ecname", ecname);
        model.addAttribute("ec", ec);

        model.addAttribute("summaryEntries", result);

        searchResults.setTotalfound(page.getTotalElements());
        searchResults.setSearchfilters(filters);
        searchResults.setSummaryentries(result);
        searchModel.setSearchresults(searchResults);
        model.addAttribute("searchModel", searchModel);
        model.addAttribute("searchConfig", searchConfig);

        String searchKey = getSearchKey(searchModel.getSearchparams());

        cacheSearch(session.getServletContext(), searchKey, searchResults);
        setLastSummaries(session, searchResults.getSummaryentries());

        clearHistory(session);
        addToHistory(session, searchModel.getSearchparams().getType(),
                searchKey);
        model.addAttribute("searchFilter", filters);
        model.addAttribute(BROWSE_VIDEO, BROWSE_VIDEO);
        request.setAttribute("searchTerm", searchModel.getSearchparams().getText());
         model.addAttribute("filtering", true);

        return RESULT;
        //return "redirect:" + RESULT;
    }

    @RequestMapping(value = SEARCH_ENZYMES, method = RequestMethod.POST)
    public String searchEnzymesByEcPost(@ModelAttribute("searchModel") SearchModel searchModel,
            @RequestParam(value = "ec", required = false) String ec, @RequestParam(value = "ecname", required = false) String ecname,
            @RequestParam(value = "subecname", required = false) String subecname,
            @RequestParam(value = "subsubecname", required = false) String subsubecname,
            @RequestParam(value = "entryecname", required = false) String entryecname,
            Model model, HttpServletRequest request, HttpSession session, Pageable pageable, RedirectAttributes attributes) {

        model.addAttribute("entryid", ec);
        model.addAttribute("entryname", entryecname);
        return searchByEcNumber(searchModel, ec, ecname, subecname, subsubecname, entryecname, model, request, session, pageable, attributes);

    }

//    @RequestMapping(value = SEARCH_EC_NUMBER, method = RequestMethod.POST)
//    public String SearchByEcNumber(@ModelAttribute("searchModel") SearchModel searchModel,
//            @RequestParam(value = "entryid", required = false) String entryID, @RequestParam(value = "entryname", required = false) String entryName,
//            Model model, HttpServletRequest request, HttpSession session, Pageable pageable, RedirectAttributes attributes) {
//
//        //return searchByEcNumber(searchModel, entryID, entryName, model, request, session, pageable, attributes);
//        return searchByEcNumber(searchModel, entryID, entryName, entryName, entryName, entryName, model, request, session, pageable, attributes);
//
//    }
    /**
     * Builds filters - species, compounds, diseases - from a result list.
     *
     * @param searchResults the result list, which will be modified by setting
     * the relevant filters.
     */
    private void buildFilters(SearchResults searchResults, Set<Species> uniqueSpecies) {
        //  String[] commonSpecie = {"HUMAN", "MOUSE", "RAT", "Fruit fly", "WORM", "Yeast", "ECOLI"};
        // CommonSpecies [] commonSpecie = {"Homo sapiens","Mus musculus","Rattus norvegicus", "Drosophila melanogaster","Saccharomyces cerevisiae"};
        // List<String> commonSpecieList = Arrays.asList(commonSpecie);
        List<String> commonSpecieList = new ArrayList<>();
        for (CommonSpecies commonSpecies : CommonSpecies.values()) {
            commonSpecieList.add(commonSpecies.getScientificName());
        }

        Map<Integer, Species> priorityMapper = new TreeMap<>();

        AtomicInteger key = new AtomicInteger(50);
        AtomicInteger customKey = new AtomicInteger(6);

        for (Species sp : uniqueSpecies) {

            if (commonSpecieList.contains(sp.getScientificname().split("\\(")[0].trim())) {
                // HUMAN, MOUSE, RAT, Fly, WORM, Yeast, ECOLI 
                // "Homo sapiens","Mus musculus","Rattus norvegicus", "Drosophila melanogaster","WORM","Saccharomyces cerevisiae","ECOLI"
                if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.HUMAN.getScientificName())) {
                    priorityMapper.put(1, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.MOUSE.getScientificName())) {
                    priorityMapper.put(2, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.RAT.getScientificName())) {
                    priorityMapper.put(3, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.FRUIT_FLY.getScientificName())) {
                    priorityMapper.put(4, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.WORM.getScientificName())) {
                    priorityMapper.put(5, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.ECOLI.getScientificName())) {
                    priorityMapper.put(6, sp);
                } else if (sp.getScientificname().split("\\(")[0].trim().equalsIgnoreCase(CommonSpecies.BAKER_YEAST.getScientificName())) {
                    priorityMapper.put(customKey.getAndIncrement(), sp);

                }
            } else {

                priorityMapper.put(key.getAndIncrement(), sp);

            }
        }

        List<Species> speciesFilters = new LinkedList<>();
        priorityMapper.entrySet().stream().forEach(map -> {
            speciesFilters.add(map.getValue());
        });

        SearchFilters filters = new SearchFilters();
        filters.setSpecies(speciesFilters);
        //filters.setCompounds(compoundFilters.stream().filter(Objects::nonNull).distinct().sorted(SORT_COMPOUND).collect(Collectors.toList()));

        //filters.setDiseases(diseaseFilters.stream().distinct().sorted(SORT_DISEASE).collect(Collectors.toList()));
        //filters.setEcNumbers(ecNumberFilters.stream().map(Family::new).distinct().sorted().map(Family::unwrapFamily).filter(Objects::nonNull).collect(Collectors.toList()));
        searchResults.setSearchfilters(filters);
    }

    private List<Species> applySpeciesFilter(Set<Species> uniqueSpecies) {
        //  String[] commonSpecie = {"HUMAN", "MOUSE", "RAT", "Fruit fly", "WORM", "Yeast", "ECOLI"};
        // CommonSpecies [] commonSpecie = {"Homo sapiens","Mus musculus","Rattus norvegicus", "Drosophila melanogaster","Saccharomyces cerevisiae"};
        // List<String> commonSpecieList = Arrays.asList(commonSpecie);
        List<String> commonSpecieList = new ArrayList<>();
        for (CommonSpecies commonSpecies : CommonSpecies.values()) {
            commonSpecieList.add(commonSpecies.getScientificName());
        }

        Map<Integer, Species> priorityMapper = new TreeMap<>();

        AtomicInteger key = new AtomicInteger(50);
        AtomicInteger customKey = new AtomicInteger(6);

        for (Species sp : uniqueSpecies) {

            if (commonSpecieList.contains(sp.getScientificname().split("\\(")[0].trim())) {
                // HUMAN, MOUSE, RAT, Fly, WORM, Yeast, ECOLI 
                // "Homo sapiens","Mus musculus","Rattus norvegicus", "Drosophila melanogaster","WORM","Saccharomyces cerevisiae","ECOLI"
                if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.HUMAN.getScientificName())) {
                    priorityMapper.put(1, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.MOUSE.getScientificName())) {
                    priorityMapper.put(2, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.RAT.getScientificName())) {
                    priorityMapper.put(3, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.FRUIT_FLY.getScientificName())) {
                    priorityMapper.put(4, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.WORM.getScientificName())) {
                    priorityMapper.put(5, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.ECOLI.getScientificName())) {
                    priorityMapper.put(6, sp);
                } else if (sp.getScientificname().split("\\(")[0].trim().equalsIgnoreCase(CommonSpecies.BAKER_YEAST.getScientificName())) {
                    priorityMapper.put(customKey.getAndIncrement(), sp);

                }
            } else {

                priorityMapper.put(key.getAndIncrement(), sp);

            }
        }

        List<Species> speciesFilters = new LinkedList<>();
        priorityMapper.entrySet().stream().forEach(map -> {
            speciesFilters.add(map.getValue());
        });

        return speciesFilters;
    }

}
