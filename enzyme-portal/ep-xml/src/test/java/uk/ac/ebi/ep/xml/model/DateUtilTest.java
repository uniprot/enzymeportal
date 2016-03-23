package uk.ac.ebi.ep.xml.model;

import uk.ac.ebi.ep.xml.util.DateUtil;

import java.time.DateTimeException;
import java.time.LocalDate;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests the behaviour of the {@link DateUtil} class.
 */
public class DateUtilTest {

    @Test(expected = NullPointerException.class)
    public void throwsExceptionConvertingNullDate() throws Exception {
        DateUtil.convertDateToString(null);
    }

    @Test
    public void convertsValidDate() throws Exception {
        LocalDate todayDate = LocalDate.now();

        String dateText = DateUtil.convertDateToString(LocalDate.now());
        LocalDate convertedDate = DateUtil.convertStringToDate(dateText);

        assertThat(convertedDate, is(todayDate));
    }

    @Test(expected = NullPointerException.class)
    public void throwsExceptionConvertingNullDateString() throws Exception {
        DateUtil.convertStringToDate(null);
    }

    @Test(expected = DateTimeException.class)
    public void throwsExceptionConvertingDateStringWithInvalidFormat() throws Exception {
        String dateString = "03/03/2003";

        DateUtil.convertStringToDate(dateString);
    }

    @Test
    public void convertsValidDateString() throws Exception {
        String dateText = "3-Mar-2003";

        LocalDate convertedDate = DateUtil.convertStringToDate(dateText);
        String convertedDateText = DateUtil.convertDateToString(convertedDate);

        assertThat(convertedDateText, is(dateText));
    }
}
