package uk.ac.ebi.ep.indexservice.helper;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author joseph
 */
public enum IndexFields {
    id, name, description, catalytic_activity, enzyme_family, alt_names, intenz_cofactors,
    primary_organism, primary_accession, related_species, UNIPROTKB, common_name, scientific_name,
    ec, entry_type, gene_name, primary_image, function, disease_name, synonym,
    with_cofactor, with_metabolite, with_pathway, with_taxonomy, with_disease, with_protein_family;

    /**
     *
     * @param isSummaryPage summary page requires more fields
     * @return List of IndexFields
     */
    public static List<String> defaultFieldList(boolean isSummaryPage) {
        if (isSummaryPage) {
            return Arrays.asList(IndexFields.id.name(), IndexFields.name.name(),
                    IndexFields.primary_accession.name(), IndexFields.primary_organism.name(),
                    IndexFields.primary_image.name(), IndexFields.function.name(), IndexFields.disease_name.name(), IndexFields.catalytic_activity.name(),
                    IndexFields.related_species.name(), IndexFields.alt_names.name(), IndexFields.gene_name.name(), IndexFields.ec.name(), IndexFields.entry_type.name());

        }
        return Arrays.asList(IndexFields.id.name(), IndexFields.name.name(), IndexFields.primary_accession.name(),
                IndexFields.primary_organism.name());
    }
}
