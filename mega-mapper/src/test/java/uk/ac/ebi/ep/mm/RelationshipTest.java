package uk.ac.ebi.ep.mm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RelationshipTest {

	private SessionFactory sessionFactory;
	List<Integer> entityIds = new ArrayList<Integer>();
	
	@Before
	public void before(){
		sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		Entry entity1 = new Entry();
		entity1.setDbName(MmDatabase.UniProt.name());
		entity1.setEntryId("ABCD_HUMAN");
		entityIds.add((Integer) session.save(entity1));
		Entry entity2 = new Entry();
		entity2.setDbName(MmDatabase.ChEBI.name());
		entity2.setEntryId("CHEBI:12345");
		entityIds.add((Integer) session.save(entity2));
		session.getTransaction().commit();
	}
	
	@After
	public void after(){
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		String relQuery = "DELETE Relationship WHERE fromEntry = :fromEnt OR toEntry = :toEnt";
		String entQuery = "DELETE Entry WHERE id = :theId";
		for (Integer id : entityIds) {
			int n = session.createQuery(relQuery)
						.setInteger("fromEnt", id)
						.setInteger("toEnt", id)
						.executeUpdate();
			System.out.println(n + " relationships deleted");
			int m = session.createQuery(entQuery)
						.setInteger("theId", id)
						.executeUpdate();
			System.out.println(m + " entries deleted");
		}
		session.getTransaction().commit();
    	sessionFactory.close();
	}
	
	@Test
	public void test() {
        try {
    		Session session = sessionFactory.getCurrentSession();
    		session.beginTransaction();
    		Entry ent1 = (Entry) session.get(Entry.class, entityIds.get(0));
    		Entry ent2 = (Entry) session.get(Entry.class, entityIds.get(1));
    		Relationship rel = new Relationship();
    		rel.setFromEntry(ent2);
    		rel.setToEntry(ent1);
    		rel.setRelationship(Relationships.is_drug_for.toString());
    		session.save(rel);
    		session.getTransaction().commit();
    		
    		session = sessionFactory.getCurrentSession();
    		session.beginTransaction();
    		final Query fromQuery =
    				session.createQuery("FROM Relationship WHERE fromEntry = :fromEnt");
    		final Query toQuery =
    				session.createQuery("FROM Relationship WHERE toEntry = :toEnt");
			assertEquals(0, fromQuery.setEntity("fromEnt", ent1).list().size());
			assertEquals(1, fromQuery.setEntity("fromEnt", ent2).list().size());
			assertEquals(1, toQuery.setEntity("toEnt", ent1).list().size());
			assertEquals(0, toQuery.setEntity("toEnt", ent2).list().size());
        } catch (Exception e){
        	fail();
        }
	}

}
