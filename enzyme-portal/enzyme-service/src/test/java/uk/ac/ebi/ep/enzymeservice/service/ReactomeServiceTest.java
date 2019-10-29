package uk.ac.ebi.ep.enzymeservice.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.util.Lists;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import uk.ac.ebi.ep.enzymeservice.reactome.model.ReactomeResult;
import uk.ac.ebi.ep.enzymeservice.reactome.service.ReactomeService;
import uk.ac.ebi.ep.enzymeservice.reactome.view.PathWay;
import uk.ac.ebi.ep.restclient.service.RestConfigService;

/**
 *
 * @author joseph
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ReactomeServiceTest {

    @Autowired
    private ReactomeService reactomeService;

    @Autowired
    private RestConfigService restConfigService;
    @Autowired
    private WebClient webClient;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(reactomeService).isNotNull();
        assertThat(restConfigService).isNotNull();
        assertThat(webClient).isNotNull();
    }

    /**
     * Test of findReactomeResultById method, of class ReactomeService.
     */
    @Test
    public void testFindReactomeResultById() {
        log.info("testFindReactomeResultById");
        String reactomeId = "R-HSA-4086398";

        ReactomeResult result = reactomeService.findReactomeResultById(reactomeId)
                .block();
        assertNotNull(result);
        assertThat(result.getSummation(), hasSize(greaterThanOrEqualTo(1)));

    }

    /**
     * Test of findPathwayById method, of class ReactomeService.
     */
    @Test
    public void testFindPathwayById() {
        log.info("testFindPathwayById");
        String pathwayId = "R-HSA-4086398";

        String expResult = "Ca2+ pathway";
        PathWay result = reactomeService.findPathwayById(pathwayId)
                .block();
        assertNotNull(result);
        assertEquals(expResult, result.getName());

    }

    /**
     * Test of findPathwayById method, of class ReactomeService.
     */
    @Test
    public void testFindPathwayBy_multiple_Ids() {
        log.info("testFindPathwayBy_multiple_Ids");

        List<String> pathwayIds = Lists.list("R-HSA-4086398", "R-HSA-2514859", "R-HSA-2485179");

        PathWay p = new PathWay();
        p.setId("R-HSA-2514859");
        p.setName("Inactivation, recovery and regulation of the phototransduction cascade");
        p.setUrl("https://www.reactome.org/content/detail/R-HSA-2514859");
        p.setImage("image=https://www.reactome.orgnull");
        p.setDescription("To terminate the single photon response and restore the system to its basal state, the three activated intermediates in phototransduction, rhodopsin (MII), transducin alpha subunit with GTP bound (GNAT1-GTP) and phosphodiesterase 6 (PDE6) all need to be efficiently deactivated. In addition, the cGMP concentrations must be restored to support reopening of the CNG channels. This section describes the inactivation and recovery events of the activated intermediates involved in phototransduction (Burns & Pugh 2010, Korenbrot 2012).");

        List<PathWay> pathways = reactomeService.findPathwaysByIds(pathwayIds)
                .block();

        assertNotNull(pathways);
        assertThat(pathways).isNotEmpty();

    }

    @Test
    public void testFindPathways_By_simulated_Ids() {
        log.info("testFindPathwayBy_simulated_Ids");

        List<String> pathwayIds = Stream.generate(() -> "R-HSA-2514859").limit(35)
                .collect(Collectors.toList());

        PathWay p = new PathWay();
        p.setId("R-HSA-2514859");
        p.setName("Inactivation, recovery and regulation of the phototransduction cascade");
        p.setUrl("https://www.reactome.org/content/detail/R-HSA-2514859");
        p.setImage("image=https://www.reactome.orgnull");
        p.setDescription("To terminate the single photon response and restore the system to its basal state, the three activated intermediates in phototransduction, rhodopsin (MII), transducin alpha subunit with GTP bound (GNAT1-GTP) and phosphodiesterase 6 (PDE6) all need to be efficiently deactivated. In addition, the cGMP concentrations must be restored to support reopening of the CNG channels. This section describes the inactivation and recovery events of the activated intermediates involved in phototransduction (Burns & Pugh 2010, Korenbrot 2012).");

        List<PathWay> pathways = reactomeService.findPathwaysByIds(pathwayIds)
                .block();

        assertNotNull(pathways);
        assertThat(pathways, hasSize(greaterThanOrEqualTo(35)));
        pathways.forEach(d -> log.info(" Pathways : " + d + "\n"));

    }

    @Test
    public void testFind_Invalid_PathwayById() {
        log.info("testFind_Invalid_PathwayById");
        String pathwayId = "INVALID-4086398";

        String expResult = null;
        PathWay result = reactomeService.findPathwayById(pathwayId)
                .block();

        assertNull(result);
        assertEquals(expResult, result);

    }

    @Test
    public void testFind_Incorrect_PathwayById() {
        log.info("testFind_Incorrect_PathwayById");

        String pathwayId = "R-4086398";

        String expResult = null;
        PathWay result = reactomeService.findPathwayById(pathwayId)
                .block();

        assertNull(result);
        assertEquals(expResult, result);

    }

    @Test
    public void testFindPathwayById_with_image() {
        log.info("testFindPathwayById_with_image");
        String pathwayId = "R-HSA-71384";
        // pathwayId ="R-HSA-156581";

        String expResult = "https://www.reactome.org/figures/ethanol_detox.jpg";
        PathWay result = reactomeService.findPathwayById(pathwayId)
                .block();

        assertNotNull(result);
        assertEquals(expResult, result.getImage());

    }

//    @Test
//    public void testFindPathwayByPathwayById_ResponseEntity() {
//        log.info("testFindPathwayByPathwayById_ResponseEntity");
//        String pathwayId = "R-HSA-4086398";
//
//        String expResult = "Ca2+ pathway";
//        ResponseEntity<ReactomeResult> result = reactomeService.findReactomeResultByPathwayId(pathwayId);
//        assertNotNull(result);
//        assertNotNull(result.getBody());
//        assertEquals(result.getStatusCode(), HttpStatus.OK);
//        assertEquals(expResult, result.getBody().getDisplayName());
//
//    }
    @Test
    public void testFindPathwayByPathwayById_Mono() {
        log.info("testFindPathwayByPathwayById_Mono");
        String pathwayId = "R-HSA-4086398";

        String expResult = "Ca2+ pathway";
        Mono<PathWay> result = reactomeService.findPathwayById(pathwayId);
        assertNotNull(result);
        result.subscribe(r -> log.info("Mono Result : " + r));
        assertEquals(expResult, result.blockOptional().orElse(new PathWay()).getName());

    }

    @Test
    public void testFindPathwayById_Mono() {
        log.info("testFindPathwayById_Mono");
        String pathwayId = "R-HSA-4086398";

        String expResult = "Ca2+ pathway";

        Mono<PathWay> mono = reactomeService.findPathwayById(pathwayId);
        assertNotNull(mono);
        assertEquals(expResult, mono.blockOptional().orElse(new PathWay()).getName());
        mono.subscribe(p -> log.info("testFindPathwayById_Mono : " + p));

    }

    @Test
    public void testFindPathways_By_simulated_Ids_returns_mono() {
        log.info("testFindPathways_By_simulated_Ids_returns_mono");

        List<String> pathwayIds = Stream.generate(() -> "R-HSA-2514859")
                .limit(20)
                .collect(Collectors.toList());

        Mono<List<PathWay>> pathways = reactomeService.findPathwaysByIds(pathwayIds);

        assertNotNull(pathways);

        pathways.subscribe(s -> log.info("Reactome Result : " + s));

    }

    @Test
    public void testFindPathways_By_simulated_Ids_returns_mono_list() {
        log.info("testFindPathways_By_simulated_Ids_returns_mono_list");

        List<String> pathwayIds = Stream.generate(() -> "R-HSA-2514859")
                .limit(10)
                .collect(Collectors.toList());

        Mono<List<PathWay>> pathways = reactomeService.findPathwaysByIds(pathwayIds);

        assertNotNull(pathways);

        pathways.subscribe(s -> log.info("Reactome Result : " + s));

    }
}
