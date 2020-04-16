package uk.ac.ebi.ep.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeEntry;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeSearchResult;
import uk.ac.ebi.ep.indexservice.model.protein.ProteinGroupEntry;
import uk.ac.ebi.ep.indexservice.model.protein.ProteinGroupSearchResult;
import uk.ac.ebi.ep.web.logging.SeachCategory;
import uk.ac.ebi.ep.web.logging.SeachType;
import uk.ac.ebi.ep.web.logging.SearchQueryLog;

/**
 *
 * @author joseph
 */
public class CommonControllerMethods {

    protected static final String ERROR_PAGE = "error";
    protected static final String ENZYME_CENTRIC_PAGE = "enzymes";
    protected static final String ENZYME_PAGE = "enzymePage";
    protected static final String SEARCH = "/search";

    protected static final String PROTEIN_CENTRIC_PAGE = "search";

    private static final String REDIRECT = "redirect:";

    protected void logSearchQuery(SeachType seachType, SeachCategory seachCategory, String term) {
        SearchQueryLog.logSearchQuery(seachType, seachCategory, term);
    }

    public static String redirect(String path) {
        StringBuilder builder = new StringBuilder();
        builder.append(REDIRECT);
        builder.append(path);
        return builder.toString();
    }

    protected int refineStartPage(Integer servicePage) {
        int startPage = 0;
        if (servicePage != null) {
            if (servicePage < 0) {
                servicePage = 1;
            }
            startPage = servicePage - 1;//EBI search paging index starts at 0
        }
        return startPage;
    }

    protected List<String> refineFilters(List<String> filters) {
        if (filters == null) {
            filters = new ArrayList<>();
        }
        if (filters.contains("")) {
            filters.remove("");

        }
        return filters;
    }

    protected String getSearchKey(String searchText) {
        if (searchText != null) {
            return searchText.trim().toLowerCase();

        }
        return searchText;
    }

    protected String getSearchTerm(String searchKey) {

        if (!StringUtils.isEmpty(searchKey)) {
            return Jsoup.clean(searchKey, Whitelist.basic());
        }
        return searchKey;
    }

    /**
     *
     * @param request
     * CHEBI:18420&searchparams.type=KEYWORD&searchparams.previoustext=Mg(2+)&searchparams.start=0&searchparams.text=Mg(2+)&keywordType=COFACTORS&searchId=CHEBI:18420
     * @param searchkey
     * @return Mg(2+)
     */
    protected String parseRequestQueryString(HttpServletRequest request, String searchkey) {
        String queryString = request.getQueryString();

        if (queryString == null) {
            return searchkey;
        }
        String [] data = queryString.split("&");
        List<String> list = Arrays.asList(data);
        String requestString = list
                .stream()
                .filter(k -> k.startsWith("searchparams.text"))
                .map(txt -> txt.split("=")[1])
                .findFirst().orElse(searchkey);
        if (requestString.contains("%")) {
            return searchkey;
        }
        return requestString;
    }

    protected String constructModel(EnzymeSearchResult ebiSearchResult, Set<EnzymeEntry> enzymeView, Page page, List<String> filters, String searchId, String searchKey, String keywordType, Model model) {
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

        model.addAttribute("ebiResult", ebiSearchResult);
        model.addAttribute("enzymeFacet", ebiSearchResult.getFacets());
        return ENZYME_CENTRIC_PAGE;

    }

    protected String constructModel(ProteinGroupSearchResult ebiSearchResult, List<ProteinGroupEntry> proteinView, Page page, List<String> filters, String ec, String searchKey, String searchId, String keywordType, Model model) {
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

        model.addAttribute("searchTerm", searchKey);
        model.addAttribute("ec", ec);

        model.addAttribute("ebiResult", ebiSearchResult);
        model.addAttribute("proteinFacet", ebiSearchResult.getFacets());
        return PROTEIN_CENTRIC_PAGE;

    }

    protected void addProteinEntryToEnzymeView(ProteinGroupSearchResult result, EnzymeEntry entry, Set<EnzymeEntry> enzymeView) {
        int proteinHits = result.getHitCount();

        if (proteinHits > 0) {
            entry.setProteinGroupEntry(result.getEntries());
            entry.setNumProteins(proteinHits);
            entry.setNumEnzymeHits(proteinHits);

            enzymeView.add(entry);
        }

    }

    protected Page<EnzymeEntry> buildEnzymeEntryPage(List<EnzymeEntry> entries, int startPage, int pageSize, long hitCount) {
        PageRequest pageable = PageRequest.of(startPage, pageSize);
        return new PageImpl<>(entries, pageable, hitCount);
    }

    protected Page<ProteinGroupEntry> buildProteinGroupEntryPage(List<ProteinGroupEntry> entries, int startPage, int pageSize, long hitCount) {
        PageRequest pageable = PageRequest.of(startPage, pageSize);
        return new PageImpl<>(entries, pageable, hitCount);
    }

}
