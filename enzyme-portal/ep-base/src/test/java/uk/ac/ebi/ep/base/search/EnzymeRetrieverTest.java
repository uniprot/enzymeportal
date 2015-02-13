/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.base.search;

import java.util.ArrayList;
import java.util.List;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import uk.ac.ebi.ep.adapter.bioportal.IBioportalAdapter;
import uk.ac.ebi.ep.adapter.literature.ILiteratureAdapter;
import uk.ac.ebi.ep.adapter.literature.LiteratureConfig;
import uk.ac.ebi.ep.data.enzyme.model.ChemicalEntity;
import uk.ac.ebi.ep.data.enzyme.model.CountableMolecules;
import uk.ac.ebi.ep.data.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.data.enzyme.model.EnzymeReaction;
import uk.ac.ebi.ep.data.enzyme.model.Pathway;
import uk.ac.ebi.ep.data.enzyme.model.ProteinStructure;
import uk.ac.ebi.ep.data.enzyme.model.ReactionPathway;
import uk.ac.ebi.ep.data.search.model.Disease;
import uk.ac.ebi.ep.data.service.EnzymePortalService;
import uk.ac.ebi.ep.ebeye.EbeyeService;
import uk.ac.ebi.ep.enzymeservices.chebi.ChebiConfig;
import uk.ac.ebi.ep.enzymeservices.chebi.IChebiAdapter;
import uk.ac.ebi.ep.enzymeservices.intenz.IntenzAdapter;
import uk.ac.ebi.ep.enzymeservices.intenz.IntenzConfig;
import uk.ac.ebi.ep.enzymeservices.reactome.IReactomeAdapter;
import uk.ac.ebi.ep.enzymeservices.reactome.ReactomeConfig;

/**
 *
 * @author joseph
 */
public class EnzymeRetrieverTest extends BaseTest {

    @Autowired
    private EnzymePortalService service;
    private EbeyeService ebeyeService;

    @Autowired
    private Environment env;

    /**
     * Test of getBioportalAdapter method, of class EnzymeRetriever.
     */
    @Test
    public void testGetBioportalAdapter() {
        System.out.println("getBioportalAdapter");

        EnzymeRetriever instance = new EnzymeRetriever(service, ebeyeService);
        IBioportalAdapter result = instance.getBioportalAdapter();
        assertNotNull(result);

    }

    /**
     * Test of getReactomeAdapter method, of class EnzymeRetriever.
     */
    @Test
    public void testGetReactomeAdapter() {
        System.out.println("getReactomeAdapter");
        EnzymeRetriever instance = new EnzymeRetriever(service, ebeyeService);

        IReactomeAdapter result = instance.getReactomeAdapter();
        assertNotNull(result);
    }

    /**
     * Test of getChebiAdapter method, of class EnzymeRetriever.
     */
    @Test
    public void testGetChebiAdapter() {
        System.out.println("getChebiAdapter");
        EnzymeRetriever instance = new EnzymeRetriever(service, ebeyeService);
        IChebiAdapter result = instance.getChebiAdapter();
        assertNotNull(result);
    }

    /**
     * Test of getLiteratureAdapter method, of class EnzymeRetriever.
     */
    @Test
    public void testGetLiteratureAdapter() {
        System.out.println("getLiteratureAdapter");
        EnzymeRetriever instance = new EnzymeRetriever(service, ebeyeService);
        ILiteratureAdapter result = instance.getLiteratureAdapter();
        assertNotNull(result);
    }

    /**
     * Test of getIntenzAdapter method, of class EnzymeRetriever.
     */
    @Test
    public void testGetIntenzAdapter() {
        System.out.println("getIntenzAdapter");
        EnzymeRetriever instance = new EnzymeRetriever(service, ebeyeService);
        IntenzAdapter result = instance.getIntenzAdapter();
        assertNotNull(result);
    }

    /**
     * Test of getEnzyme method, of class EnzymeRetriever.
     */
    @Test
    public void testGetEnzyme() {
        System.out.println("getEnzyme");
        String uniprotAccession = "O76074";

        EnzymeModel expResult = new EnzymeModel();
        expResult.setAccession(uniprotAccession);

        EnzymeRetriever instance = new EnzymeRetriever(service, ebeyeService);
        EnzymeModel result = instance.getEnzyme(uniprotAccession);
        assertEquals(expResult.getAccession(), result.getAccession());
        assertNotNull(result.getName());

    }

    /**
     * Test of getProteinStructure method, of class EnzymeR
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetProteinStructure() throws Exception {
        System.out.println("getProteinStructure");
        String uniprotAccession = "O76074";

        List<ProteinStructure> proteinstructure = new ArrayList<>();
        ProteinStructure structure = new ProteinStructure();
        structure.setId("1xoz");
        structure.setName("Catalytic Domain Of Human Phosphodiesterase 5A In Complex With Tadalafil");
        structure.setDescription("Catalytic Domain Of Human Phosphodiesterase 5A In Complex With Tadalafil");

        proteinstructure.add(structure);

        EnzymeModel expResult = new EnzymeModel();
        expResult.setAccession(uniprotAccession);
        expResult.setProteinstructure(proteinstructure);

        EnzymeRetriever instance = new EnzymeRetriever(service, ebeyeService);
        EnzymeModel result = instance.getProteinStructure(uniprotAccession);

        //result.setProteinstructure(proteinstructure);
        //assertEquals(expResult, result);
        assertNotNull(result);
       // assertEquals(expResult.getProteinstructure().stream().findAny().get().getId(), result.getProteinstructure().stream().findAny().get().getId());

    }

    /**
     * Test of getDiseases method, of class EnzymeRetriever.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetDiseases() throws Exception {
        System.out.println("getDiseases");
        String uniprotAccession = "P49768";

        EnzymeModel expResult = new EnzymeModel();

        Disease disease = new Disease("D000544", "alzheimer disease");

        List<Disease> diseases = new ArrayList<>();
        diseases.add(disease);

        expResult.setDisease(diseases);

        EnzymeRetriever instance = new EnzymeRetriever(service, ebeyeService);
        EnzymeModel result = instance.getDiseases(uniprotAccession);
        assertEquals(expResult.getDisease().stream().findFirst().get(), result.getDisease().stream().findAny().get());

    }

    /**
     * Test of getLiterature method, of class EnzymeRetriever.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetLiterature() throws Exception {
        System.out.println("getLiterature");

        String uniprotAccession = "O76074";
        EnzymeRetriever instance = new EnzymeRetriever(service, ebeyeService);
        instance.getLiteratureAdapter().setConfig(literatureConfig());

        EnzymeModel result = instance.getLiterature(uniprotAccession);

        assertNotNull(result.getLiterature().size());

    }

    /**
     * Test of getWholeModel method, of class EnzymeRetriever.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetWholeModel() throws Exception {
        System.out.println("getWholeModel");
        String acc = "O76074";
        EnzymeRetriever instance = new EnzymeRetriever(service, ebeyeService);

        instance.getIntenzAdapter().setConfig(intenzConfig());
        instance.getReactomeAdapter().setConfig(reactomeConfig());
        instance.getChebiAdapter().setConfig(chebiConfig());

        EnzymeModel result = instance.getWholeModel(acc);
        assertNotNull(result);

    }

    /**
     * Test of getMolecules method, of class EnzymeRetriever.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetMolecules() throws Exception {
        System.out.println("getMolecules");
        String uniprotAccession = "O76074";
        EnzymeRetriever instance = new EnzymeRetriever(service, ebeyeService);
        instance.getChebiAdapter().setConfig(chebiConfig());

        EnzymeModel expResult = new EnzymeModel();

        ChemicalEntity chemicalEntity = new ChemicalEntity();

        CountableMolecules molecules = new CountableMolecules();
        molecules.setTotalFound(1);

        chemicalEntity.setInhibitors(molecules);

        expResult.setMolecule(chemicalEntity);

        EnzymeModel result = instance.getMolecules(uniprotAccession);

        assertEquals(expResult.getMolecule().getInhibitors().getTotalFound(), result.getMolecule().getInhibitors().getTotalFound());

    }

    /**
     * Test of getReactionsPathways method, of class EnzymeRetriever.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetReactionsPathways() throws Exception {
        System.out.println("getReactionsPathways");
        String uniprotAccession = "O76074";
        EnzymeRetriever instance = new EnzymeRetriever(service, ebeyeService);

        EnzymeModel expResult = new EnzymeModel();

        List<ReactionPathway> reactionPathway = new ArrayList<>();

        List<EnzymeReaction> reactions = new ArrayList<>();
        EnzymeReaction reaction = new EnzymeReaction("16960", "3',5'-cyclic GMP + H2O <=> GMP + H(+)");
        reactions.add(reaction);

        List<Pathway> pathways = new ArrayList<>();
        Pathway pathway = new Pathway("REACT_23767 ", "cGMP effects");

        pathways.add(pathway);

        expResult.setPathways(pathways);

        ReactionPathway rp = new ReactionPathway();
        rp.setReactions(reactions);
        rp.setPathways(pathways);

        reactionPathway.add(rp);

        expResult.setReactionpathway(reactionPathway);

        EnzymeModel result = instance.getReactionsPathways(uniprotAccession);

        assertEquals(expResult.getReactionpathway().stream().findAny().get().getReactions().stream().findAny().get().getId(), result.getReactionpathway().stream().findAny().get().getReactions().stream().findAny().get().getId());
        assertEquals(expResult.getPathways().stream().findAny().get().getPathwayId().trim(), result.getPathways().stream().findAny().get().getPathwayId().trim());
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
