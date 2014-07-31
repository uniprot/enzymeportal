package uk.ac.ebi.ep.data.domain.epcore;

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
    
    private static List<String> allScientificNames = new LinkedList<String>();
    static {
    	for (CommonSpecies cs : CommonSpecies.values()) {
            allScientificNames.add(cs.scientificName);
		}
    }

    private CommonSpecies(String name) {
        this.scientificName = name;
    }
    private String scientificName;

    public String getScientificName() {
        return scientificName;
    }

    /**
     * List of most common species <i>scientific names</i>
     * @return
     */
    public static List<String> getScientificNames() {
        return allScientificNames;
    }

}
