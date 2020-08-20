package uk.ac.ebi.ep.indexservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uk.ac.ebi.ep.indexservice.config.IndexProperties;
import uk.ac.ebi.ep.indexservice.helper.QueryBuilder;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeEntry;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeSearchResult;
import uk.ac.ebi.ep.restclient.service.RestConfigService;

/**
 *
 * @author joseph
 */
@Slf4j
@Service
public class EnzymeCentricServiceImpl extends AbstractIndexService<EnzymeSearchResult> implements EnzymeCentricService {

    private final IndexProperties indexProperties;
    private final RestConfigService restConfigService;

    @Autowired
    public EnzymeCentricServiceImpl(RestConfigService restConfigService, IndexProperties indexProperties) {
        this.restConfigService = restConfigService;
        this.indexProperties = indexProperties;

    }

    @Override
    public EnzymeSearchResult searchForEnzymes(QueryBuilder queryBuilder) {
        String indexUrl = indexProperties.getBaseUrl() + indexProperties.getEnzymeCentricUrl();

        return searchIndex(restConfigService.getRestTemplate(), queryBuilder, indexUrl, EnzymeSearchResult.class);

    }

    @Override
    public Mono<EnzymeSearchResult> enzymeSearchResult(QueryBuilder queryBuilder) {
        String indexUrl = indexProperties.getBaseUrl() + indexProperties.getEnzymeCentricUrl();
        return searchIndex(restConfigService.getWebClient(), queryBuilder, indexUrl, EnzymeSearchResult.class);

    }

    @Override
    public Flux<EnzymeEntry> findEnzymes(QueryBuilder queryBuilder) {
        String indexUrl = indexProperties.getBaseUrl() + indexProperties.getEnzymeCentricUrl();

        return indexSearch(restConfigService.getWebClient(), queryBuilder, indexUrl, EnzymeSearchResult.class)
                .flatMapIterable(EnzymeSearchResult::getEntries);

    }
}
