package uk.ac.ebi.ep.restapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.ac.ebi.ep.restapi.util.ApiProperties;

/**
 *
 * @author joseph
 */
@Configuration
public class SpringDocOpenApiConfig {

    @Autowired
    private ApiProperties apiProperties;

    @Bean
    public OpenAPI openAPI() {

        Contact contact = new Contact();
        contact.setName(apiProperties.getContactName());
        contact.setEmail(apiProperties.getContactEmail());

        License license = new License();
        license.setName(apiProperties.getLicenseName());
        license.setUrl(apiProperties.getLicenseUrl());

        Server server = new Server();
        server.setDescription(apiProperties.getServerDesc());
        server.setUrl(apiProperties.getServerUrl());

        return new OpenAPI()
                .info(new Info()
                        .title(apiProperties.getApiTitle())
                        .description(apiProperties.getApiDesc())
                        .version(apiProperties.getApiVersion())
                        .termsOfService(apiProperties.getApiTos())
                        .license(license)
                        .contact(contact))
                .servers(Arrays.asList(server));
    }


}
