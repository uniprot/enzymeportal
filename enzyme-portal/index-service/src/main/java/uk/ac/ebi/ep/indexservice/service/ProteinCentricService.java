package uk.ac.ebi.ep.indexservice.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uk.ac.ebi.ep.indexservice.helper.QueryBuilder;
import uk.ac.ebi.ep.indexservice.model.protein.ProteinGroupEntry;
import uk.ac.ebi.ep.indexservice.model.protein.ProteinGroupSearchResult;

/**
 *
 * @author joseph
 */
public interface ProteinCentricService {

    /**
     *
     * @param queryBuilder builds the request parameters
     * @return ProteinGroupSearchResult
     */
    ProteinGroupSearchResult searchForProteins(QueryBuilder queryBuilder);

    Mono<ProteinGroupSearchResult> proteinGroupSearchResult(QueryBuilder queryBuilder);

    Flux<ProteinGroupEntry> findProteins(QueryBuilder queryBuilder);
}
