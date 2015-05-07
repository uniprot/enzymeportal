/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.centralservice.chembl.service;

import java.util.Optional;
import org.apache.log4j.Logger;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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

    private static final Logger LOGGER = Logger.getLogger(ChemblRestService.class);
    private RestTemplate restTemplate = null;// new RestTemplate(clientHttpRequestFactory());

    public ChemblRestService() {
        restTemplate = new RestTemplate(clientHttpRequestFactory());
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory();

    }

    public FdaApproved getFdaApprovedDrug(String url) {
        return restTemplate.getForObject(url.trim(), FdaApproved.class);

    }

    public Optional<ChemblMolecule> getChemblMolecule(String url) {
        //return restTemplate.getForObject(url.trim(), ChemblMolecule.class);
        Optional<ChemblMolecule> mol = Optional.empty();
        try {
            mol = Optional.ofNullable(restTemplate.getForObject(url.trim(), ChemblMolecule.class));

        } catch (Exception e) {
           
            LOGGER.error(e.getMessage(), e);
        }
        return mol;
    }

    public ChemblAssay getChemblAssay(String url) {
        return restTemplate.getForObject(url.trim(), ChemblAssay.class);

    }

    public ChemblActivity getChemblActivity(String url) {
        return restTemplate.getForObject(url.trim(), ChemblActivity.class);

    }

}
