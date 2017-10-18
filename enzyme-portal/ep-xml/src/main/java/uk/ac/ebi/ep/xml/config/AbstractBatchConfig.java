package uk.ac.ebi.ep.xml.config;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxWriterCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import uk.ac.ebi.ep.xml.generator.protein.ProteinXmlHeaderCallback;
import uk.ac.ebi.ep.xml.util.DateTimeUtil;
import uk.ac.ebi.ep.xml.util.GlobalListener;
import uk.ac.ebi.ep.xml.util.LogChunkListener;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 * @param <T> input data
 * @param <S> output data
 */
@Configuration
@EnableBatchProcessing
@Import({XmlConfig.class})
@PropertySource(value = "classpath:ep-xml-config.properties", ignoreResourceNotFound = true)
public abstract class AbstractBatchConfig<T, S> extends DefaultBatchConfigurer {

    public static final int CHUNK_SIZE = 100;
    private static final String pattern = "MMM_d_yyyy@hh:mma";
    private static final String date = DateTimeUtil.convertDateToString(LocalDateTime.now(), pattern);
    public static final String READ_DATA_JOB = "READ_DATA_FROM_DB_JOB_" + date;
    public static final String READ_PROCESS_WRITE_XML_STEP = "readProcessAndWriteXMLstep";

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    protected EntityManagerFactory entityManagerFactory;

    @Autowired
    protected XmlConfigParams xmlConfigParams;

    abstract Resource proteinCentricXmlDir();

    abstract JobLauncher jobLauncher();

    abstract Job readDataFromDBJob();

    abstract Step readProcessWriteStep();

    abstract ItemReader<T> databaseReader();

    abstract ItemProcessor<T, S> entryProcessor();

    abstract ItemWriter<S> xmlWriter();

    protected StaxWriterCallback xmlHeaderCallback(String countQuery) {
        return new ProteinXmlHeaderCallback(xmlConfigParams.getReleaseNumber(), countEntries(countQuery));
    }

    private String countEntries(String countQuery) {
       // Query query = entityManagerFactory.createEntityManager().createQuery(countQuery);
         Query query = entityManagerFactory.createEntityManager().createNativeQuery(countQuery);
        return String.valueOf(query.getSingleResult());
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

    abstract JobExecutionListener jobExecutionListener();

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

    protected Marshaller xmlMarshaller(Class<S> clazz) {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(clazz);

        Map<String, Object> jaxbProps = new HashMap<>();
        jaxbProps.put(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        marshaller.setMarshallerProperties(jaxbProps);

        return marshaller;
    }

}
