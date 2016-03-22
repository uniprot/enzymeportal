package uk.ac.ebi.ep.xml.generator;

import uk.ac.ebi.ep.data.testConfig.SpringDataMockConfig;
import uk.ac.ebi.ep.xml.config.ProteinBatchConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {ProteinBatchConfig.class, JobTestRunnerConfig.class, SpringDataMockConfig.class})
public class ProteinCentricBatchIT {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    public void successfulJobRun() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        BatchStatus status = jobExecution.getStatus();
        assertThat(status, is(BatchStatus.COMPLETED));

        StepExecution step = getStepByName(ProteinBatchConfig.PROTEIN_CENTRIC_DB_TO_XML_STEP, jobExecution);

        assertThat(step.getReadCount(), is(17));
        assertThat(step.getWriteCount(), is(17));
        assertThat(step.getSkipCount(), is(0));
    }

    private StepExecution getStepByName(String stepName, JobExecution jobExecution) {
        for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
            if (stepExecution.getStepName().equals(stepName)) {
                return stepExecution;
            }
        }

        throw new IllegalArgumentException("Step name not recognized: " + stepName);
    }
}
