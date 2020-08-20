package uk.ac.ebi.ep.reaction.mechanism.service;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.reaction.mechanism.config.ReactionMechanismUrl;
import uk.ac.ebi.ep.reaction.mechanism.model.Mechanism;
import uk.ac.ebi.ep.reaction.mechanism.model.MechanismResult;
import uk.ac.ebi.ep.reaction.mechanism.model.Reaction;
import uk.ac.ebi.ep.reaction.mechanism.model.Result;

/**
 *
 * @author Joseph
 */
@Slf4j
public class ReactionMechanismServiceImpl implements ReactionMechanismService {

    private final ReactionMechanismUrl mcsaUrl;
    private final RestTemplate restTemplate;

    @Autowired
    public ReactionMechanismServiceImpl(ReactionMechanismUrl mcsaUrl, RestTemplate restTemplate) {
        this.mcsaUrl = mcsaUrl;
        this.restTemplate = restTemplate;
    }

    private MechanismResult getMechanismResult(String urlParam) {
        String url = String.format("%s%s", mcsaUrl.getMcsaUrl(), urlParam);
        log.debug("Reaction mechanism url : " + url);
        return restTemplate.getForObject(url.trim(), MechanismResult.class);
    }

    @Override
    public MechanismResult findMechanismResultByEc(String ec, int pageSize) {
        String urlParam = "entries.reactions.ecs.codes=" + ec + "&format=json&page_size" + pageSize;
        return getMechanismResult(urlParam);
    }

    @Override
    public MechanismResult findMechanismResultByAccession(String accession, int pageSize) {
        String urlParam = "entries.proteins.sequences.uniprot_ids=" + accession + "&format=json&page_size" + pageSize;
        return getMechanismResult(urlParam);
    }

    @Override
    public List<Mechanism> findMechanismsByEc(String ec, int pageSize) {
        return findMechanismResultByEc(ec, pageSize)
                .getResults()
                .stream()
                .map(Result::getReaction)
                .map(Reaction::getMechanisms)
                .flatMap(List::stream)
                .collect(Collectors.toList());

    }

    @Override
    public List<Mechanism> findMechanismsByAccession(String accession, int pageSize) {

        return findMechanismResultByAccession(accession, pageSize)
                .getResults()
                .stream()
                .map(Result::getReaction)
                .map(Reaction::getMechanisms)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<Result> findReactionMechanismByEc(String ec, int pageSize) {
        return findMechanismResultByEc(ec, pageSize)
                .getResults();
    }

    @Override
    public Result findReactionMechanismByAccession(String accession) {
        return findMechanismResultByAccession(accession, BigInteger.ONE.intValue())
                .getResults()
                .stream()
                .findFirst()
                .orElse(null);

    }

}
