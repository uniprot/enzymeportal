package uk.ac.ebi.ep.mm;

import java.io.IOException;
import java.util.Collection;

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
	
	/**
	 * Writes one relationship to the mega-map.
	 * @param relationship
	 * @throws IOException
	 */
	public void writeRelationship(Relationship relationship) throws IOException;
	
	/**
	 * Writes entries and relationships to the mega-map.
	 * @param entries
	 * @param relationships
	 * @throws IOException
	 */
	public void write(Collection<Entry> entries, Collection<Relationship> relationships)
	throws IOException;
	
	/**
	 * Retrieves an entry for a given accession.
	 * @param dbName
	 * @param accession
	 * @return an {@link Entry}.
	 */
	public Entry getEntryForAccession(String dbName, String accession);
	
	/**
	 * Makes a query to the map.
	 * @param entry The entry we want relationships for.
	 * @param db The database to which the entry is related. If
	 * 		<code>null</code>, all available databases should be considered.
	 * @return a collection of related entries in the map.
	 */
	public Collection<Relationship> queryMap(Entry entry, MmDatabase db);
	
	/**
	 * Makes a query to the map.
	 * @param entries The entries we want relationships for.
	 * @param db The database to which the entry is related. If
	 * 		<code>null</code>, all available databases should be considered.
	 * @return a collection of related entries in the map.
	 */
	public Collection<Relationship> queryMap(Collection<Entry> entries, MmDatabase db);
	
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

}
