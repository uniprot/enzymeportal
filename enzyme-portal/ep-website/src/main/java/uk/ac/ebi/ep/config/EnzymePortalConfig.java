/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import uk.ac.ebi.ep.adapter.chembl.ChemblConfig;
import uk.ac.ebi.ep.adapter.das.DASConfig;
import uk.ac.ebi.ep.adapter.literature.LiteratureConfig;
import uk.ac.ebi.ep.common.Config;
import uk.ac.ebi.ep.enzymeservices.chebi.ChebiConfig;
import uk.ac.ebi.ep.enzymeservices.intenz.IntenzAdapter;
import uk.ac.ebi.ep.enzymeservices.intenz.IntenzConfig;
import uk.ac.ebi.ep.enzymeservices.reactome.ReactomeConfig;
import uk.ac.ebi.ep.functions.Functions;
import uk.ac.ebi.ep.mBean.FilesConfig;

/**
 *
 * @author joseph
 */
@Configuration
public class EnzymePortalConfig {

    @Autowired
    private Environment env;

    //maybe not needed anymore
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }

    @Bean
    public String pdbImgUrl() {
        String pdbImgUrl = env.getProperty("pdb.structure.img.url");
        return pdbImgUrl;
    }

    @Bean
    public String pdbStructureCompareUrl() {
        String compare = env.getProperty("pdb.structure.compare.url");
        return compare;
    }

    @Bean
    public IntenzConfig intenzConfig() {
        IntenzConfig config = new IntenzConfig();

        config.setTimeout(Integer.parseInt(env.getProperty("intenz.ws.timeout")));
        config.setIntenzXmlUrl(env.getProperty("intenz.xml.url"));
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

        config.setResultsPerPage(Integer.parseInt(env.getProperty("search.results.per.page")));
        config.setMaxPages(Integer.parseInt(env.getProperty("search.results.pages.max")));
        config.setMaxTextLength(Integer.parseInt(env.getProperty("search.summary.text.length.max")));
        config.setMaxMoleculesPerGroup(Integer.parseInt(env.getProperty("search.molecules.group.max")));
        config.setSearchCacheSize(Integer.parseInt(env.getProperty("search.cache.size")));
        return config;
    }

    @Bean
    public ReactomeConfig reactomeConfig() {
        ReactomeConfig rc = new ReactomeConfig();
        rc.setTimeout(Integer.parseInt(env.getProperty("reactome.ws.timeout")));
        rc.setUseProxy(Boolean.parseBoolean(env.getProperty("reactome.ws.proxy")));
        rc.setWsBaseUrl(env.getProperty("reactome.ws.url"));

        return rc;
    }

    @Bean
    public ChebiConfig chebiConfig() {
        ChebiConfig chebiConfig = new ChebiConfig();
        chebiConfig.setTimeout(Integer.parseInt(env.getProperty("chebi.ws.timeout")));
        chebiConfig.setMaxThreads(Integer.parseInt(env.getProperty("chebi.threads.max")));
        chebiConfig.setSearchStars(env.getProperty("chebi.search.stars"));
        chebiConfig.setMaxRetrievedMolecules(Integer.parseInt(env.getProperty("chebi.results.max")));
        chebiConfig.setCompoundBaseUrl(env.getProperty("chebi.compound.base.url"));
        chebiConfig.setCompoundImgBaseUrl(env.getProperty("chebi.compound.img.base.url"));

        return chebiConfig;
    }

    public Map<String, String> drugbankConfig() {
        Map<String, String> drugbankConfig = new HashMap<>();
        drugbankConfig.put("compound.base.url", env.getProperty("drugbank.compound.base.url"));
        drugbankConfig.put("compound.img.base.url", env.getProperty("drugbank.compound.img.base.url"));

        return drugbankConfig;
    }

    @Bean
    public ChemblConfig chemblConfig() {
        ChemblConfig chemblConfig = new ChemblConfig();

        return chemblConfig;
    }

   
    @Bean
    public LiteratureConfig literatureConfig() {
        LiteratureConfig lc = new LiteratureConfig();
        lc.setMaxThreads(Integer.parseInt(env.getProperty("literature.threads.max")));
        lc.setUseCitexploreWs(Boolean.parseBoolean(env.getProperty("literature.citexplore.ws")));
        lc.setMaxCitations(Integer.parseInt(env.getProperty("literature.results.max")));
        lc.setCitexploreClientPoolSize(Integer.parseInt(env.getProperty("literature.citexplore.client.pool.size")));
        lc.setCitexploreConnectTimeout(Integer.parseInt(env.getProperty("literature.citexplore.ws.timeout.connect")));
        lc.setCitexploreReadTimeout(Integer.parseInt(env.getProperty("literature.citexplore.ws.timeout.read")));
        return lc;
    }

    @Bean
    public DASConfig dasConfig() {
        DASConfig dASConfig = new DASConfig();
        dASConfig.setClientPoolSize(Integer.parseInt(env.getProperty("das.client.pool.size")));
        return dASConfig;
    }

    @Bean
    public Functions functions() {
        Functions functions = new Functions();
        functions.setChebiConfig(chebiConfig());
        functions.setChemblConfig(chemblConfig());
        functions.setDrugbankConfig(drugbankConfig());
        return functions;
    }

    @Bean
    public FilesConfig filesConfig() {
        FilesConfig filesConfig = new FilesConfig();
        filesConfig.setBaseDirectory(env.getProperty("ep.files.base.dir"));
        filesConfig.setSitemapIndex(env.getProperty("sitemap.index"));
        filesConfig.setSitemapUrl(env.getProperty("sitemap.directory"));
        return filesConfig;
    }

}
