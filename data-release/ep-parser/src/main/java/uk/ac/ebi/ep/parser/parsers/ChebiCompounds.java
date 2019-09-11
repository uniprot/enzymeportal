package uk.ac.ebi.ep.parser.parsers;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import uk.ac.ebi.chebi.webapps.chebiWS.model.Entity;
import uk.ac.ebi.ep.centralservice.helper.Relationship;
import uk.ac.ebi.ep.metaboliteService.service.ChebiService;
import uk.ac.ebi.ep.metaboliteService.service.MetabolightService;
import uk.ac.ebi.ep.model.ChebiCompound;
import uk.ac.ebi.ep.model.dao.ChebiReactant;
import uk.ac.ebi.ep.model.dao.Compound;
import uk.ac.ebi.ep.model.dao.MetaboliteView;
import uk.ac.ebi.ep.model.repositories.EnzymeReactionInfoRepository;
import uk.ac.ebi.ep.model.service.EnzymePortalParserService;

/**
 *
 * @author joseph
 */
@Slf4j
@Service
public class ChebiCompounds extends ChebiCofactors {

    private static final String METABOLIGHT_URL = "https://www.ebi.ac.uk/metabolights/";

    private final MetabolightService metabolightService;

    private final EnzymeReactionInfoRepository enzymeReactionInfoRepository;

    public ChebiCompounds(MetabolightService metabolightService, ChebiService chebiService, EnzymeReactionInfoRepository enzymeReactionInfoRepository, EnzymePortalParserService enzymePortalParserService) {
        super(enzymePortalParserService, chebiService);
        this.metabolightService = metabolightService;
        this.enzymeReactionInfoRepository = enzymeReactionInfoRepository;
    }

    public void loadUniqueMetabolitesToDatabase() {
        List<MetaboliteView> metabolites = enzymePortalParserService.findMetabolites();
        log.info("Number of metabolites found " + metabolites.size());
        log.debug("About to load Metabolites to database .......");
        metabolites.forEach(metabolite -> createMetabolite(metabolite));
        log.info("Done loading unique metabolites to database");
    }

    private void createMetabolite(MetaboliteView metabolite) {
        String metaboliteId = metabolite.getCompoundId().replace("CHEBI:", "MTBLC");
        String url = String.format("%s%s%s", METABOLIGHT_URL, metaboliteId, "/#biology");
        enzymePortalParserService.createMetabolite(metabolite.getCompoundId(), metabolite.getCompoundName(), url);
    }

    private void processChebiIdInReactionInfo(String chebi, String accession, AtomicInteger counter) {
        log.info("Processing CHEBI ID : " + chebi + " Num processed so far : " + counter.getAndIncrement());

        ChebiCompound chebiCompound = findChebiCompoundById(chebi);

        String chebiId = null;
        String chebiName = null;

        if (Objects.isNull(chebiCompound)) {
            Entity entity = chebiService.getCompleteChebiEntityInformation(chebi);
            chebiId = entity.getChebiId();
            chebiName = entity.getChebiAsciiName();
        } else {

            chebiId = chebiCompound.getChebiAccession();
            chebiName = chebiCompound.getCompoundName();
        }

        final String preferredName = chebiName;

        String synonyms = chebiService.getChebiSynonyms(chebi)
                .stream()
                .filter(c -> !c.equalsIgnoreCase(preferredName))
                .limit(1_000)
                .collect(Collectors.joining(";"));

        boolean isMetabolite = metabolightService.isMetabolite(chebi);
        String relationship = Relationship.is_reactant_of.name();
        String role = Compound.Role.REACTANT.name();
        String url = String.format("%s%s", "https://www.ebi.ac.uk/chebi/advancedSearchFT.do?searchString=", chebi);
        String note = "";
        if (isMetabolite) {
            relationship = Relationship.is_reactant_or_product_of.name();
            role = Compound.Role.METABOLITE.name();

        }
        if (chebiName == null || "".equals(chebiName)) {
            chebiName = chebiId;
        }

        if (Objects.nonNull(chebiId) && !blackList.contains(chebiName) && !StringUtils.isEmpty(chebiName)) {
            enzymePortalParserService.createChebiCompound(chebiId, chebiName, synonyms, relationship, accession, url, role, note);
        }

    }

    @Transactional(readOnly = true)
    public void loadChebiCompoundsToDatabase() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        AtomicInteger counter = new AtomicInteger(1);
        long total = enzymeReactionInfoRepository.countChebiReactantInfo();

        log.info("About to start streaming and processing " + total + " reactionInfo entries.");

        try (Stream<ChebiReactant> reactionInfo = enzymeReactionInfoRepository.streamChebiReactantInfo()) {

            reactionInfo
                   // .parallel()
                    .forEach(data -> processChebiIdInReactionInfo(data.getChebiId(), data.getAccession(), counter));

        }

        stopWatch.stop();

        log.info("Time taken to process " + total + " entries:  " + stopWatch.getTotalTimeSeconds() / 60 + " mins" + " Or " + stopWatch.getTotalTimeSeconds() / 3600 + " hrs");

    }

}
