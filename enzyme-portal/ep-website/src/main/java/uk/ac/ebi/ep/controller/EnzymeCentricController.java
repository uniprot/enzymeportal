/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import uk.ac.ebi.ep.data.search.model.SearchModel;
import uk.ac.ebi.ep.ebeye.ModelService;
import uk.ac.ebi.ep.ebeye.PowerService;
import uk.ac.ebi.ep.ebeye.model.EBISearchResult;
import uk.ac.ebi.ep.ebeye.model.Entry;
import uk.ac.ebi.ep.ebeye.model.Protein;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Controller
public class EnzymeCentricController extends AbstractController {

    private static final String SEARCH = "/search";
    private static final String FILTER = "/search/filter";
    private static final String ENZYME_CENTRIC_PAGE = "enzymes";

    @Autowired
    private ModelService modelService;
    @Autowired
    private PowerService powerService;

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
        int startPage = 0;
        int pageSize = 10;
        int associatedProteinLimit = 5;

        String searchKey = searchModel.getSearchparams().getText().trim().toLowerCase();

        EBISearchResult ebiSearchResult = getEbiSearchResult(searchKey, startPage);

        if (ebiSearchResult != null) {
            long hitCount = ebiSearchResult.getHitCount();
            Pageable pageable = new PageRequest(startPage, pageSize);
            Page<Entry> page = new PageImpl<>(ebiSearchResult.getEntries(), pageable, hitCount);

            List<Entry> entries = page.getContent();
            List<Entry> enzymeView = new LinkedList<>();
            entries.stream().map((entry) -> {
                List<Protein> proteins = powerService.queryForUniqueProteins(entry.getEc(), searchKey, associatedProteinLimit);
                entry.setProtein(proteins);
                entry.setNumEnzymeHits(proteins.size());
                return entry;
            }).forEach((entry) -> {
                enzymeView.add(entry);
            });
            int current = page.getNumber() + 1;
            int begin = Math.max(1, current - 5);
            int end = Math.min(begin + 10, page.getTotalPages());

            model.addAttribute("page", page);
            model.addAttribute("beginIndex", begin);
            model.addAttribute("endIndex", end);
            model.addAttribute("currentIndex", current);
            model.addAttribute("enzymeView", enzymeView);

            model.addAttribute("searchKey", searchKey);
            model.addAttribute("searchModel", searchModel);
            model.addAttribute(SEARCH_VIDEO, SEARCH_VIDEO);
            model.addAttribute("ebiResult", ebiSearchResult);
//            model.addAttribute("enzymeView", ebiSearchResult.getEntries());
            model.addAttribute("enzymeFacet", ebiSearchResult.getFacets());
            view = ENZYME_CENTRIC_PAGE;
            //view = ENZYME_CENTRIC_PAGE_V;
        }

        return view;
    }

    @RequestMapping(value = FILTER, method = RequestMethod.POST)
    public String filterSearchResult(@RequestParam(required = true, value = "searchKey") String searchKey, @RequestParam(required = false, value = "filterFacet") List<String> filters, SearchModel searchModel, Model model, HttpServletRequest request) {
        String view = "error";
        int startPage = 0;
        int pageSize = 10;
        int associatedProteinLimit = 5;
        EBISearchResult ebiSearchResult = filterEbiSearchResult(searchKey, startPage, filters);
        if (ebiSearchResult != null) {
            long hitCount = ebiSearchResult.getHitCount();
            Pageable pageable = new PageRequest(startPage, pageSize);
            Page<Entry> page = new PageImpl<>(ebiSearchResult.getEntries(), pageable, hitCount);

            List<Entry> entries = page.getContent();
            List<Entry> enzymeView = new LinkedList<>();
            entries.stream().map((entry) -> {
                List<Protein> proteins = powerService.queryForUniqueProteins(entry.getEc(), searchKey, associatedProteinLimit);
                entry.setProtein(proteins);
                entry.setNumEnzymeHits(proteins.size());
                return entry;
            }).forEach((entry) -> {
                enzymeView.add(entry);
            });

            model.addAttribute("filtersApplied", filters);
            model.addAttribute("searchKey", searchKey);
            model.addAttribute("searchModel", searchModel);
            model.addAttribute(SEARCH_VIDEO, SEARCH_VIDEO);
            model.addAttribute("ebiResult", ebiSearchResult);
            model.addAttribute("enzymeView", enzymeView);
            model.addAttribute("enzymeFacet", ebiSearchResult.getFacets());
            view = ENZYME_CENTRIC_PAGE;
            //view = ENZYME_CENTRIC_PAGE_V;
        }

        return view;
    }

    private EBISearchResult getEbiSearchResult(String query, int page) {
        ///ModelService modelService = new ModelService(new RestTemplate());

        return modelService.getModelSearchResult(query, page);
    }

    private EBISearchResult filterEbiSearchResult(String query, int page, List<String> filters) {
        // ModelService modelService = new ModelService(new RestTemplate());
        String facets = filters.stream().collect(Collectors.joining(","));
        return modelService.filterSearchResult(query, page, facets);
    }

//    public static void main(String[] args) {
//        String query = "kinase";
//        //query = "sildenafil";
//        query="mtor";
//        ModelService service = new ModelService(new RestTemplate());
//
//        EBISearchResult result = service.getModelSearchResult(query, 0);
//        for (EnzymeView entry : result.getEntries()) {
//            //System.out.println(""+ entry.getFields().getProteinName().size());
//            //entry.getFields().getProteinName().stream().forEach(name -> System.out.println("protein "+ name));
//            System.out.println("ENTRRY " + entry.getEc() + " : " + entry.getEnzymeName() + " "+ entry.getEnzymeFamily() + " "+ entry.getNumEnzymeHits() + " "+ entry.getSpecies() );
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
    public String showEnzymesView(@RequestParam(required = false, value = "s") final String searchKey, Model model) {
        if (searchKey != null) {
            //searchKey = "mTOR";
            //searchKey = "cathepsin";

            int startPage = 0;
            int pageSize = 10;
            int associatedProteinLimit = 5;
            // ModelService modelService = new ModelService(new RestTemplate());
            EBISearchResult ebiSearchResult = modelService.getModelSearchResult(searchKey, startPage);
            long hitCount = ebiSearchResult.getHitCount();
            Pageable pageable = new PageRequest(startPage, pageSize);
            Page<Entry> page = new PageImpl<>(ebiSearchResult.getEntries(), pageable, hitCount);

            List<Entry> entries = page.getContent();
            List<Entry> enzymeView = new LinkedList<>();
            entries.stream().map((entry) -> {
                final List<Protein> proteins = powerService.queryForUniqueProteins(entry.getEc(), searchKey, associatedProteinLimit);
                entry.setProtein(proteins);
                entry.setNumEnzymeHits(proteins.size());
                return entry;
            }).forEach((entry) -> {
                enzymeView.add(entry);
            });
            model.addAttribute("searchKey", searchKey);
            model.addAttribute("ebiResult", ebiSearchResult);
            model.addAttribute("enzymeView", enzymeView);
            model.addAttribute("enzymeFacet", ebiSearchResult.getFacets());
            model.addAttribute(BROWSE_VIDEO, BROWSE_VIDEO);

            return ENZYME_CENTRIC_PAGE_V;
        }
        return "error";
    }
}
