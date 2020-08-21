package uk.ac.ebi.ep.restapi.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.hateoas.CollectionModel;
import uk.ac.ebi.ep.indexservice.model.protein.ProteinGroupEntry;
import uk.ac.ebi.ep.restapi.model.ProteinModel;

/**
 *
 * @author joseph
 */
public final class ProteinUtil {

    private ProteinUtil() {
    }

    public static ProteinModel toProteinModel(ProteinGroupEntry entry) {
        return ProteinModel.builder()
                .accession(entry.getPrimaryAccession())
                .organismName(entry.getPrimaryOrganism())
                .proteinName(entry.getProteinName())
                .build();
    }

    public static CollectionModel<ProteinModel> toCollectionModel(List<ProteinGroupEntry> entries) {
        return StreamSupport
                .stream(entries.spliterator(), false)
                .map(model -> toProteinModel(model))
                .collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));

    }

}
