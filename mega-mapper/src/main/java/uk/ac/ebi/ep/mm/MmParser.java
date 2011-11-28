package uk.ac.ebi.ep.mm;

/**
 * Interface for objects parsing data for the mega-mapper.
 * @author rafa
 *
 */
public interface MmParser {

	/**
	 * Parses a XML file and extracts interesting data into a mega-map.
	 * @param xmlFile an XML file.
	 * @throws Exception 
	 */
	public void parse(String xmlFile) throws Exception;

	/**
	 * Sets a writer to make the mega-map persistent.
	 * @param writer
	 */
	public void setWriter(MegaMapper writer);
}
