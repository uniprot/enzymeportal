package uk.ac.ebi.ep.xml.config;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.orm.JpaNativeQueryProvider;
import org.springframework.batch.item.xml.StaxWriterCallback;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import uk.ac.ebi.ep.xml.listeners.GlobalListener;
import uk.ac.ebi.ep.xml.listeners.XmlProcessorListener;
import uk.ac.ebi.ep.xml.listeners.XmlStepExecutionListener;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 * @param <T> input data
 * @param <S> output data
 */
@Slf4j
public abstract class AbstractBatchConfig<T, S> {

    abstract JobExecutionListener jobExecutionListener();

    abstract Resource xmlOutputDir();

    abstract ItemReader<T> databaseReader();

    abstract ItemProcessor<T, S> entryProcessor();

    abstract ItemWriter<S> xmlWriter();

    abstract StaxWriterCallback xmlHeaderCallback(String countQuery);

    abstract String countEntries(String countQuery);

    abstract ChunkListener logChunkListener();

    protected StepExecutionListener stepExecutionListener() {
        return new XmlStepExecutionListener();
    }

    abstract ItemReadListener itemReadListener();

    protected ItemProcessListener itemProcessListener() {
        return new XmlProcessorListener<T,S>();
    }

    protected ItemWriteListener itemWriteListener() {
        return new GlobalListener<>();
    }

    protected JpaNativeQueryProvider<T> createQueryProvider(String sqlQuery, Class<T> clazz) {
        JpaNativeQueryProvider<T> queryProvider = new JpaNativeQueryProvider<>();
        queryProvider.setSqlQuery(sqlQuery);
        queryProvider.setEntityClass(clazz);
        try {
            queryProvider.afterPropertiesSet();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return queryProvider;
    }

    /**
     *
     * @param clazz class to be bound
     * @return marshaller
     */
    protected Jaxb2Marshaller xmlMarshaller(Class<S> clazz) {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(clazz);

        Map<String, Object> jaxbProps = new HashMap<>();
        jaxbProps.put(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        marshaller.setMarshallerProperties(jaxbProps);

        return marshaller;
    }

}
