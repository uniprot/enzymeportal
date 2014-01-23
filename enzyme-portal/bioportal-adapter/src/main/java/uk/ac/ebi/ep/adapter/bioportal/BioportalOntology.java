package uk.ac.ebi.ep.adapter.bioportal;

/**
 * Ontologies available from BioPortal and used by Enzyme Portal.
 * @author rafa
 */
public enum BioportalOntology {
    
    /**
     * <a href="http://www.ebi.ac.uk/efo">Experimental Factor Ontology</a>
     */
    EFO("1136", "http://www.ebi.ac.uk/efo/{0}"),
    /**
     * <a href="http://omim.org/">Online Mendelian Inheritance in Man</a>
     */
    OMIM("1348", "http://purl.bioontology.org/ontology/OMIM/{0}"),
    /**
     * <a href="http://www.ncbi.nlm.nih.gov/mesh">Medical Subject Headings</a>
     */
    MESH("1351", "http://purl.bioontology.org/ontology/MSH/{0}");

    /**
     * The BioPortal ID of the ontology.
     * @deprecated Not used from v4.0 of BioPortal web services.
     */
    private String id;
    
    /**
     * The URI of one entry (class, concept) in the ontology. It must include a
     * placeholder <code>{0}</code> which will be replaced with the entry ID.
     */
    private String classUri;

    /**
     * Ontologies related to diseases. From EFO we use children of the
     * <a href="http://www.ebi.ac.uk/efo/EFO_0000408">disease</a> term.
     */
    public static final BioportalOntology[] FOR_DISEASES =
            new BioportalOntology[]{ EFO, OMIM, MESH };
    
    private BioportalOntology(String id, String classUrl) {
        this.id = id;
        this.classUri = classUrl;
    }

    public String getId() {
        return id;
    }

    public String getClassUri() {
        return classUri;
    }

}
