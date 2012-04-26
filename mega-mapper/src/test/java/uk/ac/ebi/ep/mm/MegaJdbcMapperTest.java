package uk.ac.ebi.ep.mm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import java.util.Map;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import uk.ac.ebi.biobabel.util.db.OracleDatabaseInstance;

@Ignore
public class MegaJdbcMapperTest {

    private MegaJdbcMapper mm;
    private List<Entry> entries = new ArrayList<Entry>();
    private Logger logger = Logger.getLogger("JUNIT");
    private Connection con;

    @Before
    public void before() throws IOException, SQLException {
        logger.info("Before setting up");
        try {
            con = OracleDatabaseInstance.getInstance("ep-mm-db-enzdev").getConnection();
            mm = new MegaJdbcMapper(con);
            mm.openMap();

            Entry entry1 = new Entry();
            entry1.setDbName(MmDatabase.UniProt.name());
            entry1.setEntryAccessions(Collections.singletonList("V12345"));
            entry1.setEntryId("ABCD_VOGON");
            entry1.setEntryName("vogonase I");
            mm.writeEntry(entry1);
            entries.add(entry1);

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
            entries.add(entry2);

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
            entries.add(entry3);

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
            entries.add(entry4);

            con.commit();
        } catch (SQLException e) {
            if (con != null) {
                con.rollback();
            }
            throw e;
        }
        logger.info("After setting up");
    }

    @After
    public void after() throws IOException, SQLException {
        logger.info("Before cleaning up");
        Collections.reverse(entries); // The first one is xrefd by the others.
        for (Entry entry : entries) {
            mm.deleteEntry(entry);
        }
        con.commit();
        mm.closeMap();
        logger.info("After cleaning up");
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
        for (int i = 0; i < 100; i++) {
            logger.info("Before good accession again");
            e1 = mm.getEntryForAccession(MmDatabase.UniProt, "V12345");
        }
    }

    @Test
    public void testGetXrefsAllByEntry() {
        Entry entry = new Entry();
        entry.setDbName(MmDatabase.UniProt.name());
        entry.setEntryId("ABCD_VOGON");
        Collection<XRef> xrefs = mm.getXrefs(entry);
        assertNotNull(xrefs);
        assertTrue(!xrefs.isEmpty());
        assertEquals(3, xrefs.size());
        int chebi = 0, chembl = 0;
        for (XRef xRef : xrefs) {
            if (xRef.getFromEntry().getDbName().equals(MmDatabase.ChEMBL.name())) {
                chembl++;
            } else if (xRef.getFromEntry().getDbName().equals(MmDatabase.ChEBI.name())) {
                chebi++;
            }
        }
        assertEquals(2, chebi);
        assertEquals(1, chembl);
    }

    @Test
    public void testGetXrefsByEntry() {
        Entry entry = new Entry();
        entry.setDbName(MmDatabase.UniProt.name());
        entry.setEntryId("ABCD_VOGON");

        Collection<XRef> xrefs = mm.getXrefs(entry, MmDatabase.ChEBI);
        assertNotNull(xrefs);
        assertEquals(2, xrefs.size());

        xrefs = mm.getXrefs(entry, MmDatabase.ChEMBL);
        assertEquals(1, xrefs.size());

        xrefs = mm.getXrefs(entry, MmDatabase.ChEBI, MmDatabase.ChEMBL);
        assertEquals(3, xrefs.size());
        int chebi = 0, chembl = 0;
        for (XRef xRef : xrefs) {
            if (xRef.getFromEntry().getDbName().equals(MmDatabase.ChEMBL.name())) {
                chembl++;
            } else if (xRef.getFromEntry().getDbName().equals(MmDatabase.ChEBI.name())) {
                chebi++;
            }
        }
        assertEquals(2, chebi);
        assertEquals(1, chembl);

        xrefs = mm.getXrefs(entry, MmDatabase.PDBeChem);
        assertNull(xrefs);
    }

    @Test
    public void testGetXrefsAllByAccession() {
        logger.info("Before getting rhea xrefs");
        Collection<XRef> xrefs = mm.getXrefs(MmDatabase.Rhea, "V12345");
        assertNull(xrefs);

        logger.info("Before getting vogon xref");
        xrefs = mm.getXrefs(MmDatabase.UniProt, "V12345");
        assertNotNull(xrefs);
        assertEquals(3, xrefs.size());
        logger.info("Before looping vogon xrefs");
        int chebi = 0, chembl = 0;
        for (XRef xRef : xrefs) {
            if (xRef.getFromEntry().getDbName().equals(MmDatabase.ChEMBL.name())) {
                chembl++;
            } else if (xRef.getFromEntry().getDbName().equals(MmDatabase.ChEBI.name())) {
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
    public void testGetXrefsByAccession() {
        logger.info("Before ChEBI");
        Collection<XRef> xrefs = mm.getXrefs(MmDatabase.UniProt, "V12345", MmDatabase.ChEBI);
        assertEquals(2, xrefs.size());

        logger.info("Before ChEMBL");
        xrefs = mm.getXrefs(MmDatabase.UniProt, "V12345", MmDatabase.ChEMBL);
        assertEquals(1, xrefs.size());

        logger.info("Before PDBeChem");
        xrefs = mm.getXrefs(MmDatabase.UniProt, "V12345", MmDatabase.PDBeChem);
        assertNull(xrefs);

        logger.info("Before ChEBI+ChEMBL");
        xrefs = mm.getXrefs(MmDatabase.UniProt, "V12345", MmDatabase.ChEBI, MmDatabase.ChEMBL);
        assertEquals(3, xrefs.size());

        logger.info("After xrefsByAccession");
    }

//    /**
//     * Test of getAllUniProtAccessions method, of class MegaJdbcMapper.
//     */
//    @Test
//    public void testGetAllUniProtAccessions() {
//        System.out.println("getAllUniProtAccessions");
//        MmDatabase database = MmDatabase.UniProt;
//
//        List result = mm.getAllUniProtAccessions(database);
//        //assertNotNull(result);
//
//    }
    
//    @Test
//    public void getCompounds() {
//        System.out.println("get Compounds");
//        Map<String, String> compoundMap = null;
//        MmDatabase db = MmDatabase.UniProt;
//        String accession = "PDE5A_HUMAN";
//         //String [] acc = accessions.split("_");
//        //String accession = acc[0].concat("_%");
//        //String accession = "PDE6B_%";
//  
//        MmDatabase[] xDbs = new MmDatabase[3];
//        xDbs[1] = MmDatabase.ChEBI;
//        xDbs[0] = MmDatabase.ChEMBL;
//
//        compoundMap = mm.getCompounds(db, accession, xDbs);
//
//        assertNotNull(compoundMap);
//       // for (Map.Entry<String, String> m : compoundMap.entrySet()) {
//            //System.out.println("Result : " + m.getKey() + ": " + m.getValue());
//            //logger.info("Result : " + m.getKey() + ": " + m.getValue());
//       // }
//        
//    }
//    
//        @Test
//    public void getDisease() {
//        System.out.println("get Disease");
//        Map<String, String> diseaseMap = null;
//        MmDatabase db = MmDatabase.UniProt;
//        String accession = "CFTR_HUMAN";
//
//  
//        MmDatabase[] xDbs = new MmDatabase[3];
//  
//           
//        xDbs[0] = MmDatabase.OMIM;
//         xDbs[1] = MmDatabase.EFO;
//        xDbs[2] = MmDatabase.MeSH;
//  
//        
//        diseaseMap = mm.getDisease(db, accession, xDbs);
//        assertNotNull(diseaseMap);
//        //for (Map.Entry<String, String> m : diseaseMap.entrySet()) {
//           // System.out.println("Result : " + m.getKey() + ": " + m.getValue());
//            //logger.info("Result : " + m.getKey() + ": " + m.getValue());
//        //}
//        
//    }
}
