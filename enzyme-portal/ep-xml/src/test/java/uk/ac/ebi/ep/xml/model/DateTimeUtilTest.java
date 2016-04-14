package uk.ac.ebi.ep.xml.model;

import uk.ac.ebi.ep.xml.util.DateTimeUtil;

import java.time.DateTimeException;
import java.time.LocalDate;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests the behaviour of the {@link DateTimeUtil} class.
 */
public class DateTimeUtilTest {

    @Test(expected = NullPointerException.class)
    public void throwsExceptionConvertingNullDate() throws Exception {
        DateTimeUtil.convertDateToString(null);
    }

    @Test
    public void convertsValidDate() throws Exception {
        LocalDate todayDate = LocalDate.now();

        String dateText = DateTimeUtil.convertDateToString(LocalDate.now());
        LocalDate convertedDate = DateTimeUtil.convertStringToDate(dateText);

        assertThat(convertedDate, is(todayDate));
    }

    @Test(expected = NullPointerException.class)
    public void throwsExceptionConvertingNullDateString() throws Exception {
        DateTimeUtil.convertStringToDate(null);
    }

    @Test(expected = DateTimeException.class)
    public void throwsExceptionConvertingDateStringWithInvalidFormat() throws Exception {
        String dateString = "03/03/2003";

        DateTimeUtil.convertStringToDate(dateString);
    }

    @Test
    public void convertsValidDateString() throws Exception {
        String dateText = "3-Mar-2003";

        LocalDate convertedDate = DateTimeUtil.convertStringToDate(dateText);
        String convertedDateText = DateTimeUtil.convertDateToString(convertedDate);

        assertThat(convertedDateText, is(dateText));
    }

    @Test
    public void convertSameStartAndEndTimeStampsToText() throws Exception {
        long start = 1;
        long end = start;

        String timeText = DateTimeUtil.convertToText(start, end);

        assertThat(timeText, is("0:0:0"));
    }

    @Test
    public void convert1SecondDifferenceBetweenStartAndEndTimeStampsToText() throws Exception {
        long start = 0;
        long end = 1000;

        String timeText = DateTimeUtil.convertToText(start, end);

        assertThat(timeText, is("0:0:1"));
    }

    @Test
    public void convert1MinuteDifferenceBetweenStartAndEndTimeStampsToText() throws Exception {
        long start = 0;
        long end = 60000;

        String timeText = DateTimeUtil.convertToText(start, end);

        assertThat(timeText, is("0:1:0"));
    }

    @Test
    public void convert1HourDifferenceBetweenStartAndEndTimeStampsToText() throws Exception {
        long start = 0;
        long end = 3600000;

        String timeText = DateTimeUtil.convertToText(start, end);

        assertThat(timeText, is("1:0:0"));
    }
}
