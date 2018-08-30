/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.centralservice.chembl.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import uk.ac.ebi.ep.centralservice.chembl.service.ChemblRestService;
import uk.ac.ebi.ep.centralservice.chembl.service.ChemblService;

/**
 *
 * @author joseph
 */
@Configuration
@PropertySource({"classpath:chembl.urlConfig"})
public class ChemblServiceConfig {
    
    @Autowired
    private Environment env;
    
    @Bean
    public ChemblRestService chemblRestService() {
        return new ChemblRestService();
    }
    
    @Bean
    public ChemblService chemblService() {
        return new ChemblService(chemblRestService(), chemblServiceUrl());
    }
    
    @Bean
    public ChemblServiceUrl chemblServiceUrl() {
        ChemblServiceUrl serviceUrl = new ChemblServiceUrl();
        
        String mechanismUrl = env.getProperty("chembl.mechanism.url");
        String moleculeUrl = env.getProperty("chembl.molecule.url");
        String assayUrl = env.getProperty("chembl.assay.url");
        String activityUrl = env.getProperty("chembl.activity.url");
        String ic50ActivityUrl = env.getProperty("chembl.activity.ic50.url");
        String primaryTargetSelectorUrl = env.getProperty("chembl.primary.target.selector.url");
        String inhibitionIc50Url = env.getProperty("chembl.activity.inhibition.ic50.url");
        
        serviceUrl.setMechanismUrl(mechanismUrl);
        serviceUrl.setMoleculeUrl(moleculeUrl);
        serviceUrl.setAssayUrl(assayUrl);
        serviceUrl.setActivityUrl(activityUrl);
        serviceUrl.setIc50ActivityUrl(ic50ActivityUrl);
        serviceUrl.setPrimaryTargetSelectorUrl(primaryTargetSelectorUrl);
        serviceUrl.setInhibitionIc50Url(inhibitionIc50Url);
        
        return serviceUrl;
    }
}
