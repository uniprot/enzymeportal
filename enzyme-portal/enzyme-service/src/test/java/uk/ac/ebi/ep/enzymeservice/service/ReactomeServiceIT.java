package uk.ac.ebi.ep.enzymeservice.service;

import java.time.Duration;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
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
public class ReactomeServiceIT {

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

        Mono<ReactomeResult> result = reactomeService.findReactomeResultById(reactomeId);
        StepVerifier
                .create(result)
                .assertNext(res -> {
                    assertNotNull(res);
                    assertEquals(reactomeId, res.getStId());
                    assertThat(res.getSummation(), hasSize(greaterThanOrEqualTo(1)));

                })
                .verifyComplete();
    }

    /**
     * Test of findPathwayById method, of class ReactomeService.
     */
    @Test
    public void testFindPathwayById() {
        log.info("testFindPathwayById");
        String pathwayId = "R-HSA-4086398";

        String expResult = "Ca2+ pathway";
        Mono<PathWay> result = reactomeService.findPathwayById(pathwayId);

        StepVerifier
                .create(result)
                .assertNext(res -> {
                    assertNotNull(res);
                    assertEquals(pathwayId, res.getId());
                    assertEquals(expResult, res.getName());
                })
                .verifyComplete();

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
        p.setImage(null);
        p.setDescription("To terminate the single photon response and restore the system to its basal state, the three activated intermediates in phototransduction, rhodopsin (MII), transducin alpha subunit with GTP bound (GNAT1-GTP) and phosphodiesterase 6 (PDE6) all need to be efficiently deactivated. In addition, the cGMP concentrations must be restored to support reopening of the CNG channels. This section describes the inactivation and recovery events of the activated intermediates involved in phototransduction (Burns & Pugh 2010, Korenbrot 2012).");

        Mono<List<PathWay>> pathways = reactomeService.findPathwaysByIds(pathwayIds);
        StepVerifier
                .create(pathways)
                .assertNext(res -> {
                    assertNotNull(res);
                    assertThat(res).isNotEmpty();
                    assertThat(res).containsAnyOf(p);

                })
                .verifyComplete();

    }

    @Test
    public void testFindPathwayBy_multiple_Ids2() {
        log.info("testFindPathwayBy_multiple_Ids");

        List<String> pathwayIds = Lists.list("R-HSA-71384", "R-HSA-5365859", "R-HSA-2161541");

        PathWay p = new PathWay();
        p.setId("R-HSA-5365859");
        p.setName("RA biosynthesis pathway");
        p.setUrl("https://www.reactome.org/content/detail/R-HSA-5365859");
        p.setImage(null);
        p.setDescription("The major activated retinoid, all-trans-retinoic acid (atRA) is produced by the dehydrogenation of all-trans-retinol (atROL) by members of the short chain "
        		+ "dehydrogenase/reductase (SDR) and aldehyde dehydrogenase (RALDH) gene families (Das et al. 2014, Napoli 2012).");

        Mono<List<PathWay>> pathways = reactomeService.findPathwaysByIds(pathwayIds);
        StepVerifier
                .create(pathways)
                .assertNext(res -> {
                    assertNotNull(res);
                    assertThat(res).isNotEmpty();
                    assertThat(res).containsAnyOf(p);

                })
                .verifyComplete();

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
        p.setImage(null);
        p.setDescription("To terminate the single photon response and restore the system to its basal state, the three activated intermediates in phototransduction, rhodopsin (MII), transducin alpha subunit with GTP bound (GNAT1-GTP) and phosphodiesterase 6 (PDE6) all need to be efficiently deactivated. In addition, the cGMP concentrations must be restored to support reopening of the CNG channels. This section describes the inactivation and recovery events of the activated intermediates involved in phototransduction (Burns & Pugh 2010, Korenbrot 2012).");

        Mono<List<PathWay>> pathways = reactomeService.findPathwaysByIds(pathwayIds);

        StepVerifier
                .create(pathways)
                .assertNext(res -> {
                    assertNotNull(res);
                    assertThat(res).isNotEmpty();
                    assertThat(res, hasSize(greaterThanOrEqualTo(10)));
                    assertThat(res).containsAnyOf(p);
                    res.forEach(d -> log.info(" Pathways : " + d + "\n"));

                })
                .verifyComplete();
    }

    @Test
    public void testFind_Invalid_PathwayById() {
        log.info("testFind_Invalid_PathwayById");
        String pathwayId = "INVALID-4086398";

        Mono<PathWay> result = reactomeService.findPathwayById(pathwayId);

        StepVerifier
                .create(result)
                .expectError()
                .verify();

    }

    @Test
    public void testFind_Incorrect_PathwayById() {
        log.info("testFind_Incorrect_PathwayById");

        String pathwayId = "R-4086398";

        Mono<PathWay> result = reactomeService.findPathwayById(pathwayId);

        StepVerifier
                .create(result)
                .expectError()
                .verify();

    }

    @Test
    public void testFindPathwayById_with_image() {
        log.info("testFindPathwayById_with_image");
        String pathwayId = "R-HSA-71384";
        // pathwayId ="R-HSA-156581";

        String expResult = "https://www.reactome.org/figures/ethanol_detox.jpg";
        Mono<PathWay> result = reactomeService.findPathwayById(pathwayId);


        StepVerifier
                .create(result)
                .assertNext(res -> {
                    assertNotNull(res);
                    assertThat(res.getImage()).isEqualTo(expResult);

                })
                .verifyComplete();

    }

    @Test
    public void testFindPathwayByPathwayById_Mono() {
        log.info("testFindPathwayByPathwayById_Mono");
        String pathwayId = "R-HSA-4086398";

        String expResult = "Ca2+ pathway";
        Mono<PathWay> result = reactomeService.findPathwayById(pathwayId);
        assertNotNull(result);

        StepVerifier
                .create(result)
                .assertNext(res -> {
                    assertNotNull(res);
                    assertThat(res.getName()).isEqualTo(expResult);

                })
                .verifyComplete();
    }

    @Test
    public void testFindPathwayById_Mono() {
        log.info("testFindPathwayById_Mono");
        String pathwayId = "R-HSA-4086398";

        String expResult = "Ca2+ pathway";

        Mono<PathWay> result = reactomeService.findPathwayById(pathwayId);
        assertNotNull(result);

        StepVerifier
                .create(result)
                .assertNext(res -> {
                    assertNotNull(res);
                    assertThat(res.getName()).isEqualTo(expResult);

                })
                .verifyComplete();

    }

    @Test
    public void testFindPathways_By_simulated_Ids_returns_mono() {
        log.info("testFindPathways_By_simulated_Ids_returns_mono");

        List<String> pathwayIds = Stream.generate(() -> "R-HSA-2514859")
                .limit(20)
                .collect(Collectors.toList());

        Mono<List<PathWay>> pathways = reactomeService.findPathwaysByIds(pathwayIds);

        assertNotNull(pathways);
        StepVerifier
                .create(pathways)
                .assertNext(res -> {
                    assertNotNull(res);
                    assertThat(res).isNotEmpty();
                    assertThat(res, hasSize(greaterThanOrEqualTo(10)));
                    res.forEach(d -> log.info(" Reactome Result : " + d + "\n"));

                })
                .verifyComplete();

    }

    @Test
    public void testFindPathways_By_simulated_Ids_returns_flux() {
        log.info("testFindPathways_By_simulated_Ids_returns_flux");

        List<String> pathwayIds = Stream.generate(() -> "R-HSA-2514859")
                .limit(20)
                .collect(Collectors.toList());

        Flux<PathWay> pathways = reactomeService.pathwaysByPathwayIds(pathwayIds);

        assertNotNull(pathways);

        StepVerifier
                .create(pathways)
                .assertNext(res -> {
                    assertNotNull(res);

                })
                .expectNextCount(19)
                .expectComplete()
                .verifyThenAssertThat()
                .tookLessThan(Duration.ofSeconds(30));
    }

    @Test
    public void testFindPathways_By_simulated_Ids_returns_mono_list() {
        log.info("testFindPathways_By_simulated_Ids_returns_mono_list");

        List<String> pathwayIds = Stream.generate(() -> "R-HSA-2514859")
                .limit(10)
                .collect(Collectors.toList());

        Mono<List<PathWay>> pathways = reactomeService.findPathwaysByIds(pathwayIds);

        StepVerifier
                .create(pathways)
                .assertNext(res -> {
                    assertNotNull(res);
                    assertThat(res).isNotEmpty();
                    assertThat(res, hasSize(greaterThanOrEqualTo(10)));
                    res.forEach(d -> log.info(" Reactome Result : " + d + "\n"));

                })
                .verifyComplete();

    }
}
