/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.parser.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import uk.ac.ebi.chebi.webapps.chebiWS.client.ChebiWebServiceClient;
import uk.ac.ebi.ep.centralservice.chembl.config.ChemblServiceUrl;
import uk.ac.ebi.ep.centralservice.chembl.service.ChemblRestService;
import uk.ac.ebi.ep.centralservice.chembl.service.ChemblService;
import uk.ac.ebi.ep.parser.parsers.ChemblCompound;
import uk.ac.ebi.ep.parser.parsers.DiseaseParser;
import uk.ac.ebi.ep.parser.parsers.EnzymePortalCompoundParser;
import uk.ac.ebi.ep.parser.parsers.EnzymePortalPDBeParser;
import uk.ac.ebi.ep.parser.parsers.EnzymePortalPathwaysParser;
import uk.ac.ebi.ep.parser.parsers.FDA;
import uk.ac.ebi.ep.parser.xmlparser.ChemblXmlParser;
import uk.ac.ebi.ep.pdbeadapter.PDBeRestService;
import uk.ac.ebi.ep.pdbeadapter.PdbService;
import uk.ac.ebi.ep.pdbeadapter.config.PDBeUrl;

/**
 *
 * @author joseph
 */
@Configuration
@PropertySource({"classpath:chembl.urlConfig","classpath:pdb.urlConfig"})
public class GlobalDataConfig {

    @Autowired
    private Environment env;

    @Bean
    public DiseaseParser diseaseParser() {
        return new DiseaseParser();
    }

    @Bean
    public EnzymePortalCompoundParser enzymePortalCompoundParser() {
        return new EnzymePortalCompoundParser();
    }

    @Bean
    public EnzymePortalPathwaysParser enzymePortalPathwaysParser() {
        return new EnzymePortalPathwaysParser();
    }

    @Bean
    public PdbService pdbService() {
        return new PdbService(pdbeRestService());
    }

    @Bean
    public PDBeRestService pdbeRestService() {

        return new PDBeRestService(pDBeUrl());
    }

    @Bean
    public PDBeUrl pDBeUrl() {
        PDBeUrl pdBeUrl = new PDBeUrl();

        String summaryUrl = env.getProperty("pdb.summary.url");
        String experimentUrl = env.getProperty("pdb.experiment.url");
        String publicationsUrl = env.getProperty("pdb.publications.url");
        String moleculesUrl = env.getProperty("pdb.molecules.url");
        String structuralDomainUrl = env.getProperty("pdb.structuralDomain.url");

        pdBeUrl.setSummaryUrl(summaryUrl);
        pdBeUrl.setExperimentUrl(experimentUrl);
        pdBeUrl.setPublicationsUrl(publicationsUrl);
        pdBeUrl.setMoleculesUrl(moleculesUrl);
        pdBeUrl.setStructuralDomainUrl(structuralDomainUrl);
        return pdBeUrl;
    }

    @Bean
    public EnzymePortalPDBeParser enzymePortalPDBeParser() {
        return new EnzymePortalPDBeParser();
    }

    @Bean
    public ChebiWebServiceClient chebiWebServiceClient() {
        return new ChebiWebServiceClient();
    }

    @Bean
    public ChemblXmlParser chemblXmlParser() {
        return new ChemblXmlParser();
    }

    @Bean
    public ChemblRestService chemblRestService() {
        return new ChemblRestService();
    }

    @Bean
    public ChemblService chemblService() {
        return new ChemblService(chemblRestService(),chemblServiceUrl());
    }
    
       @Bean
    public ChemblServiceUrl chemblServiceUrl() {
        ChemblServiceUrl serviceUrl = new ChemblServiceUrl();
        
        String mechanismUrl = env.getProperty("chembl.mechanism.url");
        String moleculeUrl = env.getProperty("chembl.molecule.url");
        String assayUrl = env.getProperty("chembl.assay.url");
        String activityUrl = env.getProperty("chembl.activity.url");
        
        serviceUrl.setMechanismUrl(mechanismUrl);
        serviceUrl.setMoleculeUrl(moleculeUrl);
        serviceUrl.setAssayUrl(assayUrl);
        serviceUrl.setActivityUrl(activityUrl);
        
        return serviceUrl;
    }

    @Bean
    public ChemblCompound chemblCompound(){
        return new ChemblCompound();
    }
    
    @Bean
    public FDA fda(){
        return new FDA();
    }
}
