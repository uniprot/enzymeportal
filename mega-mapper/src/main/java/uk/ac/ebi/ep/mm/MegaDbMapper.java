package uk.ac.ebi.ep.mm;

import java.io.IOException;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Objects of this class write the mega-map to a database.
 * @author rafa
 *
 */
public class MegaDbMapper implements MegaMapper {

	private final Logger logger = Logger.getLogger(MegaDbMapper.class);
	private Session session;
	
	public MegaDbMapper(String dbConfig){
		SessionFactory sessionFactory =
				HibernateUtil.getSessionFactory(dbConfig);
		session = sessionFactory.getCurrentSession();
		session.setFlushMode(FlushMode.COMMIT);
	}
	
	public void openMap() throws IOException {
		session.beginTransaction();
		logger.info("Map opened");
	}

	public void writeEntry(Entry entry) throws IOException {
		session.merge(entry); // save or saveOrUpdate does not work!
		logger.debug(entry.getEntryId() + " written");
	}

	public void writeRelationship(Relationship relationship)
	throws IOException {
		session.merge(relationship); // save or saveOrUpdate does not work!
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
	}

}
