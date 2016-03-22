package uk.ac.ebi.ep.xml.model;

import java.text.DateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests the behaviour of the {@link ModelDateUtil} class.
 */
public class ModelDateUtilTest {

    @Test(expected = NullPointerException.class)
    public void throwsExceptionConvertingNullDate() throws Exception {
        ModelDateUtil.convertDateToString(null);
    }

    @Test
    public void convertsValidDate() throws Exception {
        LocalDate todayDate = LocalDate.now();

        String dateText = ModelDateUtil.convertDateToString(LocalDate.now());
        LocalDate convertedDate = ModelDateUtil.convertStringToDate(dateText);

        assertThat(convertedDate, is(todayDate));
    }

    @Test(expected = NullPointerException.class)
    public void throwsExceptionConvertingNullDateString() throws Exception {
        ModelDateUtil.convertStringToDate(null);
    }

    @Test(expected = DateTimeException.class)
    public void throwsExceptionConvertingDateStringWithInvalidFormat() throws Exception {
        String dateString = "03/03/2003";

        ModelDateUtil.convertStringToDate(dateString);
    }

    @Test
    public void convertsValidDateString() throws Exception {
        String dateText = "3-Mar-2003";

        LocalDate convertedDate = ModelDateUtil.convertStringToDate(dateText);
        String convertedDateText = ModelDateUtil.convertDateToString(convertedDate);

        assertThat(convertedDateText, is(dateText));
    }
}
