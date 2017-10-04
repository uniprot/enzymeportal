package uk.ac.ebi.ep.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import static uk.ac.ebi.ep.controller.AbstractController.SEARCH_VIDEO;
import uk.ac.ebi.ep.data.search.model.SearchModel;
import uk.ac.ebi.ep.ebeye.model.proteinGroup.ProteinGroupEntry;
import uk.ac.ebi.ep.ebeye.model.proteinGroup.ProteinGroupSearchResult;
import uk.ac.ebi.ep.web.utils.KeywordType;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Controller
public class ProteinCentricController extends AbstractController {

    private static final Logger logger = Logger.getLogger(ProteinCentricController.class);

    private static final String SEARCH = "/search";

    private static final String PROTEIN_CENTRIC_PAGE = "search";
    private static final int DEFAULT_EBI_SEARCH_FACET_COUNT = 1_0;

    private static final int PAGE_SIZE = 5;

    private ProteinGroupSearchResult getProteinCentricSearchTermResult(String ec, String searchTerm, int facetCount, List<String> filters, int startPage, int pageSize) {

        String facets = filters.stream().collect(Collectors.joining(","));
        ProteinGroupSearchResult result = proteinGroupService.findProteinCentricResultBySearchTermAndEC(ec, searchTerm, facetCount, facets, startPage, pageSize);

        if (result.getHitCount() == 0) {
            result = proteinGroupService.findProteinCentricResultByEC(ec, facetCount, facets, startPage, pageSize);

        }
        return result;
    }

    private ProteinGroupSearchResult getProteinCentricEcResult(String ec, int facetCount, List<String> filters, int startPage, int pageSize) {

        String facets = filters.stream().collect(Collectors.joining(","));
        ProteinGroupSearchResult result = proteinGroupService.findProteinCentricResultByEC(ec, facetCount, facets, startPage, pageSize);

        return result;
    }

    private ProteinGroupSearchResult getProteinCentricDiseaseResult(String ec, String omimId, int facetCount, List<String> filters, int startPage, int pageSize) {

        String facets = filters.stream().collect(Collectors.joining(","));
        ProteinGroupSearchResult result = proteinGroupService.findProteinCentricResultByOmimAndEC(ec, omimId, facetCount, facets, startPage, pageSize);

        if (result.getHitCount() == 0) {
            result = proteinGroupService.findProteinCentricResultByEC(ec, facetCount, facets, startPage, pageSize);

        }
        return result;
    }

    private ProteinGroupSearchResult getProteinCentricPathwaysResult(String ec, String pathwayId, int facetCount, List<String> filters, int startPage, int pageSize) {

        String facets = filters.stream().collect(Collectors.joining(","));
        ProteinGroupSearchResult result = proteinGroupService.findProteinCentricResultByPathwayAndEC(ec, pathwayId, facetCount, facets, startPage, pageSize);

        if (result.getHitCount() == 0) {
            result = proteinGroupService.findProteinCentricResultByEC(ec, facetCount, facets, startPage, pageSize);

        }
        return result;
    }

    private ProteinGroupSearchResult getProteinCentricTaxonomyResult(String ec, String taxId, int facetCount, List<String> filters, int startPage, int pageSize) {

        String facets = filters.stream().collect(Collectors.joining(","));
        ProteinGroupSearchResult result = proteinGroupService.findProteinCentricResultByTaxIdAndEC(ec, taxId, facetCount, facets, startPage, pageSize);

        if (result.getHitCount() == 0) {
            result = proteinGroupService.findProteinCentricResultByEC(ec, facetCount, facets, startPage, pageSize);

        }
        return result;
    }

    private String constructModel(ProteinGroupSearchResult ebiSearchResult, List<ProteinGroupEntry> proteinView, Page page, List<String> filters, String ec, String searchKey, String searchId, String keywordType, Model model, SearchModel searchModel) {
        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, page.getTotalPages());

        model.addAttribute("page", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("proteinView", proteinView);
        model.addAttribute("filtersApplied", filters);

        model.addAttribute("searchKey", searchKey);
        model.addAttribute("searchId", searchId);
        model.addAttribute("keywordType", keywordType);

//        
        //model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("ec", ec);

        searchModel = searchform();

        model.addAttribute("searchModel", searchModel);
        model.addAttribute(SEARCH_VIDEO, SEARCH_VIDEO);
        model.addAttribute("ebiResult", ebiSearchResult);
        model.addAttribute("proteinFacet", ebiSearchResult.getFacets());
        return PROTEIN_CENTRIC_PAGE;

    }

    private String computeSearchResult(ProteinGroupSearchResult ebiSearchResult, String ec, String searchKey, String searchId, String keywordType, int facetCount, List<String> filters, int startPage, int pageSize, Model model, SearchModel searchModel) {
        long hitCount = ebiSearchResult.getHitCount();
        Pageable pageable = new PageRequest(startPage, pageSize);
        Page<ProteinGroupEntry> page = new PageImpl<>(ebiSearchResult.getEntries(), pageable, hitCount);

        List<ProteinGroupEntry> proteinView = page.getContent();

        return constructModel(ebiSearchResult, proteinView, page, filters, ec, searchKey, searchId, keywordType, model, searchModel);

    }

    private String keywordSearch(String ec, String searchTerm, String keywordType, int facetCount, List<String> filters, int startPage, int pageSize, Model model, SearchModel searchModel) {
        String view = "error";
        ProteinGroupSearchResult ebiSearchResult = getProteinCentricSearchTermResult(ec, searchTerm, facetCount, filters, startPage * pageSize, pageSize);
        if (ebiSearchResult != null) {
            long hitCount = ebiSearchResult.getHitCount();
            Pageable pageable = new PageRequest(startPage, pageSize);
            Page<ProteinGroupEntry> page = new PageImpl<>(ebiSearchResult.getEntries(), pageable, hitCount);

            List<ProteinGroupEntry> proteinView = page.getContent();

            return constructModel(ebiSearchResult, proteinView, page, filters, ec, searchTerm, searchTerm, keywordType, model, searchModel);

        }
        return view;
    }

    private String ecSearch(String ec, String keywordType, int facetCount, List<String> filters, int startPage, int pageSize, Model model, SearchModel searchModel) {

        String view = "error";
        ProteinGroupSearchResult ebiSearchResult = getProteinCentricEcResult(ec, facetCount, filters, startPage * pageSize, pageSize);
        if (ebiSearchResult != null) {
            long hitCount = ebiSearchResult.getHitCount();
            Pageable pageable = new PageRequest(startPage, pageSize);
            Page<ProteinGroupEntry> page = new PageImpl<>(ebiSearchResult.getEntries(), pageable, hitCount);

            List<ProteinGroupEntry> proteinView = page.getContent();

            return constructModel(ebiSearchResult, proteinView, page, filters, ec, ec, ec, keywordType, model, searchModel);

        }
        return view;
    }

    private String diseaseSearch(String ec, String searchKey, String searchId, String keywordType, int facetCount, List<String> filters, int startPage, int pageSize, Model model, SearchModel searchModel) {
        String view = "error";
        ProteinGroupSearchResult ebiSearchResult = getProteinCentricDiseaseResult(ec, searchId, facetCount, filters, startPage * pageSize, pageSize);
        if (ebiSearchResult != null) {
//            long hitCount = ebiSearchResult.getHitCount();
//            Pageable pageable = new PageRequest(startPage, pageSize);
//            Page<ProteinGroupEntry> page = new PageImpl<>(ebiSearchResult.getEntries(), pageable, hitCount);
//
//            List<ProteinGroupEntry> proteinView = page.getContent();

            //return constructModel(ebiSearchResult, proteinView, page, filters, ec, searchKey, searchId, keywordType, model, searchModel);
            return computeSearchResult(ebiSearchResult, ec, searchKey, searchId, keywordType, facetCount, filters, startPage, pageSize, model, searchModel);
        }
        return view;
    }

    private String pathwaysSearch(String ec, String searchKey, String searchId, String keywordType, int facetCount, List<String> filters, int startPage, int pageSize, Model model, SearchModel searchModel) {
        String view = "error";
        ProteinGroupSearchResult ebiSearchResult = getProteinCentricPathwaysResult(ec, searchId, facetCount, filters, startPage * pageSize, pageSize);
        if (ebiSearchResult != null) {

            return computeSearchResult(ebiSearchResult, ec, searchKey, searchId, keywordType, facetCount, filters, startPage, pageSize, model, searchModel);
        }
        return view;
    }

    private String taxonomySearch(String ec, String searchKey, String searchId, String keywordType, int facetCount, List<String> filters, int startPage, int pageSize, Model model, SearchModel searchModel) {
        String view = "error";
        ProteinGroupSearchResult ebiSearchResult = getProteinCentricTaxonomyResult(ec, searchId, facetCount, filters, startPage * pageSize, pageSize);
        if (ebiSearchResult != null) {

            return computeSearchResult(ebiSearchResult, ec, searchKey, searchId, keywordType, facetCount, filters, startPage, pageSize, model, searchModel);
        }
        return view;
    }

    @RequestMapping(value = SEARCH)
    public SearchModel getSearch(Model model) {
        SearchModel searchModelForm = searchform();
        model.addAttribute("searchModel", searchModelForm);
        model.addAttribute(SEARCH_VIDEO, SEARCH_VIDEO);
        return searchModelForm;
    }

//    @RequestMapping(value = SEARCH, method = RequestMethod.GET)
//    public String getSearchResults(@RequestParam(required = false, value = "ec") String ec, 
//            @RequestParam(required = false, value = "searchKey") String searchKey,
//            @RequestParam(required = false, value = "filterFacet") List<String> filters,
//            @RequestParam(required = false, value = "servicePage") Integer servicePage,
//            @RequestParam(required = false, value = "keywordType") String keywordType,
//            @RequestParam(required = false, value = "searchId") String searchId,
//            SearchModel searchModel, BindingResult result,
//            Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
//        
//        return postSearchResult(ec, searchKey, filters, servicePage, keywordType, searchId, searchModel, model, request);
//    }
    @RequestMapping(value = SEARCH, method = RequestMethod.POST)
    public String postSearchResult(@RequestParam(required = true, value = "ec") String ec,
            @RequestParam(required = false, value = "searchKey") String searchKey,
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
        if (filters.contains("")) {
            filters.remove("");

        }
//        if (searchModel.getSearchparams().getText() != null) {
//            searchKey = searchModel.getSearchparams().getText().trim().toLowerCase();
//
//        }

        KeywordType type = KeywordType.valueOf(keywordType);
        switch (type) {
            case KEYWORD:

                String searchTerm = Jsoup.clean(searchKey, Whitelist.basic());

                boolean isEc = searchUtil.validateEc(searchTerm);
                if (isEc) {
                    view = ecSearch(ec, keywordType, facetCount, filters, startPage, pageSize, model, searchModel);
                } else {

                    if (searchTerm.contains("/")) {
                        searchTerm = searchTerm.replaceAll("/", " ");

                    }
                    view = keywordSearch(ec, searchTerm, keywordType, facetCount, filters, startPage, pageSize, model, searchModel);

                }
                break;
            case DISEASE:
                view = diseaseSearch(ec, searchKey, searchId, keywordType, facetCount, filters, startPage, pageSize, model, searchModel);

                break;
            case TAXONOMY:
                view = taxonomySearch(ec, searchKey, searchId, keywordType, facetCount, filters, startPage, pageSize, model, searchModel);

                break;
            case PATHWAYS:
                view = pathwaysSearch(ec, searchKey, searchId, keywordType, facetCount, filters, startPage, pageSize, model, searchModel);

                break;
            case EC:
                view = ecSearch(ec, keywordType, facetCount, filters, startPage, pageSize, model, searchModel);
                break;
            default:
                return view;

        }
        return view;

//        String searchTerm = Jsoup.clean(searchKey, Whitelist.basic());
//
//        view = findProteinResult(ec, searchTerm, facetCount, filters, startPage, pageSize, model, searchModel);
//
//        return view;
    }

    @RequestMapping(value = "/ep", method = RequestMethod.GET)
    public String getSearchResults(Model model) {
        String ec = "2.7.7.7";
        String searchTerm = "human";
        int facetCount = 10;
        List<String> filters = new ArrayList<>();

        filters.add("cofactor:18420");
        //filters.add("OMIM:612740");
        int startPage = 0;
        int pageSize = 20;
        String keywordType = "KEYWORD";
        SearchModel searchModel = searchform();
        return keywordSearch(ec, searchTerm, keywordType, facetCount, filters, startPage, pageSize, model, searchModel);

    }
}
