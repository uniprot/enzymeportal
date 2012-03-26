package uk.ac.ebi.ep.mm;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Interface for objects writing/reading entries and cross-references to/from
 * the mega-map.
 * @author rafa
 *
 */
public interface MegaMapper {

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
     * (not ID).
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
     * Retrieves cross references from the mega-map for a given accession
     * where database name is CheMBL.
     * (not ID).
     * @param db the database where the accession is from.
     * @param accession the accession number.
     * @param xDb the referencing/referenced database(s).
     * @return a List of xrefs in the map, or <code>null</code> if none
     * 		found. Note that the accession used in the query may be either the
     * 		origin or the target of an xref.
     */
    public List<XRef> getChMBLXrefs(MmDatabase db, String accession,
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
}
