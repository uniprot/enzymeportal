package uk.ac.ebi.ep.mm;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import uk.ac.ebi.biobabel.util.db.OracleDatabaseInstance;
import uk.ac.ebi.ep.mm.MegaMapper.Constraint;
import uk.ac.ebi.ep.search.model.Compound;
import uk.ac.ebi.ep.search.model.Disease;

public class MegaJdbcMapperTest {

    private MegaJdbcMapper mm;
    private List<Entry> entries = new ArrayList<Entry>();
    private Logger logger = Logger.getLogger("JUNIT");
    private Connection con;

    @Before
    public void before() throws IOException, SQLException {
        logger.info("Before setting up");
        try {
           // con = OracleDatabaseInstance.getInstance("ep-mm-db-enzdev")
           con = OracleDatabaseInstance.getInstance("ep-mm-db-ezprel")//testing the new instance. remove later
                    .getConnection();
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
            xref2.setRelationship(Relationship.is_cofactor_of.name());
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
            xref3.setRelationship(
                    Relationship.is_substrate_or_product_of.name());
            xref3.setToEntry(entry1);
            mm.writeXref(xref3);
            entries.add(entry3);

            Entry entry4 = new Entry();
            entry4.setDbName(MmDatabase.ChEMBL.name());
            entry4.setEntryId("CHEMBLZZZZZZ");
            entry4.setEntryAccessions(
                    Collections.singletonList("CHEMBLZZZZZZ"));
            entry4.setEntryName("vogozepam");
            XRef xref4 = new XRef();
            xref4.setFromEntry(entry4);
            xref4.setRelationship(Relationship.between(
                    MmDatabase.ChEMBL, MmDatabase.UniProt).name());
            xref4.setToEntry(entry1);
            mm.writeXref(xref4);
            entries.add(entry4);

            con.commit();
        } catch (SQLException e) {
            con.rollback();
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

    @Test
    public void testGetXrefsByAccessionAndRelationship() {
        Collection<XRef> xrefs = mm.getXrefs(MmDatabase.UniProt, "P07327",
                Relationship.belongs_to);
        assertNotNull(xrefs);
        assertEquals(2, xrefs.size()); // EC 1.1.1.1, Homo sapiens
    }

    @Test
    public void testGetXrefsByIdFragment() {
        Collection<XRef> xrefs = mm.getXrefs(MmDatabase.UniProt, "ABCD_",
                Constraint.STARTS_WITH, Relationship.is_cofactor_of);
        assertNotNull(xrefs);
        assertEquals(1, xrefs.size());
        XRef xref = xrefs.iterator().next();
        assertEquals(MmDatabase.ChEBI.name(), xref.getFromEntry().getDbName());
        assertEquals("vogonate", xref.getFromEntry().getEntryName());
        assertEquals("CHEBI:XXXXX", xref.getFromEntry().getEntryId());
        assertEquals(MmDatabase.UniProt.name(), xref.getToEntry().getDbName());
        assertEquals("vogonase I", xref.getToEntry().getEntryName());
        assertEquals("ABCD_VOGON", xref.getToEntry().getEntryId());

        xrefs = mm.getXrefs(MmDatabase.UniProt, "ABCD_",
                Constraint.STARTS_WITH, Relationship.is_substrate_or_product_of);
        assertNotNull(xrefs);
        assertEquals(1, xrefs.size());
        xref = xrefs.iterator().next();
        assertEquals(MmDatabase.ChEBI.name(), xref.getFromEntry().getDbName());
        assertEquals("vogonic acid", xref.getFromEntry().getEntryName());
        assertEquals("CHEBI:YYYYY", xref.getFromEntry().getEntryId());
        assertEquals(MmDatabase.UniProt.name(), xref.getToEntry().getDbName());
        assertEquals("vogonase I", xref.getToEntry().getEntryName());
        assertEquals("ABCD_VOGON", xref.getToEntry().getEntryId());

        xrefs = mm.getXrefs(MmDatabase.UniProt, "ABCD_",
                Constraint.STARTS_WITH,
                new MmDatabase[]{MmDatabase.ChEBI, MmDatabase.ChEMBL});
        assertNotNull(xrefs);
        assertEquals(3, xrefs.size());
        for (Iterator<XRef> it = xrefs.iterator(); it.hasNext();) {
            xref = it.next();
            switch (Relationship.valueOf(xref.getRelationship())) {
                case is_cofactor_of:
                    assertEquals("CHEBI:XXXXX", xref.getFromEntry().getEntryId());
                    break;
                case is_substrate_or_product_of:
                    assertEquals("CHEBI:YYYYY", xref.getFromEntry().getEntryId());
                    break;
                default:
                    assertEquals("CHEMBLZZZZZZ", xref.getFromEntry().getEntryId());
                    break;
            }
        }

        xrefs = mm.getXrefs(MmDatabase.UniProt, "AB_",
                Constraint.STARTS_WITH, MmDatabase.ChEBI);
        assertNull(xrefs); // underscore not taken as oracle wildcard
    }

    /**
     * Test of getAllUniProtAccessions method, of class MegaJdbcMapper.
     */
    @Test
    @Ignore("This one takes ages")
    public void testGetAllUniProtAccessions() {
        System.out.println("getAllUniProtAccessions");
        MmDatabase database = MmDatabase.UniProt;

        List result = mm.getAllUniProtAccessions(database);
        assertNotNull(result);

    }

    @Test
    public void getCompounds() {
        //System.out.println("get Compounds");
        Map<String, String> compoundMap = null;
        MmDatabase db = MmDatabase.UniProt;
        String accession = "PDE5A_HUMAN";
        //String [] acc = accessions.split("_");
        //String accession = acc[0].concat("_%");
        //String accession = "PDE6B_%";

        MmDatabase[] xDbs = new MmDatabase[3];
        xDbs[1] = MmDatabase.ChEBI;
        xDbs[0] = MmDatabase.ChEBI;

        compoundMap = mm.getCompounds(db, accession, xDbs);

        assertNotNull(compoundMap);
//        for (Map.Entry<String, String> m : compoundMap.entrySet()) {
//
//            logger.info("Result for compounds: " + m.getKey() + ": " + m.getValue());
//        }

    }

    @Test
    public void getCompoundCollection() {
        Collection<Compound> compounds = null;

        String uniprotId = "O76074";
        uniprotId = "PDE5A_";// = "PDE5A_HUMAN";

        compounds = mm.getCompounds(uniprotId);
        if (compounds != null) {
            for (Compound c : compounds) {
                logger.info("Compounds found : " + c.getId() + " : " + c.getName());

            }
        } else {
            logger.info("NO COMPOUND FOUND");
        }

    }
//    @Test
//    public void fineEC() {
//        List<String> s = mm.findEcNumbers();
//        for(String x : s){
//            System.out.println("THE ec Numbers: "+ x);
//        }
//    }

    @Test
    public void getDisease() {
        //System.out.println("get Disease");
        Collection<Disease> diseaseList = null;
        MmDatabase db = MmDatabase.UniProt;
        String accession = "CFTR_HUMAN";


        MmDatabase[] xDbs = new MmDatabase[3];


        xDbs[0] = MmDatabase.OMIM;
        xDbs[1] = MmDatabase.EFO;
        xDbs[2] = MmDatabase.MeSH;


        diseaseList = mm.getDiseaseByUniprotId(db, accession, xDbs);
        assertNotNull(diseaseList);
//        for(Disease d : diseaseList){
//           logger.info("Result for disease: " +  d); 
//        }
   

    }
   


//    @Test
//    @Deprecated(this method is nolonger in use)
//    public void getChMBLEntries() {
//
//        String accession = "P55789";
//        List<Entry> entryList = mm.getChMBLEntries(MmDatabase.UniProt, accession, MmDatabase.ChEMBL);
//        //assertNotNull(entryList);//some accession cannot be found thereby causing the test to fail. meanwhile, this method is no longer being used
//        for (Entry entry : entryList) {
//            //System.out.println("entry"+ entry.getEntryName());
//            logger.info("Entries found for Accession (" + accession + ") : " + entry.getEntryName());
//        }
//    }
}
