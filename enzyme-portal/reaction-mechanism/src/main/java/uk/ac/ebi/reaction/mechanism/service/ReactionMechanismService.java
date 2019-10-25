package uk.ac.ebi.reaction.mechanism.service;

import java.util.List;
import uk.ac.ebi.reaction.mechanism.model.Mechanism;
import uk.ac.ebi.reaction.mechanism.model.MechanismResult;
import uk.ac.ebi.reaction.mechanism.model.Result;

/**
 *
 * @author Joseph
 */
public interface ReactionMechanismService {

    MechanismResult findMechanismResultByEc(String ec);

    MechanismResult findMechanismResultByAccession(String accession);

    List<Mechanism> findMechanismsByEc(String ec);

    List<Mechanism> findMechanismsByAccession(String accession);

    List<Result> findReactionMechanismByEc(String ec);

    Result findReactionMechanismByAccession(String accession);

}
