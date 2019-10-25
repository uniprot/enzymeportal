package uk.ac.ebi.ep.dataservice.repositories;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;
import uk.ac.ebi.ep.dataservice.dto.ProteinFamily;

/**
 *
 * @author Joseph
 */
@NoRepositoryBean
public interface EnzymePortalFamilyNameRepositoryCustom {

    List<ProteinFamily> familyNameLike(String name);
}
