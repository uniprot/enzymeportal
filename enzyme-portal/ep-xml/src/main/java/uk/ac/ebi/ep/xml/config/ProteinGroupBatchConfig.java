package uk.ac.ebi.ep.xml.config;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.xml.StaxWriterCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import uk.ac.ebi.ep.data.domain.ProteinGroups;
import uk.ac.ebi.ep.xml.generator.protein.ProteinXmlFooterCallback;
import uk.ac.ebi.ep.xml.generator.protein.ProteinXmlHeaderCallback;
import uk.ac.ebi.ep.xml.generator.proteinGroup.JobCompletionNotificationListener;
import uk.ac.ebi.ep.xml.generator.proteinGroup.ProteinGroupsProcessor;
import uk.ac.ebi.ep.xml.model.Entry;
import uk.ac.ebi.ep.xml.util.LogChunkListener;
import uk.ac.ebi.ep.xml.util.LogJobListener;
import uk.ac.ebi.ep.xml.util.PrettyPrintStaxEventItemWriter;
import uk.ac.ebi.ep.xml.util.XmlFileUtils;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Deprecated
@Configuration
@EnableBatchProcessing
@Import({XmlConfig.class})
@PropertySource(value = "classpath:ep-xml-config.properties", ignoreResourceNotFound = true)
public class ProteinGroupBatchConfig extends DefaultBatchConfigurer {

    public static final String READ_DATA_JOB = "READ_DATA_FROM_DB_JOB";
    public static final String READ_PROCESS_WRITE_XML_STEP = "readProcessAndWriteXMLstep";

    public static final int CHUNK_SIZE =20;
    
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private XmlConfigParams xmlConfigParams;

    @Bean
    public Resource proteinCentricXmlDir() {
        return new FileSystemResource(xmlConfigParams.getProteinCentricXmlDir());
    }

    @Bean
    public JobLauncher jobLauncher() {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(this.getJobRepository());

        return jobLauncher;
    }

    @Bean
    public Job readDataFromDBJob() {
        return jobBuilderFactory.get(READ_DATA_JOB)
                .incrementer(new RunIdIncrementer())
                .listener(jobExecutionListener())
                .flow(readProcessAndWriteXMLStep())
                .end()
                .build();
    }

    @Bean
    public Step readProcessAndWriteXMLStep() {
        return stepBuilderFactory.get(READ_PROCESS_WRITE_XML_STEP)
//                .<ProteinGroups, Entry>chunk(xmlConfigParams.getChunkSize())
                 .<ProteinGroups, Entry>chunk(CHUNK_SIZE)
                .reader(proteinGroupsReader())
                .processor(proteinGroupsToEntryProcessor())
                .writer(xmlWriter())
                  .listener(logChunkListener())
                .build();
    }
    //EP-XML
//    @Bean
//    public Job readDataFromDBJob() {
//        return jobBuilderFactory.get(READ_DATA_JOB)
//                .start(readProcessAndWriteXMLStep())
//                .listener(logJobListener(READ_DATA_JOB))
//                //.listener(jobExecutionListener())
//                .build();
//    }
//
//    @Bean
//    public Step readProcessAndWriteXMLStep() {
//        return stepBuilderFactory.get(READ_PROCESS_WRITE_XML_STEP)
//               // .<ProteinGroups, Entry>chunk(xmlConfigParams.getChunkSize())
//                .<ProteinGroups, Entry>chunk(CHUNK_SIZE)
//                .<ProteinGroups>reader(proteinGroupsReader())
//                .processor(proteinGroupsToEntryProcessor())
//                 //.writer(e -> e.forEach(entry->System.out.println(""+entry)))
//                .writer(xmlWriter())
//                .listener(logChunkListener())
//                .build();
//    }

    @Bean
    public ItemReader<ProteinGroups> proteinGroupsReader() {
        JpaPagingItemReader<ProteinGroups> databaseReader = new JpaPagingItemReader<>();
        databaseReader.setEntityManagerFactory(entityManagerFactory);
        databaseReader.setQueryString("select p from ProteinGroups p");
        databaseReader.setPageSize(xmlConfigParams.getChunkSize());
        databaseReader.setSaveState(false);
        return databaseReader;
    }

    @Bean
    public ItemProcessor<ProteinGroups, Entry> proteinGroupsToEntryProcessor() {
        return new ProteinGroupsProcessor(xmlConfigParams);

    }

    @Bean
    public JobExecutionListener jobExecutionListener() {
        return new JobCompletionNotificationListener(READ_DATA_JOB);
    }
    
        private JobExecutionListener logJobListener(String jobName) {
        return new LogJobListener(jobName);
    }
    
        private ChunkListener logChunkListener() {
        return new LogChunkListener(xmlConfigParams.getChunkSize());
    }

    //writer
    @Bean
    public ItemWriter<Entry> xmlWriter() {
        PrettyPrintStaxEventItemWriter<Entry> xmlWriter = new PrettyPrintStaxEventItemWriter<>();
        XmlFileUtils.createDirectory(xmlConfigParams.getXmlDir());
        xmlWriter.setResource(proteinCentricXmlDir());
        xmlWriter.setRootTagName("database");
        xmlWriter.setMarshaller(entryMarshaller());
        xmlWriter.setHeaderCallback(xmlHeaderCallback());
        xmlWriter.setFooterCallback(new ProteinXmlFooterCallback());
        return xmlWriter;
    }

    private StaxWriterCallback xmlHeaderCallback() {
        return new ProteinXmlHeaderCallback(xmlConfigParams.getReleaseNumber(), countProteinGroupEntries());
    }

    private String countProteinGroupEntries() {
        Query query = entityManagerFactory.createEntityManager().createQuery("select count(p.proteinGroupId) from ProteinGroups p");
        return String.valueOf(query.getSingleResult());
    }

    /**
     * Creates a job repository that uses an in memory map to register the job's
     * progress. This should be changed to use a real data source in the
     * following cases: - If you want to be able to resume a failed job - If you
     * want more than one of the same job (with the same parameters) to be
     * launched simultaneously - If you want to multi thread the job - If you
     * have a locally partitioned step.
     *
     * @return a JobRepository
     * @throws Exception if an error is encountered whilst creating the job repo
     */
    @Override
    protected JobRepository createJobRepository() throws Exception {
        MapJobRepositoryFactoryBean factory = new MapJobRepositoryFactoryBean();
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    private Marshaller entryMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(Entry.class);

        Map<String, Object> jaxbProps = new HashMap<>();
        jaxbProps.put(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        marshaller.setMarshallerProperties(jaxbProps);

        return marshaller;
    }
}
