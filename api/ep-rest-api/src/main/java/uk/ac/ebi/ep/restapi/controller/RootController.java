package uk.ac.ebi.ep.restapi.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author joseph
 */
@Hidden
@Tag(name = "API Index", description = "API entry point with overview of provided endpoints with example input parameters.")
@RestController
public class RootController {

    @GetMapping(value = "/", produces = {MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE})
    ResponseEntity<RepresentationModel> root() {

        Pageable pageable = PageRequest.of(0, 10);
        //links to browse by pages
        RepresentationModel model = new RepresentationModel();

        Link self = linkTo(methodOn(RootController.class).root()).withSelfRel();
        Link enzymes = linkTo(methodOn(EnzymeController.class).enzymes(0, 10)).withRel("Enzymes");
        Link ec = linkTo(methodOn(EnzymeController.class).findEnzymeByEcNumber("7.1.1.1")).withRel("Search By EC");
        Link generic = linkTo(methodOn(SearchController.class).findEnzymesByKeyword(0, 10, "human")).withRel("Generic Search");
        Link protein = linkTo(methodOn(ProteinController.class).findProteinByAccession("O15297")).withRel("Search By Uniprot Accession");

        Link cofactors = linkTo(methodOn(ResourceController.class).getCofactors(pageable.getPageNumber(), pageable.getPageSize())).withRel("Cofactors");
        Link diseases = linkTo(methodOn(ResourceController.class).getDiseases(pageable.getPageNumber(), pageable.getPageSize())).withRel("Diseases");
        Link metabolites = linkTo(methodOn(ResourceController.class).getMetabolites(pageable.getPageNumber(), pageable.getPageSize())).withRel("Metabolites");
        Link pathways = linkTo(methodOn(ResourceController.class).getPathways(pageable.getPageNumber(), pageable.getPageSize())).withRel("Pathways");
        List<Link> links = Arrays.asList(self, enzymes, cofactors, diseases, metabolites, pathways, ec, generic, protein);
        model.add(links);

        return ResponseEntity.ok(model);
    }

}
