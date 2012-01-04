package uk.ac.ebi.ep.mm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MegaDbMapperTest {

	private MegaDbMapper mm;
	List<Integer> entityIds = new ArrayList<Integer>();

	@Before
	public void before() throws IOException{
		mm = new MegaDbMapper(null, 100);
		mm.openMap();
		Entry entity1 = new Entry();
		entity1.setDbName(MmDatabase.UniProt.name());
		entity1.setEntryId("ABCD_VOGON");
		List<String> accessions = new ArrayList<String>();
		accessions.add("V12345");
		accessions.add("V98765");
		entity1.setAccessions(accessions);
		mm.writeEntry(entity1);
		entityIds.add((Integer) entity1.getId());
		mm.closeMap();
		mm.openMap();
	}
	
	@After
	public void after() throws IOException{
		for (Integer id : entityIds) {
			Entry entry = (Entry) mm.session.load(Entry.class, id);
			mm.session.delete(entry);
		}
		mm.closeMap();
	}

	@Test
	public void testGetEntryForAccession() {
		Entry e1 = mm.getEntryForAccession(MmDatabase.UniProt, "V01234");
		assertNull(e1);
		e1 = mm.getEntryForAccession(MmDatabase.UniProt, "V98765");
		assertNotNull(e1);
		assertEquals("ABCD_VOGON", e1.getEntryId());
	}

}
