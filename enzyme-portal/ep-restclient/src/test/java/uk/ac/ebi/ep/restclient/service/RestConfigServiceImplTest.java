package uk.ac.ebi.ep.restclient.service;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import uk.ac.ebi.ep.restclient.EpRestclientApplicationTests;
import uk.ac.ebi.ep.restclient.config.EnzymePortalRestTemplateCustomizer;
import uk.ac.ebi.ep.restclient.config.RestClientProperties;
import uk.ac.ebi.ep.restclient.config.RestErrorHandler;

/**
 *
 * @author joseph
 */
@Slf4j
public class RestConfigServiceImplTest extends EpRestclientApplicationTests {

    @Autowired
    private WebClient webClient;
    @Autowired
    private RestConfigService restConfigService;
    @Autowired
    private EnzymePortalRestTemplateCustomizer enzymePortalRestTemplateCustomizer;
    @Autowired
    private RestErrorHandler restErrorHandler;

    @Autowired
    private RestClientProperties restClientProperties;

    @Test
    public void injectedComponentsAreNotNull() {
        log.info("injectedComponentsAreNotNull");
        assertThat(webClient).isNotNull();
        assertThat(restConfigService).isNotNull();
        assertThat(enzymePortalRestTemplateCustomizer).isNotNull();
        assertThat(restErrorHandler).isNotNull();
        assertThat(restClientProperties).isNotNull();
    }

    /**
     * Test of getWebClient method, of class RestConfigServiceImpl.
     */
    @Test
    public void testGetWebClient() {
        log.info("testGetWebClient");
        assertThat(restConfigService.getWebClient()).isNotNull();
    }

    @Test
    public void testGetRestTemplate() {
        log.info("testGetRestTemplate");
        assertThat(restConfigService.getRestTemplate()).isNotNull();
    }

    @Test
    public void testGetRestClientPropertiesFields() {
        log.info("testGetRestClientPropertiesFields");
        assertThat(restClientProperties.getProxyHost()).isNotNull();
        assertThat(restClientProperties.getProxyPort()).isNotNull();
        assertThat(restClientProperties.isUseProxy()).isNotNull();

    }

    /**
     * Test of restHttpRequest method, of class RestConfigServiceImpl.
     */
    @Test
    public void testRestHttpRequest() {
        log.info("testRestHttpRequest");
        String data = restConfigService.restHttpRequest(String.class, "https://www.ebi.ac.uk/enzymeportal/status/json");
        assertThat(data).isEqualTo("UP");
    }

    @Test
    public void givenRestTemplate_whenRequested_thenLogAndModifyResponse() {
        log.info("givenRestTemplate_whenRequested_thenLogAndModifyResponse");
        ResponseEntity<String> responseEntity
                = restConfigService.getRestTemplate().getForEntity(
                        "http://httpbin.org/get", String.class
                );

        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getHeaders().getContentType())
                .isEqualTo(MediaType.APPLICATION_JSON);

    }
    @Disabled
    @Test
    public void testwebclient() {
        log.info("testwebclient");
        WebTestClient
                .bindToServer()
                .baseUrl("https://reactome.org")
                .build()
                .get()
                .uri("/ContentService/data/query/R-HSA-163765")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json;charset=UTF-8")
                .expectBody().jsonPath("displayName", "ChREBP activates metabolic gene expression").isNotEmpty();
    }
    @Disabled
    @Test
    public void testOkhttpClient() throws Exception {
        OkHttpClient client = enzymePortalRestTemplateCustomizer.okHttpClient();
        Request request = new Request.Builder()
                .url("https://www.computingfacts.com/")
                .build();

        try (Response response = client.newCall(request).execute()) {

            assertThat(response.isSuccessful()).isTrue();
     //       assertThat(response.protocol()).isEqualTo(Protocol.HTTP_1_1);
            assertThat(response.message()).isEqualTo("200");

        }
    }
}
