package uk.ac.ebi.ep.data.common;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author joseph
 */
public enum CommonSpecies {

    HUMAN("Homo sapiens"),
    MOUSE("Mus musculus"),
    Rat("Rattus norvegicus"),
    FRUIT_FLY("Drosophila melanogaster"),
    WORM("Caenorhabditis elegans"),
    BAKER_YEAST("Saccharomyces cerevisiae"),
    ECOLI("Escherichia coli");
    
    private static final List<String> allScientificNames = new LinkedList<>();
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
