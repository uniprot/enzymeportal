package uk.ac.ebi.ep.dataservice.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.ac.ebi.ep.dataservice.dto.Pathway;
import uk.ac.ebi.ep.dataservice.dto.PathwayView;

/**
 *
 * @author joseph
 */
public interface EnzymePortalPathwayService {

    List<PathwayView> findPathways();

    Page<PathwayView> findPageablePathways(Pageable pageable);

    List<Pathway> findPathwaysWithNamesLike(String name);
}
