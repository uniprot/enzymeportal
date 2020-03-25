package uk.ac.ebi.ep.indexservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.ebi.ep.indexservice.IndexServiceApplicationTests;
import uk.ac.ebi.ep.indexservice.helper.IndexFields;
import uk.ac.ebi.ep.indexservice.helper.IndexQueryType;
import uk.ac.ebi.ep.indexservice.helper.QueryBuilder;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeSearchResult;

/**
 *
 * @author joseph
 */
public class EnzymeCentricServiceTest extends IndexServiceApplicationTests {

    @Autowired
    private EnzymeCentricService enzymeCentricService;

    private QueryBuilder queryBuilder(String searchTerm) {
        List<String> facetList = new ArrayList<>();
        String facets = facetList.stream().collect(Collectors.joining(","));

        return QueryBuilder
                .builder()
                .query(searchTerm)
                .facetcount(10)
                .facets(facets)
                .start(0)
                .size(10)
                .fields(IndexFields.defaultFieldList(false))
                .sort("_relevance")
                .reverse(Boolean.TRUE)
                .format("json")
                .build();
    }

    /**
     * Test of searchForEnzymes method, of class EnzymeCentricService.
     */
    @org.junit.jupiter.api.Test
    public void testSearchForEnzymesByKwywordSearch() {
        String term = "human";

        String searchTerm = String.format("%s %s", IndexQueryType.KEYWORD.getQueryType(), term);

        QueryBuilder queryBuilder = queryBuilder(searchTerm);
        EnzymeSearchResult result = enzymeCentricService.searchForEnzymes(queryBuilder);

        assertNotNull(result);
        assertThat(result.getEntries(), hasSize(greaterThanOrEqualTo(1)));
        assertThat(result.getFacets(), hasSize(greaterThanOrEqualTo(1)));

    }

    @org.junit.jupiter.api.Test
    public void testBrowseDisease() {
        String omim = "114480";

        String query = String.format("%s%s", IndexQueryType.OMIM.getQueryType(), omim);

        QueryBuilder queryBuilder = queryBuilder(query);
        EnzymeSearchResult result = enzymeCentricService.searchForEnzymes(queryBuilder);
        result.getEntries().forEach(x -> System.out.println("OMIM " + x.toString()));
        assertNotNull(result);
        assertThat(result.getEntries(), hasSize(greaterThanOrEqualTo(1)));
        assertThat(result.getFacets(), hasSize(greaterThanOrEqualTo(1)));

    }

}
