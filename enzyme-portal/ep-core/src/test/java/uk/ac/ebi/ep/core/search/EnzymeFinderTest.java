/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.core.search;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.ac.ebi.ep.adapter.ebeye.EbeyeConfig;
import uk.ac.ebi.ep.adapter.intenz.IntenzConfig;
import uk.ac.ebi.ep.adapter.uniprot.UniprotConfig;
import uk.ac.ebi.ep.core.search.IEnzymeFinder.UniprotImplementation;
import uk.ac.ebi.ep.core.search.IEnzymeFinder.UniprotSource;
import uk.ac.ebi.ep.search.model.SearchFilters;
import uk.ac.ebi.ep.search.model.SearchParams;
import uk.ac.ebi.ep.search.model.SearchResults;

/**
 *
 * @author joseph
 */
public class EnzymeFinderTest {

    private EnzymeFinder instance = null;

    public EnzymeFinderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        Config config = new Config();
        config.setFinderUniprotSource(UniprotSource.EBEYE.name());
        config.setRetrieverUniprotSource(UniprotSource.UNIPROT.name());
        config.setResultsPerPage(10);
        config.setMaxPages(1);
        config.setUniprotImplementation(UniprotImplementation.JAPI.name());
        //config.setUniprotImplementation(UniprotImplementation.WS.name());
        config.loadCacheData();



        //uniprot
        UniprotConfig uniprotConfig = new UniprotConfig();
        uniprotConfig.setReviewed(false);
        uniprotConfig.setTimeout(30000);
        uniprotConfig.setUseProxy(true);
        uniprotConfig.setMaxTermsPerQuery(100);
        uniprotConfig.setWsUrl("http://www.uniprot.org/uniprot/?format=tab&amp;sort=score&amp;query={0}&amp;columns={1}");


        //ebeye
        EbeyeConfig ebeyeConfig = new EbeyeConfig();
        ebeyeConfig.setMaxAccessionsInQuery(100);
        ebeyeConfig.setResultsLimit(100);
        ebeyeConfig.setMaxResults(2000);
        ebeyeConfig.setMaxChebiResults(30);
        ebeyeConfig.setMaxUniprotResults(5000);
        ebeyeConfig.setMaxUniprotResultsFromChebi(100);
        ebeyeConfig.setMaxUniprotResultsFromOtherDomains(2000);
        ebeyeConfig.setMaxThreads(50);
        ebeyeConfig.setThreadTimeout(30000);


        //intenz
        IntenzConfig intenzConfig = new IntenzConfig();
        intenzConfig.setTimeout(30000);



        instance = new EnzymeFinder(config);
        instance.uniprotAdapter.setConfig(uniprotConfig);
        instance.ebeyeAdapter.setConfig(ebeyeConfig);
        instance.intenzAdapter.setConfig(intenzConfig);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getEnzymes method, of class EnzymeFinder.
     */
    @Test
    public void testGetEnzymes() throws Exception {
        System.out.println("getEnzymes");

        SearchParams searchParams = new SearchParams();

        searchParams.setText("Q13423");
        searchParams.setPrevioustext("sildenafil");

        SearchResults expResult = new SearchResults();
        expResult.setTotalfound(4);
        expResult.setSearchfilters(new SearchFilters());
        

       // SearchResults result = instance.getEnzymes(searchParams);

       // assertEquals(expResult.getTotalfound(), result.getTotalfound());

    }
    /**
     * Test of buildFilters method, of class EnzymeFinder.
     */
//    @Test
//    public void testBuildFilters() {
//        System.out.println("buildFilters");
//        SearchResults searchResults = null;
//        EnzymeFinder instance = null;
//        instance.buildFilters(searchResults);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    /**
     * Test of getSearchParams method, of class EnzymeFinder.
     */
//    @Test
//    public void testGetSearchParams() {
//        System.out.println("getSearchParams");
//        EnzymeFinder instance = null;
//        SearchParams expResult = null;
//        SearchParams result = instance.getSearchParams();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setSearchParams method, of class EnzymeFinder.
//     */
//    @Test
//    public void testSetSearchParams() {
//        System.out.println("setSearchParams");
//        SearchParams searchParams = null;
//        EnzymeFinder instance = null;
//        instance.setSearchParams(searchParams);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getCompoundFilter method, of class EnzymeFinder.
//     */
//    @Test
//    public void testGetCompoundFilter() {
//        System.out.println("getCompoundFilter");
//        EnzymeFinder instance = null;
//        List expResult = null;
//        List result = instance.getCompoundFilter();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setCompoundFilter method, of class EnzymeFinder.
//     */
//    @Test
//    public void testSetCompoundFilter() {
//        System.out.println("setCompoundFilter");
//        List<String> compoundFilter = null;
//        EnzymeFinder instance = null;
//        instance.setCompoundFilter(compoundFilter);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getChebiIds method, of class EnzymeFinder.
//     */
//    @Test
//    public void testGetChebiIds() {
//        System.out.println("getChebiIds");
//        EnzymeFinder instance = null;
//        List expResult = null;
//        List result = instance.getChebiIds();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setChebiIds method, of class EnzymeFinder.
//     */
//    @Test
//    public void testSetChebiIds() {
//        System.out.println("setChebiIds");
//        List<String> chebiIds = null;
//        EnzymeFinder instance = null;
//        instance.setChebiIds(chebiIds);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getChebiResults method, of class EnzymeFinder.
//     */
//    @Test
//    public void testGetChebiResults() {
//        System.out.println("getChebiResults");
//        EnzymeFinder instance = null;
//        Map expResult = null;
//        Map result = instance.getChebiResults();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setChebiResults method, of class EnzymeFinder.
//     */
//    @Test
//    public void testSetChebiResults() {
//        System.out.println("setChebiResults");
//        Map<String, List<String>> chebiResults = null;
//        EnzymeFinder instance = null;
//        instance.setChebiResults(chebiResults);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getEbeyeAdapter method, of class EnzymeFinder.
//     */
//    @Test
//    public void testGetEbeyeAdapter() {
//        System.out.println("getEbeyeAdapter");
//        EnzymeFinder instance = null;
//        IEbeyeAdapter expResult = null;
//        IEbeyeAdapter result = instance.getEbeyeAdapter();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setEbeyeAdapter method, of class EnzymeFinder.
//     */
//    @Test
//    public void testSetEbeyeAdapter() {
//        System.out.println("setEbeyeAdapter");
//        IEbeyeAdapter ebeyeAdapter = null;
//        EnzymeFinder instance = null;
//        instance.setEbeyeAdapter(ebeyeAdapter);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getEnzymeSearchResults method, of class EnzymeFinder.
//     */
//    @Test
//    public void testGetEnzymeSearchResults() {
//        System.out.println("getEnzymeSearchResults");
//        EnzymeFinder instance = null;
//        SearchResults expResult = null;
//        SearchResults result = instance.getEnzymeSearchResults();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setEnzymeSearchResults method, of class EnzymeFinder.
//     */
//    @Test
//    public void testSetEnzymeSearchResults() {
//        System.out.println("setEnzymeSearchResults");
//        SearchResults enzymeSearchResults = null;
//        EnzymeFinder instance = null;
//        instance.setEnzymeSearchResults(enzymeSearchResults);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getEnzymeSummaryList method, of class EnzymeFinder.
//     */
//    @Test
//    public void testGetEnzymeSummaryList() {
//        System.out.println("getEnzymeSummaryList");
//        EnzymeFinder instance = null;
//        List expResult = null;
//        List result = instance.getEnzymeSummaryList();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setEnzymeSummaryList method, of class EnzymeFinder.
//     */
//    @Test
//    public void testSetEnzymeSummaryList() {
//        System.out.println("setEnzymeSummaryList");
//        List<EnzymeSummary> enzymeSummaryList = null;
//        EnzymeFinder instance = null;
//        instance.setEnzymeSummaryList(enzymeSummaryList);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getIntenzAdapter method, of class EnzymeFinder.
//     */
//    @Test
//    public void testGetIntenzAdapter() {
//        System.out.println("getIntenzAdapter");
//        EnzymeFinder instance = null;
//        IintenzAdapter expResult = null;
//        IintenzAdapter result = instance.getIntenzAdapter();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setIntenzAdapter method, of class EnzymeFinder.
//     */
//    @Test
//    public void testSetIntenzAdapter() {
//        System.out.println("setIntenzAdapter");
//        IintenzAdapter intenzAdapter = null;
//        EnzymeFinder instance = null;
//        instance.setIntenzAdapter(intenzAdapter);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of isNewSearch method, of class EnzymeFinder.
//     */
//    @Test
//    public void testIsNewSearch() {
//        System.out.println("isNewSearch");
//        EnzymeFinder instance = null;
//        boolean expResult = false;
//        boolean result = instance.isNewSearch();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setNewSearch method, of class EnzymeFinder.
//     */
//    @Test
//    public void testSetNewSearch() {
//        System.out.println("setNewSearch");
//        boolean newSearch = false;
//        EnzymeFinder instance = null;
//        instance.setNewSearch(newSearch);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getSpeciesFilter method, of class EnzymeFinder.
//     */
//    @Test
//    public void testGetSpeciesFilter() {
//        System.out.println("getSpeciesFilter");
//        EnzymeFinder instance = null;
//        List expResult = null;
//        List result = instance.getSpeciesFilter();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setSpeciesFilter method, of class EnzymeFinder.
//     */
//    @Test
//    public void testSetSpeciesFilter() {
//        System.out.println("setSpeciesFilter");
//        List<String> speciesFilter = null;
//        EnzymeFinder instance = null;
//        instance.setSpeciesFilter(speciesFilter);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getUniprotAdapter method, of class EnzymeFinder.
//     */
//    @Test
//    public void testGetUniprotAdapter() {
//        System.out.println("getUniprotAdapter");
//        EnzymeFinder instance = null;
//        IUniprotAdapter expResult = null;
//        IUniprotAdapter result = instance.getUniprotAdapter();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setUniprotAdapter method, of class EnzymeFinder.
//     */
//    @Test
//    public void testSetUniprotAdapter() {
//        System.out.println("setUniprotAdapter");
//        IUniprotAdapter uniprotAdapter = null;
//        EnzymeFinder instance = null;
//        instance.setUniprotAdapter(uniprotAdapter);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getUniprotEnzymeIds method, of class EnzymeFinder.
//     */
//    @Test
//    public void testGetUniprotEnzymeIds() {
//        System.out.println("getUniprotEnzymeIds");
//        EnzymeFinder instance = null;
//        List expResult = null;
//        List result = instance.getUniprotEnzymeIds();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setUniprotEnzymeIds method, of class EnzymeFinder.
//     */
//    @Test
//    public void testSetUniprotEnzymeIds() {
//        System.out.println("setUniprotEnzymeIds");
//        List<String> uniprotEnzymeIds = null;
//        EnzymeFinder instance = null;
//        instance.setUniprotEnzymeIds(uniprotEnzymeIds);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getUniprotIdPrefixSet method, of class EnzymeFinder.
//     */
//    @Test
//    public void testGetUniprotIdPrefixSet() {
//        System.out.println("getUniprotIdPrefixSet");
//        EnzymeFinder instance = null;
//        Set expResult = null;
//        Set result = instance.getUniprotIdPrefixSet();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setUniprotIdPrefixSet method, of class EnzymeFinder.
//     */
//    @Test
//    public void testSetUniprotIdPrefixSet() {
//        System.out.println("setUniprotIdPrefixSet");
//        Set<String> uniprotIdPrefixSet = null;
//        EnzymeFinder instance = null;
//        instance.setUniprotIdPrefixSet(uniprotIdPrefixSet);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getEnzymes method, of class EnzymeFinder.
//     */
//    @Test
//    public void testGetEnzymes() throws Exception {
//        System.out.println("getEnzymes");
//        SearchParams searchParams = null;
//        EnzymeFinder instance = null;
//        SearchResults expResult = null;
//        SearchResults result = instance.getEnzymes(searchParams);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of buildFilters method, of class EnzymeFinder.
//     */
//    @Test
//    public void testBuildFilters() {
//        System.out.println("buildFilters");
//        SearchResults searchResults = null;
//        EnzymeFinder instance = null;
//        instance.buildFilters(searchResults);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of resetNrOfResultsToLimit method, of class EnzymeFinder.
//     */
//    @Test
//    public void testResetNrOfResultsToLimit_List() {
//        System.out.println("resetNrOfResultsToLimit");
//        List<ParamOfGetResults> params = null;
//        EnzymeFinder instance = null;
//        instance.resetNrOfResultsToLimit(params);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of resetNrOfResultsToLimit method, of class EnzymeFinder.
//     */
//    @Test
//    public void testResetNrOfResultsToLimit_ParamOfGetResults() {
//        System.out.println("resetNrOfResultsToLimit");
//        ParamOfGetResults param = null;
//        EnzymeFinder instance = null;
//        instance.resetNrOfResultsToLimit(param);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getNrOfRecordsRelatedToUniprot method, of class EnzymeFinder.
//     */
//    @Test
//    public void testGetNrOfRecordsRelatedToUniprot() throws Exception {
//        System.out.println("getNrOfRecordsRelatedToUniprot");
//        EnzymeFinder instance = null;
//        List expResult = null;
//        List result = instance.getNrOfRecordsRelatedToUniprot();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getUniprotNrOfRecords method, of class EnzymeFinder.
//     */
//    @Test
//    public void testGetUniprotNrOfRecords() throws Exception {
//        System.out.println("getUniprotNrOfRecords");
//        EnzymeFinder instance = null;
//        ParamOfGetResults expResult = null;
//        ParamOfGetResults result = instance.getUniprotNrOfRecords();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getChebiNrOfRecords method, of class EnzymeFinder.
//     */
//    @Test
//    public void testGetChebiNrOfRecords() throws Exception {
//        System.out.println("getChebiNrOfRecords");
//        EnzymeFinder instance = null;
//        ParamOfGetResults expResult = null;
//        ParamOfGetResults result = instance.getChebiNrOfRecords();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of prepareParamsForQueryIN method, of class EnzymeFinder.
//     */
//    @Test
//    public void testPrepareParamsForQueryIN() {
//        System.out.println("prepareParamsForQueryIN");
//        String domain = "";
//        List<String> queries = null;
//        List<String> resultFields = null;
//        EnzymeFinder instance = null;
//        List expResult = null;
//        List result = instance.prepareParamsForQueryIN(domain, queries, resultFields);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getEnzymesFromUniprotAPI method, of class EnzymeFinder.
//     */
//    @Test
//    public void testGetEnzymesFromUniprotAPI() throws Exception {
//        System.out.println("getEnzymesFromUniprotAPI");
//        List<String> resultSubList = null;
//        EnzymeFinder instance = null;
//        List expResult = null;
//        List result = instance.getEnzymesFromUniprotAPI(resultSubList);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of calTotalResultsFound method, of class EnzymeFinder.
//     */
//    @Test
//    public void testCalTotalResultsFound() {
//        System.out.println("calTotalResultsFound");
//        List<ParamOfGetResults> resultList = null;
//        int expResult = 0;
//        int result = EnzymeFinder.calTotalResultsFound(resultList);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of addIntenzSynonyms method, of class EnzymeFinder.
//     */
//    @Test
//    public void testAddIntenzSynonyms() throws Exception {
//        System.out.println("addIntenzSynonyms");
//        List<EnzymeSummary> enzymeSummaryList = null;
//        EnzymeFinder instance = null;
//        instance.addIntenzSynonyms(enzymeSummaryList);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setPdbeAccession method, of class EnzymeFinder.
//     */
//    @Test
//    public void testSetPdbeAccession() {
//        System.out.println("setPdbeAccession");
//        Map<String, String> pdbeAccs = null;
//        EnzymeFinder instance = null;
//        instance.setPdbeAccession(pdbeAccs);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getUniprotIds method, of class EnzymeFinder.
//     */
//    @Test
//    public void testGetUniprotIds() {
//        System.out.println("getUniprotIds");
//        List<EnzymeSummary> enzymeSummaryList = null;
//        EnzymeFinder instance = null;
//        List expResult = null;
//        List result = instance.getUniprotIds(enzymeSummaryList);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of createSpeciesFilter method, of class EnzymeFinder.
//     */
//    @Test
//    public void testCreateSpeciesFilter() {
//        System.out.println("createSpeciesFilter");
//        SearchResults enzymeSearchResults = null;
//        EnzymeFinder instance = null;
//        instance.createSpeciesFilter(enzymeSearchResults);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of prepareGetRelatedRecordsToUniprotQueries method, of class EnzymeFinder.
//     */
//    @Test
//    public void testPrepareGetRelatedRecordsToUniprotQueries() {
//        System.out.println("prepareGetRelatedRecordsToUniprotQueries");
//        SearchParams searchParams = null;
//        List expResult = null;
//        List result = EnzymeFinder.prepareGetRelatedRecordsToUniprotQueries(searchParams);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of prepareGetUniprotIdQueries method, of class EnzymeFinder.
//     */
//    @Test
//    public void testPrepareGetUniprotIdQueries() {
//        System.out.println("prepareGetUniprotIdQueries");
//        SearchParams searchParams = null;
//        ParamOfGetResults expResult = null;
//        ParamOfGetResults result = EnzymeFinder.prepareGetUniprotIdQueries(searchParams);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
