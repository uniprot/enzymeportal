package uk.ac.ebi.ep.mm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

//@Ignore
public class MegaDbMapperTest {

	private MegaDbMapper mm;
	private Collection<Integer> entries = new HashSet<Integer>();
	private Logger logger = Logger.getLogger("JUNIT");
	
	@Before
	public void before() throws IOException{
		logger.info("Before setting up");
		mm = new MegaDbMapper(null, 100);
		mm.openMap();
		
		Entry entry1 = new Entry();
		entry1.setDbName(MmDatabase.UniProt.name());
		entry1.setEntryAccessions(Collections.singletonList("V12345"));
		entry1.setEntryId("ABCD_VOGON");
		mm.writeEntry(entry1);
		entries.add(entry1.getId());
		
		Entry entry2 = new Entry();
		entry2.setDbName(MmDatabase.ChEBI.name());
		entry2.setEntryId("CHEBI:XXXXX");
		entry2.setEntryAccessions(Collections.singletonList("CHEBI:XXXXX"));
		entry2.setEntryName("vogonate");
		XRef xref2 = new XRef();
		xref2.setFromEntry(entry2);
		xref2.setRelationship(Relationship.between(MmDatabase.ChEBI, MmDatabase.UniProt).name());
		xref2.setToEntry(entry1);
		mm.writeXref(xref2);
		entries.add(entry2.getId());
		
		Entry entry3 = new Entry();
		entry3.setDbName(MmDatabase.ChEBI.name());
		entry3.setEntryId("CHEBI:YYYYY");
		entry3.setEntryAccessions(Collections.singletonList("CHEBI:YYYYY"));
		entry3.setEntryName("vogonic acid");
		XRef xref3 = new XRef();
		xref3.setFromEntry(entry3);
		xref3.setRelationship(Relationship.between(MmDatabase.ChEBI, MmDatabase.UniProt).name());
		xref3.setToEntry(entry1);
		mm.writeXref(xref3);
		entries.add(entry3.getId());
		
		Entry entry4 = new Entry();
		entry4.setDbName(MmDatabase.ChEMBL.name());
		entry4.setEntryId("CHEMBLZZZZZZ");
		entry4.setEntryAccessions(Collections.singletonList("CHEMBLZZZZZZ"));
		entry4.setEntryName("vogozepam");
		XRef xref4 = new XRef();
		xref4.setFromEntry(entry4);
		xref4.setRelationship(Relationship.between(MmDatabase.ChEMBL, MmDatabase.UniProt).name());
		xref4.setToEntry(entry1);
		mm.writeXref(xref4);
		entries.add(entry4.getId());
		
		mm.closeMap();
		mm.openMap();
		logger.info("After setting up");
	}
	
	@After
	public void after() throws IOException{
		logger.info("Before cleaning up");
		mm.closeMap();
		mm.openMap();
		String relQuery = "DELETE XRef WHERE fromEntry.id = :fromEnt OR toEntry.id = :toEnt";
		String entQuery = "DELETE Entry WHERE id = :theId";
		for (Integer id : entries) {
			int n = mm.session.createQuery(relQuery)
						.setInteger("fromEnt", id)
						.setInteger("toEnt", id)
						.executeUpdate();
			System.out.println(n + " xrefs deleted");
			int m = mm.session.createQuery(entQuery)
						.setInteger("theId", id)
						.executeUpdate();
			System.out.println(m + " entries deleted");
		}
		mm.closeMap();
		logger.info("After cleaning up");
	}

	@Test
	public void testGetEntryForAccession() {
		Entry e1 = mm.getEntryForAccession(MmDatabase.UniProt, "V01234");
		assertNull(e1);
		e1 = mm.getEntryForAccession(MmDatabase.UniProt, "V12345");
		assertNotNull(e1);
		assertEquals("ABCD_VOGON", e1.getEntryId());
	}
	
	@Test
	public void testGetXrefsAllByEntry(){
		Entry entry = new Entry();
		entry.setDbName(MmDatabase.UniProt.name());
		entry.setEntryId("ABCD_VOGON");
		Collection<XRef> xrefs = mm.getXrefs(entry);
		assertNotNull(xrefs);
		assertTrue(!xrefs.isEmpty());
		assertEquals(3, xrefs.size());
		int chebi = 0, chembl = 0;
		for (XRef xRef : xrefs) {
			if (xRef.getFromEntry().getDbName().equals(MmDatabase.ChEMBL.name())){
				chembl++;
			} else if (xRef.getFromEntry().getDbName().equals(MmDatabase.ChEBI.name())){
				chebi++;
			}
		}
		assertEquals(2, chebi);
		assertEquals(1, chembl);
	}
	
	@Test
	public void testGetXrefsByEntry(){
		Entry entry = new Entry();
		entry.setDbName(MmDatabase.UniProt.name());
		entry.setEntryId("ABCD_VOGON");
		
		Collection<XRef> xrefs = mm.getXrefs(entry, MmDatabase.ChEBI);
		assertEquals(2, xrefs.size());
		
		xrefs = mm.getXrefs(entry, MmDatabase.ChEMBL);
		assertEquals(1, xrefs.size());
		
		xrefs = mm.getXrefs(entry, MmDatabase.ChEBI, MmDatabase.ChEMBL);
		assertEquals(3, xrefs.size());
		int chebi = 0, chembl = 0;
		for (XRef xRef : xrefs) {
			if (xRef.getFromEntry().getDbName().equals(MmDatabase.ChEMBL.name())){
				chembl++;
			} else if (xRef.getFromEntry().getDbName().equals(MmDatabase.ChEBI.name())){
				chebi++;
			}
		}
		assertEquals(2, chebi);
		assertEquals(1, chembl);
		
		xrefs = mm.getXrefs(entry, MmDatabase.PDBeChem);
		assertTrue(xrefs.isEmpty());
	}
	
	@Test
	public void testGetXrefsAllByAccession(){
		Collection<XRef> xrefs = mm.getXrefs(MmDatabase.Rhea, "V12345");
		assertTrue(xrefs.isEmpty());
		
		xrefs = mm.getXrefs(MmDatabase.UniProt, "V12345");
		assertEquals(3, xrefs.size());
		int chebi = 0, chembl = 0;
		for (XRef xRef : xrefs) {
			if (xRef.getFromEntry().getDbName().equals(MmDatabase.ChEMBL.name())){
				chembl++;
			} else if (xRef.getFromEntry().getDbName().equals(MmDatabase.ChEBI.name())){
				chebi++;
			}
		}
		assertEquals(2, chebi);
		assertEquals(1, chembl);
		
		xrefs = mm.getXrefs(MmDatabase.ChEBI, "CHEBI:XXXXX");
		assertEquals(1, xrefs.size());
		assertEquals("ABCD_VOGON", xrefs.iterator().next().getToEntry().getEntryId());
		
		xrefs = mm.getXrefs(MmDatabase.ChEMBL, "CHEMBLZZZZZZ");
		assertEquals(1, xrefs.size());
		assertEquals("ABCD_VOGON", xrefs.iterator().next().getToEntry().getEntryId());
	}
	
	@Test
	public void testGetXrefsByAccession(){
		logger.info("Before ChEBI");
		Collection<XRef> xrefs = mm.getXrefs(MmDatabase.UniProt, "V12345", MmDatabase.ChEBI);
		assertEquals(2, xrefs.size());
		
		logger.info("Before ChEMBL");
		xrefs = mm.getXrefs(MmDatabase.UniProt, "V12345", MmDatabase.ChEMBL);
		assertEquals(1, xrefs.size());
		
		logger.info("Before PDBeChem");
		xrefs = mm.getXrefs(MmDatabase.UniProt, "V12345", MmDatabase.PDBeChem);
		assertEquals(0, xrefs.size());
		
		logger.info("Before ChEBI+ChEMBL"); // this one is very slow
		xrefs = mm.getXrefs(MmDatabase.UniProt, "V12345", MmDatabase.ChEBI, MmDatabase.ChEMBL);
		assertEquals(3, xrefs.size());
		// perhaps the reason is that entry has not been indexed,
		// being just inserted for the test.
		// Exactly the same query (as hibernate output) takes 10ms or less in SQLDeveloper.

		logger.info("After xrefsByAccession");
	}

}
