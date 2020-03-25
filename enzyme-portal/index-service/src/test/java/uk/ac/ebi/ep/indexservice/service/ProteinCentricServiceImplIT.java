package uk.ac.ebi.ep.indexservice.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.ebi.ep.indexservice.IndexServiceApplicationTests;
import uk.ac.ebi.ep.indexservice.helper.IndexFields;
import uk.ac.ebi.ep.indexservice.helper.IndexQueryType;
import uk.ac.ebi.ep.indexservice.helper.QueryBuilder;
import uk.ac.ebi.ep.indexservice.model.protein.ProteinGroupSearchResult;

/**
 *
 * @author joseph
 */
@Slf4j
public class ProteinCentricServiceImplIT extends IndexServiceApplicationTests {

    @Autowired
    private ProteinCentricService proteinCentricService;

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
                .fields(Arrays.asList(IndexFields.id.name(), IndexFields.name.name(), IndexFields.common_name.name(), IndexFields.primary_accession.name(), IndexFields.primary_organism.name(), IndexFields.entry_type.name(), IndexFields.enzyme_family.name(), IndexFields.catalytic_activity.name()))
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
                .fields(Arrays.asList(IndexFields.id.name(), IndexFields.name.name(), IndexFields.common_name.name(), IndexFields.primary_accession.name(), IndexFields.primary_organism.name(), IndexFields.entry_type.name(), IndexFields.enzyme_family.name(), IndexFields.catalytic_activity.name(),
                        IndexFields.with_cofactor.name(), IndexFields.with_taxonomy.name(), IndexFields.with_disease.name(), IndexFields.with_protein_family.name(), IndexFields.with_metabolite.name()))
                .sort("_relevance")
                .reverse(Boolean.TRUE)
                .format("json")
                .build();
    }

    /**
     * Test of searchForProteins method, of class ProteinCentricServiceImpl.
     */
    @Test
    public void testSearchForProteins() {
        log.info("testSearchForProteins");

        String term = "sildenafil";

        String searchTerm = String.format("%s %s", IndexQueryType.KEYWORD.getQueryType(), term);

        QueryBuilder queryBuilder = defaultQueryBuilder(searchTerm.trim());

        ProteinGroupSearchResult result = proteinCentricService.searchForProteins(queryBuilder);
        assertNotNull(result);
        assertThat(result.getEntries(), hasSize(greaterThanOrEqualTo(1)));
        assertThat(result.getFacets(), hasSize(greaterThanOrEqualTo(1)));

    }

    @Test
    public void testSearchAssociatedProteins_with_ec() {
        log.info("testSearchAssociatedProteins_with_ec");

        String term = "sildenafil";
        String ec = "3.1.4.35";

        String searchTerm = String.format("%s AND %s%s", term, IndexQueryType.EC.getQueryType(), ec);

        QueryBuilder queryBuilder = defaultQueryBuilder(searchTerm);

        ProteinGroupSearchResult result = proteinCentricService.searchForProteins(queryBuilder);

        assertNotNull(result);
        assertThat(result.getEntries(), hasSize(greaterThanOrEqualTo(1)));
        assertThat(result.getFacets(), hasSize(greaterThanOrEqualTo(1)));

    }

    @Test
    public void testProteinResult_with_facet_taxonomy() {
        log.info("testProteinResult_with_facet_taxonomy");

        String term = "pyruvate kinase";
        String ec = "2.7.11.2";

        String query = String.format("%s AND %s%s", term, IndexQueryType.EC.getQueryType(), ec);

        QueryBuilder defaultQueryBuilder = defaultQueryBuilder(query);
        ProteinGroupSearchResult noFacetResult = proteinCentricService.searchForProteins(defaultQueryBuilder);

        assertNotNull(noFacetResult);
        assertThat(noFacetResult.getEntries(), hasSize(greaterThanOrEqualTo(1)));
        assertThat(noFacetResult.getFacets(), hasSize(greaterThanOrEqualTo(1)));

        int taxFacet = noFacetResult.getFacets().stream()
                .filter(h -> h.getId().equalsIgnoreCase("TAXONOMY"))
                .findAny().get().getFacetValues().stream()
                .filter(h -> h.getLabel().equalsIgnoreCase("Homo sapiens"))
                .findAny().get().getCount();

        assertEquals(4, taxFacet);

        List<String> facetList = Arrays.asList("TAXONOMY:9606");
        String facets = facetList.stream().collect(Collectors.joining(","));

        QueryBuilder queryBuilder = QueryBuilder
                .builder()
                .query(query)
                .facetcount(1000)
                .facets(facets)
                .start(0)
                .size(10)
                .fields(Arrays.asList(IndexFields.id.name(), IndexFields.name.name(), IndexFields.common_name.name(), IndexFields.primary_accession.name(), IndexFields.primary_organism.name(), IndexFields.entry_type.name(), IndexFields.enzyme_family.name(), IndexFields.catalytic_activity.name()))
                .sort("_relevance")
                .reverse(Boolean.TRUE)
                .format("json")
                .build();

        ProteinGroupSearchResult result = proteinCentricService.searchForProteins(queryBuilder);

        assertNotNull(result);
        assertThat(result.getEntries(), hasSize(greaterThanOrEqualTo(1)));
        assertThat(result.getFacets(), hasSize(greaterThanOrEqualTo(1)));

        int numFacets = result.getFacets().stream()
                .filter(h -> h.getId().equalsIgnoreCase("TAXONOMY"))
                .findAny().get().getFacetValues().stream()
                .filter(h -> h.getLabel().equalsIgnoreCase("Homo sapiens"))
                .findAny().get().getCount();

        assertEquals(4, numFacets);
        assertEquals(4, result.getHitCount().longValue());

    }

    @Test
    public void testProteinResult_with_facet_enzyme_family() {
        log.info("testProteinResult_with_facet_enzyme_family");

        String term = "pyruvate kinase";
        String ec = "2.7.1.40";

        String query = String.format("%s AND %s%s", term, IndexQueryType.EC.getQueryType(), ec);

        QueryBuilder defaultQueryBuilder = defaultQueryBuilder(query);
        ProteinGroupSearchResult noFacetResult = proteinCentricService.searchForProteins(defaultQueryBuilder);

        assertNotNull(noFacetResult);
        assertThat(noFacetResult.getEntries(), hasSize(greaterThanOrEqualTo(1)));
        assertThat(noFacetResult.getFacets(), hasSize(greaterThanOrEqualTo(1)));

        int taxFacet = noFacetResult.getFacets().stream()
                .filter(h -> h.getId().equalsIgnoreCase("enzyme_family"))
                .findAny().get().getFacetValues().stream()
                .filter(h -> h.getLabel().equalsIgnoreCase("Transferases"))
                .findAny().get().getCount();

        assertEquals(47, taxFacet);

        //filter by human
        List<String> facetList = Arrays.asList("TAXONOMY:9606");
        String facets = facetList.stream().collect(Collectors.joining(","));

        QueryBuilder queryBuilder = QueryBuilder
                .builder()
                .query(query)
                .facetcount(1000)
                .facets(facets)
                .start(0)
                .size(10)
                .fields(Arrays.asList(IndexFields.id.name(), IndexFields.name.name(), IndexFields.common_name.name(), IndexFields.primary_accession.name(), IndexFields.primary_organism.name(), IndexFields.entry_type.name(), IndexFields.enzyme_family.name(), IndexFields.catalytic_activity.name()))
                .sort("_relevance")
                .reverse(Boolean.TRUE)
                .format("json")
                .build();

        ProteinGroupSearchResult result = proteinCentricService.searchForProteins(queryBuilder);

        assertNotNull(result);
        assertThat(result.getEntries(), hasSize(greaterThanOrEqualTo(1)));
        assertThat(result.getFacets(), hasSize(greaterThanOrEqualTo(1)));

        long numFacets = result.getFacets().stream()
                .filter(h -> h.getId().equalsIgnoreCase("enzyme_family"))
                .findFirst().get().getFacetValues().stream()
                .filter(h -> h.getLabel().equalsIgnoreCase("Transferases"))
                .findAny().get().getCount();

        assertEquals(3, numFacets);
        assertEquals(3, result.getHitCount().longValue());

    }

    @Test
    public void test_AssociatedProteinResult_with_facet_cofactor() {
        log.info("test_AssociatedProteinResult_with_facet_cofactor");

        String ec = "2.4.1.304";
        String chebiId = "CHEBI:49807";

        List<String> facetList = new ArrayList<>();
        String cofactorId = chebiId.replace("CHEBI:", "");

        String associatedQuery = String.format("%s%s AND %s%s", IndexQueryType.COFACTOR.getQueryType(), cofactorId, IndexQueryType.EC.getQueryType(), ec);

        QueryBuilder defaultQueryBuilder = defaultQueryBuilder(associatedQuery, facetList);
        ProteinGroupSearchResult result = proteinCentricService.searchForProteins(defaultQueryBuilder);

        assertNotNull(result);
        assertThat(result.getEntries(), hasSize(greaterThanOrEqualTo(1)));
        assertThat(result.getFacets(), hasSize(greaterThanOrEqualTo(1)));

    }

    @Test
    public void test_AssociatedProteinResult_with_facet_Metabolite() {
        log.info("test_AssociatedProteinResult_with_facet_Metabolite");

        String ec = "6.5.1.5";
        String chebiId = "CHEBI:37565";

        List<String> facetList = new ArrayList<>();

        String chebiIdSuffix = chebiId.replace("CHEBI:", "");
        String metaboliteId = String.format("MTBLC%s", chebiIdSuffix);

        String associatedQuery = String.format("%s%s AND %s%s", IndexQueryType.METABOLIGHTS.getQueryType(), metaboliteId, IndexQueryType.INTENZ.getQueryType(), ec);

        QueryBuilder defaultQueryBuilder = defaultQueryBuilder(associatedQuery, facetList);

        ProteinGroupSearchResult result = proteinCentricService.searchForProteins(defaultQueryBuilder);

        assertNotNull(result);
        assertThat(result.getEntries(), hasSize(greaterThanOrEqualTo(1)));
        assertThat(result.getFacets(), hasSize(greaterThanOrEqualTo(1)));

    }

    public static String escape(String term) {
        return "\"" + term + "\"";
    }

}
