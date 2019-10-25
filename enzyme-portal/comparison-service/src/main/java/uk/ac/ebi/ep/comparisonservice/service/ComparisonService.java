package uk.ac.ebi.ep.comparisonservice.service;

import java.util.List;
import uk.ac.ebi.ep.comparisonservice.model.ComparisonProteinModel;
import uk.ac.ebi.ep.comparisonservice.model.Disease;
import uk.ac.ebi.ep.comparisonservice.model.Molecule;
import uk.ac.ebi.ep.comparisonservice.model.ReactionPathway;

/**
 *
 * @author joseph
 */
public interface ComparisonService {

    ComparisonProteinModel getComparisonProteinModel(String accession);

    Molecule getCompareEnzymeMolecule(String accession);

    ReactionPathway getCompareEnzymeReactionPathay(String accession);

    List<Disease> getCompareEnzymeDisease(String accession);
}
