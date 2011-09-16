package uk.ac.ebi.ep.web;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.ac.ebi.ep.core.search.EnzymeFinder;
import uk.ac.ebi.ep.core.search.EnzymeRetriever;
import uk.ac.ebi.ep.core.search.IEnzymeFinder;
import uk.ac.ebi.ep.core.search.IEnzymeRetriever;
import uk.ac.ebi.ep.entry.exception.EnzymeRetrieverException;
import uk.ac.ebi.ep.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.search.exception.EnzymeFinderException;
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
    public static final int TOP_RESULT_SIZE = 10;
    public static final int MAX_DISPLAYED_PAGES = 1;
    private static final Logger LOGGER = Logger.getLogger(SearchController.class);
//********************************* VARIABLES ********************************//


    public enum Fields {
        enzyme,
        proteinStructure,
        reactionsPathways,
        molecules,
        diseaseDrugs,
        literature,
        brief,
        full
    }


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

    /**
     * Process the entry page
     * @param accession
     * @param field
     * @param model
     * @return
     */
    @RequestMapping(value="/search/{accession}/{field}")
    public String viewReationsPathways(
            @PathVariable String accession, @PathVariable String field, Model model) {
        Fields requestedField = Fields.valueOf(field);
        IEnzymeRetriever retriever = new EnzymeRetriever();
        EnzymeModel enzymeModel = null;
        String responsePage = "entry";
        switch (requestedField) {
            case proteinStructure: {
                try {
                    enzymeModel = retriever.getProteinStructure(accession);
                } catch (EnzymeRetrieverException ex) {
                    LOGGER.error("Unable to retrieve the entry!",  ex);
                }
                 break;
            }
            case reactionsPathways: {
                try {
                    enzymeModel = retriever.getReactionsPathways(accession);
                } catch (EnzymeRetrieverException ex) {
                    LOGGER.error("Unable to retrieve the entry!",  ex);
                }
                 break;
            }
            case molecules: {
                try {
                    enzymeModel = retriever.getMolecules(accession);
                } catch (EnzymeRetrieverException ex) {
                    LOGGER.error("Unable to retrieve the entry!",  ex);
                }
                 break;
            }
            case diseaseDrugs: {
                try {
                    enzymeModel = retriever.getEnzyme(accession);
                } catch (EnzymeRetrieverException ex) {
                    LOGGER.error("Unable to retrieve the entry!",  ex);
                }
                 break;
            }            
            case literature: {
                try {
                    enzymeModel = retriever.getLiterature(accession);
                } catch (EnzymeRetrieverException ex) {
                    LOGGER.error("Unable to retrieve the entry!",  ex);
                }
                 break;
            }            
            
            default: {
                try {
                    enzymeModel = retriever.getEnzyme(accession);
                } catch (EnzymeRetrieverException ex) {
                    LOGGER.error("Unable to retrieve the entry! ",  ex);
                }
                requestedField = Fields.enzyme;
                 break;
            }
        }
        enzymeModel.setRequestedfield(requestedField.name());
        model.addAttribute("enzymeModel", enzymeModel);
        return responsePage;
    }

    /**
     * This method is an entry point that accepts the request when the search home
     * page is loaded. It then forwards the request to the search page.
     *
     * @param model
     * @return
     */
    @RequestMapping(value="/")
    public String viewSearchHome(Model model) {
        SearchModel searchModelForm = new SearchModel();
        SearchParams searchParams = new SearchParams();
        searchParams.setText("Enter a name to search");
        searchParams.setStart(0);
        searchModelForm.setSearchparams(searchParams);
         model.addAttribute("searchModel", searchModelForm);
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
    public String getSearchResult(SearchModel searchModel, BindingResult result, Model model) {
        postSearchResult(searchModel, result, model);
        return "search";
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
    public String postSearchResult(SearchModel searchModelForm,  BindingResult result, Model model) {
        if (searchModelForm == null) {
            return "search";
        }
        LOGGER.debug("SEARCH start");
        SearchParams searchParameters = searchModelForm.getSearchparams();        
        IEnzymeFinder finder = new EnzymeFinder();
        searchParameters.setSize(SearchController.TOP_RESULT_SIZE);
        SearchResults resultSet = null;
        LOGGER.debug("SEARCH before finder.getEnzymes");
        try {
            resultSet = finder.getEnzymes(searchParameters);
        } catch (EnzymeFinderException ex) {
            LOGGER.error("Unable to create the result list because an error " +
                    "has occurred in the find method! \n", ex);
        }
        LOGGER.debug("SEARCH before pagination");
        Pagination pagination = new Pagination(
                resultSet.getTotalfound(), searchParameters.getSize());
        pagination.setMaxDisplayedPages(MAX_DISPLAYED_PAGES);
        int totalPage = pagination.calTotalPages();
        pagination.setTotalPages(totalPage);
        pagination.calCurrentPage(searchParameters.getStart());
        model.addAttribute("pagination", pagination);
        LOGGER.debug("SEARCH after  pagination");
        searchModelForm.setSearchresults(resultSet);
        model.addAttribute("searchModel", searchModelForm);
        LOGGER.debug("SEARCH end");
        return "search";
    }

    @RequestMapping(value = "/underconstruction", method = RequestMethod.GET)
    public String getSearchResult(Model model) {
        return "underconstruction";
    }
}
