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
import org.springframework.batch.item.database.orm.JpaNativeQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import uk.ac.ebi.ep.model.ProteinGroups;
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

    protected static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ProteinGroupsBatchConfig.class);

    private static final String READ_QUERY = "select p from ProteinGroups p";
    private static final String COUNT_QUERY = "select count(p.proteinGroupId) from ProteinGroups p";
     //private static final String COUNT_QUERY = "select count(PROTEIN_GROUP_ID) from PROTEIN_GROUPS";
    private static final String ROOT_TAG_NAME = "database";
     private static final String NATIVE_QUERY = "SELECT * FROM PROTEIN_GROUPS";
// private static final String NATIVE_QUERY = "SELECT * FROM PROTEIN_GROUPS WHERE ROWNUM <= 500";

      //private static final String NATIVE_QUERY = "SELECT * FROM PROTEIN_GROUPS WHERE ENTRY_TYPE=0";
 
    // private static final String NATIVE_QUERY = "SELECT * FROM PROTEIN_GROUPS WHERE ENTRY_TYPE=0 AND ROWNUM <= 10000";
    //private static final String NATIVE_QUERY = "SELECT * FROM PROTEIN_GROUPS WHERE PROTEIN_GROUP_ID='E142E6'";
    
    //private static final String NATIVE_QUERY = "SELECT * FROM PROTEIN_GROUPS WHERE PROTEIN_GROUP_ID='E76XC1'";
    // private static final String NATIVE_QUERY = "SELECT * FROM PROTEIN_GROUPS WHERE ENTRY_TYPE=0 AND ROWNUM <= 5";
//    private static final String NATIVE_QUERY = "select * from PROTEIN_GROUPS where ENTRY_TYPE=0 and rownum<=1\n"
//            + "union\n"
//            + "select * from PROTEIN_GROUPS where ENTRY_TYPE=1 and rownum<=100";

       //private static final String NATIVE_QUERY = "SELECT * FROM PROTEIN_GROUPS WHERE PROTEIN_GROUP_ID='EJLX47'";
    // private static final String NATIVE_QUERY = "SELECT * FROM PROTEIN_GROUPS WHERE PROTEIN_GROUP_ID='EU9G0Q'";
    //private static final String NATIVE_QUERY = "SELECT * FROM PROTEIN_GROUPS WHERE PROTEIN_GROUP_ID='EU6OHA'";
//       private static final String NATIVE_QUERY = "select * from PROTEIN_GROUPS where ENTRY_TYPE=0 and rownum<=1000 \n"
//            + "union\n"
//            + "select * from PROTEIN_GROUPS where ENTRY_TYPE=1 and rownum<=100";
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
                .listener(stepExecutionListener())
                .listener(itemReadListener())
                .listener(itemProcessListener())
                .listener(itemWriteListener())
                .build();

    }

    @Override
    @Bean(destroyMethod = "")
    public ItemReader<ProteinGroups> databaseReader() {

        JpaPagingItemReader<ProteinGroups> databaseReader = new JpaPagingItemReader<>();
        databaseReader.setEntityManagerFactory(entityManagerFactory);
        //databaseReader.setPageSize(xmlConfigParams.getChunkSize());
        databaseReader.setPageSize(50);
       // databaseReader.setQueryString(READ_QUERY);//JPQL 
        databaseReader.setQueryProvider(createQueryProvider());//NATIVE QUERY// 
  
        return databaseReader;
    }

//    @Bean
//    public SqlPagingQueryProviderFactoryBean sqlPagingQueryProviderFactoryBean(){
//        SqlPagingQueryProviderFactoryBean factoryBean = new SqlPagingQueryProviderFactoryBean();
//        factoryBean.setSelectClause("SELECT *");
//        factoryBean.setFromClause("FROM PROTEIN_GROUPS");
//        factoryBean.setWhereClause("WHERE ENTRY_TYPE=0 AND ROWNUM <= 10000");
//        return factoryBean;
//    }
    private JpaNativeQueryProvider<ProteinGroups> createQueryProvider() {
        JpaNativeQueryProvider<ProteinGroups> queryProvider = new JpaNativeQueryProvider<>();
        queryProvider.setSqlQuery(NATIVE_QUERY);
        queryProvider.setEntityClass(ProteinGroups.class);
        try {
            queryProvider.afterPropertiesSet();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return queryProvider;
    }

    @Override
    @Bean(destroyMethod = "")
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
