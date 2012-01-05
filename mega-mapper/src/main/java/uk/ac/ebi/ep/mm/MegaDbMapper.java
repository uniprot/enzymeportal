package uk.ac.ebi.ep.mm;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Objects of this class write the mega-map to a database.
 * @author rafa
 *
 */
public class MegaDbMapper implements MegaMapper {

	private final Logger logger = Logger.getLogger(MegaDbMapper.class);
	protected Session session;
	private final int chunkSize;
	private int counter = 0;
	private SessionFactory sessionFactory;
	
	private Query entryForAccessionQuery,
		xrefsForAccessionQuery, allXrefsForAccessionQuery,
		xrefsForEntryQuery, allXrefsForEntryQuery;
	
	/**
	 * @param dbConfig file name for hibernate configuration. If
	 * 		<code>null</code>, defaults to hibernate default.
	 * @param chunkSize size of the chunks which will be committed to the
	 * 		database to avoid huge sessions and running out of memory.
	 */
	public MegaDbMapper(String dbConfig, int chunkSize){
		sessionFactory = HibernateUtil.getSessionFactory(dbConfig);
		this.chunkSize = chunkSize;
	}
	
	public void openMap() throws IOException {
		openNewSession();
	}

	private void openNewSession() {
		session = sessionFactory.getCurrentSession();
		session.setFlushMode(FlushMode.COMMIT);
		session.beginTransaction();
		entryForAccessionQuery = null;
		xrefsForAccessionQuery = null;
		allXrefsForAccessionQuery =  null;
		xrefsForEntryQuery = null;
		allXrefsForEntryQuery = null;
	}

	/**
	 * Checks if the session has reached the chunk size to commit.
	 */
	private void checkChunkSize() {
		if (counter++ >= chunkSize){
			session.getTransaction().commit();
			openNewSession();
			counter = 0;
		}
	}

	public void writeEntry(Entry entry) throws IOException {
        if (entry.getEntryName() != null && entry.getEntryName().length() > 300){
            logger.warn("[BIG: " + entry.getEntryName().length() + "] "
            		+ entry.getEntryName());
        }
		session.merge(entry); // save or saveOrUpdate does not work!
		logger.debug(entry.getEntryId() + " written");
		checkChunkSize();
	}

	public void writeXref(XRef xref)
	throws IOException {
		session.merge(xref); // save or saveOrUpdate does not work!
		logger.debug(xref.getFromEntry().getEntryAccession()
				+ "-" + xref.getToEntry().getEntryAccession()
				+ " written");
		checkChunkSize();
	}

	public void write(Collection<Entry> entries,
			Collection<XRef> xrefs) throws IOException {
		for (Entry entry : entries) {
			writeEntry(entry);
		}
		for (XRef xref : xrefs) {
			writeXref(xref);
		}
	}

	public Entry getEntryForAccession(MmDatabase db, String accession) {
		if (entryForAccessionQuery == null){
			entryForAccessionQuery = session.createQuery("from Entry where" +
					" dbName = :dbName and :accession = entryAccession");
		}
		return (Entry) entryForAccessionQuery
				.setString("dbName", db.name())
				.setString("accession", accession).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public Collection<XRef> getXrefs(Entry entry) {
		if (allXrefsForEntryQuery == null){
			allXrefsForEntryQuery = session.createQuery("from XRef" +
					" where fromEntry = :entry or toEntry = :entry");
		}
		return (List<XRef>) allXrefsForEntryQuery
				.setEntity("entry", entry)
				.list();
	}

	@SuppressWarnings("unchecked")
	public Collection<XRef> getXrefs(Entry entry, MmDatabase... db) {
		if (xrefsForEntryQuery == null){
			xrefsForEntryQuery = session.createQuery("from XRef" +
					" where (fromEntry = :entry and toEntry.dbName in (:dbNames))" +
					" or (toEntry = :entry and fromEntry.dbName in (:dbNames))");
		}
		return (List<XRef>) xrefsForEntryQuery
				.setEntity("entry", entry)
				.setParameterList("dbNames", db)
				.list();
	}

	public Collection<XRef> getXrefs(Collection<Entry> entries,
			MmDatabase... db) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	public Collection<XRef> getXrefs(MmDatabase db, String accession) {
		if (allXrefsForAccessionQuery == null){
			allXrefsForAccessionQuery = session.createQuery("from XRef" +
					" where (fromEntry.dbName = :dbName" +
					" and :accession = fromEntry.entryAccession)" +
					" or (toEntry.dbName = :dbName" +
					" and :accession = toEntry.entryAccession)");
		}
		return (List<XRef>) allXrefsForAccessionQuery
				.setString("dbName", db.name())
				.setString("accession", accession)
				.list();
	}

	@SuppressWarnings("unchecked")
	public Collection<XRef> getXrefs(MmDatabase db, String accession,
			MmDatabase... xDb) {
		if (xrefsForAccessionQuery == null){
			xrefsForAccessionQuery = session.createQuery("from XRef" +
					" where (fromEntry.dbName = :dbName" +
					" and :accession = fromEntry.entryAccession" +
					" and toEntry.dbName in (:xDbNames))" +
					" or (toEntry.dbName = :dbName" +
					" and :accession = toEntry.entryAccession" +
					" and fromEntry.dbName in (:xDbNames))");
		}
		return (List<XRef>) xrefsForAccessionQuery
				.setString("dbName", db.name())
				.setString("accession", accession)
				.setParameterList("xDbNames", xDb)
				.list();
	}

	public void handleError() throws IOException {
		session.getTransaction().rollback();
		logger.error("Session rolled back");
	}

	public void closeMap() throws IOException {
		session.flush();
		session.getTransaction().commit();
		logger.info("Session committed");
		sessionFactory.close();
	}

}
