package uk.ac.ebi.ep.restclient.config;

import java.util.Collections;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author joseph
 */
public class EnzymePortalRestTemplateCustomizer implements RestTemplateCustomizer {

    private static final int TIME_OUT = 360;

    @Override
    public void customize(RestTemplate restTemplate) {

        restTemplate.setInterceptors(Collections.singletonList(new GenericClientHttpRequestInterceptor()));

        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(okHttpClientHttpRequestFactory()));

    }

    private ClientHttpRequestFactory okHttpClientHttpRequestFactory() {
        return new OkHttp3ClientHttpRequestFactory(okHttpClient());
    }

    private OkHttpClient okHttpClient() {

        return new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .build();
    }

}
