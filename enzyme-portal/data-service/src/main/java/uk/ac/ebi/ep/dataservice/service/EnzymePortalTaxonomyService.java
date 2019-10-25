package uk.ac.ebi.ep.dataservice.service;

import java.util.List;
import uk.ac.ebi.ep.dataservice.dto.Taxonomy;

/**
 *
 * @author joseph
 */
public interface EnzymePortalTaxonomyService {

    List<Taxonomy> getModelOrganisms();

    List<Taxonomy> findOrganismsWithNameLike(String organismName);
}
