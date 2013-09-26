package uk.ac.ebi.ep.adapter.ebeye;

/**
 * Enumeration for EB-Eye domains, containing also default values for the lists
 * of searched fields for each of them. Please note that <i>these fields may
 * change in future versions of EB-Eye</i> (see
 * <a href="http://www.ebi.ac.uk/Tools/webservices/services/eb-eye">doc</a> on
 * <code>listFieldsInformation</code> method)</i>.
 * @author rafa
 * @since 1.0.4 (previously inner to IEbeyeAdapter).
*/
public enum Domains {
    intenz(new String[]{"id", "acc", "name", "synonym", "systematic_name",
            "reaction"}),
    uniprot(new String[]{"id", "acc", "descRecName", "description",
            "keywords", "organism_species", "organism_scientific_name",
            "organism_lineage", "organism_host_species",
            "dbRefProp_EMBL_protein_sequence_ID",
            "dbRefProp_EnsemblBacteria_protein_sequence_ID",
            "dbRefProp_EnsemblFungi_protein_sequence_ID",
            "dbRefProp_EnsemblMetazoa_protein_sequence_ID",
            "dbRefProp_EnsemblPlants_protein_sequence_ID",
            "dbRefProp_EnsemblProtists_protein_sequence_ID",
            "dbRefProp_Ensembl_protein_sequence_ID",
            "dbRefProp_GeneFarm_family_ID",
            "dbRefProp_WormBase_protein_sequence_ID"}),
    rhea(new String[]{"id", "acc", "name", "chebiidequation"}),
    reactome(new String[]{"id", "acc", "name", "description", "organism"}),
    chebi(new String[]{"id", "acc", "name", "synonym", "description",
            "formula", "inchi", "inchikey", "smiles", "iupac_name",
            "secondary_accession_number"}),
    pdbe(new String[]{"id", "acc", "name", "description", "keywords"}),
    chembl_compound(new String[]{"id", "compound_synonym"}),
    efo(new String[]{"id", "name", "synonym"
         /* extra: , "description", "domain_source" */}),
    mesh(new String[]{"id", "name"
         /* extra: , "annotation", "domain_source", "historynote",
         "onlinenote", "publicmeshnote", "qualifiername" */}),
    omim(new String[]{"id", "title", "alternative_title"
         /* extra: , "description", "domain_source", "allelic_variations",
         "clinical_synopsis" */})
    //, chembl_target
    ;

    private final String[] searchFields;

    private Domains(String[] searchFields) {
        this.searchFields = searchFields;
    }

    String[] getSearchFields() {
        return searchFields;
    }
}
