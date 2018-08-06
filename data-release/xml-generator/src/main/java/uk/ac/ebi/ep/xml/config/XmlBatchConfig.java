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
import uk.ac.ebi.ep.xml.entity.enzyme.EnzymePortalUniqueEc;
import uk.ac.ebi.ep.xml.entity.protein.ProteinGroups;
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

    @Bean(name = "enzymeXmlJob")
    public Job enzymeXmlJob(JobBuilderFactory jobBuilderFactory,
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

    @Bean(name = "proteinXmlJob")
    public Job proteinXmlJob(JobBuilderFactory jobBuilderFactory,
            StepBuilderFactory stepBuilderFactory, ProteinCentricConfiguration proteinConfig) throws Exception {

        Step proteinGroupStep = stepBuilderFactory.get(ProteinCentricConfiguration.PROTEIN_READ_PROCESS_WRITE_XML_STEP)
                .<ProteinGroups, Entry>chunk(xmlFileProperties().getChunkSize())
                .reader(proteinConfig.databaseReader())
                .processor(proteinConfig.entryProcessor())
                .writer(proteinConfig.xmlWriter())
                .listener(proteinConfig.logChunkListener())
                .listener(proteinConfig.stepExecutionListener())
                .listener(proteinConfig.itemReadListener())
                .listener(proteinConfig.itemProcessListener())
                .listener(proteinConfig.itemWriteListener())
                .build();

        return jobBuilderFactory.get(ProteinCentricConfiguration.PROTEIN_CENTRIC_XML_JOB)
                .start(proteinGroupStep)
                .listener(proteinConfig.jobExecutionListener())
                .build();

    }

    @Bean
    public ProteinCentricConfiguration proteinCentricConfiguration() {
        return new ProteinCentricConfiguration(entityManagerFactory, xmlFileProperties());
    }

    @Bean
    public XmlFileProperties xmlFileProperties() {
        return new XmlFileProperties();
    }

}
