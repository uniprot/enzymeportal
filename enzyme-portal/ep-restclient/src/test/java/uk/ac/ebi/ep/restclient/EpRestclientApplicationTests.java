package uk.ac.ebi.ep.restclient;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;
import uk.ac.ebi.ep.restclient.config.EnzymePortalRestTemplateCustomizer;
import uk.ac.ebi.ep.restclient.config.RestErrorHandler;
import uk.ac.ebi.ep.restclient.service.RestConfigService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EpRestclientApplicationTests {

    @Autowired
    private WebClient webClient;
    @Autowired
    private RestConfigService restConfigService;
    @Autowired
    private EnzymePortalRestTemplateCustomizer enzymePortalRestTemplateCustomizer;
    @Autowired
    private RestErrorHandler restErrorHandler;

    @Test
    public void contextLoads() {
        assertThat(webClient).isNotNull();
        assertThat(restConfigService).isNotNull();
        assertThat(enzymePortalRestTemplateCustomizer).isNotNull();
        assertThat(restErrorHandler).isNotNull();
    }

}
