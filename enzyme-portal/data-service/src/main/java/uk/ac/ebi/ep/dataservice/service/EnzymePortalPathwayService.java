package uk.ac.ebi.ep.dataservice.service;

import java.util.List;
import uk.ac.ebi.ep.dataservice.dto.Pathway;
import uk.ac.ebi.ep.dataservice.dto.PathwayView;

/**
 *
 * @author joseph
 */
public interface EnzymePortalPathwayService {

    List<PathwayView> findPathways();

    List<Pathway> findPathwaysWithNamesLike(String name);
}
