package uk.ac.ebi.ep.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
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
import static uk.ac.ebi.ep.controller.AbstractController.SEARCH_VIDEO;
import uk.ac.ebi.ep.data.search.model.SearchModel;
import uk.ac.ebi.ep.ebeye.model.proteinGroup.ProteinGroupEntry;
import uk.ac.ebi.ep.ebeye.model.proteinGroup.ProteinGroupSearchResult;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Controller
public class ProteinCentricController extends AbstractController {

    private static final Logger logger = Logger.getLogger(ProteinCentricController.class);

    private static final String SEARCH = "/search";

    private static final String PROTEIN_CENTRIC_PAGE = "proteins";
    private static final int DEFAULT_EBI_SEARCH_FACET_COUNT = 1_000;

    private static final int PAGE_SIZE = 10;

    private ProteinGroupSearchResult getProteinCentricResult(String ec, String searchTerm, int facetCount, List<String> filters, int startPage, int pageSize) {

        String facets = filters.stream().collect(Collectors.joining(","));
        ProteinGroupSearchResult result = proteinGroupService.findProteinCentricResultBySearchTermAndEC(ec, searchTerm, facetCount, facets, startPage, pageSize);

        if (result.getHitCount() == 0) {
            result = proteinGroupService.findProteinCentricResultByEC(ec, facetCount, facets, startPage, pageSize);

        }
        return result;
    }

    private String constructModel(ProteinGroupSearchResult ebiSearchResult, List<ProteinGroupEntry> proteinView, Page page, List<String> filters, String ec, String searchTerm, Model model, SearchModel searchModel) {
        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, page.getTotalPages());

        model.addAttribute("page", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("proteinView", proteinView);
        model.addAttribute("filtersApplied", filters);

//        model.addAttribute("searchKey", searchKey);
//        model.addAttribute("keywordType", keywordType);
//        model.addAttribute("searchId", searchId);
//        
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("ec", ec);

        model.addAttribute("searchModel", searchModel);
        model.addAttribute(SEARCH_VIDEO, SEARCH_VIDEO);
        model.addAttribute("ebiResult", ebiSearchResult);
        model.addAttribute("enzymeFacet", ebiSearchResult.getFacets());
        return PROTEIN_CENTRIC_PAGE;

    }

    private String findProteinResult(String ec, String searchTerm, int facetCount, List<String> filters, int startPage, int pageSize, Model model, SearchModel searchModel) {
        String view = "error";
        ProteinGroupSearchResult ebiSearchResult = getProteinCentricResult(ec, searchTerm, facetCount, filters, startPage * pageSize, pageSize);
        if (ebiSearchResult != null) {
            long hitCount = ebiSearchResult.getHitCount();
            Pageable pageable = new PageRequest(startPage, pageSize);
            Page<ProteinGroupEntry> page = new PageImpl<>(ebiSearchResult.getEntries(), pageable, hitCount);

            List<ProteinGroupEntry> proteinView = page.getContent();

            return constructModel(ebiSearchResult, proteinView, page, filters, ec, searchTerm, model, searchModel);

        }
        return view;
    }

    @RequestMapping(value = SEARCH, method = RequestMethod.GET)
    public String getSearchResults(@RequestParam(required = false, value = "ec") String ec, @RequestParam(required = false, value = "searchKey") String searchKey,
            @RequestParam(required = false, value = "filterFacet") List<String> filters,
            @RequestParam(required = false, value = "servicePage") Integer servicePage,
            @RequestParam(required = false, value = "keywordType") String keywordType,
            @RequestParam(required = false, value = "searchId") String searchId,
            SearchModel searchModel, BindingResult result,
            Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        return postSearchResult(ec, searchKey, filters, servicePage, keywordType, searchId, searchModel, model, request);
    }

    @RequestMapping(value = SEARCH, method = RequestMethod.POST)
    public String postSearchResult(@RequestParam(required = true, value = "ec") String ec, @RequestParam(required = false, value = "searchKey") String searchKey,
            @RequestParam(required = false, value = "filterFacet") List<String> filters,
            @RequestParam(required = false, value = "servicePage") Integer servicePage,
            @RequestParam(required = false, value = "keywordType") String keywordType,
            @RequestParam(required = false, value = "searchId") String searchId,
            SearchModel searchModel, Model model, HttpServletRequest request) {
        String view = "error";
        int startPage = 0;
        if (servicePage != null) {
            if (servicePage < 0) {
                servicePage = 1;
            }
            startPage = servicePage - 1;//EBI search paging index starts at 0
        }

        int pageSize = PAGE_SIZE;
        int facetCount = DEFAULT_EBI_SEARCH_FACET_COUNT;
        //int associatedProteinLimit = 7;// ASSOCIATED_PROTEIN_LIMIT;
        if (filters == null) {
            filters = new ArrayList<>();
        }
        if (searchModel.getSearchparams().getText() != null) {
            searchKey = searchModel.getSearchparams().getText().trim().toLowerCase();

        }

        String searchTerm = Jsoup.clean(searchKey, Whitelist.basic());
        if (filters.contains("")) {
            filters.remove("");

        }

        view = findProteinResult(ec, searchTerm, facetCount, filters, startPage, pageSize, model, searchModel);

        return view;
    }

}
