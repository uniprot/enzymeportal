/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.search.model.Compound;
import uk.ac.ebi.ep.data.search.model.Disease;
import uk.ac.ebi.ep.data.search.model.EcNumber;
import uk.ac.ebi.ep.data.search.model.SearchFilters;
import uk.ac.ebi.ep.data.search.model.SearchModel;
import uk.ac.ebi.ep.data.search.model.SearchParams;
import uk.ac.ebi.ep.data.search.model.SearchResults;
import uk.ac.ebi.ep.data.search.model.Species;
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
    private static final String FILTER_BY_FACETS = "/taxonomy/filter";
    private static final String GET_COUNT_FOR_ORGANISMS = "/service/organism-count";

    private static final int SEARCH_PAGESIZE = 10;

    @RequestMapping(value = BROWSE_TAXONOMY, method = RequestMethod.GET)
    public String showOrganisms(Model model) {

        SearchModel searchModelForm = searchform();
        model.addAttribute("searchModel", searchModelForm);

        return ORGANISMS;
    }

    @RequestMapping(value = SEARCH_BY_TAX_ID, method = RequestMethod.GET)
    public String searchByTaxId(@ModelAttribute("searchModel") SearchModel searchModel,
            @RequestParam(value = "entryid", required = true) Long entryID, @RequestParam(value = "entryname", required = false) String entryName,
            Model model, HttpServletRequest request, HttpSession session, Pageable pageable, RedirectAttributes attributes) {

        pageable = new PageRequest(0, SEARCH_PAGESIZE, Sort.Direction.ASC, "function", "entryType");

        Page<UniprotEntry> page = this.enzymePortalService.findEnzymesByTaxonomy(entryID, pageable);

        List<Species> species = enzymePortalService.findSpeciesByTaxId(entryID);
        List<Compound> compouds = enzymePortalService.findCompoundsByTaxId(entryID);
        List<Disease> diseases = enzymePortalService.findDiseasesByTaxId(entryID);

        List<EcNumber> enzymeFamilies = enzymePortalService.findEnzymeFamiliesByTaxId(entryID);

        SearchParams searchParams = searchModel.getSearchparams();
        searchParams.setStart(0);
        searchParams.setType(SearchParams.SearchType.KEYWORD);
        searchParams.setText(entryName);
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

        model.addAttribute("organismName", entryName);
        model.addAttribute("taxId", entryID);

        // model.addAttribute("summaryEntries", result);
        SearchResults searchResults = new SearchResults();

        searchResults.setTotalfound(page.getTotalElements());
        SearchFilters filters = new SearchFilters();
        filters.setSpecies(species);
        filters.setCompounds(compouds);
        filters.setEcNumbers(enzymeFamilies);
        filters.setDiseases(diseases);

        searchResults.setSearchfilters(filters);
        searchResults.setSummaryentries(result);

        searchModel.setSearchresults(searchResults);
        model.addAttribute("searchModel", searchModel);
        model.addAttribute("searchConfig", searchConfig);

        model.addAttribute("searchFilter", filters);

        String searchKey = getSearchKey(searchModel.getSearchparams());
        cacheSearch(session.getServletContext(), searchKey, searchResults);
        setLastSummaries(session, searchResults.getSummaryentries());
        clearHistory(session);
        addToHistory(session, searchModel.getSearchparams().getType(),
                searchKey);

        return RESULT;
    }

    @RequestMapping(value = SEARCH_BY_TAX_ID + "/page={pageNumber}", method = RequestMethod.GET)
    public String searchByTaxIdPaginated(@PathVariable Integer pageNumber, @ModelAttribute("searchModel") SearchModel searchModel,
            @RequestParam(value = "entryid", required = true) Long entryID, @RequestParam(value = "entryname", required = false) String entryName,
            Model model, HttpSession session, RedirectAttributes attributes) {

        if (pageNumber < 1) {
            pageNumber = 1;
        }

        Pageable pageable = new PageRequest(pageNumber - 1, SEARCH_PAGESIZE, Sort.Direction.ASC, "function", "entryType");

        Page<UniprotEntry> page = this.enzymePortalService.findEnzymesByTaxonomy(entryID, pageable);

        List<Species> species = enzymePortalService.findSpeciesByTaxId(entryID);
        List<Compound> compouds = enzymePortalService.findCompoundsByTaxId(entryID);
        List<Disease> diseases = enzymePortalService.findDiseasesByTaxId(entryID);
        List<EcNumber> enzymeFamilies = enzymePortalService.findEnzymeFamiliesByTaxId(entryID);

        SearchParams searchParams = searchModel.getSearchparams();
        searchParams.setStart(0);
        searchParams.setType(SearchParams.SearchType.KEYWORD);
        searchParams.setText(entryName);
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

        model.addAttribute("organismName", entryName);
        model.addAttribute("taxId", entryID);

        // model.addAttribute("summaryEntries", result);
        SearchResults searchResults = new SearchResults();

        searchResults.setTotalfound(page.getTotalElements());
        SearchFilters filters = new SearchFilters();

        filters.setSpecies(species);
        filters.setCompounds(compouds);
        filters.setEcNumbers(enzymeFamilies);
        filters.setDiseases(diseases);

        searchResults.setSearchfilters(filters);
        searchResults.setSummaryentries(result);

        searchModel.setSearchresults(searchResults);
        model.addAttribute("searchModel", searchModel);
        model.addAttribute("searchConfig", searchConfig);

        model.addAttribute("searchFilter", filters);

        String searchKey = getSearchKey(searchModel.getSearchparams());
        cacheSearch(session.getServletContext(), searchKey, searchResults);
        setLastSummaries(session, searchResults.getSummaryentries());
        clearHistory(session);
        addToHistory(session, searchModel.getSearchparams().getType(),
                searchKey);

        return RESULT;

    }

    @RequestMapping(value = FILTER_BY_FACETS, method = RequestMethod.GET)
    public String filterByFacets(@ModelAttribute("searchModel") SearchModel searchModel,
            @RequestParam(value = "taxId", required = true) Long taxId,
            @RequestParam(value = "organismName", required = false) String organismName,
            Model model, HttpServletRequest request, HttpSession session, RedirectAttributes attributes) {

        List<Species> species = enzymePortalService.findSpeciesByTaxId(taxId);
        List<Compound> compouds = enzymePortalService.findCompoundsByTaxId(taxId);
        List<Disease> diseases = enzymePortalService.findDiseasesByTaxId(taxId);
        List<EcNumber> enzymeFamilies = enzymePortalService.findEnzymeFamiliesByTaxId(taxId);

        SearchFilters filters = new SearchFilters();
        filters.setSpecies(species);
        filters.setCompounds(compouds);
        filters.setDiseases(diseases);
        filters.setEcNumbers(enzymeFamilies);

        SearchParams searchParams = searchModel.getSearchparams();
        searchParams.setText(organismName);
        searchParams.setSize(SEARCH_PAGESIZE);
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
            defaultEcNumberList.stream().filter((ec) -> (selectedEcFamily.equals(ec.getEc()))).forEach((ec) -> {
                ec.setSelected(true);
            });
        });

        Pageable pageable = new PageRequest(0, SEARCH_PAGESIZE, Sort.Direction.ASC, "function", "entryType");
        Page<UniprotEntry> page = new PageImpl<>(new ArrayList<>(), pageable, 0);

        //specie only
        if (specieFilter.isEmpty() && compoundFilter.isEmpty() && diseaseFilter.isEmpty()) {
            page = enzymePortalService.filterBySpecie(taxId, pageable);

        }

        //specie only
        if (!specieFilter.isEmpty() && compoundFilter.isEmpty() && diseaseFilter.isEmpty()) {
            page = enzymePortalService.filterBySpecie(taxId, pageable);

        }

        // compounds only
        if (!compoundFilter.isEmpty() && diseaseFilter.isEmpty()) {
            page = enzymePortalService.filterBySpecieAndCompounds(taxId, compoundFilter, pageable);

        }
        // disease only
        if (compoundFilter.isEmpty() && !diseaseFilter.isEmpty()) {
            page = enzymePortalService.filterBySpecieAndDiseases(taxId, diseaseFilter, pageable);

        }

        //ec only
        if (compoundFilter.isEmpty() && diseaseFilter.isEmpty() && !ecFilter.isEmpty()) {
            page = enzymePortalService.filterBySpecieAndEc(taxId, ecFilter, pageable);

        }

        //compound and diseases
        if (!compoundFilter.isEmpty() && !diseaseFilter.isEmpty() && ecFilter.isEmpty()) {
            page = enzymePortalService.filterBySpecieAndCompoundsAndDiseases(taxId, compoundFilter, diseaseFilter, pageable);

        }

        //compound and ec
        if (!compoundFilter.isEmpty() && !ecFilter.isEmpty() && diseaseFilter.isEmpty()) {
            page = enzymePortalService.filterBySpecieAndCompoundsAndEc(taxId, compoundFilter, ecFilter, pageable);

        }

        //disease and ec
        if (!ecFilter.isEmpty() && !diseaseFilter.isEmpty() && compoundFilter.isEmpty()) {
            page = enzymePortalService.filterBySpecieAndDiseasesAndEc(taxId, diseaseFilter, ecFilter, pageable);

        }

         //disease and compounds and ec
        if (!ecFilter.isEmpty() && !diseaseFilter.isEmpty() && !compoundFilter.isEmpty()) {
            page = enzymePortalService.filterBySpecieAndCompoundsAndDiseasesAndEc(taxId, compoundFilter, diseaseFilter, ecFilter, pageable);

        }
        model.addAttribute("searchFilter", filters);
        List<UniprotEntry> result = page.getContent();

        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, page.getTotalPages());

        model.addAttribute("page", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);

        model.addAttribute("organismName", organismName);
        model.addAttribute("taxId", taxId);

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

        return RESULT;
    }

    @RequestMapping(value = SEARCH_TAXONOMY, method = RequestMethod.POST)
    public String SearchByTaxId(@ModelAttribute("searchModel") SearchModel searchModel,
            @RequestParam(value = "entryid", required = false) Long entryID, @RequestParam(value = "entryname", required = false) String entryName,
            Model model, HttpServletRequest request, HttpSession session, Pageable pageable, RedirectAttributes attributes) {

        return searchByTaxId(searchModel, entryID, entryName, model, request, session, pageable, attributes);

    }

    /**
     * Applies filters taken from the search parameters to the search results.
     *
     * @param searchModel
     * @param request
     */
    private void applyFiltersAdapted(SearchModel searchModel, HttpServletRequest request) {

        if (searchModel != null) {

            SearchParams searchParameters = searchModel.getSearchparams();
            SearchResults resultSet = searchModel.getSearchresults();

            String compound_autocompleteFilter = request.getParameter("searchparams.compounds");
            String specie_autocompleteFilter = request.getParameter("_ctempList_selected");
            String diseases_autocompleteFilter = request.getParameter("_DtempList_selected");

            // Filter:
            List<String> speciesFilter = searchParameters.getSpecies();
            List<String> compoundsFilter = searchParameters.getCompounds();
            List<String> diseasesFilter = searchParameters.getDiseases();

            //remove empty string in the filter to avoid unsual behavior of the filter facets
            if (speciesFilter.contains("")) {
                speciesFilter.remove("");

            }
            if (compoundsFilter.contains("")) {
                compoundsFilter.remove("");

            }
            if (diseasesFilter.contains("")) {
                diseasesFilter.remove("");
            }

            //to ensure that the seleted item is used in species filter, add the selected to the list. this is a workaround. different JS were used for auto complete and normal filter
            if ((specie_autocompleteFilter != null && StringUtils.hasLength(specie_autocompleteFilter) == true) && StringUtils.isEmpty(compound_autocompleteFilter) && StringUtils.isEmpty(diseases_autocompleteFilter)) {
                speciesFilter.add(specie_autocompleteFilter);

            }

            if ((diseases_autocompleteFilter != null && StringUtils.hasLength(diseases_autocompleteFilter) == true) && StringUtils.isEmpty(compound_autocompleteFilter) && StringUtils.isEmpty(specie_autocompleteFilter)) {
                diseasesFilter.add(diseases_autocompleteFilter);

            }

//both from auto complete and normal selection. selected items are displayed on top the list and returns back to the orignial list when not selected.
            SearchResults searchResults = resultSet;
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

        }

    }

    @ResponseBody
    @RequestMapping(value = GET_COUNT_FOR_ORGANISMS, method = RequestMethod.GET)
    public List<Taxonomy> getCountForOrganisms(@RequestParam(value = "taxids", required = true) List<Long> taxids) {

        long startTime = System.nanoTime();

        List<Taxonomy> organisms = enzymePortalService.getCountForOrganisms(taxids);

        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        long elapsedtime = TimeUnit.SECONDS.convert(duration, TimeUnit.NANOSECONDS);
        LOGGER.warn("Duration :  (" + elapsedtime + " sec)");

        //return enzymePortalService.getCountForOrganisms(taxids);
        return organisms;
    }

}
