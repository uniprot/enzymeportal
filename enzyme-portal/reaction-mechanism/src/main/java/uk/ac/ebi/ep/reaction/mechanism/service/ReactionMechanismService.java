package uk.ac.ebi.ep.reaction.mechanism.service;

import java.util.List;
import uk.ac.ebi.ep.reaction.mechanism.model.Mechanism;
import uk.ac.ebi.ep.reaction.mechanism.model.MechanismResult;
import uk.ac.ebi.ep.reaction.mechanism.model.Result;

/**
 *
 * @author Joseph
 */
public interface ReactionMechanismService {

    MechanismResult findMechanismResultByEc(String ec, int pageSize);

    MechanismResult findMechanismResultByAccession(String accession, int pageSize);

    List<Mechanism> findMechanismsByEc(String ec, int pageSize);

    List<Mechanism> findMechanismsByAccession(String accession, int pageSize);

    List<Result> findReactionMechanismByEc(String ec, int pageSize);

    Result findReactionMechanismByAccession(String accession);

}
