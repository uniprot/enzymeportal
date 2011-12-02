package uk.ac.ebi.ep.mm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EntryTest {

	private Entry entry;
	private SessionFactory sessionFactory;
	
	@Before
	public void before(){
        entry = new Entry();
        entry.setEntryId("PDE5A_HUMAN");
        entry.setDbName(MmDatabase.UniProt.name());
        entry.setEntryName("cGMP-specific 3',5'-cyclic phosphodiesterase");
        List<String> accessions = new ArrayList<String>();
        accessions.add("O76074");
        accessions.add("A0AV69");
        accessions.add("A8K2C4");
        accessions.add("O75026");
        accessions.add("O75887");
        entry.setAccessions(accessions);

		sessionFactory = HibernateUtil.getSessionFactory();
	}
	
	@After
	public void after(){
		sessionFactory.close();
	}
	
	@Test
	public void testSaveGetDelete() {
		Session session = sessionFactory.getCurrentSession();
        Entry myEntry = null;
        Integer savedId = null;
		try {
            session.beginTransaction();
	        savedId = (Integer) session.save(entry);
	        
	        myEntry = (Entry) session.get(Entry.class, savedId);
	        assertEquals("PDE5A_HUMAN", myEntry.getEntryId());
	        assertEquals(5, myEntry.getAccessions().size());
	        assertEquals("O76074", myEntry.getAccessions().get(0));
	        
	        session.delete(myEntry);
	        myEntry = (Entry) session.get(Entry.class, savedId);
	        assertNull(myEntry);
        } catch (Exception e){
        	session.getTransaction().rollback();
        	e.printStackTrace();
        	fail();
        } finally {
        	if (savedId != null){
    	        myEntry = (Entry) session.get(Entry.class, savedId);
    	        if (myEntry != null){
    		        session.delete(myEntry);
    		        session.getTransaction().commit();
    	        }
        	}
        	sessionFactory.close();
        }
	}
	
	@Test
	public void testSameSameSession(){
		Session session1 = null;
		try {
			session1 = sessionFactory.getCurrentSession();
            session1.beginTransaction();
	        session1.merge(entry);
	        
	        Entry same = new Entry();
	        same.setEntryId("PDE5A_HUMAN");
	        same.setDbName(MmDatabase.UniProt.name());
	        assertEquals(entry, same);
	        
	        session1.merge(same);
	        session1.getTransaction().commit();
	        assertEquals(entry.getId(), same.getId());
        } catch (Exception e){
        	if (session1 != null && session1.isOpen()){
            	session1.getTransaction().rollback();
        	}
        	e.printStackTrace();
        	fail();
        } finally {
        	sessionFactory.close();
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
	        same.setEntryId("PDE5A_HUMAN");
	        same.setDbName(MmDatabase.UniProt.name());
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
