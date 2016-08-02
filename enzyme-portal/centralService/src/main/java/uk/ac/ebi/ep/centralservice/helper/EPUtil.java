package uk.ac.ebi.ep.centralservice.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for the Enzyme Portal. Its main use is text processing.
 * $Author$
 */
public class EPUtil {

    private static final String REGEXP_HOW
            = "(?: with low affinity| competitively)?";
    private static final String REGEXP_PROHIB
            = "(?!(?:auto|de)?phosphorylation|(?:de)?acetylation|substrate|the "
            + "|[\\w\\s-]+?(?:binding|cleavage|phosphorylation))";
    private static final String REGEXP_REL_CONC
            = "(?:(?:.+?) (?:concentration|level)s? of |\\d+% )?";
    private static final String REGEXP_COMP_GRP
            = "(?:thiol-specific compounds |reductants such as |.*?antibiotics "
            + "|.*?drugs? |.*?ions, such as "
            + "|various compounds, including )?";
//            "(?:.*?(?:compound|reductant|antibiotic|drug|ion)s?" +
//                    "(?: |,? such as |,? including ))?";
    private static final String REGEXP_EXPLANATION
            = "(?:,? an? .+?|,? whose .+?|,? which .+?|,? irrespective .+?"
            + "| as .+?| to .+?| with .+?| at .+?| concentrations .+?"
            + "|, this [^\\.]+| during .+?|(?: while)? in .+?| binding)?";
    private static final String INH_REGEXP_OTHERS_LESS
            = ",? (?:and(?! activated by)|as well as)"
            + "(?:, to a (?:lower|lesser) extent, by| slightly inhibited by| by)?";
    private static final String ACT_REGEXP_OTHERS_LESS
            = ",? (?:and(?! in(?:hibited|activated) by)|as well as)"
            + "(?:, to a (?:lower|lesser) extent, by| slightly activated by| by)?";
    private static final String INH_REGEXP_DONT_CONTINUE
            = "(?! (?:and )?activated by| but not | the )";
    private static final String ACT_REGEXP_DONT_CONTINUE
            = "(?! (?:and )?in(?:hibited|activated) by| but not | the )";
    private static final String INH_REGEXP_END
            = "(?:\\.?$|[\\.;:] |,? but not "
            + "|,?(?: and)?(?: \\w+ly)? activated by)";
    private static final String ACT_REGEXP_END
            = "(?:\\.?$|[\\.;:] |,? but not "
            + "|,?(?: and)?(?: \\w+ly)? in(?:hibited|activated) by)";

    /**
     * Regular expression for inhibitor compounds. This regexp has got 2
     * capturing groups.
     */
    private static final String INHIBITOR_REGEXP
            = "(.+?) is a(?:.*?)? inhibitor"
            + "|(?<![Nn]ot )\\b[Ii]n(?:hibited|activated)"
            + REGEXP_HOW
            + " by "
            + REGEXP_PROHIB
            + REGEXP_REL_CONC
            + REGEXP_COMP_GRP
            + "(.+?)"
            + REGEXP_EXPLANATION
            + "(?:"
            + "(?: or|" + INH_REGEXP_OTHERS_LESS
            + "|," + INH_REGEXP_DONT_CONTINUE + ") "
            + "(.+?)"
            + REGEXP_EXPLANATION
            + ")?"
            + INH_REGEXP_END;

    /**
     * Regular expression for activator compounds. This regexp has got 2
     * capturing groups.
     */
    private static final String ACTIVATOR_REGEXP
            = "(?<![Nn]ot )\\b[Aa]ctivated"
            + REGEXP_HOW
            + " by "
            + REGEXP_PROHIB
            + REGEXP_REL_CONC
            + REGEXP_COMP_GRP
            + "(.+?)"
            + REGEXP_EXPLANATION
            + "(?:"
            + "(?: or|" + ACT_REGEXP_OTHERS_LESS
            + "|," + ACT_REGEXP_DONT_CONTINUE + ") "
            + "(.+?)"
            + REGEXP_EXPLANATION
            + ")?"
            + ACT_REGEXP_END;

    private static final Pattern INHIBITOR_PATTERN
            = Pattern.compile(INHIBITOR_REGEXP);

    private static final Pattern ACTIVATOR_PATTERN
            = Pattern.compile(ACTIVATOR_REGEXP);
    
    private EPUtil(){}

    /**
     * Extracts the prefix from a UniProt ID (strips the species suffix).
     *
     * @param id a UniProt ID.
     * @return an ID without the species suffix.
     */
    public static String getIdPrefix(String id) {
        return id.split("_")[0];
    }

    /**
     * Extracts the prefixes from UniProt IDs (strips the species suffixes).
     *
     * @param ids a collection of UniProt IDs.
     * @return a list of distinct UniProt IDs without the species suffix.
     */
    public static List<String> getIdPrefixes(Collection<String> ids) {
        Set<String> prefixes = new LinkedHashSet<>();
        for (String id : ids) {
            prefixes.add(getIdPrefix(id));
        }
        return new ArrayList<>(prefixes);
    }

    /**
     * Converts the UniProt IDs to prefixes with a wildcard (<code>_*</code>) at
     * the end, which should match orthologs.
     *
     * @param ids a collection of UniProt IDs, either complete (ex. ALR1_YEAST)
     * or just prefixes without wildcard (ex. ALR1).
     * @return a list of distinct UniProt IDs with the species suffix replaced
     * with a wildcard.
     */
    public static List<String> getWildcardIds(Collection<String> ids) {
        Set<String> wIds = new LinkedHashSet<>();
        for (String id : ids) {
            String idPrefix = id.indexOf('_') > -1 ? getIdPrefix(id) : id;
            wIds.add(idPrefix + "_*");
        }
        return new ArrayList<>(wIds);
    }

    /**
     * Parses a text to get a list of molecules acting as inhibitors.
     *
     * @param text an enzyme regulation comment from UniProt.
     * @return A list of molecules with just a name (no ID). It can be empty,
     * but never <code>null</code>.
     */
    public static Set<String> parseTextForInhibitors(String text) {
        return getMolecules(INHIBITOR_PATTERN.matcher(text));
    }

    /**
     * Parses a text to get a list of molecules acting as activators.
     *
     * @param text an enzyme regulation comment from UniProt.
     * @return A list of molecules with just a name (no ID). It can be empty,
     * but never <code>null</code>.
     */
    public static Set<String> parseTextForActivators(String text) {
        return getMolecules(ACTIVATOR_PATTERN.matcher(text));
    }

    /**
     * Extracts and cleans names from a matcher, building a molecule for each of
     * them.<br/>
     * <i>Cleaning</i> includes removing conjunctions from the text and also
     * chemical concentrations.<br/>
     * Please note that any synonyms in parentheses - ex. <code>"AIM-100
     * (4-amino-5,6-biaryl-furo[2,3-d]pyrimidine)"</code> will be included in
     * the molecule name.
     *
     * @param m a matcher for activators/inhibitors patterns.
     * @return a list of molecules with just a name. The list can be empty, but
     * never <code>null</code>.
     */
    private static Set<String> getMolecules(Matcher m) {
        Set<String> names = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
        //List<Molecule> molecules = new ArrayList<Molecule>();
        while (m.find()) {
            for (int i = 1; i <= m.groupCount(); i++) {
                if (m.group(i) == null) {
                    continue;
                }
                String[] namesArray = m.group(i)
                        // remove concentrations (ex: 1 mM, 0.5 nM)
                        .replaceAll("\\d+(\\.\\d+)? .M ", "")
                        // remove stop words
                        .replaceAll(",? and|,? or", ",")
                        .split(", ");
                names.addAll(Arrays.asList(namesArray));
            }
        }

        return names;
    }

}
