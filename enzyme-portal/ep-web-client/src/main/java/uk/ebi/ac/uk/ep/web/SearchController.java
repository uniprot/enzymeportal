package uk.ebi.ac.uk.ep.web;

import java.util.List;
import org.apache.log4j.Logger;
import uk.ac.ebi.ep.search.exception.EnzymeFinderException;
import uk.ac.ebi.ep.search.parameter.SearchParams;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.ac.ebi.ep.core.search.EnzymeFinder;
import uk.ac.ebi.ep.core.search.IEnzymeFinder;
import uk.ac.ebi.ep.search.result.Pagination;
import uk.ac.ebi.ep.search.result.jaxb.EnzymeSearchResults;
import uk.ac.ebi.ep.search.result.jaxb.EnzymeSummary;
import uk.ac.ebi.ep.search.result.jaxb.EnzymeSummaryCollection;
import uk.ac.ebi.ep.search.result.jaxb.SearchFilter;

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
    public static final int MAX_DISPLAYED_PAGES = 5;
    private static Logger log = Logger.getLogger(SearchController.class);
//********************************* VARIABLES ********************************//


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//
    
    @RequestMapping(value="/entry")
    public String viewHomePage(Model model) {
         model.addAttribute("entryParameters", new SearchParams());
        return "entry";
    }


    @RequestMapping(value="/")
    public String viewSearchHome(Model model) {
         model.addAttribute("searchParameters", new SearchParams());
        return "searchHome";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String searchByKeywords(SearchParams searchParameters, BindingResult result, Model model) {
        System.out.println(searchParameters.getKeywords());
        model.addAttribute("searchParameters", searchParameters);
        return "searchHome";
    }

    @RequestMapping(value = "/showResults", method = RequestMethod.GET)
    public String viewSearchResult(SearchParams searchParameters, BindingResult result, Model model) {
        IEnzymeFinder finder = new EnzymeFinder();
        int start = searchParameters.getStart();
        searchParameters.setSize(SearchController.TOP_RESULT_SIZE);
        EnzymeSearchResults resultSet = null;
        try {
            resultSet = finder.getEnzymes(searchParameters);
        } catch (EnzymeFinderException ex) {
            log.error("Unable to create the result list because an error " +
                    "has occurred in the find method! \n", ex);
        }
        EnzymeSummaryCollection collection = resultSet.getEnzymesummarycollection();
        List<EnzymeSummary> enzymeSummaryList =collection.getEnzymesummary();
        SearchFilter searchFilter = resultSet.getSearchfilter();
        /*
        PagedListHolder pagedListHolder = new PagedListHolder(enzymeSummaryList);
        pagedListHolder.s
         *
         */
        //searchParameters.setStart(1);
        //TODO: merge paging to search param?
        Pagination pagination = new Pagination(
                collection.getTotalfound(), searchParameters.getSize());
        pagination.setMaxDisplayedPages(MAX_DISPLAYED_PAGES);
        int totalPage = pagination.calTotalPages();
        pagination.setTotalPages(totalPage);
        pagination.calCurrentPage(start);

        model.addAttribute("searchParameters", searchParameters);
        model.addAttribute("pagination", pagination);
        //model.addAttribute("enzymeSummaryCollection", collection);
        //model.addAttribute("searchFilter", searchFilter);
        model.addAttribute("resultSet", resultSet);
        return "searchHome";
    }
}
