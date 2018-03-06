package uk.ac.ebi.ep.xml.config;

import java.time.LocalDateTime;
import javax.persistence.EntityManagerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.ac.ebi.ep.xml.entity.EnzymePortalUniqueEc;
import uk.ac.ebi.ep.xml.listeners.GlobalListener;
import uk.ac.ebi.ep.xml.listeners.JobCompletionNotificationListener;
import uk.ac.ebi.ep.xml.listeners.LogChunkListener;
import uk.ac.ebi.ep.xml.schema.Entry;
import uk.ac.ebi.ep.xml.util.DateTimeUtil;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Configuration
@EnableBatchProcessing
public class EzymeBatchConfig {

    public static final int CHUNK_SIZE = 100;
    private static final String pattern = "MMM_d_yyyy@hh:mma";
    private static final String date = DateTimeUtil.convertDateToString(LocalDateTime.now(), pattern);
    public static final String XML_JOB = "XML_JOB_" + date;
    public static final String READ_PROCESS_WRITE_XML_STEP = "readProcessAndWriteXMLstep";

    @Autowired
    protected EntityManagerFactory entityManagerFactory;
 

    @Bean(name = "xmlJob")
    public Job job(JobBuilderFactory jobBuilderFactory,
            StepBuilderFactory stepBuilderFactory, EnzymePortalUniqueEcConfiguration ecConfiguration) throws Exception {

        Step uniqueEcStep = stepBuilderFactory.get(READ_PROCESS_WRITE_XML_STEP)
                .<EnzymePortalUniqueEc, Entry>chunk(CHUNK_SIZE)
                .reader(ecConfiguration.databaseReader())
                .processor(ecConfiguration.entryProcessor())
                .writer(ecConfiguration.xmlWriter())
                .listener(logChunkListener())
                .listener(stepExecutionListener())
                .listener(itemReadListener())
                .listener(itemProcessListener())
                .listener(itemWriteListener())
                .build();

        return jobBuilderFactory.get(XML_JOB)
                .start(uniqueEcStep)
                .listener(jobExecutionListener())
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

    @Bean
    public JobExecutionListener jobExecutionListener() {
        return new JobCompletionNotificationListener(XML_JOB);
    }

    protected ChunkListener logChunkListener() {
        return new LogChunkListener(CHUNK_SIZE);
    }

    protected StepExecutionListener stepExecutionListener() {
        return new GlobalListener<>();
    }

    protected ItemReadListener itemReadListener() {
        return new GlobalListener<>();
    }

    protected ItemProcessListener itemProcessListener() {
        return new GlobalListener<>();
    }

    protected ItemWriteListener itemWriteListener() {
        return new GlobalListener<>();
    }
}
