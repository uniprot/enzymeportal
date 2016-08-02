package uk.ac.ebi.ep.centralservice.chembl.service;

import java.util.Optional;
import org.apache.log4j.Logger;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.centralservice.chembl.activity.ChemblActivity;
import uk.ac.ebi.ep.centralservice.chembl.assay.ChemblAssay;
import uk.ac.ebi.ep.centralservice.chembl.mechanism.FdaApproved;
import uk.ac.ebi.ep.centralservice.chembl.molecule.ChemblMolecule;

/**
 *
 * @author joseph
 */
public class ChemblRestService {

    private final Logger logger = Logger.getLogger(ChemblRestService.class);
    private RestTemplate restTemplate = null;

    public ChemblRestService() {
        restTemplate = new RestTemplate(clientHttpRequestFactoryTimeout());
    }

    protected ClientHttpRequestFactory clientHttpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory();

    }

    private ClientHttpRequestFactory clientHttpRequestFactoryTimeout() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(60000);//1 min
        factory.setConnectTimeout(60000);
        return factory;
    }

    public Optional<FdaApproved> getFdaApprovedDrug(String url) {

        Optional<FdaApproved> fda = Optional.empty();
        try {

            fda = Optional.ofNullable(restTemplate.getForObject(url.trim(), FdaApproved.class));

        } catch (RestClientException ex) {
            logger.error(ex.getMessage(), ex);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return fda;

    }

    public Optional<ChemblMolecule> getChemblMolecule(String url) {

        Optional<ChemblMolecule> mol = Optional.empty();
        try {
            mol = Optional.ofNullable(restTemplate.getForObject(url.trim(), ChemblMolecule.class));

        } catch (RestClientException ex) {
            logger.error(ex.getMessage(), ex);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return mol;
    }

    public Optional<ChemblAssay> getChemblAssay(String url) {

        Optional<ChemblAssay> assay = Optional.empty();
        try {
            assay = Optional.ofNullable(restTemplate.getForObject(url.trim(), ChemblAssay.class));

        } catch (RestClientException ex) {
            logger.error(ex.getMessage(), ex);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return assay;

    }

    public Optional<ChemblActivity> getChemblActivity(String url) {

        Optional<ChemblActivity> activity = Optional.empty();
        try {
            activity = Optional.ofNullable(restTemplate.getForObject(url.trim(), ChemblActivity.class));

        } catch (RestClientException ex) {
            logger.error(ex.getMessage(), ex);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return activity;
    }

}
