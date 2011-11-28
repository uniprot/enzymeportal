package uk.ac.ebi.ep.mm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;

public class EntryTest {

	@Test
	public void test() {
		final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
        Long id = -1L;
        Entry myEntity = null;
        try {
            session.beginTransaction();
	        Entry entity = new Entry();
	        entity.setId(id);
	        entity.setEntryId("PDE5A_HUMAN");
	        entity.setDbName("SWISS-PROT");
	        entity.setEntryName("cGMP-specific 3',5'-cyclic phosphodiesterase");
	        List<String> accessions = new ArrayList<String>();
	        accessions.add("O76074");
	        accessions.add("A0AV69");
	        accessions.add("A8K2C4");
	        accessions.add("O75026");
	        accessions.add("O75887");
	        entity.setAccessions(accessions);
	        id = (Long) session.save(entity);
	        
	        myEntity = (Entry) session.get(Entry.class, id);
	        assertEquals("PDE5A_HUMAN", myEntity.getEntryId());
	        assertEquals(5, myEntity.getAccessions().size());
	        assertEquals("O76074", myEntity.getAccessions().get(0));
	        
	        session.delete(myEntity);
	        myEntity = (Entry) session.get(Entry.class, id);
	        assertNull(myEntity);
        } catch (Exception e){
        	session.getTransaction().rollback();
        	e.printStackTrace();
        	fail();
        } finally {
        	if (id != null){
    	        myEntity = (Entry) session.get(Entry.class, id);
    	        if (myEntity != null){
    		        session.delete(myEntity);
    		        session.getTransaction().commit();
    	        }
        	}
        	sessionFactory.close();
        }
	}
}
