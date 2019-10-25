package uk.ac.ebi.ep.dataservice.repositories;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.ac.ebi.ep.dataservice.dto.Disease;
import uk.ac.ebi.ep.dataservice.dto.DiseaseView;
import uk.ac.ebi.ep.dataservice.entities.EnzymePortalDisease;
import uk.ac.ebi.ep.dataservice.entities.UniprotEntry;

/**
 *
 * @author joseph
 */

@Slf4j
@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class DiseaseRepositoryTest {

    @Autowired
    private DiseaseRepository diseaseRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    private void prepareTestData() {
        testEntityManager.flush();
        UniprotEntry entry = new UniprotEntry("P1234");
        testEntityManager.persist(entry);

        EnzymePortalDisease d1 = new EnzymePortalDisease();
        d1.setDiseaseId(1L);
        d1.setOmimNumber("OMIM123");
        d1.setDiseaseName("Breast cancer");
        d1.setUniprotAccession(entry);

        EnzymePortalDisease d2 = new EnzymePortalDisease();
        d2.setDiseaseId(2L);
        d2.setOmimNumber("OMIM456");
        d2.setDiseaseName("Bowel cancer");
        d2.setUniprotAccession(entry);

        EnzymePortalDisease d3 = new EnzymePortalDisease();
        d3.setDiseaseId(3L);
        d3.setOmimNumber("OMIM777");
        d3.setDiseaseName("schizophrenia");
        d3.setUniprotAccession(entry);

        testEntityManager.persistAndFlush(d1);
        testEntityManager.persistAndFlush(d2);
        testEntityManager.persistAndFlush(d3);
    }

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(testEntityManager).isNotNull();
        assertThat(diseaseRepository).isNotNull();
    }

    @Test
    public void testFindDiseasesView() {
        log.info("testFindDiseasesView");

        prepareTestData();

        List<DiseaseView> result = diseaseRepository.findDiseasesView();

        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));

    }

    @Test
    public void testFindDiseasesNameLike() {
        log.info("testFindDiseasesNameLike");
        prepareTestData();

        String name = "cancer";
        String diseaseName = "Breast cancer";
        List<Disease> result = diseaseRepository.findDiseasesNameLike(name);

        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));
        assertThat(result.stream().findAny().get().getName()).isEqualTo(diseaseName);

    }

}
