package uk.ac.ebi.ep.xml.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.ac.ebi.ep.xml.entities.ProteinGroups;
import uk.ac.ebi.ep.xml.schema.Entry;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Configuration
@EnableBatchProcessing
@Slf4j
public class XmlBatchConfig {


    private final XmlFileProperties xmlFileProperties;

    @Autowired
    public XmlBatchConfig(XmlFileProperties xmlFileProperties) {
        this.xmlFileProperties = xmlFileProperties;
    }



    @Bean(name = "proteinXmlJob")
    public Job proteinXmlJob(JobBuilderFactory jobBuilderFactory,
            StepBuilderFactory stepBuilderFactory, ProteinCentricConfiguration proteinConfig) {

        Step proteinGroupStep = stepBuilderFactory.get(ProteinCentricConfiguration.PROTEIN_READ_PROCESS_WRITE_XML_STEP)
                .<ProteinGroups, Entry>chunk(xmlFileProperties.getChunkSize())
                .reader(proteinConfig.databaseReader())
                .processor(proteinConfig.entryProcessor())
                .writer(proteinConfig.xmlWriter())
                .listener(proteinConfig.logChunkListener())
                //.listener(proteinConfig.stepExecutionListener())
                //.listener(proteinConfig.itemReadListener())
                //.listener(proteinConfig.itemProcessListener())
                //.listener(proteinConfig.itemWriteListener())
                //.taskExecutor(new SimpleAsyncTaskExecutor())
                //.throttleLimit(8)
                .build();

        return jobBuilderFactory.get(ProteinCentricConfiguration.PROTEIN_CENTRIC_XML_JOB)
                .incrementer(new RunIdIncrementer())
                .start(proteinGroupStep)
                .listener(proteinConfig.jobExecutionListener())
                .build();
//        return jobBuilderFactory.get(ProteinCentricConfiguration.PROTEIN_CENTRIC_XML_JOB)
//                .incrementer(new RunIdIncrementer())
//                .listener(proteinConfig.jobExecutionListener())
//                .flow(proteinGroupStep).end().build();

    }


  
}
