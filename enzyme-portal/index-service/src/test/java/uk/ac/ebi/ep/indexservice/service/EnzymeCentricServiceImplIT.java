package uk.ac.ebi.ep.indexservice.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import uk.ac.ebi.ep.indexservice.IndexServiceApplicationTests;
import uk.ac.ebi.ep.indexservice.helper.IndexFields;
import uk.ac.ebi.ep.indexservice.helper.IndexQueryType;
import uk.ac.ebi.ep.indexservice.helper.QueryBuilder;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeSearchResult;

/**
 *
 * @author joseph
 */
@Slf4j
public class EnzymeCentricServiceImplIT extends IndexServiceApplicationTests {

    @Autowired
    private EnzymeCentricService enzymeCentricService;

    private QueryBuilder defaultQueryBuilder(String searchTerm) {
        List<String> facetList = new ArrayList<>();
        String facets = facetList.stream().collect(Collectors.joining(","));

        return QueryBuilder
                .builder()
                .query(searchTerm)
                .facetcount(10)
                .facets(facets)
                .start(0)
                .size(10)
                .fields(Arrays.asList(IndexFields.id.name(), IndexFields.name.name(), IndexFields.description.name(), IndexFields.enzyme_family.name(), IndexFields.catalytic_activity.name()))
                .sort("_relevance")
                .reverse(Boolean.TRUE)
                .format("json")
                .build();
    }

    private QueryBuilder defaultQueryBuilder(String searchTerm, List<String> facetList) {

        String facets = facetList.stream().collect(Collectors.joining(","));

        return QueryBuilder
                .builder()
                .query(searchTerm)
                .facetcount(10)
                .facets(facets)
                .start(0)
                .size(10)
                .fields(Arrays.asList(IndexFields.id.name(), IndexFields.name.name(), IndexFields.description.name(), IndexFields.enzyme_family.name(), IndexFields.catalytic_activity.name()))
                .sort("_relevance")
                .reverse(Boolean.TRUE)
                .format("json")
                .build();
    }

    private QueryBuilder defaultQueryBuilderNoFacet(String searchTerm) {

        return QueryBuilder
                .builder()
                .query(searchTerm)
                .facetcount(0)
                .start(0)
                .size(10)
                .fields(Arrays.asList(IndexFields.id.name(), IndexFields.name.name(), IndexFields.description.name(), IndexFields.enzyme_family.name(), IndexFields.catalytic_activity.name()))
                .sort("_relevance")
                .reverse(Boolean.TRUE)
                .format("json")
                .build();
    }

    @Test
    public void testSearchForEnzymesNo_facet() {
        log.info("testSearchForEnzymes");

        String term = "1.1.1.1";

        String searchTerm = String.format("%s %s", IndexQueryType.KEYWORD.getQueryType(), term);

        QueryBuilder queryBuilder = defaultQueryBuilderNoFacet(searchTerm);

        EnzymeSearchResult result = enzymeCentricService.searchForEnzymes(queryBuilder);

        assertNotNull(result);
        assertThat(result.getEntries(), hasSize(greaterThanOrEqualTo(1)));
        assertThat(result.getFacets(), hasSize(0));

    }

    /**
     * Test of searchForEnzymes method, of class EnzymeCentricServiceImpl.
     */
    @Test
    public void testSearchForEnzymes() {
        log.info("testSearchForEnzymes");

        String term = "1.1.1.1";

        String searchTerm = String.format("%s %s", IndexQueryType.KEYWORD.getQueryType(), term);

        QueryBuilder queryBuilder = defaultQueryBuilder(searchTerm);

        EnzymeSearchResult result = enzymeCentricService.searchForEnzymes(queryBuilder);

        assertNotNull(result);
        assertThat(result.getEntries(), hasSize(greaterThanOrEqualTo(1)));
        assertThat(result.getFacets(), hasSize(greaterThanOrEqualTo(1)));

    }

    @Test
    public void testSearchForEnzymes_with_empty_searchTerm() {
        log.info("testSearchForEnzymes_with_empty_searchTerm");

        String term = " ";

        String searchTerm = String.format("%s %s", IndexQueryType.KEYWORD.getQueryType(), term);

        QueryBuilder queryBuilder = defaultQueryBuilder(searchTerm);

        EnzymeSearchResult result = enzymeCentricService.searchForEnzymes(queryBuilder);

        assertNotNull(result);

        assertThat(result.getEntries(), hasSize(0));
        assertThat(result.getFacets(), hasSize(lessThanOrEqualTo(0)));

    }

    /**
     * Test of searchForEnzymes method, of class EnzymeCentricServiceImpl.
     */
    @Test
    public void testSearchForEnzymes_Reactive() {
        log.info("testSearchForEnzymes_Reactive");

        String term = "1.1.1.1";

        String searchTerm = String.format("%s %s", IndexQueryType.KEYWORD.getQueryType(), term);

        QueryBuilder queryBuilder = defaultQueryBuilder(searchTerm);

        Mono<EnzymeSearchResult> result = enzymeCentricService.searchForEnzymesNonBlocking(queryBuilder);

        assertNotNull(result);
        result.subscribe(data -> log.info("Result : " + data.getEntries()));
        assertThat(result.blockOptional().orElse(new EnzymeSearchResult()).getEntries(), hasSize(greaterThanOrEqualTo(1)));
        assertThat(result.blockOptional().orElse(new EnzymeSearchResult()).getFacets(), hasSize(greaterThanOrEqualTo(1)));

    }

    @Test
    public void testSearchForEnzymes_by_Cofactor() {
        log.info("testSearchForEnzymes_by_Cofactor");
        String chebiId = "CHEBI:49807";

        String chebiIdSuffix = chebiId.replace("CHEBI:", "");
        String cofactor = String.format("cofactor%s", chebiIdSuffix);
        String query = String.format("%s%s", IndexQueryType.COFACTOR.getQueryType(), cofactor);

        List<String> facetList = new ArrayList<>();

        QueryBuilder queryBuilder = defaultQueryBuilder(query, facetList);

        EnzymeSearchResult result = enzymeCentricService.searchForEnzymes(queryBuilder);

        assertNotNull(result);
        assertThat(result.getEntries(), hasSize(greaterThanOrEqualTo(1)));
        assertThat(result.getFacets(), hasSize(greaterThanOrEqualTo(1)));

        result.getEntries().forEach(x -> log.info(" With Cofactor " + x.getEnzymeName() + " " + x.getEc() + " " + x));
    }

    @Test
    public void testSearchForEnzymes_by_Metabolite() {
        log.info("testSearchForEnzymes_by_Metabolite");
        String chebiId = "CHEBI:37565";

        String chebiIdSuffix = chebiId.replace("CHEBI:", "");

        String metaboliteId = String.format("MTBLC%s", chebiIdSuffix);

        String query = String.format("%s%s", IndexQueryType.METABOLIGHTS.getQueryType(), metaboliteId);

        List<String> facetList = new ArrayList<>();

        QueryBuilder queryBuilder = defaultQueryBuilder(query, facetList);

        EnzymeSearchResult result = enzymeCentricService.searchForEnzymes(queryBuilder);

        assertNotNull(result);
        assertThat(result.getEntries(), hasSize(greaterThanOrEqualTo(1)));
        assertThat(result.getFacets(), hasSize(greaterThanOrEqualTo(1)));

        result.getEntries().forEach(x -> log.info(" With Metabolite " + x.getEnzymeName() + " " + x.getEc() + " " + x));
    }

    public static String escape(String term) {
        return "\"" + term + "\"";
    }

}
