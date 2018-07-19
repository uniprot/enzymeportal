package uk.ac.ebi.ep.xml.config;

import java.time.LocalDateTime;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.StaxWriterCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import uk.ac.ebi.ep.xml.entity.EnzymePortalUniqueEc;
import uk.ac.ebi.ep.xml.helper.CustomStaxEventItemWriter;
import uk.ac.ebi.ep.xml.helper.XmlFooterCallback;
import uk.ac.ebi.ep.xml.helper.XmlHeaderCallback;
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
public class EnzymeCentricConfiguration extends AbstractBatchConfig {


    private static final String NATIVE_COUNT_QUERY = "SELECT COUNT(*) FROM ENZYME_PORTAL_UNIQUE_EC WHERE TRANSFER_FLAG='N'";
    private static final String NATIVE_READ_QUERY = "SELECT * FROM ENZYME_PORTAL_UNIQUE_EC  WHERE TRANSFER_FLAG='N'";
    private static final String ROOT_TAG_NAME = "database";

    //demo//// delete after use
    // private static final String NATIVE_COUNT_QUERY = "SELECT COUNT(*) FROM ENZYME_PORTAL_UNIQUE_EC WHERE ROWNUM <= 2";
    //private static final String NATIVE_COUNT_QUERY = "SELECT COUNT(*) FROM ENZYME_PORTAL_UNIQUE_EC WHERE EC_FAMILY=6"; 
    // private static final String NATIVE_READ_QUERY = "SELECT /*+ PARALLEL */ * FROM ENZYME_PORTAL_UNIQUE_EC WHERE EC_FAMILY=6";
    //private static final String NATIVE_READ_QUERY = "SELECT /*+ PARALLEL */ * FROM ENZYME_PORTAL_UNIQUE_EC WHERE EC_NUMBER='1.1.1.1'";
       // private static final String NATIVE_READ_QUERY = "SELECT /*+ PARALLEL */ * FROM ENZYME_PORTAL_UNIQUE_EC WHERE EC_NUMBER='6.1.2.2'";
    
    //private static final String NATIVE_READ_QUERY = "SELECT /*+ PARALLEL */ * FROM ENZYME_PORTAL_UNIQUE_EC WHERE EC_NUMBER='3.6.1.3'";
    ///demo end /////

    private static final String pattern = "MMM_d_yyyy@hh:mma";
    private static final String date = DateTimeUtil.convertDateToString(LocalDateTime.now(), pattern);
    public static final String ENZYME_CENTRIC_XML_JOB = "ENZYME_CENTRIC_XML_JOB_" + date;
    public static final String ENZYME_READ_PROCESS_WRITE_XML_STEP = "enzymeReadProcessAndWriteXMLstep_"+date;

    protected final EntityManagerFactory entityManagerFactory;

    private final XmlFileProperties xmlFileProperties;

    @Autowired
    public EnzymeCentricConfiguration(EntityManagerFactory entityManagerFactory, XmlFileProperties xmlFileProperties) {
        this.entityManagerFactory = entityManagerFactory;
        this.xmlFileProperties = xmlFileProperties;
    }

    @Bean(destroyMethod = "")
    @Override
    public ItemReader<EnzymePortalUniqueEc> databaseReader() {

        return new JpaPagingItemReaderBuilder<EnzymePortalUniqueEc>()
                .name("READ_UNIQUE_EC")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(5)
                .queryProvider(createQueryProvider(NATIVE_READ_QUERY,EnzymePortalUniqueEc.class))
                 //.queryProvider(createQueryProvider())
                .build();

    }

//    private JpaNativeQueryProvider<EnzymePortalUniqueEc> createQueryProvider() {
//        JpaNativeQueryProvider<EnzymePortalUniqueEc> queryProvider = new JpaNativeQueryProvider<>();
//        queryProvider.setSqlQuery(NATIVE_READ_QUERY);
//        queryProvider.setEntityClass(EnzymePortalUniqueEc.class);
//        try {
//            queryProvider.afterPropertiesSet();
//        } catch (Exception ex) {
//            logger.error(ex.getMessage(), ex);
//        }
//        return queryProvider;
//    }

    @Override
    public ItemProcessor<EnzymePortalUniqueEc, Entry> entryProcessor() {

        return new EnzymeProcessor(xmlFileProperties);

    }

    @Bean(destroyMethod = "")
    @Override
    public ItemWriter<Entry> xmlWriter() {
        StaxEventItemWriter<Entry> xmlWriter = new CustomStaxEventItemWriter<>();

        //XmlFileUtils.createDirectory(xmlFileProperties.getDir());
        xmlWriter.setName("WRITE_XML_TO_FILE");
        xmlWriter.setResource(XmlOutputDir());
        xmlWriter.setRootTagName(ROOT_TAG_NAME);
        xmlWriter.setMarshaller(xmlMarshaller(Entry.class));
        xmlWriter.setHeaderCallback(xmlHeaderCallback(NATIVE_COUNT_QUERY));
        xmlWriter.setFooterCallback(new XmlFooterCallback());
        return xmlWriter;

    }

    @Bean
    @Override
    public Resource XmlOutputDir() {
        return new FileSystemResource(xmlFileProperties.getEnzymeCentric());
    }

    @Override
    public String countEntries(String countQuery) {
        // Query query = entityManagerFactory.createEntityManager().createQuery(countQuery);
        Query query = entityManagerFactory.createEntityManager().createNativeQuery(countQuery);
        return String.valueOf(query.getSingleResult());
    }

    @Override
    public StaxWriterCallback xmlHeaderCallback(String countQuery) {
        return new XmlHeaderCallback(xmlFileProperties.getReleaseNumber(), countEntries(countQuery));
    }

    @Override
    @Bean
    public JobExecutionListener jobExecutionListener() {
        return new JobCompletionNotificationListener(ENZYME_CENTRIC_XML_JOB);
    }

    @Override
    ChunkListener logChunkListener() {
        return new LogChunkListener(xmlFileProperties.getChunkSize());
    }

}
