package uk.ac.ebi.ep.indexservice.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uk.ac.ebi.ep.indexservice.helper.QueryBuilder;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeEntry;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeSearchResult;

/**
 *
 * @author joseph
 */
public interface EnzymeCentricService {

    /**
     *
     * @param queryBuilder builds the request parameters
     * @return EnzymeSearchResult
     */
    EnzymeSearchResult searchForEnzymes(QueryBuilder queryBuilder);

    Mono<EnzymeSearchResult> enzymeSearchResult(QueryBuilder queryBuilder);

    Flux<EnzymeEntry> findEnzymes(QueryBuilder queryBuilder);

}
