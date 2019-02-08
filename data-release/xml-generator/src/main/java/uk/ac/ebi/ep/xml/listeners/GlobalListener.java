package uk.ac.ebi.ep.xml.listeners;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import uk.ac.ebi.ep.xml.util.DateTimeUtil;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 * @param <T>
 * @param <S>
 */
@Slf4j
public class GlobalListener<T, S> implements StepExecutionListener, ItemReadListener<T>, ItemProcessListener<T, S>, ItemWriteListener<T> {

     private long readStartTime;
    private long processorStartTime;
    private long writeStartTime;

    @Override
    public void beforeStep(StepExecution se) {
        log.debug("Step Execution Info :: " + se);
    }

    @Override
    public ExitStatus afterStep(StepExecution se) {
        log.warn("Step Execution Exit Status Info :: " + se);
        return se.getExitStatus();
    }

    @Override
    public void beforeRead() {
        readStartTime = System.currentTimeMillis();
    }

    @Override
    public void afterRead(T t) {
        String timeText = printTime(readStartTime);

        log.warn("Time taken to read the entry [" + t + "]: " + timeText);
    }

    @Override
    public void onReadError(Exception excptn) {
        log.error("Read Error :: " + excptn);
    }

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

    @Override
    public void beforeWrite(List<? extends T> list) {
        writeStartTime = System.currentTimeMillis();
    }

    @Override
    public void afterWrite(List<? extends T> list) {
        String timeText = printTime(writeStartTime);

        log.error("Time taken to write the entry size [" + list.size() + "]: " + timeText);
    }

    @Override
    public void onWriteError(Exception excptn, List<? extends T> list) {
        log.error("writer Error :: " + excptn);
    }

    private String printTime(long time) {
        long finishProcessing = System.currentTimeMillis();

        return DateTimeUtil.convertToText(time, finishProcessing);

    }

}
