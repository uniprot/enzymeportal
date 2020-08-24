package uk.ac.ebi.ep.restapi.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uk.ac.ebi.ep.indexservice.helper.IndexQueryType;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeEntry;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeSearchResult;
import uk.ac.ebi.ep.indexservice.model.protein.ProteinGroupEntry;

/**
 *
 * @author joseph
 */
public interface SearchIndexService {

    Mono<EnzymeSearchResult> findEnzyme(String query, int startPage, int pageSize, int facetCount, List<String> facetList);

    Flux<ProteinGroupEntry> associatedProteinByEc(String ec, int limit);

    Flux<ProteinGroupEntry> associatedProteinSummaryByEc(String ec, int limit);

    Flux<ProteinGroupEntry> findProteinSummaries(String searchTerm, String ec, int start, int pageSize);

    Flux<EnzymeEntry> getEnzyme(String query);

    Flux<EnzymeEntry> getEnzymes(int startPage, int pageSize);

    Page<EnzymeEntry> getEnzymeEntries(Pageable pageable);

    Flux<EnzymeEntry> getEnzymes(String searchTerm, int startPage, int pageSize);

    public Flux<EnzymeEntry> getEnzymes(String searchTerm, String resourceId, IndexQueryType resourceQueryType, int startPage, int pageSize);

    int getEnzymeHitCount(String query);

}
