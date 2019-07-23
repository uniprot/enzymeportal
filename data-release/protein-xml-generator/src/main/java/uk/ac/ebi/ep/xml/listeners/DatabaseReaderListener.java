package uk.ac.ebi.ep.xml.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;
import uk.ac.ebi.ep.xml.util.DateTimeUtil;

/**
 *
 * @author joseph
 * @param <T>
 */
@Slf4j
public class DatabaseReaderListener<T> implements ItemReadListener<T> {

    private long readStartTime;

    @Override
    public void beforeRead() {
        readStartTime = System.currentTimeMillis();
    }

    @Override
    public void afterRead(T t) {
        String timeText = printTime(readStartTime);

        log.warn("Time taken to read the entry :: [" + t + "]: " + timeText);
    }

    @Override
    public void onReadError(Exception excptn) {
        log.error("Read Error :: " + excptn);
    }

    private String printTime(long time) {
        long finishProcessing = System.currentTimeMillis();

        return DateTimeUtil.convertToText(time, finishProcessing);

    }

}
