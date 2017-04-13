package uk.ac.ebi.ep.xml.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import uk.ac.ebi.ep.data.domain.ProteinGroups;
import static uk.ac.ebi.ep.xml.config.AbstractBatchConfig.READ_DATA_JOB;
import uk.ac.ebi.ep.xml.generator.protein.ProteinXmlFooterCallback;
import uk.ac.ebi.ep.xml.generator.proteinGroup.JobCompletionNotificationListener;
import uk.ac.ebi.ep.xml.generator.proteinGroup.ProteinGroupsProcessor;
import uk.ac.ebi.ep.xml.model.Entry;
import uk.ac.ebi.ep.xml.util.PrettyPrintStaxEventItemWriter;
import uk.ac.ebi.ep.xml.util.XmlFileUtils;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class ProteinGroupsBatchConfig extends AbstractBatchConfig<ProteinGroups, Entry> {

    private static final String READ_QUERY = "select p from ProteinGroups p";
    private static final String COUNT_QUERY = "select count(p.proteinGroupId) from ProteinGroups p";
    private static final String ROOT_TAG_NAME = "database";

    @Bean
    @Override
    public Resource proteinCentricXmlDir() {
        return new FileSystemResource(xmlConfigParams.getProteinCentricXmlDir());
    }

    @Bean
    @Override
    public JobLauncher jobLauncher() {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(this.getJobRepository());

        return jobLauncher;
    }

    @Bean
    @Override
    public Job readDataFromDBJob() {
//        return jobBuilderFactory.get(READ_DATA_JOB)
//                .incrementer(new RunIdIncrementer())
//                .listener(jobExecutionListener())
//                .flow(readProcessWriteStep())
//                .end()
//                .build();

        return jobBuilderFactory.get(READ_DATA_JOB)
                .start(readProcessWriteStep())
                .listener(jobExecutionListener())
                .build();

    }

    @Override
    public ItemProcessor<ProteinGroups, Entry> entryProcessor() {

        return new ProteinGroupsProcessor(xmlConfigParams);
    }

    @Override
    @Bean
    public Step readProcessWriteStep() {

        return stepBuilderFactory.get(READ_PROCESS_WRITE_XML_STEP)
                .<ProteinGroups, Entry>chunk(CHUNK_SIZE)
                .reader(databaseReader())
                .processor(entryProcessor())
                .writer(xmlWriter())
                .listener(logChunkListener())
                .build();

    }

    @Override
    @Bean
    public ItemReader<ProteinGroups> databaseReader() {

        JpaPagingItemReader<ProteinGroups> databaseReader = new JpaPagingItemReader<>();
        databaseReader.setEntityManagerFactory(entityManagerFactory);
        databaseReader.setQueryString(READ_QUERY);
        databaseReader.setPageSize(xmlConfigParams.getChunkSize());
        databaseReader.setSaveState(false);
        return databaseReader;
    }

    @Override
    @Bean
    public ItemWriter<Entry> xmlWriter() {
        PrettyPrintStaxEventItemWriter<Entry> xmlWriter = new PrettyPrintStaxEventItemWriter<>();
        XmlFileUtils.createDirectory(xmlConfigParams.getXmlDir());
        xmlWriter.setResource(proteinCentricXmlDir());
        xmlWriter.setRootTagName(ROOT_TAG_NAME);
        xmlWriter.setMarshaller(xmlMarshaller(Entry.class));
        xmlWriter.setHeaderCallback(xmlHeaderCallback(COUNT_QUERY));
        xmlWriter.setFooterCallback(new ProteinXmlFooterCallback());
        return xmlWriter;
    }

    @Bean
    @Override
    public JobExecutionListener jobExecutionListener() {
        return new JobCompletionNotificationListener(READ_DATA_JOB);
    }

}
