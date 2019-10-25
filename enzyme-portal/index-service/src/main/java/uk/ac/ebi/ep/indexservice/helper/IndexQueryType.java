package uk.ac.ebi.ep.indexservice.helper;

import lombok.Getter;

/**
 *
 * @author joseph
 */
@Getter
public enum IndexQueryType {
    KEYWORD(""), ID("id:"), CHEBI("CHEBI:"), CHEBI_ID("chebi_id:"), COFACTOR("cofactor:"),
    METABOLITE("metabolite:"), RHEA("RHEA:"), INTENZ("INTENZ:"), EC("ec:"), OMIM("OMIM:"),
    PROTEIN_FAMILY("PROTEIN_FAMILY:"), PROTEIN_FAMILY_ID("protein_family_id:"),
    TAXONOMY("TAXONOMY:"), REACTOME("REACTOME:"),
    HAS_TAXONOMY("has_taxonomy:true_taxonomy"), HAS_PROTEIN_FAMILY("has_protein_family:true_protein_family"),
    HAS_DISEASE("has_disease:true_disease"), HAS_COFACTOR("has_cofactor:true_cofactor"),
    HAS_PATHWAY("has_pathway:true_pathway");
    private final String queryType;

    private IndexQueryType(String queryType) {
        this.queryType = queryType;
    }
}
