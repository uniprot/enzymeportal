/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import uk.ac.ebi.ep.data.search.model.SearchModel;
import uk.ac.ebi.ep.ebeye.model.EBISearchResult;
import uk.ac.ebi.ep.ebeye.model.ModelService;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Controller
public class EnzymeCentricController extends AbstractController {

    private static final String SEARCH = "/search";
    private static final String FILTER = "/search/filter";
    private static final String ENZYME_CENTRIC_PAGE = "enzymes";

    @RequestMapping(value = SEARCH, method = RequestMethod.GET)
    public String getSearchResults(@RequestParam(required = false, value = "searchKey") String searchKey, @RequestParam(required = false, value = "filterFacet") List<String> filters, SearchModel searchModel, BindingResult result,
            Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        if (filters == null) {
            filters = new ArrayList<>();
        }
        if (!filters.isEmpty()) {
            return filterSearchResult(searchKey, filters, searchModel, model, request);
        }

        return postSearchResult(searchModel, model, request);
    }

    @RequestMapping(value = SEARCH, method = RequestMethod.POST)
    public String postSearchResult(SearchModel searchModel, Model model, HttpServletRequest request) {
        String view = "error";
        int page = 0;

        String searchKey = searchModel.getSearchparams().getText().trim().toLowerCase();
        EBISearchResult ebiSearchResult = getEbiSearchResult(searchKey, page);
        if (ebiSearchResult != null) {

            model.addAttribute("searchKey", searchKey);
            model.addAttribute("searchModel", searchModel);
            model.addAttribute(SEARCH_VIDEO, SEARCH_VIDEO);
            model.addAttribute("ebiResult", ebiSearchResult);
            model.addAttribute("enzymeView", ebiSearchResult.getEntries());
            model.addAttribute("enzymeFacet", ebiSearchResult.getFacets());
            view = ENZYME_CENTRIC_PAGE;
        }

        return view;
    }

    @RequestMapping(value = FILTER, method = RequestMethod.POST)
    public String filterSearchResult(@RequestParam(required = true, value = "searchKey") String searchKey, @RequestParam(required = false, value = "filterFacet") List<String> filters, SearchModel searchModel, Model model, HttpServletRequest request) {
        String view = "error";
        int page = 0;
        EBISearchResult ebiSearchResult = filterEbiSearchResult(searchKey, page, filters);
        if (ebiSearchResult != null) {

            model.addAttribute("filtersApplied",filters);
            model.addAttribute("searchKey", searchKey);
            model.addAttribute("searchModel", searchModel);
            model.addAttribute(SEARCH_VIDEO, SEARCH_VIDEO);
            model.addAttribute("ebiResult", ebiSearchResult);
            model.addAttribute("enzymeView", ebiSearchResult.getEntries());
            model.addAttribute("enzymeFacet", ebiSearchResult.getFacets());
            view = ENZYME_CENTRIC_PAGE;
            //return "redirect:"+view;
        }

        return view;
    }

    private EBISearchResult getEbiSearchResult(String query, int page) {
        ModelService service = new ModelService();

        return service.getModelSearchResult(query, page);
    }

    private EBISearchResult filterEbiSearchResult(String query, int page, List<String> filters) {
        ModelService service = new ModelService();
        String facets = filters.stream().collect(Collectors.joining(","));
        return service.filterSearchResult(query, page, facets);
    }

//    public static void main(String[] args) {
//        String query = "kinase";
//        query = "sildenafil";
//        ModelService service = new ModelService();
//
//        EBISearchResult result = service.getModelSearchResult(query, 0);
//        for (EnzymeView entry : result.getEntries()) {
//            //System.out.println(""+ entry.getFields().getProteinName().size());
//            //entry.getFields().getProteinName().stream().forEach(name -> System.out.println("protein "+ name));
//            //System.out.println("ENTRRY " + entry.getEc() + " : " + entry.getEnzymeName() + " "+ entry.getEnzymeFamily() + " "+ entry.getNumEnzymeHits() + " "+ entry.getSpecies() );
//            System.out.println(" protein " + entry.getProteins());
//        }
////        for (Facet facet : result.getFacets()) {
////            System.out.println(facet.getTotal() + "FACETS " + facet.getId() + " " + facet.getLabel() + " value : " + facet.getFacetValues());
////        }
////
////
//    }
    
    
    
    
    
    
    
        private static final String SHOW_ENZYMES_V = "/eview";
    private static final String ENZYME_CENTRIC_PAGE_V = "eview";

    @RequestMapping(value = SHOW_ENZYMES_V, method = RequestMethod.GET)
    public String showEnzymesView(Model model) {
        String query = "cathepsin";
        query = "mTOR";
        int page = 0;
        ModelService service = new ModelService();

        EBISearchResult eBISearchResult = service.getModelSearchResult(query, page);

         model.addAttribute("searchKey", query);
        model.addAttribute("ebiResult", eBISearchResult);
        model.addAttribute("enzymeView", eBISearchResult.getEntries());
        model.addAttribute("enzymeFacet", eBISearchResult.getFacets());
        model.addAttribute(BROWSE_VIDEO, BROWSE_VIDEO);

        return ENZYME_CENTRIC_PAGE_V;
    }
}
