package uk.ac.ebi.ep.xml;

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
import uk.ac.ebi.ep.xml.config.XmlFileProperties;
import uk.ac.ebi.ep.xml.entity.protein.ProteinGroups;
import uk.ac.ebi.ep.xml.helper.CustomStaxEventItemWriter;
import uk.ac.ebi.ep.xml.helper.XmlFooterCallback;
import uk.ac.ebi.ep.xml.helper.XmlHeaderCallback;
import uk.ac.ebi.ep.xml.listeners.JobCompletionNotificationListener;
import uk.ac.ebi.ep.xml.listeners.LogChunkListener;
import uk.ac.ebi.ep.xml.schema.Entry;
import uk.ac.ebi.ep.xml.transformer.ProteinGroupsProcessor;
import uk.ac.ebi.ep.xml.util.DateTimeUtil;

/**
 *
 * @author Joseph
 */
@Configuration
public class MockProteinCentricConfig extends MockAbstractBatchConfig {

    private static final String NATIVE_COUNT_QUERY = "SELECT COUNT(*) FROM(select * from PROTEIN_GROUPS where ENTRY_TYPE=0 AND ROWNUM <=1 UNION (select * from PROTEIN_GROUPS where ENTRY_TYPE=1 AND ROWNUM <=2))";

    private static final String ROOT_TAG_NAME = "database";

    private static final String NATIVE_READ_QUERY = "select * from PROTEIN_GROUPS where ENTRY_TYPE=0 and rownum<=1 \n"
            + "union\n"
            + "select * from PROTEIN_GROUPS where ENTRY_TYPE=1 and rownum<=2";
 
   //private static final String NATIVE_READ_QUERY =  "SELECT * FROM protein_groups WHERE protein_group_id='EI595W'";

    private static final String PATTERN = "MMM_d_yyyy@hh:mm:ssa";
    private static final String DATE = DateTimeUtil.convertDateToString(LocalDateTime.now(), PATTERN);
    public static final String PROTEIN_CENTRIC_XML_JOB = "PROTEIN_CENTRIC_XML_JOB_" + DATE;
    public static final String PROTEIN_READ_PROCESS_WRITE_XML_STEP = "proteinReadProcessAndWriteXMLstep_" + DATE;

    protected final EntityManagerFactory entityManagerFactory;

    private final XmlFileProperties xmlFileProperties;

    @Autowired
    public MockProteinCentricConfig(EntityManagerFactory entityManagerFactory, XmlFileProperties xmlFileProperties) {
        this.entityManagerFactory = entityManagerFactory;
        this.xmlFileProperties = xmlFileProperties;
    }

    @Override
    @Bean(destroyMethod = "", name = "proteinDatabaseReaderTest")
    public ItemReader<ProteinGroups> databaseReader() {

        return new JpaPagingItemReaderBuilder<ProteinGroups>()
                .name("READ_UNIQUE_PROTEIN_GROUP")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(5)
                .queryProvider(createQueryProvider(NATIVE_READ_QUERY, ProteinGroups.class))
                .build();
    }

    @Override
    public ItemProcessor<ProteinGroups, Entry> entryProcessor() {

        return new ProteinGroupsProcessor();

    }

    @Bean(destroyMethod = "", name = "proteinXmlWriterTest")
    @Override
    public ItemWriter<Entry> xmlWriter() {
        StaxEventItemWriter<Entry> xmlWriter = new CustomStaxEventItemWriter<>();

        //XmlFileUtils.createDirectory(xmlFileProperties.getDir());
        xmlWriter.setName("WRITE_PROTEIN_CENTRIC_XML_TO_FILE");
        xmlWriter.setResource(xmlOutputDir());
        xmlWriter.setRootTagName(ROOT_TAG_NAME);
        xmlWriter.setMarshaller(xmlMarshaller(Entry.class));
        xmlWriter.setHeaderCallback(xmlHeaderCallback(NATIVE_COUNT_QUERY));
        xmlWriter.setFooterCallback(new XmlFooterCallback());
        return xmlWriter;

    }

    @Bean(name = "proteinXmlOutputDirTest")
    @Override
    public Resource xmlOutputDir() {
        return new FileSystemResource(xmlFileProperties.getProteinCentric());
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
    @Bean(name = "proteinJobExecutionListenerTest")
    public JobExecutionListener jobExecutionListener() {
        return new JobCompletionNotificationListener(PROTEIN_CENTRIC_XML_JOB);
    }

    @Override
    ChunkListener logChunkListener() {
        return new LogChunkListener(xmlFileProperties.getChunkSize());
    }

}
