package uk.ac.ebi.ep.restapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.ep.indexservice.helper.IndexQueryType;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeEntry;
import uk.ac.ebi.ep.indexservice.model.protein.ProteinGroupEntry;
import uk.ac.ebi.ep.restapi.hateoas.PaginationModelAssembler;
import uk.ac.ebi.ep.restapi.hateoas.ProteinSummaryModelAssembler;
import uk.ac.ebi.ep.restapi.model.EnzymeModel;
import uk.ac.ebi.ep.restapi.model.ProteinSummary;
import uk.ac.ebi.ep.restapi.service.EnzymeApiService;
import uk.ac.ebi.ep.restapi.service.ResourceService;
import uk.ac.ebi.ep.restapi.util.SearchUtil;

/**
 *
 * @author joseph
 */
@Slf4j
@Tag(name = "Search", description = "Search for Enzymes using keyword (gene name, metabolites, incomplete EC etc.) and enzyme resource ID")
@RequestMapping(value = "/search", produces = {MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE})
@RestController
public class SearchController {

    private static final String CHEBI_ID_PREFIX = "chebi:";
    private static final String MTBLC = "MTBLC";

    private final EnzymeApiService enzymeApiService;
    private final ResourceService resourceService;

    private final PagedResourcesAssembler<EnzymeEntry> pagedResourcesAssembler;

    private final PagedResourcesAssembler<ProteinGroupEntry> proteinPagedResourcesAssembler;

    private final ProteinSummaryModelAssembler proteinSummaryModelAssembler;
    private final PaginationModelAssembler pageModelAssembler;

    @Autowired
    public SearchController(EnzymeApiService enzymeApiService, ResourceService resourceService, PagedResourcesAssembler<EnzymeEntry> pagedResourcesAssembler, PagedResourcesAssembler<ProteinGroupEntry> proteinPagedResourcesAssembler, ProteinSummaryModelAssembler proteinSummaryModelAssembler, PaginationModelAssembler pageModelAssembler) {
        this.enzymeApiService = enzymeApiService;
        this.resourceService = resourceService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.proteinPagedResourcesAssembler = proteinPagedResourcesAssembler;
        this.proteinSummaryModelAssembler = proteinSummaryModelAssembler;
        this.pageModelAssembler = pageModelAssembler;
    }

    private Pageable getPageable(int page, int pageSize) {
        SearchUtil.validatePageSize(pageSize);
        return PageRequest.of(page, pageSize);
    }

    @Operation(operationId = "findEnzymesByKeyword", summary = "Get a list of Enzymes that matches the given search query", description = "search for enzyme results using  a valid search query", tags = {"Search"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = EnzymeModel.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input parameter", content = @Content)})
    @GetMapping(value = "/")
    public ResponseEntity<PagedModel<EnzymeModel>> findEnzymesByKeyword(@Parameter(description = "Page number", example = "1", explode = Explode.TRUE, in = ParameterIn.QUERY, name = "page", style = ParameterStyle.FORM) @RequestParam(value = "page", defaultValue = "1") int page,
            @Parameter(description = "Page size", explode = Explode.TRUE, in = ParameterIn.QUERY, name = "pageSize", style = ParameterStyle.FORM) @Valid @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @Parameter(description = "search query", explode = Explode.TRUE, in = ParameterIn.QUERY, name = "query", style = ParameterStyle.FORM, example = "pyruvate kinase") @Valid @RequestParam(value = "query", required = true) String query) {

        page = refineStartPage(page);
        Pageable pageable = getPageable(page, pageSize);

        boolean isEc = SearchUtil.validateEc(query);
        if (isEc) {
            String validEc = SearchUtil.transformIncompleteEc(query);
            String indexQuery = String.format("%s%s", IndexQueryType.ID.getQueryType(), validEc);

            Page<EnzymeEntry> searchResult = enzymeApiService.findEnzymesBySearchTerm(validEc, indexQuery, pageable);

            return getPagedModelResult(searchResult);

        }

        String indexQuery = String.format("%s", query);

        Page<EnzymeEntry> searchResult = enzymeApiService.findEnzymesBySearchTerm(query, indexQuery, pageable);

        return getPagedModelResult(searchResult);

    }

    @GetMapping(value = "/proteins")
    public PagedModel<ProteinSummary> findAssociatedProteins(@Parameter(description = "Page number", explode = Explode.TRUE, in = ParameterIn.QUERY, name = "page", style = ParameterStyle.FORM) @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "Page size", explode = Explode.TRUE, in = ParameterIn.QUERY, name = "pageSize", style = ParameterStyle.FORM) @Valid @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @Parameter(example = "pyruvate kinase", description = "search query", explode = Explode.TRUE, in = ParameterIn.QUERY, name = "query", style = ParameterStyle.FORM) @Valid @RequestParam(value = "query", required = true) String query,
            @Parameter(example = "2.7.1.40", description = "ec number", explode = Explode.TRUE, in = ParameterIn.QUERY, name = "ec", style = ParameterStyle.FORM) @Valid @RequestParam(value = "ec", required = true) String ecNumber) {
        page = refineStartPage(page);

        Pageable pageable = getPageable(page, pageSize);

        Page<ProteinGroupEntry> pages = enzymeApiService.findProteinSummary(query, ecNumber, pageable);

        PagedModel<ProteinSummary> pageModel = proteinPagedResourcesAssembler.toModel(pages, proteinSummaryModelAssembler);

        return pageModel;
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = EnzymeModel.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input parameter", content = @Content)})
    @GetMapping(value = "/cofactors/{chebiId}")
    public ResponseEntity<PagedModel<EnzymeModel>> findEnzymesByCofactor(@Parameter(description = "a valid CHEBI ID. e.g CHEBI:57925 or 57925", example = "CHEBI:57925", required = true) @PathVariable("chebiId") String chebiId, @Parameter(description = "Page number", example = "1", explode = Explode.TRUE, in = ParameterIn.QUERY, name = "page", style = ParameterStyle.FORM) @RequestParam(value = "page", defaultValue = "1") int page,
            @Parameter(description = "Page size", explode = Explode.TRUE, in = ParameterIn.QUERY, name = "pageSize", style = ParameterStyle.FORM) @Valid @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        page = refineStartPage(page);

        String cofactorId = String.format("cofactor%s", chebiId);
        String chebiIdSuffix = chebiId;
        if (chebiId.toLowerCase().startsWith(CHEBI_ID_PREFIX)) {
            chebiIdSuffix = chebiId.toLowerCase().replace(CHEBI_ID_PREFIX.toLowerCase(), "");
            cofactorId = String.format("cofactor%s", chebiIdSuffix);
        }

        String query = String.format("%s%s", IndexQueryType.COFACTOR.getQueryType(), cofactorId);

        Pageable pageable = getPageable(page, pageSize);
        Page<EnzymeEntry> searchResult = resourceService.findEnzymesByResourceId(query, chebiIdSuffix, IndexQueryType.COFACTOR, pageable);

        return getPagedModelResult(searchResult);

    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = EnzymeModel.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input parameter", content = @Content)})
    @GetMapping(value = "/diseases/{omimId}")
    public ResponseEntity<PagedModel<EnzymeModel>> findEnzymesByDisease(@Parameter(description = "a valid OMIM number.", example = "114480", required = true) @PathVariable("omimId") String omimId, @Parameter(description = "Page number", example = "1", explode = Explode.TRUE, in = ParameterIn.QUERY, name = "page", style = ParameterStyle.FORM) @RequestParam(value = "page", defaultValue = "1") int page,
            @Parameter(description = "Page size", explode = Explode.TRUE, in = ParameterIn.QUERY, name = "pageSize", style = ParameterStyle.FORM) @Valid @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        page = refineStartPage(page);

        String query = String.format("%s%s", IndexQueryType.OMIM.getQueryType(), omimId);

        Pageable pageable = getPageable(page, pageSize);
        Page<EnzymeEntry> searchResult = resourceService.findEnzymesByResourceId(query, omimId, IndexQueryType.OMIM, pageable);

        return getPagedModelResult(searchResult);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = EnzymeModel.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input parameter", content = @Content)})
    @GetMapping(value = "/metabolites/{metabolightId}")
    public ResponseEntity<PagedModel<EnzymeModel>> findEnzymesByMetabolight(@Parameter(description = "a valid Metabolight Id. e.g. MTBLC57925 or 57925", example = "MTBLC57925", required = true) @PathVariable("metabolightId") String metabolightId, @Parameter(description = "Page number", example = "1", explode = Explode.TRUE, in = ParameterIn.QUERY, name = "page", style = ParameterStyle.FORM) @RequestParam(value = "page", defaultValue = "1") int page,
            @Parameter(description = "Page size", explode = Explode.TRUE, in = ParameterIn.QUERY, name = "pageSize", style = ParameterStyle.FORM) @Valid @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        page = refineStartPage(page);

        String metaboliteId = metabolightId;
        String resourceId = metabolightId;
        if (metaboliteId.contains(CHEBI_ID_PREFIX.toUpperCase())) {
            String chebiIdSuffix = metabolightId.replace(CHEBI_ID_PREFIX.toUpperCase(), "");
            metaboliteId = String.format(MTBLC + "%s", chebiIdSuffix);
            resourceId = String.format(MTBLC + "%s", chebiIdSuffix);
        }
        if (!metaboliteId.startsWith(MTBLC)) {
            metaboliteId = String.format(MTBLC + "%s", metabolightId);
            resourceId = metabolightId;
        }
        String query = String.format("%s%s", IndexQueryType.METABOLIGHTS.getQueryType(), metaboliteId);

        Pageable pageable = getPageable(page, pageSize);
        Page<EnzymeEntry> searchResult = resourceService.findEnzymesByResourceId(query, resourceId, IndexQueryType.METABOLIGHTS, pageable);

        return getPagedModelResult(searchResult);

    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = EnzymeModel.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input parameter", content = @Content)})
    @GetMapping(value = "/organisms/{taxId}")
    public ResponseEntity<PagedModel<EnzymeModel>> findEnzymesByTaxonomy(@Parameter(description = "a valid Tax ID.", example = "9606", required = true) @PathVariable("taxId") String taxId, @Parameter(description = "Page number", example = "1", explode = Explode.TRUE, in = ParameterIn.QUERY, name = "page", style = ParameterStyle.FORM) @RequestParam(value = "page", defaultValue = "1") int page,
            @Parameter(description = "Page size", explode = Explode.TRUE, in = ParameterIn.QUERY, name = "pageSize", style = ParameterStyle.FORM) @Valid @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        page = refineStartPage(page);

        String query = String.format("%s%s", IndexQueryType.TAXONOMY.getQueryType(), taxId);

        Pageable pageable = getPageable(page, pageSize);
        Page<EnzymeEntry> searchResult = resourceService.findEnzymesByResourceId(query, taxId, IndexQueryType.TAXONOMY, pageable);

        return getPagedModelResult(searchResult);

    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = EnzymeModel.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input parameter", content = @Content)})
    @GetMapping(value = "/pathways/{reactomeId}")
    public ResponseEntity<PagedModel<EnzymeModel>> findEnzymesByPathways(@Parameter(description = "a valid  Reactome Id.", example = "R-211981", required = true) @PathVariable("reactomeId") String reactomeId, @Parameter(description = "Page number", example = "1", explode = Explode.TRUE, in = ParameterIn.QUERY, name = "page", style = ParameterStyle.FORM) @RequestParam(value = "page", defaultValue = "1") int page,
            @Parameter(description = "Page size", explode = Explode.TRUE, in = ParameterIn.QUERY, name = "pageSize", style = ParameterStyle.FORM) @Valid @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        page = refineStartPage(page);

        String pathwayId = reactomeId;
        if (reactomeId.toUpperCase().startsWith("R-HSA-")) {
            pathwayId = reactomeId.replace("R-HSA-", "R-");
        }
        if (!reactomeId.toUpperCase().contains("R-")) {
            pathwayId = String.format("R-", reactomeId);
        }

        String query = String.format("%s%s", IndexQueryType.REACTOME.getQueryType(), pathwayId);

        Pageable pageable = getPageable(page, pageSize);
        Page<EnzymeEntry> searchResult = resourceService.findEnzymesByResourceId(query, reactomeId, IndexQueryType.REACTOME, pageable);

        return getPagedModelResult(searchResult);

    }

    private ResponseEntity<PagedModel<EnzymeModel>> getPagedModelResult(Page<EnzymeEntry> pages) {
        PagedModel<EnzymeModel> pageModel = pagedResourcesAssembler.toModel(pages, pageModelAssembler);

        return ResponseEntity.ok(pageModel);
    }

    private int refineStartPage(int page) {

        if (page <= 0) {
            page = 1;
        }
        int startPage = page - 1;//EBI search paging index starts at 0

        return startPage;
    }

}
