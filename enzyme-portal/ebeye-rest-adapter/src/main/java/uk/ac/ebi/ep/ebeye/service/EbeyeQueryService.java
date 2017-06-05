
package uk.ac.ebi.ep.ebeye.service;

import rx.Observable;

/**
 * Service responsible for querying the EbeyeSearch REST webservice.
 *
 * The service decides on the best way to achieve the clients response in the shortest amount of time.
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 * @param <E> Entry
 */
public interface EbeyeQueryService<E> {
        /**
     * Submits the {@param query} to the EbeyeSearch REST service. The result will be an {@link Observable}
     * containing instances of {@link Entry} that match the query.
     *
     * @param query the query to search for
     * @return an Observable with the resulting Entry objects
     */
    Observable<E> executeQuery(String query);

}
