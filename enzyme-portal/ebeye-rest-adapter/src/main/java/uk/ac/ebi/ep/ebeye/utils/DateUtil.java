
package uk.ac.ebi.ep.ebeye.utils;

import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public final class DateUtil {

    public static String convertToText(long start, long end) {
        long processingTimeNano = end - start;

        return String.format("%d:%d:%d",
                MILLISECONDS.toHours(processingTimeNano),
                MILLISECONDS.toMinutes(processingTimeNano) - HOURS.toMinutes(MILLISECONDS.toHours(processingTimeNano)),
                MILLISECONDS.toSeconds(processingTimeNano) - MINUTES.toSeconds(MILLISECONDS.toMinutes(processingTimeNano)));
    }
}
