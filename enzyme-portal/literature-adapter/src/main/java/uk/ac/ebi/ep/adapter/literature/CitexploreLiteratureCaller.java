package uk.ac.ebi.ep.adapter.literature;

import java.util.Collection;
import java.util.concurrent.Callable;

import uk.ac.ebi.biobabel.citations.CitexploreWSClient;
import uk.ac.ebi.cdb.webservice.Result;

public class CitexploreLiteratureCaller implements Callable<Collection<Result>> {

	private String query;
    private Integer connectTimeout;
    private Integer readTimeout;
    private Integer maxCitations;

    CitexploreLiteratureCaller(String query){
		this(query, 0, 0, 50);
	}

    /**
     * Complete constructor.
     * @param query the query to send to CiteXplore web services (complete,
     *      following the syntax for any
     *      <a href="http://europepmc.org/Help#fieldsearch">field searches</a>).
     * @param connectTimeout the connection timeout for the request to
     *      CiteXplore.
     * @param readTimeout the read timeout for the request to CiteXplore.
     * @param maxCitations the maximum number of citations to retrieve.
     * @since 1.0.7
     */
    public CitexploreLiteratureCaller(String query, Integer connectTimeout,
            Integer readTimeout, Integer maxCitations) {
        this.query = query;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.maxCitations = maxCitations;
    }

    public Collection<Result> call() throws Exception {
        return searchCitations();
	}

    /**
     * Searches CiteXplore for citations.
     * @return a collection of citations from CiteXplore web services, or
     *      <code>null</code> if none found.
     * @throws Exception
     * @since 1.0.7 (refactored out of {@link #call()})
     */
    Collection<Result> searchCitations() throws Exception {
        CitexploreWSClient citexploreClient = null;
        try {
            citexploreClient =
                    CitexploreWSClientPool.getInstance().borrowObject();
            citexploreClient.setConnectTimeout(connectTimeout);
            citexploreClient.setReadTimeout(readTimeout);
            citexploreClient.setMaxCitations(maxCitations);
            return citexploreClient.searchCitations(query);
        } finally {
            if (citexploreClient != null){
                CitexploreWSClientPool.getInstance()
                        .returnObject(citexploreClient);
            }
        }
    }

}
