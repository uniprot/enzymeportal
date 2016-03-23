package uk.ac.ebi.ep.xml.util;

import java.util.concurrent.TimeUnit;

/**
 * Utility class to aid in time manipulation.
 */
public final class TimeUtil {
    private TimeUtil(){}

    public static String convertToText(long start, long end, TimeUnit unit) {
        long processingTimeNano = end - start;

        return String.format("%d:%d:%d",
                unit.toHours(processingTimeNano),
                unit.toMinutes(processingTimeNano) - TimeUnit.HOURS.toSeconds(unit.toHours(processingTimeNano)),
                unit.toSeconds(processingTimeNano) - TimeUnit.MINUTES.toSeconds(unit.toMinutes(processingTimeNano)));
    }
}
