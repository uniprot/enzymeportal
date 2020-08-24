package uk.ac.ebi.ep.restapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import uk.ac.ebi.ep.brendaservice.dto.BrendaResult;
import uk.ac.ebi.ep.brendaservice.service.BrendaService;
import uk.ac.ebi.ep.indexservice.helper.IndexQueryType;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeEntry;
import uk.ac.ebi.ep.indexservice.model.protein.ProteinGroupEntry;
import uk.ac.ebi.ep.literatureservice.model.EuropePMC;
import uk.ac.ebi.ep.literatureservice.model.Result;
import uk.ac.ebi.ep.literatureservice.service.LiteratureService;
import uk.ac.ebi.ep.reaction.mechanism.model.MechanismResult;
import uk.ac.ebi.ep.reaction.mechanism.service.ReactionMechanismService;
import uk.ac.ebi.ep.restapi.dto.BrendaParameter;
import uk.ac.ebi.ep.restapi.model.EnzymeModel;
import uk.ac.ebi.ep.restapi.model.ProteinModel;
import uk.ac.ebi.ep.restapi.util.SearchUtil;

/**
 *
 * @author joseph
 */
@Service
class EnzymeApiServiceImpl implements EnzymeApiService {

    private static final int MCSA_PAGE_SIZE = 1;
    protected final LiteratureService literatureService;

    private final ReactionMechanismService reactionMechanismService;
    private final SearchIndexService searchIndexService;
    private final BrendaService brendaService;

    public EnzymeApiServiceImpl(LiteratureService literatureService, ReactionMechanismService reactionMechanismService, SearchIndexService searchIndexService, BrendaService brendaService) {
        this.literatureService = literatureService;
        this.reactionMechanismService = reactionMechanismService;
        this.searchIndexService = searchIndexService;
        this.brendaService = brendaService;

    }

    private int validateLimit(int limit) {
        if (limit < 0 || limit > 100) {
            limit = 10;
        }
        return limit;
    }

    protected Page<ProteinGroupEntry> buildProteinGroupEntryPage(List<ProteinGroupEntry> entries, int startPage, int pageSize, long hitCount) {
        PageRequest pageable = PageRequest.of(startPage, pageSize);
        return new PageImpl<>(entries, pageable, hitCount);
    }

    @Override
    public Flux<EnzymeModel> findEnzymes(int page, int pageSize) {

        return searchIndexService.getEnzymes(page, pageSize)
                .map(this::toEnzyme);

    }

    @Override
    public Page<EnzymeEntry> getEnzymeEntries(Pageable pageable) {

        return searchIndexService.getEnzymeEntries(pageable);

    }

    @Override
    public Flux<ProteinGroupEntry> findProteinSummary(String searchTerm, String ec, int start, int pageSize) {
        return searchIndexService.findProteinSummaries(searchTerm, ec, start, pageSize);
    }

    @Override
    public Page<ProteinGroupEntry> findProteinSummary(String query, String ec, Pageable pageable) {
        String hitsQuery = String.format("%s AND %s%s", query, IndexQueryType.EC.getQueryType(), ec);

        int hitCount = getEnzymesHitCount(hitsQuery);

        List<ProteinGroupEntry> resultList = searchIndexService.findProteinSummaries(query, ec, pageable.getPageNumber(), pageable.getPageSize())
                .collectList()
                .block();
        return new PageImpl(resultList, pageable, hitCount);
    }

    @Override
    public Page<EnzymeEntry> findEnzymesBySearchTerm(String searchTerm, String indexQuery, Pageable pageable) {

        int hitCount = getEnzymesHitCount(indexQuery);

        List<EnzymeEntry> enzymes = searchIndexService.getEnzymes(searchTerm, pageable.getPageNumber(), pageable.getPageSize())
                .collectList().block();
        return new PageImpl(enzymes, pageable, hitCount);
    }

    @Override
    public int getEnzymesHitCount(String query) {
        return searchIndexService.getEnzymeHitCount(query);
    }

    @Override
    public Flux<ProteinGroupEntry> associatedProteinByEc(String ec, int limit) {
        limit = validateLimit(limit);
        return searchIndexService.associatedProteinByEc(ec, limit);

    }

    @Override
    public List<ProteinGroupEntry> associatedProteinSummaryByEc(String ec, int limit) {
        limit = validateLimit(limit);
        return searchIndexService.associatedProteinSummaryByEc(ec, limit)
                .collectList()
                .block();
    }

    private ProteinModel toProtein(ProteinGroupEntry protein) {

        return ProteinModel.builder()
                .accession(protein.getPrimaryAccession())
                .proteinName(protein.getProteinName())
                .organismName(protein.getPrimaryOrganism())
                .build();
    }

    private CollectionModel<ProteinModel> toProtein(List<ProteinGroupEntry> proteinList) {

        List<ProteinModel> proteinModels = proteinList.stream()
                .map(protein -> toProtein(protein))
                .collect(Collectors.toList());
        return CollectionModel.of(proteinModels);
    }

    @Override
    public BrendaParameter reactionParameterByEcNumber(String ec, int limit) {
        limit = validateLimit(limit);
        BrendaResult brendaResult = brendaService.findBrendaResultByEc(ec, limit, false);

        return BrendaParameter.builder()
                .kinectics(brendaResult.getBrenda())
                .temperature(brendaResult.getTemperature())
                .ph(brendaResult.getPh())
                .build();

    }

    @Override
    public MechanismResult findReactionMechanism(String ec) {

        return reactionMechanismService.findMechanismResultByEc(ec, MCSA_PAGE_SIZE);
    }

    @Override
    public List<Result> findCitations(String enzymeName, int limit) {
        limit = validateLimit(limit);
        EuropePMC epmc = literatureService.getCitationsBySearchTerm(enzymeName, limit);
        if (epmc != null) {
            return epmc
                    .getResultList()
                    .getResult();
        }
        return new ArrayList<>();
    }

    @Override
    public EnzymeEntry enzymeByEcNumber(@Valid String ecNumber) {
        String validEc = SearchUtil.transformIncompleteEc(ecNumber);
        String query = String.format("%s%s", IndexQueryType.ID.getQueryType(), validEc);
        return searchIndexService.getEnzyme(query)
                .single().block();
    }

    @Override
    public Flux<EnzymeEntry> getEnzyme(@Valid String ecNumber) {
        String validEc = SearchUtil.transformIncompleteEc(ecNumber);
        String query = String.format("%s%s", IndexQueryType.ID.getQueryType(), validEc);
        return searchIndexService.getEnzyme(query)
                .switchIfEmpty(Flux.empty());
    }

    private EnzymeModel toEnzyme(EnzymeEntry entry) {
        return EnzymeModel.builder()
                .ecNumber(entry.getId())
                .enzymeName(entry.getEnzymeName())
                .enzymeFamily(entry.getEnzymeFamily())
                .alternativeNames(entry.getFields().getAltNames())
                .catalyticActivities(entry.getFields().getCatalyticActivity())
                .associatedProteins(toProtein(entry.getProteinGroupEntry()))
                .build();
    }

}
