package uk.ac.ebi.ep.mm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.cli.CommandLine;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Parser for EB-Eye XML files to populate a mega-map.
 * Currently, this implementation has been used with ChEBI and
 * ChEMBL-target files (ChEMBL target is the
 * only one for this database which links compounds with protein IDs).
 * @author rafa
 *
 */
public class EbeyeSaxParser extends DefaultHandler implements MmParser {

	/**
	 * Cross-references to be extracted from the EB-Eye XML file, which depend
	 * on the file (database) being parsed.
	 */
	private static Map<MmDatabase, MmDatabase[]> interestingXrefs =
			new HashMap<MmDatabase, MmDatabase[]>();
	
	static {
		final MmDatabase[] chebiInterestingXrefs = new MmDatabase[]{
			MmDatabase.UniProt,
			MmDatabase.PDBeChem,
			MmDatabase.ChEMBL
		};
		Arrays.sort(chebiInterestingXrefs);
		interestingXrefs.put(MmDatabase.ChEBI, chebiInterestingXrefs);
		interestingXrefs.put(MmDatabase.PDBeChem,
				new MmDatabase[]{ MmDatabase.PDB });
		interestingXrefs.put(MmDatabase.ChEMBL,
				new MmDatabase[]{ MmDatabase.UniProt });
	}
	
	private static final String DATABASE_NAME = "//database/name";
	private static final String DATABASE_ENTRIES = "//database/entries";
	private static final String DATABASE_ENTRIES_ENTRY =
			DATABASE_ENTRIES + "/entry";
	private static final String DATABASE_ENTRIES_ENTRY_NAME = 
			DATABASE_ENTRIES_ENTRY + "/name";
	private static final String DATABASE_ENTRIES_ENTRY_XREFS =
			DATABASE_ENTRIES_ENTRY + "/cross_references";
	private static final String DATABASE_ENTRIES_ENTRY_XREFS_REF =
			DATABASE_ENTRIES_ENTRY_XREFS + "/ref";
	
	private final Logger LOGGER = Logger.getLogger(EbeyeSaxParser.class);

	/**
	 * The current element (tree path) being parsed.
	 */
	private Stack<String> currentContext = new Stack<String>();
	
	/**
	 * The text value of the current element being parsed.
	 */
	private StringBuilder currentChars = new StringBuilder();

	private boolean isDbName;
	private boolean isEntry;
	private boolean isEntryName;
	private boolean isXrefs;
	private boolean isRef;
	
	private MmDatabase db;
	private Entry entry;
	private Collection<XRef> xrefs = new HashSet<XRef>();
	
	private MegaMapper mm;
	
	/**
	 * Parses a EB-Eye XML file and indexes/stores the entry IDs, accessions
	 * and xrefs into a mega-map.
	 * @param args see {@link CliOptionsParser#getCommandLine(String...)}
	 * @throws Exception in case of error while parsing.
	 */
	public static void main(String... args) throws Exception{
        CommandLine cl = CliOptionsParser.getCommandLine(args);
        if (cl != null){
    		MmParser parser = new EbeyeSaxParser();
    		MegaMapper writer = cl.hasOption("indexDir")?
    				new MegaLuceneMapper(cl.getOptionValue("indexDir")):
    				new MegaDbMapper(cl.getOptionValue("dbConfig"), 1000);
    		parser.setWriter(writer);
    		parser.parse(cl.getOptionValue("xmlFile"));
        }
	}
	
	public void parse(String xmlFile) throws Exception {
		if (mm == null){
			// Don't go ahead:
			throw new NullPointerException("A MegaMapper must be configured");
		}
		File ebeyeFile = new File(xmlFile);
		LOGGER.info("Mega-map open to import EB-Eye entries");
		try {
        	mm.openMap();
            XMLReader xr = XMLReaderFactory.createXMLReader();
            xr.setContentHandler(this);
            xr.setErrorHandler(this);
            InputStream is = new FileInputStream(ebeyeFile);
            InputSource source = new InputSource(is);
            LOGGER.info("Parsing start");
            xr.parse(source);
            LOGGER.info("Parsing end");
            mm.closeMap();
            LOGGER.info("Map closed");
        } catch (Exception e){
            LOGGER.error("During parsing", e);
            mm.handleError();
            throw e;
        }
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		currentContext.push(localName);
		// Update flags:
		String currentXpath = getCurrentXpath();
		isDbName = DATABASE_NAME.equals(currentXpath);
		isEntry = DATABASE_ENTRIES_ENTRY.equals(currentXpath);
		isEntryName = DATABASE_ENTRIES_ENTRY_NAME.equals(currentXpath);
		isXrefs = DATABASE_ENTRIES_ENTRY_XREFS.equals(currentXpath);
		isRef = DATABASE_ENTRIES_ENTRY_XREFS_REF.equals(currentXpath);
		
		if (isEntry && !MmDatabase.ChEMBL_Target.equals(db)){
			entry = new Entry();
			entry.setDbName(db.name());
			entry.setEntryId(attributes.getValue("", "id"));
			entry.setEntryAccession(attributes.getValue("", "acc"));
			LOGGER.debug("Parsing entry " + entry.getEntryAccession());
		} else if (isXrefs){
			xrefs.clear();
		} else if (isRef){
			final MmDatabase refdDb =
					MmDatabase.parse(attributes.getValue("", "dbname"));
			String dbKey = attributes.getValue("", "dbkey");
			// Strange case: PDBeChem refers to PDBe using altkey:
			if (dbKey == null){
				dbKey = attributes.getValue("", "altkey");
			}
			if (MmDatabase.ChEMBL_Target.equals(db)
					&& MmDatabase.ChEMBL.equals(refdDb)){
				entry = new Entry();
				entry.setDbName(refdDb.name());
				entry.setEntryAccession(dbKey);
				LOGGER.debug("Parsing entry " + entry.getEntryAccession());
			} else if (isInterestingXref(db, refdDb)){
				Entry refEntry = new Entry();
				refEntry.setDbName(refdDb.name());
				refEntry.setEntryAccession(dbKey);
				LOGGER.debug("\tParsing xref to " + refEntry.getEntryAccession());
				XRef xref = new XRef();
				xref.setFromEntry(entry);
				xref.setRelationship(Relationship.between(db, refdDb).name());
				xref.setToEntry(refEntry);
				xrefs.add(xref);
			}
		}
		// Clear placeholder:
		currentChars.delete(0, Integer.MAX_VALUE);
	}

	/**
	 * Are we interested in a cross reference?
	 * @param db the database providing the EB-Eye XML file.
	 * @param refdDb the referenced database.
	 * @return <code>true</code> if the xref is interesting
	 * (see {@link #interestingXrefs}).
	 */
	private boolean isInterestingXref(MmDatabase db, MmDatabase refdDb) {
		return Arrays.binarySearch(interestingXrefs.get(db), refdDb) > -1;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (isDbName || isEntryName){
			currentChars.append(Arrays.copyOfRange(ch, start, start+length));
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (isDbName){
			db = MmDatabase.parse(currentChars.toString());
			LOGGER.info("Parsing EB-Eye file for " + db.name());
		} else if (isEntryName){
			entry.setEntryName(currentChars.toString());
		} else if (isEntry && !xrefs.isEmpty()){
			try {
				mm.write(Collections.singleton(entry), xrefs);
			} catch (IOException e) {
				throw new RuntimeException("Adding entry to mega-map");
			}
		}
		currentContext.pop();
		// Update flags:
		String currentXpath = getCurrentXpath();
		isXrefs = DATABASE_ENTRIES_ENTRY_XREFS.equals(currentXpath);
		isEntry = DATABASE_ENTRIES_ENTRY.equals(currentXpath);
		isDbName = false;
		isEntryName = false;
		isRef = false;
	}

	public void setWriter(MegaMapper writer) {
		this.mm = writer;
	}

	protected String getCurrentXpath() {
		StringBuilder xpath = new StringBuilder("/");
		for (String string : currentContext) {
			xpath.append('/').append(string);
		}
		return xpath.toString();
	}

}
