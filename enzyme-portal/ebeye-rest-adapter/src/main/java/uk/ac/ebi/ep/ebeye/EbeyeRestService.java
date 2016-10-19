package uk.ac.ebi.ep.ebeye;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestClientException;
import rx.Observable;
import uk.ac.ebi.ep.ebeye.protein.model.Entry;
import uk.ac.ebi.ep.ebeye.protein.model.Protein;
import uk.ac.ebi.ep.ebeye.utils.Preconditions;
import uk.ac.ebi.ep.ebeye.utils.UrlUtil;

/**
 * REST client that communicates with the EBeye search web-service.
 *
 * @author joseph
 */
public class EbeyeRestService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final EbeyeQueryService queryService;

    public EbeyeRestService(EbeyeQueryService queryService) {
        Preconditions.checkArgument(queryService != null, "'EbeyeQueryService' must not be null");

        this.queryService = queryService;
    }

    public List<String> queryForUniqueAccessions(String ec, String searchTerm, int limit) {
        Preconditions.checkArgument(ec != null, "ec can not be null");
        Preconditions.checkArgument(searchTerm != null, "searchTerm can not be null");
        String query = searchTerm + " AND INTENZ:" + ec;

        query = UrlUtil.encode(query);

        return queryForUniqueAccessions(query, limit);
    }

    /**
     *
     * @param omimId omim number
     * @param ec ec number
     * @param limit max number of Accessions to retrieve
     * @return list of unique accessions
     */
    public List<String> queryForUniqueAccessionsByOmimIdAndEc(String omimId, String ec, int limit) {
        Preconditions.checkArgument(omimId != null, "omimId can not be null");
        Preconditions.checkArgument(ec != null, "ec can not be null");

        String query = "OMIM:" + omimId + " AND INTENZ:" + ec;

        query = UrlUtil.encode(query);

        return queryForUniqueAccessions(query, limit);
    }

    /**
     *
     * @param ec EC number
     * @param limit max number of Accessions to retrieve
     * @return list of unique accessions
     */
    public List<String> queryForUniqueAccessionsByEc(String ec, int limit) {
        Preconditions.checkArgument(ec != null, "ec can not be null");

        String query = "INTENZ:" + ec;

        query = UrlUtil.encode(query);

        return queryForUniqueAccessions(query, limit);
    }

    /**
     *
     * @param taxId taxId
     * @param ec ec number
     * @param limit max number of Accessions to retrieve
     * @return list of unique accessions
     */
    public List<String> queryForUniqueAccessionsByTaxIdAndEc(String taxId, String ec, int limit) {
        Preconditions.checkArgument(taxId != null, "taxId can not be null");
        Preconditions.checkArgument(ec != null, "ec can not be null");

        String query = "TAXONOMY:" + taxId + " AND INTENZ:" + ec;

        query = UrlUtil.encode(query);

        return queryForUniqueAccessions(query, limit);
    }

    /**
     *
     * @param pathwayId pathwayId
     * @param ec ec number
     * @param limit max number of Accessions to retrieve
     * @return list of unique accessions
     */
    public List<String> queryForUniqueAccessionsByPathwayIdAndEc(String pathwayId, String ec, int limit) {
        Preconditions.checkArgument(pathwayId != null, "pathwayId can not be null");
        Preconditions.checkArgument(ec != null, "ec can not be null");

        String query = "REACTOME:" + pathwayId + " AND INTENZ:" + ec;

        query = UrlUtil.encode(query);

        return queryForUniqueAccessions(query, limit);
    }

    /**
     * Sends a query to the Ebeye search service and creates a response with the
     * accessions of the entries that fulfill the search criteria.
     *
     * @param query the client query
     * @param limit limit the number of results from Ebeye service. Use
     * {@link #NO_RESULT_LIMIT} if no limit is to be specified
     * @return list of accessions that fulfill the query
     */
    public List<String> queryForUniqueAccessions(String query, int limit) {
        Preconditions.checkArgument(limit > 0, "Limit can not be less than 1");

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<String> uniqueAccessions = queryForUniqueAccessions(query)
                .limit(limit)
                .toList()
                .toBlocking()
                .single();

        stopWatch.stop();
        logger.debug("Executing query:{}, took: {}", query, stopWatch.getTotalTimeSeconds() + " sec");

        return uniqueAccessions;
    }

    /**
     * Sends a query to the Ebeye search service and creates a response with the
     * proteins of the entries that fulfill the search criteria.
     *
     * @param query the client query
     * @param limit limit the number of results from Ebeye service. Use
     * {@link #NO_RESULT_LIMIT} if no limit is to be specified
     * @return list of Proteins that fulfill the query
     */
    public List<Protein> searchForUniqueProteins(String query, int limit) {
        Preconditions.checkArgument(limit > 0, "Limit can not be less than 1");

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<Protein> uniqueProteins = queryForUniqueProteins(query)
                .limit(limit)
                .toList()
                .toBlocking()
                .single();

        stopWatch.stop();
        logger.debug("Executing query:{}, took: {}", query, stopWatch.getTotalTimeSeconds() + " sec");

        return uniqueProteins;
    }

    /**
     * Sends a query to the Ebeye search service and creates an
     * {@link Observable} which pushes the results as they are calculated. This
     * allows the results to be processed asynchronously by the client calling
     * the method.
     *
     * @param query the client query
     * @return Observable of accessions that fulfill the query
     */
    public Observable<String> queryForUniqueAccessions(String query) {
        Preconditions.checkArgument(query != null, "Query can not be null");

        Observable<String> uniqueAccessions;

        try {
            Observable<Entry> distinctEntries = queryService.executeQuery(query).distinct();

            uniqueAccessions = getDistinctAccessionsFromEntries(distinctEntries);
        } catch (RestClientException e) {
            logger.error(e.getMessage(), e);
            uniqueAccessions = Observable.empty();
        }

        return uniqueAccessions;
    }

    private Observable<String> getDistinctAccessionsFromEntries(Observable<Entry> accessionObservable) {
        return accessionObservable
                .map(Entry::getUniprotAccession)
                .distinct();
    }
    //proteins

    private Observable<Protein> getDistinctProteinsFromEntries(Observable<Entry> proteinObservable) {
        return proteinObservable
                .map(Entry::getProtein)
                .distinct();

    }

    /**
     * Sends a query to the Ebeye search service and creates an
     * {@link Observable} which pushes the results as they are calculated. This
     * allows the results to be processed asynchronously by the client calling
     * the method.
     *
     * @param query the client query
     * @return Observable of proteins that fulfill the query
     */
    public Observable<Protein> queryForUniqueProteins(String query) {
        Preconditions.checkArgument(query != null, "Query can not be null");

        Observable<Protein> uniqueProteins;
        try {
            Observable<Entry> distinctEntries = queryService.executeQuery(query).distinct();
            uniqueProteins = getDistinctProteinsFromEntries(distinctEntries);
        } catch (RestClientException e) {
            logger.error(e.getMessage(), e);
            uniqueProteins = Observable.empty();
        }

        return uniqueProteins;
    }

    public List<Protein> queryForUniqueProteins(String ec, int limit) {
        Preconditions.checkArgument(ec != null, "ec can not be null");
        String query = "INTENZ:" + ec;
        query = UrlUtil.encode(query);

        return searchForUniqueProteins(query, limit);
    }

    public List<Protein> queryForUniqueProteins(String ec, String searchTerm, int limit) {
        Preconditions.checkArgument(ec != null, "ec can not be null");
        Preconditions.checkArgument(searchTerm != null, "searchTerm can not be null");
        String query = searchTerm + " AND INTENZ:" + ec;
        query = UrlUtil.encode(query);
        return searchForUniqueProteins(query, limit);

    }

    public List<Protein> findUniqueProteinsByOmimIdAndEc(String omimId, String ec, int limit) {
        Preconditions.checkArgument(omimId != null, "omimId can not be null");
        Preconditions.checkArgument(ec != null, "ec can not be null");

        String query = "OMIM:" + omimId + " AND INTENZ:" + ec;

        query = UrlUtil.encode(query);
        return searchForUniqueProteins(query, limit);

    }

    public List<Protein> findUniqueProteinsByTaxIdAndEc(String taxId, String ec, int limit) {
        Preconditions.checkArgument(taxId != null, "taxId can not be null");
        Preconditions.checkArgument(ec != null, "ec can not be null");

        String query = "TAXONOMY:" + taxId + " AND INTENZ:" + ec;

        query = UrlUtil.encode(query);
        return searchForUniqueProteins(query, limit);

    }

    public List<Protein> findUniqueProteinsByPathwayIdAndEc(String pathwayId, String ec, int limit) {
        Preconditions.checkArgument(pathwayId != null, "pathwayId can not be null");
        Preconditions.checkArgument(ec != null, "ec can not be null");

        String query = "REACTOME:" + pathwayId + " AND INTENZ:" + ec;

        query = UrlUtil.encode(query);
        return searchForUniqueProteins(query, limit);

    }

}
