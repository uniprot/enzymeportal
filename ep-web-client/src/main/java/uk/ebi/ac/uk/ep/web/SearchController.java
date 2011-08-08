package uk.ebi.ac.uk.ep.web;

import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import uk.ac.ebi.ep.entry.exception.EnzymeRetrieverException;
import uk.ac.ebi.ep.search.exception.EnzymeFinderException;
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
import uk.ac.ebi.ep.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.search.result.Pagination;
import uk.ac.ebi.ep.search.model.SearchResults;
import uk.ac.ebi.ep.search.model.SearchModel;
import uk.ac.ebi.ep.search.model.SearchParams;

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
    private static Logger log = Logger.getLogger(SearchController.class);
//********************************* VARIABLES ********************************//


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//
    
    @RequestMapping(value="/entry/{accession}")
    public String viewHomePage(@PathVariable String accession, EnzymeModel searchModelForm,  BindingResult result, Model model) {
        String acc = accession;
        IEnzymeRetriever retriever = new EnzymeRetriever();        
        EnzymeModel enzymeModel = null;
        try {
            enzymeModel = retriever.retieveEnzyme(accession);
        } catch (EnzymeRetrieverException ex) {
            log.error("Unable to retrieve the entry! ",  ex);
        }
         model.addAttribute("enzymeModel", enzymeModel);
        return "entry";
    }


    @RequestMapping(value="/")
    public String viewSearchHome(Model model) {
        SearchModel searchModelForm = new SearchModel();
        SearchParams searchParams = new SearchParams();
        //searchParams.setText("sildenafil");
        searchParams.setStart(0);
        searchModelForm.setSearchparams(searchParams);
        //resultSetForm.getSummaryentries().setEnzymesummarycollection(emptyResults);
         model.addAttribute("searchModel", searchModelForm);
        return "index";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String searchByKeywords(SearchModel searchModel, BindingResult result, Model model) {
        //System.out.println(searchParameters.getKeywords());
        model.addAttribute("searchModel", searchModel);
        return "index";
    }

    @RequestMapping(value = "/results", method = RequestMethod.POST)
    public String viewSearchResult(SearchModel searchModelForm,  BindingResult result, Model model) {
        SearchParams searchParameters = searchModelForm.getSearchparams();
        List<String> String = searchModelForm.getSearchparams().getSpecies();
        //String[] selectedSpecies = searchParameters.getSelectedSPecies();
        IEnzymeFinder finder = new EnzymeFinder();
        //int start = searchParameters.getStart();
        searchParameters.setSize(SearchController.TOP_RESULT_SIZE);

        SearchResults resultSet = null;
        try {
            resultSet = finder.getEnzymes(searchParameters);
        } catch (EnzymeFinderException ex) {
            log.error("Unable to create the result list because an error " +
                    "has occurred in the find method! \n", ex);
        }
        /*
        PagedListHolder pagedListHolder = new PagedListHolder(enzymeSummaryList);
        pagedListHolder.s
         *
         */
        //searchParameters.setStart(1);
        //TODO: merge paging to search param?
        Pagination pagination = new Pagination(
                resultSet.getTotalfound(), searchParameters.getSize());
        pagination.setMaxDisplayedPages(MAX_DISPLAYED_PAGES);
        int totalPage = pagination.calTotalPages();
        pagination.setTotalPages(totalPage);
        pagination.calCurrentPage(searchParameters.getStart());

        //model.addAttribute("searchParameters", searchParameters);
        model.addAttribute("pagination", pagination);
        //model.addAttribute("enzymeSummaryCollection", collection);
        //model.addAttribute("searchFilter", searchFilter);
        searchModelForm.setSearchresults(resultSet);
        model.addAttribute("searchModel", searchModelForm);
        //model.addAttribute("enzymeModel", new EnzymeModel());
        return "index";
    }
}
