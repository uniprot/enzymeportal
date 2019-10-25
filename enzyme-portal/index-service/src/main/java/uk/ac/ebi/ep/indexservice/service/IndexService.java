package uk.ac.ebi.ep.indexservice.service;

import java.util.List;
import reactor.core.publisher.Mono;
import uk.ac.ebi.ep.indexservice.helper.QueryBuilder;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeEntry;

/**
 *
 * @author joseph
 */
public interface IndexService {

    Mono<EnzymeEntry> getEnzymePageEntry(QueryBuilder enzymeQueryBuilder);

    Mono<List<EnzymeEntry>> getEnzymeEntries(QueryBuilder enzymeQueryBuilder,String resourceQuery);
    
}
