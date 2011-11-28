package uk.ac.ebi.ep.mm;

import java.io.IOException;
import java.util.Collection;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Objects of this class write the mega-map to a database.
 * @author rafa
 *
 */
public class MegaDbMapper implements MegaMapper {

	private SessionFactory sessionFactory;
	private Session session;
	
	public MegaDbMapper(String dbConfig){
		sessionFactory = HibernateUtil.getSessionFactory(dbConfig);
		session = sessionFactory.getCurrentSession();
	}
	
	@Override
	public void openMap() throws IOException {
		session.beginTransaction();
	}

	@Override
	public void writeEntry(Entry entry) throws IOException {
		session.save(entry);
	}

	@Override
	public void writeRelationship(Relationship relationship) throws IOException {
		session.save(relationship);
	}

	@Override
	public void write(Collection<Entry> entries,
			Collection<Relationship> relationships) throws IOException {
		for (Entry entry : entries) {
			session.save(entry);
		}
		for (Relationship relationship : relationships) {
			session.save(relationship);
		}
	}

	@Override
	public Collection<Relationship> queryMap(Entry entry, MmDatabase db) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Relationship> queryMap(Collection<Entry> entries,
			MmDatabase db) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handleError() throws IOException {
		session.getTransaction().rollback();
	}

	@Override
	public void closeMap() throws IOException {
		session.getTransaction().commit();
	}

}
