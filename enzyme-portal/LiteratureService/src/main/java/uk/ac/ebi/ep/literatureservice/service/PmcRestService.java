package uk.ac.ebi.ep.literatureservice.service;

import java.util.Optional;
import org.apache.log4j.Logger;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.literatureservice.config.PmcServiceUrl;
import uk.ac.ebi.ep.literatureservice.model.EuropePMC;

/**
 *
 * @author joseph
 */
public class PmcRestService {

    private final Logger LOGGER = Logger.getLogger(PmcRestService.class);
    private final RestTemplate restTemplate;
    private final PmcServiceUrl pmcServiceUrl;

    public PmcRestService(PmcServiceUrl pmcServiceUrl, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.pmcServiceUrl = pmcServiceUrl;
    }

    public Optional<EuropePMC> findPublicationsByKeyword(String keyword) {
        String url = pmcServiceUrl.getGenericUrl() + keyword + "&format=json&resulttype=core";
        return publicationsByKeyword(keyword, url);

    }

    public Optional<EuropePMC> findPublicationsByKeyword(String keyword, int limit) {
        String url = pmcServiceUrl.getGenericUrl() + keyword + "&format=json&pageSize=" + limit + "&resulttype=core";
        return publicationsByKeyword(keyword, url);

    }

    public Optional<EuropePMC> publicationsByKeyword(String keyword, String url) {
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

    public Optional<EuropePMC> findPublicationsByAccession(String accession, int limit) {
        String url = pmcServiceUrl.getSpecificUrl() + accession + "&format=json&pageSize=" + limit + "&resulttype=core";
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
