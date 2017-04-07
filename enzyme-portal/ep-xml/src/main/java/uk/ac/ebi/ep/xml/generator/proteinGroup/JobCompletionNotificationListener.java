package uk.ac.ebi.ep.xml.generator.proteinGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import uk.ac.ebi.ep.xml.util.DateTimeUtil;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String jobName;

    public JobCompletionNotificationListener(String jobName) {
        this.jobName = jobName;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        logger.warn(jobName + " job starting...");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        logger.warn(jobName + "job status :: " + jobExecution.getStatus());
        
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            logger.warn(jobName + "job has completed.");

            String duration = DateTimeUtil.convertToText(jobExecution.getStartTime().getTime(),
                    jobExecution.getEndTime().getTime());

            logger.info("=====================================================");
            logger.info("              " + jobName + " Job Statistics                 ");
            logger.info("Exit status   : {}", jobExecution.getExitStatus().getExitCode());
            logger.info("Start time    : {}", jobExecution.getStartTime());
            logger.info("End time      : {}", jobExecution.getEndTime());
            logger.warn("Duration      : {}", duration);
        }
    }
}
