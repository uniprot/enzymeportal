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
import uk.ac.ebi.ep.pdbeadapter.summary.PdbSearchResult;

/**
 *
 * @author joseph
 */
public class PdbServiceTest extends BaseTest {

    @Autowired
    private PdbService pdbService;
    @Autowired
    private PDBeRestService restService;

    /**
     * Test of getPdbSearchResults method, of class PdbService.
     */
    @Test
    public void testGetPdbSearchResults() {

        LOGGER.info("getPdbSearchResults");

        try {

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
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

    }

    /**
     * Test of computeProteinStructure method, of class PdbService.
     */
    @Test
    public void testComputeProteinStructure() {
        LOGGER.info("computeProteinStructure");
        String pdbId = "3tge";

        PDB expResult = new PDB();
        expResult.setId(pdbId);
        PDB result = pdbService.computeProteinStructure(pdbId);
        assertEquals(expResult.getId(), result.getId());

    }

}
