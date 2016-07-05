package uk.ac.ebi.ep.ebeye.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.ebeye.AccessionService;
import uk.ac.ebi.ep.ebeye.EbeyeProteinService;
import uk.ac.ebi.ep.ebeye.EbeyeQueryService;
import uk.ac.ebi.ep.ebeye.EbeyeQueryServiceImpl;
import uk.ac.ebi.ep.ebeye.EbeyeRestService;
import uk.ac.ebi.ep.ebeye.EbeyeSuggestionService;
import uk.ac.ebi.ep.ebeye.EnzymeCentricService;
import uk.ac.ebi.ep.ebeye.ModelService;
import uk.ac.ebi.ep.ebeye.PowerService;
import uk.ac.ebi.ep.ebeye.ProteinCentricService;

/**
 * Configures the services that interface with the Ebeye search.
 *
 * @author joseph
 */
@Configuration
@PropertySource({"classpath:ebeye.es"})
public class EbeyeConfig {

    private static final int REQUEST_TIMEOUT_MILLIS = 2500;

    @Autowired
    private Environment env;

    //delete later
    @Bean
    public AsyncRestTemplate asyncRestTemplate() {
        //return new AsyncRestTemplate(asyncClientHttpRequestFactory());
        return new AsyncRestTemplate();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(clientHttpRequestFactory());
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(REQUEST_TIMEOUT_MILLIS);
        factory.setConnectTimeout(REQUEST_TIMEOUT_MILLIS);

        return factory;
    }

    @Bean
    public EbeyeRestService ebeyeRestService(EbeyeQueryService proteinQueryService) {
        return new EbeyeRestService(proteinQueryService);
    }

    //Configure protein centric services
    @Bean
    public EbeyeQueryService proteinQueryService(EbeyeIndexProps proteinCentricProps, RestTemplate restTemplate) {
        return new EbeyeQueryServiceImpl(proteinCentricProps, restTemplate);
    }
//
//    @Bean(name="proteinCentricProps")
//    public EbeyeIndexProps proteinCentricProps() {
//        int maxEbiRequests = Integer.parseInt(env.getProperty("ebeye.max.ebi.requests"));
//        int chunkSize = Integer.parseInt(env.getProperty("ebeye.chunk.size"));
//        String defaultSearchUrl = env.getProperty("ep.default.search.url");
//
//        EbeyeIndexProps url = new EbeyeIndexProps();
//        //url.setEbeyeSearchUrl(defaultSearchUrl);
//        url.setDefaultSearchIndexUrl(defaultSearchUrl);
//        url.setChunkSize(chunkSize);
//        url.setMaxEbiSearchLimit(maxEbiRequests);
//        return url;
//    }

    //EbeyeIndexProps ebeyeIndexUrl, RestTemplate restTemplate, AsyncRestTemplate asyncRestTemplate
    @Bean
    public EnzymeCentricService enzymeCentricService(RestTemplate restTemplate, EbeyeIndexProps enzymeCentricProps) {
        return new EnzymeCentricService(restTemplate, enzymeCentricProps);
    }

    @Bean
    public EbeyeQueryService enzymeQueryService(EbeyeIndexProps enzymeCentricProps, RestTemplate restTemplate) {
        return new EbeyeQueryServiceImpl(enzymeCentricProps, restTemplate);
    }

    @Bean
    public EbeyeIndexProps ebeyeIndexProps() {
        int maxEbiRequests = Integer.parseInt(env.getProperty("ebeye.max.ebi.requests"));
        int chunkSize = Integer.parseInt(env.getProperty("ebeye.chunk.size"));
        String enzymeCentricUrl = env.getProperty("ep.enzyme.centric.search.url");
        String proteinCentricUrl = env.getProperty("ep.protein.centric.search.url");

        EbeyeIndexProps url = new EbeyeIndexProps();
        //url.setEbeyeSearchUrl(enzymeCentricUrl);
        //url.setDefaultSearchIndexUrl(enzymeCentricUrl);

        url.setProteinCentricSearchUrl(proteinCentricUrl);
        url.setEnzymeCentricSearchUrl(enzymeCentricUrl);
        url.setChunkSize(chunkSize);
        url.setMaxEbiSearchLimit(maxEbiRequests);

        return url;
    }

    @Bean
    public EbeyeSuggestionService ebeyeSuggestionService(EbeyeIndexProps proteinCentricProps, RestTemplate restTemplate) {
        return new EbeyeSuggestionService(proteinCentricProps, restTemplate);
    }

    @Bean
    public EbeyeProteinService ebeyeProteinService(EbeyeIndexProps ebeyeIndexUrl, RestTemplate restTemplate) {
        return new EbeyeProteinService(ebeyeIndexUrl, restTemplate);
    }

    @Bean
    public ProteinCentricService proteinCentricService(EbeyeIndexProps ebeyeIndexUrl, AsyncRestTemplate asyncRestTemplate, RestTemplate restTemplate) {
        return new ProteinCentricService(ebeyeIndexUrl, asyncRestTemplate, restTemplate);
    }

    //temporal services
    @Bean
    public PowerService powerService(EbeyeIndexProps ebeyeIndexUrl, AsyncRestTemplate asyncRestTemplate, RestTemplate restTemplate) {
        return new PowerService(ebeyeIndexUrl, asyncRestTemplate, restTemplate);
    }

    @Bean
    public ModelService modelService(RestTemplate restTemplate) {
        return new ModelService(restTemplate);
    }

    @Bean
    public AccessionService accessionService(EbeyeIndexProps ebeyeIndexUrl, AsyncRestTemplate asyncRestTemplate, RestTemplate restTemplate) {
        return new AccessionService(ebeyeIndexUrl, asyncRestTemplate, restTemplate);
    }

//
//    private final int requestTimeout = 2500;
//    private final int HTTP_CLIENT_MAX_CONNECTION_TOTAL = 500;
//    private final int HTTP_CLIENT_MAX_CONNECTION_PER_ROUTE = 500;
//
//    @Autowired
//    private Environment env;
//
//    @Bean
//    public EbeyeRestService ebeyeRestService(EbeyeIndexProps ebeyeIndexUrl, RestTemplate restTemplate) {
//        return new EbeyeRestService(ebeyeIndexUrl, restTemplate);
//    }
//
//    @Bean
//    public EbeyeProteinService ebeyeProteinService(EbeyeIndexProps ebeyeIndexUrl, RestTemplate restTemplate) {
//        return new EbeyeProteinService(ebeyeIndexUrl, restTemplate);
//    }
//
//    @Bean
//    public PowerService powerService(EbeyeIndexProps ebeyeIndexUrl, AsyncRestTemplate asyncRestTemplate, RestTemplate restTemplate) {
//        return new PowerService(ebeyeIndexUrl, asyncRestTemplate, restTemplate);
//    }
//
//    @Bean
//    public ModelService modelService(RestTemplate restTemplate) {
//        return new ModelService(restTemplate);
//    }
//
//    @Bean
//    public AccessionService accessionService(EbeyeIndexProps ebeyeIndexUrl, AsyncRestTemplate asyncRestTemplate, RestTemplate restTemplate) {
//        return new AccessionService(ebeyeIndexUrl, asyncRestTemplate, restTemplate);
//    }
//
//    @Bean
//    public AsyncRestTemplate chunkBasedAsyncRestTemplate() {
//        //return new AsyncRestTemplate(asyncClientHttpRequestFactory());
//        return new AsyncRestTemplate();
//    }
//
//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate(clientHttpRequestFactory());
//    }
//
//    private ClientHttpRequestFactory clientHttpRequestFactory() {
//        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
//        factory.setReadTimeout(requestTimeout);
//        factory.setConnectTimeout(requestTimeout);
//
//        return factory;
//    }
//
////    private AsyncClientHttpRequestFactory asyncClientHttpRequestFactory() {
////        return httpComponentsAsyncClientHttpRequestFactory();
////    }
////
////    @Bean
////    public HttpComponentsAsyncClientHttpRequestFactory httpComponentsAsyncClientHttpRequestFactory() {
////        CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom()
////                .setMaxConnTotal(HTTP_CLIENT_MAX_CONNECTION_TOTAL)
////                .setMaxConnPerRoute(HTTP_CLIENT_MAX_CONNECTION_PER_ROUTE)
////                .build();
////
////        HttpComponentsAsyncClientHttpRequestFactory factory = new HttpComponentsAsyncClientHttpRequestFactory();
////        factory.setReadTimeout(requestTimeout);
////        factory.setConnectTimeout(requestTimeout);
////        factory.setHttpAsyncClient(httpclient);
////        return factory;
////
////    }
//    //TODO
//    private AsyncListenableTaskExecutor asyncListenableTaskExecutor() {
//        ExecutorService executor = Executors.newWorkStealingPool();
//        AsyncListenableTaskExecutor asyncListenableTaskExecutor = new TaskExecutorAdapter(executor);
//        return asyncListenableTaskExecutor;
//    }
//
//    @Bean
//    public EbeyeIndexProps ebeyeIndexUrl() {
//        String defaultSearchUrl = env.getProperty("ep.default.search.url");
//
//        int maxEbiRequests = Integer.parseInt(env.getProperty("ebeye.max.ebi.requests"));
//        int chunkSize = Integer.parseInt(env.getProperty("ebeye.chunk.size"));
//        String enzymeCentriUrl = env.getProperty("ep.enzyme.centric.search.url");
//
//        EbeyeIndexProps url = new EbeyeIndexProps();
//        url.setDefaultSearchIndexUrl(defaultSearchUrl);
//        url.setEnzymeCentricSearchUrl(enzymeCentriUrl);
//        url.setChunkSize(chunkSize);
//        url.setMaxEbiSearchLimit(maxEbiRequests);
//        return url;
//    }
//
//    @Bean
//    public EnzymeCentricService enzymeCentricService(EbeyeIndexProps ebeyeIndexUrl, RestTemplate restTemplate,
//            AsyncRestTemplate asyncRestTemplate) {
//        return new EnzymeCentricService(ebeyeIndexUrl, restTemplate, asyncRestTemplate);
//    }
}
