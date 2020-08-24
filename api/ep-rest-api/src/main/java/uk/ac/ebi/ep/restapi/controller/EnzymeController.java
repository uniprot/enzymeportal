package uk.ac.ebi.ep.restapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeEntry;
import uk.ac.ebi.ep.indexservice.model.protein.ProteinGroupEntry;
import uk.ac.ebi.ep.literatureservice.model.Result;
import uk.ac.ebi.ep.reaction.mechanism.model.MechanismResult;
import uk.ac.ebi.ep.restapi.dto.BrendaParameter;
import uk.ac.ebi.ep.restapi.exceptions.ResourceNotFoundException;
import uk.ac.ebi.ep.restapi.hateoas.EnzymeModelAssembler;
import uk.ac.ebi.ep.restapi.hateoas.PaginationModelAssembler;
import uk.ac.ebi.ep.restapi.hateoas.ProteinSummaryModelAssembler;
import uk.ac.ebi.ep.restapi.model.EnzymeModel;
import uk.ac.ebi.ep.restapi.model.ProteinModel;
import uk.ac.ebi.ep.restapi.model.ProteinSummary;
import uk.ac.ebi.ep.restapi.service.EnzymeApiService;
import uk.ac.ebi.ep.restapi.util.SearchUtil;

/**
 *
 * @author joseph
 */
@Slf4j
@Tag(name = "Enzyme Entry", description = "Search for all enzymes or an enzyme by a valid complete EC number")
@RequestMapping(value = "/enzymes", produces = {MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE})
@RestController
public class EnzymeController {

    private final EnzymeModelAssembler enzymeModelAssembler;
    private final EnzymeApiService enzymeApiService;
    private final ProteinSummaryModelAssembler proteinSummaryModelAssembler;
    private final PaginationModelAssembler paginationModelAssembler;

    private final PagedResourcesAssembler<EnzymeEntry> pagedResourcesAssembler;

    @Autowired
    public EnzymeController(PagedResourcesAssembler<EnzymeEntry> pagedResourcesAssembler, PaginationModelAssembler resultModelAssembler, EnzymeModelAssembler enzymeModelAssembler, EnzymeApiService enzymeApiService, ProteinSummaryModelAssembler proteinSummaryModelAssembler) {
        this.enzymeModelAssembler = enzymeModelAssembler;
        this.enzymeApiService = enzymeApiService;
        this.proteinSummaryModelAssembler = proteinSummaryModelAssembler;
        this.paginationModelAssembler = resultModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Operation(operationId = "findEnzymes", summary = "Get All Enzymes", description = "Get all Enzymes available", tags = {"Enzyme-centric"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = EnzymeModel.class)))})
    @GetMapping(value = "/")
    public PagedModel<EnzymeModel> enzymes(@Parameter(description = "page number") @RequestParam(value = "page", defaultValue = "0", name = "page") int page, @Parameter(description = " result limit") @RequestParam(value = "size", defaultValue = "10") int size) {
        SearchUtil.validatePageSize(size);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "ecNumber");
        Page<EnzymeEntry> enzymes = enzymeApiService.getEnzymeEntries(pageable);
        return pagedResourcesAssembler.toModel(enzymes, paginationModelAssembler);
    }

    /**
     * GET /enzyme/{ec} : Get Enzyme by EC number Find the enzyme with the given
     * valid EC number.
     *
     * @param ec a valid EC number (including incomplete EC number) (required)
     * @return enzyme entry (status code 200) or bad input parameter (status
     * code 400) or Not Found (status code 404)
     */
    @Operation(operationId = "findEnzymeByEcNumber", summary = "Get Enzyme by EC number", description = "Find the enzyme with the given valid EC number", tags = {"Enzyme-centric"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = EnzymeModel.class))),
        @ApiResponse(responseCode = "400", description = "Invalid EC number", content = @Content)})
    @GetMapping(value = "/{ec}")

    public ResponseEntity<EnzymeModel> findEnzymeByEcNumber(@Parameter(description = "a valid EC number.", required = true) @PathVariable("ec") @Size(min = 7, max = 7) String ec) {

        validateEC(ec);

        return Optional.ofNullable(enzymeApiService.enzymeByEcNumber(ec))
                .map(enzymeModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Enzyme with ec=%s not found", ec)));
    }

    @GetMapping(value = "/{ec}/citations")
    public ResponseEntity<List<Result>> findCitationByEcNumber(@Parameter(description = "a valid EC number.", required = true) @PathVariable("ec") String ec, @Parameter(description = "citation result limit") @RequestParam(value = "limit", defaultValue = "10") @PositiveOrZero int limit) {

        validateEC(ec);
        return Optional.ofNullable(enzymeApiService.findCitations(ec, limit))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ProteinModel.class))),
        @ApiResponse(responseCode = "400", description = "Invalid EC number", content = @Content)})
    @GetMapping(value = "/{ec}/proteins")
    public ResponseEntity<CollectionModel<ProteinSummary>> findAssociatedProteinsByEcNumber(@Parameter(description = "a valid EC number.", required = true) @PathVariable("ec") String ec, @Parameter(description = " result limit") @RequestParam(value = "limit", defaultValue = "10") int limit) {
        validateEC(ec);
        SearchUtil.validatePageSize(limit);
        Link selfLink = linkTo(methodOn(EnzymeController.class).findAssociatedProteinsByEcNumber(ec, limit)).withSelfRel();

        List<ProteinGroupEntry> proteins = enzymeApiService.associatedProteinSummaryByEc(ec, limit);

        return Optional.ofNullable(proteinSummaryModelAssembler.toCollectionModel(proteins)
                .add(selfLink))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("No Protein found for ec=%s and limit=%s ", ec, limit)));

    }

    @GetMapping(value = "/{ec}/kineticParameters")
    public ResponseEntity<BrendaParameter> findKineticParametersByEcNumber(@Parameter(description = "a valid EC number.", required = true) @PathVariable("ec") String ec, @Parameter(description = " result limit") @RequestParam(value = "limit", defaultValue = "10") int limit) {

        validateEC(ec);
        return Optional.ofNullable(enzymeApiService.reactionParameterByEcNumber(ec, limit))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/{ec}/mechanisms")
    public ResponseEntity<MechanismResult> findMechanismsByEcNumber(@Parameter(description = "a valid EC number.", required = true) @PathVariable("ec") String ec) {

        validateEC(ec);
        return Optional.ofNullable(enzymeApiService.findReactionMechanism(ec))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private void validateEC(String ec) {

        SearchUtil.validateEcNumber(ec);
    }

}
