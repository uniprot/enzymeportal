package uk.ac.ebi.ep.indexservice.helper;

import lombok.Getter;

/**
 *
 * @author joseph
 */
@Getter
public enum IndexQueryType {
    KEYWORD(""), ID("id:"), CHEBI("CHEBI:"), CHEBI_ID("chebi_id:"), COFACTOR("cofactor:"),
    METABOLIGHTS("METABOLIGHTS:"), RHEA("RHEA:"), INTENZ("INTENZ:"), EC("ec:"), OMIM("OMIM:"),
    PROTEIN_FAMILY("PROTEIN_FAMILY:"), PROTEIN_FAMILY_ID("protein_family_id:"),
    TAXONOMY("TAXONOMY:"), REACTOME("REACTOME:");
    private final String queryType;

    private IndexQueryType(String queryType) {
        this.queryType = queryType;
    }
}
