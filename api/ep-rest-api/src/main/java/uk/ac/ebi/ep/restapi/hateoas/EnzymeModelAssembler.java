package uk.ac.ebi.ep.restapi.hateoas;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.stereotype.Component;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeEntry;
import uk.ac.ebi.ep.restapi.controller.EnzymeController;
import uk.ac.ebi.ep.restapi.model.EnzymeModel;

/**
 *
 * @author joseph
 */
@Component
public class EnzymeModelAssembler implements RepresentationModelAssembler<EnzymeEntry, EnzymeModel> {

    private static final int LIMIT = 10;
    private static final String ASSOCIATED_PROTEINS = "associated Proteins";
    private static final int START = 0;
    private static final String ENZYMES = "enzymes";

    @Autowired
    private ProteinModelAssembler proteinModelAssembler;

    @Override
    public EnzymeModel toModel(EnzymeEntry enzyme) {
        EnzymeModel model = buildEnzymeModel(enzyme);
        Class<EnzymeController> controllerClass = EnzymeController.class;
        model.add(linkTo(methodOn(controllerClass).findEnzymeByEcNumber(enzyme.getEc())).withSelfRel());
        model.add(linkTo(methodOn(controllerClass).enzymes(START, LIMIT)).withRel(ENZYMES));
        model.add(linkTo(methodOn(controllerClass).findAssociatedProteinsByEcNumber(enzyme.getEc(), LIMIT)).withRel(ASSOCIATED_PROTEINS));

        return model;
    }

    @Override
    public CollectionModel<EnzymeModel> toCollectionModel(Iterable<? extends EnzymeEntry> enzymes) {
        List<EnzymeModel> models = StreamSupport
                .stream(enzymes.spliterator(), false)
                .map(enzyme -> toModel(enzyme))
                .collect(Collectors.toList());
        return CollectionModel.of(models);
    }

    private EnzymeModel buildEnzymeModel(EnzymeEntry enzyme) {
        return EnzymeModel.builder()
                .ecNumber(enzyme.getEc())
                .enzymeName(enzyme.getEnzymeName())
                .enzymeFamily(enzyme.getEnzymeFamily())
                .alternativeNames(enzyme.getAltNames())
                .catalyticActivities(enzyme.getCatalyticActivities())
                .associatedProteins(proteinModelAssembler.toCollectionModel(enzyme.getProteinGroupEntry()))
                .build();
    }
}
