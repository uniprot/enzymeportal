package uk.ac.ebi.ep.metaboliteService.service;

import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.ebi.ep.metaboliteService.MetaboliteServiceApplicationTests;
import uk.ac.ebi.ep.metaboliteService.model.Metabolite;

/**
 *
 * @author joseph
 */
@Slf4j
public class MetabolightServiceImplIT extends MetaboliteServiceApplicationTests {

    @Autowired
    private MetabolightService metabolightService;

    @org.junit.jupiter.api.Test
    public void injectedComponentsAreNotNull() {
        log.info("injectedComponentsAreNotNull");
        assertThat(metabolightService).isNotNull();

    }

    /**
     * Test of isMetabolite method, of class MetabolightServiceImpl.
     */
    @Test
    public void testGetMetabolite() {
        log.info("testGetMetabolite");
        String chebiId = "CHEBI:16345";
        Metabolite metabolite = metabolightService.getMetabolite(chebiId);
   
        assertThat(metabolite).isNotNull();
        chebiId = "FAKE_ID:1234";
        metabolite = metabolightService.getMetabolite(chebiId);

        assertThat(metabolite.getMetabolightsId()).isNull();

    }

    /**
     * Test of isMetabolite method, of class MetabolightServiceImpl.
     */
    @Test
    public void testIsMetabolite() {
        log.info("testIsMetabolite");
        String chebiId = "CHEBI:16345";
        boolean isMetabolite = metabolightService.isMetabolite(chebiId);
        assertThat(isMetabolite).isTrue();
        chebiId = "FAKE_ID:1234";
        isMetabolite = metabolightService.isMetabolite(chebiId);
        assertThat(isMetabolite).isFalse();

    }

}
