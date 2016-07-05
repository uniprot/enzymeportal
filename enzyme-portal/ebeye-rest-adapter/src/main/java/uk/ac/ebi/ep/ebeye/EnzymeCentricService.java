package uk.ac.ebi.ep.ebeye;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.ebeye.config.EbeyeIndexProps;
import uk.ac.ebi.ep.ebeye.model.EBISearchResult;
import uk.ac.ebi.ep.ebeye.utils.Preconditions;

/**
 * REST client that communicates with the EBeye search web-service retrieving
 * only enzyme centric data.
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class EnzymeCentricService {

    //public static final int NO_RESULT_LIMIT = Integer.MAX_VALUE;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RestTemplate restTemplate;
    private final EbeyeIndexProps enzymeCentricProps;

    public EnzymeCentricService(RestTemplate restTemplate, EbeyeIndexProps enzymeCentricPropsFile) {
        this.restTemplate = restTemplate;
        this.enzymeCentricProps = enzymeCentricPropsFile;
    }

//    private String buildQueryUrl(String endpoint, String query, int resultSize, int start) {
//        String ebeyeQueryUrl = "%s?query=%s&size=%d&start=%d&fields=name,status&format=json";
//
//        return String.format(ebeyeQueryUrl, endpoint, query, resultSize, start);
//    }
    private String buildQueryUrl(String endpoint, String query, int facetCount, String facets, int startPage, int pageSize) {
        //String ebeyeQueryUrl = "%s?query=%s&size=%d&start=%d&fields=name,status&format=json";
        String ebeyeQueryUrl = "%s?query=%s&facetcount=%d&facets:TAXONOMY,OMIM,compound_type&compound_name&start=%d&size=%d&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,compound_name,disease_name,enzyme_family&format=json";

        //String url = "http://www.ebi.ac.uk/ebisearch/ws/rest/enzymeportal_enzymes?query=" + query + "&facetcount=" 
        // + facetCount + "&facets:TAXONOMY,OMIM,compound_type&compound_name&start=" + startPage + "&size=" + pageSize + "&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,compound_name,disease_name,enzyme_family&format=json";
        if (!StringUtils.isEmpty(facets) && StringUtils.hasText(facets)) {
            // url = "http://www.ebi.ac.uk/ebisearch/ws/rest/enzymeportal_enzymes?query=" + query + "&facetcount=" + facetCount + "&facets=" + facets + "&start=" + startPage + "&size=" + pageSize + "&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,compound_name,disease_name,enzyme_family&format=json";
            ebeyeQueryUrl = "%s?query=%s&facetcount=%d&facets=%s&start=%d&size=%d&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,compound_name,disease_name,enzyme_family&format=json";
            return String.format(ebeyeQueryUrl, endpoint, query, facetCount, facets, startPage, pageSize);
        }
        //System.out.println(""+ ebeyeQueryUrl + " end "+ endpoint + " query "+ query + " facount "+ facetCount + " facets "+ facets + " start"+ startPage + " size "+ pageSize );
        return String.format(ebeyeQueryUrl, endpoint, query, facetCount, startPage, pageSize);
    }

    /**
     *
     * @param query searchTerm
     * @param startPage start page
     * @param pageSize page size
     * @param facets comma separated facets
     * @param facetCount number of facets to be returned
     * @return
     */
    public EBISearchResult getSearchResult(String query, int startPage, int pageSize, String facets, int facetCount) {

        Preconditions.checkArgument(startPage > -1, "startPage can not be less than 0");
        Preconditions.checkArgument(pageSize > -1, "pageSize can not be less than 0");
        Preconditions.checkArgument(query != null, "'query' must not be null");
        Preconditions.checkArgument(facets != null, "'facets' must not be null");
        Preconditions.checkArgument(facetCount > -1, "startPage can not be less than 0");

        if (facetCount > 1_000) {
            facetCount = 1_000;
        }
        //http://wwwdev.ebi.ac.uk/ebisearch/ws/rest/enzymeportal_enzymes?query=3.4.24.55&facetcount=10&facets:TAXONOMY,OMIM,compound_type&compound_name&start=0&size=10&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,compound_name,disease_name,enzyme_family&format=json
        //System.out.println("FACETS "+ facets);

//        String url = "http://www.ebi.ac.uk/ebisearch/ws/rest/enzymeportal_enzymes?query=" + query + "&facetcount=" + facetCount + "&facets:TAXONOMY,OMIM,compound_type&compound_name&start=" + startPage + "&size=" + pageSize + "&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,compound_name,disease_name,enzyme_family&format=json";
//        if (!StringUtils.isEmpty(facets) && StringUtils.hasText(facets)) {
//            url = "http://www.ebi.ac.uk/ebisearch/ws/rest/enzymeportal_enzymes?query=" + query + "&facetcount=" + facetCount + "&facets=" + facets + "&start=" + startPage + "&size=" + pageSize + "&fields=id,name,description,UNIPROTKB,protein_name,common_name,scientific_name,compound_name,disease_name,enzyme_family&format=json";
//
//        }
        System.out.println("INDEX " + enzymeCentricProps.getEnzymeCentricSearchUrl());

        return getEbiSearchResult(buildQueryUrl(enzymeCentricProps.getEnzymeCentricSearchUrl(), query, facetCount, facets, startPage, pageSize));
    }

    private EBISearchResult getEbiSearchResult(String url) {
        EBISearchResult results = restTemplate.getForObject(url.trim(), EBISearchResult.class);
        return results;
    }

    ///observable impl
//    private final EbeyeQueryService queryService;
//
//    public EnzymeCentricService(EbeyeQueryService queryService) {
//        Preconditions.checkArgument(queryService != null, "'EbeyeQueryService' must not be null");
//
//        this.queryService = queryService;
//    }
//    
//    
//
//
//    /**
//     * Sends a query to the Ebeye search service and creates a response with the
//     * EC numbers of the entries that fulfill the search criteria.
//     *
//     * @param query the client query
//     * @param limit limit the number of results from Ebeye service. Use
//     * {@link #NO_RESULT_LIMIT} if no limit is to be specified
//     * @return list of EC numbers that fulfill the query
//     */
//    public List<String> queryEbeyeForEcNumbers(String query, int limit) {
//        Preconditions.checkArgument(limit > 0, "Limit must be greater than 0");
//
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//
//        List<String> uniqueECs = queryEbeyeForEcNumbers(query)
//                .limit(limit)
//                .toList()
//                .toBlocking()
//                .single();
//
//        stopWatch.stop();
//        logger.debug("Executing query:{}, took: {}", query, stopWatch.getTotalTimeMillis());
//
//        return uniqueECs;
//    }
//
//    /**
//     * Sends a query to the Ebeye search service and creates an {@link Observable} which pushes the results as they
//     * are calculated. This allows the results to be processed asynchronously by the client calling the method.
//     *
//     * @param query the client query
//     * @return Observable of accessions that fulfill the query
//     */
//    public Observable<String> queryEbeyeForEcNumbers(String query) {
//        Preconditions.checkArgument(query != null, "Query can not be null");
//
//        Observable<String> uniqueAccessions;
//
//        try {
//            Observable<Entry> distinctEntries = queryService.executeQuery(query);
//
//            uniqueAccessions = getDistinctEcNumbersFromEntries(distinctEntries);
//        } catch (RestClientException e) {
//            logger.error(e.getMessage(), e);
//            uniqueAccessions = Observable.empty();
//        }
//
//        return uniqueAccessions;
//    }
//
//    private Observable<String> getDistinctEcNumbersFromEntries(Observable<Entry> entryObservable) {
//        return entryObservable
//                .map(EnzymeEntry::getEc)
//                .distinct();
//    }   
//    
    //end of observable impl
//    private final Logger logger = LoggerFactory.getLogger(EnzymeCentricService.class);
//    
//    private final AsyncRestTemplate asyncRestTemplate;
//    
//    private final EbeyeIndexProps ebeyeIndexUrl;
//    
//    private final RestTemplate restTemplate;
//    //Maximum number of entries that this service will ask from the EbeyeSearch
//    private final int MAX_HITS_TO_PROCESS = 20_000;
//    private final int FJP_TRIGGER = 4;
//    private final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();
//    
//    //@Autowired
//   // private HttpComponentsAsyncClientHttpRequestFactory httpComponentsAsyncClientHttpRequestFactory;
//    
//    public EnzymeCentricService(EbeyeIndexProps ebeyeIndexUrl, RestTemplate restTemplate, AsyncRestTemplate asyncRestTemplate) {
//        Assert.notNull(restTemplate, "'restTemplate' must not be null");
//        Assert.notNull(asyncRestTemplate, "'asyncRestTemplate' must not be null");
//        Assert.notNull(ebeyeIndexUrl, "'ebeyeIndexUrl' must not be null");
//        this.asyncRestTemplate = asyncRestTemplate;
//        this.ebeyeIndexUrl = ebeyeIndexUrl;
//        this.restTemplate = restTemplate;
//    }
//    
//    public List<String> queryEbeyeForEcNumbers(String query, int limit) {
//        Assert.notNull(query, "Query cannot be null");
//        Assert.isTrue(limit > 0, "Limit must be greater than 0");
//        EbeyeSearchResult searchResult = synchronousQuery(query);
//        
//        int synchronousHitCount = searchResult.getHitCount();
//        int hitCount = synchronousHitCount < MAX_HITS_TO_PROCESS ? synchronousHitCount : MAX_HITS_TO_PROCESS;
//        
//        logger.debug("Number of hits for search for [" + query + "] : " + hitCount);
//        
//        if (hitCount <= ebeyeIndexUrl.getMaxEbiSearchLimit()) {
//            
//            return extractDistinctEcNumbersFromSynchronousQuery(searchResult);
//        } else {
//            
//            int numIteration = (int) Math.ceil((double) hitCount / (double) ebeyeIndexUrl.getMaxEbiSearchLimit());
//            return asynchronousQuery(query, numIteration, limit);
//        }
//        
//    }
//    
//    private EbeyeSearchResult synchronousQuery(String query) {
//        String url = buildQueryUrl(ebeyeIndexUrl.getEnzymeCentricSearchUrl(),
//                query,
//                ebeyeIndexUrl.getMaxEbiSearchLimit(),
//                0);
//        
//        return restTemplate.getForObject(url, EbeyeSearchResult.class);
//    }
//    
//    private List<String> extractDistinctEcNumbersFromSynchronousQuery(EbeyeSearchResult searchResult) {
//        
//        return searchResult
//                .getEntries().stream()
//                .map(ec -> (ec.getEc()))
//                .distinct()
//                .collect(Collectors.toList());
//        
//    }
//    
//    private String buildQueryUrl(String endpoint, String query, int resultSize, int start) {
//        String ebeyeQueryUrl = "%s?query=%s&size=%d&start=%d&fields=name&format=json";
//        
//        return String.format(ebeyeQueryUrl, endpoint, query, resultSize, start);
//    }
//    
//    private List<String> buildUrlsForAsyncRequest(String query, int numIteration) {
//        List<String> urls = IntStream.range(0, numIteration)
//                .parallel()
//                .mapToObj(index -> buildQueryUrl(ebeyeIndexUrl.getEnzymeCentricSearchUrl(),
//                                query,
//                                ebeyeIndexUrl.getMaxEbiSearchLimit(),
//                                index * ebeyeIndexUrl.getMaxEbiSearchLimit()))
//                .distinct()
//                .collect(Collectors.toList());
//        return Collections.unmodifiableList(urls);
//        
//    }
//    
//    private List<String> asynchronousQuery(String query, int iteration, int limit) {
//        
//        if (AVAILABLE_PROCESSORS >= FJP_TRIGGER) {
//            
//            try {
//                return executeUsingFJP(AVAILABLE_PROCESSORS, query, iteration, limit).get();
//            } catch (InterruptedException | ExecutionException ex) {
//                logger.error(ex.getMessage(), ex);
//            }
//        }
//        
//        return searchEbeyeDomain(buildUrlsForAsyncRequest(query, iteration), limit);
//    }
//    
//    private CompletableFuture<List<String>> executeUsingFJP(int availableProcessors, String query, int iteration, int limit) {
//        final ForkJoinPool forkJoinPool = new ForkJoinPool(availableProcessors);
//        CompletableFuture<List<String>> completableFutureEntries = CompletableFuture.supplyAsync(()
//                -> searchEbeyeDomain(buildUrlsForAsyncRequest(query, iteration), limit),
//                forkJoinPool
//        );
//        return completableFutureEntries;
//    }
//    
//    private List<String> searchEbeyeDomain(List<String> urls, int limit) {
//        
//        HttpMethod method = HttpMethod.GET;
//
//        // Define response type
//        Class<EbeyeSearchResult> responseType = EbeyeSearchResult.class;
//
//        // Define headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        
//        HttpEntity<EbeyeSearchResult> requestEntity = new HttpEntity<>(headers);
//        
//        LazyReact lazyReact = new LazyReact();
//        
//        List<String> result = lazyReact
//                .fromStream(urls.stream()
//                        .map(url -> toCompletableFuture(asyncRestTemplate.exchange(url, method, requestEntity, responseType))))
//                .map(ResponseEntity<EbeyeSearchResult>::getBody)
//                .map(EbeyeSearchResult::getEntries)
//                .flatMap(Collection::stream).distinct()
//                .map(entry -> entry.getEc())
//                .limit(limit)
//                .collect(Collectors.toList());
//        try {
//            destroyHttpComponentsAsyncClientHttpRequestFactory();
//        } catch (Exception ex) {
//            logger.error(ex.getMessage(), ex);
//        }
//        return result;
//        
//    }
//    
//    protected void destroyHttpComponentsAsyncClientHttpRequestFactory() throws Exception {
//        //httpComponentsAsyncClientHttpRequestFactory.destroy();
//    }
//    
}
