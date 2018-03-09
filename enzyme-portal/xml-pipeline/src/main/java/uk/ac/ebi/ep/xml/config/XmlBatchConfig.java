package uk.ac.ebi.ep.xml.config;

import javax.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import uk.ac.ebi.ep.xml.entity.EnzymePortalUniqueEc;
import uk.ac.ebi.ep.xml.schema.Entry;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Configuration
@EnableBatchProcessing
public class XmlBatchConfig {

    @Autowired
    protected EntityManagerFactory entityManagerFactory;

//    @Bean(name = "xmlJob")
//    public Job job(JobBuilderFactory jobBuilderFactory,
//            StepBuilderFactory stepBuilderFactory, EnzymeCentricConfiguration ecConfig) throws Exception {
//
//        Step uniqueEcStep = stepBuilderFactory.get(READ_PROCESS_WRITE_XML_STEP)
//                .<EnzymePortalUniqueEc, Entry>chunk(CHUNK_SIZE)
//                .reader(ecConfig.databaseReader())
//                .processor(ecConfig.entryProcessor())
//                .writer(ecConfig.xmlWriter())
//                .listener(logChunkListener())
//                .listener(stepExecutionListener())
//                .listener(itemReadListener())
//                .listener(itemProcessListener())
//                .listener(itemWriteListener())
//                .build();
//
//        return jobBuilderFactory.get(XML_JOB)
//                .start(uniqueEcStep)
//                .listener(jobExecutionListener())
//                .build();
//
//    }
    @Bean(name = "enzymeXmlJob")
    public Job job(JobBuilderFactory jobBuilderFactory,
            StepBuilderFactory stepBuilderFactory, EnzymeCentricConfiguration ecConfig) throws Exception {

        Step uniqueEcStep = stepBuilderFactory.get(EnzymeCentricConfiguration.ENZYME_READ_PROCESS_WRITE_XML_STEP)
                .<EnzymePortalUniqueEc, Entry>chunk(xmlFileProperties().getChunkSize())
                .reader(ecConfig.databaseReader())
                .processor(ecConfig.entryProcessor())
                .writer(ecConfig.xmlWriter())
                .listener(ecConfig.logChunkListener())
                .listener(ecConfig.stepExecutionListener())
                .listener(ecConfig.itemReadListener())
                .listener(ecConfig.itemProcessListener())
                .listener(ecConfig.itemWriteListener())
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();

        return jobBuilderFactory.get(EnzymeCentricConfiguration.ENZYME_CENTRIC_XML_JOB)
                .start(uniqueEcStep)
                .listener(ecConfig.jobExecutionListener())
                .build();

    }

    @Bean
    public EnzymeCentricConfiguration enzymeCentricConfiguration() {
        return new EnzymeCentricConfiguration(entityManagerFactory, xmlFileProperties());
    }

    @Bean
    public XmlFileProperties xmlFileProperties() {
        return new XmlFileProperties();
    }

}
