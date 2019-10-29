package uk.ac.ebi.reaction.mechanism.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.reaction.mechanism.config.ReactionMechanismUrl;
import uk.ac.ebi.reaction.mechanism.model.Mechanism;
import uk.ac.ebi.reaction.mechanism.model.MechanismResult;
import uk.ac.ebi.reaction.mechanism.model.Reaction;
import uk.ac.ebi.reaction.mechanism.model.Result;

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
    public MechanismResult findMechanismResultByEc(String ec) {
        String urlParam = "entries.reactions.ecs.codes=" + ec + "&format=json";
        return getMechanismResult(urlParam);
    }

    @Override
    public MechanismResult findMechanismResultByAccession(String accession) {
        String urlParam = "entries.proteins.sequences.uniprot_ids=" + accession + "&format=json";
        return getMechanismResult(urlParam);
    }

    @Override
    public List<Mechanism> findMechanismsByEc(String ec) {
        return findMechanismResultByEc(ec)
                .getResults()
                .stream()
                .map(Result::getReaction)
                .map(Reaction::getMechanisms)
                .flatMap(List::stream)
                .collect(Collectors.toList());

    }

    @Override
    public List<Mechanism> findMechanismsByAccession(String accession) {

        return findMechanismResultByAccession(accession)
                .getResults()
                .stream()
                .map(Result::getReaction)
                .map(Reaction::getMechanisms)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<Result> findReactionMechanismByEc(String ec) {
        return findMechanismResultByEc(ec)
                .getResults();
    }

    @Override
    public Result findReactionMechanismByAccession(String accession) {
        return findMechanismResultByAccession(accession)
                .getResults()
                .stream()
                .findFirst()
                .orElse(null);

    }

}
