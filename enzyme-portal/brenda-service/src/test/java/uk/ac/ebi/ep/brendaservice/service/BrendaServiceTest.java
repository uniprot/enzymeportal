package uk.ac.ebi.ep.brendaservice.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.StopWatch;
import uk.ac.ebi.ep.brendaservice.dto.Brenda;
import uk.ac.ebi.ep.brendaservice.dto.BrendaResult;
import uk.ac.ebi.ep.brendaservice.dto.Ph;
import uk.ac.ebi.ep.brendaservice.dto.Temperature;

/**
 *
 * @author joseph
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BrendaServiceTest {

    @Autowired
    private BrendaService brendaService;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(brendaService).isNotNull();
    }

    @Test
    public void testCachedFindBrendaResultByEc() {
        log.info("testCachedFindBrendaResultByEc");
        String ec = "6.1.1.1";

        int limit = 5;
        boolean addAcc = true;
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        BrendaResult result = brendaService.findBrendaResultByEc(ec, limit, addAcc);
        stopWatch.stop();
        double originalElaspedTime = stopWatch.getTotalTimeSeconds();
        assertNotNull(result);
        assertThat(result.getBrenda(), hasSize(greaterThanOrEqualTo(2)));
        assertThat(result.getPh(), hasSize(greaterThanOrEqualTo(2)));
        assertThat(result.getTemperature(), hasSize(greaterThanOrEqualTo(1)));

        limit = 1;
        StopWatch sw = new StopWatch();
        sw.start();
        BrendaResult cachedResult = brendaService.findBrendaResultByEc(ec, limit, addAcc);
        sw.stop();
        double afterCachedElaspedTime = sw.getTotalTimeSeconds();
        assertNotNull(cachedResult);
        assertThat(cachedResult.getBrenda(), hasSize(greaterThanOrEqualTo(2)));
        assertThat(cachedResult.getPh(), hasSize(greaterThanOrEqualTo(2)));
        assertThat(cachedResult.getTemperature(), hasSize(greaterThanOrEqualTo(1)));

        log.info("OoriginalElaspedTime :  " + originalElaspedTime + " afterCachedElaspedTime : " + afterCachedElaspedTime);
        assertThat(originalElaspedTime, greaterThan(afterCachedElaspedTime));

    }

    /**
     * Test of findTemperatureByEcAndOrganism method, of class BrendaService.
     */
    @Test
    public void testFindTemperatureByEcAndOrganism() {
        log.info("testFindTemperatureByEcAndOrganism");
        String ec = "1.1.1.1";
        String organism = "Candida maris";

        Temperature result = brendaService.findTemperatureByEcAndOrganism(ec, organism);

        assertNotNull(result);

    }

    /**
     * Test of findTemperatureByEcAndOrganism method, of class BrendaService.
     */
    @Test
    public void testFindTemperaturesByEcAndOrganism() {
        log.info("testFindTemperaturesByEcAndOrganism");
        String ec = "1.1.1.1";
        String organism = "Candida maris";
        int limit = 5;

        List<Temperature> result = brendaService.findTemperatureByEcAndOrganism(ec, organism, limit);

        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));

    }

    /**
     * Test of findTemperatureByEc method, of class BrendaService.
     */
    @Test
    public void testFindTemperatureByEc_limit() {
        log.info("testFindTemperaturesByEcAndOrganism");
        String ec = "6.1.1.1";

        int limit = 5;
        boolean addAcc = false;
        List<Temperature> result = brendaService.findTemperatureByEc(ec, limit, addAcc);

        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));
    }

    @Test
    public void testFindTemperatureByEc_limit_with_Accession() {
        log.info("testFindTemperaturesByEcAndOrganism");
        String ec = "6.1.1.1";

        int limit = 5;
        boolean addAcc = true;
        List<Temperature> result = brendaService.findTemperatureByEc(ec, limit, addAcc);

        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));
    }

    /**
     * Test of findPhByEcAndOrganism method, of class BrendaService.
     */
    @Test
    public void testFindPhByEcAndOrganism() {

        log.info("testFindPhByEcAndOrganism");
        String ec = "1.1.1.1";
        String organism = "Candida maris";

        Ph result = brendaService.findPhByEcAndOrganism(ec, organism);

        assertNotNull(result);
    }

    /**
     * Test of findPhByEc method, of class BrendaService.
     */
    @Test
    public void testFindPhListByEc() {

        log.info("testFindPhListByEc");
        String ec = "1.1.1.1";

        int limit = 5;
        boolean addAcc = false;
        List<Ph> result = brendaService.findPhByEc(ec, limit, addAcc);

        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));
    }

    @Test
    public void testFindPhListByEc_with_Acc() {

        log.info("testFindPhListByEc");
        String ec = "1.1.1.1";

        int limit = 5;
        boolean addAcc = true;
        List<Ph> result = brendaService.findPhByEc(ec, limit, addAcc);

        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));
    }

    /**
     * Test of findBrendaResultByEc method, of class BrendaService.
     */
    @Test
    public void testFindBrendaResultByEc() {
        log.info("testFindKineticsByEc");
        String ec = "1.11.1.16";
        int limit = 50;

        List<Brenda> result = brendaService.findKineticsByEc(ec, limit);

        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));
    }

    /**
     * Test of findKcatKmValueByEc method, of class BrendaService.
     */
    @Test
    public void testFindKcatKmValueByEc() {
        log.info("testFindKcatKmValueByEc");
        String ec = "2.1.1.1";

        int limit = 5;
        boolean addAcc = false;
        List<Brenda> result = brendaService.findKcatKmValueByEc(ec, limit, addAcc);

        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));
    }

    @Test
    public void testFindKcatKmValueByEc_addAccession() {
        log.info("testFindKcatKmValueByEc");
        String ec = "2.1.1.1";

        int limit = 5;
        boolean addAcc = true;
        List<Brenda> result = brendaService.findKcatKmValueByEc(ec, limit, addAcc);

        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));
    }

    /**
     * Test of findKmValueByEc method, of class BrendaService.
     */
    @Test
    public void testFindKmValueByEc() {
        log.info("testFindKmValueByEc");
        String ec = "7.1.1.1";

        int limit = 5;
        boolean addAcc = false;
        List<Brenda> result = brendaService.findKmValueByEc(ec, limit, addAcc);

        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));
    }

    @Test
    public void testFindKmValueByEc_with_Acc() {
        log.info("testFindKmValueByEc");
        String ec = "7.1.1.1";

        int limit = 5;
        boolean addAcc = true;
        List<Brenda> result = brendaService.findKmValueByEc(ec, limit, addAcc);

        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));
    }

}
