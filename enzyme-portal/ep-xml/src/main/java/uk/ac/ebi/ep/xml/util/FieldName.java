package uk.ac.ebi.ep.xml.util;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public enum FieldName {

    UNIPROT_NAME("uniprot_name"), PROTEIN_NAME("protein_name"),GENE_NAME("gene_name"),
    SCIENTIFIC_NAME("scientific_name"), COMMON_NAME("common_name"), SYNONYM("synonym"), STATUS("status"), SOURCE("source"),
    COMPOUND_NAME("compound_name"), COMPOUND_TYPE("compound_type"), DISEASE_NAME("disease_name"),ENZYME_FAMILY("enzyme_family"),
    TRANSFER_FLAG("transfer_flag"),INTENZ_COFACTORS("intenz_cofactors"),INTENZ_ALT_NAMES("alt_names"),
    PRIMARY_ACCESSION("primary_accession"),PRIMARY_ORGANISM("primary_organism");

    private FieldName(String name) {
        this.name = name;
    }
    private final String name;

    public String getName() {
        return name;
    }

}
