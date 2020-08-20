package uk.ac.ebi.ep.dataservice.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ep.dataservice.dto.Pathway;
import uk.ac.ebi.ep.dataservice.dto.PathwayView;
import uk.ac.ebi.ep.dataservice.repositories.EnzymePortalPathwaysRepository;

/**
 *
 * @author joseph
 */
@Service
class EnzymePortalPathwayServiceImpl implements EnzymePortalPathwayService {

    private final EnzymePortalPathwaysRepository enzymePortalPathwaysRepository;

    @Autowired
    public EnzymePortalPathwayServiceImpl(EnzymePortalPathwaysRepository enzymePortalPathwaysRepository) {
        this.enzymePortalPathwaysRepository = enzymePortalPathwaysRepository;
    }

    @Override
    public List<PathwayView> findPathways() {
        return enzymePortalPathwaysRepository.findPathways();
    }

    @Override
    public List<Pathway> findPathwaysWithNamesLike(String name) {
        return enzymePortalPathwaysRepository.findPathwaysWithNamesLike(name);
    }

    @Override
    public Page<PathwayView> findPageablePathways(Pageable pageable) {
        return enzymePortalPathwaysRepository.getPageablePathwayView(pageable);
    }

}
