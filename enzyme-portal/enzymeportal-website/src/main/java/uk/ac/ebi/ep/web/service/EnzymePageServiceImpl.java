package uk.ac.ebi.ep.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ep.indexservice.helper.IndexQueryType;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeEntry;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeFields;
import uk.ac.ebi.ep.literatureservice.model.EuropePMC;
import uk.ac.ebi.ep.literatureservice.model.Result;
import uk.ac.ebi.ep.literatureservice.service.LiteratureService;
import uk.ac.ebi.ep.web.model.EnzymePage;
import uk.ac.ebi.reaction.mechanism.model.MechanismResult;
import uk.ac.ebi.reaction.mechanism.service.ReactionMechanismService;

/**
 *
 * @author joseph
 */
@Service
class EnzymePageServiceImpl implements EnzymePageService {

    private static final int CITATION_LIMIT = 11;
    protected final LiteratureService literatureService;

    private final ReactionMechanismService reactionMechanismService;
    private final SearchIndexService searchIndexService;

    @Autowired
    EnzymePageServiceImpl(LiteratureService literatureService, ReactionMechanismService reactionMechanismService, SearchIndexService searchIndexService) {
        this.literatureService = literatureService;
        this.reactionMechanismService = reactionMechanismService;
        this.searchIndexService = searchIndexService;
    }

    @Override
    public MechanismResult findReactionMechanism(String ec) {
        return reactionMechanismService.findMechanismResultByEc(ec);
    }

    private List<Result> findCitations(String enzymeName, int limit) {

        EuropePMC epmc = literatureService.getCitationsBySearchTerm(enzymeName, limit);
        if (epmc != null) {
            return epmc
                    .getResultList()
                    .getResult();
        }
        return new ArrayList<>();
    }

    @Override
    public EnzymeEntry findEnzymeByEcNumber(String ecNumber) {
        String query = String.format("%s%s", IndexQueryType.ID.getQueryType(), ecNumber);

        return searchIndexService.findEnzyme(query)
                .getEntries()
                .stream()
                .findAny()
                .orElseGet(() -> new EnzymeEntry(ecNumber, new EnzymeFields()));

    }

    public EnzymeEntry findEnzymePageEntry(String ecNumber) {
        String query = String.format("%s%s", IndexQueryType.ID.getQueryType(), ecNumber);
        return searchIndexService.getEnzymePageEntry(query)
                .blockOptional()
                .orElse(new EnzymeEntry());

    }

    @Override
    public EnzymePage buildEnzymePage(String ecNumber, String enzymeName, int limit) {

        CompletableFuture<EnzymeEntry> enzyme = CompletableFuture.supplyAsync(() -> findEnzymePageEntry(ecNumber));
        CompletableFuture<List<Result>> citations = CompletableFuture.supplyAsync(() -> findCitations(enzymeName, CITATION_LIMIT));

        EnzymeEntry enzymeEntry = enzyme.join();
        List<Result> citation = citations.join();

        return EnzymePage.enzymePageBuilder()
                .enzymeName(enzymeEntry.getEnzymeName())
                .ec(enzymeEntry.getEc())
                .altNames(enzymeEntry.getFields().getAltNames())
                .cofactors(enzymeEntry.getFields().getIntenzCofactors())
                .catalyticActivities(enzymeEntry.getFields().getCatalyticActivity().stream().findAny().orElse(""))
                .numProteins(enzymeEntry.getNumProteins())
                .associatedProteins(enzymeEntry.getProteinGroupEntry())
                .citations(citation)
                .numCitations(citation.size())
                .build();

    }

}
