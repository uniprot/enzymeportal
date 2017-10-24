/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import uk.ac.ebi.ep.adapter.chembl.ChemblConfig;
import uk.ac.ebi.ep.common.Config;
import uk.ac.ebi.ep.enzymeservices.chebi.ChebiConfig;
import uk.ac.ebi.ep.enzymeservices.intenz.IntenzAdapter;
import uk.ac.ebi.ep.enzymeservices.intenz.IntenzConfig;
import uk.ac.ebi.ep.enzymeservices.reactome.ReactomeConfig;
import uk.ac.ebi.ep.functions.Functions;
import uk.ac.ebi.ep.mBean.FilesConfig;
import uk.ac.ebi.ep.pdbeadapter.PDBeRestService;
import uk.ac.ebi.ep.pdbeadapter.PdbService;
import uk.ac.ebi.ep.pdbeadapter.config.PDBeUrl;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Configuration
public class ApplicationContextMock {
    

    //maybe not be needed anymore as we now use spting 4 +
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }

    @Bean
    public String uniprotAlignUrl() {
        String alignUrl = "http://www.uniprot.org/align/?redirect=yes&annotated=false&program=clustalo&query={0}+{1}";
        return alignUrl;
    }

    @Bean
    public String pdbImgUrl() {
        String pdbImgUrl = "https://www.ebi.ac.uk/pdbe/static/entry/{0}_deposited_chain_front_image-200x200.png";
        return pdbImgUrl;
    }

    @Bean
    public String pdbStructureCompareUrl() {
        String pdbStructureCompareUrl = "https://www.ebi.ac.uk/msd-srv/ssm/cgi-bin/ssmserver?";
        return pdbStructureCompareUrl;
    }

    @Bean
    public IntenzConfig intenzConfig() {
        IntenzConfig config = new IntenzConfig();

        config.setTimeout(30000);
        config.setIntenzXmlUrl("https://www.ebi.ac.uk/intenz/ws/EC/{0}.{1}.{2}.{3}.xml");
        config.setEcBaseUrl("https://www.ebi.ac.uk/intenz/query?cmd=Search&q=");
        return config;
    }

    @Bean
    public IntenzAdapter intenzAdapter() {
        IntenzAdapter intenz = new IntenzAdapter();
        intenz.setConfig(intenzConfig());
        return intenz;
    }
    
    @Bean
    public Config searchConfig() {
        Config config = new Config();

        config.setResultsPerPage(10);
        config.setMaxPages(1);
        config.setMaxTextLength(60);
        config.setMaxMoleculesPerGroup(3);
        config.setSearchCacheSize(100);
        return config;
    }

    @Bean
    public ReactomeConfig reactomeConfig() {
        ReactomeConfig rc = new ReactomeConfig();
        rc.setTimeout(30000);
        rc.setUseProxy(Boolean.TRUE);
        rc.setWsBaseUrl("http://reactomews.oicr.on.ca:8080/ReactomeRESTfulAPI/RESTfulWS/queryById/");
        rc.setEventBaseUrl("http://www.reactome.org/content/detail/");

        return rc;
    }
    
    @Bean
    public ChebiConfig chebiConfig() {
        ChebiConfig chebiConfig = new ChebiConfig();
        chebiConfig.setTimeout(30000);
        chebiConfig.setMaxThreads(10);
        chebiConfig.setSearchStars("ALL");
        chebiConfig.setMaxRetrievedMolecules(3);
        chebiConfig.setCompoundBaseUrl("https://www.ebi.ac.uk/chebi/searchId.do?chebiId=");
        chebiConfig.setCompoundImgBaseUrl("https://www.ebi.ac.uk/chebi/displayImage.do?defaultImage=true&imageIndex=0&chebiId=");

        return chebiConfig;
    }

    public Map<String, String> drugbankConfig() {
        Map<String, String> drugbankConfig = new HashMap<>();
        drugbankConfig.put("compound.base.url", "http://www.drugbank.ca/drugs/");
        drugbankConfig.put("compound.img.base.url", "http://structures.wishartlab.com/molecules/{0}/image.png");

        return drugbankConfig;
    }

    @Bean
    public ChemblConfig chemblConfig() {
        ChemblConfig chemblConfig = new ChemblConfig();

        return chemblConfig;
    }


    @Bean
    public Functions functions() {
        Functions functions = new Functions();
        Functions.setChebiConfig(chebiConfig());
        Functions.setChemblConfig(chemblConfig());
        Functions.setDrugbankConfig(drugbankConfig());
        return functions;
    }

    @Bean
    public FilesConfig filesConfig() {
        FilesConfig filesConfig = new FilesConfig();
        filesConfig.setBaseDirectory("/nfs/public/rw/uniprot/enzyme_portal");
        filesConfig.setSitemapIndex("file:/nfs/public/rw/uniprot/enzyme_portal/sitemap");
        filesConfig.setSitemapUrl("file:/nfs/public/rw/uniprot/enzyme_portal/sitemap/sitemap_index.xml");
        return filesConfig;
    }


    @Bean
    public PdbService pdbService() {
        return new PdbService(pdbeRestService());
    }

    @Bean
    public PDBeRestService pdbeRestService() {

        return new PDBeRestService();
    }
    
    @Bean
    public PDBeUrl pDBeUrl() {
        PDBeUrl pdBeUrl = new PDBeUrl();

        String summaryUrl = "https://www.ebi.ac.uk/pdbe/api/pdb/entry/summary/";
        String experimentUrl = "https://www.ebi.ac.uk/pdbe/api/pdb/entry/experiment/";
        String publicationsUrl = "https://www.ebi.ac.uk/pdbe/api/pdb/entry/publications/";
        String moleculesUrl = "https://www.ebi.ac.uk/pdbe/api/pdb/entry/molecules/";
        String structuralDomainUrl = "https://www.ebi.ac.uk/pdbe/api/mappings/cath/";

        pdBeUrl.setSummaryUrl(summaryUrl);
        pdBeUrl.setExperimentUrl(experimentUrl);
        pdBeUrl.setPublicationsUrl(publicationsUrl);
        pdBeUrl.setMoleculesUrl(moleculesUrl);
        pdBeUrl.setStructuralDomainUrl(structuralDomainUrl);
        return pdBeUrl;
    }

    
      @Bean
  public SpelAwareProxyProjectionFactory projectionFactory() {
    return new SpelAwareProxyProjectionFactory();
  }
  
}
