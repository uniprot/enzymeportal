package uk.ac.ebi.ep.core.search;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author joseph
 */
public enum CommonSpecies {

    Human("Homo sapiens"),
    Mouse("Mus musculus"),
    Rat("Rattus norvegicus"),
    Fruit_fly("Drosophila melanogaster"),
    Worm("Caenorhabditis elegans"),
    Baker_Yeast("Saccharomyces cerevisiae"),
    Ecoli("Escherichia coli");

    private CommonSpecies(String name) {
        this.scientificName = name;
        commonSpeciesList.add(scientificName);
    }
    private String scientificName;
    private List<String> commonSpeciesList = new LinkedList<String>();

    public String getScientificName() {
        return scientificName;
    }

    public List<String> getCommonSpecies() {
        return commonSpeciesList;
    }

}
