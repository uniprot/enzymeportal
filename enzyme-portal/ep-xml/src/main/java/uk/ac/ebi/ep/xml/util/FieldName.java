package uk.ac.ebi.ep.xml.util;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public enum FieldName {

    PROTEIN_GROUP_ID("protein_group_id"), UNIPROT_NAME("uniprot_name"), PROTEIN_NAME("protein_name"),PROTEIN_INFO("protein_info"),GENE_NAME("gene_name"),
    SCIENTIFIC_NAME("scientific_name"), COMMON_NAME("common_name"), SYNONYM("synonym"), STATUS("status"), SOURCE("source"),
    COMPOUND_NAME("compound_name"), COMPOUND_TYPE("compound_type"), DISEASE_NAME("disease_name"),ENZYME_FAMILY("enzyme_family");

    private FieldName(String name) {
        this.name = name;
    }
    private final String name;

    public String getName() {
        return name;
    }

}
