package uk.ac.ebi.ep.data.helper;

/**
 * Interface for parsers which deal with ebinocle XML files provided by many
 * EBI resources to be indexed for the EBI search engine.
 * @author rafa
 * @since 1.0.22
 */
public interface EbinocleParser {
    
    /*
     * XPaths for interesting elements of the ebinocle file.
     */
    public static final String DATABASE_NAME = "//database/name";
    
    public static final String DATABASE_ENTRIES = "//database/entries";
    
    public static final String DATABASE_ENTRIES_ENTRY =
            DATABASE_ENTRIES + "/entry";
    
    public static final String DATABASE_ENTRIES_ENTRY_NAME = 
            DATABASE_ENTRIES_ENTRY + "/name";
    
    public static final String DATABASE_ENTRIES_ENTRY_XREFS =
            DATABASE_ENTRIES_ENTRY + "/cross_references";
    
    public static final String DATABASE_ENTRIES_ENTRY_XREFS_REF =
            DATABASE_ENTRIES_ENTRY_XREFS + "/ref";

    public static final String DATABASE_ENTRIES_ENTRY_FIELDS =
            DATABASE_ENTRIES_ENTRY + "/additional_fields";

    public static final String DATABASE_ENTRIES_ENTRY_FIELD =
            DATABASE_ENTRIES_ENTRY_FIELDS + "/field";
}
