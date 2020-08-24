package uk.ac.ebi.ep.restapi.hateoas;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeEntry;
import uk.ac.ebi.ep.restapi.controller.EnzymeController;
import uk.ac.ebi.ep.restapi.model.EnzymeModel;
import uk.ac.ebi.ep.restapi.util.ProteinUtil;

/**
 *
 * @author joseph
 */
@Component
public class PaginationModelAssembler implements RepresentationModelAssembler<EnzymeEntry, EnzymeModel> {

    private static final int LIMIT = 10;
    private static final String ENZYME = "enzyme";
    private static final String ASSOCIATED_PROTEINS = "associated Proteins";

    @Override
    public EnzymeModel toModel(EnzymeEntry enzyme) {

        EnzymeModel model = buildEnzymeModel(enzyme);

        model.add(linkTo(methodOn(EnzymeController.class).findEnzymeByEcNumber(enzyme.getEc())).withRel(ENZYME));
        model.add(linkTo(methodOn(EnzymeController.class).findAssociatedProteinsByEcNumber(enzyme.getEc(), LIMIT)).withRel(ASSOCIATED_PROTEINS));
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
                .associatedProteins(ProteinUtil.toCollectionModel(enzyme.getProteinGroupEntry()))
                .build();
    }

}
