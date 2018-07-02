
package uk.ac.ebi.ep.web.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import uk.ac.ebi.ep.adapter.chembl.ChemblConfig;
import uk.ac.ebi.ep.base.search.EnzymeRetriever;
import uk.ac.ebi.ep.common.Config;
import uk.ac.ebi.ep.enzymeservices.chebi.ChebiAdapter;
import uk.ac.ebi.ep.enzymeservices.chebi.ChebiConfig;
import uk.ac.ebi.ep.enzymeservices.intenz.IntenzAdapter;
import uk.ac.ebi.ep.enzymeservices.intenz.IntenzConfig;
import uk.ac.ebi.ep.enzymeservices.reactome.ReactomeConfig;
import uk.ac.ebi.ep.enzymeservices.rhea.RheaWsAdapter;
import uk.ac.ebi.ep.functions.Functions;
import uk.ac.ebi.ep.mBean.FilesConfig;
import uk.ac.ebi.ep.pdbeadapter.PDBeRestService;
import uk.ac.ebi.ep.pdbeadapter.PdbService;
import uk.ac.ebi.ep.pdbeadapter.config.PDBeUrl;
import uk.ac.ebi.ep.web.utils.SearchUtil;

/**
 *
 * @author joseph
 */
@Configuration
@PropertySources({
    @PropertySource(value = "classpath:log4j.properties", ignoreResourceNotFound = true),
    @PropertySource("classpath:ep-web-client.properties"),
    @PropertySource("classpath:chembl-adapter.properties")

})
public class EnzymePortalConfig {

    @Autowired
    private Environment env;

    //maybe not be needed anymore as we now use spting 4 +
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }

    @Bean
    public String uniprotAlignUrl() {
        String alignUrl = env.getProperty("uniprot.align.url");
        return alignUrl;
    }

    @Bean
    public String pdbImgUrl() {
        String pdbImgUrl = env.getProperty("pdb.structure.img.url");
        return pdbImgUrl;
    }

    @Bean
    public String pdbStructureCompareUrl() {
        String pdbStructureCompareUrl = env.getProperty("pdb.structure.compare.url");
        return pdbStructureCompareUrl;
    }

    @Bean
    public IntenzConfig intenzConfig() {
        IntenzConfig config = new IntenzConfig();

        config.setTimeout(Integer.parseInt(env.getProperty("intenz.ws.timeout")));
        config.setIntenzXmlUrl(env.getProperty("intenz.xml.url"));
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
    public RheaWsAdapter rheaAdapter() {
        return new RheaWsAdapter();
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
        rc.setEventBaseUrl("https://www.reactome.org/content/detail/");

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

    @Bean
    public ChebiAdapter chebiAdapter() {
        ChebiAdapter chebiAdapter = new ChebiAdapter();
        chebiAdapter.setConfig(chebiConfig());
        return chebiAdapter;
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
    public Functions functions() {
        Functions functions = new Functions();
        Functions.setChebiConfig(chebiConfig());
        Functions.setChemblConfig(chemblConfig());
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
    public SpelAwareProxyProjectionFactory projectionFactory() {
        return new SpelAwareProxyProjectionFactory();
    }

    @Bean
    public EnzymeRetriever enzymeRetriever() {
        return new EnzymeRetriever();
    }

    @Bean
    public SearchUtil searchUtil() {
        return new SearchUtil();
    }

}
