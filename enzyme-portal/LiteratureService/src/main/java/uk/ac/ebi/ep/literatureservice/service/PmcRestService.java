/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.literatureservice.service;

import java.util.Optional;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.literatureservice.config.PmcConfig;
import uk.ac.ebi.ep.literatureservice.model.EuropePMC;

/**
 *
 * @author joseph
 */
public class PmcRestService {

    private final Logger LOGGER = Logger.getLogger(PmcRestService.class);
    private RestTemplate restTemplate = null;// new RestTemplate(clientHttpRequestFactory());
    @Autowired
    private PmcConfig pmcConfig;

    public PmcRestService() {
        restTemplate = new RestTemplate(clientHttpRequestFactoryTimeout());
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory();

    }

    private ClientHttpRequestFactory clientHttpRequestFactoryTimeout() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(60000);//1 min
        factory.setConnectTimeout(60000);
        return factory;
    }

    public Optional<EuropePMC> getGenericPMC(String keyword) {
        String url = pmcConfig.pmcServiceUrl().getGenericUrl() + keyword + "&format=json&resulttype=core";
        Optional<EuropePMC> pmc = Optional.empty();
        try {

            pmc = Optional.ofNullable(restTemplate.getForObject(url.trim(), EuropePMC.class));

        } catch (RestClientException ex) {
            LOGGER.error(ex.getMessage(), ex);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return pmc;

    }

    public Optional<EuropePMC> getPmcByAccession(String accession) {
        String url = pmcConfig.pmcServiceUrl().getSpecificUrl() + accession + "&format=json&pageSize=100&resulttype=core";
        Optional<EuropePMC> pmc = Optional.empty();
        try {

            pmc = Optional.ofNullable(restTemplate.getForObject(url.trim(), EuropePMC.class));

        } catch (RestClientException ex) {
            LOGGER.error(ex.getMessage(), ex);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return pmc;

    }
}
