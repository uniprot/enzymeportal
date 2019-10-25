package uk.ac.ebi.ep.literatureservice.service;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.literatureservice.config.PmcServiceUrl;
import uk.ac.ebi.ep.literatureservice.model.EuropePMC;

/**
 *
 * @author joseph
 */
@Slf4j
public class PmcRestService {

    private final RestTemplate pmcRestTemplate;
    private final PmcServiceUrl pmcServiceUrl;

    @Autowired
    public PmcRestService(PmcServiceUrl pmcServiceUrl, RestTemplate pmcRestTemplate) {
        this.pmcRestTemplate = pmcRestTemplate;
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

            pmc = Optional.ofNullable(pmcRestTemplate.getForObject(url.trim(), EuropePMC.class));

        } catch (RestClientException ex) {
            log.error(ex.getMessage(), ex);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return pmc;
    }

    public Optional<EuropePMC> findPublicationsByAccession(String accession, int limit) {
        String url = pmcServiceUrl.getSpecificUrl() + accession + "&format=json&pageSize=" + limit + "&resulttype=core";
        Optional<EuropePMC> pmc = Optional.empty();
        try {

            pmc = Optional.ofNullable(pmcRestTemplate.getForObject(url.trim(), EuropePMC.class));

        } catch (RestClientException ex) {
            log.error(ex.getMessage(), ex);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return pmc;

    }
}
