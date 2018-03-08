package uk.ac.ebi.ep.xml.config;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.database.orm.JpaNativeQueryProvider;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.StaxWriterCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import uk.ac.ebi.ep.xml.entity.EnzymePortalUniqueEc;
import uk.ac.ebi.ep.xml.helper.CustomStaxEventItemWriter;
import uk.ac.ebi.ep.xml.helper.XmlFooterCallback;
import uk.ac.ebi.ep.xml.helper.XmlHeaderCallback;
import uk.ac.ebi.ep.xml.schema.Entry;
import uk.ac.ebi.ep.xml.transformer.UniqueEcProcessor;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Configuration
public class EnzymePortalUniqueEcConfiguration {

    protected static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(EnzymePortalUniqueEcConfiguration.class);

    //private static final String READ_QUERY = "select p from ProteinGroups p";
       // private static final String NATIVE_COUNT_QUERY = "SELECT COUNT(*) FROM ENZYME_PORTAL_UNIQUE_EC WHERE ROWNUM <= 2";
        //private static final String NATIVE_COUNT_QUERY = "SELECT COUNT(*) FROM ENZYME_PORTAL_UNIQUE_EC WHERE EC_FAMILY=6";
    private static final String NATIVE_COUNT_QUERY = "SELECT COUNT(*) FROM ENZYME_PORTAL_UNIQUE_EC";
    private static final String ROOT_TAG_NAME = "database";
     private static final String NATIVE_READ_QUERY = "SELECT /*+ PARALLEL */ * FROM ENZYME_PORTAL_EC_NUMBERS";
   // private static final String NATIVE_READ_QUERY = "SELECT /*+ PARALLEL */ * FROM ENZYME_PORTAL_EC_NUMBERS WHERE EC_FAMILY=6";
    //private static final String NATIVE_READ_QUERY = "SELECT /*+ PARALLEL */ * FROM ENZYME_PORTAL_UNIQUE_EC WHERE EC_NUMBER='1.1.1.1'";

    protected final EntityManagerFactory entityManagerFactory;

    private final XmlFileProperties xmlFileProperties;

    @Autowired
    public EnzymePortalUniqueEcConfiguration(EntityManagerFactory entityManagerFactory, XmlFileProperties xmlFileProperties) {
        this.entityManagerFactory = entityManagerFactory;
        this.xmlFileProperties = xmlFileProperties;
    }


    @Bean(destroyMethod = "")
    public ItemReader<EnzymePortalUniqueEc> databaseReader() {

        return new JpaPagingItemReaderBuilder<EnzymePortalUniqueEc>()
                .name("READ_UNIQUE_EC")
                .entityManagerFactory(entityManagerFactory)
                //.pageSize(50)
                .queryProvider(createQueryProvider())
                .build();

    }

    private JpaNativeQueryProvider<EnzymePortalUniqueEc> createQueryProvider() {
        JpaNativeQueryProvider<EnzymePortalUniqueEc> queryProvider = new JpaNativeQueryProvider<>();
        queryProvider.setSqlQuery(NATIVE_READ_QUERY);
        queryProvider.setEntityClass(EnzymePortalUniqueEc.class);
        try {
            queryProvider.afterPropertiesSet();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return queryProvider;
    }

    @Bean(destroyMethod = "")
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

    protected Marshaller xmlMarshaller(Class<Entry> clazz) {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

        marshaller.setClassesToBeBound(clazz);
        //marshaller.setPackagesToScan("uk.ac.ebi.ep.xml.schema");

        Map<String, Object> jaxbProps = new HashMap<>();
        jaxbProps.put(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        marshaller.setMarshallerProperties(jaxbProps);

        return marshaller;
    }

    @Bean
    public Resource XmlOutputDir() {
        return new FileSystemResource(xmlFileProperties.getEnzymeCentric());
    }

    private String countEntries(String countQuery) {
        // Query query = entityManagerFactory.createEntityManager().createQuery(countQuery);
        Query query = entityManagerFactory.createEntityManager().createNativeQuery(countQuery);
        return String.valueOf(query.getSingleResult());
    }

    protected StaxWriterCallback xmlHeaderCallback(String countQuery) {
        return new XmlHeaderCallback(xmlFileProperties.getReleaseNumber(), countEntries(countQuery));
    }

    //processor
    public ItemProcessor<EnzymePortalUniqueEc, Entry> entryProcessor() {

        return new UniqueEcProcessor(xmlFileProperties);
        // return new EntryProcessor(xmlFileProperties);
    }

}
