package uk.ac.ebi.ep.dataservice.repositories;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;
import uk.ac.ebi.ep.dataservice.dto.Taxonomy;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@NoRepositoryBean
interface EnzymePortalTaxonomyRepositoryCustom {

    List<Taxonomy> getCountForOrganisms(List<Long> taxids);

    List<Taxonomy> getModelOrganisms();

    List<Taxonomy> organismNameLike(String name);

}
