package uk.ac.ebi.ep.xml.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import uk.ac.ebi.ep.xml.util.DateTimeUtil;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Slf4j
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {


    private final String jobName;

    public JobCompletionNotificationListener(String jobName) {
        this.jobName = jobName;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.warn(jobName + " job starting...");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.warn(jobName + "job status :: " + jobExecution.getStatus());
        
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.warn(jobName + "job has completed.");

            String duration = DateTimeUtil.convertToText(jobExecution.getStartTime().getTime(),
                    jobExecution.getEndTime().getTime());

            log.info("=====================================================");
            log.info("              " + jobName + " Job Statistics                 ");
            log.info("Exit status   : {}", jobExecution.getExitStatus().getExitCode());
            log.info("Start time    : {}", jobExecution.getStartTime());
            log.info("End time      : {}", jobExecution.getEndTime());
            log.warn("Duration      : {}", duration);
        }
    }
}
