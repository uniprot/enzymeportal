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
import uk.ac.ebi.ep.dataservice.entities.EnzymePortalCofactor;
import uk.ac.ebi.ep.dataservice.dto.Cofactor;

/**
 *
 * @author joseph
 */

@Slf4j
public class EnzymePortalCofactorServiceIT extends DataServiceBaseIT {

    @Autowired
    private EnzymePortalCofactorService enzymePortalCofactorService;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(enzymePortalCofactorService).isNotNull();
    }

    /**
     * Test of cofactors method, of class EnzymePortalCofactorService.
     */
    @Test
    public void testCofactors() {
        log.info("cofactors");

        List<EnzymePortalCofactor> result = enzymePortalCofactorService.cofactors();
        assertNotNull(result);
        log.info("Total NUmber of Cofactors found : " + result.size());
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));

    }

    /**
     * Test of findCofactorsLike method, of class EnzymePortalCofactorService.
     */
    @Test
    public void testFindCofactorsLike() {
        log.info("findCofactorsLike");
        String cofactorName = "glutat";
        String cofactor = "glutathione";
        List<Cofactor> result = enzymePortalCofactorService.findCofactorsLike(cofactorName);

        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));
        assertThat(result.stream().findAny().get().getCompoundName()).isEqualTo(cofactor);

    }

}
