
package uk.ac.ebi.ep.xml.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;
import uk.ac.ebi.ep.xml.util.DateTimeUtil;

/**
 *
 * @author joseph
 */
@Slf4j
public class XmlProcessorListener<T,S> implements ItemProcessListener<T, S> {
    private long processorStartTime;
    @Override
    public void beforeProcess(T t) {
        processorStartTime = System.currentTimeMillis();

    }

    @Override
    public void afterProcess(T t, S s) {
        String timeText = printTime(processorStartTime);

        log.error("Time taken to process the entry [" + t + "]: " + timeText);
       
    }

    @Override
    public void onProcessError(T t, Exception excptn) {
        log.error("Error while Processing Entry ::  " + t + " :: " + excptn);
    }
    
        private String printTime(long time) {
        long finishProcessing = System.currentTimeMillis();

        return DateTimeUtil.convertToText(time, finishProcessing);

    }
    
}
