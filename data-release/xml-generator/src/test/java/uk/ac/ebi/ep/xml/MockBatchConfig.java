package uk.ac.ebi.ep.xml;

import javax.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.ac.ebi.ep.xml.config.XmlFileProperties;
import uk.ac.ebi.ep.xml.entities.EnzymePortalUniqueEc;
import uk.ac.ebi.ep.xml.entities.ProteinGroups;
import uk.ac.ebi.ep.xml.schema.Entry;

/**
 *
 * @author Joseph
 */
@Configuration
@EnableBatchProcessing
public class MockBatchConfig {

    @Autowired
    protected EntityManagerFactory entityManagerFactory;
    @Autowired
    private XmlFileProperties xmlFileProperties;
    


    @Bean(name = "enzymeXmlJobTest")
    public Job enzymeXmlJobTest(JobBuilderFactory jobBuilderFactory,
            StepBuilderFactory stepBuilderFactory, MockEnzymeCentricConfig ecConfig) throws Exception {

   
        Step uniqueEcStep = stepBuilderFactory.get(MockEnzymeCentricConfig.ENZYME_READ_PROCESS_WRITE_XML_STEP)
                .<EnzymePortalUniqueEc, Entry>chunk(xmlFileProperties.getChunkSize())
                .reader(ecConfig.databaseReader())
                .processor(ecConfig.entryProcessor())
                .writer(ecConfig.xmlWriter())
                .listener(ecConfig.logChunkListener())
                .listener(ecConfig.stepExecutionListener())
                .listener(ecConfig.itemReadListener())
                .listener(ecConfig.itemProcessListener())
                //.listener(ecConfig.itemWriteListener())
                .build();

        return jobBuilderFactory.get(MockEnzymeCentricConfig.ENZYME_CENTRIC_XML_JOB)
                .start(uniqueEcStep)
                .listener(ecConfig.jobExecutionListener())
                .build();

    }

    @Bean(name = "proteinXmlJobTest")
    public Job proteinXmlJobTest(JobBuilderFactory jobBuilderFactory,
            StepBuilderFactory stepBuilderFactory, MockProteinCentricConfig proteinConfig) throws Exception {

        Step proteinGroupStep = stepBuilderFactory.get(MockProteinCentricConfig.PROTEIN_READ_PROCESS_WRITE_XML_STEP)
                .<ProteinGroups, Entry>chunk(xmlFileProperties.getChunkSize())
                .reader(proteinConfig.databaseReader())
                .processor(proteinConfig.entryProcessor())
                .writer(proteinConfig.xmlWriter())
                .listener(proteinConfig.logChunkListener())
                .listener(proteinConfig.stepExecutionListener())
                .listener(proteinConfig.itemReadListener())
                .listener(proteinConfig.itemProcessListener())
                //.listener(proteinConfig.itemWriteListener())
                .build();

        return jobBuilderFactory.get(MockProteinCentricConfig.PROTEIN_CENTRIC_XML_JOB)
                .start(proteinGroupStep)
                .listener(proteinConfig.jobExecutionListener())
                .build();

    }
}
