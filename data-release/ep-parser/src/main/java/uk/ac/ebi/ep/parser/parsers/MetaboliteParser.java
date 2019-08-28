package uk.ac.ebi.ep.parser.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import uk.ac.ebi.ep.centralservice.helper.Relationship;
import uk.ac.ebi.ep.metaboliteService.service.MetabolightService;
import uk.ac.ebi.ep.model.EnzymePortalReactant;
import uk.ac.ebi.ep.model.dao.Compound;
import uk.ac.ebi.ep.model.repositories.EnzymePortalReactantRepository;

/**
 *
 * @author joseph
 */
@Slf4j
public class MetaboliteParser {

    private final EnzymePortalReactantRepository enzymePortalReactantRepository;

    public MetaboliteParser(EnzymePortalReactantRepository enzymePortalReactantRepository) {

        this.enzymePortalReactantRepository = enzymePortalReactantRepository;

    }

    @Transactional
    public void updateMetabolite(MetabolightService metabolightService) {
        AtomicInteger counter = new AtomicInteger(1);
        List<String> chebiList = new ArrayList<>();

        StopWatch watch = new StopWatch();
        watch.start();
        try (Stream<String> chebiIds = enzymePortalReactantRepository.streamChebiIds()) {

                 chebiIds
                    .parallel()
                    .filter(id -> metabolightService.isMetabolite(id))
                    .forEach(id -> chebiList.add(id));

        }

        log.info("Number of Reactants to process " + chebiList.size());

        chebiList
                .forEach(id -> updateReactantByChebiId(id, counter));
        
        watch.stop();
        
        log.info("Time taken  ::: "+ watch.getTotalTimeSeconds() +" secs" + " Or "+ watch.getTotalTimeSeconds()/60 + " mins");

    }

    @Transactional
    private void updateReactantByChebiId(String chebiId, AtomicInteger counter) {
        List<EnzymePortalReactant> reactants = enzymePortalReactantRepository.findEnzymePortalReactantByReactantId(chebiId);

        //updateALLById(reactants, chebiId);
        updateById(reactants, chebiId);
        log.info(reactants.size() + " entries updated done for ChEBI Id ::: " + chebiId + " Count : "+ counter.getAndIncrement());

    }

    @Modifying(clearAutomatically = true)
    @Transactional
    private void updateALLById(List<EnzymePortalReactant> reactants, String chebiId) {
        reactants
                .stream()
                .parallel()
                .map(m -> updateEnzymePortalReactant(m))
                .collect(Collectors.toList());

        log.debug("Num of Entries to update for ID " + chebiId + " = " + reactants.size());

        enzymePortalReactantRepository.saveAll(reactants);
    }

    @Modifying(clearAutomatically = true)
    @Transactional
    private void updateById(List<EnzymePortalReactant> reactants, String chebiId) {
        log.debug("Num of Entries to update for ID " + chebiId + " = " + reactants.size());
        reactants
                .parallelStream()
                .map(m -> updateEnzymePortalReactant(m))
                .collect(Collectors.toList())
                .parallelStream()
                .forEach(metabolite -> enzymePortalReactantRepository.save(metabolite));

    }

    private EnzymePortalReactant updateEnzymePortalReactant(EnzymePortalReactant reactant) {
        String relationship = Relationship.is_reactant_or_product_of.name();
        String role = Compound.Role.METABOLITE.name();
        reactant.setRelationship(relationship);
        reactant.setReactantRole(role);
        return reactant;
    }

}
