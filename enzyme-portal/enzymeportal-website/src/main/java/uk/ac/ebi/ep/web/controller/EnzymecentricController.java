package uk.ac.ebi.ep.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uk.ac.ebi.ep.brendaservice.dto.BrendaResult;
import uk.ac.ebi.ep.brendaservice.service.BrendaService;
import uk.ac.ebi.ep.indexservice.helper.IndexQueryType;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeEntry;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeSearchResult;
import uk.ac.ebi.ep.indexservice.model.protein.ProteinGroupSearchResult;
import static uk.ac.ebi.ep.web.controller.CommonControllerMethods.ENZYME_PAGE;
import uk.ac.ebi.ep.web.logging.SeachCategory;
import uk.ac.ebi.ep.web.logging.SeachType;
import uk.ac.ebi.ep.web.logging.SearchQueryLog;
import uk.ac.ebi.ep.web.model.EnzymePage;
import uk.ac.ebi.ep.web.service.EnzymePageService;
import uk.ac.ebi.ep.web.service.SearchIndexService;
import uk.ac.ebi.ep.web.utils.KeywordType;
import uk.ac.ebi.ep.web.utils.SearchUtil;
import uk.ac.ebi.reaction.mechanism.model.MechanismResult;

/**
 *
 * @author joseph
 */
@Slf4j
@Controller
public class EnzymecentricController extends CommonControllerMethods {

    private static final String ENZYMES = "/enzymes";

    private static final String ENZYME_PAGE_REQUEST = "/ec/";
    private static final int DEFAULT_EBI_SEARCH_FACET_COUNT = 10;
    private static final int PAGE_SIZE = 10;

    private static final String CHEBI_ID_PREFIX = "chebi:";
    private static final String RHEA_PREFIX = "rhea:";
    private static final String MTBLC = "MTBLC";

    private final SearchIndexService searchIndexService;
    private final EnzymePageService enzymePageService;

    @Autowired
    private BrendaService brendaService;

    @Autowired
    public EnzymecentricController(SearchIndexService searchIndexService, EnzymePageService enzymePageService) {
        this.searchIndexService = searchIndexService;
        this.enzymePageService = enzymePageService;

    }

    @GetMapping(value = ENZYMES)
    public String getSearchResults(@RequestParam(required = false, value = "searchKey") String searchKey,
            @RequestParam(required = false, value = "filterFacet") List<String> filters,
            @RequestParam(required = false, value = "servicePage") Integer servicePage,
            @RequestParam(required = false, value = "keywordType") String keywordType,
            @RequestParam(required = false, value = "searchId") String searchId,
            @RequestParam(required = false, value = "searchparams.text") String searchText,
            Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        return postSearchResult(searchKey, filters, servicePage, keywordType, searchId, searchText, model, request);
    }

    @PostMapping(value = ENZYMES)
    public String postSearchResult(@RequestParam(required = false, value = "searchKey") String searchKey,
            @RequestParam(required = false, value = "filterFacet") List<String> filters,
            @RequestParam(required = false, value = "servicePage") Integer servicePage,
            @RequestParam(required = false, value = "keywordType") String keywordType,
            @RequestParam(required = false, value = "searchId") String searchId,
            @RequestParam(required = false, value = "searchparams.text") String searchText,
            Model model, HttpServletRequest request) {

        int pageSize = PAGE_SIZE;
        int facetCount = DEFAULT_EBI_SEARCH_FACET_COUNT;
        int associatedProteinLimit = 5;// ASSOCIATED_PROTEIN_LIMIT;

        int startPage = refineStartPage(servicePage);
        filters = refineFilters(filters);
        searchKey = getSearchKey(searchText);
        String searchTerm = getSearchTerm(searchKey);

        KeywordType type = KeywordType.valueOf(keywordType);

        switch (type) {
            case KEYWORD:

                boolean isEc = SearchUtil.validateEc(searchTerm);
                if (isEc) {

                    logSearchQuery(SeachType.Keyword, SeachCategory.EC, searchTerm);
                    return findEnzymesByEC(searchTerm, startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model);
                } else {

                    if (searchTerm.contains("/")) {
                        searchTerm = searchTerm.replaceAll("/", " ");

                    }

                    if (searchTerm.toLowerCase().startsWith(CHEBI_ID_PREFIX)) {

                        keywordType = KeywordType.CHEBI.getKeywordType();
                        logSearchQuery(SeachType.Keyword, SeachCategory.CHEBI, searchTerm);
                        return findEnzymesByChebiId(searchTerm.toUpperCase(), startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model);

                    }
                    if (searchTerm.toLowerCase().startsWith(RHEA_PREFIX)) {
                        keywordType = KeywordType.RHEA.getKeywordType();

                        logSearchQuery(SeachType.Keyword, SeachCategory.RHEA, searchTerm);
                        return findEnzymesByRheaId(searchTerm.toUpperCase(), startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model);

                    }

                    logSearchQuery(SeachType.Keyword, SeachCategory.Fulltext, searchTerm);
                    return findEnzymesBySearchTerm(searchTerm, startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model);
                }

            case DISEASE:
                String disease = searchKey + "-" + searchId;
                logSearchQuery(SeachType.BrowseBy, SeachCategory.Diseases, disease);
                return findEnzymesByOmimId(searchId, startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model);

            case FAMILIES:

                logSearchQuery(SeachType.BrowseBy, SeachCategory.Families, searchKey);
                return findEnzymesByFamilyGroupId(searchId, startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model);

            case COFACTORS:

                searchKey = parseRequestQueryString(request, searchKey);
                String cofactor = searchKey + "-" + searchId;

                logSearchQuery(SeachType.BrowseBy, SeachCategory.cofactors, cofactor);
                return findEnzymesByCofactorId(searchId, startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model);

            case METABOLITES:

                searchKey = parseRequestQueryString(request, searchKey);
                String metabolite = searchKey + "-" + searchId;

                logSearchQuery(SeachType.BrowseBy, SeachCategory.Metabolite, metabolite);
                return findEnzymesByMetaboliteId(searchId, startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model);

            case CHEBI:

                searchKey = parseRequestQueryString(request, searchKey);
                String chebi = searchKey + "-" + searchId;

                logSearchQuery(SeachType.Keyword, SeachCategory.CHEBI, chebi);
                return findEnzymesByChebiId(searchTerm.toUpperCase(), startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model);

            case RHEA:

                logSearchQuery(SeachType.Keyword, SeachCategory.RHEA, searchId);
                return findEnzymesByRheaId(searchId, startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model);

            case TAXONOMY:
                String tax = searchKey + "-" + searchId;

                logSearchQuery(SeachType.BrowseBy, SeachCategory.Taxonomy, tax);
                return findEnzymesByTaxId(searchId, startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model);

            case PATHWAYS:
                String pathway = searchKey + "-" + searchId;

                logSearchQuery(SeachType.BrowseBy, SeachCategory.Pathways, pathway);
                return findEnzymesByPathwayId(searchId, startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model);

            case EC:

                logSearchQuery(SeachType.BrowseBy, SeachCategory.EC, searchId);
                return findEnzymesByEC(searchId, startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model);

            default:
                return ERROR_PAGE;

        }

    }

    public void addKinetics(String ec, Model model) {
        int limit = 7;
        boolean addAccession = false;

        BrendaResult brendaList = brendaService.findBrendaResultByEc(ec, limit, addAccession);

        boolean isKm = brendaList.isKm();
        model.addAttribute("isKm", isKm);

        model.addAttribute("brendaList", brendaList.getBrenda());
        model.addAttribute("phList", brendaList.getPh());
        model.addAttribute("tempList", brendaList.getTemperature());

    }

    @GetMapping(value = "/ec/{ec}")
    public String showEnzyme(@PathVariable("ec") String ec, Model model) {
        int resultLimit = 7;// ASSOCIATED_PROTEIN_LIMIT;

        boolean isEc = SearchUtil.validateEc(ec);

        if (isEc) {

            SearchQueryLog.logSearchQuery(SeachType.Keyword, SeachCategory.EC, ec);

            addKinetics(ec, model);

            EnzymeEntry entry = enzymePageService.findEnzymeByEcNumber(ec);
            String enzymeName = entry.getEnzymeName();

            EnzymePage enzymePage = enzymePageService.buildEnzymePage(ec, enzymeName, resultLimit);
            CompletableFuture<MechanismResult> mechanismsFuture = CompletableFuture.supplyAsync(() -> enzymePageService.findReactionMechanism(ec));

            MechanismResult reactionMechanism = mechanismsFuture.join();
            model.addAttribute("reactionMechanism", reactionMechanism);

            model.addAttribute("enzymePage", enzymePage);

            model.addAttribute("link", false);
            model.addAttribute("ec", ec);
            model.addAttribute("searchKey", ec);

            model.addAttribute("keywordType", KeywordType.EC.name());
            model.addAttribute("searchId", ec);

            return ENZYME_PAGE;
        } else {
            String searchTerm = ec;
            int pageSize = PAGE_SIZE;
            int facetCount = DEFAULT_EBI_SEARCH_FACET_COUNT;
            int associatedProteinLimit = 7;
            String keywordType = SeachType.Keyword.name();
            List<String> filters = new ArrayList<>();
            String searchKey = getSearchKey(ec);
            int startPage = refineStartPage(1);

            logSearchQuery(SeachType.Keyword, SeachCategory.Fulltext, searchTerm);
            return findEnzymesBySearchTerm(searchTerm, startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model);
        }

    }

    private String findEnzymesBySearchTerm(String searchTerm, int startPage, int pageSize, int facetCount, List<String> filters, int associatedProteinLimit, String searchKey, String keywordType, Model model) {

        String query = String.format("%s", searchTerm);
        EnzymeSearchResult enzymeSearchResult = searchIndexService.findEnzyme(query, startPage, pageSize, facetCount, filters);

        if (enzymeSearchResult != null) {
            long hitCount = enzymeSearchResult.getHitCount();

            Page<EnzymeEntry> page = buildEnzymeEntryPage(enzymeSearchResult.getEntries(), startPage, pageSize, hitCount);

            Set<EnzymeEntry> enzymeView = page.getContent().stream().collect(Collectors.toSet());
            enzymeView.parallelStream()
                    .forEach(entry -> processAssociatedProtein(enzymeView, entry, searchTerm, associatedProteinLimit));

            return constructModel(enzymeSearchResult, enzymeView, page, filters, searchTerm, searchKey, keywordType, model);

        }
        return ERROR_PAGE;
    }

    private void processAssociatedProtein(Set<EnzymeEntry> enzymeView, EnzymeEntry entry, String searchTerm, int limit) {

        String associatedQuery = String.format("%s AND %s%s", searchTerm, IndexQueryType.EC.getQueryType(), entry.getEc());

        ProteinGroupSearchResult result = searchIndexService.findAssociatedProtein(associatedQuery, limit, IndexQueryType.KEYWORD);

        if (result.getHitCount() == 0) {
            log.debug("No Associated Protein found for " + searchTerm + " With EC: " + entry.getEc());
            int LOWEST_BEST_MATCHED_RESULT_SIZE = 2;//shouldn't happen
            String ecQuery = String.format("%s%s", IndexQueryType.EC.getQueryType(), entry.getEc());

            result = searchIndexService.findAssociatedProtein(ecQuery, LOWEST_BEST_MATCHED_RESULT_SIZE, IndexQueryType.KEYWORD);

            if (result.getHitCount() == 0) {
                String intenzQuery = String.format("%s%s", IndexQueryType.INTENZ.getQueryType(), entry.getEc());

                result = searchIndexService.findAssociatedProtein(intenzQuery, LOWEST_BEST_MATCHED_RESULT_SIZE, IndexQueryType.EC);

            }

            //limit asscoated protein result
            if (result.getHitCount() > LOWEST_BEST_MATCHED_RESULT_SIZE) {
                result.setHitCount(LOWEST_BEST_MATCHED_RESULT_SIZE);
            }

        }
        addProteinEntryToEnzymeView(result, entry, enzymeView);

    }

    private String findEnzymesByChebiId(String chebiId, int startPage, int pageSize, int facetCount, List<String> filters, int associatedProteinLimit, String searchKey, String keywordType, Model model) {
        String escapedChebiId = SearchUtil.escape(chebiId);
        String query = String.format("%s%s", IndexQueryType.CHEBI.getQueryType(), escapedChebiId);
        return processEnzymeResultByResourceId(query, chebiId, escapedChebiId, IndexQueryType.CHEBI, startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model);

    }

    private String findEnzymesByRheaId(String rheaRawId, int startPage, int pageSize, int facetCount, List<String> filters, int associatedProteinLimit, String searchKey, String keywordType, Model model) {
        String rheaId = SearchUtil.escape(rheaRawId);
        String query = String.format("%s%s", IndexQueryType.RHEA.getQueryType(), rheaId);

        return processEnzymeResultByResourceId(query, rheaRawId, rheaId, IndexQueryType.RHEA, startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model);

    }

    private String findEnzymesByCofactorId(String chebiId, int startPage, int pageSize, int facetCount, List<String> filters, int associatedProteinLimit, String searchKey, String keywordType, Model model) {

        String chebiIdSuffix = chebiId.replace(CHEBI_ID_PREFIX.toUpperCase(), "");
        String cofactor = String.format("cofactor%s", chebiIdSuffix);
        String query = String.format("%s%s", IndexQueryType.COFACTOR.getQueryType(), cofactor);

        return processEnzymeResult(query, chebiIdSuffix, IndexQueryType.COFACTOR, startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model);

    }

    private String findEnzymesByMetaboliteId(String chebiId, int startPage, int pageSize, int facetCount, List<String> filters, int associatedProteinLimit, String searchKey, String keywordType, Model model) {

        String metaboliteId = chebiId;
        if (metaboliteId.contains(CHEBI_ID_PREFIX.toUpperCase())) {
            String chebiIdSuffix = chebiId.replace(CHEBI_ID_PREFIX.toUpperCase(), "");
            metaboliteId = String.format(MTBLC + "%s", chebiIdSuffix);
        }
        String query = String.format("%s%s", IndexQueryType.METABOLIGHTS.getQueryType(), metaboliteId);

        return processEnzymeResult(query, metaboliteId, IndexQueryType.METABOLIGHTS, startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model);

    }

    private String findEnzymesByOmimId(String omimId, int startPage, int pageSize, int facetCount, List<String> filters, int associatedProteinLimit, String searchKey, String keywordType, Model model) {
        String query = String.format("%s%s", IndexQueryType.OMIM.getQueryType(), omimId);
        return processEnzymeResult(query, omimId, IndexQueryType.OMIM, startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model);

    }

    private String findEnzymesByFamilyGroupId(String familyId, int startPage, int pageSize, int facetCount, List<String> filters, int associatedProteinLimit, String searchKey, String keywordType, Model model) {

        String query = String.format("%s%s", IndexQueryType.PROTEIN_FAMILY_ID.getQueryType(), familyId);
        return processEnzymeResult(query, familyId, IndexQueryType.PROTEIN_FAMILY_ID, startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model);

    }

    private String findEnzymesByTaxId(String taxId, int startPage, int pageSize, int facetCount, List<String> filters, int associatedProteinLimit, String searchKey, String keywordType, Model model) {
        String query = String.format("%s%s", IndexQueryType.TAXONOMY.getQueryType(), taxId);
        return processEnzymeResult(query, taxId, IndexQueryType.TAXONOMY, startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model);
    }

    private String findEnzymesByPathwayId(String pathwayId, int startPage, int pageSize, int facetCount, List<String> filters, int associatedProteinLimit, String searchKey, String keywordType, Model model) {
        String query = String.format("%s%s", IndexQueryType.REACTOME.getQueryType(), pathwayId);
        return processEnzymeResult(query, pathwayId, IndexQueryType.REACTOME, startPage, pageSize, facetCount, filters, associatedProteinLimit, searchKey, keywordType, model);

    }

    private String findEnzymesByEC(String ec, int startPage, int pageSize, int facetCount, List<String> filters, int associatedProteinLimit, String searchKey, String keywordType, Model model) {

        String validEc = SearchUtil.transformIncompleteEc(ec);
        String query = String.format("%s%s", IndexQueryType.ID.getQueryType(), validEc);
        EnzymeSearchResult enzymeSearchResult = searchIndexService.findEnzyme(query, startPage, pageSize, facetCount, filters);

        if (enzymeSearchResult != null) {
            long hitCount = enzymeSearchResult.getHitCount();

            if (hitCount == 1) {

                return redirect(ENZYME_PAGE_REQUEST + ec);

            }

            Page<EnzymeEntry> page = buildEnzymeEntryPage(enzymeSearchResult.getEntries(), startPage, pageSize, hitCount);

            Set<EnzymeEntry> enzymeView = page.getContent().stream().collect(Collectors.toSet());
            enzymeView.parallelStream()
                    .forEach(entry -> processAssociatedProteinWithEC(entry, enzymeView, associatedProteinLimit));

            return constructModel(enzymeSearchResult, enzymeView, page, filters, ec, searchKey, keywordType, model);

        }
        return ERROR_PAGE;
    }

    private void processAssociatedProteinWithEC(EnzymeEntry entry, Set<EnzymeEntry> enzymeView, int associatedProteinLimit) {
        String ecQuery = String.format("%s%s", IndexQueryType.EC.getQueryType(), entry.getEc());

        ProteinGroupSearchResult result = searchIndexService.findAssociatedProtein(ecQuery, associatedProteinLimit, IndexQueryType.EC);
        if (result.getHitCount() == 0) {
            ecQuery = String.format("%s%s", IndexQueryType.EC.getQueryType(), entry.getEc());
            result = searchIndexService.findAssociatedProtein(ecQuery, associatedProteinLimit, IndexQueryType.EC);
        }

        addProteinEntryToEnzymeView(result, entry, enzymeView);
    }

    private String processEnzymeResult(String query, String resourceId, IndexQueryType resourceQueryType, int startPage, int pageSize, int facetCount, List<String> filters, int associatedProteinLimit, String searchKey, String keywordType, Model model) {

        EnzymeSearchResult enzymeSearchResult = searchIndexService.findEnzyme(query, startPage, pageSize, facetCount, filters);

        if (enzymeSearchResult != null) {
            long hitCount = enzymeSearchResult.getHitCount();

            Page<EnzymeEntry> page = buildEnzymeEntryPage(enzymeSearchResult.getEntries(), startPage, pageSize, hitCount);

            Set<EnzymeEntry> enzymeView = page.getContent().stream().collect(Collectors.toSet());

            page.getContent()
                    .parallelStream()
                    .forEach(entry -> processAssociatedProtein(enzymeView, entry, resourceId, resourceQueryType, associatedProteinLimit));

            return constructModel(enzymeSearchResult, enzymeView, page, filters, resourceId, searchKey, keywordType, model);

        }
        return ERROR_PAGE;
    }

    private String processEnzymeResultByResourceId(String query, String resourceId, String escapedId, IndexQueryType resourceQueryType, int startPage, int pageSize, int facetCount, List<String> filters, int associatedProteinLimit, String searchKey, String keywordType, Model model) {
        EnzymeSearchResult enzymeSearchResult = searchIndexService.findEnzyme(query, startPage, pageSize, facetCount, filters);
        if (enzymeSearchResult != null) {
            long hitCount = enzymeSearchResult.getHitCount();

            Page<EnzymeEntry> page = buildEnzymeEntryPage(enzymeSearchResult.getEntries(), startPage, pageSize, hitCount);

            Set<EnzymeEntry> enzymeView = page.getContent().stream().collect(Collectors.toSet());

            page.getContent()
                    .parallelStream()
                    .forEach(entry -> processAssociatedProtein(enzymeView, entry, escapedId, resourceQueryType, associatedProteinLimit));

            return constructModel(enzymeSearchResult, enzymeView, page, filters, resourceId, searchKey, keywordType, model);

        }
        return ERROR_PAGE;
    }

    private void processAssociatedProtein(Set<EnzymeEntry> enzymeView, EnzymeEntry entry, String resourceId, IndexQueryType resourceQueryType, int limit) {
        String associatedQuery = String.format("%s%s AND %s%s", resourceQueryType.getQueryType(), resourceId, IndexQueryType.EC.getQueryType(), entry.getEc());
        ProteinGroupSearchResult result = searchIndexService.findAssociatedProtein(associatedQuery, limit, resourceQueryType);

        if (result.getHitCount() == 0) {
            associatedQuery = String.format("%s%s AND %s%s", resourceQueryType.getQueryType(), resourceId, IndexQueryType.INTENZ.getQueryType(), entry.getEc());
            result = searchIndexService.findAssociatedProtein(associatedQuery, limit, resourceQueryType);
        }

        addProteinEntryToEnzymeView(result, entry, enzymeView);

    }

}
