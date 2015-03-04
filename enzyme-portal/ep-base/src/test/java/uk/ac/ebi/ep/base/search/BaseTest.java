/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.base.search;

import javax.sql.DataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ep.adapter.literature.LiteratureConfig;
import uk.ac.ebi.ep.data.dataconfig.GlobalConfig;
import uk.ac.ebi.ep.data.service.EnzymePortalService;
import uk.ac.ebi.ep.data.testConfig.SpringDataMockConfig;
import uk.ac.ebi.ep.ebeye.EbeyeRestService;
import uk.ac.ebi.ep.ebeye.config.EbeyeConfig;
import uk.ac.ebi.ep.enzymeservices.chebi.ChebiConfig;
import uk.ac.ebi.ep.enzymeservices.intenz.IntenzAdapter;
import uk.ac.ebi.ep.enzymeservices.intenz.IntenzConfig;
import uk.ac.ebi.ep.enzymeservices.reactome.ReactomeConfig;

/**
 *
 * @author joseph
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringDataMockConfig.class, GlobalConfig.class, EbeyeConfig.class, EbeyeRestService.class})
public abstract class BaseTest {

//RestTemplate restTemplate = new RestTemplate();
// MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);
    @Autowired
    protected EnzymePortalService service;
    @Autowired
    protected EbeyeRestService ebeyeService;

  
    @Autowired
    protected Environment env;

    @Autowired
    protected DataSource dataSource;
    
    
        protected EnzymeRetriever instance;

    @Before
    public void setUp() {
        instance = new EnzymeRetriever(service, ebeyeService);
    }

//    @Before
//    //@Override
//    public void setUp() {
//
////        context = new AnnotationConfigApplicationContext();
////        context.getEnvironment().setActiveProfiles("uzpdev");
////        context.register(SpringDataMockConfig.class,GlobalConfig.class, EbeyeRestService.class);
////        context.scan("uk.ac.ebi.ep.data.testConfig","uk.ac.ebi.ep.ebeye");
////        context.refresh();
////
////        //service = context.getBean(EnzymePortalService.class);
////        ebeyeService = context.getBean(EbeyeRestService.class);
////        env = context.getEnvironment();
//    }

    //@Override
    @After
    public void tearDown() throws Exception {
        //context.close();
        dataSource.getConnection().close();

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
}
