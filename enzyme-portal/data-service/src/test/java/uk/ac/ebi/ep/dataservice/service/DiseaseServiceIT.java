package uk.ac.ebi.ep.dataservice.service;

import java.util.List;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.ebi.ep.dataservice.dto.Disease;
import uk.ac.ebi.ep.dataservice.dto.DiseaseView;

/**
 *
 * @author joseph
 */

@Slf4j
public class DiseaseServiceIT extends DataServiceBaseIT{

    @Autowired
    private DiseaseService diseaseService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private EntityManager entityManager;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(dataSource).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(diseaseService).isNotNull();
    }

    /**
     * Test of findDiseasesView method, of class DiseaseRepository.
     */
    @Test
    public void testFindDiseases() {
        log.info("testFindDiseasesView");

        List<DiseaseView> result = diseaseService.findDiseases();
        assertNotNull(result);
        log.info("Total NUmber of diseases found : " + result.size());
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));

    }

    @Test
    public void testFindDiseasesNameLike() {
        log.info("testFindDiseasesNameLike");

        String name = "cancer";

        List<Disease> result = diseaseService.findDiseasesNameLike(name);
        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));

    }

}
