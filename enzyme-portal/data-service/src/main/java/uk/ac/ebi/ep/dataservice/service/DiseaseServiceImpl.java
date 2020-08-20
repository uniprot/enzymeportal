package uk.ac.ebi.ep.dataservice.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ep.dataservice.dto.Disease;
import uk.ac.ebi.ep.dataservice.dto.DiseaseView;
import uk.ac.ebi.ep.dataservice.repositories.DiseaseRepository;

/**
 *
 * @author joseph
 */
@Service
class DiseaseServiceImpl implements DiseaseService {

    private final DiseaseRepository diseaseRepository;

    @Autowired
    public DiseaseServiceImpl(DiseaseRepository diseaseRepository) {
        this.diseaseRepository = diseaseRepository;
    }

    @Override
    public List<Disease> findDiseasesNameLike(String name) {
        return diseaseRepository.findDiseasesNameLike(name);
    }

    @Override
    public List<DiseaseView> findDiseases() {
        return diseaseRepository.findDiseasesView();
    }

    @Override
    public Page<DiseaseView> getPageableDiseases(Pageable pageable) {
        return diseaseRepository.getPageableDiseasesView(pageable);
    }
}
