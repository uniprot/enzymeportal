package uk.ac.ebi.ep.dataservice.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.ac.ebi.ep.dataservice.dto.WebStatXrefDto;
import uk.ac.ebi.ep.dataservice.dto.WebStatXrefView;
import uk.ac.ebi.ep.dataservice.entities.WebStatXref;

/**
 *
 * @author joseph
 */
public interface WebStatService {

    WebStatXrefView findWebStatisticsXrefByReleaseId(String releaseId);

    WebStatXrefDto findWebStatXrefByReleaseId(String releaseId);

    List<WebStatXrefView> findWebStatisticsXref();

    List<WebStatXrefView> findWebStatXrefReleaseMonths();

    //
    List<String> findReleaseMonths();

    WebStatXrefDto findCurrentRelease();

    WebStatXrefDto findLatestRelease();

    WebStatXrefDto findByReleaseId(String releaseId);

    List<WebStatXrefDto> findLast3MonthsStatistics();

    List<WebStatXrefDto> findAnnualWebStatistics();

    Page<WebStatXref> findStatisticsInLast3Months(Pageable pageable);

}
