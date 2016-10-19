/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.controller;

import javax.sql.DataSource;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.projection.ProjectionFactory;
import uk.ac.ebi.ep.base.search.EnzymeRetriever;
import uk.ac.ebi.ep.data.repositories.DiseaseRepository;
import uk.ac.ebi.ep.data.repositories.EnzymeCatalyticActivityRepository;
import uk.ac.ebi.ep.data.repositories.EnzymePortalCompoundRepository;
import uk.ac.ebi.ep.data.repositories.EnzymePortalEcNumbersRepository;
import uk.ac.ebi.ep.data.repositories.EnzymePortalPathwaysRepository;
import uk.ac.ebi.ep.data.repositories.EnzymePortalReactionRepository;
import uk.ac.ebi.ep.data.repositories.EnzymePortalSummaryRepository;
import uk.ac.ebi.ep.data.repositories.EnzymesToTaxonomyRepository;
import uk.ac.ebi.ep.data.repositories.IntenzEnzymesRepository;
import uk.ac.ebi.ep.data.repositories.RelatedProteinsRepository;
import uk.ac.ebi.ep.data.repositories.SpEnzymeEvidenceRepository;
import uk.ac.ebi.ep.data.repositories.TempCompoundCompareRepository;
import uk.ac.ebi.ep.data.repositories.UniprotEntryRepository;
import uk.ac.ebi.ep.data.repositories.UniprotXrefRepository;
import uk.ac.ebi.ep.data.service.AnalysisService;
import uk.ac.ebi.ep.data.service.EnzymePortalService;
import uk.ac.ebi.ep.ebeye.EbeyeRestService;
import uk.ac.ebi.ep.ebeye.EbeyeSuggestionService;
import uk.ac.ebi.ep.ebeye.EnzymeCentricService;
import uk.ac.ebi.ep.enzymeservices.chebi.ChebiAdapter;
import uk.ac.ebi.ep.enzymeservices.intenz.IntenzAdapter;
import uk.ac.ebi.ep.enzymeservices.rhea.RheaWsAdapter;
import uk.ac.ebi.ep.literatureservice.service.LiteratureService;
import uk.ac.ebi.ep.web.utils.SearchUtil;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Configuration
public class WebTestConfig {

    @Bean
    public EnzymePortalService enzymePortalService() {
        return Mockito.mock(EnzymePortalService.class);
    }

    @Bean
    public EnzymeCentricService enzymeCentricService() {
        return Mockito.mock(EnzymeCentricService.class);
    }

    @Bean
    public EbeyeRestService ebeyeRestService() {
        return Mockito.mock(EbeyeRestService.class);
    }

    @Bean
    public EbeyeSuggestionService ebeyeSuggestionService() {
        return Mockito.mock(EbeyeSuggestionService.class);
    }

    @Bean
    public LiteratureService literatureService() {
        return Mockito.mock(LiteratureService.class);
    }

   
    @Bean
    public BasketController basketController() {
        return Mockito.mock(BasketController.class);
    }

    @Bean
    public AbstractController abstractController() {
        return Mockito.mock(AbstractController.class);
    }

    @Bean
    public BrowseDiseasesController browseDiseasesController() {
        return Mockito.mock(BrowseDiseasesController.class);
    }

    @Bean
    public BrowseEnzymesController browseEnzymesController() {
        return Mockito.mock(BrowseEnzymesController.class);
    }

    @Bean
    public BrowsePathwaysController browsePathwaysController() {
        return Mockito.mock(BrowsePathwaysController.class);
    }

    @Bean
    public BrowseTaxonomyController browseTaxonomyController() {
        return Mockito.mock(BrowseTaxonomyController.class);
    }

    @Bean
    public ControllerAdvisor controllerAdvisor() {
        return Mockito.mock(ControllerAdvisor.class);
    }

    @Bean
    public SearchController searchController() {
        return Mockito.mock(SearchController.class);
    }

    @Bean
    public StatusController statusController() {
        return Mockito.mock(StatusController.class);
    }

    @Bean
    public SpEnzymeEvidenceRepository spEnzymeEvidenceRepository() {
        return Mockito.mock(SpEnzymeEvidenceRepository.class);
    }

    @Bean
    public AnalysisService analysisService() {
        return Mockito.mock(AnalysisService.class);
    }

    @Bean
    public DiseaseRepository diseaseRepository() {
        return Mockito.mock(DiseaseRepository.class);
    }

    @Bean
    public UniprotXrefRepository uniprotXrefRepository() {
        return Mockito.mock(UniprotXrefRepository.class);
    }

    @Bean
    public UniprotEntryRepository uniprotEntryRepository() {
        return Mockito.mock(UniprotEntryRepository.class);
    }

    @Bean
    public EnzymePortalCompoundRepository enzymePortalCompoundRepository() {
        return Mockito.mock(EnzymePortalCompoundRepository.class);
    }

    @Bean
    public TempCompoundCompareRepository tempCompoundCompareRepository() {
        return Mockito.mock(TempCompoundCompareRepository.class);
    }

    @Bean
    public EnzymePortalPathwaysRepository enzymePortalPathwaysRepository() {
        return Mockito.mock(EnzymePortalPathwaysRepository.class);
    }

    @Bean
    public EnzymePortalReactionRepository enzymePortalReactionRepository() {
        return Mockito.mock(EnzymePortalReactionRepository.class);
    }

    @Bean
    public RelatedProteinsRepository relatedProteinsRepository() {
        return Mockito.mock(RelatedProteinsRepository.class);
    }

    @Bean
    public EnzymePortalEcNumbersRepository ecNumbersRepository() {
        return Mockito.mock(EnzymePortalEcNumbersRepository.class);
    }

    @Bean
    public EnzymeCatalyticActivityRepository catalyticActivityRepository() {
        return Mockito.mock(EnzymeCatalyticActivityRepository.class);
    }

    @Bean
    public EnzymesToTaxonomyRepository enzymesToTaxonomyRepository() {
        return Mockito.mock(EnzymesToTaxonomyRepository.class);
    }

    @Bean
    public ProjectionFactory projectionFactory() {
        return Mockito.mock(ProjectionFactory.class);
    }

    @Bean
    public EnzymePortalSummaryRepository enzymePortalSummaryRepository() {
        return Mockito.mock(EnzymePortalSummaryRepository.class);
    }

    @Bean
    public IntenzEnzymesRepository intenzEnzymesRepository() {
        return Mockito.mock(IntenzEnzymesRepository.class);
    }

    @Bean
    public DataSource dataSource() {
        return Mockito.mock(DataSource.class);
    }

    @Bean
    public RheaWsAdapter rheaAdapter() {
        return Mockito.mock(RheaWsAdapter.class);
    }

    @Bean
    public EnzymeRetriever enzymeRetriever() {
        return Mockito.mock(EnzymeRetriever.class);
    }

    @Bean
    public ChebiAdapter chebiAdapter() {
        return Mockito.mock(ChebiAdapter.class);
    }

    @Bean
    public IntenzAdapter intenzAdapter() {
        return Mockito.mock(IntenzAdapter.class);
    }
    
        @Bean
    public SearchUtil searchUtil(){
        return Mockito.mock(SearchUtil.class);
    }

}
