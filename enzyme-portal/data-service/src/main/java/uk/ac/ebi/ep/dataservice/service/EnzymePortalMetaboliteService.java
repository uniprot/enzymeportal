package uk.ac.ebi.ep.dataservice.service;

import java.util.List;
import uk.ac.ebi.ep.dataservice.dto.Metabolite;
import uk.ac.ebi.ep.dataservice.dto.MetaboliteView;

/**
 *
 * @author joseph
 */
public interface EnzymePortalMetaboliteService {

    List<MetaboliteView> findMetabolites();

    List<Metabolite> findMetaboliteNameLike(String name);
}
