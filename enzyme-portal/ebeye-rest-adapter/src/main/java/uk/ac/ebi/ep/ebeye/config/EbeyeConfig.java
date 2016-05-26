package uk.ac.ebi.ep.ebeye.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.ebeye.EbeyeProteinService;
import uk.ac.ebi.ep.ebeye.EbeyeRestService;
import uk.ac.ebi.ep.ebeye.EnzymeCentricService;
import uk.ac.ebi.ep.ebeye.ModelService;
import uk.ac.ebi.ep.ebeye.PowerService;

/**
 * Configures the services that interface with the Ebeye search.
 *
 * @author joseph
 */
@Configuration
@PropertySource({"classpath:ebeye.es"})
public class EbeyeConfig {

    private final int requestTimeout = 2500;
    private final int HTTP_CLIENT_MAX_CONNECTION_TOTAL = 500;
    private final int HTTP_CLIENT_MAX_CONNECTION_PER_ROUTE = 500;

    @Autowired
    private Environment env;

    @Bean
    public EbeyeRestService ebeyeRestService(EbeyeIndexUrl ebeyeIndexUrl, RestTemplate restTemplate) {
        return new EbeyeRestService(ebeyeIndexUrl, restTemplate);
    }

    @Bean
    public EbeyeProteinService ebeyeProteinService(EbeyeIndexUrl ebeyeIndexUrl, RestTemplate restTemplate) {
        return new EbeyeProteinService(ebeyeIndexUrl, restTemplate);
    }

    @Bean
    public PowerService powerService(EbeyeIndexUrl ebeyeIndexUrl,AsyncRestTemplate asyncRestTemplate, RestTemplate restTemplate ){
        return new PowerService(ebeyeIndexUrl, asyncRestTemplate, restTemplate);
    }

    @Bean
    public ModelService modelService(RestTemplate restTemplate) {
        return new ModelService(restTemplate);
    }

    @Bean
    public AsyncRestTemplate chunkBasedAsyncRestTemplate() {
        //return new AsyncRestTemplate(asyncClientHttpRequestFactory());
        return new AsyncRestTemplate();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(clientHttpRequestFactory());
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(requestTimeout);
        factory.setConnectTimeout(requestTimeout);

        return factory;
    }

//    private AsyncClientHttpRequestFactory asyncClientHttpRequestFactory() {
//        return httpComponentsAsyncClientHttpRequestFactory();
//    }
//
//    @Bean
//    public HttpComponentsAsyncClientHttpRequestFactory httpComponentsAsyncClientHttpRequestFactory() {
//        CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom()
//                .setMaxConnTotal(HTTP_CLIENT_MAX_CONNECTION_TOTAL)
//                .setMaxConnPerRoute(HTTP_CLIENT_MAX_CONNECTION_PER_ROUTE)
//                .build();
//
//        HttpComponentsAsyncClientHttpRequestFactory factory = new HttpComponentsAsyncClientHttpRequestFactory();
//        factory.setReadTimeout(requestTimeout);
//        factory.setConnectTimeout(requestTimeout);
//        factory.setHttpAsyncClient(httpclient);
//        return factory;
//
//    }

    //TODO
    private AsyncListenableTaskExecutor asyncListenableTaskExecutor() {
        ExecutorService executor = Executors.newWorkStealingPool();
        AsyncListenableTaskExecutor asyncListenableTaskExecutor = new TaskExecutorAdapter(executor);
        return asyncListenableTaskExecutor;
    }

    @Bean
    public EbeyeIndexUrl ebeyeIndexUrl() {
        String defaultSearchUrl = env.getProperty("ep.default.search.url");

        int maxEbiRequests = Integer.parseInt(env.getProperty("ebeye.max.ebi.requests"));
        int chunkSize = Integer.parseInt(env.getProperty("ebeye.chunk.size"));
        String enzymeCentriUrl = env.getProperty("ep.enzyme.centric.search.url");

        EbeyeIndexUrl url = new EbeyeIndexUrl();
        url.setDefaultSearchIndexUrl(defaultSearchUrl);
        url.setEnzymeCentricSearchUrl(enzymeCentriUrl);
        url.setChunkSize(chunkSize);
        url.setMaxEbiSearchLimit(maxEbiRequests);
        return url;
    }

    @Bean
    public EnzymeCentricService enzymeCentricService(EbeyeIndexUrl ebeyeIndexUrl, RestTemplate restTemplate,
            AsyncRestTemplate asyncRestTemplate) {
        return new EnzymeCentricService(ebeyeIndexUrl, restTemplate, asyncRestTemplate);
    }
}
