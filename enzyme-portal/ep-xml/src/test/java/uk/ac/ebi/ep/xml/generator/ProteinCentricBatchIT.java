package uk.ac.ebi.ep.xml.generator;

import java.io.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import static org.hamcrest.CoreMatchers.is;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

import uk.ac.ebi.ep.data.testConfig.SpringDataMockConfig;
import uk.ac.ebi.ep.xml.config.MockProteinBatchConfig;
import uk.ac.ebi.ep.xml.config.MockXmlConfig;
import uk.ac.ebi.ep.xml.config.ProteinBatchConfig;
import uk.ac.ebi.ep.xml.config.XmlConfigParams;
import uk.ac.ebi.ep.xml.validator.EnzymePortalXmlValidator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MockProteinBatchConfig.class, MockXmlConfig.class, JobTestRunnerConfig.class,
        SpringDataMockConfig.class})
public class ProteinCentricBatchIT {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private XmlConfigParams mockXmlConfigParams;

    private EntityManager entityManager;

    @Before
    public void setUp() throws Exception {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @After
    public void closeResources() {
        entityManager.close();
    }

    @Test
    public void successfulJobRun() throws Exception {
        int expectedEntries = countEntries();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        BatchStatus status = jobExecution.getStatus();
        assertThat(status, is(BatchStatus.COMPLETED));

        StepExecution step = getStepByName(ProteinBatchConfig.PROTEIN_CENTRIC_DB_TO_XML_STEP, jobExecution);

        assertThat(step.getReadCount(), is(expectedEntries));
        assertThat(step.getWriteCount(), is(expectedEntries));
        assertThat(step.getSkipCount(), is(0));

        printXml();
    }

    @Test
    public void validatesXmlFromSuccessfulJobRun() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        BatchStatus status = jobExecution.getStatus();
        assertThat(status, is(BatchStatus.COMPLETED));

        String xmlFilePath = mockXmlConfigParams.getProteinCentricXmlDir();
        String[] ebeyeXSDs = mockXmlConfigParams.getEbeyeXSDs().split(",");

        printXml();

        assertThat(EnzymePortalXmlValidator.validateXml(xmlFilePath, ebeyeXSDs), is(true));
    }

    private StepExecution getStepByName(String stepName, JobExecution jobExecution) {
        for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
            if (stepExecution.getStepName().equals(stepName)) {
                return stepExecution;
            }
        }

        throw new IllegalArgumentException("Step name not recognized: " + stepName);
    }

    private int countEntries() {
        Query query = entityManager.createQuery("select count(u.dbentryId) from UniprotEntry u");

        return ((Long) query.getSingleResult()).intValue();
    }

    private void printXml() throws IOException {
        try (FileReader fileReader = new FileReader(mockXmlConfigParams.getProteinCentricXmlDir());
                BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}