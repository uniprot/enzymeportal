
package uk.ac.ebi.ep.ebeye.protein.model;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public enum ModelOrganisms {

//    //Human,Mouse, Mouse-ear cress, fruit fly, yeast, e.coli,Rat,//worm
//    HUMAN("9606"),
//    MOUSE("10090"),
//    MOUSE_EAR_CRESS("3702"),
//    FRUIT_FLY("7227"),
//    BAKER_YEAST("4932"),
//    ECOLI("83333"),
//    RAT("10116");
//
//    private static final List<String> taxIds = new LinkedList<>();
//
//    static {
//        for (ModelOrganisms cs : ModelOrganisms.values()) {
//            taxIds.add(cs.taxId);
//        }
//    }
//
//    private String taxId;
//
//    private ModelOrganisms(String taxId) {
//        this.taxId = taxId;
//    }
//
//    public String getTaxId() {
//        return taxId;
//    }
//
//    public static List<String> getTaxIds() {
//        return taxIds;
//    }


    //Human,Mouse, Mouse-ear cress, fruit fly, yeast, e.coli,Rat,//worm
//    HUMAN("Homo sapiens"),
//    MOUSE("Mus musculus"),
//    MOUSE_EAR_CRESS("Arabidopsis thaliana"),
//    FRUIT_FLY("Drosophila melanogaster"),
//    BAKER_YEAST("Saccharomyces cerevisiae"),
//    ECOLI("Escherichia coli"),
//    RAT("Rattus norvegicus");
//    // WORM("Caenorhabditis elegans");
    HUMAN("Human"),
    MOUSE("Mouse"),
    MOUSE_EAR_CRESS("Arabidopsis thaliana"),
    FRUIT_FLY("Fruit fly"),
    BAKER_YEAST("Saccharomyces cerevisiae"),
    ECOLI("Escherichia coli"),
    RAT("Rat");
    // WORM("Caenorhabditis elegans");// WORM("Caenorhabditis elegans");

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
