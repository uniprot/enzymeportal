package uk.ac.ebi.ep.restapi.controller;

import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author joseph
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RootControllerTest {

    @LocalServerPort
    private int port;
    @Autowired
    protected MockMvc mockMvc;

    @Test
    public void shouldDisplaySwaggerUiPage() throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertTrue(contentAsString.contains("Swagger UI"));
    }

    @Test
    public void shouldRedirectToSwaggerUiPage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        assertEquals("/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config", mvcResult.getResponse().getRedirectedUrl());
    }

    @Test
    public void testApiIndexPageLinks() throws Exception {
        String url = "http://localhost:" + this.port + "/enzymeportal/rest/";

        Traverson traverson = new Traverson(URI.create(url), MediaTypes.HAL_JSON);

        Link selfLink = traverson
                .follow(IanaLinkRelations.SELF.value())
                .asLink();

        assertEquals("self", selfLink.getRel().value());

        Link cofactorLink = traverson
                .follow("Cofactors")
                .asLink();
        assertEquals("Cofactors", cofactorLink.getRel().value());

        Link diseasesLink = traverson
                .follow("Diseases")
                .asLink();
        assertEquals("Diseases", diseasesLink.getRel().value());

        Link metabolitesLink = traverson
                .follow("Metabolites")
                .asLink();
        assertEquals("Metabolites", metabolitesLink.getRel().value());

        Link pathwaysLink = traverson
                .follow("Pathways")
                .asLink();
        assertEquals("Pathways", pathwaysLink.getRel().value());

        Link accessionLink = traverson
                .follow("Search By Uniprot Accession")
                .asLink();
        assertEquals("Search By Uniprot Accession", accessionLink.getRel().value());

    }

}
