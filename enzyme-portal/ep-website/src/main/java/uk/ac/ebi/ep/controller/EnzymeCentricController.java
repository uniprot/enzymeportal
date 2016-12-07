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
import uk.ac.ebi.ep.data.search.model.SearchModel;
import uk.ac.ebi.ep.ebeye.enzyme.model.Entry;
import uk.ac.ebi.ep.ebeye.model.EBISearchResult;
import uk.ac.ebi.ep.ebeye.protein.model.Protein;
import uk.ac.ebi.ep.web.utils.KeywordType;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Controller
public class EnzymeCentricController extends AbstractController {

    private static final Logger logger = Logger.getLogger(EnzymeCentricController.class);

    private static final String SEARCH = "/enzymes";
    private static final String FILTER = "/search/filter";
    private static final String ENZYME_CENTRIC_PAGE = "enzymes";
    private static final int DEFAULT_EBI_SEARCH_FACET_COUNT = 1_000;
    private static final int ASSOCIATED_PROTEIN_LIMIT = 8_00;
    private static final int PAGE_SIZE = 10;

    @RequestMapping(value = SEARCH, method = RequestMethod.GET)
    public String getSearchResults(@RequestParam(required = false, value = "searchKey") String searchKey,
            @RequestParam(required = false, value = "filterFacet") List<String> filters,
            @RequestParam(required = false, value = "servicePage") Integer servicePage,
            @RequestParam(required = false, value = "keywordType") String keywordType,
            @RequestParam(required = false, value = "searchId") String searchId,
            SearchModel searchModel, BindingResult result,
            Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        return postSearchResult(searchKey, filters, servicePage, keywordType, searchId, searchModel, model, request);
    }

    @RequestMapping(value = SEARCH, method = RequestMethod.POST)
    public String postSearchResult(@RequestParam(required = false, value = "searchKey") String searchKey,
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
        int associatedProteinLimit = ASSOCIATED_PROTEIN_LIMIT;
        if (filters == null) {
            filters = new ArrayList<>();
        }
        if (searchModel.getSearchparams().getText() != null) {
            searchKey = searchModel.getSearchparams().getText().trim().toLowerCase();

        }

        final String searchTerm = Jsoup.clean(searchKey, Whitelist.basic());
        if (filters.contains("")) {
            filters.remove("");

        }
        KeywordType type = KeywordType.valueOf(keywordType);
        switch (type) {
            case KEYWORD:
                System.out.println("UTIL "+ searchUtil);
                boolean isEc = searchUtil.validateEc(searchTerm);
                if (isEc) {
                    view = findEnzymesByEC(searchTerm, startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model, searchModel, view);
                } else {
                    view = findEnzymesBySearchTerm(searchTerm, startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model, searchModel, view);
                }
                break;
            case DISEASE:
                view = findEnzymesByOmimId(searchId, startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model, searchModel, view);
                break;
            case TAXONOMY:
                view = findEnzymesByTaxId(searchId, startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model, searchModel, view);
                break;
            case PATHWAYS:
                view = findEnzymesByPathwayId(searchId, startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model, searchModel, view);
                break;
            case EC:
                view = findEnzymesByEC(searchId, startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model, searchModel, view);
                break;
            default:
                return view;

        }
        return view;
    }

    private EBISearchResult getEbiSearchResult(String query, int startPage, int pageSize, int facetCount, List<String> filters) {
        String facets = filters.stream().collect(Collectors.joining(","));
        return enzymeCentricService.getSearchResult(query, startPage, pageSize, facets, facetCount);
    }

    private EBISearchResult getEbiSearchResultByOmimId(String omimId, int startPage, int pageSize, int facetCount, List<String> filters) {
        String facets = filters.stream().collect(Collectors.joining(","));
        return enzymeCentricService.findEbiSearchResultsByOmimId(omimId, startPage, pageSize, facets, facetCount);

    }

    private EBISearchResult getEbiSearchResultByTaxId(String taxId, int startPage, int pageSize, int facetCount, List<String> filters) {
        String facets = filters.stream().collect(Collectors.joining(","));

        return enzymeCentricService.findEbiSearchResultsByTaxId(taxId, startPage, pageSize, facets, facetCount);

    }

    private EBISearchResult getEbiSearchResultByEC(String ec, int startPage, int pageSize, int facetCount, List<String> filters) {
        String facets = filters.stream().collect(Collectors.joining(","));
        return enzymeCentricService.findEbiSearchResultsByEC(ec, startPage, pageSize, facets, facetCount);

    }

    private EBISearchResult getEbiSearchResultByPathwayId(String pathwayId, int startPage, int pageSize, int facetCount, List<String> filters) {
        String facets = filters.stream().collect(Collectors.joining(","));
        return enzymeCentricService.findEbiSearchResultsByPathwayId(pathwayId, startPage, pageSize, facets, facetCount);

    }

    private String findEnzymesBySearchTerm(String searchTerm, int startPage, int pageSize, int facetCount, List<String> filters, int associatedProteinLimit, String searchKey, String keywordType, Model model, SearchModel searchModel, String view) {
        EBISearchResult ebiSearchResult = getEbiSearchResult(searchTerm, startPage * pageSize, pageSize, facetCount, filters);
        int LOWEST_BEST_MATCHED_RESULT_SIZE = 4;
        if (ebiSearchResult != null) {
            long hitCount = ebiSearchResult.getHitCount();
            Pageable pageable = new PageRequest(startPage, pageSize);
            Page<Entry> page = new PageImpl<>(ebiSearchResult.getEntries(), pageable, hitCount);

            List<Entry> entries = page.getContent();
            List<Entry> enzymeView = new LinkedList<>();
            entries.stream().forEach(entry -> {
                List<Protein> proteins = ebeyeRestService.queryForUniqueProteins(entry.getEc(), searchTerm, associatedProteinLimit)
                        .stream()
                        .sorted()
                        .collect(Collectors.toList());
                if (proteins.isEmpty()) {
                    proteins = ebeyeRestService.queryForUniqueProteins(entry.getEc(), associatedProteinLimit)
                            .stream()
                            .limit(LOWEST_BEST_MATCHED_RESULT_SIZE)
                            .sorted()
                            .collect(Collectors.toList());
                }

                addProteinEntryToEnzymeView(proteins, entry, enzymeView);

            });

            if (enzymeView.isEmpty() && !ebiSearchResult.getEntries().isEmpty()) {
                logger.info(ebiSearchResult.getEntries().size()
                        + " results are found in Enzyme-centric index for query " + searchTerm + " But none in Protein-centric index");
                ebiSearchResult = new EBISearchResult();
                ebiSearchResult.setFacets(new ArrayList<>());
                ebiSearchResult.setHitCount(0);
                ebiSearchResult.setEntries(new ArrayList<>());
                page = new PageImpl<>(ebiSearchResult.getEntries(), pageable, 0);

            }
            return constructModel(ebiSearchResult, enzymeView, page, filters, searchTerm, searchKey, keywordType, model, searchModel);

        }
        return view;
    }

    private String findEnzymesByOmimId(String omimId, int startPage, int pageSize, int facetCount, List<String> filters, int associatedProteinLimit, String searchKey, String keywordType, Model model, SearchModel searchModel, String view) {

        EBISearchResult ebiSearchResult = getEbiSearchResultByOmimId(omimId, startPage * pageSize, pageSize, facetCount, filters);
        if (ebiSearchResult != null) {
            long hitCount = ebiSearchResult.getHitCount();
            Pageable pageable = new PageRequest(startPage, pageSize);
            Page<Entry> page = new PageImpl<>(ebiSearchResult.getEntries(), pageable, hitCount);

            List<Entry> entries = page.getContent();
            List<Entry> enzymeView = new LinkedList<>();
            entries.stream()
                    .forEach(entry -> {

                        List<Protein> proteins = ebeyeRestService.findUniqueProteinsByOmimIdAndEc(omimId, entry.getEc(), associatedProteinLimit)
                        .stream()
                        .sorted()
                        .collect(Collectors.toList());
                        addProteinEntryToEnzymeView(proteins, entry, enzymeView);

                    });

            return constructModel(ebiSearchResult, enzymeView, page, filters, omimId, searchKey, keywordType, model, searchModel);

        }
        return view;
    }

    private String findEnzymesByTaxId(String taxId, int startPage, int pageSize, int facetCount, List<String> filters, int associatedProteinLimit, String searchKey, String keywordType, Model model, SearchModel searchModel, String view) {

        EBISearchResult ebiSearchResult = getEbiSearchResultByTaxId(taxId, startPage * pageSize, pageSize, facetCount, filters);
        if (ebiSearchResult != null) {
            long hitCount = ebiSearchResult.getHitCount();
            Pageable pageable = new PageRequest(startPage, pageSize);
            Page<Entry> page = new PageImpl<>(ebiSearchResult.getEntries(), pageable, hitCount);

            List<Entry> entries = page.getContent();
            List<Entry> enzymeView = new LinkedList<>();
            entries.stream()
                    .forEach(entry -> {
                        List<Protein> proteins = ebeyeRestService.findUniqueProteinsByTaxIdAndEc(taxId, entry.getEc(), associatedProteinLimit)
                        .stream()
                        .sorted()
                        .collect(Collectors.toList());

                        addProteinEntryToEnzymeView(proteins, entry, enzymeView);

                    });

            return constructModel(ebiSearchResult, enzymeView, page, filters, taxId, searchKey, keywordType, model, searchModel);

        }
        return view;
    }

    private String findEnzymesByEC(String ec, int startPage, int pageSize, int facetCount, List<String> filters, int associatedProteinLimit, String searchKey, String keywordType, Model model, SearchModel searchModel, String view) {

        EBISearchResult ebiSearchResult = getEbiSearchResultByEC(ec, startPage * pageSize, pageSize, facetCount, filters);
        if (ebiSearchResult != null) {
            long hitCount = ebiSearchResult.getHitCount();
            Pageable pageable = new PageRequest(startPage, pageSize);
            Page<Entry> page = new PageImpl<>(ebiSearchResult.getEntries(), pageable, hitCount);

            List<Entry> entries = page.getContent();
            List<Entry> enzymeView = new LinkedList<>();
            entries.stream()
                    .forEach(entry -> {

                        List<Protein> proteins = ebeyeRestService.queryForUniqueProteins(entry.getEc(), associatedProteinLimit)
                        .stream()
                        .sorted()
                        .collect(Collectors.toList());

                        addProteinEntryToEnzymeView(proteins, entry, enzymeView);

                    });

            return constructModel(ebiSearchResult, enzymeView, page, filters, ec, searchKey, keywordType, model, searchModel);

        }
        return view;
    }

    private String findEnzymesByPathwayId(String pathwayId, int startPage, int pageSize, int facetCount, List<String> filters, int associatedProteinLimit, String searchKey, String keywordType, Model model, SearchModel searchModel, String view) {

        EBISearchResult ebiSearchResult = getEbiSearchResultByPathwayId(pathwayId, startPage * pageSize, pageSize, facetCount, filters);
        if (ebiSearchResult != null) {
            long hitCount = ebiSearchResult.getHitCount();
            Pageable pageable = new PageRequest(startPage, pageSize);
            Page<Entry> page = new PageImpl<>(ebiSearchResult.getEntries(), pageable, hitCount);

            List<Entry> entries = page.getContent();
            List<Entry> enzymeView = new LinkedList<>();
            entries.stream().forEach(entry -> {

                List<Protein> proteins = ebeyeRestService.findUniqueProteinsByPathwayIdAndEc(pathwayId, entry.getEc(), associatedProteinLimit)
                        .stream()
                        .sorted()
                        .collect(Collectors.toList());

                addProteinEntryToEnzymeView(proteins, entry, enzymeView);

            });

            return constructModel(ebiSearchResult, enzymeView, page, filters, pathwayId, searchKey, keywordType, model, searchModel);

        }
        return view;
    }

    private String constructModel(EBISearchResult ebiSearchResult, List<Entry> enzymeView, Page page, List<String> filters, String searchId, String searchKey, String keywordType, Model model, SearchModel searchModel) {
        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, page.getTotalPages());

        model.addAttribute("page", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("enzymeView", enzymeView);
        model.addAttribute("filtersApplied", filters);

        model.addAttribute("searchKey", searchKey);
        model.addAttribute("keywordType", keywordType);
        model.addAttribute("searchId", searchId);
        model.addAttribute("searchModel", searchModel);
        model.addAttribute(SEARCH_VIDEO, SEARCH_VIDEO);
        model.addAttribute("ebiResult", ebiSearchResult);
        model.addAttribute("enzymeFacet", ebiSearchResult.getFacets());
        return ENZYME_CENTRIC_PAGE;

    }

    private void addProteinEntryToEnzymeView(List<Protein> proteins, Entry entry, List<Entry> enzymeView) {

        int proteinHits = proteins.size();

        if (proteinHits > 0) {
            entry.setProteins(proteins);
            entry.setNumProteins(proteinHits);
            //entry.setNumEnzymeHits(hitCount);
            entry.setNumEnzymeHits(proteinHits);
            enzymeView.add(entry);
        }

    }

}
