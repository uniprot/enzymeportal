package uk.ac.ebi.ep.enzymeservice.intenz.service;

import java.util.Collection;
import java.util.List;
import uk.ac.ebi.ep.enzymeservice.intenz.dto.EnzymeHierarchy;
import uk.ac.ebi.ep.enzymeservice.intenz.model.Intenz;

/**
 *
 * @author joseph
 */
public interface IntenzService {

    List<Intenz> getIntenz(Collection<String> ecList);

    List<EnzymeHierarchy> getEnzymeHierarchies(List<String> ecList);
}
