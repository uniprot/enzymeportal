package uk.ac.ebi.ep.restapi.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.net.URI;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import static org.springframework.hateoas.client.Hop.rel;
import org.springframework.hateoas.client.Traverson;
import org.springframework.hateoas.server.core.TypeReferences;
import org.springframework.hateoas.server.core.TypeReferences.CollectionModelType;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import uk.ac.ebi.ep.literatureservice.model.Result;
import uk.ac.ebi.ep.reaction.mechanism.model.MechanismResult;
import uk.ac.ebi.ep.restapi.dto.BrendaParameter;
import uk.ac.ebi.ep.restapi.model.EnzymeModel;
import uk.ac.ebi.ep.restapi.model.ProteinModel;

/**
 *
 * @author joseph
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EnzymeControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private Traverson getTraversonClient(String endpoint) {
        String url = "http://localhost:" + this.port + "/enzymeportal/rest/" + endpoint;
        return new Traverson(URI.create(url), MediaTypes.HAL_JSON);
    }

    /**
     * Test of enzymes method, of class EnzymeController.
     */
    @Test
    public void testEnzymes() {
        String endPoint = "enzymes/";
        Traverson traverson = getTraversonClient(endPoint);
        Traverson.TraversalBuilder client = traverson.follow(IanaLinkRelations.SELF.value());

        PagedModel<EnzymeModel> enzymes = client
                .toObject(new TypeReferences.PagedModelType<EnzymeModel>() {
                });

        assertNotNull(enzymes);
        assertNotNull(enzymes.getContent());
        assertTrue(enzymes.hasLinks());

        assertTrue(enzymes.hasLink(IanaLinkRelations.FIRST));
        assertTrue(enzymes.hasLink(IanaLinkRelations.SELF));
        assertTrue(enzymes.hasLink(IanaLinkRelations.NEXT));
        assertTrue(enzymes.hasLink(IanaLinkRelations.LAST));

        assertNotNull(enzymes.getMetadata());
        assertTrue(enzymes.getMetadata().getNumber() == 0);
        assertTrue(enzymes.getMetadata().getTotalPages() > 100);
        assertTrue(enzymes.getMetadata().getTotalElements() > 1_000);
        assertTrue(enzymes.getMetadata().getSize() == 10);

        List<String> enzymeNames = client.toObject("$._embedded.enzymes.[*].enzymeName");
        assertThat(enzymeNames).hasSizeGreaterThanOrEqualTo(10);

        List<String> associatedProteinsAccession = client.toObject("$._embedded.enzymes.[0].associatedProteins._embedded.proteins[*].accession");

        assertThat(associatedProteinsAccession).hasSizeGreaterThanOrEqualTo(2);

    }

    /**
     * Test of findEnzymeByEcNumber method, of class EnzymeController.
     */
     @Test
    public void testFindEnzymeByEcNumber() {
        String ec = "1.1.1.1";
        String endPoint = "enzymes/" + ec;
        Traverson traverson = getTraversonClient(endPoint);

        Traverson.TraversalBuilder client = traverson.follow(IanaLinkRelations.SELF.value());

        EnzymeModel enzymeModel = client.toObject(EnzymeModel.class);

        assertNotNull(enzymeModel);
        assertNotNull(enzymeModel.getEnzymeName());
        assertEquals("1.1.1.1", enzymeModel.getEcNumber());
        assertEquals("Alcohol dehydrogenase", enzymeModel.getEnzymeName());
        assertEquals("Oxidoreductases", enzymeModel.getEnzymeFamily());
        assertThat(enzymeModel.getCatalyticActivities()).hasSizeGreaterThanOrEqualTo(1);
        assertThat(enzymeModel.getAssociatedProteins()).hasSizeGreaterThanOrEqualTo(2);
        assertThat(enzymeModel.getAlternativeNames()).containsAnyOf("Aldehyde reductase.");

        assertTrue(enzymeModel.hasLinks());
        assertTrue(enzymeModel.getLinks().hasSize(3));
        assertTrue(enzymeModel.hasLink(LinkRelation.of(IanaLinkRelations.SELF.value())));
        assertTrue(enzymeModel.hasLink(LinkRelation.of("enzymes")));
        assertTrue(enzymeModel.hasLink("associated Proteins"));

        String proteinsHref = "/enzymes/{ec}/proteins{?limit}";
        Link proteinLink = Link.of(proteinsHref);
        assertThat(proteinLink.isTemplated()).isTrue();
        assertThat(proteinLink.getVariableNames()).contains("ec", "limit");
        CollectionModel<ProteinModel> proteins = traverson.follow("associated Proteins")
                .toObject(new TypeReferences.CollectionModelType<ProteinModel>() {
                });

        assertNotNull(proteins);
        assertNotNull(proteins.getContent());
        assertTrue(proteins.hasLinks());
        assertTrue(proteins.hasLink(IanaLinkRelations.SELF));

    }

    /**
     * Test of findCitationByEcNumber method, of class EnzymeController.
     */
    @Test
    public void testFindCitationByEcNumber() {

        String requestUrl = "http://localhost:" + port + "/enzymeportal/rest/enzymes/1.1.1.1/citations?limit=2";
        RequestEntity entity = new RequestEntity(HttpMethod.GET, URI.create(requestUrl));

        ResponseEntity<String> responseEntity = restTemplate.exchange(entity, String.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseEntity.getBody());

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        List<Result> result = gson.fromJson(responseEntity.getBody(), List.class);

        assertNotNull(result);
        assertThat(result).hasSize(2);

    }

    /**
     * Test of findAssociatedProteinsByEcNumber method, of class
     * EnzymeController.
     */
    @Test
    public void testFindAssociatedProteinsByEcNumber() {

        String ec = "1.1.1.1";
        int limit = 10;
        String endPoint = "enzymes/" + ec + "/proteins?limit=" + limit;
        Traverson traverson = getTraversonClient(endPoint);

        Traverson.TraversalBuilder client = traverson.follow(IanaLinkRelations.SELF.value());

        CollectionModelType<ProteinModel> collectionModelType = new TypeReferences.CollectionModelType<ProteinModel>() {
        };

        CollectionModel<ProteinModel> proteinModel = traverson.
                follow(rel("self")).
                toObject(collectionModelType);

        assertTrue(proteinModel.hasLink(IanaLinkRelations.SELF));

        List<String> accessions = client.toObject("$._embedded.proteinSummaries.[*].accession");

        assertThat(accessions).hasSizeGreaterThanOrEqualTo(2);
    }

    /**
     * Test of findKineticParametersByEcNumber method, of class
     * EnzymeController.
     */
    @Test
    public void testFindKineticParametersByEcNumber() {
        String requestUrl = "http://localhost:" + port + "/enzymeportal/rest/enzymes/1.1.1.1/kineticParameters?limit=2";
        RequestEntity entity = new RequestEntity(HttpMethod.GET, URI.create(requestUrl));

        ResponseEntity<String> responseEntity = restTemplate.exchange(entity, String.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseEntity.getBody());

        Gson gson = TestUtil.getGson();

        BrendaParameter result = gson.fromJson(responseEntity.getBody(), BrendaParameter.class);
        assertNotNull(result);

    }

    /**
     * Test of findMechanismsByEcNumber method, of class EnzymeController.
     */
    @Test
    public void testFindMechanismsByEcNumber() {

        String requestUrl = "http://localhost:" + port + "/enzymeportal/rest/enzymes/1.1.1.1/mechanisms";
        RequestEntity entity = new RequestEntity(HttpMethod.GET, URI.create(requestUrl));

        ResponseEntity<String> responseEntity = restTemplate.exchange(entity, String.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseEntity.getBody());

        Gson gson = TestUtil.getGson();

        MechanismResult result = gson.fromJson(responseEntity.getBody(), MechanismResult.class);
        assertNotNull(result);
        assertNotNull(result.getResults());
        assertThat(result.getResults()).hasSizeGreaterThanOrEqualTo(5);
    }


}
