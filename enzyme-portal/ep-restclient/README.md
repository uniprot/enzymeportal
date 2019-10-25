# ep-restclient

Rest client config for the Enzyme Portal web project.

## Built With

[Maven](https://maven.apache.org/)- Dependency Management


```bash
> mvn clean install
```

## Usage

Test use-cases to demonstrate how to use this module.
```java
import.*

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

    @Test
    public void injectedComponentsAreNotNull() {
        log.info("injectedComponentsAreNotNull");
        assertThat(webClient).isNotNull();
        assertThat(restConfigService).isNotNull();
        assertThat(enzymePortalRestTemplateCustomizer).isNotNull();
        assertThat(restErrorHandler).isNotNull();
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
                .expectBody().jsonPath("displayName", "ChREBP activates metabolic gene expression");
    }

}
```
## Authors

* Joseph Sampson [linkedin](https://www.linkedin.com/in/joseph-sampson-o-66399b30/)

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[Enzyme Portal](https://www.ebi.ac.uk/enzymeportal/) software released under the [Apache 2.0 license.](https://www.apache.org/licenses/LICENSE-2.0.html)

[MIT](https://choosealicense.com/licenses/mit/)