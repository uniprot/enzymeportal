package uk.ac.ebi.ep.dataservice.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.dataservice.dto.Taxonomy;
import uk.ac.ebi.ep.dataservice.repositories.EnzymePortalTaxonomyRepository;

/**
 *
 * @author joseph
 */
@Service
public class EnzymePortalTaxonomyServiceImpl implements EnzymePortalTaxonomyService {

    private final EnzymePortalTaxonomyRepository enzymePortalTaxonomyRepository;

    @Autowired
    public EnzymePortalTaxonomyServiceImpl(EnzymePortalTaxonomyRepository enzymePortalTaxonomyRepository) {
        this.enzymePortalTaxonomyRepository = enzymePortalTaxonomyRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Taxonomy> getModelOrganisms() {
        return enzymePortalTaxonomyRepository.getModelOrganisms();

    }

    @Transactional(readOnly = true)
    @Override
    public List<Taxonomy> findOrganismsWithNameLike(String organismName) {
        return enzymePortalTaxonomyRepository.organismNameLike(organismName);

    }

}
