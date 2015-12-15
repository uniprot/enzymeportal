/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.common;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public enum ModelOrganisms {

    //Human,Mouse, Mouse-ear cress, fruit fly, yeast, e.coli,Rat,//worm
    HUMAN("Homo sapiens"),
    MOUSE("Mus musculus"),
    MOUSE_EAR_CRESS("Arabidopsis thaliana"),
    FRUIT_FLY("Drosophila melanogaster"),
    BAKER_YEAST("Saccharomyces cerevisiae"),
    ECOLI("Escherichia coli"),
    RAT("Rattus norvegicus");
    // WORM("Caenorhabditis elegans");

    private static final List<String> allScientificNames = new LinkedList<>();

    static {
        for (ModelOrganisms cs : ModelOrganisms.values()) {
            allScientificNames.add(cs.scientificName);
        }
    }
    private String scientificName;

    private ModelOrganisms(String name) {
        this.scientificName = name;
    }

    public String getScientificName() {
        return scientificName;
    }

    /**
     * List of most common species <i>scientific names</i>
     *
     * @return
     */
    public static List<String> getScientificNames() {
        return allScientificNames;
    }

}
