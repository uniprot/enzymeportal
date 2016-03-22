package uk.ac.ebi.ep.xml.util;

import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;

/**
 * Listener used to gather stats on a given job
 *
 * @author Ricardo Antunes
 */
public class LogJobListener implements JobExecutionListener {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String jobName;

    public LogJobListener(String jobName) {
        this.jobName = jobName;
    }

    @Override public void beforeJob(JobExecution jobExecution) {
        logger.info(jobName + " job starting.");
    }

    @Override public void afterJob(JobExecution jobExecution) {
        logger.info(jobName + "job complete.");

        // compute duration
        long durationMillis = jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime();

        String duration = String.format("%d hrs, %d min, %d sec",
                TimeUnit.MILLISECONDS.toHours(durationMillis),
                TimeUnit.MILLISECONDS.toMinutes(durationMillis) - TimeUnit.HOURS.toSeconds(TimeUnit.MILLISECONDS
                        .toHours(durationMillis)),
                TimeUnit.MILLISECONDS.toSeconds(durationMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                        .toMinutes(durationMillis))
        );

        logger.info("=====================================================");
        logger.info("              " + jobName + " Job Statistics                 ");
        logger.info("Exit status   : {}", jobExecution.getExitStatus().getExitCode());
        logger.info("Start time    : {}", jobExecution.getStartTime());
        logger.info("End time      : {}", jobExecution.getEndTime());
        logger.info("Duration      : {}", duration);

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
        logger.info("Read count    : {}", readCount);
        logger.info("Write count   : {}", writeCount);
        logger.info("Skip count    : {} ({} read / {} processing / {} write)", skipCount, readSkips, processingSkips,
                writeSkips);
        logger.info("=====================================================");
        jobExecution.getExitStatus();
    }
}