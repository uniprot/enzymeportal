
package uk.ac.ebi.ep.xml.config;

import java.time.LocalDateTime;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ItemReadListener;
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
import uk.ac.ebi.ep.xml.entity.protein.UniprotEntry;
import uk.ac.ebi.ep.xml.helper.CustomStaxEventItemWriter;
import uk.ac.ebi.ep.xml.helper.XmlFooterCallback;
import uk.ac.ebi.ep.xml.helper.XmlHeaderCallback;
import uk.ac.ebi.ep.xml.listeners.DatabaseReaderListener;
import uk.ac.ebi.ep.xml.listeners.JobCompletionNotificationListener;
import uk.ac.ebi.ep.xml.listeners.LogChunkListener;
import uk.ac.ebi.ep.xml.schema.Entry;
import uk.ac.ebi.ep.xml.transformer.ProteinProcessor;
import uk.ac.ebi.ep.xml.util.DateTimeUtil;

/**
 *
 * @author joseph
 */
@Configuration
public class ProteinConfiguration extends AbstractBatchConfig {
   
    
    private static final String NATIVE_COUNT_QUERY = "SELECT COUNT(*) FROM UNIPROT_ENTRY";
   
    private static final String ROOT_TAG_NAME = "database";
    private static final String NATIVE_READ_QUERY = "SELECT * FROM UNIPROT_ENTRY";

    //------- TEST QUERY --------

      //private static final String NATIVE_READ_QUERY  = "SELECT * FROM UNIPROT_ENTRY WHERE ACCESSION='K0NQ24'";
       // private static final String NATIVE_READ_QUERY  = "SELECT * FROM UNIPROT_ENTRY WHERE ACCESSION='O76074'";
    // END -- TEST QUERY ----
    private static final String PATTERN = "MMM_d_yyyy@hh:mma";
    private static final String DATE = DateTimeUtil.convertDateToString(LocalDateTime.now(), PATTERN);
    public static final String UNIPROT_ENTRY_XML_JOB = "UNIPROT_ENTRY_XML_JOB_" + DATE;
    public static final String UNPROT_ENTRY_READ_PROCESS_WRITE_XML_STEP = "unirpotEntryReadProcessAndWriteXMLstep_" + DATE;

    protected final EntityManagerFactory entityManagerFactory;

    private final XmlFileProperties xmlFileProperties;

    @Autowired
    public ProteinConfiguration(EntityManagerFactory entityManagerFactory, XmlFileProperties xmlFileProperties) {
        this.entityManagerFactory = entityManagerFactory;
        this.xmlFileProperties = xmlFileProperties;
    }

    @Override
    @Bean(destroyMethod = "", name = "uniprotEntryDatabaseReader")
    public ItemReader<UniprotEntry> databaseReader() {

        return new JpaPagingItemReaderBuilder<UniprotEntry>()
                .name("READ_UNIPROT_ENTRY")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(xmlFileProperties.getPageSize())
                .queryProvider(createQueryProvider(NATIVE_READ_QUERY, UniprotEntry.class))
                .saveState(false)
                .transacted(false)
                .build();
    }

    @Override
    public ItemProcessor<UniprotEntry, Entry> entryProcessor() {

        return new ProteinProcessor();

    }

    @Bean(destroyMethod = "", name = "uniprotEntryXmlWriter")
    @Override
    public ItemWriter<Entry> xmlWriter() {
        StaxEventItemWriter<Entry> xmlWriter = new CustomStaxEventItemWriter<>();

        //XmlFileUtils.createDirectory(xmlFileProperties.getDir());
        xmlWriter.setName("WRITE_UNIPROT_ENTRY_XML_TO_FILE");
        xmlWriter.setResource(xmlOutputDir());
        xmlWriter.setRootTagName(ROOT_TAG_NAME);
        xmlWriter.setMarshaller(xmlMarshaller(Entry.class));
        xmlWriter.setHeaderCallback(xmlHeaderCallback(NATIVE_COUNT_QUERY));
        xmlWriter.setFooterCallback(new XmlFooterCallback());
        return xmlWriter;

    }

    @Bean(name = "uniprotEntryXmlOutputDir")
    @Override
    public Resource xmlOutputDir() {
        return new FileSystemResource(xmlFileProperties.getProteinEntry());
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
    @Bean(name = "uniprotEntryJobExecutionListener")
    public JobExecutionListener jobExecutionListener() {
        return new JobCompletionNotificationListener(UNIPROT_ENTRY_XML_JOB);
    }

    @Override
    ChunkListener logChunkListener() {
        return new LogChunkListener(xmlFileProperties.getChunkSize());
    }

    @Override
    ItemReadListener itemReadListener() {

        return new DatabaseReaderListener<UniprotEntry>();

    }
}
