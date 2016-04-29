package uk.ac.ebi.ep.ebeye.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.http.client.AsyncClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.ebeye.EbeyeRestService;
import uk.ac.ebi.ep.ebeye.EnzymeCentricService;

/**
 * Configures the services that interface with the Ebeye search.
 *
 * @author joseph
 */
@Configuration
@PropertySource({"classpath:ebeye.es"})
public class EbeyeConfig {

    //@Value("${request.timeout.millis:5000}") 
    private final int requestTimeout = 0b111110100;

    @Autowired
    private Environment env;

    @Bean
    static PropertySourcesPlaceholderConfigurer propertyPlaceHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public EbeyeRestService ebeyeRestService(EbeyeIndexUrl ebeyeIndexUrl, RestTemplate restTemplate,
            AsyncRestTemplate asyncRestTemplate) {
        return new EbeyeRestService(ebeyeIndexUrl, restTemplate, asyncRestTemplate);
    }

    /**
     * 
     * TODO - benchmark performance and side effects against WorkStealingPool 
     */
    //private final AsyncListenableTaskExecutor executor = new TaskExecutorAdapter(Runnable::run);

    private AsyncListenableTaskExecutor constructAsyncListenableTaskExecutor() {
        ExecutorService executor = Executors.newWorkStealingPool();
        AsyncListenableTaskExecutor asyncListenableTaskExecutor = new TaskExecutorAdapter(executor);
        return asyncListenableTaskExecutor;
    }

    @Bean
    public AsyncRestTemplate asyncRestTemplate() {
        return new AsyncRestTemplate(constructAsyncListenableTaskExecutor());
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

    private AsyncClientHttpRequestFactory asyncClientHttpRequestFactory() {
        HttpComponentsAsyncClientHttpRequestFactory factory = new HttpComponentsAsyncClientHttpRequestFactory();
        factory.setReadTimeout(requestTimeout);
        factory.setConnectTimeout(requestTimeout);

        return factory;
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
