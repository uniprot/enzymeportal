package uk.ac.ebi.ep.mm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import org.apache.commons.cli.CommandLine;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * UniProt XML parser which takes into account only accessions, entry names
 * (IDs) and organisms, and indexes/stores them in a lucene index.
 * @author rafa
 *
 */
public class UniprotIndexer extends DefaultHandler implements MmIndexer {

	private static final String UNIPROT_ENTRY =
			"//uniprot/entry";

	private static final String UNIPROT_ENTRY_NAME =
			"//uniprot/entry/name";

	private static final String UNIPROT_ENTRY_ACCESSION =
			"//uniprot/entry/accession";

	private static final Object UNIPROT_ENTRY_DBREFERENCE =
			"//uniprot/entry/dbReference";

	private static final String UNIPROT_ENTRY_ORGANISM_NAME =
			"//uniprot/entry/organism/name";

	private static final Logger LOGGER = Logger.getLogger(UniprotIndexer.class);

	/**
	 * The current element (tree path) being parsed.
	 */
	private Stack<String> currentContext = new Stack<String>();
	
	/**
	 * The text value of the current element being parsed.
	 */
	protected StringBuilder currentChars = new StringBuilder();

	protected boolean isEntry;

	protected boolean isAccession;

	protected boolean isEntryName;

	protected boolean isOrgSciName;
	
	protected boolean isEcRef;

	/**
	 * The lucene index directory.
	 */
	private IndexWriter indexWriter;

	protected List<String> accessions = new ArrayList<String>();

	protected List<String> entryNames = new ArrayList<String>();

	protected List<String> ecs = new ArrayList<String>();

	protected String orgSciName;

	/**
	 * Parses a UniProt XML file and indexes/stores the UniProt accessions,
	 * IDs and organisms into a lucene index.
	 * @param args
	 * <ul>
	 * 	<li>-xmlFile: the XML file to parse</li>
	 * 	<li>-indexDir: the directory for the lucene index. If it does not
	 * 		exist, a new one is created.</li>
	 * </ul>
	 * @throws Exception in case of error while parsing.
	 */
	public static void main(String... args) throws Exception{
        CommandLine cl = CliOptionsParser.getCommandLine(args);
        if (cl != null){
    		MmIndexer parser = new UniprotIndexer();
    		parser.parse(cl.getOptionValue("xmlFile"), cl.getOptionValue("indexDir"));
        }
	}	
	
	/**
	 * Parses a UniProt XML file and indexes/stores the UniProt accessions,
	 * IDs and organisms into a lucene index.<br>
	 * This method is not thread safe.
	 * @param uniprotXml the XML file to parse
	 * @param luceneIndexDir the directory for the lucene index. If it does not
	 * 		exist, a new one is created.
	 * @throws FileNotFoundException if the UniProt XML file is not found
	 * 		or not readable.
	 * @throws SAXException if no default XMLReader can be found or
	 * 		instantiated, or exception during parsing.
	 * @throws IOException if the lucene index cannot be opened/created,
	 * 		or from the parser.
	 */
	public void parse(String uniprotXml, String luceneIndexDir)
	throws FileNotFoundException, SAXException, IOException {
		File uniprotXmlFile = new File(uniprotXml);
		File indexDir = getIndexDir(luceneIndexDir);
		indexWriter = new IndexWriter(
				new NIOFSDirectory(indexDir),
				new StandardAnalyzer(Version.LUCENE_30),
				MaxFieldLength.LIMITED);
		LOGGER.info("Index open to import UniProt entries");
		
		XMLReader xr = XMLReaderFactory.createXMLReader();
		xr.setContentHandler(this);
		xr.setErrorHandler(this);
		InputStream is = new FileInputStream(uniprotXmlFile);
		InputSource source = new InputSource(is);
		LOGGER.info("Parsing start");
		xr.parse(source);
		LOGGER.info("Parsing end");
		indexWriter.close();
		LOGGER.info("Index closed");
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		currentContext.push(localName);
		// Update flags:
		String currentXpath = getCurrentXpath();
		isEntry = UNIPROT_ENTRY.equals(currentXpath);
		isAccession = UNIPROT_ENTRY_ACCESSION.equals(currentXpath);
		isEntryName = UNIPROT_ENTRY_NAME.equals(currentXpath);
		isOrgSciName = UNIPROT_ENTRY_ORGANISM_NAME.equals(currentXpath)
				&& "scientific".equals(attributes.getValue("", "type"));
		isEcRef = UNIPROT_ENTRY_DBREFERENCE.equals(currentXpath)
				&& "EC".equals(attributes.getValue("", "type"));
		// Clear placeholder:
		currentChars.delete(0, Integer.MAX_VALUE);
		if (isEcRef){
			ecs.add(attributes.getValue("", "id"));
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// Check whether we need to do something:
		if (isAccession || isEntryName || isOrgSciName){
			currentChars.append(Arrays.copyOfRange(ch, start, start+length));
		}
	}

	/**
	 * Stores interesting data into the index.
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
	throws SAXException {
		if (isAccession){
			accessions.add(currentChars.toString());
		} else if (isEntryName){
			entryNames.add(currentChars.toString());
		} else if (isOrgSciName){
			orgSciName = currentChars.toString();
		} else if (isEntry){
			if (!ecs.isEmpty()){ // XXX here is the enzyme filter
				try {
					indexWriter.addDocument(buildDoc());
				} catch (Exception e) {
					throw new RuntimeException("Adding document to index", e);
				}
			}
			// Clean up:
			accessions.clear();
			entryNames.clear();
			ecs.clear();
			orgSciName = null;
		}
		currentContext.pop();
		// Update flags:
		String currentXpath = getCurrentXpath();
		isEntry = UNIPROT_ENTRY.equals(currentXpath);
		isAccession = false;
		isEntryName = false;
		isOrgSciName = false;
	}

	private Document buildDoc() {
		Document doc = new Document();
		if (!accessions.isEmpty()){
			for (String accession : accessions) {
				doc.add(new Field(MmField.UNIPROT_ACCESSION.name(), accession,
						Field.Store.YES, Field.Index.ANALYZED));
			}
		}
		if (!entryNames.isEmpty()){
			for (String entryName : entryNames) {
				doc.add(new Field(MmField.UNIPROT_NAME.name(), entryName,
						Field.Store.YES, Field.Index.ANALYZED));
			}
		}
		doc.add(new Field(MmField.SPECIES.name(), orgSciName,
				Field.Store.YES, Field.Index.ANALYZED));
		for (String ec : ecs) {
			doc.add(new Field(MmField.EC.name(), ec,
					Field.Store.YES, Field.Index.ANALYZED));
		}
		return doc;
	}

	protected File getIndexDir(String luceneIndexDir) throws IOException {
		File indexDir = new File(luceneIndexDir);
		if (indexDir.exists()){
			LOGGER.info("Using existing index directory: " + luceneIndexDir);
		} else {
			boolean created = indexDir.mkdirs();
			if (created){
				LOGGER.info("Created new index directory: " + luceneIndexDir);
			} else {
				throw new IOException("Could not create directory for the index: "
						+ luceneIndexDir);
			}
		}
		return indexDir;
	}

	protected String getCurrentXpath() {
		StringBuilder xpath = new StringBuilder("/");
		for (String string : currentContext) {
			xpath.append('/').append(string);
		}
		return xpath.toString();
	}
	
	protected String listToString(List<String> list){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			if (i > 0) sb.append(',');
			sb.append(list.get(i));
		}
		return sb.toString();
	}

}
