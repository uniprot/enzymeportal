/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.pdbeadapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.pdbeadapter.config.PDBeUrl;
import uk.ac.ebi.ep.pdbeadapter.experiment.PDBexperiments;
import uk.ac.ebi.ep.pdbeadapter.molecule.PDBmolecules;
import uk.ac.ebi.ep.pdbeadapter.publication.PDBePublications;
import uk.ac.ebi.ep.pdbeadapter.summary.PdbSearchResult;

/**
 *
 * @author joseph
 */
public class PDBeRestService {


    @Autowired
    private PDBeUrl pDBeUrl;

    private static final Logger LOGGER = Logger.getLogger(PDBeRestService.class);
    private final RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());

    private ClientHttpRequestFactory clientHttpRequestFactory() {
       return new HttpComponentsClientHttpRequestFactory();
        
    }

    private PdbSearchResult getPdbSearchResult(String url) {
       return restTemplate.getForObject(url.trim(), PdbSearchResult.class);
      
    }

    public PdbSearchResult getPdbSummaryResults(String pdbId) {

        String url = pDBeUrl.getSummaryUrl() + pdbId;
        PdbSearchResult pdb = null;

        try {
  
            pdb = getPdbSearchResult(url.trim());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return pdb;

    }

    public PDBexperiments getPDBexperimentResults(String pdbId) {

        String url = pDBeUrl.getExperimentUrl() + pdbId;

        PDBexperiments experiments = null;
        try {
            experiments = restTemplate.getForObject(url.trim(), PDBexperiments.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return experiments;
    }

    public PDBePublications getPDBpublicationResults(String pdbId) {


        String url = pDBeUrl.getPublicationsUrl() + pdbId;

        PDBePublications publications = null;
        try {
            publications = restTemplate.getForObject(url.trim(), PDBePublications.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return publications;
    }

    public PDBmolecules getPDBmoleculeResults(String pdbId) {

        String url = pDBeUrl.getMoleculesUrl() + pdbId;

        PDBmolecules molecules = null;
        try {
            molecules = restTemplate.getForObject(url.trim(), PDBmolecules.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return molecules;
    }

    public String getStructuralDomain(String pdbId) {
        try {

            String url = pDBeUrl.getStructuralDomainUrl() + pdbId;

            //convert json to string 
            String json = IOUtils.toString(new URL(url));

            // create an ObjectMapper instance.
            ObjectMapper mapper = new ObjectMapper();
            // use the ObjectMapper to read the json string and create a tree
            JsonNode nodes = mapper.readTree(json);

            return nodes.findValue("homology").textValue();

           
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return "";
    }


}
