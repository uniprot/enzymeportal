package uk.ac.ebi.ep.restapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import uk.ac.ebi.ep.enzymeservice.reactome.view.PathWay;
import uk.ac.ebi.ep.literatureservice.dto.LabelledCitation;
import uk.ac.ebi.ep.restapi.dto.EnzymeDisease;
import uk.ac.ebi.ep.restapi.dto.PdbInfo;
import uk.ac.ebi.ep.restapi.dto.Reaction;
import uk.ac.ebi.ep.restapi.dto.SmallMolecule;
import uk.ac.ebi.ep.restapi.exceptions.ResourceNotFoundException;
import uk.ac.ebi.ep.restapi.hateoas.ProteinResourceAssembler;
import uk.ac.ebi.ep.restapi.model.Protein;
import uk.ac.ebi.ep.restapi.service.ProteinApiService;

/**
 *
 * @author joseph
 */
@BasePathAwareController
@Tag(name = "Protein-centric", description = "Protein centred endpoints")
@RequestMapping(value = "/protein", produces = {MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE})
@RestController
public class ProteinController {

    private final ProteinApiService proteinApiService;
    private final ProteinResourceAssembler  proteinResourceAssembler;

    @Autowired
    public ProteinController(ProteinApiService proteinApiService, ProteinResourceAssembler proteinAssembler) {
        this.proteinApiService = proteinApiService;
        this.proteinResourceAssembler = proteinAssembler;
    }

    /**
     * GET /protein/{accession} : Get Protein by Accession Find the protein with
     * the associated Uniprot accession.
     *
     * @param accession a valid Uniprot accession (required)
     * @return protein entry (status code 200) or bad input parameter (status
     * code 400) or Not Found (status code 404)
     */
    @Operation(operationId = "findProteinByAccession", summary = "Get Protein by Accession", description = "Find the protein with the associated Uniprot accession.", tags = {"Protein-centric"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Protein.class))),
        @ApiResponse(responseCode = "400", description = "Invalid Uniprot accession", content = @Content)})
    @GetMapping(value = "/{accession}")
    public ResponseEntity<EntityModel<Protein>> findProteinByAccession(@Parameter(description = "a valid Uniprot accession.", required = true, example = "P07327") @PathVariable("accession") String accession) {

        if (Objects.isNull(accession)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return Optional.ofNullable(proteinApiService.proteinByAccession(accession))
                .map(proteinResourceAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Protein with uniProt Accession=%s not found", accession)));

    }

    @Operation(operationId = "findProteinStructureByAccession", summary = "Get PDBe Protein Structure by UniProt Accession", description = "Find the PDBe protein structure information associated with the given Uniprot accession.", tags = {"Protein-centric"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = PdbInfo.class))),
        @ApiResponse(responseCode = "400", description = "Invalid Uniprot accession", content = @Content)})
    @GetMapping(value = "/{accession}/proteinStructure")
    public List<PdbInfo> findProteinStructureByAccession(@Parameter(description = "a valid Uniprot accession.", required = true, example = "P07327") @PathVariable("accession") @Valid String accession) {

        return proteinApiService.pdbByAccession(accession);
    }

    @GetMapping(value = "/{accession}/reaction")
    public ResponseEntity<Reaction> findReactionsByAccession(@Parameter(description = "a valid Uniprot accession.", required = true, example = "P07327") @PathVariable("accession") String accession, @Parameter(description = "number of reaction mechanism result to return") @RequestParam(value = "limit", defaultValue = "1") int limit) {

        Optional<Reaction> reaction = Optional.ofNullable(proteinApiService.enzymeReactionByAccession(accession, limit));

        return reaction
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @GetMapping(value = "/{accession}/pathways")
    public Flux<PathWay> findPathwaysByAccession(@Parameter(description = "a valid Uniprot accession.", required = true, example = "P07327") @PathVariable("accession") String accession) {

        return proteinApiService.pathwaysByAccession(accession);
    }

    @GetMapping(value = "/{accession}/smallmolecules")
    public ResponseEntity<SmallMolecule> findSmallmoleculesByAccession(@Parameter(description = "a valid Uniprot accession.", required = true, example = "P07327") @PathVariable("accession") String accession) {
        Optional<SmallMolecule> molecule = Optional.ofNullable(proteinApiService.smallMoleculeByAccession(accession));

        return molecule
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/{accession}/diseases")
    public List<EnzymeDisease> findDiseasesByAccession(@Parameter(description = "a valid Uniprot accession.", required = true, example = "O15297") @PathVariable("accession") String accession) {

        return proteinApiService.diseasesByAccession(accession);
    }

    @GetMapping(value = "/{accession}/citation")
    public ResponseEntity<List<LabelledCitation>> findCitationsByAccession(@Parameter(description = "a valid Uniprot accession.", required = true, example = "P07327") @PathVariable("accession") String accession, @Parameter(description = "citation result limit") @RequestParam(value = "limit", defaultValue = "1") int limit) {

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS).cachePrivate().mustRevalidate())
                .body(proteinApiService.citationsByAccession(accession, limit));
    }

}
