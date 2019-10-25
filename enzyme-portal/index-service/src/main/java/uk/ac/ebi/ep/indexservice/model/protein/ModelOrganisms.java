package uk.ac.ebi.ep.indexservice.model.protein;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public enum ModelOrganisms {

    HUMAN("Human"),
    MOUSE("Mouse"),
    MOUSE_EAR_CRESS("Arabidopsis thaliana"),
    FRUIT_FLY("Fruit fly"),
    BAKER_YEAST("Saccharomyces cerevisiae"),
    ECOLI("Escherichia coli"),
    RAT("Rat");

    private static final List<String> allCommonNames = new LinkedList<>();

    static {
        for (ModelOrganisms cs : ModelOrganisms.values()) {
            allCommonNames.add(cs.commonName);
        }
    }
    private String commonName;

    private ModelOrganisms(String name) {
        this.commonName = name;
    }

    public String getCommonName() {
        return commonName;
    }

    /**
     * List of most common species <i>common names</i>
     *
     * @return
     */
    public static List<String> getCommonNames() {
        return allCommonNames;
    }
}
