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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.pdbeadapter.cofactor.PdbCofactor;
import uk.ac.ebi.ep.pdbeadapter.config.PDBeUrl;
import uk.ac.ebi.ep.pdbeadapter.experiment.PDBexperiments;
import uk.ac.ebi.ep.pdbeadapter.ligand.PdbLigand;
import uk.ac.ebi.ep.pdbeadapter.molecule.PDBmolecules;
import uk.ac.ebi.ep.pdbeadapter.publication.PDBePublications;
import uk.ac.ebi.ep.pdbeadapter.summary.PdbSearchResult;

/**
 *
 * @author joseph
 */
public class PDBeRestService {

    private static final Logger LOGGER = Logger.getLogger(PDBeRestService.class);

    @Autowired
    private PDBeUrl pDBeUrl;

    private RestTemplate restTemplate = new RestTemplate();

    public PDBeRestService() {
        restTemplate = new RestTemplate(clientHttpRequestFactory());
    }

    public PDBeRestService(PDBeUrl pDBeUrl) {
        restTemplate = new RestTemplate(clientHttpRequestFactory());
        this.pDBeUrl = pDBeUrl;
    }

    /**
     *
     * @param service restTemplate
     * @param pDBeUrl pdbUrl
     */
    public PDBeRestService(RestTemplate service, PDBeUrl pDBeUrl) {
        this.restTemplate = service;
        this.pDBeUrl = pDBeUrl;
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory();

    }

    private PdbSearchResult getPdbSearchResult(String url) {
        return restTemplate.getForObject(url.trim(), PdbSearchResult.class);

    }

    /**
     *
     * @param pdbId pdbe id
     * @return the summary results for the pdb id
     */
    public PdbSearchResult getPdbSummaryResults(String pdbId) {

        String url = pDBeUrl.getSummaryUrl() + pdbId;
        if (pDBeUrl.getSummaryUrl() == null) {
            url = "https://www.ebi.ac.uk/pdbe/api/pdb/entry/summary/" + pdbId;
        }
        PdbSearchResult pdb = null;

        try {

            pdb = getPdbSearchResult(url.trim());
        } catch (Exception e) {
            LOGGER.error("Error while searching with PDB ID " + pdbId + " : " + e.getMessage(), e);
        }

        return pdb;

    }

    /**
     *
     * @param pdbId pdbe id
     * @return experiment results for the pdb id
     */
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

    /**
     *
     * @param pdbId pdbe id
     * @return publication result for the pdbe id
     */
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

    /**
     *
     * @param pdbId pdbe id
     * @return molecules result for the pdbe id
     */
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

    /**
     *
     * @param pdbId pdbe id
     * @return structural domain for the pdbe id
     */
    public String getStructuralDomain(String pdbId) {
        try {

            String url = pDBeUrl.getStructuralDomainUrl() + pdbId;

            //convert json to string 
            String json = IOUtils.toString(new URL(url));

            // create an ObjectMapper instance.
            ObjectMapper mapper = new ObjectMapper();
            // use the ObjectMapper to read the json string 
            JsonNode nodes = mapper.readTree(json);

            return nodes.findValue("homology").textValue();

        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return "";
    }

    public PdbLigand findPdbLigand(String pdbId) {

        String url = pDBeUrl.getLigandUrl() + pdbId;

        if (pDBeUrl.getLigandUrl() == null) {
            url = "https://www.ebi.ac.uk/pdbe/api/pdb/entry/ligand_monomers/" + pdbId;
        }

        try {
            return restTemplate.getForObject(url.trim(), PdbLigand.class);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return null;
    }

    public PdbCofactor findPdbCofactor(String pdbId) {

        String url = pDBeUrl.getCofactorUrl() + pdbId;
        if (pDBeUrl.getCofactorUrl() == null) {
            url = "https://www.ebi.ac.uk/pdbe/api/pdb/entry/cofactor/" + pdbId;
        }

        try {
            return restTemplate.getForObject(url.trim(), PdbCofactor.class);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return null;
    }

}
