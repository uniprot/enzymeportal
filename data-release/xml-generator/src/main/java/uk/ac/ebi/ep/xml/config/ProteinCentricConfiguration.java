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
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.StaxWriterCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import uk.ac.ebi.ep.xml.entities.ProteinGroups;
import uk.ac.ebi.ep.xml.entities.repositories.ProteinXmlRepository;
import uk.ac.ebi.ep.xml.helper.PrettyPrintStaxEventItemWriter;
import uk.ac.ebi.ep.xml.helper.XmlFooterCallback;
import uk.ac.ebi.ep.xml.helper.XmlHeaderCallback;
import uk.ac.ebi.ep.xml.listeners.DatabaseReaderListener;
import uk.ac.ebi.ep.xml.listeners.JobCompletionNotificationListener;
import uk.ac.ebi.ep.xml.listeners.LogChunkListener;
import uk.ac.ebi.ep.xml.schema.Entry;
import uk.ac.ebi.ep.xml.transformer.ProteinGroupsProcessor;
import uk.ac.ebi.ep.xml.util.DateTimeUtil;
import uk.ac.ebi.ep.xml.util.XmlFileUtils;

/**
 *
 * @author Joseph
 */
@Configuration
public class ProteinCentricConfiguration extends AbstractBatchConfig {

    private static final String NATIVE_COUNT_QUERY = "SELECT COUNT(*) FROM PROTEIN_GROUPS";

    private static final String ROOT_TAG_NAME = "database";
    private static final String NATIVE_READ_QUERY = "SELECT * FROM PROTEIN_GROUPS";
    //private static final String NATIVE_READ_QUERY = "SELECT * FROM PROTEIN_GROUPS WHERE PROTEIN_GROUP_ID='E99MXF'";
    //private static final String NATIVE_READ_QUERY = "SELECT * FROM PROTEIN_GROUPS WHERE PROTEIN_GROUP_ID='E76XC1'";
    //private static final String NATIVE_READ_QUERY = "SELECT * FROM PROTEIN_GROUPS WHERE PROTEIN_GROUP_ID='EOCUP4'";

    private static final String PATTERN = "MMM_d_yyyy@hh:mma";
    private static final String DATE = DateTimeUtil.convertDateToString(LocalDateTime.now(), PATTERN);
    public static final String PROTEIN_CENTRIC_XML_JOB = "PROTEIN_CENTRIC_XML_JOB_" + DATE;
    public static final String PROTEIN_READ_PROCESS_WRITE_XML_STEP = "proteinReadProcessAndWriteXMLstep_" + DATE;

    protected final EntityManagerFactory entityManagerFactory;

    private final XmlFileProperties xmlFileProperties;

    private final ProteinXmlRepository proteinXmlRepository;

    @Autowired
    public ProteinCentricConfiguration(EntityManagerFactory entityManagerFactory, XmlFileProperties xmlFileProperties, ProteinXmlRepository proteinXmlRepository) {
        this.entityManagerFactory = entityManagerFactory;
        this.xmlFileProperties = xmlFileProperties;
        this.proteinXmlRepository = proteinXmlRepository;

    }

    @Bean(destroyMethod = "", name = "proteinDatabaseReader")
    @Override
    public JpaPagingItemReader<ProteinGroups> databaseReader() {

        return new JpaPagingItemReaderBuilder<ProteinGroups>()
                .name("READ_UNIQUE_PROTEIN_GROUPS_" + DATE)
                .entityManagerFactory(entityManagerFactory)
                .queryProvider(createQueryProvider(NATIVE_READ_QUERY, ProteinGroups.class))
                .pageSize(xmlFileProperties.getPageSize())
                .saveState(false)
                .transacted(false)
                .build();
    }

    @Override
    public ItemProcessor<ProteinGroups, Entry> entryProcessor() {

        return new ProteinGroupsProcessor(proteinXmlRepository);

    }

    @Bean(destroyMethod = "", name = "proteinXmlWriter")
    @Override
    public ItemWriter<Entry> xmlWriter() {
        StaxEventItemWriter<Entry> xmlWriter = new PrettyPrintStaxEventItemWriter<>();// new CustomStaxEventItemWriter<>();

        xmlWriter.setName("WRITE_PROTEIN_CENTRIC_XML_TO_FILE_" + DATE);
        xmlWriter.setResource(xmlOutputDir());
        xmlWriter.setRootTagName(ROOT_TAG_NAME);
        xmlWriter.setMarshaller(xmlMarshaller(Entry.class));
        xmlWriter.setHeaderCallback(xmlHeaderCallback(NATIVE_COUNT_QUERY));
        xmlWriter.setFooterCallback(new XmlFooterCallback());
        xmlWriter.setSaveState(false);
        xmlWriter.setTransactional(false);
        return xmlWriter;

    }

    @Bean(name = "proteinXmlOutputDir")
    @Override
    public Resource xmlOutputDir() {
        XmlFileUtils.createDirectory(xmlFileProperties.getProteinCentric());
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

    @Bean(name = "proteinJobExecutionListener")
    @Override
    public JobExecutionListener jobExecutionListener() {
        return new JobCompletionNotificationListener(PROTEIN_CENTRIC_XML_JOB);
    }

    @Override
    ChunkListener logChunkListener() {
        return new LogChunkListener(xmlFileProperties.getChunkSize());
    }

    @Override
    ItemReadListener itemReadListener() {

        return new DatabaseReaderListener<ProteinGroups>();

    }

}
