package uk.ac.ebi.ep.dataservice.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ep.dataservice.dto.Cofactor;
import uk.ac.ebi.ep.dataservice.entities.EnzymePortalCofactor;
import uk.ac.ebi.ep.dataservice.repositories.EnzymePortalCofactorRepository;

/**
 *
 * @author joseph
 */
@Service
public class EnzymePortalCofactorServiceImpl implements EnzymePortalCofactorService {

    private final EnzymePortalCofactorRepository enzymePortalCofactorRepository;

    @Autowired
    public EnzymePortalCofactorServiceImpl(EnzymePortalCofactorRepository enzymePortalCofactorRepository) {
        this.enzymePortalCofactorRepository = enzymePortalCofactorRepository;
    }

    @Override
    public List<EnzymePortalCofactor> cofactors() {
        return enzymePortalCofactorRepository.cofactors();
    }

    @Override
    public List<Cofactor> findCofactorsLike(String cofactorName) {
        return enzymePortalCofactorRepository.cofactorsNameLike(cofactorName);
    }

    @Override
    public Page<EnzymePortalCofactor> cofactors(Pageable pageable) {
        return enzymePortalCofactorRepository.getPageableCofactors(pageable);
    }

}
