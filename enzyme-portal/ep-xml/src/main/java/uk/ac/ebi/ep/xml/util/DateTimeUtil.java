package uk.ac.ebi.ep.xml.util;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.*;

/**
 * Utility method used to define how dates within the model should be defined.
 *
 * @author Ricardo Antunes
 */
public final class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("d-MMM-uuuu");

    private DateTimeUtil() {}

    /**
     * Converts a {@link LocalDate} into its String representation according to the format defined by
     * {@link #DATE_TIME_FORMATTER}.
     *
     * @param date the date to format
     * @return the string representation of the date
     * @throws NullPointerException if date to convert is null
     * @throws DateTimeException if an error occurs during printing
     */
    public static String convertDateToString(LocalDate date) {
        return date.format(DATE_TIME_FORMATTER);
    }

    /**
     * Converts a String representation of a date into a {@link LocalDate}. The date string needs to follow the
     * format defined by the {@link #DATE_TIME_FORMATTER}.
     *
     * @param dateText the string to convert into a date
     * @return a LocalDate
     * @throws NullPointerException if date to convert is null
     * @throws DateTimeException if the text can not be parsed
     */
    public static LocalDate convertStringToDate(String dateText) {
        return LocalDate.parse(dateText, DATE_TIME_FORMATTER);
    }

    /**
     * Given a start and end timestamp print out the difference between the two, in a human readable format.
     *
     * The time stamp needs to be at the millisecond range.
     *
     * The output will be format in hours:minutes:seconds
     *
     * @param start the start timestamp
     * @param end the end timestamp
     * @return a string representing the time difference between the start and end timestamps
     */
    public static String convertToText(long start, long end) {
        long processingTimeNano = end - start;

        return String.format("%d:%d:%d",
                MILLISECONDS.toHours(processingTimeNano),
                MILLISECONDS.toMinutes(processingTimeNano) - HOURS.toMinutes(MILLISECONDS.toHours(processingTimeNano)),
                MILLISECONDS.toSeconds(processingTimeNano) - MINUTES.toSeconds(MILLISECONDS.toMinutes(processingTimeNano)));
    }
}
