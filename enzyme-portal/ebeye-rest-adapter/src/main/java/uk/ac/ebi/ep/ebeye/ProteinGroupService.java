package uk.ac.ebi.ep.ebeye;

import java.util.List;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.ebeye.config.EbeyeIndexProps;
import uk.ac.ebi.ep.ebeye.model.proteinGroup.ProteinGroupSearchResult;
import uk.ac.ebi.ep.ebeye.service.ProteinQueryServiceImpl;
import uk.ac.ebi.ep.ebeye.utils.Preconditions;
import uk.ac.ebi.ep.ebeye.utils.UrlUtil;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
public class ProteinGroupService extends ProteinQueryServiceImpl {

    // private final Logger logger = LoggerFactory.getLogger(this.getClass());
//    public static final String simpleUrl = "http://wwwdev.ebi.ac.uk/ebisearch/ws/rest/enzymeportal_proteins?query=sildenafil&start=0&size=100&fields=id,name";
//    public static final String pGroupByEC = "http://wwwdev.ebi.ac.uk/ebisearch/ws/rest/enzymeportal_proteins?query=INTENZ:2.1.1.1&start=0&size=100&fields=id,name";
//    String pGroupByECJson = "http://wwwdev.ebi.ac.uk/ebisearch/ws/rest/enzymeportal_proteins?query=INTENZ:2.1.1.1&start=0&size=100&fields=id,name&sort=_relevance&reverse=true&format=json";
//    public static final String restUrl = "http://wwwdev.ebi.ac.uk/ebisearch/ws/rest/enzymeportal_proteins?query=sildenafil&fields=id,name,common_name,scientific_name,compound_name,disease_name";
    // public static final String QUERY_URL = "%s?query=%s&start=%d&size=%d&fields=id,primary_organism,primary_accession,name,UNIPROTKB,common_name,scientific_name&sort=_relevance&reverse=true&format=json";
    public static final String QUERY_URL = "%s?query=%s&start=%d&size=%d&fields=id,primary_organism,primary_accession,name,common_name,scientific_name&sort=_relevance&reverse=true&format=json";

    //private final RestTemplate restTemplate;
    //private final EbeyeIndexProps proteinGroupProps;
    private static final int MAX_RETRIEVABLE_ENTRIES = 10_000;

    public ProteinGroupService(RestTemplate restTemplate, EbeyeIndexProps proteinGroupPropertyFile) {
        super(proteinGroupPropertyFile, restTemplate, MAX_RETRIEVABLE_ENTRIES);
        //this.restTemplate = restTemplate;
        //this.proteinGroupProps = proteinGroupPropertyFile;
    }

    private ProteinGroupSearchResult getProteinGroupResult(String url) {

        logger.info("URL sent to EBI Service " + url);

        ProteinGroupSearchResult results = restTemplate.getForObject(url.trim(), ProteinGroupSearchResult.class);
        return results;
    }

    private String buildQueryUrl(String endpoint, String query, int startPage, int pageSize) {

        //String ebeyeQueryUrl = "%s?query=%s&facetcount=%d&facets:TAXONOMY&start=%d&size=%d&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,enzyme_family&sort=_relevance&reverse=true&format=json";
        return String.format(QUERY_URL, endpoint, query, startPage, pageSize);
    }

    /**
     *
     * @param query searchTerm
     * @param startPage start page
     * @param pageSize page size
     * @return
     */
    private ProteinGroupSearchResult getSearchResult(String query, int startPage, int pageSize) {

        Preconditions.checkArgument(startPage > -1, "startPage can not be less than 0");
        Preconditions.checkArgument(pageSize > -1, "pageSize can not be less than 0");
        Preconditions.checkArgument(query != null, "'query' must not be null");

        return getProteinGroupResult(buildQueryUrl(ebeyeIndexProps.getProteinGroupSearchUrl(), query, startPage, pageSize));
    }

    public ProteinGroupSearchResult findProteinGroupResultByEC(String ec, int startPage, int pageSize) {
        Preconditions.checkArgument(ec != null, "'ec' must not be null");
        if (pageSize > 100) {
            pageSize = 100;
        }

        String query = "INTENZ:" + ec;

        return getSearchResult(query, startPage, pageSize);

    }

    public ProteinGroupSearchResult findProteinGroupResultBySearchTermAndEC(String ec, String searchTerm, int startPage, int pageSize) {
        Preconditions.checkArgument(ec != null, "ec can not be null");
        Preconditions.checkArgument(searchTerm != null, "searchTerm can not be null");
        String query = searchTerm + " AND INTENZ:" + ec;
        if (pageSize > 100) {
            pageSize = 100;
        }
         return getSearchResult(query, startPage, pageSize);
    }

    public ProteinGroupSearchResult findUniqueProteinsByOmimIdAndEc(String omimId, String ec, int limit) {
        Preconditions.checkArgument(omimId != null, "omimId can not be null");
        Preconditions.checkArgument(ec != null, "ec can not be null");
        int startPage = 0;
        if (limit > 100) {
            limit = 100;
        }
        String query = "OMIM:" + omimId + " AND INTENZ:" + ec;

        return getSearchResult(query, startPage, limit);

    }

    public List<String> queryForPrimaryAccessionsByOmimIdAndEc(String omimId, String ec, int limit) {
        Preconditions.checkArgument(omimId != null, "omimId can not be null");
        Preconditions.checkArgument(ec != null, "ec can not be null");

        String query = "OMIM:" + omimId + " AND INTENZ:" + ec;

        query = UrlUtil.encode(query);
        return queryForUniquePrimaryAccessions(query, limit);

    }

    public ProteinGroupSearchResult findUniqueProteinsByPathwayIdAndEc(String pathwayId, String ec, int limit) {
        Preconditions.checkArgument(pathwayId != null, "pathwayId can not be null");
        Preconditions.checkArgument(ec != null, "ec can not be null");

        String query = "REACTOME:" + pathwayId + " AND INTENZ:" + ec;
        int startPage = 0;
        if (limit > 100) {
            limit = 100;
        }
 
        return getSearchResult(query, startPage, limit);

    }

    public List<String> queryForPrimaryAccessionsByPathwayIdAndEc(String pathwayId, String ec, int limit) {
        Preconditions.checkArgument(pathwayId != null, "pathwayId can not be null");
        Preconditions.checkArgument(ec != null, "ec can not be null");

        String query = "REACTOME:" + pathwayId + " AND INTENZ:" + ec;
        query = UrlUtil.encode(query);
        return queryForUniquePrimaryAccessions(query, limit);

    }

    public ProteinGroupSearchResult findUniqueProteinsByTaxIdAndEc(String taxId, String ec, int limit) {
        Preconditions.checkArgument(taxId != null, "taxId can not be null");
        Preconditions.checkArgument(ec != null, "ec can not be null");
        int startPage = 0;
        if (limit > 100) {
            limit = 100;
        }
        String query = "TAXONOMY:" + taxId + " AND INTENZ:" + ec;

      
        return getSearchResult(query, startPage, limit);

    }

    public List<String> queryForPrimaryAccessionsByTaxIdAndEc(String taxId, String ec, int limit) {
        Preconditions.checkArgument(taxId != null, "taxId can not be null");
        Preconditions.checkArgument(ec != null, "ec can not be null");

        String query = "TAXONOMY:" + taxId + " AND INTENZ:" + ec;

        query = UrlUtil.encode(query);
        return queryForUniquePrimaryAccessions(query, limit);

    }

    public List<String> queryForPrimaryAccessionsByEcAndKeyword(String ec, String searchTerm, int limit) {
        Preconditions.checkArgument(ec != null, "ec can not be null");
        Preconditions.checkArgument(searchTerm != null, "searchTerm can not be null");
        String query = searchTerm + " AND INTENZ:" + ec;

        query = UrlUtil.encode(query);

        return queryForUniquePrimaryAccessions(query, limit);
    }

    public List<String> queryForPrimaryAccessionsByEc(String ec, int limit) {
        Preconditions.checkArgument(ec != null, "ec can not be null");

        String query = "INTENZ:" + ec;

        query = UrlUtil.encode(query);

        return queryForUniquePrimaryAccessions(query, limit);
    }

//    
//        /**
//     * Sends a query to the Ebeye search service and creates a response with the
//     * accessions of the entries that fulfill the search criteria.
//     *
//     * @param query the client query
//     * @param limit limit the number of results from Ebeye service. Use
//     * {@link #NO_RESULT_LIMIT} if no limit is to be specified
//     * @return list of accessions that fulfill the query
//     */
//    public List<String> queryForUniqueAccessions(String query, int limit) {
//        Preconditions.checkArgument(limit > 0, "Limit can not be less than 1");
//
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//
//        List<String> uniqueAccessions = queryForUniqueAccessions(query)
//                .limit(limit)
//                .toList()
//                .toBlocking()
//                .single();
//
//        stopWatch.stop();
//        logger.debug("Executing query:{}, took: {}", query, stopWatch.getTotalTimeSeconds() + " sec");
//
//        return uniqueAccessions;
//    }
//    
//    
//    
//    /**
//     * Sends a query to the Ebeye search service and creates an
//     * {@link Observable} which pushes the results as they are calculated. This
//     * allows the results to be processed asynchronously by the client calling
//     * the method.
//     *
//     * @param query the client query
//     * @return Observable of accessions that fulfill the query
//     */
//    public Observable<String> queryForUniqueAccessions(String query) {
//        Preconditions.checkArgument(query != null, "Query can not be null");
//
//        Observable<String> uniqueAccessions;
//
//        try {
//            Observable<ProteinGroupEntry> distinctEntries = executeQuery(query).distinct();
//
//            uniqueAccessions = getDistinctAccessionsFromEntries(distinctEntries);
//        } catch (RestClientException e) {
//            logger.error(e.getMessage(), e);
//            uniqueAccessions = Observable.empty();
//        }
//
//        return uniqueAccessions;
//    }
//
//    private Observable<String> getDistinctAccessionsFromEntries(Observable<ProteinGroupEntry> accessionObservable) {
//        return accessionObservable
//                .map(ProteinGroupEntry::getPrimaryAccession)
//                .distinct();
//    }
    //// TODO
//    private ProteinGroupSearchResult getProteinViewResult(String url) {
//
//        logger.info("URL sent to EBI Service " + url);
//        System.out.println("protein group  URL SENT " + url);
//        ProteinGroupSearchResult results = restTemplate.getForObject(url.trim(), ProteinGroupSearchResult.class);
//        return results;
//    }
//
//    /**
//     *
//     * @param query searchTerm
//     * @param startPage start page
//     * @param pageSize page size
//     * @return
//     */
//    private ProteinGroupSearchResult getProteinSearchResult(String query, int startPage, int pageSize) {
//
//        Preconditions.checkArgument(startPage > -1, "startPage can not be less than 0");
//        Preconditions.checkArgument(pageSize > -1, "pageSize can not be less than 0");
//        Preconditions.checkArgument(query != null, "'query' must not be null");
//
//        return getProteinViewResult(buildQueryUrl(proteinGroupProps.getProteinGroupSearchUrl(), query, startPage, pageSize));
//    }
//
//    public ProteinGroupSearchResult findProteinGroupResultsByEC(String ec, int startPage, int pageSize) {
//        Preconditions.checkArgument(ec != null, "ec can not be null");
//        //Preconditions.checkArgument(searchTerm != null, "searchTerm can not be null");
//        String query = "INTENZ:" + ec;
//        if (pageSize > 100) {
//            pageSize = 100;
//        }
//        //query = UrlUtil.encode(query);
//        return getProteinSearchResult(query, startPage, pageSize);
//    }
//    
}
