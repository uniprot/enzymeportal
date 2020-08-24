package uk.ac.ebi.ep.restapi.hateoas;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import uk.ac.ebi.ep.indexservice.model.protein.ProteinGroupEntry;
import uk.ac.ebi.ep.restapi.controller.ProteinController;
import uk.ac.ebi.ep.restapi.model.ProteinModel;

/**
 *
 * @author joseph
 */
public class ProteinModelAssembler extends RepresentationModelAssemblerSupport<ProteinGroupEntry, ProteinModel> {

    private static final int LIMIT = 10;
    private static final String PROTEIN_STRUCTURE = "protein structure";
    private static final String REACTIONS = "reactions";
    private static final String PATHWAYS = "pathways";
    private static final String SMALL_MOLECULES = "small molecules";
    private static final String DISEASES = "diseases";
    private static final String LITERATURE = "literature";
    private static final String PROTEIN = "protein";

    public ProteinModelAssembler(Class<?> controllerClass, Class<ProteinModel> resourceType) {
        super(controllerClass, resourceType);
    }

    @Override
    public ProteinModel toModel(ProteinGroupEntry protein) {
        ProteinModel model = toProtein(protein);

        model.add(linkTo(methodOn(ProteinController.class).findProteinByAccession(protein.getPrimaryAccession())).withRel(PROTEIN));
        model.add(linkTo(methodOn(ProteinController.class).findProteinStructureByAccession(protein.getPrimaryAccession())).withRel(PROTEIN_STRUCTURE));
        model.add(linkTo(methodOn(ProteinController.class).findReactionsByAccession(protein.getPrimaryAccession(), LIMIT)).withRel(REACTIONS));
        model.add(linkTo(methodOn(ProteinController.class).findPathwaysByAccession(protein.getPrimaryAccession())).withRel(PATHWAYS));
        model.add(linkTo(methodOn(ProteinController.class).findSmallmoleculesByAccession(protein.getPrimaryAccession())).withRel(SMALL_MOLECULES));
        model.add(linkTo(methodOn(ProteinController.class).findDiseasesByAccession(protein.getPrimaryAccession())).withRel(DISEASES));
        model.add(linkTo(methodOn(ProteinController.class).findCitationsByAccession(protein.getPrimaryAccession(), LIMIT)).withRel(LITERATURE));
        return model;

    }

    public List<ProteinModel> toProteinModel(List<ProteinGroupEntry> entries) {
        List<ProteinModel> models = StreamSupport
                .stream(entries.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.toList());
        return models;
    }

    public ProteinModel toProtein(ProteinGroupEntry entry) {

        return ProteinModel.builder()
                .accession(entry.getPrimaryAccession())
                .organismName(entry.getPrimaryOrganism())
                .proteinName(entry.getProteinName())
                .build();
    }

   

}
