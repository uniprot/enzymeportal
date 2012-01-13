package uk.ac.ebi.ep.mm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Collections;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/*
 * Long test times are due to the setup and clean up procedures.
 */
@Ignore("using hibernate, deprecated in this module")
public class EntryTest {

	private Entry entry;
	private SessionFactory sessionFactory;
	private Logger logger = Logger.getLogger("JUNIT");
	
	@Before
	public void before(){
        logger.info("Before setting up");
        entry = new Entry();
        entry.setDbName(MmDatabase.UniProt.name());
        entry.setEntryAccessions(Collections.singletonList("V01234"));
        entry.setEntryId("XYZ_VOGON");
        entry.setEntryName("vogodiesterase");

		sessionFactory = HibernateUtil.getSessionFactory();
        logger.info("After setting up");
	}
	
	@After
	public void after() throws Exception{
        logger.info("Before cleaning up");
		Session session = sessionFactory.getCurrentSession();
		Transaction tr = (Transaction) session.beginTransaction();
		Query q = session.createQuery("delete from Entry where entryId = 'XYZ_VOGON'");
		int n = q.executeUpdate();
		tr.commit();
		System.out.println(n + " entries cleaned up");
		sessionFactory.close();
        logger.info("After cleaning up");
	}
	
	@Test
	public void testSaveGetDelete() {
        logger.info("Before getting session");
		Session session = sessionFactory.getCurrentSession();
        Entry myEntry = null;
        Integer savedId = null;
		try {
            logger.info("Before beginning transaction");
            session.beginTransaction();
            logger.info("Before saving entry");
	        savedId = (Integer) session.save(entry);
	        
            logger.info("Before getting entry");
	        myEntry = (Entry) session.get(Entry.class, savedId);
	        assertEquals("XYZ_VOGON", myEntry.getEntryId());
	        assertEquals(1, myEntry.getEntryAccessions().size());
	        assertEquals("V01234", myEntry.getEntryAccessions().get(0));
	        
            logger.info("Before deleting entry");
	        session.delete(myEntry);
            logger.info("Before getting null entry");
	        myEntry = (Entry) session.get(Entry.class, savedId);
	        assertNull(myEntry);
            logger.info("Before committing");
	        session.getTransaction().commit();
            logger.info("After committing");
        } catch (Exception e){
        	session.getTransaction().rollback();
        	e.printStackTrace();
        	fail();
        }
	}
	
	@Test
	public void testSameSameSession(){
		Session session1 = null;
		try {
			session1 = sessionFactory.getCurrentSession();
            session1.beginTransaction();
	        
	        Entry same = new Entry();
	        same.setDbName(MmDatabase.UniProt.name());
	        same.setEntryId("XYZ_VOGON");
	        assertEquals(entry, same);
	        
	        session1.merge(same);
	        session1.getTransaction().commit();
	        assertEquals(entry.getEntryId(), same.getEntryId());
        } catch (Exception e){
        	if (session1 != null && session1.isOpen()){
            	session1.getTransaction().rollback();
        	}
        	e.printStackTrace();
        	fail();
        }
	}
	
	@Test
	public void testSameDiffSession(){
		Session session1 = null, session2 = null;
		try {
			session1 = sessionFactory.getCurrentSession();
            session1.beginTransaction();
	        session1.saveOrUpdate(entry);
	        session1.getTransaction().commit();
	        
	        Entry same = new Entry();
	        same.setDbName(MmDatabase.UniProt.name());
	        same.setEntryId("XYZ_VOGON");
	        assertEquals(entry, same);
	        
	        session2 = sessionFactory.getCurrentSession();
	        session2.beginTransaction();
	        session2.saveOrUpdate(same);
	        session2.getTransaction().commit();
	        assertEquals(entry.getId(), same.getId());
        } catch (Exception e){
        	if (session1 != null && session1.isOpen()){
            	session1.getTransaction().rollback();
        	}
        	if (session2 != null && session2.isOpen()){
            	session2.getTransaction().rollback();
        	}
        	e.printStackTrace();
        	fail();
        } finally {
        	sessionFactory.close();
        }
	}
}
