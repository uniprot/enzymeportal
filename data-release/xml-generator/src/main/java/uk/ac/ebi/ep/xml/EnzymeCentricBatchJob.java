package uk.ac.ebi.ep.xml;

import java.util.concurrent.ForkJoinPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import uk.ac.ebi.ep.xml.config.DataConfig;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Slf4j
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackageClasses = {DataConfig.class})
public class EnzymeCentricBatchJob {

    public static void main(String[] args) throws Exception {
//xmllint --nocdata --format enzyme-portal-enzymes-ebeye.xml | grep -c '<entry id="2.7.1.1">'
        //System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");
        ForkJoinPool forkJoinPool = new ForkJoinPool();
                log.error("ForkJoinPool.getCommonPoolParallelism() : " + ForkJoinPool.getCommonPoolParallelism() +"  FJP : "+ forkJoinPool.getParallelism());
        ConfigurableApplicationContext context = SpringApplication.run(EnzymeCentricBatchJob.class, args);

        Job xmlJob = context.getBean("enzymeXmlJob", Job.class);

        JobLauncher launcher = context.getBean(JobLauncher.class);
        launcher.run(xmlJob, new JobParameters());

    }

}
