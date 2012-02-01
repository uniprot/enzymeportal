package uk.ac.ebi.ep.web;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.ac.ebi.ep.adapter.chebi.ChebiConfig;
import uk.ac.ebi.ep.adapter.ebeye.EbeyeConfig;
import uk.ac.ebi.ep.adapter.intenz.IntenzConfig;
import uk.ac.ebi.ep.adapter.reactome.ReactomeConfig;
import uk.ac.ebi.ep.adapter.uniprot.UniprotConfig;
import uk.ac.ebi.ep.core.SpeciesDefaultWrapper;
import uk.ac.ebi.ep.core.filter.CompoundsPredicate;
import uk.ac.ebi.ep.core.filter.DiseasesPredicate;
import uk.ac.ebi.ep.core.filter.SpeciesPredicate;
import uk.ac.ebi.ep.core.search.Config;
import uk.ac.ebi.ep.core.search.EnzymeFinder;
import uk.ac.ebi.ep.core.search.EnzymeRetriever;
import uk.ac.ebi.ep.entry.Field;
import uk.ac.ebi.ep.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.search.exception.EnzymeFinderException;
import uk.ac.ebi.ep.search.model.EnzymeAccession;
import uk.ac.ebi.ep.search.model.EnzymeSummary;
import uk.ac.ebi.ep.search.model.SearchModel;
import uk.ac.ebi.ep.search.model.SearchParams;
import uk.ac.ebi.ep.search.model.SearchResults;
import uk.ac.ebi.ep.search.result.Pagination;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
@Controller
public class SearchController {

    private static final Logger LOGGER = Logger.getLogger(SearchController.class);

    private enum ResponsePage {

        ENTRY, ERROR;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
    @Autowired
    private EbeyeConfig ebeyeConfig;
    @Autowired
    private UniprotConfig uniprotConfig;
    @Autowired
    private Config searchConfig;
    @Autowired
    private IntenzConfig intenzConfig;
    @Autowired
    private ReactomeConfig reactomeConfig;
    @Autowired
    private ChebiConfig chebiConfig;

    /**
     * Process the entry page,
     * @param accession The UniProt accession of the enzyme.
     * @param field the requested tab.
     * @param model
     * @return 
     */
    @RequestMapping(value = "/search/{accession}/{field}")
    protected String getEnzymeModel(Model model,
            @PathVariable String accession, @PathVariable String field,
            HttpSession session) {
        Field requestedField = Field.valueOf(field);
        EnzymeRetriever retriever = new EnzymeRetriever(searchConfig);
        retriever.getEbeyeAdapter().setConfig(ebeyeConfig);
        retriever.getUniprotAdapter().setConfig(uniprotConfig);
        retriever.getIntenzAdapter().setConfig(intenzConfig);
        EnzymeModel enzymeModel = null;
        String responsePage = ResponsePage.ENTRY.toString();
        try {
            switch (requestedField) {
                case proteinStructure:
                    enzymeModel = retriever.getProteinStructure(accession);
                    break;
                case reactionsPathways:                 
                    retriever.getReactomeAdapter().setConfig(reactomeConfig);
                    enzymeModel = retriever.getReactionsPathways(accession);
                    break;
                case molecules:
                	retriever.getChebiAdapter().setConfig(chebiConfig);
                    enzymeModel = retriever.getMolecules(accession);
                    break;
                case diseaseDrugs:
                    enzymeModel = retriever.getEnzyme(accession);
                    break;
                case literature:
                    enzymeModel = retriever.getLiterature(accession);
                    break;
                default:
                    enzymeModel = retriever.getEnzyme(accession);
                    requestedField = Field.enzyme;
                    break;
            }
            enzymeModel.setRequestedfield(requestedField.name());
            model.addAttribute("enzymeModel", enzymeModel);
            addToHistory(session, accession);
        } catch (Exception ex) {
            LOGGER.error("Unable to retrieve the entry!", ex);
            responsePage = ResponsePage.ERROR.toString();
        }
        return responsePage;
    }

    /**
     * This method is an entry point that accepts the request when the search home
     * page is loaded. It then forwards the request to the search page.
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/")
    public String viewSearchHome(Model model, HttpSession session) {
        SearchModel searchModelForm = new SearchModel();
        SearchParams searchParams = new SearchParams();
        searchParams.setText("Enter a name to search");
        searchParams.setStart(0);
        searchModelForm.setSearchparams(searchParams);
        model.addAttribute("searchModel", searchModelForm);
        clearHistory(session);
        return "index";
    }

    /**
     * A wrapper of {@code postSearchResult} method, created to accept the search
     * request using GET.
     * @param searchModel
     * @param result
     * @param model
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String getSearchResult(SearchModel searchModel, BindingResult result,
            Model model, HttpSession session) {
        return postSearchResult(searchModel, result, model, session);
    }

    /**
     * Processes the search request. When user enters a search text and presses
     * the submit button the request is processed here.
     * @param searchModelForm
     * @param result
     * @param model
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String postSearchResult(SearchModel searchModelForm, BindingResult result,
            Model model, HttpSession session) {
        String view = "search";

        List<EnzymeSummary> summaryEntryFilteredResults = new LinkedList<EnzymeSummary>();

        if (searchModelForm != null) {
            try {
                SearchParams searchParameters = searchModelForm.getSearchparams();
                searchParameters.setSize(searchConfig.getResultsPerPage());
                SearchResults resultSet = null;
                // See if it is already there, perhaps we are paginating:
                @SuppressWarnings("unchecked")
                Map<String, SearchResults> prevSearches = (Map<String, SearchResults>) session.getServletContext().getAttribute("searches");
                if (prevSearches != null) {

                    resultSet = prevSearches.get(searchParameters.getText().toLowerCase());
                } else {
                    // Map implementation which maintains the order of access:
                    prevSearches = new LinkedHashMap<String, SearchResults>(
                            searchConfig.getSearchCacheSize(), 1, true);
                    session.getServletContext().setAttribute("searches", prevSearches);
                }
                if (resultSet == null) {
                    // Make a new search:
                    EnzymeFinder finder = new EnzymeFinder(searchConfig);
                    finder.getEbeyeAdapter().setConfig(ebeyeConfig);
                    finder.getUniprotAdapter().setConfig(uniprotConfig);
                    finder.getIntenzAdapter().setConfig(intenzConfig);
                    try {
                        resultSet = finder.getEnzymes(searchParameters);
                        // cache it in the session, making room if necessary:
                        while (prevSearches.size() >= searchConfig.getSearchCacheSize()) {
                            // remove the eldest:
                            prevSearches.remove(prevSearches.keySet().iterator().next());
                        }
                        prevSearches.put(searchParameters.getText().toLowerCase(), resultSet);
                    } catch (EnzymeFinderException ex) {
                        LOGGER.error("Unable to create the result list because an error "
                                + "has occurred in the find method! \n", ex);
                    }
                }

                final int numOfResults = resultSet.getSummaryentries().size();
                                Pagination pagination = new Pagination(
                        numOfResults, searchParameters.getSize());
                        pagination.setFirstResult(searchParameters.getStart());
                                
                // Filter:
                List<String> speciesFilter = searchParameters.getSpecies();
                List<String> compoundsFilter = searchParameters.getCompounds();
                List<String> diseasesFilter = searchParameters.getDiseases();
                if (!speciesFilter.isEmpty() || !compoundsFilter.isEmpty() || !diseasesFilter.isEmpty()) {
                    List<EnzymeSummary> filteredResults =
                            new LinkedList<EnzymeSummary>(resultSet.getSummaryentries());

                    CollectionUtils.filter(filteredResults,
                            new SpeciesPredicate(speciesFilter));
                    CollectionUtils.filter(filteredResults,
                            new CompoundsPredicate(compoundsFilter));
                    CollectionUtils.filter(filteredResults,
                            new DiseasesPredicate(diseasesFilter));
                    // Create a new SearchResults, don't modify the one in session
                    SearchResults sr = new SearchResults();

                    /**
                     * filter the result based on the species selected by user
                     */
                    List<String> checkBoxParams = searchParameters.getSpecies();
                    for (EnzymeSummary enzymeSummary : filteredResults) {

                        for (String selected : checkBoxParams) {
                            for (EnzymeAccession enzymeAccession : enzymeSummary.getRelatedspecies()) {

                                if (selected.equalsIgnoreCase(enzymeAccession.getSpecies().getScientificname())) {
                                    enzymeSummary.getUniprotaccessions().add(0, enzymeAccession.getUniprotaccessions().get(0));
                                    // enzymeSummary.setSpecies(enzymeAccession.getSpecies());
                                    enzymeSummary.setSpecies(new SpeciesDefaultWrapper(enzymeAccession.getSpecies()).getSpecies());
                                }

                            }
                        }
                        // adding the updated enzyme summaries to the filtered result
                        summaryEntryFilteredResults.add(enzymeSummary);
                    }
                   
                   pagination = new Pagination(
                         summaryEntryFilteredResults.size(), searchParameters.getSize());
                     pagination.setFirstResult(0);
                     model.addAttribute("pagination", pagination);
                    sr.setSearchfilters(resultSet.getSearchfilters());
                    sr.setSummaryentries(summaryEntryFilteredResults);
                    // show the total number of hits (w/o filtering):
                    sr.setTotalfound(resultSet.getTotalfound());
                    searchModelForm.setSearchresults(sr);

                
                
                } else {
                    // Show all of them:
                    searchModelForm.setSearchresults(resultSet);
                }
               
                model.addAttribute("searchModel", searchModelForm);
                
                model.addAttribute("pagination", pagination);

//                // Paginate:
//                final int numOfResults =
//                        searchModelForm.getSearchresults().getSummaryentries().size();
//                Pagination pagination = new Pagination(
//                        numOfResults, searchParameters.getSize());
//               // pagination.setMaxDisplayedPages(searchConfig.getMaxPages());
//                pagination.setFirstResult(searchParameters.getStart());
//                model.addAttribute("pagination", pagination);

                addToHistory(session,
                        "searchparams.text=" + searchParameters.getText());
            } catch (Exception e) {
                LOGGER.error("Failed search", e);
                view = "error";
            }
        }
        return view;
    }

    @RequestMapping(value = "/underconstruction", method = RequestMethod.GET)
    public String getSearchResult(Model model) {
        return "underconstruction";
    }

    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public String getAbout(Model model) {
        return "about";
    }

    private void addToHistory(HttpSession session, String s) {
        @SuppressWarnings("unchecked")
        List<String> history = (List<String>) session.getAttribute("history");
        if (history == null) {
            history = new ArrayList<String>();
            session.setAttribute("history", history);
        }
        if (history.isEmpty() || !history.get(history.size() - 1).equals(s)) {
            history.add(s);
        }
    }

    private void clearHistory(HttpSession session) {
        @SuppressWarnings("unchecked")
        List<String> history = (List<String>) session.getAttribute("history");
        if (history == null) {
            history = new ArrayList<String>();
            session.setAttribute("history", history);
        } else {
            history.clear();
        }
    }
}
