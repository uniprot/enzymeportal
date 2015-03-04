package uk.ac.ebi.ep.pdbeadapter;

import java.io.IOException;
import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import uk.ac.ebi.ep.pdbeadapter.experiment.PDBexperiments;
import uk.ac.ebi.ep.pdbeadapter.molecule.PDBmolecules;
import uk.ac.ebi.ep.pdbeadapter.publication.PDBePublications;
import uk.ac.ebi.ep.pdbeadapter.summary.PdbSearchResult;

/**
 *
 * @author joseph
 */
public class PDBeRestServiceTest extends AbstractPDBeTest {

    @Autowired
    private PDBeRestService restService;

    @Test
    public void testPDBeRestService() {
        LOGGER.info("testPDBeRestService");
        PDBeRestService service = new PDBeRestService(restTemplate, pDBeUrl);

        assertNotNull(service);
    }

    /**
     * Test of getPdbSummaryResults method, of class PDBeRestService.
     */
    @Test
    public void testGetPdbSummaryResults() {
        LOGGER.info("getPdbSummaryResults");

        String pdbId = "3tge";
        String url = pDBeUrl.getSummaryUrl() + pdbId;

        String filename = "summary.json";
        String json = getJsonFile(filename);

        mockRestServer.expect(requestTo(url)).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        PdbSearchResult expResult = restTemplate.getForObject(url.trim(), PdbSearchResult.class);

        PdbSearchResult result = restService.getPdbSummaryResults(pdbId);

        String title = expResult.get(pdbId).stream().findFirst().get().getTitle();
        mockRestServer.verify();

        assertThat(result.get(pdbId).stream().findFirst().get().getTitle(), containsString(title));
        assertEquals(title, result.get(pdbId).stream().findFirst().get().getTitle());
        assertEquals(expResult.get(pdbId).stream().findFirst().get().getTitle(), result.get(pdbId).stream().findFirst().get().getTitle());
    }

    /**
     * Test of getPDBexperimentResults method, of class PDBeRestService.
     */
    @Test
    public void testGetPDBexperimentResults() {

        LOGGER.info("getPDBexperimentResults");

        String pdbId = "3tge";
        String url = pDBeUrl.getExperimentUrl() + pdbId;

        String filename = "experiment.json";
        String json = getJsonFile(filename);

        mockRestServer.expect(requestTo(url)).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        PDBexperiments result = restService.getPDBexperimentResults(pdbId);
        PDBexperiments expResult = restTemplate.getForObject(url.trim(), PDBexperiments.class);

        String experimentalMethod = expResult.get(pdbId).stream().findFirst().get().getExperimentalMethod();

        mockRestServer.verify();

        assertThat(result.get(pdbId).stream().findFirst().get().getExperimentalMethod(), containsString(experimentalMethod));
        assertEquals(experimentalMethod, result.get(pdbId).stream().findFirst().get().getExperimentalMethod());

    }

    /**
     * Test of getPDBpublicationResults method, of class PDBeRestService.
     */
    @Test
    public void testGetPDBpublicationResults() {
        LOGGER.info("getPDBpublicationResults");

        String pdbId = "3tge";
        String url = pDBeUrl.getPublicationsUrl() + pdbId;

        String filename = "publication.json";
        String json = getJsonFile(filename);

        mockRestServer.expect(requestTo(url)).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        PDBePublications expResult = restTemplate.getForObject(url.trim(), PDBePublications.class);

        PDBePublications result = restService.getPDBpublicationResults(pdbId);

        String title = expResult.get(pdbId).stream().findFirst().get().getTitle();
        mockRestServer.verify();

        assertThat(result.get(pdbId).stream().findFirst().get().getTitle(), containsString(title));
        assertEquals(title, result.get(pdbId).stream().findFirst().get().getTitle());
        assertEquals(expResult.get(pdbId).stream().findFirst().get().getTitle(), result.get(pdbId).stream().findFirst().get().getTitle());

    }

    /**
     * Test of getPDBmoleculeResults method, of class PDBeRestService.
     */
    @Test
    public void testGetPDBmoleculeResults() {
        LOGGER.info("getPDBmoleculeResults");

        String pdbId = "3tge";
        String url = pDBeUrl.getMoleculesUrl() + pdbId;

        String filename = "molecule.json";
        String json = getJsonFile(filename);

        mockRestServer.expect(requestTo(url)).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        PDBmolecules expResult = restTemplate.getForObject(url.trim(), PDBmolecules.class);

        PDBmolecules result = restService.getPDBmoleculeResults(pdbId);

        String name = expResult.get(pdbId).stream().findFirst().get().getMoleculeName();
        mockRestServer.verify();

        assertThat(result.get(pdbId).stream().findFirst().get().getMoleculeName(), containsString(name));
        assertEquals(name, result.get(pdbId).stream().findFirst().get().getMoleculeName());
        assertEquals(expResult.get(pdbId).stream().findFirst().get().getMoleculeName(), result.get(pdbId).stream().findFirst().get().getMoleculeName());

    }

    /**
     * Test of getStructuralDomain method, of class PDBeRestService.
     */
    @Test
    public void testGetStructuralDomain() {
        LOGGER.info("getStructuralDomain");

        try {

            String pdbId = "3tge";
            String url = pDBeUrl.getStructuralDomainUrl() + pdbId;

            String filename = "structureDomain.json";
            String json = getJsonFile(filename);

            mockRestServer.expect(requestTo(url)).andExpect(method(HttpMethod.GET))
                    .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

            String result = restService.getStructuralDomain(pdbId);

            String data = restTemplate.getForObject(url.trim(), String.class);

            String homology = "homology";
            String expResult = getValueFromJsonData(data, homology);
            mockRestServer.verify();

            assertThat(result, containsString(expResult));
            assertEquals(expResult, result);

        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

    }

}
