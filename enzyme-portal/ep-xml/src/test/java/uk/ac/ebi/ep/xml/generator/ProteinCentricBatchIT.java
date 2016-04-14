package uk.ac.ebi.ep.xml.generator;

import java.io.File;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {MockProteinBatchConfig.class, MockXmlConfig.class, JobTestRunnerConfig.class, SpringDataMockConfig.class})
public class ProteinCentricBatchIT {

    @ClassRule
    public static TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;

    static File xmlFile;
    @Autowired
    protected XmlConfigParams mockXmlConfigParams;

    @BeforeClass
    public static void setupXmlFileLocation() throws IOException {
        xmlFile = temporaryFolder.newFile();
        System.setProperty("ep.protein.centric.xml.dir", xmlFile.getAbsolutePath());
    }

    @Before
    public void setUp() throws Exception {
        entityManager = entityManagerFactory.createEntityManager();
        mockXmlConfigParams.setProteinCentricXmlDir(xmlFile.getAbsolutePath());
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
}
