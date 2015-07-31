/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.uniprotservice.eupmc;

import java.util.Optional;
import org.apache.log4j.Logger;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author joseph
 */

public class PmcService {

    private final Logger LOGGER = Logger.getLogger(PmcService.class);
    private RestTemplate restTemplate = null;// new RestTemplate(clientHttpRequestFactory());

    public PmcService() {
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

    public Optional<EuropePMC> getEuropePMC(String url) {

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
