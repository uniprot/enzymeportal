package uk.ac.ebi.ep.adapter.literature;

/**
 * @author rafa
 * @since 1.0.7 2013-04-09 (refactored out from SimpleLiteratureAdapter)
 */
public enum CitationLabel {

    // FIXME take strings from common properties file

    ENZYME("Enzyme"),
    PROTEIN_STRUCTURE("Protein structure"),
    SMALL_MOLECULES("Small molecules"),
    DISEASES("Diseases");

    private final String label;

    private CitationLabel(String label){
        this.label = label;
    }

    @Override
    public String toString(){ return label; }

    public String getCode(){ return name(); }

    public String getDisplayText(){ return label; }

    /**
     * Chooses the proper label for a database.
     * @param dbName a database name (letter case does not matter)
     * @return the proper label for the database, or <code>null</code> if the
     *      database is not known by this class.
     * @since 1.0.7
     */
    public static CitationLabel forDatabase(String dbName){
        CitationLabel label = null;
        if (dbName.equalsIgnoreCase("UNIPROT")) label = ENZYME;
        else if (dbName.equalsIgnoreCase("PDB")) label = PROTEIN_STRUCTURE;
        else if (dbName.equalsIgnoreCase("CHEBI")
                || dbName.equalsIgnoreCase("CHEMBL")) label = SMALL_MOLECULES;
        else if (dbName.equalsIgnoreCase("OMIM")) label = DISEASES;
        return label;
    }
}
