package uk.ac.ebi.ep.web.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uk.ac.ebi.ep.indexservice.helper.IndexQueryType;
import uk.ac.ebi.ep.indexservice.model.protein.ProteinGroupEntry;
import uk.ac.ebi.ep.indexservice.model.protein.ProteinGroupSearchResult;
import static uk.ac.ebi.ep.web.controller.CommonControllerMethods.ERROR_PAGE;
import uk.ac.ebi.ep.web.service.SearchIndexService;
import uk.ac.ebi.ep.web.utils.KeywordType;
import uk.ac.ebi.ep.web.utils.SearchUtil;

/**
 *
 * @author joseph
 */
@Slf4j
@Controller
public class ProteincentricController extends CommonControllerMethods {

    private static final int DEFAULT_EBI_SEARCH_FACET_COUNT = 1_000;

    private static final int PAGE_SIZE = 10;

    private final SearchIndexService searchIndexService;

    @Autowired
    public ProteincentricController(SearchIndexService searchIndexService) {
        this.searchIndexService = searchIndexService;
    }

    @PostMapping(value = SEARCH)
    public String postSearchResult(@RequestParam(required = true, value = "ec") String ec,
            @RequestParam(required = false, value = "searchKey") String searchKey,
            @RequestParam(required = false, value = "filterFacet") List<String> filterFacet,
            @RequestParam(required = false, value = "servicePage") Integer servicePage,
            @RequestParam(required = false, value = "keywordType") String keywordType,
            @RequestParam(required = false, value = "searchId") String searchId,
            @RequestParam(required = false, value = "searchparams.text") String searchText, Model model, HttpServletRequest request) {

     
        int pageSize = PAGE_SIZE;
        int facetCount = DEFAULT_EBI_SEARCH_FACET_COUNT;

        int startPage = refineStartPage(servicePage);
        List<String> filters = refineFilters(filterFacet);

        if (searchKey == null && searchText != null) {
            searchKey = getSearchKey(searchText);
        }

        KeywordType type = KeywordType.valueOf(keywordType);

        switch (type) {
            case KEYWORD:

                String searchTerm = getSearchTerm(searchKey);

                boolean isEc = SearchUtil.validateEc(searchTerm);
                if (isEc) {
                    return ecSearch(ec, keywordType, facetCount, filters, startPage, pageSize, model);
                } else {

                    if (searchTerm.contains("/")) {
                        searchTerm = searchTerm.replace("/", " ");

                    }
                    return keywordSearch(ec, searchTerm, keywordType, facetCount, filters, startPage, pageSize, model);

                }

            case DISEASE:
                return diseaseSearch(ec, searchKey, searchId, keywordType, facetCount, filters, startPage, pageSize, model);

            case TAXONOMY:
                return taxonomySearch(ec, searchKey, searchId, keywordType, facetCount, filters, startPage, pageSize, model);

            case PATHWAYS:

                return pathwaysSearch(ec, searchKey, searchId, keywordType, facetCount, filters, startPage, pageSize, model);

            case EC:
                return ecSearch(ec, keywordType, facetCount, filters, startPage, pageSize, model);
            case METABOLITES:

                return metaboliteSearch(ec, searchKey, searchId, keywordType, facetCount, filters, startPage, pageSize, model);

            case COFACTORS:

                return cofactorSearch(ec, searchKey, searchId, keywordType, facetCount, filters, startPage, pageSize, model);

            case CHEBI:

                return chebiSearch(ec, searchKey, searchId, keywordType, facetCount, filters, startPage, pageSize, model);

            case RHEA:

                return rheaSearch(ec, searchKey, searchId, keywordType, facetCount, filters, startPage, pageSize, model);

            case FAMILIES:

                return proteinFamilySearch(ec, searchKey, searchId, keywordType, facetCount, filters, startPage, pageSize, model);

            default:
                return ERROR_PAGE;

        }

    }

    private String keywordSearch(String ec, String searchTerm, String keywordType, int facetCount, List<String> filters, int startPage, int pageSize, Model model) {

        String query = String.format("%s AND %s%s", searchTerm, IndexQueryType.EC.getQueryType(), ec);

        return processProteinResult(query, ec, searchTerm, keywordType, facetCount, filters, startPage, pageSize, model);

    }

    private String processProteinResult(String query, String ec, String search, String keywordType, int facetCount, List<String> filters, int startPage, int pageSize, Model model) {

        ProteinGroupSearchResult proteinSearchResult = searchIndexService.findProteinResult(query, startPage, pageSize, facetCount, filters, IndexQueryType.KEYWORD);

        if (proteinSearchResult != null) {
            long hitCount = proteinSearchResult.getHitCount();
            if (hitCount == 0) {

                query = String.format("%s%s", IndexQueryType.EC.getQueryType(), ec);
                proteinSearchResult = searchIndexService.findProteinResult(query, startPage, pageSize, facetCount, filters, IndexQueryType.EC);
            }

            Page<ProteinGroupEntry> page = buildProteinGroupEntryPage(proteinSearchResult.getEntries(), startPage, pageSize, hitCount);

            List<ProteinGroupEntry> proteinView = page.getContent();

            return constructModel(proteinSearchResult, proteinView, page, filters, ec, search, search, keywordType, model);

        }
        return ERROR_PAGE;

    }

    protected String processProteinResult(String ec, String resourceId, IndexQueryType resourceQueryType, String searchKey, String keywordType, int facetCount, List<String> filters, int startPage, int pageSize, Model model) {
        String query = String.format("%s%s AND %s%s", resourceQueryType.getQueryType(), resourceId, IndexQueryType.EC.getQueryType(), ec);

        ProteinGroupSearchResult ebiSearchResult = searchIndexService.findProteinResult(query, startPage, pageSize, facetCount, filters, resourceQueryType);

        if (ebiSearchResult != null) {
            long hitCount = ebiSearchResult.getHitCount();

            if (hitCount == 0) {

                String xrefQuery = String.format("%s%s AND %s%s", resourceQueryType.getQueryType(), resourceId, IndexQueryType.INTENZ.getQueryType(), ec);

                ebiSearchResult = searchIndexService.findProteinResult(xrefQuery, startPage, pageSize, facetCount, filters, resourceQueryType);

            }

            Page<ProteinGroupEntry> page = buildProteinGroupEntryPage(ebiSearchResult.getEntries(), startPage, pageSize, hitCount);

            List<ProteinGroupEntry> proteinView = page.getContent();

            return constructModel(ebiSearchResult, proteinView, page, filters, ec, searchKey, resourceId, keywordType, model);

        }
        return ERROR_PAGE;
    }

    private String ecSearch(String ec, String keywordType, int facetCount, List<String> filters, int startPage, int pageSize, Model model) {

        String query = String.format("%s%s", IndexQueryType.EC.getQueryType(), ec);
        return processProteinResult(query, ec, ec, keywordType, facetCount, filters, startPage, pageSize, model);

    }

    private String diseaseSearch(String ec, String searchKey, String searchId, String keywordType, int facetCount, List<String> filters, int startPage, int pageSize, Model model) {

        return processProteinResult(ec, searchId, IndexQueryType.OMIM, searchKey, keywordType, facetCount, filters, startPage, pageSize, model);

    }

    private String taxonomySearch(String ec, String searchKey, String searchId, String keywordType, int facetCount, List<String> filters, int startPage, int pageSize, Model model) {

        return processProteinResult(ec, searchId, IndexQueryType.TAXONOMY, searchKey, keywordType, facetCount, filters, startPage, pageSize, model);

    }

    private String pathwaysSearch(String ec, String searchKey, String searchId, String keywordType, int facetCount, List<String> filters, int startPage, int pageSize, Model model) {

        return processProteinResult(ec, searchId, IndexQueryType.REACTOME, searchKey, keywordType, facetCount, filters, startPage, pageSize, model);

    }

    private String metaboliteSearch(String ec, String searchKey, String searchId, String keywordType, int facetCount, List<String> filters, int startPage, int pageSize, Model model) {

        return processProteinResult(ec, searchId, IndexQueryType.METABOLIGHTS, searchKey, keywordType, facetCount, filters, startPage, pageSize, model);

    }

    private String cofactorSearch(String ec, String searchKey, String searchId, String keywordType, int facetCount, List<String> filters, int startPage, int pageSize, Model model) {

        return processProteinResult(ec, searchId, IndexQueryType.COFACTOR, searchKey, keywordType, facetCount, filters, startPage, pageSize, model);

    }

    private String chebiSearch(String ec, String searchKey, String searchId, String keywordType, int facetCount, List<String> filters, int startPage, int pageSize, Model model) {
        String chebiId = SearchUtil.escape(searchId);
        return processProteinResult(ec, chebiId, IndexQueryType.CHEBI, searchKey, keywordType, facetCount, filters, startPage, pageSize, model);

    }

    private String rheaSearch(String ec, String searchKey, String searchId, String keywordType, int facetCount, List<String> filters, int startPage, int pageSize, Model model) {

        String rheaId = SearchUtil.escape(searchId);
        return processProteinResult(ec, rheaId, IndexQueryType.RHEA, searchKey, keywordType, facetCount, filters, startPage, pageSize, model);

    }

    private String proteinFamilySearch(String ec, String searchKey, String searchId, String keywordType, int facetCount, List<String> filters, int startPage, int pageSize, Model model) {
        return processProteinResult(ec, searchId, IndexQueryType.PROTEIN_FAMILY_ID, searchKey, keywordType, facetCount, filters, startPage, pageSize, model);

    }

}
