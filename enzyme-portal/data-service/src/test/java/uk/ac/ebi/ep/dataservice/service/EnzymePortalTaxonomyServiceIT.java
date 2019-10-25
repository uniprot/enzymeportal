package uk.ac.ebi.ep.dataservice.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.ebi.ep.dataservice.dto.Taxonomy;

/**
 *
 * @author joseph
 */

@Slf4j
public class EnzymePortalTaxonomyServiceIT extends DataServiceBaseIT {

    @Autowired
    private EnzymePortalTaxonomyService enzymePortalTaxonomyService;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(enzymePortalTaxonomyService).isNotNull();
    }

    /**
     * Test of getModelOrganisms method, of class EnzymePortalTaxonomyService.
     */
    @Test
    public void testGetModelOrganisms() {
        log.info("getModelOrganisms");

        List<Taxonomy> result = enzymePortalTaxonomyService.getModelOrganisms();
        assertNotNull(result);
        log.info("Total NUmber of Model Organisms found : " + result.size());
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));

    }

    /**
     * Test of findOrganismsWithNameLike method, of class
     * EnzymePortalTaxonomyService.
     */
    @Test
    public void testFindOrganismsWithNameLike() {
        log.info("findOrganismsWithNameLike");
        String organismName = "homo s";
        String human = "Homo sapiens";
        List<Taxonomy> result = enzymePortalTaxonomyService.findOrganismsWithNameLike(organismName);

        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));
        assertThat(result.stream().findAny().get().getScientificName()).isEqualTo(human);

    }

}
