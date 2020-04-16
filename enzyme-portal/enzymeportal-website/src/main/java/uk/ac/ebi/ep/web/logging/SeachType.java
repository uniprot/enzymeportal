package uk.ac.ebi.ep.web.logging;

/**
 *
 * @author joseph
 */
public enum SeachType {
    KEYWORD("Keyword"), BROWSE_BY("BrowseBy"), PROTEIN_PAGE("ProteinPage");

    private final String typeName;

    private SeachType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

}
