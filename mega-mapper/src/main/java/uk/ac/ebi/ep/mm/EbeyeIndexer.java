package uk.ac.ebi.ep.mm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
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
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;

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
	private QueryParser luceneParser;
	private MmField dbField;

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
	throws JAXBException, IOException, ParseException {
		openDirectory(luceneIndexDir);
		EntriesType entries = getEntries(xmlFile);
		for (EntryType entry : entries.getEntry()) {
			List<String> uniprotAccs = new ArrayList<String>();
			for (RefType xref : entry.getCrossReferences().getRef()) {
				// Take only UniProt:
				if (xref.getDbname().equalsIgnoreCase("UniProt")
						|| xref.getDbname().equalsIgnoreCase("SWISS-PROT")){
					uniprotAccs.add(xref.getDbkey());
				}
			}
			if (!uniprotAccs.isEmpty()){
				// We may associate with the accession if it is in the index
			    Query query = luceneParser.parse(getQuery(uniprotAccs));
			    ScoreDoc[] hits = indexSearcher.search(query, null, 1000).scoreDocs;
			    if (hits.length > 0){
			    	String idAcc = null; // ID or accession
			    	switch(dbField){
			    	case CHEMBL_ID:
			    		// ChEMBL does not provide accession, just ID:
			    		idAcc = entry.getId();
			    		break;
		    		default:
			    		idAcc = entry.getAcc();
			    	}
			    	for (int i = 0; i < hits.length; i++) {
						Document hitDoc = indexSearcher.doc(hits[i].doc);
						hitDoc.add(new Field(dbField.name(), idAcc,
								Field.Store.YES, Field.Index.ANALYZED));
					}
			    }
			}
		}
		// TODO: write changes
		closeDirectory();
	}

	private String getQuery(List<String> uniprotAccs) {
		// TODO Auto-generated method stub
		return null;
	}

	private EntriesType getEntries(String xmlFile)
	throws JAXBException, FileNotFoundException, IOException {
		JAXBContext jc = JAXBContext.newInstance( "uk.ac.ebi.ebinocle" );
		Unmarshaller u = jc.createUnmarshaller();
		FileInputStream fis = null;
		EntriesType entries = null;
		try {
			fis = new FileInputStream(xmlFile);
			entries = (EntriesType) u.unmarshal(fis);
			// TODO get dbField
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
		luceneParser = new QueryParser(Version.LUCENE_30,
				MmField.UNIPROT_ACCESSION.name(), analyzer);
	}
	
	private void closeDirectory() throws IOException {
		if (indexSearcher != null) indexSearcher.close();
		if (indexWriter != null) indexWriter.close();
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
