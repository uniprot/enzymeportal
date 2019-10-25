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
import uk.ac.ebi.ep.dataservice.dto.Pathway;
import uk.ac.ebi.ep.dataservice.dto.PathwayView;

/**
 *
 * @author joseph
 */
@Slf4j
public class EnzymePortalPathwayServiceIT extends DataServiceBaseIT {

    @Autowired
    private EnzymePortalPathwayService enzymePortalPathwayService;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(enzymePortalPathwayService).isNotNull();
    }

    /**
     * Test of findPathways method, of class EnzymePortalPathwayService.
     */
    @Test
    public void testFindPathways() {
        log.info("findPathways");

        List<PathwayView> result = enzymePortalPathwayService.findPathways();
        assertNotNull(result);
        log.info("Total NUmber of Pathways found : " + result.size());
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));

    }

    /**
     * Test of findPathwaysWithNamesLike method, of class
     * EnzymePortalPathwayService.
     */
    @Test
    public void testFindPathwaysWithNamesLike() {
        log.info("findPathwaysWithNamesLike");
        String name = "xeno";
        String pathwayName = "Xenobiotics";

        List<Pathway> result = enzymePortalPathwayService.findPathwaysWithNamesLike(name);
 
        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));
        assertThat(result.stream().findAny().get().getPathwayName()).isEqualTo(pathwayName);

    }

}
