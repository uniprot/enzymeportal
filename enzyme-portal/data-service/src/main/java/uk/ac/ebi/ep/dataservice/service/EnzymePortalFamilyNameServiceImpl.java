package uk.ac.ebi.ep.dataservice.service;

import java.util.List;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ep.dataservice.dto.ProteinFamily;
import uk.ac.ebi.ep.dataservice.dto.ProteinFamilyView;
import uk.ac.ebi.ep.dataservice.repositories.EnzymePortalFamilyNameRepository;

/**
 *
 * @author joseph
 */
@Service
class EnzymePortalFamilyNameServiceImpl implements EnzymePortalFamilyNameService {

    private final EnzymePortalFamilyNameRepository enzymePortalFamilyNameRepository;

    public EnzymePortalFamilyNameServiceImpl(EnzymePortalFamilyNameRepository enzymePortalFamilyNameRepository) {
        this.enzymePortalFamilyNameRepository = enzymePortalFamilyNameRepository;
    }

    @Override
    public List<ProteinFamilyView> findProteinFamilies() {
        return enzymePortalFamilyNameRepository.findProteinFamilies();
    }

    @Override
    public List<ProteinFamily> findProteinFamiliesWithNamesLike(String name) {
        return enzymePortalFamilyNameRepository.familyNameLike(name);
    }

}
