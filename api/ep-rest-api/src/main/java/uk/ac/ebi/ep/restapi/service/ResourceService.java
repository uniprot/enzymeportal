package uk.ac.ebi.ep.restapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.ac.ebi.ep.dataservice.dto.DiseaseView;
import uk.ac.ebi.ep.dataservice.dto.MetaboliteView;
import uk.ac.ebi.ep.dataservice.dto.PathwayView;
import uk.ac.ebi.ep.dataservice.entities.EnzymePortalCofactor;
import uk.ac.ebi.ep.indexservice.helper.IndexQueryType;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeEntry;

/**
 *
 * @author joseph
 */
public interface ResourceService {

    Page<DiseaseView> diseases(Pageable pageable);

    Page<EnzymePortalCofactor> cofactors(Pageable pageable);

    Page<MetaboliteView> metabolites(Pageable pageable);

    Page<PathwayView> pathways(Pageable pageable);

    int getEnymesHitCount(String query);

    Page<EnzymeEntry> findEnzymesByResourceId(String query, String resourceId, IndexQueryType resourceQueryType, Pageable pageable);

}
