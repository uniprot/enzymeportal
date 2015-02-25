/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.pdbeadapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import static junit.framework.TestCase.assertEquals;
import org.apache.commons.io.IOUtils;
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
public class PDBeRestServiceTest extends BaseTest {

    @Autowired
    private PDBeRestService restService;



    /**
     * Test of getPdbSummaryResults method, of class PDBeRestService.
     */
    @Test
    public void testGetPdbSummaryResults() {
        System.out.println("getPdbSummaryResults");
        try {

            String pdbId = "3tge";
            String url = pDBeUrl.getSummaryUrl() + pdbId;
            InputStream in = this.getClass().getClassLoader()
                    .getResourceAsStream("summary.json");

            String json = IOUtils.toString(in);

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
            Logger.getLogger(PDBeRestServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of getPDBexperimentResults method, of class PDBeRestService.
     */
    @Test
    public void testGetPDBexperimentResults() {

        System.out.println("getPDBexperimentResults");

        try {

            String pdbId = "3tge";
            String url = pDBeUrl.getExperimentUrl() + pdbId;
            InputStream in = this.getClass().getClassLoader()
                    .getResourceAsStream("experiment.json");

            String json = IOUtils.toString(in);

            mockRestServer.expect(requestTo(url)).andExpect(method(HttpMethod.GET))
                    .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

            PDBexperiments result = restService.getPDBexperimentResults(pdbId);
            PDBexperiments expResult = restTemplate.getForObject(url.trim(), PDBexperiments.class);

            String experimental_method = expResult.get(pdbId).stream().findFirst().get().getExperimentalMethod();

            mockRestServer.verify();

            assertThat(result.get(pdbId).stream().findFirst().get().getExperimentalMethod(), containsString(experimental_method));
            assertEquals(experimental_method, result.get(pdbId).stream().findFirst().get().getExperimentalMethod());
        } catch (IOException ex) {
            Logger.getLogger(PDBeRestServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Test of getPDBpublicationResults method, of class PDBeRestService.
     */
    @Test
    public void testGetPDBpublicationResults() {
        System.out.println("getPDBpublicationResults");

        try {

            String pdbId = "3tge";
            String url = pDBeUrl.getPublicationsUrl() + pdbId;
            InputStream in = this.getClass().getClassLoader()
                    .getResourceAsStream("publication.json");

            String json = IOUtils.toString(in);

            mockRestServer.expect(requestTo(url)).andExpect(method(HttpMethod.GET))
                    .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

            PDBePublications expResult = restTemplate.getForObject(url.trim(), PDBePublications.class);

            PDBePublications result = restService.getPDBpublicationResults(pdbId);

            String title = expResult.get(pdbId).stream().findFirst().get().getTitle();
            mockRestServer.verify();

            assertThat(result.get(pdbId).stream().findFirst().get().getTitle(), containsString(title));
            assertEquals(title, result.get(pdbId).stream().findFirst().get().getTitle());
            assertEquals(expResult.get(pdbId).stream().findFirst().get().getTitle(), result.get(pdbId).stream().findFirst().get().getTitle());
        } catch (IOException ex) {
            Logger.getLogger(PDBeRestServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Test of getPDBmoleculeResults method, of class PDBeRestService.
     */
    @Test
    public void testGetPDBmoleculeResults() {
        System.out.println("getPDBmoleculeResults");

        try {

            String pdbId = "3tge";
            String url = pDBeUrl.getMoleculesUrl() + pdbId;
            InputStream in = this.getClass().getClassLoader()
                    .getResourceAsStream("molecule.json");

            String json = IOUtils.toString(in);

            mockRestServer.expect(requestTo(url)).andExpect(method(HttpMethod.GET))
                    .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

            PDBmolecules expResult = restTemplate.getForObject(url.trim(), PDBmolecules.class);

            PDBmolecules result = restService.getPDBmoleculeResults(pdbId);

            String name = expResult.get(pdbId).stream().findFirst().get().getMoleculeName();
            mockRestServer.verify();

            assertThat(result.get(pdbId).stream().findFirst().get().getMoleculeName(), containsString(name));
            assertEquals(name, result.get(pdbId).stream().findFirst().get().getMoleculeName());
            assertEquals(expResult.get(pdbId).stream().findFirst().get().getMoleculeName(), result.get(pdbId).stream().findFirst().get().getMoleculeName());
        } catch (IOException ex) {
            Logger.getLogger(PDBeRestServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Test of getStructuralDomain method, of class PDBeRestService.
     */
    @Test
    public void testGetStructuralDomain() {
        System.out.println("getStructuralDomain");

        try {

            String pdbId = "3tge";
            String url = pDBeUrl.getStructuralDomainUrl() + pdbId;
            InputStream in = this.getClass().getClassLoader()
                    .getResourceAsStream("structureDomain.json");

            String json = IOUtils.toString(in);

            mockRestServer.expect(requestTo(url)).andExpect(method(HttpMethod.GET))
                    .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

            String result = restService.getStructuralDomain(pdbId);

            String data = restTemplate.getForObject(url.trim(), String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode nodes = mapper.readTree(data);
            String homology = nodes.findValue("homology").textValue();

            String expResult = homology;
            mockRestServer.verify();

            assertThat(result, containsString(homology));
            assertEquals(expResult, result);

        } catch (IOException ex) {
            Logger.getLogger(PDBeRestServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
