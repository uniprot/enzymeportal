package uk.ebi.ac.uk.ep.web;

import java.util.logging.Level;
import javax.xml.rpc.ServiceException;
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
import uk.ac.ebi.ep.search.result.EnzymeSearchResults;

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
    private static Logger log = Logger.getLogger(SearchController.class);
//********************************* VARIABLES ********************************//


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

    @RequestMapping(value="/")
    public String viewSearchHome(Model model) {
         model.addAttribute("searchParameters", new SearchParams());
        return "searchHome";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String searchByKeywords(SearchParams searchParameters, BindingResult result, Model model) {
        System.out.println(searchParameters.getKeywords());
        model.addAttribute("searchParameters", searchParameters);
        return "searchHome";
    }

    @RequestMapping(value = "/showResults", method = RequestMethod.POST)
    public String viewSearchResult(SearchParams searchParameters, BindingResult result, Model model) {
        IEnzymeFinder finder = null;
        try {
            finder = new EnzymeFinder();
        } catch (ServiceException ex) {
            log.error(ex.getMessage());
        }
        EnzymeSearchResults resultSet = null;
        try {
            resultSet = finder.find(searchParameters);
        } catch (EnzymeFinderException ex) {
            log.error("Unable to create the result list because an error " +
                    "has occurred in the find method! \n", ex);
        }
        model.addAttribute("searchParameters", searchParameters);
        model.addAttribute("enzymeSummaryCollection", resultSet.getEnzymesummarycollection());
        return "searchHome";
    }
}
