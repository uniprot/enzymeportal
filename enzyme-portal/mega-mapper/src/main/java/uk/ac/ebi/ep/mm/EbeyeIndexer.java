package uk.ac.ebi.ep.mm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.cli.CommandLine;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;

import uk.ac.ebi.biobabel.lucene.LuceneParser;
import uk.ac.ebi.ebinocle.DatabaseType;
import uk.ac.ebi.ebinocle.EntriesType;
import uk.ac.ebi.ebinocle.EntryType;
import uk.ac.ebi.ebinocle.RefType;

/**
 * Indexer for EB-Eye XML files, which parses them and indexes/stores
 * database identifiers corresponding to UniProt accessions <i>already existing
 * in the index</i>.<br>
 * Currently, ChEBI and ChEMBL (target) are supported. PDBeChem is not, as it
 * does not seem to provide xrefs to UniProt in its EB-Eye XML file. 
 * @author rafa
 *
 */
public class EbeyeIndexer implements MmIndexer {

	private static final Logger LOGGER = Logger.getLogger(EbeyeIndexer.class);

	private IndexSearcher indexSearcher;
	private IndexWriter indexWriter;
	
	/**
	 * Biobabel utility object to prepare query strings for lucene.
	 */
	private LuceneParser luceneParser = new LuceneParser();
;
	
	/**
	 * Lucene parser to prepare a query from a string.
	 */
	private QueryParser queryParser;

	/**
	 * The database we are indexing.
	 */
	private MmDatabase database;

	/**
	 * Parses a EB-Eye XML file and indexes/stores the database identifiers
	 * corresponding to UniProt accessions.
	 * @param args
	 * <ul>
	 * 	<li>-xmlFile: the XML file to parse</li>
	 * 	<li>-indexDir: the directory for the lucene index. If it does not
	 * 		exist, a new one is created.</li>
	 * </ul>
	 * @throws Exception
	 */
	public static void main(String... args) throws Exception {
        CommandLine cl = CliOptionsParser.getCommandLine(args);
        if (cl != null){
    		MmIndexer parser = new EbeyeIndexer();
    		parser.parse(cl.getOptionValue("xmlFile"), cl.getOptionValue("indexDir"));
        }
	}

	public void parse(String xmlFile, String luceneIndexDir)
	throws Exception {
		openDirectory(luceneIndexDir);
		try {
			LOGGER.info("Parsing start");
			EntriesType entries = getEntries(xmlFile);
			for (EntryType entry : entries.getEntry()) {
				if (entry.getCrossReferences() == null) continue;
		    	String idAcc = null; // ID or accession
		    	String name = null; // entry name
		    	switch(database){
		    	case ChEMBL:
		    		// ChEMBL does not provide accession, just ID:
		    		idAcc = entry.getId();
		    		break;
	    		default:
		    		idAcc = entry.getAcc();
		    		name = entry.getName();
		    	}
				List<String> uniprotAccs = new ArrayList<String>();
				for (RefType xref : entry.getCrossReferences().getRef()) {
					// Take only UniProt:
					if (xref.getDbname().equalsIgnoreCase("UniProt")
							|| xref.getDbname().equalsIgnoreCase("SWISS-PROT")){
						uniprotAccs.add(xref.getDbkey());
					}
				}
				if (!uniprotAccs.isEmpty()){
					// We may associate with the accession if it is in the index.
				    // We might have too many xrefs for lucene to handle (max. is
					// 1024 by default), let's pass them in chunks:
                    int chunkSize = 1024;
                    for (int i = 0; i < ((uniprotAccs.size() % chunkSize) + 1); i++){
                        int from = i * chunkSize;
                        int to = from + chunkSize - 1;
                        if (to > uniprotAccs.size()) to = uniprotAccs.size();
                        List<String> chunk = uniprotAccs.subList(from, to); 

                        Query query = queryParser.parse(getQuery(chunk));
                        ScoreDoc[] hits = indexSearcher.search(query, null, 1000).scoreDocs;
                        if (hits.length > 0){
                            List<Document> hitDocs = new ArrayList<Document>();
                            for (int j = 0; j < hits.length; j++) {
                                Document hitDoc = indexSearcher.doc(hits[j].doc);
                                hitDoc.add(new Field(
                                		database.getIdField().name(), idAcc,
                                        Field.Store.YES, Field.Index.ANALYZED));
                                hitDoc.add(new Field(
                                		database.getNameField().name(), name,
                                        Field.Store.YES, Field.Index.ANALYZED));
                                hitDocs.add(hitDoc);
                            }
                            // Write changes
                            indexWriter.deleteDocuments(query);
                            for (Document hitDoc : hitDocs) {
                                indexWriter.addDocument(hitDoc);
                            }
                        }

                        if (to == uniprotAccs.size()) break;
                    }
				}
			}
			LOGGER.info("Parsing end");
        } catch (Exception e){
            LOGGER.error("During parsing", e);
            indexWriter.rollback();
            throw e;
		} finally {
			closeDirectory();
		}
	}

	/**
	 * Prepares a query for lucene.
	 * @param uniprotAccs list of UniProt accessions.
	 * @return a String ready for lucene, with OR'ed UniProt accessions.
	 */
	private String getQuery(List<String> uniprotAccs) {
		return luceneParser.group(null, MmField.UNIPROT_ACCESSION.name(),
				false, null, uniprotAccs.toArray(new String[]{}));
	}

	/**
	 * Detects the database being processed - stored in the field
	 * <code>dbField</code> - and returns the entries in it.
	 * @param xmlFile an EB-Eye XML file.
	 * @return the entries in the XML file.
	 * @throws JAXBException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private EntriesType getEntries(String xmlFile)
	throws JAXBException, FileNotFoundException, IOException {
		LOGGER.info("Getting entries from EB-Eye XML file.");
		JAXBContext jc = JAXBContext.newInstance( "uk.ac.ebi.ebinocle" );
		Unmarshaller u = jc.createUnmarshaller();
		FileInputStream fis = null;
		EntriesType entries = null;
		try {
			fis = new FileInputStream(xmlFile);
			@SuppressWarnings("unchecked")
			JAXBElement<DatabaseType> elem = (JAXBElement<DatabaseType>) u.unmarshal(fis);
			DatabaseType db = (DatabaseType) elem.getValue();
			String dbName = db.getName();
			LOGGER.info("Getting entries for " + dbName);
			if (dbName.toUpperCase().startsWith("CHEBI")){
				database = MmDatabase.ChEBI;
//			} else if (dbName.toUpperCase().startsWith("CHEMBL")){
//				database = MmDatabase.ChEMBL;
			} else {
				throw new RuntimeException("Database not supported: " + dbName);
			}
			entries = db.getEntries();
			LOGGER.info("Got them!");
		} catch (FileNotFoundException e) {
			LOGGER.error("XML file not found", e);
			throw e;
		} finally {
			if (fis != null) fis.close();
		}
		return entries;
	}

	/**
	 * Opens the lucene directory and prepares objects required for its use.
	 * @param luceneIndexDir
	 * @throws IOException
	 * @throws CorruptIndexException
	 * @throws LockObtainFailedException
	 */
	private void openDirectory(String luceneIndexDir)
	throws IOException, CorruptIndexException, LockObtainFailedException {
		File indexDir = getIndexDir(luceneIndexDir);
		Directory directory = new NIOFSDirectory(indexDir);
		final StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
		indexSearcher = new IndexSearcher(directory, false);
		indexWriter = new IndexWriter(directory, analyzer, MaxFieldLength.LIMITED);
		// The lucene parses is for the UniProt accession field:
		queryParser = new QueryParser(Version.LUCENE_30,
				MmField.UNIPROT_ACCESSION.name(), analyzer);
//		BooleanQuery.setMaxClauseCount(4096);
		LOGGER.info("Index opened");
	}
	
	private void closeDirectory() throws IOException {
		if (indexSearcher != null) indexSearcher.close();
		if (indexWriter != null) indexWriter.close();
		LOGGER.info("Index closed");
	}

	/**
	 * Checks if the directory already exists.
	 * @param luceneIndexDir
	 * @return a File object for an existing directory.
	 * @throws IllegaArgumentException if the directory does not exist.
	 */
	protected File getIndexDir(String luceneIndexDir) {
		File indexDir = new File(luceneIndexDir);
		if (indexDir.exists()){
			LOGGER.info("Using existing index directory: " + luceneIndexDir);
		} else {
			throw new IllegalArgumentException("No lucene index directory found at "
					+ luceneIndexDir);
		}
		return indexDir;
	}

}
