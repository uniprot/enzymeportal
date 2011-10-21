package uk.ac.ebi.ep.mm;

/**
 * Interface for objects indexing data into the mega-mapper.
 * @author rafa
 *
 */
public interface MmIndexer {

	/**
	 * Parses a XML file and extracts interesting data into a lucene index.
	 * @param xmlFile an XML file.
	 * @param luceneIndexDir a directory for a lucene index.
	 * @throws Exception 
	 */
	public void parse(String xmlFile, String luceneIndexDir)
	throws Exception;
}
