package uk.ac.ebi.ep.mm;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import uk.ac.ebi.ep.search.model.Compound;

/**
 * Interface for objects writing/reading entries and cross-references to/from
 * the mega-map.
 * @author rafa
 *
 */
public interface MegaMapper {

	public enum Constraint { EQUALS, STARTS_WITH, CONTAINS, ENDS_WITH }
	
    /**
     * Opens the mega-map for writing.
     * @throws IOException
     */
    public void openMap() throws IOException;

    /**
     * Writes one entry to the mega-map.
     * @param entry
     * @throws IOException
     */
    public void writeEntry(Entry entry) throws IOException;

    public void writeEntries(Collection<Entry> entries) throws IOException;
    
  
    /**
     * Update entry (with reference to the entry_name)
     * @param entry
     * @return number of rows affected
     * @throws IOException 
     */
    int updateEntry(Entry entry) throws IOException;

    /**
     * Writes one cross-reference to the mega-map.
     * @param xref
     * @throws IOException
     */
    public void writeXref(XRef xref) throws IOException;

    public void writeXrefs(Collection<XRef> xrefs) throws IOException;

    /**
     * Writes entries and relationships to the mega-map.
     * @param entries
     * @param xrefs
     * @throws IOException
     */
    public void write(Collection<Entry> entries, Collection<XRef> xrefs)
            throws IOException;

    /**
     * Retrieves an entry for a given accession.
     * @param db
     * @param accession
     * @return an {@link Entry}, or <code>null</code> if not found.
     */
    public Entry getEntryForAccession(MmDatabase db, String accession);

    /**
     * Retrieves cross references from the mega-map regardless of the
     * referencing/referenced database.
     * @param entry The entry we want relationships for.
     * @return a collection of xrefs in the map, regardless of the
     * 		database. Note that the entry used in the query may be either the
     * 		origin or the target of an xref.
     */
    public Collection<XRef> getXrefs(Entry entry);

    /**
     * Retrieves cross references from the mega-map.
     * @param entry The entry we want relationships for.
     * @param db The database(s) to which the entry is related.
     * @return a collection of xrefs in the map. Note that the entry
     * 		used in the query may be either the origin or the target of an
     * 		xref.
     */
    public Collection<XRef> getXrefs(Entry entry, MmDatabase... db);

    /**
     * Retrieves cross references from the mega-map for several entries.
     * @param entries The entries we want relationships for.
     * @param db The database(s) to which the entries are related.
     * @return a collection of xrefs in the map. Note that the entries
     * 		used in the query may be either the origin or the target of an
     * 		xref.
     */
    public Collection<XRef> getXrefs(Collection<Entry> entries, MmDatabase... db);

    /**
     * Retrieves cross references from the mega-map for a given accession
     * (not ID) regardless of the referencing/referenced database..
     * @param db the database where the accession is from.
     * @param accession the accession number.
     * @return a collection of xrefs in the map. Note that the accession
     * 		used in the query may be either the origin or the target of an
     * 		xref.
     */
    public Collection<XRef> getXrefs(MmDatabase db, String accession);

    /**
     * Retrieves cross references from the mega-map for a given accession
     * (not ID) to/from the given database(s).
     * @param db the database where the accession is from.
     * @param accession the accession number.
     * @param xDb the referencing/referenced database(s).
     * @return a collection of xrefs in the map, or <code>null</code> if none
     * 		found. Note that the accession used in the query may be either the
     * 		origin or the target of an xref.
     */
    public Collection<XRef> getXrefs(MmDatabase db, String accession,
            MmDatabase... xDb);
    
    /**
     * Retrieves cross references from the mega-map for a given accession (not
     * ID) given a concrete relationship.
     * @param db the database where the accession is from.
     * @param accession the accession number.
     * @param relationship the relationship between the given accession and its
     * 		cross references.
     * @return a collection of xrefs in the map, or <code>null</code> if none
     * 		found. Note that the accession used in the query may be either the
     * 		origin or the target of an xref.
     */
    public Collection<XRef> getXrefs(MmDatabase db, String accession,
    		Relationship relationship);

    /**
     * Retrieves cross references from the mega-map for a given text which
     * matches an entry ID, who has a given relationship (if any).<br>
     * This method is provided in order to search for UniProt ID prefixes (ex.
     * "CFTR_") but can be used for other purposes as well.
     * @param db the Database providing IDs matching <code>idFragment</code>. If
     * 		<code>null</code>, all databases will be considered.
     * @param idFragment A text which matches entry IDs. Please note that when
     * 		searching for UniProt ID prefixes it is advisable to include the
     * 		underscore (_) character, so that "DHSA" won't return xrefs for
     * 		"DHSA1".
     * @param constraint the constraint to use with the given
     * 		<code>idFragment</code>.
     * @param relationship The relationship searched, if any (can be
     * 		<code>null</code>).
     * @return a collection of xrefs in the map, or <code>null</code> if none
     * 		found. Note that the entries corresponding to the query may be
     * 		either the origin or the target of an xref.
     */
    public Collection<XRef> getXrefs(MmDatabase db, String idFragment,
    		Constraint constraint, Relationship relationship);

    /**
     * Retrieves cross references from the mega-map for a given text which
     * matches an entry ID, who is related to the given databases.<br>
     * This method is provided in order to search for UniProt ID prefixes (ex.
     * "CFTR_") but can be used for other purposes as well.
     * @param db the Database providing IDs matching <code>idFragment</code>. If
     * 		<code>null</code>, all databases will be considered.
     * @param idFragment A text which matches entry IDs. Please note that when
     * 		searching for UniProt ID prefixes it is advisable to include the
     * 		underscore (_) character, so that "DHSA" won't return xrefs for
     * 		"DHSA1".
     * @param constraint the constraint to use with the given
     * 		<code>idFragment</code>.
     * @param xDbs the databases for which we want cross-references to be
     * 		retrieved.
     * @return a collection of xrefs in the map, or <code>null</code> if none
     * 		found. Note that the entries corresponding to the query may be
     * 		either the origin or the target of an xref.
     */
	public Collection<XRef> getXrefs(MmDatabase db, String idFragment,
			Constraint constraint, MmDatabase... xDbs);
    
    /**
     * Retrieves Entries from the mega-map for a given accession
     * where database name is CheMBL.
     * (not ID).
     * @param db the database where the accession is from.
     * @param accession the accession number.
     * @param xDb the referencing/referenced database(s).
     * @return a List of entries in the map, or <code>null</code> if none
     * 		found. Note that the accession used in the query may be either the
     * 		origin or the target of an xref.
     */
    public List<Entry> getChMBLEntries(MmDatabase db, String accession,
            MmDatabase... xDb);

    /**
     * retrieves the total number of Xrefs found for a given accession.
     * @param db the database where the accession is found
     * @param accession the accession number
     * @param xDb the referencing/referenced database(s).
     * @return the total number of Xrefs found or 0 if none was found.
     */
    int getXrefsSize(MmDatabase db, String accession,
            MmDatabase... xDb);

    /**
     * Handles any errors which might affect the mega-map.
     * @throws IOException
     */
    public void handleError() throws IOException;

    /**
     * Closes the mega-map.
     * @throws IOException
     */
    public void closeMap() throws IOException;

    /**
     * Commits any pending changes to the underlying mega-map.
     * @throws IOException 
     */
    public void commit() throws IOException;

    /**
     * Rolls back any pending changes to the underlying mega-map.
     * @throws IOException 
     */
    public void rollback() throws IOException;

    List<String> getAllUniProtAccessions(MmDatabase database);

    /**
     * Retrieves compounds related to a UniProt ID.
     * @param uniprotId A UniProt ID (name). If it ends with underscore ('_') it
     *      is treated as a prefix in order to get compounds for all of the
     *      orthologs.
     * @return a collection of compounds related to the UniProt entry -
     *      activators, inhibitors, cofactors, drugs, bioactive compounds,
     *      reaction participants - or <code>null</code> if none found.
     */
    Collection<Compound> getCompounds(String uniprotId);

    /**
     *
     * @param db
     * @param uniprotId
     * @param xDbs
     * @return
     * @deprecated db is always UniProt. Use {@link #getCompounds(String)}
     *      instead.
     */
    Map<?,?> getCompounds(MmDatabase db, String uniprotId,
            MmDatabase... xDbs);
   
     Map<String, String> getDiseaseByUniprotId(MmDatabase db, String accessions,
            MmDatabase... xDbs);
        Map<String, String> getDiseaseByAccession(MmDatabase db, String accessions,
            MmDatabase... xDbs);
    
     /**
      * 
      * @param database the database to get the entry ids from
      * @return all entry Id's
      */
    List<String> getAllEntryIds(MmDatabase database);
    
    ResultSet getAllEntryIds(MmDatabase database, String query);
}
