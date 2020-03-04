package uk.ac.ebi.ep.enzymeservice.uniprot.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.enzymeservice.uniprot.model.Comment;
import uk.ac.ebi.ep.enzymeservice.uniprot.model.Kinetics;
import uk.ac.ebi.ep.enzymeservice.uniprot.model.PhDependence;
import uk.ac.ebi.ep.enzymeservice.uniprot.model.TemperatureDependence;
import uk.ac.ebi.ep.enzymeservice.uniprot.model.UniprotApi;
import uk.ac.ebi.ep.restclient.service.RestConfigService;

/**
 *
 * @author joseph
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UniprotServiceTest {

    @Autowired
    private UniprotService uniprotService;

    @Autowired
    private RestConfigService restConfigService;
    @Autowired
    private RestTemplate restTemplate;

    private final String accession = "Q5JHF1";

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(uniprotService).isNotNull();
        assertThat(restConfigService).isNotNull();
        assertThat(restTemplate).isNotNull();
    }

    /**
     * Test of uniprotApiByAccession method, of class UniprotService.
     */
    @Test
    public void testUniprotApiByAccession() {
        log.info("testUniprotApiByAccession");
        UniprotApi result = uniprotService.uniprotApiByAccession(accession);

        assertNotNull(result);
        assertEquals(accession, result.getAccession());
        assertThat(result.getComments(), hasSize(greaterThanOrEqualTo(1)));
    }

    @Test
    public void testUniprotApiByEc() {
        log.info("testUniprotApiByEc");
        String ec = "1.1.1.1";
        //ec ="2.1.1.61";
        List<UniprotApi> results = uniprotService.uniprotApiByEc(ec);
        assertNotNull(results);
        assertThat(results, hasSize(greaterThanOrEqualTo(1)));
        results.stream().forEach(result -> System.out.println("Uniprot Entry " + result.getId() + " - " + result.getAccession()));

    }

    /**
     * Test of findKinectParamsCommentByAccession method, of class
     * UniprotService.
     */
    @Test
    public void testFindKinectParamsCommentByAccession() {
        log.info("testFindKinectParamsCommentByAccession");

        Comment result = uniprotService.findKinectParamsCommentByAccession(accession);
        assertNotNull(result);
        assertNotNull(result.getKinetics());
        assertThat(result.getPhDependence(), hasSize(greaterThanOrEqualTo(1)));
        assertThat(result.getTemperatureDependence(), hasSize(greaterThanOrEqualTo(1)));

    }

    /**
     * Test of findKineticsByAccession method, of class UniprotService.
     */
    @Test
    public void testFindKineticsByAccession() {
        log.info("testFindKineticsByAccession");
        Kinetics result = uniprotService.findKineticsByAccession(accession);
        assertNotNull(result);

        assertThat(result.getKm(), hasSize(greaterThanOrEqualTo(1)));
        assertThat(result.getVmax(), hasSize(greaterThanOrEqualTo(1)));
    }

    /**
     * Test of findPhDependenceByAccession method, of class UniprotService.
     */
    @Test
    public void testFindPhDependenceByAccession() {
        log.info("testFindPhDependenceByAccession");

        List<PhDependence> result = uniprotService.findPhDependenceByAccession(accession);
        assertNotNull(result);

        assertThat(result, hasSize(greaterThanOrEqualTo(1)));

    }

    /**
     * Test of findTemperatureDependenceByAccession method, of class
     * UniprotService.
     */
    @Test
    public void testFindTemperatureDependenceByAccession() {
        log.info("testFindTemperatureDependenceByAccession");
        List<TemperatureDependence> result = uniprotService.findTemperatureDependenceByAccession(accession);
        assertNotNull(result);

        assertThat(result, hasSize(greaterThanOrEqualTo(1)));
    }

}
