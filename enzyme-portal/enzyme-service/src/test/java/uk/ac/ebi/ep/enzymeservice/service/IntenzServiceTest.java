package uk.ac.ebi.ep.enzymeservice.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.ebi.ep.enzymeservice.intenz.dto.EcClass;
import uk.ac.ebi.ep.enzymeservice.intenz.dto.EnzymeHierarchy;
import uk.ac.ebi.ep.enzymeservice.intenz.model.Intenz;
import uk.ac.ebi.ep.enzymeservice.intenz.service.IntenzService;

/**
 *
 * @author joseph
 */
@Slf4j
public class IntenzServiceTest extends EnzymeServiceBaseTest {

    @Autowired
    private IntenzService intenzService;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(intenzService).isNotNull();
    }

    /**
     * Test of getIntenz method, of class IntenzService.
     */
    @Test
    public void testGetIntenz() {
        log.info("getIntenz");
        Collection<String> ecList = Arrays.asList("6.1.1.1", "2.1.1.1");
        List<Intenz> result = intenzService.getIntenz(ecList);

        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(2)));

    }

    /**
     * Test of getIntenz method, of class IntenzService.
     */
    @Test
    public void testGetIntenzIncompleteEC() {
        log.info("testGetIntenzIncompleteEC");
        Collection<String> ecList = Arrays.asList("6.1.1.1", "2.1.1.-");

        List<Intenz> result = intenzService.getIntenz(ecList);

        assertNotNull(result);
        assertThat(result, hasSize(1));

    }

    /**
     * Test of getEnzymeHierarchies method, of class IntenzService.
     */
    @Test
    public void testGetEnzymeHierarchies() {
        log.info("testGetEnzymeHierarchies");
        List<String> ecList = Arrays.asList("6.1.1.1", "2.1.1.1");
        List<EnzymeHierarchy> result = intenzService.getEnzymeHierarchies(ecList);

        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(2)));

    }

    @Test
    public void testGetEnzymeHierarchies_details() {
        log.info("testGetEnzymeHierarchies_details");
        List<String> ecList = Arrays.asList("1.1.1.1");
        List<EnzymeHierarchy> result = intenzService.getEnzymeHierarchies(ecList);

        EcClass e1 = new EcClass();
        e1.setEc("1");
        e1.setName("Oxidoreductases");
        EcClass e2 = new EcClass();
        e2.setEc("1.1");
        e2.setName("Acting on the CH-OH group of donors");
        EcClass e3 = new EcClass();
        e3.setEc("1.1.1");
        e3.setName("With NAD(+) or NADP(+) as acceptor");
        EcClass e4 = new EcClass();
        e4.setEc("1.1.1.1");
        e4.setName("Alcohol dehydrogenase");

        EnzymeHierarchy hierarchy = new EnzymeHierarchy();
        hierarchy.getEcclass().add(e1);
        hierarchy.getEcclass().add(e2);
        hierarchy.getEcclass().add(e3);
        hierarchy.getEcclass().add(e4);

        List<EnzymeHierarchy> expResult = Arrays.asList(hierarchy);

        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));

        EcClass resultEcClass = result.stream().findFirst().get().getEcclass().stream().findFirst().get();
        EcClass expResultEcClass = expResult.stream().findFirst().get().getEcclass().stream().findFirst().get();
        assertEquals(expResultEcClass, resultEcClass);

    }
    
        /**
     * Test of getEnzymeHierarchies method, of class IntenzService.
     */
    @Test
    public void testGetEnzymeHierarchies_incomplete_ec() {
        log.info("testGetEnzymeHierarchies");
        List<String> ecList = Arrays.asList("6.1.-.-");
        List<EnzymeHierarchy> result = intenzService.getEnzymeHierarchies(ecList);
        assertNotNull(result);
        assertThat(result).isEmpty();
        assertThat(result, hasSize(0));

    }

}
