package uk.ac.ebi.ep.dataservice.service;

import java.util.List;
import uk.ac.ebi.ep.dataservice.dto.WebComponentDto;

/**
 *
 * @author joseph
 */
public interface WebStatComponentService {

    List<String> findReleaseMonths();

    WebComponentDto findCurrentRelease();

    WebComponentDto findLatestRelease();

    WebComponentDto findByReleaseId(String releaseId);

    List<WebComponentDto> findLast3MonthsStatistics();

    List<WebComponentDto> findAnnualWebStatistics();

}
