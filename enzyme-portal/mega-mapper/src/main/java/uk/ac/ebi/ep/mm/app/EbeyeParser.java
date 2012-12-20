package uk.ac.ebi.ep.mm.app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.cli.CommandLine;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;

import uk.ac.ebi.biobabel.lucene.LuceneParser;
import uk.ac.ebi.ebinocle.DatabaseType;
import uk.ac.ebi.ebinocle.EntriesType;
import uk.ac.ebi.ebinocle.EntryType;
import uk.ac.ebi.ebinocle.RefType;
import uk.ac.ebi.ep.mm.Entry;
import uk.ac.ebi.ep.mm.MegaDbMapper;
import uk.ac.ebi.ep.mm.MegaLuceneMapper;
import uk.ac.ebi.ep.mm.MegaMapper;
import uk.ac.ebi.ep.mm.MmDatabase;

/**
 * Indexer for EB-Eye XML files, which parses them and indexes/stores
 * database identifiers corresponding to UniProt accessions <i>already existing
 * in the index</i>.<br>
 * Currently, ChEBI and ChEMBL (target) are supported. PDBeChem is not, as it
 * does not seem to provide xrefs to UniProt in its EB-Eye XML file. 
 * @author rafa
 * @deprecated use EbeyeSaxParser instead, this one requires lots of memory
 * 		for big files.
 */
public class EbeyeParser implements MmParser {

	private static final Logger LOGGER = Logger.getLogger(EbeyeParser.class);

	private IndexSearcher indexSearcher;
	private IndexWriter indexWriter;
	
	/**
	 * Biobabel utility object to prepare query strings for lucene.
	 */
	private LuceneParser luceneParser = new LuceneParser();
	
	/**
	 * Lucene parser to prepare a query from a string.
	 */
	private QueryParser queryParser;

	/**
	 * The database we are indexing.
	 */
	private MmDatabase database;
	
	private MegaMapper mm;

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
    		MmParser parser = new EbeyeParser();
    		MegaMapper writer = cl.hasOption("dbConfig")?
    				new MegaDbMapper(cl.getOptionValue("dbConfig"), 1000):
    				new MegaLuceneMapper(cl.getOptionValue("indexDir"));
    		parser.setWriter(writer);
    		parser.parse(cl.getOptionValue("xmlFile"));
        }
	}

	public void parse(String xmlFile)
	throws Exception {
		if (mm == null){
			// Don't go ahead:
			throw new NullPointerException("A MegaMapper must be configured");
		}
		try {
			mm.openMap();
			LOGGER.info("Parsing start");
			EntriesType entries = getEntries(xmlFile);
			for (EntryType entry : entries.getEntry()) {
				// no cross-references? No use:
				if (entry.getCrossReferences() == null) continue;
				Entry ent = new Entry();
				ent.setEntryId(entry.getId());
				ent.setEntryName(entry.getName());
				ent.setEntryAccessions(Collections.singletonList(entry.getAcc()));
				ent.setDbName(database.name());
				mm.writeEntry(ent);
				
				for (RefType xref : entry.getCrossReferences().getRef()) {
					/*
					*/
				}
				List<String> uniprotAccs = new ArrayList<String>();
				for (RefType xref : entry.getCrossReferences().getRef()) {
					// Take only UniProt:
					if (MmDatabase.UniProt.equals(MmDatabase.parse(xref.getDbname()))){
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

                        Query query = queryParser.parse(getQuery(MmDatabase.UniProt, chunk));
                        ScoreDoc[] hits = indexSearcher.search(query, null, 1000).scoreDocs;
                        if (hits.length > 0){
                            List<Document> hitDocs = new ArrayList<Document>();
                            for (int j = 0; j < hits.length; j++) {
                                Document hitDoc = indexSearcher.doc(hits[j].doc);
                                hitDoc.add(new Field(
                                		database.getIdField(), ent.getEntryId(),
                                        Field.Store.YES, Field.Index.ANALYZED));
                                hitDoc.add(new Field(
                                		database.getNameField(), ent.getEntryName(),
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
			mm.closeMap();
		}
	}

	public void setWriter(MegaMapper writer) {
		this.mm = writer;
	}

	/**
	 * Prepares a query for lucene.
	 * @param db the database whose accessions are queried.
	 * @param accs list of accessions.
	 * @return a String ready for lucene, with OR'ed UniProt accessions.
	 */
	private String getQuery(MmDatabase db, List<String> accs) {
		return luceneParser.group(null, db.getAccessionField(),
				false, null, accs.toArray(new String[]{}));
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
}
