package uk.ac.ebi.ep.indexservice.helper;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

/**
 *
 * @author joseph
 */
@Builder
@Data
public class QueryBuilder {

    private final String query;
    private final String facets;
    private final String sort;
    private final String format;
    private final int start;
    private final int size;
    private final int facetcount;
    @Singular
    private final List<String> fields;
    private final Boolean reverse;

}
