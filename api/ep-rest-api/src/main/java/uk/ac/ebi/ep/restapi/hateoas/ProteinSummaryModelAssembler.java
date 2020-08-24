package uk.ac.ebi.ep.restapi.hateoas;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.stereotype.Component;
import uk.ac.ebi.ep.indexservice.model.protein.ProteinGroupEntry;
import uk.ac.ebi.ep.restapi.controller.ProteinController;
import uk.ac.ebi.ep.restapi.model.ProteinSummary;

/**
 *
 * @author joseph
 */
@Component
public class ProteinSummaryModelAssembler implements RepresentationModelAssembler<ProteinGroupEntry, ProteinSummary> {//extends RepresentationModelAssemblerSupport<ProteinGroupEntry, ProteinSummary> {

    private static final int LIMIT = 10;
    private static final String PROTEIN_STRUCTURE = "protein structure";
    private static final String REACTIONS = "reactions";
    private static final String PATHWAYS = "pathways";
    private static final String SMALL_MOLECULES = "small molecules";
    private static final String DISEASES = "diseases";
    private static final String LITERATURE = "literature";
    private static final String PROTEIN = "protein";

    @Override
    public ProteinSummary toModel(ProteinGroupEntry p) {

        ProteinSummary model = toProtein(p);
        model.add(linkTo(methodOn(ProteinController.class).findProteinByAccession(p.getPrimaryAccession())).withRel(PROTEIN));
        model.add(linkTo(methodOn(ProteinController.class).findProteinStructureByAccession(p.getPrimaryAccession())).withRel(PROTEIN_STRUCTURE));
        model.add(linkTo(methodOn(ProteinController.class).findReactionsByAccession(p.getPrimaryAccession(), LIMIT)).withRel(REACTIONS));
        model.add(linkTo(methodOn(ProteinController.class).findPathwaysByAccession(p.getPrimaryAccession())).withRel(PATHWAYS));
        model.add(linkTo(methodOn(ProteinController.class).findSmallmoleculesByAccession(p.getPrimaryAccession())).withRel(SMALL_MOLECULES));
        model.add(linkTo(methodOn(ProteinController.class).findDiseasesByAccession(p.getPrimaryAccession())).withRel(DISEASES));
        model.add(linkTo(methodOn(ProteinController.class).findCitationsByAccession(p.getPrimaryAccession(), LIMIT)).withRel(LITERATURE));
        return model;
    }

    public ProteinSummary toProtein(ProteinGroupEntry entry) {

        return ProteinSummary.builder()
                .accession(entry.getPrimaryAccession())
                .organismName(entry.getPrimaryOrganism())
                .proteinName(entry.getProteinName())
                .diseases(entry.getdiseases())
                .function(entry.getFunction())
                .geneName(entry.getGeneName())
                .pdbAccession(entry.getPrimaryImage())
                .synonyms(entry.getSynonym())
                .catalyticActivities(entry.getCatalyticActivities())
                .ecNumbers(entry.getEc())
                .build();
    }

    @Override
    public CollectionModel<ProteinSummary> toCollectionModel(Iterable<? extends ProteinGroupEntry> proteins) {

        List<ProteinSummary> models
                = StreamSupport
                        .stream(proteins.spliterator(), false)
                        .map(p -> toProtein(p))
                        .collect(Collectors.toList());

        return CollectionModel.of(models);
    }

}
