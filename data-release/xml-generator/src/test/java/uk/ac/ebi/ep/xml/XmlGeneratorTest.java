package uk.ac.ebi.ep.xml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.ep.xml.config.XmlFileProperties;

/**
 *
 * @author Joseph
 */
@RunWith(SpringRunner.class)
@SpringBootTest
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = XmlGeneratorApplication.class)
@TestPropertySource(properties = {"management.port=0"})
@ActiveProfiles("uzpdev")
//@ContextConfiguration(classes = { BatchConfig.class })
//@RunWith(SpringJUnit4ClassRunner.class)
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
//@ComponentScan(basePackageClasses = {DataConfig.class,BatchConfig.class,EnzymeCentricConfig.class,ProteinCentricConfig.class})
public class XmlGeneratorTest {

    @Autowired
    private Job enzymeXmlJobTest;

    @Autowired
    private Job proteinXmlJobTest;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private XmlFileProperties xmlFileProperties;

    //@Autowired
    //private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;

    @Before
    public void setUp() {
        entityManager = entityManagerFactory.createEntityManager();

    }

    @After
    public void closeResources() throws SQLException {
        entityManager.close();
    }
    
        @Test
    public void successfulProteinCentricJobRun() throws Exception {
//        String query ="select COUNT(*) from PROTEIN_GROUPS where ENTRY_TYPE=0 and rownum<=1 \n"
//            + "union\n"
//            + "select * from PROTEIN_GROUPS where ENTRY_TYPE=1 and rownum<=2";
        
        //String query ="SELECT COUNT(*) FROM PROTEIN_GROUPS";
        
        String query = "SELECT COUNT(*) FROM(select * from PROTEIN_GROUPS where ENTRY_TYPE=0 AND ROWNUM <=1 UNION (select * from PROTEIN_GROUPS where ENTRY_TYPE=1 AND ROWNUM <=2))";

        
        int expectedEntries = countEntries(query);

//        JobLauncherTestUtils jobLauncherTestUtils = new JobLauncherTestUtils();
//            System.out.println("JOB UTIL "+ jobLauncherTestUtils);
//        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

       JobExecution jobExecution =  jobLauncher.run(proteinXmlJobTest, new JobParameters());
        BatchStatus status = jobExecution.getStatus();
        assertThat(status, is(BatchStatus.COMPLETED));

        StepExecution step = getStepByName(MockProteinCentricConfig.PROTEIN_READ_PROCESS_WRITE_XML_STEP, jobExecution);

        assertThat(step.getReadCount(), is(expectedEntries));
        assertThat(step.getWriteCount(), is(expectedEntries));
        assertThat(step.getSkipCount(), is(0));

        printXml();
    }

    @Test
    public void startProteinCentricBatch() throws Exception {
        System.out.println("ABOUT TO START PROTEIN BATCH " + xmlFileProperties.getProteinCentric());

        System.out.println("JOB " + proteinXmlJobTest);
        System.out.println("LUNCHER " + jobLauncher);

        jobLauncher.run(proteinXmlJobTest, new JobParameters());

    }

    @Test
    public void startEnzymeCentricBatch() throws Exception {
        System.out.println("ABOUT TO START ENZYME BATCH " + xmlFileProperties.getEnzymeCentric());

        System.out.println("JOB ENZYME " + enzymeXmlJobTest);

        jobLauncher.run(enzymeXmlJobTest, new JobParameters());

    }

    private StepExecution getStepByName(String stepName, JobExecution jobExecution) {
        for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
            if (stepExecution.getStepName().equals(stepName)) {
                return stepExecution;
            }
        }

        throw new IllegalArgumentException("Step name not recognized: " + stepName);
    }

    private int countEntries(String queryString) {
        //Query query = entityManager.createQuery("select count(p.proteinGroupId) from ProteinGroups p");
        //Query query = entityManager.createQuery(queryString);
        Query query = entityManager.createNativeQuery(queryString);
    String count = query.getSingleResult().toString();
        System.out.println("COUNT "+ count);
        return Integer.parseInt(count);
        //return (int) query.getSingleResult();
    }

    private void printXml() throws IOException {
        try (FileReader fileReader = new FileReader(xmlFileProperties.getProteinCentric());
                BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
