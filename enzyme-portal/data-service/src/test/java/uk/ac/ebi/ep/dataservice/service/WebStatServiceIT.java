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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.ac.ebi.ep.dataservice.dto.WebStatXrefDto;
import uk.ac.ebi.ep.dataservice.dto.WebStatXrefView;
import uk.ac.ebi.ep.dataservice.entities.WebStatXref;

/**
 *
 * @author joseph
 */
@Disabled
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("uzpdev")
public class WebStatServiceIT { //extends DataServiceBaseIT {

    @Autowired
    private WebStatService webStatService;
    @Autowired
    private DataSource dataSource;

    @Autowired
    private EntityManager entityManager;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(dataSource).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(webStatService).isNotNull();
    }

    /**
     * Test of findWebStatisticsXrefByReleaseId method, of class WebStatService.
     */
    @Test
    public void testFindWebStatisticsXrefByReleaseId() {

        String releaseId = String.format("%d-%d", Month.MARCH.getValue(), LocalDate.now().getYear());

        WebStatXrefView result = webStatService.findWebStatisticsXrefByReleaseId(releaseId);

        assertNotNull(result);

        WebStatXrefDto dao = webStatService.findWebStatXrefByReleaseId(releaseId);
        assertNotNull(dao);
        List<String> months = webStatService.findReleaseMonths();
        assertNotNull(months);

        WebStatXrefDto current = webStatService.findCurrentRelease();
        assertNotNull(current);

        WebStatXrefDto latest = webStatService.findLatestRelease();
        assertNotNull(latest);

        WebStatXrefDto data = webStatService.findByReleaseId(releaseId);
        assertNotNull(data);

        List<WebStatXrefDto> annual = webStatService.findAnnualWebStatistics();
        assertNotNull(annual);
        List<WebStatXrefDto> three = webStatService.findLast3MonthsStatistics();
        assertNotNull(three);

        Sort sort = Sort.by("releaseDate").ascending();
        Pageable p = PageRequest.of(0, 3, sort);
        Page<WebStatXref> page = webStatService.findStatisticsInLast3Months(p);
        assertNotNull(page);

    }

    /**
     * Test of findWebStatisticsXref method, of class WebStatService.
     */
    @Test
    public void testFindWebStatisticsXref() {
        List<WebStatXrefView> result = webStatService.findWebStatisticsXref();
        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));
    }

    /**
     * Test of findWebStatXrefReleaseMonths method, of class WebStatService.
     */
    @Test
    public void testFindWebStatXrefReleaseMonths() {
        List<WebStatXrefView> result = webStatService.findWebStatXrefReleaseMonths();
        assertNotNull(result);
        assertThat(result, hasSize(greaterThanOrEqualTo(1)));
    }

}
