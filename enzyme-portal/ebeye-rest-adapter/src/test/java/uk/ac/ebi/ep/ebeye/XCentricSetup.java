package uk.ac.ebi.ep.ebeye;

import uk.ac.ebi.ep.ebeye.search.Entry;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
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
