package uk.ac.ebi.ep.xml;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import uk.ac.ebi.ep.xml.config.DataConfig;

/**
 *
 * @author Joseph
 */
//@Slf4j
@EnableAutoConfiguration
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackageClasses = {DataConfig.class})
public class ProteinCentricBatchJob {

    public static void main(String[] args) throws Exception {

        ConfigurableApplicationContext context = SpringApplication.run(ProteinCentricBatchJob.class, args);

        Job xmlJob = context.getBean("proteinXmlJob", Job.class);

        JobLauncher launcher = context.getBean(JobLauncher.class);
        launcher.run(xmlJob, new JobParameters());

    }

}
