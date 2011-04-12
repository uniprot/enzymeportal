package uk.ebi.ac.uk.ep.web;

import uk.ac.ebi.ep.search.parameter.SearchParams;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.ac.ebi.ep.core.search.EnzymeFinder;
import uk.ac.ebi.ep.core.search.IEnzymeFinder;
import uk.ac.ebi.ep.search.result.EnzymeResultSet;

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
        IEnzymeFinder finder = new EnzymeFinder();
        EnzymeResultSet resultSet = finder.find(searchParameters);
        model.addAttribute("searchParameters", searchParameters);
        model.addAttribute("enzymes", resultSet.getEnzymeSummaryCollection().getEnzymeSummary());
        return "searchHome";
    }
}
