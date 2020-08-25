package uk.ac.ebi.ep.restapi.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.ac.ebi.ep.literatureservice.dto.LabelledCitation;
import uk.ac.ebi.ep.restapi.dto.EnzymeDisease;
import uk.ac.ebi.ep.restapi.dto.PdbInfo;
import uk.ac.ebi.ep.restapi.dto.SmallMolecule;
import uk.ac.ebi.ep.restapi.model.Protein;

/**
 *
 * @author joseph
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProteinControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Test of findProteinByAccession method, of class ProteinController.
     */
    @Test
    public void testFindProteinByAccession() {

        String accession = "P07327";
        String endpoint = "protein/" + accession;
        String url = "http://localhost:" + this.port + "/enzymeportal/rest/" + endpoint;

        Traverson traverson = new Traverson(URI.create(url), MediaTypes.HAL_JSON);

        Traverson.TraversalBuilder client = traverson.follow(IanaLinkRelations.SELF.value());

        EntityModel<Protein> proteinModel = client.toObject(EntityModel.class);
        assertNotNull(proteinModel);
        Protein protein = client.toObject(Protein.class);
        assertNotNull(protein);

        assertNotNull(proteinModel.getContent());
        assertEquals("P07327", protein.getAccession());
        assertEquals("Alcohol dehydrogenase 1A", protein.getProteinName());
        assertEquals("Homo sapiens", protein.getScientificName());
        assertEquals("Human", protein.getCommonName());

        assertThat(protein.getEcNumbers()).hasSizeGreaterThanOrEqualTo(1);
        assertThat(protein.getOtherNames()).hasSizeGreaterThanOrEqualTo(1);
        assertThat(protein.isExperimentalEvidence()).isTrue();

        assertTrue(proteinModel.hasLinks());
        assertTrue(proteinModel.getLinks().hasSize(7));
        assertTrue(proteinModel.hasLink(LinkRelation.of(IanaLinkRelations.SELF.value())));
        assertTrue(proteinModel.hasLink(LinkRelation.of("protein structure")));
        assertTrue(proteinModel.hasLink("reactions"));
        assertTrue(proteinModel.hasLink("pathways"));
        assertTrue(proteinModel.hasLink("small molecules"));
        assertTrue(proteinModel.hasLink("diseases"));
        assertTrue(proteinModel.hasLink("literature"));

    }

    /**
     * Test of findProteinStructureByAccession method, of class
     * ProteinController.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testFindProteinStructureByAccession() throws IOException {
        String requestUrl = "http://localhost:" + port + "/enzymeportal/rest/protein/P07327/proteinStructure";
        RequestEntity entity = new RequestEntity(HttpMethod.GET, URI.create(requestUrl));

        ResponseEntity<String> responseEntity = restTemplate.exchange(entity, String.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseEntity.getBody());

        List<String> pdbAccessions = TestUtil.getValuesFromJsonData(responseEntity.getBody(), "pdbAccession");

        assertThat(pdbAccessions).containsAnyOf("1u3t", "1hso");

        Gson gson = TestUtil.getGson();

        List<PdbInfo> result = gson.fromJson(responseEntity.getBody(), List.class);
        assertNotNull(result);
        assertThat(result).hasSizeGreaterThanOrEqualTo(2);

    }

    /**
     * Test of findReactionsByAccession method, of class ProteinController.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testFindReactionsByAccession() throws IOException {
        String requestUrl = "http://localhost:" + port + "/enzymeportal/rest/protein/P07327/reaction?limit=10";
        RequestEntity entity = new RequestEntity(HttpMethod.GET, URI.create(requestUrl));

        ResponseEntity<String> responseEntity = restTemplate.exchange(entity, String.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseEntity.getBody());

        List<JsonNode> activities = TestUtil.getJsonNodeFromJsonData(responseEntity.getBody(), "catalyticActivities");

        List<JsonNode> reactions = TestUtil.getJsonNodeFromJsonData(responseEntity.getBody(), "rheaReactions");

        assertThat(activities).hasSizeGreaterThanOrEqualTo(1);
        assertThat(reactions).hasSizeGreaterThanOrEqualTo(1);

    }

    /**
     * Test of findPathwaysByAccession method, of class ProteinController.
     */
    @Test
    public void testFindPathwaysByAccession() throws IOException {
        String requestUrl = "http://localhost:" + port + "/enzymeportal/rest/protein/P07327/pathways";
        RequestEntity entity = new RequestEntity(HttpMethod.GET, URI.create(requestUrl));

        ResponseEntity<String> responseEntity = restTemplate.exchange(entity, String.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseEntity.getBody());

        List<JsonNode> id = TestUtil.getJsonNodeFromJsonData(responseEntity.getBody(), "id");
        List<String> name = TestUtil.getValuesFromJsonData(responseEntity.getBody(), "name");

        assertThat(name).hasSizeGreaterThanOrEqualTo(1);
        assertThat(id).hasSizeGreaterThanOrEqualTo(1);
        assertThat(name).containsAnyOf("RA biosynthesis pathway", "Ethanol oxidation", "Abacavir metabolism");

    }

    /**
     * Test of findSmallmoleculesByAccession method, of class ProteinController.
     */
    @Test
    public void testFindSmallmoleculesByAccession() {
        String requestUrl = "http://localhost:" + port + "/enzymeportal/rest/protein/P07327/smallmolecules";
        RequestEntity entity = new RequestEntity(HttpMethod.GET, URI.create(requestUrl));

        ResponseEntity<String> responseEntity = restTemplate.exchange(entity, String.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseEntity.getBody());

        Gson gson = TestUtil.getGson();

        SmallMolecule result = gson.fromJson(responseEntity.getBody(), SmallMolecule.class);

        assertNotNull(result);
        assertNotNull(result.getInhibitors());
        assertNotNull(result.getCofactors());
        assertThat(result.getCofactors()).hasSizeGreaterThanOrEqualTo(1);
        assertThat(result.getInhibitors()).hasSizeGreaterThanOrEqualTo(1);
        assertThat(result.getActivators()).hasSize(0);
    }

    /**
     * Test of findDiseasesByAccession method, of class ProteinController.
     */
    @Test
    public void testFindDiseasesByAccession() {
        String requestUrl = "http://localhost:" + port + "/enzymeportal/rest/protein/O15297/diseases";
        RequestEntity entity = new RequestEntity(HttpMethod.GET, URI.create(requestUrl));

        ResponseEntity<String> responseEntity = restTemplate.exchange(entity, String.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseEntity.getBody());

        Gson gson = TestUtil.getGson();

        List<EnzymeDisease> result = gson.fromJson(responseEntity.getBody(), List.class);

        assertNotNull(result);

        assertThat(result).hasSizeGreaterThanOrEqualTo(2);
    }

    /**
     * Test of findCitationsByAccession method, of class ProteinController.
     */
    @Test
    public void testFindCitationsByAccession() {
        String requestUrl = "http://localhost:" + port + "/enzymeportal/rest/protein/P07327/citation?limit=2";
        RequestEntity entity = new RequestEntity(HttpMethod.GET, URI.create(requestUrl));

        ResponseEntity<String> responseEntity = restTemplate.exchange(entity, String.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseEntity.getBody());

        Gson gson = TestUtil.getGson();

        List<LabelledCitation> result = gson.fromJson(responseEntity.getBody(), List.class);
        assertNotNull(result);
        assertThat(result).hasSizeGreaterThanOrEqualTo(2);
    }

}
