package uk.ac.ebi.ep.ebeye;

import uk.ac.ebi.ep.ebeye.config.EbeyeIndexUrl;
import uk.ac.ebi.ep.ebeye.search.EbeyeSearchResult;
import uk.ac.ebi.ep.ebeye.search.Entry;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.observers.TestSubscriber;

/**
 * Parent class for the {@link EbeyeRestServiceTest} and the {@link EnzymeCentricServiceTest} classes.
 *
 * This class is responsible for setting up common configurations, and holding utility methods shared between the
 * test classes.
 */
public abstract class XCentricSetup {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    protected List<Entry> createEntries(int num, BiFunction<String, String, Entry> entryCreator) {
        return IntStream.range(0, num)
                .mapToObj(String::valueOf)
                .map(id -> entryCreator.apply(id, id))
                .collect(Collectors.toList());
    }

    protected <T> TestSubscriber<T> subscribe(Observable<T> observable) {
        TestSubscriber<T> testSubscriber = new TestSubscriber<>();
        observable.subscribe(testSubscriber);

        return testSubscriber;
    }
}
