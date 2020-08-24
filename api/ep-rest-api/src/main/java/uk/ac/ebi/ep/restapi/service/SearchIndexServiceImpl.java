package uk.ac.ebi.ep.restapi.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uk.ac.ebi.ep.indexservice.helper.IndexFields;
import uk.ac.ebi.ep.indexservice.helper.IndexQueryType;
import uk.ac.ebi.ep.indexservice.helper.QueryBuilder;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeEntry;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeSearchResult;
import uk.ac.ebi.ep.indexservice.model.protein.ProteinGroupEntry;
import uk.ac.ebi.ep.indexservice.service.ApiIndexService;

/**
 *
 * @author joseph
 */
@Slf4j
@Service
class SearchIndexServiceImpl implements SearchIndexService {

    private static final String RELEVANCE = "_relevance";
    private static final String ENTRY_TYPE_ASC = "entry_type:ascending";
    private static final String FORMAT = "json";
    private static final String DOMAIN_QUERY = "domain_source:enzymeportal_enzymes";

    private final ApiIndexService apiIndexService;

    @Autowired
    public SearchIndexServiceImpl(ApiIndexService apiIndexService) {
        this.apiIndexService = apiIndexService;
    }

    private Mono<EnzymeSearchResult> enzymeResult(String query, int startPage, int pageSize, int facetCount, List<String> facetList) {

        String facets = facetList.stream().collect(Collectors.joining(","));

        List<String> fieldList = Arrays.asList(IndexFields.id.name(), IndexFields.name.name(), IndexFields.enzyme_family.name(), IndexFields.intenz_cofactors.name());
        QueryBuilder queryBuilder = QueryBuilder
                .builder()
                .query(query)
                .facetcount(facetCount)
                .facets(facets)
                .start(startPage * pageSize)
                .size(pageSize)
                .fields(fieldList)
                .sort(RELEVANCE)
                .reverse(Boolean.TRUE)
                .format(FORMAT)
                .build();

        return apiIndexService.enzymeSearchResult(queryBuilder);

    }

    private QueryBuilder getQueryBuilder(int startPage, int pageSize, String query, List<String> fieldList) {
        return QueryBuilder
                .builder()
                .query(query)
                .start(startPage * pageSize)
                .size(pageSize)
                .fields(fieldList)
                .sort(RELEVANCE)
                .reverse(Boolean.TRUE)
                .format(FORMAT)
                .build();
    }

    @Override
    public Mono<EnzymeSearchResult> findEnzyme(String query, int startPage, int pageSize, int facetCount, List<String> facetList) {

        return enzymeResult(query, startPage, pageSize, facetCount, facetList);
    }

    @Override
    public Page<EnzymeEntry> getEnzymeEntries(Pageable pageable) {

        String query = DOMAIN_QUERY;
        List<String> fieldList = Arrays.asList(IndexFields.id.name(), IndexFields.name.name());

        QueryBuilder queryBuilder = getQueryBuilder(pageable.getPageNumber(), pageable.getPageSize(), query, fieldList);
        int hitCount = apiIndexService.getEnzymeHitCount(queryBuilder);
        List<EnzymeEntry> enzymes = apiIndexService.getEnzymeEntries(queryBuilder);
        return new PageImpl(enzymes, pageable, hitCount);

    }

    @Override
    public Flux<ProteinGroupEntry> associatedProteinByEc(String ec, int limit) {
        return apiIndexService.proteinsByEc(ec, limit);
    }

    @Override
    public Flux<ProteinGroupEntry> associatedProteinSummaryByEc(String ec, int limit) {
        String query = String.format("%s%s", IndexQueryType.EC.getQueryType(), ec);

        List<String> fieldList = Arrays.asList(IndexFields.id.name(), IndexFields.name.name(),
                IndexFields.primary_accession.name(), IndexFields.primary_organism.name(),
                IndexFields.primary_image.name(), IndexFields.function.name(), IndexFields.disease_name.name(), IndexFields.catalytic_activity.name(),
                IndexFields.alt_names.name(), IndexFields.gene_name.name(), IndexFields.ec.name());

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
        return apiIndexService.proteinResult(queryBuilder);
    }

    @Override
    public Flux<ProteinGroupEntry> findProteinSummaries(String searchTerm, String ec, int start, int pageSize) {
        String query = String.format("%s AND %s%s", searchTerm, IndexQueryType.EC.getQueryType(), ec);

        List<String> fieldList = Arrays.asList(IndexFields.id.name(), IndexFields.name.name(),
                IndexFields.primary_accession.name(), IndexFields.primary_organism.name(),
                IndexFields.primary_image.name(), IndexFields.function.name(), IndexFields.disease_name.name(), IndexFields.catalytic_activity.name(),
                IndexFields.alt_names.name(), IndexFields.gene_name.name(), IndexFields.ec.name());

        QueryBuilder queryBuilder = QueryBuilder
                .builder()
                .query(query)
                .start(start)
                .size(pageSize)
                .fields(fieldList)
                .sort(ENTRY_TYPE_ASC)
                .reverse(Boolean.FALSE)
                .format(FORMAT)
                .build();
        return apiIndexService.proteinResult(queryBuilder);
    }

    @Override
    public Flux<EnzymeEntry> getEnzyme(String query) {
        List<String> fieldList = Arrays.asList(IndexFields.id.name(), IndexFields.name.name(), IndexFields.alt_names.name(),
                IndexFields.description.name(), IndexFields.enzyme_family.name(), IndexFields.intenz_cofactors.name(), IndexFields.catalytic_activity.name());
        QueryBuilder queryBuilder = QueryBuilder
                .builder()
                .query(query)
                .start(0)
                .size(1)
                .fields(fieldList)
                .sort(RELEVANCE)
                .reverse(Boolean.TRUE)
                .format(FORMAT)
                .build();

        return apiIndexService.getEnzyme(queryBuilder);

    }

    @Override
    public Flux<EnzymeEntry> getEnzymes(int startPage, int pageSize) {
        String query = DOMAIN_QUERY;
        List<String> fieldList = Arrays.asList(IndexFields.id.name(), IndexFields.name.name(), IndexFields.alt_names.name(),
                IndexFields.description.name(), IndexFields.enzyme_family.name(), IndexFields.intenz_cofactors.name(), IndexFields.catalytic_activity.name());
        QueryBuilder queryBuilder = QueryBuilder
                .builder()
                .query(query)
                .start(startPage * pageSize)
                .size(pageSize)
                .fields(fieldList)
                .sort(RELEVANCE)
                .reverse(Boolean.TRUE)
                .format(FORMAT)
                .build();

        return apiIndexService.getEnzyme(queryBuilder);

    }

    @Override
    public Flux<EnzymeEntry> getEnzymes(String searchTerm, int startPage, int pageSize) {
        List<String> fieldList = Arrays.asList(IndexFields.id.name(), IndexFields.name.name(), IndexFields.alt_names.name(),
                IndexFields.description.name(), IndexFields.enzyme_family.name(), IndexFields.intenz_cofactors.name(), IndexFields.catalytic_activity.name());
        QueryBuilder queryBuilder = QueryBuilder
                .builder()
                .query(searchTerm)
                .start(startPage * pageSize)
                .size(pageSize)
                .fields(fieldList)
                .sort(RELEVANCE)
                .reverse(Boolean.TRUE)
                .format(FORMAT)
                .build();

        return apiIndexService.getEnzymes(queryBuilder, searchTerm);

    }

    @Override
    public Flux<EnzymeEntry> getEnzymes(String searchTerm, String resourceId, IndexQueryType resourceQueryType, int startPage, int pageSize) {
        List<String> fieldList = Arrays.asList(IndexFields.id.name(), IndexFields.name.name(), IndexFields.alt_names.name(),
                IndexFields.description.name(), IndexFields.enzyme_family.name(), IndexFields.intenz_cofactors.name(), IndexFields.catalytic_activity.name());
        QueryBuilder queryBuilder = QueryBuilder
                .builder()
                .query(searchTerm)
                .start(startPage * pageSize)
                .size(pageSize)
                .fields(fieldList)
                .sort(RELEVANCE)
                .reverse(Boolean.TRUE)
                .format(FORMAT)
                .build();

        return apiIndexService.getEnzymes(queryBuilder, resourceId, resourceQueryType);

    }

    @Override
    public int getEnzymeHitCount(String query) {
        List<String> fieldList = Arrays.asList(IndexFields.id.name());
        QueryBuilder queryBuilder = QueryBuilder
                .builder()
                .query(query)
                .start(0)
                .size(1)
                .fields(fieldList)
                .sort(RELEVANCE)
                .reverse(Boolean.TRUE)
                .format(FORMAT)
                .build();

        return apiIndexService.getEnzymeHitCount(queryBuilder);

    }

}
