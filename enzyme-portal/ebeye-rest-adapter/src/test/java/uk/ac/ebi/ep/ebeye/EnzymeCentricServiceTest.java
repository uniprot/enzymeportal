package uk.ac.ebi.ep.ebeye;

import java.util.List;
import java.util.function.BiFunction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;
import rx.observers.TestSubscriber;

import uk.ac.ebi.ep.ebeye.search.Entry;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@RunWith(MockitoJUnitRunner.class)
public class EnzymeCentricServiceTest extends XCentricSetup {
    private BiFunction<String, String, Entry> entryCreator;

    private EnzymeCentricService enzymeCentricService;

    @Mock
    private EbeyeQueryService ebeyeQueryService;

    @Before
    public void setUp() throws Exception {
        entryCreator = createEntry();

        enzymeCentricService = new EnzymeCentricService(ebeyeQueryService);
    }

    @Test
    public void null_ebeye_query_service_throws_exception() throws Exception {
        EbeyeQueryService ebeyeQueryService = null;

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("'EbeyeQueryService' must not be null");

        enzymeCentricService = new EnzymeCentricService(ebeyeQueryService);
    }

    @Test
    public void null_EcQuery_in_query_for_EcNumbers_throws_exception() throws Exception {
        String query = null;
        int limit = EnzymeCentricService.NO_RESULT_LIMIT;

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Query can not be null");

        enzymeCentricService.queryEbeyeForEcNumbers(query, limit);
    }

    @Test
    public void negative_retrieval_limit_value_in_query_for_EcNumbers_throws_exception() throws Exception {
        String query = "kinase";
        int limit = -1;

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Limit must be greater than 0");

        enzymeCentricService.queryEbeyeForEcNumbers(query, limit);
    }

    @Test
    public void zero_retrieval_limit_value_in_query_for_EcNumbers_throws_exception() throws Exception {
        String query = "kinase";
        int limit = 0;

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Limit must be greater than 0");

        enzymeCentricService.queryEbeyeForEcNumbers(query, limit);
    }

    @Test
    public void no_accessions_generated_because_ebeye_query_service_returns_empty_response() {
        String query = "query";

        when(ebeyeQueryService.executeQuery(query)).thenReturn(Observable.empty());

        Observable<String> actualAccessions = enzymeCentricService.queryEbeyeForEcNumbers(query);

        TestSubscriber<String> testSubscriber = subscribe(actualAccessions);

        testSubscriber.assertCompleted();
        testSubscriber.assertNoValues();
    }

    @Test
    public void returns_ec_numbers_of_entries_with_the_distinct_ec_numbers() {
        String query = "query";

        String commonEC = "1.2.3.4";

        String acc1 = "1";
        Entry entry1 = entryCreator.apply(acc1, commonEC);

        String acc2 = "2";
        Entry entry2 = entryCreator.apply(acc2, commonEC);

        when(ebeyeQueryService.executeQuery(query)).thenReturn(Observable.just(entry1, entry2));

        Observable<String> actualAccessions = enzymeCentricService.queryEbeyeForEcNumbers(query);

        TestSubscriber<String> testSubscriber = subscribe(actualAccessions);

        testSubscriber.assertCompleted();
        testSubscriber.assertValue(commonEC);
    }

    @Test
    public void returns_all_ec_numbers_of_distinct_entries() {
        String query = "query";
        int entriesSize = 10;

        List<Entry> distinctEntries = createEntries(entriesSize, entryCreator);

        when(ebeyeQueryService.executeQuery(query)).thenReturn(Observable.from(distinctEntries));

        Observable<String> actualAccessions = enzymeCentricService.queryEbeyeForEcNumbers(query);

        TestSubscriber<String> testSubscriber = subscribe(actualAccessions);

        testSubscriber.assertCompleted();
        testSubscriber.assertValueCount(entriesSize);
    }

    @Test
    public void accession_list_is_empty_because_ebeye_query_service_returns_empty_response() {
        String query = "query";
        int limit = EbeyeRestService.NO_RESULT_LIMIT;

        when(ebeyeQueryService.executeQuery(query)).thenReturn(Observable.empty());

        List<String> actualAccessions = enzymeCentricService.queryEbeyeForEcNumbers(query, limit);

        assertThat(actualAccessions, hasSize(0));
    }

    @Test
    public void list_contains_5_accessions_because_limit_is_set_to_5() {
        String query = "query";
        int limit = 5;

        int entriesSize = 10;

        List<Entry> distinctEntries = createEntries(entriesSize, entryCreator);

        when(ebeyeQueryService.executeQuery(query)).thenReturn(Observable.from(distinctEntries));

        List<String> actualAccessions = enzymeCentricService.queryEbeyeForEcNumbers(query, limit);

        assertThat(actualAccessions, hasSize(5));
    }

    private BiFunction<String, String, Entry> createEntry() {
        return (id, ec) -> {
            Entry entry = new Entry(id, id + "_" + id);
            entry.setEc(ec);
            return entry;
        };
    }
}