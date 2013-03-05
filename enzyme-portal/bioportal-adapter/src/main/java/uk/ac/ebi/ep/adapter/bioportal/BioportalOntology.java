package uk.ac.ebi.ep.adapter.bioportal;

/**
 * Ontologies available from BioPortal and used by Enzyme Portal.
 * @author rafa
 */
public enum BioportalOntology {
    /**
     * <a href="http://www.ebi.ac.uk/efo">Experimental Factor Ontology</a>
     */
    EFO("1136"),
    /**
     * <a href="http://omim.org/">Online Mendelian Inheritance in Man</a>
     */
    OMIM("1348"),
    /**
     * <a href="http://www.ncbi.nlm.nih.gov/mesh">Medical Subject Headings</a>
     */
    MESH("1351");

    /**
     * The BioPortal ID of the ontology.
     */
    private String id;

    /**
     * Ontologies related to diseases. From EFO we use children of the
     * <a href="http://www.ebi.ac.uk/efo/EFO_0000408">disease</a> term.
     */
    public static final BioportalOntology[] FOR_DISEASES =
            new BioportalOntology[]{ EFO, OMIM, MESH };
    
    private BioportalOntology(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
