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

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uk.ac.ebi.ep.indexservice.helper.IndexFields;
import uk.ac.ebi.ep.indexservice.helper.IndexQueryType;
import uk.ac.ebi.ep.indexservice.helper.QueryBuilder;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeEntry;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeSearchResult;
import uk.ac.ebi.ep.indexservice.model.protein.ProteinGroupEntry;
import uk.ac.ebi.ep.indexservice.model.protein.ProteinGroupSearchResult;

/**
 *
 * @author joseph
 */
@Service
public class ApiIndexServiceImpl implements ApiIndexService {

    private static final String ENTRY_TYPE_ASC = "entry_type:ascending";
    private static final String FORMAT = "json";
    private static final int DEFAULT_PROTEIN_SIZE = 2;
    private final EnzymeCentricService enzymeCentricService;
    private final ProteinCentricService proteinCentricService;

    @Autowired
    public ApiIndexServiceImpl(EnzymeCentricService enzymeCentricService, ProteinCentricService proteinCentricService) {
        this.enzymeCentricService = enzymeCentricService;
        this.proteinCentricService = proteinCentricService;
    }

    @Override
    public int getEnzymeHitCount(QueryBuilder enzymeQueryBuilder) {
        return enzymeCentricService.searchForEnzymes(enzymeQueryBuilder).getHitCount();
    }

    @Override
    public int getProteinHitCount(QueryBuilder enzymeQueryBuilder) {
        return proteinCentricService.searchForProteins(enzymeQueryBuilder).getHitCount();
    }

    private Flux<EnzymeEntry> enzymes(QueryBuilder enzymeQueryBuilder) {
        return enzymeCentricService.findEnzymes(enzymeQueryBuilder);
    }

    @Override
    public Flux<EnzymeEntry> getEnzyme(QueryBuilder enzymeQueryBuilder) {

        return enzymes(enzymeQueryBuilder)
                .flatMap(this::proteinToEnzymeEntry);

    }

    @Override
    public EnzymeEntry getEnzymeEntry(QueryBuilder enzymeQueryBuilder) {

        return enzymes(enzymeQueryBuilder)
                .flatMap(this::proteinToEnzymeEntry)
                .blockLast();
    }

    @Override
    public List<EnzymeEntry> getEnzymeEntries(QueryBuilder enzymeQueryBuilder) {

        return enzymes(enzymeQueryBuilder)
                .flatMap(this::proteinToEnzymeEntry)
                .collectList()
                .block();

    }

    @Override
    public Flux<EnzymeEntry> getEnzymes(QueryBuilder enzymeQueryBuilder, String searchTerm) {

        return enzymes(enzymeQueryBuilder)
                .flatMap(enzymeEntry -> proteinToEnzymeEntry(enzymeEntry, searchTerm));

    }

    @Override
    public Flux<EnzymeEntry> getEnzymes(QueryBuilder enzymeQueryBuilder, String resourceId, IndexQueryType resourceQueryType) {

        return enzymes(enzymeQueryBuilder)
                .flatMap(enzymeEntry -> proteinToEnzymeEntry(enzymeEntry, resourceId, resourceQueryType));

    }

    @Override
    public Flux<ProteinGroupEntry> proteinsByEc(String ec, int limit) {

        String query = String.format("%s%s", IndexQueryType.EC.getQueryType(), ec);

        List<String> fieldList = Arrays.asList(IndexFields.id.name(), IndexFields.name.name(),
                IndexFields.primary_accession.name(), IndexFields.primary_organism.name());
        QueryBuilder queryBuilder = QueryBuilder
                .builder()
                .query(query)
                .start(0)
                .size(limit)
                .fields(fieldList)
                .sort(ENTRY_TYPE_ASC)
                .reverse(Boolean.FALSE)
                .format(FORMAT)
                .build();

        return proteinCentricService.findProteins(queryBuilder);

    }

    @Override
    public Flux<ProteinGroupEntry> proteinResult(QueryBuilder queryBuilder) {
        return proteinCentricService.findProteins(queryBuilder);
    }

    @Override
    public List<ProteinGroupEntry> getProteinGroupEntry(QueryBuilder queryBuilder) {
        return proteinCentricService.findProteins(queryBuilder).collectList().block();
    }

    @Override
    public Mono<EnzymeSearchResult> enzymeSearchResult(QueryBuilder queryBuilder) {
        return enzymeCentricService.enzymeSearchResult(queryBuilder);
    }

    @Override
    public Mono<ProteinGroupSearchResult> proteinGroupSearchResult(QueryBuilder queryBuilder) {
        return proteinCentricService.proteinGroupSearchResult(queryBuilder);
    }

    private Flux<EnzymeEntry> proteinToEnzymeEntry(EnzymeEntry entry, String searchTerm) {
        List<String> fieldList = IndexFields.defaultFieldList(false);
        String query = String.format("%s AND %s%s", searchTerm, IndexQueryType.EC.getQueryType(), entry.getEc());

        QueryBuilder queryBuilder = QueryBuilder
                .builder()
                .query(query)
                .start(0)
                .size(DEFAULT_PROTEIN_SIZE)
                .fields(fieldList)
                .sort(ENTRY_TYPE_ASC)
                .reverse(Boolean.FALSE)
                .format(FORMAT)
                .build();
        return proteinCentricService.proteinGroupSearchResult(queryBuilder)
                .map(proteinEntry -> buildEnzymeEntry(proteinEntry, entry))
                .flux();

    }

    private Flux<EnzymeEntry> proteinToEnzymeEntry(EnzymeEntry entry, String resourceId, IndexQueryType resourceQueryType) {
        List<String> fieldList = IndexFields.defaultFieldList(false);

        String query = String.format("%s%s AND %s%s", resourceQueryType.getQueryType(), resourceId, IndexQueryType.EC.getQueryType(), entry.getEc());

        QueryBuilder queryBuilder = QueryBuilder
                .builder()
                .query(query)
                .start(0)
                .size(DEFAULT_PROTEIN_SIZE)
                .fields(fieldList)
                .sort(ENTRY_TYPE_ASC)
                .reverse(Boolean.FALSE)
                .format(FORMAT)
                .build();
        return proteinCentricService.proteinGroupSearchResult(queryBuilder)
                .map(proteinEntry -> buildEnzymeEntry(proteinEntry, entry))
                .flux();

    }

    private Mono<EnzymeEntry> proteinToEnzymeEntry(EnzymeEntry entry) {
        List<String> fieldList = IndexFields.defaultFieldList(false);
        String query = String.format("%s%s", IndexQueryType.EC.getQueryType(), entry.getEc());

        QueryBuilder queryBuilder = QueryBuilder
                .builder()
                .query(query)
                .start(0)
                .size(DEFAULT_PROTEIN_SIZE)
                .fields(fieldList)
                .sort(ENTRY_TYPE_ASC)
                .reverse(Boolean.FALSE)
                .format(FORMAT)
                .build();
        return proteinCentricService.proteinGroupSearchResult(queryBuilder)
                .map(proteinEntry -> buildEnzymeEntry(proteinEntry, entry));

    }

    private EnzymeEntry buildEnzymeEntry(ProteinGroupSearchResult result, EnzymeEntry entry) {
        int proteinHits = result.getHitCount();
        if (proteinHits > 0) {
            entry.setProteinGroupEntry(result.getEntries());
            entry.setNumProteins(result.getHitCount());
            entry.setNumEnzymeHits(result.getHitCount());

        }
        return entry;
    }

}
