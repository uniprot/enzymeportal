/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.pdbeadapter;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author joseph
 */
@Service
public class PdbService {

    private final RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        return factory;
    }

    private PdbSearchResult getPdbSearchResult(String url) {
        PdbSearchResult results = restTemplate.getForObject(url.trim(), PdbSearchResult.class);
        return results;
    }

    public PdbSearchResult getPdbSearchResults(String pdbId) {
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

}
