package uk.ac.ebi.ep.base.comparison;

/**
 * Comparison of strings (ignores letter case).
 * 
 * @author rafa
 * @since 1.1.0
 */
public class StringComparison extends AbstractComparison<String> {

    public StringComparison(String s1, String s2) {
        compared = new String[] { s1, s2 };
        init(s1, s2);
    }

    @Override
    protected void getSubComparisons(String s1, String s2) {
        differ = s1 == null ? s2 != null : !s1.equalsIgnoreCase(s2);
    }

}
