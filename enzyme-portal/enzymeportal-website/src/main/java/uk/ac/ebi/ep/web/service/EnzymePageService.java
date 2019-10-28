package uk.ac.ebi.ep.web.service;

import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeEntry;
import uk.ac.ebi.ep.web.model.EnzymePage;
import uk.ac.ebi.reaction.mechanism.model.MechanismResult;

/**
 *
 * @author joseph
 */
public interface EnzymePageService {

    MechanismResult findReactionMechanism(String ec);

    EnzymeEntry findEnzymeByEcNumber(String ecNumber);

    EnzymePage buildEnzymePage(String ecNumber, String enzymeName, int limit);
}
