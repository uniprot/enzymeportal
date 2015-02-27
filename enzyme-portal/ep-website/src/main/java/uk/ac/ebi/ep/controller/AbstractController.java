/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import uk.ac.ebi.ep.base.common.CompoundsPredicate;
import uk.ac.ebi.ep.base.common.DiseasesPredicate;
import uk.ac.ebi.ep.base.common.SpeciesPredicate;
import uk.ac.ebi.ep.base.search.EnzymeFinder;
import uk.ac.ebi.ep.common.Config;
import uk.ac.ebi.ep.common.Pagination;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.search.model.Compound;
import uk.ac.ebi.ep.data.search.model.Disease;
import uk.ac.ebi.ep.data.search.model.SearchModel;
import uk.ac.ebi.ep.data.search.model.SearchParams;
import uk.ac.ebi.ep.data.search.model.SearchResults;
import uk.ac.ebi.ep.data.search.model.Species;
import uk.ac.ebi.ep.data.service.EnzymePortalService;
import uk.ac.ebi.ep.ebeye.EbeyeRestService;
import uk.ac.ebi.ep.functions.Functions;
import uk.ac.ebi.ep.functions.HtmlUtility;



/**
 *
 * @author joseph
 */
public abstract class AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractController.class);
  
    @Autowired
    protected Config searchConfig;
    //@Autowired
   // protected IntenzConfig intenzConfig;
    @Autowired
    protected EnzymePortalService enzymePortalService;
 
    @Autowired
    protected EbeyeRestService ebeyeRestService;
  

    @ModelAttribute("searchModel")
    public SearchModel searchform() {
        SearchModel searchModelForm = new SearchModel();
        SearchParams searchParams = new SearchParams();
        searchParams.setStart(0);
        searchParams.setType(SearchParams.SearchType.KEYWORD);
        searchModelForm.setSearchparams(searchParams);
        return searchModelForm;
    }

    /**
     * Stores a search result in the application context.
     *
     * @param servletContext the application context.
     * @param searchKey the key to use for the search results in the table.
     * @param searchResult the search results.
     */
    protected void cacheSearch(ServletContext servletContext, String searchKey,
            SearchResults searchResult) {
        Map<String, SearchResults> prevSearches =
                getPreviousSearches(servletContext);
        synchronized (prevSearches) {
            while (prevSearches.size() >= searchConfig.getSearchCacheSize()) {
                // remove the eldest:
                prevSearches.remove(prevSearches.keySet().iterator().next());
            }
            prevSearches.put(searchKey, searchResult);
        }
    }

    /**
     * Retrieves any previous searches stored in the application context.
     *
     * @param servletContext the application context.
     * @return a map of searches to results.
     */
    @SuppressWarnings("unchecked")
    protected Map<String, SearchResults> getPreviousSearches(
            ServletContext servletContext) {
        Map<String, SearchResults> prevSearches = (Map<String, SearchResults>) servletContext.getAttribute(Attribute.prevSearches.name());
        if (prevSearches == null) {
            // Map implementation which maintains the order of access:
            prevSearches = Collections.synchronizedMap(
                    new LinkedHashMap<String, SearchResults>(
                    searchConfig.getSearchCacheSize(), 1, true));
            servletContext.setAttribute(Attribute.prevSearches.getName(),
                    prevSearches);

        }
        return prevSearches;
    }

    /**
     * Updates the {@link lastSummaries Attribute#lastSummaries} attribute in
     * the user's session.
     *
     * @param session
     * @param summaries
     */
    protected void setLastSummaries(HttpSession session,
            List<UniprotEntry> summaries) {
        @SuppressWarnings("unchecked")
        Map<String, UniprotEntry> lastSummaries = (Map<String, UniprotEntry>) session.getAttribute(Attribute.lastSummaries.getName());
        if (lastSummaries == null) {
            lastSummaries = new HashMap<>();
            session.setAttribute(Attribute.lastSummaries.getName(), lastSummaries);
        } else {
            lastSummaries.clear();
        }
        for (UniprotEntry summary : summaries) {
            lastSummaries.put(Functions.getSummaryBasketId(summary), summary);
        }
    }

    protected void clearHistory(HttpSession session) {
        @SuppressWarnings("unchecked")
        LinkedList<String> history = (LinkedList<String>) session.getAttribute(Attribute.history.getName());
        if (history == null) {
            history = new LinkedList<>();
            session.setAttribute(Attribute.history.getName(), history);
        } else {
            history.clear();
        }
    }

    protected void addToHistory(HttpSession session, String s) {
        @SuppressWarnings("unchecked")
        LinkedList<String> history = (LinkedList<String>) session.getAttribute(Attribute.history.getName());
        if (history == null) {
            history = new LinkedList<>();
            session.setAttribute(Attribute.history.getName(), history);
        }
        if (history.isEmpty() || !history.get(history.size() - 1).equals(s)) {
            String cleanedText = HtmlUtility.cleanText(s);
            history.add(cleanedText);
        }
    }

    /**
     * Adds a search to the user history. The history item (String) actually
     * stored depends on the type of search, so that the links can be re-created
     * in the web page properly (see
     * <code>breadcrumbs.jsp</code>).
     *
     * @param session the user session.
     * @param searchType the search type.
     * @param s the text to be added to history.
     */
    protected void addToHistory(HttpSession session, SearchParams.SearchType searchType, String s) {
        switch (searchType) {
            case KEYWORD:
                addToHistory(session, "searchparams.text=" + s);
                break;
            case COMPOUND:
                addToHistory(session,
                        "searchparams.type=COMPOUND&searchparams.text=" + s);
                break;
            case SEQUENCE:
                addToHistory(session, "searchparams.sequence=" + s);
                break;
        }
    }

    /**
     * Processes a string to normalise it to use as a key in the application
     * cache.<br> Note that the key for a ChEBI ID depends on the type of
     * search: if a keyword search, the prefix will be lowercase (
     * <code>chebi:</code>); if a compound structure search, the prefix will be
     * uppercase (
     * <code>CHEBI:</code>).
     *
     * @param searchParams the search parameters, including the original search
     * text from the user.
     * @return A normalised string.
     */
    protected String getSearchKey(SearchParams searchParams) {

        String key = null;

        switch (searchParams.getType()) {
            case KEYWORD:
                key = searchParams.getText().trim().toLowerCase();
                break;
            case SEQUENCE:
                key = searchParams.getSequence().trim().toUpperCase()
                        .replaceAll("[\n\r]", "");
                break;
            case COMPOUND:
                key = searchParams.getText().trim().toUpperCase();
                break;
            default:
                key = searchParams.getText().trim().toLowerCase();
        }
        return key;
    }

    /**
     * Searches by keyword.
     *
     * @param searchParameters the search parameters.
     * @return the search results.
     */
    protected SearchResults searchKeyword(SearchParams searchParameters) {
        SearchResults results = null;
        EnzymeFinder finder = new EnzymeFinder(enzymePortalService,ebeyeRestService);
 
            results = finder.getEnzymes(searchParameters);
  
        return results;
    }

    /**
     * Adds a pagination object to the model, suitable to the search results and
     * search parameters.
     *
     * @param searchModel the search model including the search parameters
     * (including pagination start).
     * @return a pagination.
     */
    protected Pagination getPagination(SearchModel searchModel) {
        Pagination pagination = new Pagination(
                searchModel.getSearchresults().getSummaryentries().size(),
                searchConfig.getResultsPerPage());
        pagination.setFirstResult(searchModel.getSearchparams().getStart());
        return pagination;
    }

    /**
     * Applies filters taken from the search parameters to the search results.
     *
     * @param searchModel
     * @param request
     */
    protected void applyFilters(SearchModel searchModel, HttpServletRequest request) {

        if (searchModel != null) {

            SearchParams searchParameters = searchModel.getSearchparams();
            searchParameters.setSize(searchConfig.getResultsPerPage());
            SearchResults resultSet = searchModel.getSearchresults();


            final int numOfResults = resultSet.getSummaryentries().size();
            Pagination pagination = new Pagination(
                    numOfResults, searchParameters.getSize());
            pagination.setFirstResult(searchParameters.getStart());

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

            for (String selectedItems : searchParameters.getSpecies()) {

                for (Species theSpecies : defaultSpeciesList) {
                    if (selectedItems.equals(theSpecies.getScientificname())) {
                        theSpecies.setSelected(true);
                    }

                }
            }

            List<Compound> defaultCompoundList = searchResults.getSearchfilters().getCompounds();
            resetSelectedCompounds(defaultCompoundList);

            for (String SelectedCompounds : searchParameters.getCompounds()) {
                for (Compound theCompound : defaultCompoundList) {

                    if (SelectedCompounds.equals(theCompound.getName())) {
                        theCompound.setSelected(true);

                    }
                }
            }

            List<Disease> defaultDiseaseList = searchResults.getSearchfilters().getDiseases();
            resetSelectedDisease(defaultDiseaseList);

            for (String selectedDisease : searchParameters.getDiseases()) {
                for (Disease disease : defaultDiseaseList) {
                    if (selectedDisease.equals(disease.getName())) {
                        disease.setSelected(true);
                    }
                }
            }



            //if an item is seleted, then filter the list
            if (!speciesFilter.isEmpty() || !compoundsFilter.isEmpty() || !diseasesFilter.isEmpty()) {
                List<UniprotEntry> filteredResults =
                        new LinkedList<>(resultSet.getSummaryentries());



                CollectionUtils.filter(filteredResults,
                        new SpeciesPredicate(speciesFilter));
                CollectionUtils.filter(filteredResults,
                        new CompoundsPredicate(compoundsFilter));
                CollectionUtils.filter(filteredResults,
                        new DiseasesPredicate(diseasesFilter));
                

                // Create a new SearchResults, don't modify the one in session
                SearchResults sr = new SearchResults();

                // Update the number of results to paginate:
                pagination.setNumberOfResults(filteredResults.size());

               
                sr.setSearchfilters(resultSet.getSearchfilters());
                sr.setSummaryentries(filteredResults);
                // show the total number of hits (w/o filtering):
                sr.setTotalfound(resultSet.getTotalfound());
                searchModel.setSearchresults(sr);


        }


    }

    }

    protected void resetSelectedSpecies(List<Species> speciesList) {
        for (Species sp : speciesList) {

            sp.setSelected(false);

        }
    }

    protected void resetSelectedCompounds(List<Compound> compounds) {
        for (Compound compound : compounds) {
            compound.setSelected(false);
        }
    }

    protected void resetSelectedDisease(List<Disease> diseases) {
        for (Disease disease : diseases) {
            disease.setSelected(false);
        }
    }
}
