package uk.ac.ebi.ep.mm;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;

/**
 * Mega-mapper writing/reading to/from a lucene index.
 * @author rafa
 *
 */
public class MegaLuceneMapper implements MegaMapper {

	private static final Logger LOGGER = Logger.getLogger(MegaLuceneMapper.class);
	
	/**
	 * The lucene index writer.
	 */
	private IndexWriter indexWriter;
	
	/**
	 * The lucene index searcher.
	 */
	private IndexSearcher indexSearcher;

	/**
	 * The directory containing the index files.
	 */
	private String luceneIndexDir;

	private QueryParser queryParser;

	public MegaLuceneMapper(String luceneIndexDir) {
		this.luceneIndexDir = luceneIndexDir;
	}

	public void openMap() throws IOException {
		File indexDir = getIndexDir(luceneIndexDir);
		Directory directory = new NIOFSDirectory(indexDir);
		final StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
		indexSearcher = new IndexSearcher(directory, false);
		indexWriter = new IndexWriter(directory, analyzer, MaxFieldLength.LIMITED);
		// The lucene parses is for the UniProt accession field:
		queryParser = new QueryParser(Version.LUCENE_30,
				MmDatabase.UniProt.getAccessionField(), analyzer);
	}

	public void writeEntry(Entry entry) throws CorruptIndexException, IOException {
		Document doc = new Document();
		addEntryToDoc(doc, entry);
		indexWriter.addDocument(doc);
	}

	/**
	 * {@inheritDoc}<br>
	 * Currently, this implementation just adds the entries at both ends of the
	 * relationship to the same lucene document.
	 */
	public void writeRelationship(Relationship relationship)
	throws CorruptIndexException, IOException {
		Document doc = new Document();
		addEntryToDoc(doc, relationship.getFromEntry());
		addEntryToDoc(doc, relationship.getToEntry());
		indexWriter.addDocument(doc);
	}

	/**
	 * @throws IOException 
	 * @throws CorruptIndexException 
	 */
	public void write(Collection<Entry> entries, Collection<Relationship> rels)
			throws CorruptIndexException, IOException {
		Document doc = new Document();
		// FIXME: before creating a new doc, check whether there is one already
		// for any of these entries.
		for (Entry entry : entries) {
			addEntryToDoc(doc, entry);
		}
		for (Relationship rel : rels) {
			addEntryToDoc(doc, rel.getFromEntry());
			addEntryToDoc(doc, rel.getToEntry());
		}
		indexWriter.addDocument(doc);
	}

	public Collection<Relationship> queryMap(Entry entry, MmDatabase db) {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<Relationship> queryMap(Collection<Entry> entries,
			MmDatabase db) {
		// TODO Auto-generated method stub
		return null;
	}

	public void handleError() throws IOException {
        indexWriter.rollback();
	}

	public void closeMap() throws IOException {
        if (indexWriter != null){
        	indexWriter.commit();
        	indexWriter.close();
        }
        if (indexSearcher != null){
        	indexSearcher.close();
        }
	}

	/**
	 * @param doc
	 * @param entry
	 */
	private void addEntryToDoc(Document doc, Entry entry) {
		doc.add(new Field(entry.getDbName() + "_ID", entry.getEntryId(),
				Field.Store.YES, Field.Index.ANALYZED));
		if (entry.getEntryName() != null){
			String fieldName = entry.getDbName() + "_NAME";
			doc.add(new Field(fieldName, entry.getEntryName(),
					Field.Store.YES, Field.Index.ANALYZED));
		}
		if (entry.getAccessions() != null){
			String fieldName = entry.getDbName() + "_ACCESSION";
			for (String accession : entry.getAccessions()) {
				doc.add(new Field(fieldName, accession,
						Field.Store.YES, Field.Index.ANALYZED));
			}
		}
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

	@Override
	protected void finalize() throws Throwable {
		closeMap();
	}

	public Entry getEntryForAccession(String dbName, String accession) {
		// TODO Auto-generated method stub
		return null;
	}

}
