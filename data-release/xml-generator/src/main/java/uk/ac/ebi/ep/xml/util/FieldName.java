package uk.ac.ebi.ep.xml.util;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public enum FieldName {

    UNIPROT_NAME("uniprot_name"),ENTRY_TYPE("entry_type"),FUNCTION("function"), PROTEIN_NAME("protein_name"),GENE_NAME("gene_name"),
    SCIENTIFIC_NAME("scientific_name"), COMMON_NAME("common_name"), SYNONYM("synonym"), STATUS("status"), SOURCE("source"),
    COFACTOR("cofactor"),INHIBITOR("inhibitor"),ACTIVATOR("activator"),COFACTOR_NAME("cofactor_name"),INHIBITOR_NAME("inhibitor_name"),ACTIVATOR_NAME("activator_name"),
    PRIMARY_IMAGE("primary_image"),PRIMARY_IMAGE_SPECIE("primary_image_specie"),EC("ec"),CATALYTIC_ACTIVITY("catalytic_activity"),
    COMPOUND_NAME("compound_name"), COMPOUND_TYPE("compound_type"), DISEASE_NAME("disease_name"),ENZYME_FAMILY("enzyme_family"),
    TRANSFER_FLAG("transfer_flag"),INTENZ_COFACTORS("intenz_cofactors"),INTENZ_ALT_NAMES("alt_names"),
    RELATED_SPECIES("related_species"),PRIMARY_ACCESSION("primary_accession"),PRIMARY_ORGANISM("primary_organism"), UNIPROT_FAMILY("uniprot_family");

    private FieldName(String name) {
        this.name = name;
    }
    private final String name;

    public String getName() {
        return name;
    }

}
