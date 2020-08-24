package uk.ac.ebi.ep.restapi.hateoas;

import javax.validation.constraints.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;
import uk.ac.ebi.ep.restapi.controller.ProteinController;
import uk.ac.ebi.ep.restapi.model.Protein;

/**
 *
 * @author joseph
 */
@Component
public class ProteinResourceAssembler implements RepresentationModelAssembler<Protein, EntityModel<Protein>> {

    private static final int LIMIT = 10;
    private static final String PROTEIN_STRUCTURE = "protein structure";
    private static final String REACTIONS = "reactions";
    private static final String PATHWAYS = "pathways";
    private static final String SMALL_MOLECULES = "small molecules";
    private static final String DISEASES = "diseases";
    private static final String LITERATURE = "literature";

    @Override
    public EntityModel<Protein> toModel(@NotNull Protein protein) {

        return EntityModel.of(protein,
                linkTo(methodOn(ProteinController.class).findProteinByAccession(protein.getAccession())).withSelfRel(),
                linkTo(methodOn(ProteinController.class).findProteinStructureByAccession(protein.getAccession())).withRel(PROTEIN_STRUCTURE),
                linkTo(methodOn(ProteinController.class).findReactionsByAccession(protein.getAccession(), LIMIT)).withRel(REACTIONS),
                linkTo(methodOn(ProteinController.class).findPathwaysByAccession(protein.getAccession())).withRel(PATHWAYS),
                linkTo(methodOn(ProteinController.class).findSmallmoleculesByAccession(protein.getAccession())).withRel(SMALL_MOLECULES),
                linkTo(methodOn(ProteinController.class).findDiseasesByAccession(protein.getAccession())).withRel(DISEASES),
                linkTo(methodOn(ProteinController.class).findCitationsByAccession(protein.getAccession(), LIMIT)).withRel(LITERATURE));
    }

}
