package uk.ac.ebi.ep.base.comparison;

import uk.ac.ebi.ep.data.enzyme.model.Sequence;

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
        init(seq1, seq2);
        if (seq1.getSequence() == null && seq2.getSequence() == null){
            differ = true; // safe guess
        }
    }

    @Override
    protected void getSubComparisons(Sequence seq1, Sequence seq2) {
        subComparisons.put("Sequence", new StringComparison(
                seq1.getSequence(), seq2.getSequence()));
        subComparisons.put("Weight", new StringComparison(
                seq1.getWeight(), seq2.getWeight()));
        subComparisons.put("Length", new StringComparison(
                seq1.getLength() == null? null :
                    String.valueOf(seq1.getLength()),
                seq2.getLength() == null? null :
                    String.valueOf(seq2.getLength())));
    }

    @Override
    public String toString() {
        return "Sequence";
    }

}
