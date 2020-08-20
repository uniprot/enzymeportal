package uk.ac.ebi.ep.web.service;

import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeEntry;
import uk.ac.ebi.ep.reaction.mechanism.model.MechanismResult;
import uk.ac.ebi.ep.web.model.EnzymePage;

/**
 *
 * @author joseph
 */
public interface EnzymePageService {

    MechanismResult findReactionMechanism(String ec);

    EnzymeEntry findEnzymeByEcNumber(String ecNumber);

    EnzymePage buildEnzymePage(String ecNumber, String enzymeName, int limit);
}
