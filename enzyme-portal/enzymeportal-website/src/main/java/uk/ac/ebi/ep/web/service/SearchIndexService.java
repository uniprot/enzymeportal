package uk.ac.ebi.ep.web.service;

import java.util.List;
import reactor.core.publisher.Mono;
import uk.ac.ebi.ep.indexservice.helper.IndexQueryType;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeEntry;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeSearchResult;
import uk.ac.ebi.ep.indexservice.model.protein.ProteinGroupSearchResult;

/**
 *
 * @author joseph
 */
public interface SearchIndexService {

    EnzymeSearchResult findEnzyme(String query);

    EnzymeSearchResult findEnzyme(String query, int startPage, int pageSize, int facetCount, List<String> facetList);

    ProteinGroupSearchResult findAssociatedProtein(String query, int pageSize,IndexQueryType resourceQueryType);

    ProteinGroupSearchResult findProteinResult(String query, int startPage, int pageSize, int facetCount, List<String> facetList,IndexQueryType resourceQueryType);

    Mono<EnzymeEntry> getEnzymePageEntry(String query);
}
