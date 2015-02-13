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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.pdbeadapter.experiment.PDBexperiments;
import uk.ac.ebi.ep.pdbeadapter.molecule.PDBmolecules;
import uk.ac.ebi.ep.pdbeadapter.publication.PDBePublications;
import uk.ac.ebi.ep.pdbeadapter.summary.PdbSearchResult;

/**
 *
 * @author joseph
 */
@Service
public class PDBeRestService {

    private final RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        return factory;
    }

    private PdbSearchResult getPdbSearchResult(String url) {
        PdbSearchResult results = restTemplate.getForObject(url.trim(), PdbSearchResult.class);
        return results;
    }

//    public PdbSearchResult getPdbSearchResults(String pdbId) {
//        String url = "http://wwwdev.ebi.ac.uk/pdbe/api/pdb/entry/summary/" + pdbId;
//        // Add the Jackson message converter
//        //restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//        PdbSearchResult pdb = null;
//        try {
//            pdb = restTemplate.getForObject(url.trim(), PdbSearchResult.class);
//        } catch (Exception e) {
//
//        }
//
//        return pdb;
//
//    }
    public PdbSearchResult getPdbSummaryResults(String pdbId) {
        String url = "http://wwwdev.ebi.ac.uk/pdbe/api/pdb/entry/summary/" + pdbId;
        // Add the Jackson message converter
        //restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        PdbSearchResult pdb = null;
        try {
            pdb = restTemplate.getForObject(url.trim(), PdbSearchResult.class);
        } catch (Exception e) {

        }

        return pdb;

    }

    public PDBexperiments getPDBexperimentResults(String pdbId) {
        String url = "http://wwwdev.ebi.ac.uk/pdbe/api/pdb/entry/experiment/" + pdbId;

        PDBexperiments experiments = null;
        try {
            experiments = restTemplate.getForObject(url.trim(), PDBexperiments.class);
        } catch (Exception e) {

        }

        return experiments;
    }

    public PDBePublications getPDBpublicationResults(String pdbId) {

        String url = "http://wwwdev.ebi.ac.uk/pdbe/api/pdb/entry/publications/" + pdbId;

        PDBePublications publications = null;
        try {
            publications = restTemplate.getForObject(url.trim(), PDBePublications.class);
        } catch (Exception e) {

        }

        return publications;
    }

    public PDBmolecules getPDBmoleculeResults(String pdbId) {

        String url = "http://wwwdev.ebi.ac.uk/pdbe/api/pdb/entry/molecules/" + pdbId;

        PDBmolecules molecules = null;
        try {
            molecules = restTemplate.getForObject(url.trim(), PDBmolecules.class);
        } catch (Exception e) {

        }
        return molecules;
    }

    public String getStructuralDomain(String pdbId) {
        try {
            String pdburl = "http://wwwdev.ebi.ac.uk/pdbe/api/mappings/cath/" + pdbId;

            //convert json to string 
            String json = IOUtils.toString(new URL(pdburl));

            // create an ObjectMapper instance.
            ObjectMapper mapper = new ObjectMapper();
            // use the ObjectMapper to read the json string and create a tree
            JsonNode nodes = mapper.readTree(json);

            String homology = nodes.findValue("homology").textValue();

            return homology;
        } catch (IOException ex) {
            Logger.getLogger(PDBeRestService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }



}
