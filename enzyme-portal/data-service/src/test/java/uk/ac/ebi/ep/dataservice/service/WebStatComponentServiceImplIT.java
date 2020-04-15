package uk.ac.ebi.ep.dataservice.service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.ac.ebi.ep.dataservice.dto.WebComponentDto;

/**
 *
 * @author joseph
 */
@Disabled
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("uzpdev")
public class WebStatComponentServiceImplIT {
    
    @Autowired
    private WebStatComponentService webStatComponentService;
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private EntityManager entityManager;
    
    @Test
    void injectedComponentsAreNotNull() {
        assertThat(dataSource).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(webStatComponentService).isNotNull();
        
    }

    /**
     * Test of findReleaseMonths method, of class WebStatComponentServiceImpl.
     */
    @Test
    public void testFindReleaseMonths() {
        List<String> result = webStatComponentService.findReleaseMonths();
        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));
    }

    /**
     * Test of findCurrentRelease method, of class WebStatComponentServiceImpl.
     */
    @Test
    public void testFindCurrentRelease() {
        WebComponentDto result = webStatComponentService.findCurrentRelease();
        assertNotNull(result);
    }

    /**
     * Test of findLatestRelease method, of class WebStatComponentServiceImpl.
     */
    @Test
    public void testFindLatestRelease() {
        WebComponentDto result = webStatComponentService.findLatestRelease();
        assertNotNull(result);
    }

    /**
     * Test of findByReleaseId method, of class WebStatComponentServiceImpl.
     */
    @Test
    public void testFindByReleaseId() {
        String releaseId = String.format("%d-%d", Month.MARCH.getValue(), LocalDate.now().getYear());
        
        WebComponentDto dao = webStatComponentService.findByReleaseId(releaseId);
        assertNotNull(dao);
    }

    /**
     * Test of findLast3MonthsStatistics method, of class
     * WebStatComponentServiceImpl.
     */
    @Test
    public void testFindLast3MonthsStatistics() {
        List<WebComponentDto> result = webStatComponentService.findLast3MonthsStatistics();
        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));
    }

    /**
     * Test of findAnnualWebStatistics method, of class
     * WebStatComponentServiceImpl.
     */
    @Test
    public void testFindAnnualWebStatistics() {
        List<WebComponentDto> result = webStatComponentService.findAnnualWebStatistics();
        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));
        
    }
    
}
