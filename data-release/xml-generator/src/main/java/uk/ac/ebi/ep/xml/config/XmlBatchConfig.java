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
import uk.ac.ebi.ep.xml.entities.EnzymePortalUniqueEc;
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
                .build();

        return jobBuilderFactory.get(ProteinCentricConfiguration.PROTEIN_CENTRIC_XML_JOB)
                .incrementer(new RunIdIncrementer())
                .start(proteinGroupStep)
                .listener(proteinConfig.jobExecutionListener())
                .build();

    }

    @Bean(name = "enzymeXmlJob")
    public Job enzymeXmlJob(JobBuilderFactory jobBuilderFactory,
            StepBuilderFactory stepBuilderFactory, EnzymeCentricConfiguration ecConfig) {

        Step uniqueEcStep = stepBuilderFactory.get(EnzymeCentricConfiguration.ENZYME_READ_PROCESS_WRITE_XML_STEP)
                .<EnzymePortalUniqueEc, Entry>chunk(xmlFileProperties.getChunkSize())
                .reader(ecConfig.databaseReader())
                .processor(ecConfig.entryProcessor())
                .writer(ecConfig.xmlWriter())
                .listener(ecConfig.logChunkListener())
                .build();

        return jobBuilderFactory.get(EnzymeCentricConfiguration.ENZYME_CENTRIC_XML_JOB)
                .start(uniqueEcStep)
                .listener(ecConfig.jobExecutionListener())
                .build();

    }

}
