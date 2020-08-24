package uk.ac.ebi.ep.restapi.controller;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.ep.dataservice.dto.DiseaseView;
import uk.ac.ebi.ep.dataservice.dto.MetaboliteView;
import uk.ac.ebi.ep.dataservice.dto.PathwayView;
import uk.ac.ebi.ep.dataservice.entities.EnzymePortalCofactor;
import uk.ac.ebi.ep.restapi.service.ResourceService;

/**
 *
 * @author joseph
 */
@Slf4j
@Tag(name = "Enzyme Resource", description = "Enzymes resources endpoint")
@RequestMapping(value = "/resource", produces = {MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE})
@RestController
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    private Pageable getPageable(int page, int pageSize) {
        return PageRequest.of(page, pageSize);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = EnzymePortalCofactor.class))),
        @ApiResponse(responseCode = "400", description = "Invalid EC number", content = @Content)})
    @GetMapping(value = "/cofactors")
    public Page<EnzymePortalCofactor> getCofactors(@Parameter(description = "Page number", example = "0", explode = Explode.TRUE, in = ParameterIn.QUERY, name = "page", style = ParameterStyle.FORM) @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "Page size", explode = Explode.TRUE, in = ParameterIn.QUERY, name = "pageSize", style = ParameterStyle.FORM) @Valid @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        Pageable pageable = getPageable(page, pageSize);
        return resourceService.cofactors(pageable);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = DiseaseView.class))),
        @ApiResponse(responseCode = "400", description = "Invalid EC number", content = @Content)})
    @GetMapping(value = "/diseases")
    public Page<DiseaseView> getDiseases(@Parameter(description = "Page number", example = "0", explode = Explode.TRUE, in = ParameterIn.QUERY, name = "page", style = ParameterStyle.FORM) @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "Page size", explode = Explode.TRUE, in = ParameterIn.QUERY, name = "pageSize", style = ParameterStyle.FORM) @Valid @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        Pageable pageable = getPageable(page, pageSize);
        return resourceService.diseases(pageable);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = MetaboliteView.class))),
        @ApiResponse(responseCode = "400", description = "Invalid EC number", content = @Content)})
    @GetMapping(value = "/metabolites")
    public Page<MetaboliteView> getMetabolites(@Parameter(description = "Page number", example = "0", explode = Explode.TRUE, in = ParameterIn.QUERY, name = "page", style = ParameterStyle.FORM) @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "Page size", explode = Explode.TRUE, in = ParameterIn.QUERY, name = "pageSize", style = ParameterStyle.FORM) @Valid @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Pageable pageable = getPageable(page, pageSize);
        return resourceService.metabolites(pageable);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = PathwayView.class))),
        @ApiResponse(responseCode = "400", description = "Invalid EC number", content = @Content)})
    @GetMapping(value = "/pathways")
    public Page<PathwayView> getPathways(@Parameter(description = "Page number", example = "0", explode = Explode.TRUE, in = ParameterIn.QUERY, name = "page", style = ParameterStyle.FORM) @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "Page size", explode = Explode.TRUE, in = ParameterIn.QUERY, name = "pageSize", style = ParameterStyle.FORM) @Valid @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        Pageable pageable = getPageable(page, pageSize);

        return resourceService.pathways(pageable);
    }

}
