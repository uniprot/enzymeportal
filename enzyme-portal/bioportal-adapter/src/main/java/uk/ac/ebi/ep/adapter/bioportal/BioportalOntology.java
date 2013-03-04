package uk.ac.ebi.ep.adapter.bioportal;

/**
 * Ontologies available from BioPortal and used by Enzyme Portal.
 * @author rafa
 */
public enum BioportalOntology {
    EFO("1136"),
    OMIM("1348"),
    MESH("1351");
    
    private String id;

    private static BioportalOntology[] forDiseases =
            new BioportalOntology[]{ EFO, OMIM, MESH };
    
    private BioportalOntology(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    /**
     * Getter for ontologies related to diseases.
     * @return only those ontologies related to diseases.
     */
    public static BioportalOntology[] forDiseases() {
        return BioportalOntology.forDiseases;
    }
    
}
