package uk.ac.ebi.ep.dataservice.service;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ep.dataservice.dto.WebComponentDto;
import uk.ac.ebi.ep.dataservice.repositories.WebStatComponentRepository;

/**
 *
 * @author joseph
 */
@Service
public class WebStatComponentServiceImpl implements WebStatComponentService {

    private final WebStatComponentRepository webStatComponentRepository;

    @Autowired
    public WebStatComponentServiceImpl(WebStatComponentRepository webStatComponentRepository) {
        this.webStatComponentRepository = webStatComponentRepository;
    }

    private String monthNameFromReleaseId(String id) {
        int monthInNumber = Integer.parseInt(id.split("-")[0]);

        Month month = Month.of(monthInNumber);

        return month.getDisplayName(TextStyle.FULL, Locale.UK);
    }

    @Override
    public List<String> findReleaseMonths() {
        List<String> releaseIds = webStatComponentRepository.findReleaseIds();

        return releaseIds
                .stream()
                .map(this::monthNameFromReleaseId)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public WebComponentDto findCurrentRelease() {
        String releaseId = String.format("%d-%d", LocalDate.now().getMonthValue(), LocalDate.now().getYear());
        return webStatComponentRepository.findByReleaseId(releaseId, WebComponentDto.class);
    }

    @Override
    public WebComponentDto findLatestRelease() {
        return webStatComponentRepository.findTopByOrderByReleaseDateDesc(WebComponentDto.class);
    }

    @Override
    public WebComponentDto findByReleaseId(String releaseId) {
        return webStatComponentRepository.findByReleaseId(releaseId, WebComponentDto.class);

    }

    @Override
    public List<WebComponentDto> findLast3MonthsStatistics() {
        return webStatComponentRepository.findTop3ByOrderByReleaseDateAsc(WebComponentDto.class);
    }

    @Override
    public List<WebComponentDto> findAnnualWebStatistics() {
        return webStatComponentRepository.findTop12ByOrderByReleaseDateDesc(WebComponentDto.class);

    }

}
