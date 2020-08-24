package uk.ac.ebi.ep.restapi.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ep.dataservice.dto.DiseaseView;
import uk.ac.ebi.ep.dataservice.dto.MetaboliteView;
import uk.ac.ebi.ep.dataservice.dto.PathwayView;
import uk.ac.ebi.ep.dataservice.entities.EnzymePortalCofactor;
import uk.ac.ebi.ep.dataservice.service.DiseaseService;
import uk.ac.ebi.ep.dataservice.service.EnzymePortalCofactorService;
import uk.ac.ebi.ep.dataservice.service.EnzymePortalMetaboliteService;
import uk.ac.ebi.ep.dataservice.service.EnzymePortalPathwayService;
import uk.ac.ebi.ep.indexservice.helper.IndexQueryType;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeEntry;
import uk.ac.ebi.ep.restapi.exceptions.ResourceNotFoundException;

/**
 *
 * @author joseph
 */
@Service
class ResourceServiceImpl implements ResourceService {

    private final EnzymePortalCofactorService enzymePortalCofactorService;
    private final DiseaseService diseaseService;
    private final EnzymePortalMetaboliteService enzymePortalMetaboliteService;
    private final EnzymePortalPathwayService enzymePortalPathwayService;
    private final SearchIndexService searchIndexService;

    @Autowired
    public ResourceServiceImpl(EnzymePortalCofactorService enzymePortalCofactorService, DiseaseService diseaseService, EnzymePortalMetaboliteService enzymePortalMetaboliteService, EnzymePortalPathwayService enzymePortalPathwayService, SearchIndexService searchIndexService) {
        this.enzymePortalCofactorService = enzymePortalCofactorService;
        this.diseaseService = diseaseService;
        this.enzymePortalMetaboliteService = enzymePortalMetaboliteService;
        this.enzymePortalPathwayService = enzymePortalPathwayService;
        this.searchIndexService = searchIndexService;
    }

    @Override
    public Page<DiseaseView> diseases(Pageable pageable) {
        return diseaseService.getPageableDiseases(pageable);
    }

    @Override
    public Page<EnzymePortalCofactor> cofactors(Pageable pageable) {
        return enzymePortalCofactorService.cofactors(pageable);

    }

    @Override
    public Page<MetaboliteView> metabolites(Pageable pageable) {
        return enzymePortalMetaboliteService.findPageableMetabolites(pageable);

    }

    @Override
    public Page<PathwayView> pathways(Pageable pageable) {
        return enzymePortalPathwayService.findPageablePathways(pageable);

    }

    @Override
    public int getEnymesHitCount(String query) {
        return searchIndexService.getEnzymeHitCount(query);
    }

    @Override
    public Page<EnzymeEntry> findEnzymesByResourceId(String query, String resourceId, IndexQueryType resourceQueryType, Pageable pageable) {

        int hitCount = getEnymesHitCount(query);

        if (hitCount == 0) {
            throw new ResourceNotFoundException(String.format("No result found for Id=%s", resourceId));

        }

        List<EnzymeEntry> enzymes = searchIndexService.getEnzymes(query, resourceId, resourceQueryType, pageable.getPageNumber(), pageable.getPageSize())
                .collectList()
                .block();
        return new PageImpl(enzymes, pageable, hitCount);

    }

}
