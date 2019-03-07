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
 * @author Joseph
 */
@Slf4j
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackageClasses = {DataConfig.class})
public class ProteinCentricBatchJob {

    public static void main(String[] args) throws Exception {
 System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "48");
 //System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "16"); 
         ForkJoinPool forkJoinPool =  new ForkJoinPool();
                log.error("ForkJoinPool.getCommonPoolParallelism() : " + ForkJoinPool.getCommonPoolParallelism() +"  FJP : "+ forkJoinPool.getParallelism());
        ConfigurableApplicationContext context = SpringApplication.run(ProteinCentricBatchJob.class, args);

        Job xmlJob = context.getBean("proteinXmlJob", Job.class);

        JobLauncher launcher = context.getBean(JobLauncher.class);
        launcher.run(xmlJob, new JobParameters());

    }

}
