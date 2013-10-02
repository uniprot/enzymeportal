package uk.ac.ebi.ep.core.compare;

import uk.ac.ebi.ep.enzyme.model.Sequence;

/**
 * Comparison for protein sequences, taking into account the sequence itself and
 * the weight.
 * 
 * @author rafa
 * @since 1.1.0
 */
public class SequenceComparison extends AbstractComparison<Sequence> {

    public SequenceComparison(Sequence seq1, Sequence seq2) {
        compared = new Sequence[] { seq1, seq2 };
        subComparisons.add(new StringComparison(
                seq1.getSequence(), seq2.getSequence()));
        subComparisons.add(new StringComparison(
                seq1.getWeight(), seq2.getWeight()));
        doDiffer();
    }

}
