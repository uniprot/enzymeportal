
package uk.ac.ebi.ep.web.utils;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public enum KeywordType {

    KEYWORD("KEYWORD"),
    DISEASE("DISEASE"),
    EC("EC"),
    EC2PROTEIN("EC2PROTEIN"),
    TAXONOMY("TAXONOMY"),
    PATHWAYS("PATHWAYS"),
    FAMILIES("FAMILIES"),
    COFACTORS("COFACTORS"),
    CHEBI("CHEBI"),
    RHEA("RHEA"),
    METABOLITES("METABOLITES");

    private final String type;

    private KeywordType(String keywordType) {
        this.type = keywordType;
    }

    public String getType() {
        return type;
    }

}
