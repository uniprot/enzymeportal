package uk.ac.ebi.ep.core.compare;

import uk.ac.ebi.ep.enzyme.model.EnzymeModel;

/**
 * Comparison for enzyme summaries. It takes into account:
 * <ul>
 * <li>enzyme name</li>
 * <li>species (just scientific name)</li>
 * <li>function</li>
 * <li>EC numbers</li>
 * <li>sequence</li>
 * </ul>
 * 
 * @author rafa
 * @since 1.1.0
 */
public class SummaryComparison extends AbstractComparison<EnzymeModel> {

    public SummaryComparison(EnzymeModel e1, EnzymeModel e2) {
        compared = new EnzymeModel[] { e1, e2 };
        subComparisons.add(new StringComparison(e1.getName(), e2.getName()));
        subComparisons.add(new StringComparison(
                e1.getSpecies().getScientificname(),
                e2.getSpecies().getScientificname()));
        subComparisons.add(new StringComparison(
                e1.getFunction(), e2.getFunction()));
        subComparisons.add(new ListComparison(e1.getEc(), e2.getEc()));
        subComparisons.add(new SequenceComparison(
                e1.getEnzyme().getSequence(), e2.getEnzyme().getSequence()));
        doDiffer();
    }

}
