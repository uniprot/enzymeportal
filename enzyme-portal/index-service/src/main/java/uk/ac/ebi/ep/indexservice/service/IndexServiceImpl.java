package uk.ac.ebi.ep.indexservice.service;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import uk.ac.ebi.ep.indexservice.helper.IndexFields;
import uk.ac.ebi.ep.indexservice.helper.IndexQueryType;
import uk.ac.ebi.ep.indexservice.helper.QueryBuilder;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeEntry;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeSearchResult;
import uk.ac.ebi.ep.indexservice.model.protein.ProteinGroupSearchResult;

/**
 *
 * @author joseph
 */
@Service
public class IndexServiceImpl implements IndexService {

    private static final String ENTRY_TYPE_ASC = "entry_type:ascending";
    private static final String FORMAT = "json";
    private static final int DEFAULT_PROTEIN_SIZE = 7;
    private final EnzymeCentricService enzymeCentricService;
    private final ProteinCentricService proteinCentricService;

    @Autowired
    public IndexServiceImpl(EnzymeCentricService enzymeCentricService, ProteinCentricService proteinCentricService) {
        this.enzymeCentricService = enzymeCentricService;
        this.proteinCentricService = proteinCentricService;
    }

    @Override
    public Mono<List<EnzymeEntry>> getEnzymeEntries(QueryBuilder enzymeQueryBuilder, String resourceQuery) {
        return enzymeCentricService.searchForEnzymesNonBlocking(enzymeQueryBuilder)
                .map(EnzymeSearchResult::getEntries)
                .flatMapIterable(entryList -> entryList)
                .flatMap(data -> addProtein(data, resourceQuery))
                .collectList();

    }

    private Mono<EnzymeEntry> addProtein(EnzymeEntry entry, String query) {

        List<String> fieldList = IndexFields.defaultFieldList(false);
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

        return proteinCentricService.searchForProteinsNonBlocking(queryBuilder)
                .map(result -> buildEnzymeEntry(result, entry));

    }

    @Override
    public Mono<EnzymeEntry> getEnzymePageEntry(QueryBuilder enzymeQueryBuilder) {
        return enzymeCentricService.searchForEnzymesNonBlocking(enzymeQueryBuilder)
                .map(e -> e.getEntries()
                .stream()
                .findFirst()
                .orElse(new EnzymeEntry()))
                .flatMap(this::addProtein);

    }

    private Mono<EnzymeEntry> addProtein(EnzymeEntry entry) {

        String query = String.format("%s%s", IndexQueryType.EC.getQueryType(), entry.getEc());

        List<String> fieldList = Arrays.asList(IndexFields.id.name(), IndexFields.name.name(),
                IndexFields.primary_accession.name(), IndexFields.primary_organism.name());
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

        return proteinCentricService.searchForProteinsNonBlocking(queryBuilder)
                .map(result -> buildEnzymeEntry(result, entry));

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
