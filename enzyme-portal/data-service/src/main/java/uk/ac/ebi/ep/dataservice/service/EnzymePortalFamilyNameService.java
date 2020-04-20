package uk.ac.ebi.ep.dataservice.service;

import java.util.List;
import uk.ac.ebi.ep.dataservice.dto.ProteinFamily;
import uk.ac.ebi.ep.dataservice.dto.ProteinFamilyView;
import uk.ac.ebi.ep.dataservice.entities.EnzymePortalFamilyName;

/**
 *
 * @author joseph
 */
public interface EnzymePortalFamilyNameService {

    List<ProteinFamilyView> findProteinFamilies();

    List<ProteinFamily> findProteinFamiliesWithNamesLike(String name);

    List<EnzymePortalFamilyName> proteinFamilies();
}
