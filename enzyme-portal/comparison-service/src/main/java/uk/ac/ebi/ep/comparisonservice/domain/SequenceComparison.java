package uk.ac.ebi.ep.comparisonservice.domain;

/**
 * Comparison for protein sequences, taking into account the sequence itself and
 * the weight.
 *
 * @author Joseph
 * @since 1.1.0
 */
public class SequenceComparison extends AbstractComparison<Integer> {

    public SequenceComparison(Integer seq1, Integer seq2) {
        compared = new Integer[]{seq1, seq2};
        init(seq1, seq2);
        if (seq1 == null && seq2 == null) {
            differ = true; // safe guess
        }
    }

    @Override
    protected void getSubComparisons(Integer seq1, Integer seq2) {
        subComparisons.put("Sequence", new IntegerComparison(
                seq1, seq2));
    }

    @Override
    public String toString() {
        return "Sequence";
    }

}
