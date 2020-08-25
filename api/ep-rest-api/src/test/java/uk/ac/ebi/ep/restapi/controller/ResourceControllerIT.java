package uk.ac.ebi.ep.restapi.controller;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 *
 * @author joseph
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ResourceControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Test of getCofactors method, of class ResourceController.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testGetCofactors() throws IOException {

        String requestUrl = "http://localhost:" + port + "/enzymeportal/rest/resource/cofactors?pageSize=3";
        RequestEntity entity = new RequestEntity(HttpMethod.GET, URI.create(requestUrl));

        ResponseEntity<String> responseEntity = restTemplate.exchange(entity, String.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseEntity.getBody());

        List<JsonNode> id = TestUtil.getJsonNodeFromJsonData(responseEntity.getBody(), "cofactorId");
        List<String> name = TestUtil.getValuesFromJsonData(responseEntity.getBody(), "cofactorName");
        List<String> pageSize = TestUtil.getValuesFromJsonData(responseEntity.getBody(), "pageSize");

        assertThat(name).hasSizeGreaterThanOrEqualTo(1);
        assertThat(id).hasSizeGreaterThanOrEqualTo(1);
        assertThat(Integer.parseInt(pageSize.get(0)) == 3);
        assertThat(name).containsAnyOf("(6R)-5,10-methylene-5,6,7,8-tetrahydrofolate", "(6R)-L-erythro-5,6,7,8-tetrahydrobiopterin", "(6S)-5,6,7,8-tetrahydrofolate");

    }

    /**
     * Test of getDiseases method, of class ResourceController.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testGetDiseases() throws IOException {
        String requestUrl = "http://localhost:" + port + "/enzymeportal/rest/resource/diseases?pageSize=4";
        RequestEntity entity = new RequestEntity(HttpMethod.GET, URI.create(requestUrl));

        ResponseEntity<String> responseEntity = restTemplate.exchange(entity, String.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseEntity.getBody());

        List<JsonNode> id = TestUtil.getJsonNodeFromJsonData(responseEntity.getBody(), "omimNumber");
        List<String> name = TestUtil.getValuesFromJsonData(responseEntity.getBody(), "diseaseName");
        List<String> pageSize = TestUtil.getValuesFromJsonData(responseEntity.getBody(), "pageSize");

        assertThat(name).hasSizeGreaterThanOrEqualTo(4);
        assertThat(id).hasSizeGreaterThanOrEqualTo(4);
        assertThat(Integer.parseInt(pageSize.get(0)) == 4);

    }

    /**
     * Test of getMetabolites method, of class ResourceController.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testGetPathways() throws IOException {
        String requestUrl = "http://localhost:" + port + "/enzymeportal/rest/resource/pathways?pageSize=4";
        RequestEntity entity = new RequestEntity(HttpMethod.GET, URI.create(requestUrl));

        ResponseEntity<String> responseEntity = restTemplate.exchange(entity, String.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseEntity.getBody());

        List<JsonNode> id = TestUtil.getJsonNodeFromJsonData(responseEntity.getBody(), "pathwayGroupId");
        List<String> name = TestUtil.getValuesFromJsonData(responseEntity.getBody(), "pathwayName");
        List<String> pageSize = TestUtil.getValuesFromJsonData(responseEntity.getBody(), "pageSize");

        assertThat(name).hasSizeGreaterThanOrEqualTo(4);
        assertThat(id).hasSizeGreaterThanOrEqualTo(4);
        assertThat(Integer.parseInt(pageSize.get(0)) == 4);
    }

    /**
     * Test of getPathways method, of class ResourceController.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testGetMetabolites() throws IOException {
        String requestUrl = "http://localhost:" + port + "/enzymeportal/rest/resource/metabolites?pageSize=4";
        RequestEntity entity = new RequestEntity(HttpMethod.GET, URI.create(requestUrl));

        ResponseEntity<String> responseEntity = restTemplate.exchange(entity, String.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseEntity.getBody());

        List<JsonNode> id = TestUtil.getJsonNodeFromJsonData(responseEntity.getBody(), "metaboliteId");
        List<String> name = TestUtil.getValuesFromJsonData(responseEntity.getBody(), "metaboliteName");
        List<String> pageSize = TestUtil.getValuesFromJsonData(responseEntity.getBody(), "pageSize");

        assertThat(name).hasSizeGreaterThanOrEqualTo(4);
        assertThat(id).hasSizeGreaterThanOrEqualTo(4);
        assertThat(Integer.parseInt(pageSize.get(0)) == 4);
    }

}
