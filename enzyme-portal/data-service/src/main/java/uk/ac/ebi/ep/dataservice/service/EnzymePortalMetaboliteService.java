package uk.ac.ebi.ep.dataservice.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.ac.ebi.ep.dataservice.dto.Metabolite;
import uk.ac.ebi.ep.dataservice.dto.MetaboliteView;
import uk.ac.ebi.ep.dataservice.entities.EnzymePortalMetabolite;

/**
 *
 * @author joseph
 */
public interface EnzymePortalMetaboliteService {

    List<MetaboliteView> findMetabolites();

    Page<MetaboliteView> findPageableMetabolites(Pageable pageable);

    List<Metabolite> findMetaboliteNameLike(String name);

    List<EnzymePortalMetabolite> metabolites();
}
