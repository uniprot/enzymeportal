package uk.ac.ebi.ep.xml.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import uk.ac.ebi.ep.xml.util.DateTimeUtil;

/**
 * Listener used to gather stats on a given job
 *
 * @author Joseph
 */
@Slf4j
public class LogJobListener implements JobExecutionListener {

    private final String jobName;

    public LogJobListener(String jobName) {
        this.jobName = jobName;
    }

    @Override public void beforeJob(JobExecution jobExecution) {
        log.info(jobName + " job starting.");
    }

    @Override public void afterJob(JobExecution jobExecution) {
        log.info(jobName + "job complete.");

        String duration = DateTimeUtil.convertToText(jobExecution.getStartTime().getTime(),
                jobExecution.getEndTime().getTime());

        log.info("=====================================================");
        log.info("              " + jobName + " Job Statistics                 ");
        log.info("Exit status   : {}", jobExecution.getExitStatus().getExitCode());
        log.info("Start time    : {}", jobExecution.getStartTime());
        log.info("End time      : {}", jobExecution.getEndTime());
        log.warn("Duration      : {}", duration);

        long skipCount = 0L;
        long readSkips = 0L;
        long writeSkips = 0L;
        long processingSkips = 0L;
        long readCount = 0L;
        long writeCount = 0L;

        for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
            readSkips += stepExecution.getReadSkipCount();
            writeSkips += stepExecution.getWriteSkipCount();
            processingSkips += stepExecution.getProcessSkipCount();
            readCount += stepExecution.getReadCount();
            writeCount += stepExecution.getWriteCount();
            skipCount += stepExecution.getSkipCount();

        }
        log.info("Read count    : {}", readCount);
        log.info("Write count   : {}", writeCount);
        log.info("Skip count    : {} ({} read / {} processing / {} write)", skipCount, readSkips, processingSkips,
                writeSkips);
        log.info("=====================================================");
        jobExecution.getExitStatus();
    }
}