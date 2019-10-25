package uk.ac.ebi.ep.indexservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import uk.ac.ebi.ep.indexservice.config.IndexProperties;
import uk.ac.ebi.ep.indexservice.helper.QueryBuilder;
import uk.ac.ebi.ep.indexservice.model.protein.ProteinGroupSearchResult;
import uk.ac.ebi.ep.restclient.service.RestConfigService;

/**
 *
 * @author joseph
 */
@Slf4j
@Service
public class ProteinCentricServiceImpl extends AbstractIndexService<ProteinGroupSearchResult> implements ProteinCentricService {

    private final IndexProperties indexProperties;

    private final RestConfigService restConfigService;


    public ProteinCentricServiceImpl(RestConfigService restConfigService, IndexProperties indexProperties) {
        this.restConfigService = restConfigService;
        this.indexProperties = indexProperties;
    }

  
    @Override
    public ProteinGroupSearchResult searchForProteins(QueryBuilder queryBuilder) {
        String indexUrl = indexProperties.getBaseUrl() + indexProperties.getProteinCentricUrl();

        return searchIndexBlocking(restConfigService.getRestTemplate(), queryBuilder, indexUrl, ProteinGroupSearchResult.class);

    }

    @Override
    public Mono<ProteinGroupSearchResult> searchForProteinsNonBlocking(QueryBuilder queryBuilder) {

        String indexUrl = indexProperties.getBaseUrl() + indexProperties.getProteinCentricUrl();
        return searchIndex(restConfigService.getWebClient(), queryBuilder, indexUrl, ProteinGroupSearchResult.class);
    }

}
