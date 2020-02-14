package uk.ac.ebi.ep.xml.util;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public enum DatabaseName {
    UNIPROTKB("UNIPROTKB"), CHEBI("CHEBI"), INTENZ("INTENZ"), TAXONOMY("TAXONOMY"), OMIM("OMIM"), REACTOME("REACTOME"), PROTEIN_FAMILY("PROTEIN_FAMILY"), RHEA("RHEA"),METABOLIGHTS("METABOLIGHTS");

    DatabaseName(String name) {
        this.dbName = name;
    }

    private final String dbName;

    public String getDbName() {
        return dbName;
    }

}
