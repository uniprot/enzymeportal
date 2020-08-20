/*
 * The MIT License
 *
 * Copyright 2020 joseph.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package uk.ac.ebi.ep.indexservice.service;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uk.ac.ebi.ep.indexservice.IndexServiceApplicationTests;
import uk.ac.ebi.ep.indexservice.helper.IndexFields;
import uk.ac.ebi.ep.indexservice.helper.IndexQueryType;
import uk.ac.ebi.ep.indexservice.helper.QueryBuilder;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeEntry;
import uk.ac.ebi.ep.indexservice.model.protein.ProteinGroupEntry;

/**
 *
 * @author joseph
 */
@Slf4j
public class ApiIndexServiceIT extends IndexServiceApplicationTests {

    @Autowired
    private ApiIndexService apiIndexService;

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
    public void testGetEnzymeHitCount() {
        String term = "human";

        String searchTerm = String.format("%s %s", IndexQueryType.KEYWORD.getQueryType(), term);

        QueryBuilder queryBuilder = defaultQueryBuilderNoFacet(searchTerm);
        int hitcount = apiIndexService.getEnzymeHitCount(queryBuilder);
        assertTrue(hitcount > 100);

    }

    @Test
    public void testGetProteinHitCount() {
        String term = "kinase";

        String searchTerm = String.format("%s %s", IndexQueryType.KEYWORD.getQueryType(), term);

        QueryBuilder queryBuilder = defaultQueryBuilderNoFacet(searchTerm);
        int hitcount = apiIndexService.getProteinHitCount(queryBuilder);
        assertTrue(hitcount > 100);
    }

    @Test
    public void testGetEnzyme() {

        String ec = "3.1.4.35";

        String query = String.format("%s%s", IndexQueryType.ID.getQueryType(), ec);

        QueryBuilder queryBuilder = defaultQueryBuilder(query);

        Mono<EnzymeEntry> entry = apiIndexService.getEnzyme(queryBuilder).singleOrEmpty();

        Mono.when(entry).block();
        assertNotNull(entry);
        assertNotNull(entry.block().getProteinGroupEntry());

        assertThat(entry.block().getProteinGroupEntry(), hasSize(greaterThanOrEqualTo(2)));
        entry.doOnNext(e -> log.info("Enzyme " + e + " Associated Proteins : " + e.getProteinGroupEntry())).block();

    }

    @Test
    public void testGetEnzymes() {

        String term = "kinase";

        String searchTerm = String.format("%s %s", IndexQueryType.KEYWORD.getQueryType(), term);

        QueryBuilder queryBuilder = defaultQueryBuilderNoFacet(searchTerm);

        Flux<EnzymeEntry> entry = apiIndexService.getEnzymes(queryBuilder, searchTerm);

        assertNotNull(entry);
        assertThat(entry.collectList().block(), hasSize(greaterThanOrEqualTo(2)));

        entry.doOnNext(e -> log.info("Enzymes : " + e)).blockLast();

        entry.doOnNext(enzyme -> log.info("EC " + enzyme.getEc() + " Enzyme name" + enzyme.getEnzymeName() + " Num proteins" + enzyme.getNumProteins() + " Protein Info " + enzyme.getProteinGroupEntry()))
                .blockLast(Duration.ofSeconds(2));

    }

    @Test
    public void testGetEnzymes_with_resource_id() {

        String chebiId = "CHEBI:57925";

        String chebiIdSuffix = chebiId.replace("CHEBI:", "");
        String cofactor = String.format("cofactor%s", chebiIdSuffix);
        String query = String.format("%s%s", IndexQueryType.COFACTOR.getQueryType(), cofactor);

        QueryBuilder queryBuilder = defaultQueryBuilderNoFacet(query);

        Flux<EnzymeEntry> entry = apiIndexService.getEnzymes(queryBuilder, chebiId, IndexQueryType.COFACTOR);

        assertNotNull(entry);
        assertThat(entry.collectList().block(), hasSize(greaterThanOrEqualTo(2)));

        entry.doOnNext(e -> log.info("Enzymes by Resource Id : " + e)).blockLast();

        entry.doOnNext(enzyme -> log.info("EC " + enzyme.getEc() + " Enzyme name" + enzyme.getEnzymeName() + " Num proteins" + enzyme.getNumProteins() + " Protein Info " + enzyme.getProteinGroupEntry()))
                .blockLast(Duration.ofSeconds(2));

    }

    /**
     * Test of proteinsByEc method, of class ApiIndexService.
     */
    @Test
    public void testProteinsByEc() {

        String ec = "1.1.1.1";
        int limit = 10;

        Flux<ProteinGroupEntry> proteins = apiIndexService.proteinsByEc(ec, limit);
        assertNotNull(proteins);
        assertThat(proteins.collectList().block(), hasSize(greaterThanOrEqualTo(10)));
        proteins.doOnNext(p -> log.info("Protein : " + p.getProteinName() + " Organism : " + p.getPrimaryOrganism())).blockLast();

    }

}
