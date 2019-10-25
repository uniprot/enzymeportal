
package uk.ac.ebi.ep.comparisonservice.domain;

/**
 *
 * @author joseph
 */
public class IntegerComparison extends AbstractComparison<Integer> {

        public IntegerComparison(Integer s1, Integer s2) {
        compared = new Integer[] { s1, s2 };
        init(s1, s2);
    }
    
    @Override
    protected void getSubComparisons(Integer s1, Integer s2) {
      differ = s1 == null ? s2 != null : s1.intValue() != s2.intValue();
    }
    
}
