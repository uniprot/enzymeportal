/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye.model;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public enum ModelOrganisms {

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
    FRUIT_FLY("Drosophila melanogaster"),
    BAKER_YEAST("Saccharomyces cerevisiae"),
    ECOLI("Escherichia coli"),
    RAT("Rattus norvegicus");
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
