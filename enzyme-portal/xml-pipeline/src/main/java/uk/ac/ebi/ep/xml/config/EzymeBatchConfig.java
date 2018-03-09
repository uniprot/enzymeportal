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
public class EzymeBatchConfig {

    @Autowired
    protected EntityManagerFactory entityManagerFactory;

//    @Bean(name = "xmlJob")
//    public Job job(JobBuilderFactory jobBuilderFactory,
//            StepBuilderFactory stepBuilderFactory, EnzymePortalUniqueEcConfiguration ecConfiguration) throws Exception {
//
//        Step uniqueEcStep = stepBuilderFactory.get(READ_PROCESS_WRITE_XML_STEP)
//                .<EnzymePortalUniqueEc, Entry>chunk(CHUNK_SIZE)
//                .reader(ecConfiguration.databaseReader())
//                .processor(ecConfiguration.entryProcessor())
//                .writer(ecConfiguration.xmlWriter())
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
    @Bean(name = "xmlJob")
    public Job job(JobBuilderFactory jobBuilderFactory,
            StepBuilderFactory stepBuilderFactory, EnzymePortalUniqueEcConfiguration ecConfiguration) throws Exception {

        Step uniqueEcStep = stepBuilderFactory.get(EnzymePortalUniqueEcConfiguration.ENZYME_READ_PROCESS_WRITE_XML_STEP)
                .<EnzymePortalUniqueEc, Entry>chunk(xmlFileProperties().getChunkSize())
                .reader(ecConfiguration.databaseReader())
                .processor(ecConfiguration.entryProcessor())
                .writer(ecConfiguration.xmlWriter())
                .listener(ecConfiguration.logChunkListener())
                .listener(ecConfiguration.stepExecutionListener())
                .listener(ecConfiguration.itemReadListener())
                .listener(ecConfiguration.itemProcessListener())
                .listener(ecConfiguration.itemWriteListener())
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();

        return jobBuilderFactory.get(EnzymePortalUniqueEcConfiguration.ENZYME_CENTRIC_XML_JOB)
                .start(uniqueEcStep)
                .listener(ecConfiguration.jobExecutionListener())
                .build();

    }

    @Bean
    public EnzymePortalUniqueEcConfiguration enzymePortalUniqueEcConfiguration() {
        return new EnzymePortalUniqueEcConfiguration(entityManagerFactory, xmlFileProperties());
    }

    @Bean
    public XmlFileProperties xmlFileProperties() {
        return new XmlFileProperties();
    }

}
