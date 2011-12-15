package uk.ac.ebi.ep.mm;

import java.io.IOException;
import java.util.Collection;

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
	
	private Query entryForAccessionQuery;
	
	/**
	 * @param dbConfig file name for hibernate configuration. If
	 * 		<code>null</code>, defaults to hibernate default.
	 * @param chunkSize size of the chunks which will be commited to the
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
		entryForAccessionQuery = session.createQuery("from Entry where" +
				" dbName = :dbName and :accession in elements(accessions)");
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
        if (entry.getEntryName().length() > 300){
            logger.warn("[BIG] " + entry.getEntryName());
        }
		session.merge(entry); // save or saveOrUpdate does not work!
		logger.debug(entry.getEntryId() + " written");
		checkChunkSize();
	}

	public void writeRelationship(Relationship relationship)
	throws IOException {
		session.merge(relationship); // save or saveOrUpdate does not work!
		logger.debug(relationship.getFromEntry().getEntryId()
				+ "-" + relationship.getToEntry().getEntryId()
				+ " written");
		checkChunkSize();
	}

	public void write(Collection<Entry> entries,
			Collection<Relationship> relationships) throws IOException {
		for (Entry entry : entries) {
			writeEntry(entry);
		}
		for (Relationship relationship : relationships) {
			writeRelationship(relationship);
		}
	}

	public Entry getEntryForAccession(String dbName, String accession) {
//		session.createCriteria(Entry.class).add(Restrictions.and(
//				Property.forName("dbName").eq(dbName),
//				Property.forName("accessions").))
		return (Entry) entryForAccessionQuery
				.setString("dbName", dbName)
				.setString("accession", accession).uniqueResult();
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
		session.getTransaction().rollback();
		logger.error("Session rolled back");
	}

	public void closeMap() throws IOException {
		session.flush();
		session.getTransaction().commit();
		logger.error("Session committed");
		sessionFactory.close();
	}

}
