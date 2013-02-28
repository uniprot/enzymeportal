package uk.ac.ebi.ep.mm.app;

import uk.ac.ebi.ep.mm.MegaMapper;

/**
 * Interface for objects parsing data for the mega-mapper.
 * @author rafa
 *
 */
public interface MmParser {

	/**
	 * Parses a file and extracts interesting data into a mega-map.
	 * @param file a file (XML, HTML, tab).
	 * @throws Exception 
	 */
	public void parse(String file) throws Exception;

	/**
	 * Sets a writer to make the mega-map persistent.
	 * @param writer
	 */
	public void setWriter(MegaMapper writer);
}
