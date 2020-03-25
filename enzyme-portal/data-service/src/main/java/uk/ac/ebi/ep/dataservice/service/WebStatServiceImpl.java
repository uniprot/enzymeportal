package uk.ac.ebi.ep.dataservice.service;

import com.querydsl.core.types.Predicate;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.dataservice.dto.WebStatXrefDto;
import uk.ac.ebi.ep.dataservice.dto.WebStatXrefView;
import uk.ac.ebi.ep.dataservice.entities.QWebStatXref;
import uk.ac.ebi.ep.dataservice.entities.WebStatXref;
import uk.ac.ebi.ep.dataservice.repositories.WebStatXrefRepository;

/**
 *
 * @author joseph
 */
@Service
@Transactional
public class WebStatServiceImpl implements WebStatService {

    private final WebStatXrefRepository webStatXrefRepository;

    @Autowired
    public WebStatServiceImpl(WebStatXrefRepository webStatXrefRepository) {
        this.webStatXrefRepository = webStatXrefRepository;
    }

    @Override
    public WebStatXrefView findWebStatisticsXrefByReleaseId(String releaseId) {
        return webStatXrefRepository.findWebStatXrefViewByReleaseId(releaseId);
    }

    @Override
    public List<WebStatXrefView> findWebStatisticsXref() {
        return webStatXrefRepository.findWebStatXrefView();
    }

    @Override
    public List<WebStatXrefView> findWebStatXrefReleaseMonths() {
        return webStatXrefRepository.findWebStatXrefReleaseMonths();
    }

    //DTO
    @Override
    public WebStatXrefDto findWebStatXrefByReleaseId(String releaseId) {

        return webStatXrefRepository.findByReleaseId(releaseId, WebStatXrefDto.class);
    }

    @Override
    public List<String> findReleaseMonths() {

        List<String> releaseIds = webStatXrefRepository.findReleaseIds();

        return releaseIds
                .stream()
                .map(id -> monthNameFromReleaseId(id))
                .distinct()
                .collect(Collectors.toList());

    }

    @Override
    public WebStatXrefDto findCurrentRelease() {
        String releaseId = String.format("%d-%d", LocalDate.now().getMonthValue(), LocalDate.now().getYear());
        return webStatXrefRepository.findByReleaseId(releaseId, WebStatXrefDto.class);
    }

    @Override
    public WebStatXrefDto findLatestRelease() {
        return webStatXrefRepository.findTopByOrderByReleaseDateDesc(WebStatXrefDto.class);
    }

    @Override
    public WebStatXrefDto findByReleaseId(String releaseId) {
        return webStatXrefRepository.findByReleaseId(releaseId, WebStatXrefDto.class);

    }

    @Override
    public List<WebStatXrefDto> findAnnualWebStatistics() {
        return webStatXrefRepository.findTop12ByOrderByReleaseDateDesc(WebStatXrefDto.class);

    }

    @Override
    public List<WebStatXrefDto> findLast3MonthsStatistics() {
        return webStatXrefRepository.findTop3ByOrderByReleaseDateAsc(WebStatXrefDto.class);
    }

    @Override
    public Page<WebStatXref> findStatisticsInLast3Months(Pageable pageable) {
        return webStatXrefRepository.findAll(predicate(), pageable);
    }

    private String monthNameFromReleaseId(String id) {
        int monthInNumber = Integer.parseInt(id.split("-")[0]);

        Month month = Month.of(monthInNumber);

        return month.getDisplayName(TextStyle.FULL, Locale.UK);
    }

    private Date convertToDateViaInstant(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    private Predicate predicate() {

        LocalDate today = LocalDate.now();
        Date date = convertToDateViaInstant(today);

        LocalDate last3months = LocalDate.now().minusMonths(3);
        Date past3Months = convertToDateViaInstant(last3months);

        Predicate predicate = QWebStatXref.webStatXref.releaseDate.between(past3Months, date);
        return predicate;
    }

}
