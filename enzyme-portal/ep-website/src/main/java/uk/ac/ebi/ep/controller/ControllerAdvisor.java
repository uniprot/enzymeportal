/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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

/**
 *
 * @author joseph
 */
@Controller
public class ControllerAdvisor extends AbstractController {
        protected static final String FILTER_BY_FACETS = "/search-enzymes/filter";
    private static final String RESULT = "/searches";

    private static final int SEARCH_PAGESIZE = 10;
    
    @RequestMapping(value = FILTER_BY_FACETS, method = RequestMethod.POST)
    public String filterByFacetsPost(@ModelAttribute("searchModel") SearchModel searchModel, @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "ec", required = false) String ec, @RequestParam(value = "ecname", required = false) String ecname,
            @RequestParam(value = "taxId", required = false) Long taxId, @RequestParam(value = "organismName", required = false) String organismName,
            Model model, HttpServletRequest request, HttpSession session, RedirectAttributes attributes) {

        return filterByFacets(searchModel, pageNumber, ec, ecname, taxId, organismName, model, request, session, attributes);
    }

    @RequestMapping(value = FILTER_BY_FACETS, method = RequestMethod.GET)
    public String filterByFacets(@ModelAttribute("searchModel") SearchModel searchModel, @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "ec", required = true) String ec, @RequestParam(value = "ecname", required = false) String ecname,
            @RequestParam(value = "taxId", required = false) Long taxId, @RequestParam(value = "organismName", required = false) String organismName,
            Model model, HttpServletRequest request, HttpSession session, RedirectAttributes attributes) {

        if (pageNumber < 1) {
            pageNumber = 1;
        }
        if (!StringUtils.isEmpty(ec)) {

            return filterByFacetsEC(searchModel, pageNumber, ec, ecname, model, request, session, attributes);
        } else {

            return filterByFacetsTaxonomy(searchModel, pageNumber, taxId, organismName, model, request, session, attributes);
        }

    }

    //@RequestMapping(value = FILTER_BY_FACETS, method = RequestMethod.GET)
    private String filterByFacetsEC(@ModelAttribute("searchModel") SearchModel searchModel, @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
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
        //Set<Species> speciesFilter = species.stream().collect(Collectors.toSet());
        List<Species> speciesFacets = applySpeciesFilter(species);
        filters.setSpecies(speciesFacets);
        filters.setCompounds(compouds);
        filters.setDiseases(diseases);
        filters.setEcNumbers(enzymeFamilies);

        SearchParams searchParams = searchModel.getSearchparams();
        searchParams.setText(ec);
        searchParams.setSize(SEARCH_PAGESIZE);
        searchParams.setType(SearchParams.SearchType.KEYWORD);
        searchParams.setPrevioustext("");
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
            defaultCompoundList.stream().filter((theCompound) -> (SelectedCompounds.equals(theCompound.getId()))).forEach((theCompound) -> {
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

        String searchKey = getSearchKey(searchModel.getSearchparams());

        cacheSearch(session.getServletContext(), searchKey, searchResults);
        setLastSummaries(session, searchResults.getSummaryentries());

        clearHistory(session);
        addToHistory(session, searchModel.getSearchparams().getType(),
                searchKey);
        model.addAttribute("searchFilter", filters);
        model.addAttribute(BROWSE_VIDEO, BROWSE_VIDEO);
        request.setAttribute("searchTerm", searchModel.getSearchparams().getText());
        model.addAttribute("searchModel", searchModel);
        model.addAttribute("searchConfig", searchConfig);

        return RESULT;

    }

    private String filterByFacetsTaxonomy(@ModelAttribute("searchModel") SearchModel searchModel, @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
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

        Pageable pageable = new PageRequest(pageNumber - 1, SEARCH_PAGESIZE, Sort.Direction.ASC, "function", "entryType");
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
        request.setAttribute("searchTerm", searchModel.getSearchparams().getText());

        return RESULT;
    }

    protected List<Species> applySpeciesFilter(List<Species> uniqueSpecies) {
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

        uniqueSpecies.stream().forEach((sp) -> {
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
        });

        List<Species> speciesFilters = new LinkedList<>();
        priorityMapper.entrySet().stream().forEach(map -> {
            speciesFilters.add(map.getValue());
        });

        return speciesFilters;
    }

}
