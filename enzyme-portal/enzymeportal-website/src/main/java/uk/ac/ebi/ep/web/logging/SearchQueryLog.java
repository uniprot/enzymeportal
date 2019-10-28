package uk.ac.ebi.ep.web.logging;

import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author joseph
 */
@Slf4j
public class SearchQueryLog {

    public static void logSearchQuery(SeachType seachType, SeachCategory seachCategory, String term) {
        String logAnalysis = String.format("SearchType=%s,SearchCategory=%s,searchTerm=%s,date=%s", seachType.name(), seachCategory.name(), term, LocalDate.now().toString());
        log.info(logAnalysis);
    }
}
