//package uk.ac.ebi.ep.data.service;
//
//import java.sql.SQLException;
//import java.util.List;
//import java.util.stream.Stream;
//import org.junit.After;
//import org.junit.Test;
//import uk.ac.ebi.ep.data.domain.IntenzEnzymes;
//import uk.ac.ebi.ep.data.domain.ProteinGroups;
//import uk.ac.ebi.ep.data.domain.UniprotEntry;
//
///**
// *
// * @author Joseph <joseph@ebi.ac.uk>
// */
//public class EnzymePortalXmlServiceIT extends AbstractDataTest {
//
//    @After
//    @Override
//    public void tearDown() throws SQLException {
//        dataSource.getConnection().close();
//
//    }
//
//    /**
//     * Test of findAllIntenzEnzymes method, of class EnzymePortalXmlService.
//     */
//    @Test
//    public void testFindAllIntenzEnzymes() {
//        System.out.println("findAllIntenzEnzymes");
//
//        int expResult = 3;
//        List<IntenzEnzymes> result = enzymePortalXmlService.findAllIntenzEnzymes();
//
//        assertEquals(expResult, result.size());
//
//    }
//
//    /**
//     * Test of findSwissprotEnzymesByEcNumber method, of class
//     * EnzymePortalXmlService.
//     */
//    @Test
//    public void testFindSwissprotEnzymesByEcNumber() {
//        System.out.println("findSwissprotEnzymesByEcNumber");
//        String ec = "3.4.24.85";
//
//        int expResult = 2;
//        Iterable<UniprotEntry> result = enzymePortalXmlService.findSwissprotEnzymesByEcNumber(ec);
//        assertNotNull(result);
//        assertEquals(expResult, result.spliterator().getExactSizeIfKnown());
//
//    }
//
//    /**
//     * Test of findEnzymesByEcNumber method, of class EnzymePortalXmlService.
//     */
//    @Test
//    public void testFindEnzymesByEcNumber() {
//        System.out.println("findEnzymesByEcNumber");
//        String ec = "3.4.24.85";
//
//        int expResult = 5;
//        Iterable<UniprotEntry> result = enzymePortalXmlService.findEnzymesByEcNumber(ec);
//
//        assertNotNull(result);
//        assertEquals(expResult, result.spliterator().getExactSizeIfKnown());
//
//    }
//
//    /**
//     * Test of findEnzymesByEcNumberNativeQuery method, of class
//     * EnzymePortalXmlService.
//     */
//    @Test
//    public void testFindEnzymesByEcNumberNativeQuery() {
//        System.out.println("findEnzymesByEcNumberNativeQuery");
//        String ec = "3.4.24.85";
//
//        int expResult = 5;
//        List<UniprotEntry> result = enzymePortalXmlService.findEnzymesByEcNumberNativeQuery(ec);
//
//        assertNotNull(result);
//        assertEquals(expResult, result.size());
//
//    }
//
//    /**
//     * Test of findUniprotEntries method, of class EnzymePortalXmlService.
//     */
//    @Test
//    public void testFindUniprotEntries() {
//        System.out.println("findUniprotEntries");
//
//        int expResult = 17;
//        List<UniprotEntry> result = enzymePortalXmlService.findUniprotEntries();
//        assertNotNull(result);
//       // assertEquals(expResult, result.size());
//
//    }
//
//    /**
//     * Test of countUniprotEntries method, of class EnzymePortalXmlService.
//     */
//    @Test
//    public void testCountUniprotEntries() {
//        System.out.println("countUniprotEntries");
//
//        Long expResult = 17L;
//        Long result = enzymePortalXmlService.countUniprotEntries();
//
//        assertEquals(expResult, result);
//
//    }
//
//    /**
//     * Test of findUniprotEntriesOrderedByEntryType method, of class
//     * EnzymePortalXmlService.
//     */
//    @Test
//    public void testFindUniprotEntriesOrderedByEntryType() {
//        System.out.println("findUniprotEntriesOrderedByEntryType");
//
//        int expResult = 17;
//        List<UniprotEntry> result = enzymePortalXmlService.findUniprotEntriesOrderedByEntryType();
//        assertNotNull(result);
//        assertEquals(expResult, result.size());
//
//    }
//
//    /**
//     * Test of findSwissprotEnzymesByEc method, of class EnzymePortalXmlService.
//     */
//    @Test
//    public void testFindSwissprotEnzymesByEc() {
//        System.out.println("findSwissprotEnzymesByEc");
//        String ec = "3.4.24.85";
//
//        int expResult = 2;
//        List<UniprotEntry> result = enzymePortalXmlService.findSwissprotEnzymesByEc(ec);
//
//        assertNotNull(result);
//        assertEquals(expResult, result.size());
//
//    }
//
//    /**
//     * Test of findStreamedSwissprotEnzymesByEc method, of class
//     * EnzymePortalXmlService.
//     */
//    /**
//     * Test of findNonTransferredEnzymes method, of class
//     * EnzymePortalXmlService.
//     */
//    @Test
//    public void testFindNonTransferredEnzymes() {
//        System.out.println("findNonTransferredEnzymes");
//
//        int expResult = 3;
//        List<IntenzEnzymes> result = enzymePortalXmlService.findNonTransferredEnzymes();
//
//        assertEquals(expResult, result.size());
//
//    }
//
//    /**
//     * Test of streamIntenzEnzymes method, of class EnzymePortalXmlService.
//     */
//    @Test
//    public void testStreamIntenzEnzymesInBatch() {
//        System.out.println("streamIntenzEnzymes");
//        int batchSize = 10;
//
//        int expResult = 3;
//        String query = "select e from IntenzEnzymes e where transferFlag='N'";
//        Stream<IntenzEnzymes> result = enzymePortalXmlService.streamIntenzEnzymesInBatch(sessionFactory, query, batchSize);
//
//        assertEquals(expResult, result.count());
//
//    }
//
//    /**
//     * Test of streamUniprotEntriesInBatch method, of class
//     * EnzymePortalXmlService.
//     */
//    @Test
//    public void testStreamUniprotEntriesInBatch() {
//        System.out.println("streamUniprotEntriesInBatch");
//        int batchSize = 100;
//
//        int expResult = 17;
//        String query = "SELECT u FROM UniprotEntry u";
//        Stream<UniprotEntry> result = enzymePortalXmlService.streamUniprotEntriesInBatch(sessionFactory, query, batchSize);
//
//        assertNotNull(result);
//        assertEquals(expResult, result.count());
//
//    }
//
//    /**
//     * Test of streamUniProtProteinsInBatch method, of class
//     * EnzymePortalXmlService.
//     */
//    @Test
//    public void testStreamProteinGroupsInBatch() {
//        System.out.println("streamUniProtProteinsInBatch");
//        int batchSize = 10;
//
//        int expResult = 2;
//        String query = "SELECT p FROM ProteinGroups p";
//        Stream<ProteinGroups> result = enzymePortalXmlService.streamProteinGroupsInBatch(sessionFactory, query, batchSize);
//        assertNotNull(result);
//        assertEquals(expResult, result.count());
//
//    }
//
//    /**
//     * Test of countProteinGroups method, of class EnzymePortalXmlService.
//     */
//    @Test
//    public void testCountProteinGroups() {
//        System.out.println("countProteinGroups");
//
//        Long expResult = 2L;
//        Long result = enzymePortalXmlService.countProteinGroups();
//
//        assertEquals(expResult, result);
//
//    }
//
//}
