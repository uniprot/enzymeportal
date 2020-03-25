package uk.ac.ebi.ep.indexservice.service;

import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import uk.ac.ebi.ep.indexservice.IndexServiceApplicationTests;
import uk.ac.ebi.ep.indexservice.helper.IndexFields;
import uk.ac.ebi.ep.indexservice.helper.IndexQueryType;
import uk.ac.ebi.ep.indexservice.helper.QueryBuilder;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeEntry;

/**
 *
 * @author joseph
 */
@Slf4j
public class IndexServiceImplTest extends IndexServiceApplicationTests {
    
    @Autowired
    private IndexService indexService;
    
    private QueryBuilder defaultQueryBuilder(String query) {
        List<String> fieldList = Arrays.asList(IndexFields.id.name(), IndexFields.name.name(), IndexFields.alt_names.name(),
                IndexFields.description.name(), IndexFields.enzyme_family.name(), IndexFields.intenz_cofactors.name(), IndexFields.catalytic_activity.name());
        return QueryBuilder
                .builder()
                .query(query)
                .start(0)
                .size(1)
                .fields(fieldList)
                .sort("_relevance")
                .reverse(Boolean.TRUE)
                .format("json")
                .build();
    }

    /**
     * Test of getEnzymeEntries method, of class IndexServiceImpl.
     */
    @Test
    public void testGetEnzymeEntries() {
        String ec = "3.1.4.35";
        String query = String.format("%s%s", IndexQueryType.ID.getQueryType(), ec);
        
        QueryBuilder queryBuilder = defaultQueryBuilder(query);
        
        Mono<EnzymeEntry> entry = indexService.getEnzymePageEntry(queryBuilder);
        
        assertNotNull(entry);
        entry.subscribe(e -> log.info("Enzyme Page  " + e.getEnzymeName() + " Associated Protein : " + e.getProteinGroupEntry()));
        assertThat(entry.blockOptional().orElse(new EnzymeEntry()).getProteinGroupEntry(), hasSize(greaterThanOrEqualTo(1)));
        
    }
    

}
