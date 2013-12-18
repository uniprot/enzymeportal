package uk.ac.ebi.ep.mm;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

@Ignore("using hibernate, deprecated in this module")
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
		// Fucking hibernate could not delete the fucking xrefs,
		// let's try with some fucking plain SQL:
		String deleteFuckingXrefs = "delete from mm_xref where from_entry = ? or to_entry = ?";
		String deleteFuckingAccs = "delete from mm_accession where id = ?";
		String deleteFuckingEntries = "delete from mm_entry where id = ?";
		
		SQLQuery dfxQuery = mm.session.createSQLQuery(deleteFuckingXrefs);
		SQLQuery dfaQuery = mm.session.createSQLQuery(deleteFuckingAccs);
		SQLQuery dfeQuery = mm.session.createSQLQuery(deleteFuckingEntries);
		for (Integer id : entries) {
			dfxQuery.setInteger(0, id);
			dfxQuery.setInteger(1, id);
			int n = dfxQuery.executeUpdate();
			System.out.println(n + " fucking xrefs deleted");
			dfaQuery.setInteger(0, id);
			n = dfaQuery.executeUpdate();
			System.out.println(n + " fucking accessions deleted");
			dfeQuery.setInteger(0, id);
			n = dfeQuery.executeUpdate();
			System.out.println(n + " fucking entries deleted");
		}
		mm.closeMap();
		logger.info("After fucking cleaning up");
	}

	@Test
	public void testGetEntryForAccession() {
		logger.info("Before wrong accession");
		Entry e1 = mm.getEntryForAccession(MmDatabase.UniProt, "V01234");
		assertNull(e1);
		logger.info("Before good accession");
		e1 = mm.getEntryForAccession(MmDatabase.UniProt, "V12345");
		assertNotNull(e1);
		assertEquals("ABCD_VOGON", e1.getEntryId());
		for (int i = 0; i < 30; i++) {
			logger.info("Before good accession again");
			e1 = mm.getEntryForAccession(MmDatabase.UniProt, "V12345");
		}
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
		logger.info("Before getting rhea xrefs");
		Collection<XRef> xrefs = mm.getXrefs(MmDatabase.Rhea, "V12345");
		assertTrue(xrefs.isEmpty());
		
		logger.info("Before getting vogon xref");
		xrefs = mm.getXrefs(MmDatabase.UniProt, "V12345");
		assertEquals(3, xrefs.size());
		logger.info("Before looping vogon xrefs");
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
		
		logger.info("Before getting xxx");
		xrefs = mm.getXrefs(MmDatabase.ChEBI, "CHEBI:XXXXX");
		assertEquals(1, xrefs.size());
		assertEquals("ABCD_VOGON", xrefs.iterator().next().getToEntry().getEntryId());
		
		logger.info("Before getting zzz");
		xrefs = mm.getXrefs(MmDatabase.ChEMBL, "CHEMBLZZZZZZ");
		assertEquals(1, xrefs.size());
		assertEquals("ABCD_VOGON", xrefs.iterator().next().getToEntry().getEntryId());
		logger.info("Before returning");
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
		
		logger.info("Before ChEBI+ChEMBL");
		xrefs = mm.getXrefs(MmDatabase.UniProt, "V12345", MmDatabase.ChEBI, MmDatabase.ChEMBL);
		assertEquals(3, xrefs.size());

		logger.info("After xrefsByAccession");
	}

}
