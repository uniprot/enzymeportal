package uk.ac.ebi.ep.xml.config;

import java.time.LocalDateTime;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.database.orm.JpaNativeQueryProvider;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.StaxWriterCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import uk.ac.ebi.ep.xml.entities.EnzymePortalUniqueEc;
import uk.ac.ebi.ep.xml.entities.repositories.ProteinXmlRepository;
import uk.ac.ebi.ep.xml.helper.CustomStaxEventItemWriter;
import uk.ac.ebi.ep.xml.helper.XmlFooterCallback;
import uk.ac.ebi.ep.xml.helper.XmlHeaderCallback;
import uk.ac.ebi.ep.xml.listeners.DatabaseReaderListener;
import uk.ac.ebi.ep.xml.listeners.JobCompletionNotificationListener;
import uk.ac.ebi.ep.xml.listeners.LogChunkListener;
import uk.ac.ebi.ep.xml.schema.Entry;
import uk.ac.ebi.ep.xml.transformer.EnzymeProcessor;
import uk.ac.ebi.ep.xml.util.DateTimeUtil;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Configuration
@Import(DataConfig.class)
public class EnzymeCentricConfiguration extends AbstractBatchConfig {

    private static final String NATIVE_COUNT_QUERY = "SELECT COUNT(*) FROM ENZYME_PORTAL_UNIQUE_EC WHERE TRANSFER_FLAG='N' OR TRANSFER_FLAG is null";
    private static final String NATIVE_READ_QUERY = "SELECT * FROM ENZYME_PORTAL_UNIQUE_EC  WHERE TRANSFER_FLAG='N' OR TRANSFER_FLAG is null";
    private static final String ROOT_TAG_NAME = "database";

    ////------- TEST QUERY --------
    //private static final String NATIVE_READ_QUERY = "SELECT * FROM ENZYME_PORTAL_UNIQUE_EC WHERE TRANSFER_FLAG='N' OR TRANSFER_FLAG is null and rownum<=1";
    //private static final String NATIVE_READ_QUERY ="SELECT * FROM ENZYME_PORTAL_UNIQUE_EC where EC_NUMBER='1.1.1.1'"; //3.2.1.21
    //private static final String NATIVE_READ_QUERY ="SELECT * FROM ENZYME_PORTAL_UNIQUE_EC where EC_NUMBER='3.1.4.35'"; //3.2.1.21
    //private static final String NATIVE_READ_QUERY ="SELECT * FROM ENZYME_PORTAL_UNIQUE_EC where EC_NUMBER='2.7.7.7'"; //3.2.1.21
    //private static final String NATIVE_READ_QUERY ="SELECT * FROM ENZYME_PORTAL_UNIQUE_EC where EC_NUMBER='3.5.1.-'"; //3.2.1.21
    /// end TEST QUERY /////
    private static final String PATTERN = "MMM_d_yyyy@hh:mma";
    private static final String DATE = DateTimeUtil.convertDateToString(LocalDateTime.now(), PATTERN);
    public static final String ENZYME_CENTRIC_XML_JOB = "ENZYME_CENTRIC_XML_JOB_" + DATE;
    public static final String ENZYME_READ_PROCESS_WRITE_XML_STEP = "enzymeReadProcessAndWriteXMLstep_" + DATE;

    protected final EntityManagerFactory entityManagerFactory;

    private final XmlFileProperties xmlFileProperties;
    private final ProteinXmlRepository proteinXmlRepository;

    @Autowired
    public EnzymeCentricConfiguration(EntityManagerFactory entityManagerFactory, XmlFileProperties xmlFileProperties, ProteinXmlRepository proteinXmlRepository) {
        this.entityManagerFactory = entityManagerFactory;
        this.xmlFileProperties = xmlFileProperties;
        this.proteinXmlRepository = proteinXmlRepository;
    }

    @Bean(destroyMethod = "", name = "enzymeDatabaseReader")
    @Override
    public JpaPagingItemReader<EnzymePortalUniqueEc> databaseReader() {
        JpaNativeQueryProvider<EnzymePortalUniqueEc> queryProvider = createQueryProvider(NATIVE_READ_QUERY, EnzymePortalUniqueEc.class);

        return new JpaPagingItemReaderBuilder<EnzymePortalUniqueEc>()
                .name("READ_UNIQUE_EC_" + DATE)
                .entityManagerFactory(entityManagerFactory)
                .pageSize(xmlFileProperties.getPageSize())
                .queryProvider(queryProvider)
                .saveState(false)
                .transacted(false)
                .build();

    }

    @Override
    public ItemProcessor<EnzymePortalUniqueEc, Entry> entryProcessor() {

        return new EnzymeProcessor(proteinXmlRepository);

    }

    @Bean(destroyMethod = "", name = "enzymeXmlWriter")
    @Override
    public ItemWriter<Entry> xmlWriter() {
        StaxEventItemWriter<Entry> xmlWriter = new CustomStaxEventItemWriter<>();

        xmlWriter.setName("WRITE_XML_TO_FILE");
        xmlWriter.setResource(xmlOutputDir());
        xmlWriter.setRootTagName(ROOT_TAG_NAME);
        xmlWriter.setMarshaller(xmlMarshaller(Entry.class));
        xmlWriter.setHeaderCallback(xmlHeaderCallback(NATIVE_COUNT_QUERY));
        xmlWriter.setFooterCallback(new XmlFooterCallback());
        return xmlWriter;

    }

    @Bean(name = "enzymeXmlOutputDir")
    @Override
    public Resource xmlOutputDir() {
        return new FileSystemResource(xmlFileProperties.getEnzymeCentric());
    }

    @Override
    public String countEntries(String countQuery) {
        Query query = entityManagerFactory.createEntityManager().createNativeQuery(countQuery);
        return String.valueOf(query.getSingleResult());
    }

    @Override
    public StaxWriterCallback xmlHeaderCallback(String countQuery) {
        return new XmlHeaderCallback(xmlFileProperties.getReleaseNumber(), countEntries(countQuery));
    }

    @Override
    @Bean(name = "enzymeJobExecutionListener")
    public JobExecutionListener jobExecutionListener() {
        return new JobCompletionNotificationListener(ENZYME_CENTRIC_XML_JOB);
    }

    @Override
    ChunkListener logChunkListener() {
        return new LogChunkListener(xmlFileProperties.getChunkSize());
    }

    @Override
    ItemReadListener itemReadListener() {

        return new DatabaseReaderListener<EnzymePortalUniqueEc>();

    }

}
