package uk.ac.ebi.ep.restclient.config;

import io.netty.channel.ChannelOption;
import java.net.InetSocketAddress;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.ProxyProvider;

/**
 *
 * @author joseph
 */
@Slf4j
@Configuration
public class RestClientConfig {

    @Autowired
    private RestClientProperties restClientProperties;

    @Bean
    public EnzymePortalRestTemplateCustomizer enzymePortalRestTemplateCustomizer() {
        return new EnzymePortalRestTemplateCustomizer();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder, RestErrorHandler errorHandler) {
        return restTemplateBuilder
                .errorHandler(errorHandler)
                .setConnectTimeout(Duration.ofSeconds(30))
                .setReadTimeout(Duration.ofSeconds(30))
                .requestFactory(this::getClientHttpRequestFactory)
                .build();
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        int timeout = 30_000;
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
                = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(timeout);
        clientHttpRequestFactory.setConnectionRequestTimeout(timeout);
        clientHttpRequestFactory.setReadTimeout(timeout);
        return clientHttpRequestFactory;
    }

    @Bean
    @DependsOn(value = {"enzymePortalRestTemplateCustomizer"})
    public RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder(enzymePortalRestTemplateCustomizer());
    }

    @Bean
    public WebClient webClient() {

        HttpClient httpClient = httpClient();

        if (restClientProperties.isUseProxy()) {
            httpClient = proxyHttpClient();
        }

        return WebClient
                .builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                                .exchangeStrategies(ExchangeStrategies.builder()
                                        .codecs(configurer -> configurer
                                        .defaultCodecs()
                                        .maxInMemorySize(16 * 1024 * 1024))//16MB
                                        .build())
                .build();

    }

    private HttpClient proxyHttpClient() {
        return HttpClient.create()
                .compress(true)
                .tcpConfiguration(client -> client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30_000)
                .proxy(addressSpec -> addressSpec.type(ProxyProvider.Proxy.HTTP)
                .address(InetSocketAddress
                        .createUnresolved(restClientProperties.getProxyHost(), restClientProperties.getProxyPort())))
                );
    }

    private HttpClient httpClient() {
        return HttpClient.create()
                .compress(true)
                .tcpConfiguration(client -> client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30_000));

    }

    @Bean
    public ReactorResourceFactory reactorResourceFactory() {
        ReactorResourceFactory factory = new ReactorResourceFactory();
        factory.setUseGlobalResources(true);
        return factory;
    }

}
