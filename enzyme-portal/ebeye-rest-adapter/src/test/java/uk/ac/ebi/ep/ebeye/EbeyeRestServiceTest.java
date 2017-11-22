package uk.ac.ebi.ep.ebeye;

import java.util.List;
import java.util.function.BiFunction;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;
import rx.Observable;
import rx.observers.TestSubscriber;
import uk.ac.ebi.ep.ebeye.model.Fields;
import uk.ac.ebi.ep.ebeye.protein.model.Entry;
import uk.ac.ebi.ep.ebeye.protein.model.Protein;
import uk.ac.ebi.ep.ebeye.utils.UrlUtil;

/**
 * Tests the behaviour of the {@link uk.ac.ebi.ep.ebeye.EbeyeRestService} class.
 * @author joseph
 */
@Ignore
@RunWith(MockitoJUnitRunner.class)
public class EbeyeRestServiceTest extends XCentricSetup {

    public static final int NO_RESULT_LIMIT = Integer.MAX_VALUE;
    private BiFunction<String, String, Entry> entryCreator;
    private EbeyeRestService restService;

    @Mock
    private EbeyeQueryService ebeyeQueryService;

    @Before
    public void setUp() throws Exception {
        entryCreator = createEntry();

        restService = new EbeyeRestService(ebeyeQueryService);
    }

    @Test
    public void null_ebeye_query_service_throws_exception() throws Exception {
        EbeyeQueryService ebeyeQueryServiceLocal = null;

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("'EbeyeQueryService' must not be null");

        restService = new EbeyeRestService(ebeyeQueryServiceLocal);
    }

    @Test
    public void null_query_in_unique_accession_search_throws_exception() throws Exception {
        String query = null;
        int limit = 1;

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Query can not be null");

        restService.queryForUniqueAccessions(query, limit);
    }

    @Test
    public void negative_limit_in_unique_accession_search_throws_exception() throws Exception {
        String query = "query";
        int limit = -1;

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Limit can not be less than 1");

        restService.queryForUniqueAccessions(query, limit);
    }

    @Test
    public void zero_limit_in_accession_search_throws_exception() throws Exception {
        String query = "query";
        int limit = 0;

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Limit can not be less than 1");

        restService.queryForUniqueAccessions(query, limit);
    }

    @Test
    public void no_accessions_generated_because_ebeye_query_service_returns_empty_response() {
        String query = "query";

        when(ebeyeQueryService.executeQuery(query)).thenReturn(Observable.empty());

        Observable<String> actualAccessions = restService.queryForUniqueAccessions(query);

        TestSubscriber<String> testSubscriber = subscribe(actualAccessions);

        testSubscriber.assertCompleted();
        testSubscriber.assertNoValues();
    }

    // @Test
    public void returns_accessions_of_entries_with_the_distinct_titles() {
        String query = "query";

        String commonTitle = "title";

        String acc1 = "1";
        Entry entry1 = entryCreator.apply(acc1, commonTitle);

        String acc2 = "2";
        Entry entry2 = entryCreator.apply(acc2, commonTitle);

        when(ebeyeQueryService.executeQuery(query)).thenReturn(Observable.just(entry1, entry2));

        Observable<String> actualAccessions = restService.queryForUniqueAccessions(query);

        TestSubscriber<String> testSubscriber = subscribe(actualAccessions);

        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertCompleted();
        testSubscriber.assertValue(acc1);
    }

    @Test
    public void returns_accessions_of_entries_with_distinct_accessions() {
        String query = "query";

        String commonAccession = "1";

        String title1 = "title1";
        Entry entry1 = entryCreator.apply(commonAccession, title1);

        String title2 = "title2";
        Entry entry2 = entryCreator.apply(commonAccession, title2);

        when(ebeyeQueryService.executeQuery(query)).thenReturn(Observable.just(entry1, entry2));

        Observable<String> actualAccessions = restService.queryForUniqueAccessions(query);

        TestSubscriber<String> testSubscriber = subscribe(actualAccessions);

        testSubscriber.assertCompleted();
        testSubscriber.assertValue(commonAccession);
    }

    @Test
    public void returns_all_accessions_of_distinct_entries() {
        String query = "query";
        int entriesSize = 10;

        List<Entry> distinctEntries = createEntries(entriesSize, entryCreator);

        when(ebeyeQueryService.executeQuery(query)).thenReturn(Observable.from(distinctEntries));

        Observable<String> actualAccessions = restService.queryForUniqueAccessions(query);

        TestSubscriber<String> testSubscriber = subscribe(actualAccessions);

        testSubscriber.assertCompleted();
        testSubscriber.assertValueCount(entriesSize);
    }

    @Test
    public void accession_list_is_empty_because_ebeye_query_service_returns_empty_response() {
        String query = "query";
        int limit = NO_RESULT_LIMIT;

        when(ebeyeQueryService.executeQuery(query)).thenReturn(Observable.empty());

        List<String> actualAccessions = restService.queryForUniqueAccessions(query, limit);

        assertThat(actualAccessions, hasSize(0));
    }

    @Test
    public void list_contains_5_accessions_because_limit_is_set_to_5() {
        String query = "query";
        int limit = 5;

        int entriesSize = 10;

        List<Entry> distinctEntries = createEntries(entriesSize, entryCreator);

        when(ebeyeQueryService.executeQuery(query)).thenReturn(Observable.from(distinctEntries));

        List<String> actualAccessions = restService.queryForUniqueAccessions(query, limit);

        assertThat(actualAccessions, hasSize(5));
    }

    private BiFunction<String, String, Entry> createEntry() {
        return (id, title) -> {
            Fields fields = new Fields();
            Entry entry = new Entry(id, "uniprotname_" + id, "enzymeportal", fields);

            fields.getName().add(title + id);
            fields.getScientificName().add("homo_" + id);
            fields.getStatus().add("Reviewed" + id);

            entry.setFields(fields);

            Protein protein = createProtein().apply(id, "_prtoeinName_" + id);
            entry.setProtein(protein);

            return entry;
        };
    }

    private BiFunction<String, String, Protein> createProtein() {
        return (acc, name) -> new Protein(acc, "proteinName_" + name, "human", "reviewed");
     
    }

    /**
     * Test of queryForUniqueProteins method, of class EbeyeRestService.
     */
    @Test
    public void testQueryForUniqueProteins() {
        String query = "query";
        int entriesSize = 10;

        List<Entry> distinctEntries = createEntries(entriesSize, entryCreator);

        when(ebeyeQueryService.executeQuery(query)).thenReturn(Observable.from(distinctEntries));

        Observable<Protein> actualProteins = restService.queryForUniqueProteins(query);

        TestSubscriber<Protein> testSubscriber = subscribe(actualProteins);

        testSubscriber.assertCompleted();
        testSubscriber.assertValueCount(entriesSize);

    }

    /**
     * Test of queryForUniqueAccessions method, of class EbeyeRestService.
     */
    @Test
    public void testQuery_with_term_and_ec_ForUniqueProteins_with_limit() {
        String searchTerm = "query";
        String ec = "1.1.1.1";
        int entriesSize = 10;
        int limit = 5;
        String query = searchTerm + " AND INTENZ:" + ec;
        query = UrlUtil.encode(query);
        List<Entry> distinctEntries = createEntries(entriesSize, entryCreator);

        when(ebeyeQueryService.executeQuery(query)).thenReturn(Observable.from(distinctEntries));
        List<Protein> actualProteins = restService.queryForUniqueProteins(ec, searchTerm, limit);

        assertThat(actualProteins, hasSize(lessThanOrEqualTo(limit)));

    }

    /**
     * Test of searchForUniqueProteins method, of class EbeyeRestService.
     */
    @Test
    public void testSearchForUniqueProteins_with_ec_and_limit_in_size() {

        String ec = "1.1.1.1";
        int entriesSize = 10;
        int limit = 5;
        String query = "INTENZ:" + ec;
        query = UrlUtil.encode(query);
        List<Entry> distinctEntries = createEntries(entriesSize, entryCreator);

        when(ebeyeQueryService.executeQuery(query)).thenReturn(Observable.from(distinctEntries));
        List<Protein> actualProteins = restService.queryForUniqueProteins(ec, limit);

        assertThat(actualProteins, hasSize(lessThanOrEqualTo(limit)));

    }
}
