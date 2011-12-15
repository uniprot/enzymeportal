package uk.ac.ebi.ep.mm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Stack;

import org.apache.commons.cli.CommandLine;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class EbeyeSaxParser extends DefaultHandler implements MmParser {

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
	private Collection<XRef> rels = new HashSet<XRef>();
	
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
		
		if (isEntry){
			entry = new Entry();
			entry.setDbName(db.name());
			entry.setEntryId(attributes.getValue("", "id"));
			entry.setAccessions(Collections.singleton(attributes.getValue("", "acc")));
		} else if (isXrefs){
			rels.clear();
		} else if (isRef){
			final MmDatabase refdDb =
					MmDatabase.parse(attributes.getValue("", "dbname"));
			// XXX: we are considering only UniProt xrefs
			if (MmDatabase.UniProt.equals(refdDb)){
				final String uniprotAccession = attributes.getValue("", "dbkey");
				Entry refEntry = mm.getEntryForAccession(
						MmDatabase.UniProt.name(), uniprotAccession);
				if (refEntry != null){
					XRef rel = new XRef();
					rel.setFromEntry(entry);
					rel.setRelationship(Relationship.between(db, refdDb));
					rel.setToEntry(refEntry);
					rels.add(rel);
				}
			}
		}
		// Clear placeholder:
		currentChars.delete(0, Integer.MAX_VALUE);
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
		} else if (isEntryName){
			entry.setEntryName(currentChars.toString());
		} else if (isEntry){
			try {
				mm.write(Collections.singleton(entry), rels);
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
