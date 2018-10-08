
package uk.ac.ebi.ep.unisave.config;

import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractReactiveCouchbaseConfiguration;
import org.springframework.data.couchbase.core.RxJavaCouchbaseTemplate;
import org.springframework.data.couchbase.core.WriteResultChecking;
import org.springframework.data.couchbase.core.query.Consistency;
import org.springframework.data.couchbase.repository.config.EnableReactiveCouchbaseRepositories;
import org.springframework.data.couchbase.repository.support.IndexManager;

/**
 *
 * @author Joseph
 */
@Configuration
@Slf4j
@EnableReactiveCouchbaseRepositories(basePackages = {"uk.ac.ebi.ep.unisave.repositories"})
public class ReactiveCouchbaseConfig extends AbstractReactiveCouchbaseConfiguration {
    

    @Autowired
    private CouchbaseProperties couchbaseProperties;
//  
    @Bean
    public String couchbaseAdminUser() {
         return couchbaseProperties.getAdminUser();
    }

    @Bean
    public String couchbaseAdminPassword() {
        return couchbaseProperties.getAdminPassword();
    }

    @Override
    protected List<String> getBootstrapHosts() {
        return Collections.singletonList(couchbaseProperties.getHost());
    }

    @Override
    protected String getBucketName() {
        return couchbaseProperties.getBucket();
    }

    @Override
    protected String getBucketPassword() {
        return couchbaseProperties.getPassword();
    }
    //Additionally, the SDK environment can be tuned by overriding the getEnvironment() method to return a properly tuned CouchbaseEnvironment.
    @Override
    protected CouchbaseEnvironment getEnvironment() {
        return DefaultCouchbaseEnvironment.builder()
                .connectTimeout(10000)
                .kvTimeout(10000)
                .queryTimeout(10000)
                .viewTimeout(10000)
                .build();
    }

        @Override
    public RxJavaCouchbaseTemplate  reactiveCouchbaseTemplate() throws Exception {
        RxJavaCouchbaseTemplate template = super.reactiveCouchbaseTemplate();
        template.setWriteResultChecking(WriteResultChecking.LOG);
        return template;
    }

    // to auto-create indexes -- see comment in post
    @Override
    public IndexManager indexManager() {
        return new IndexManager(true, true, true);
    }

//    For strong consistency (at the expense of performance)
    @Override
    protected Consistency getDefaultConsistency() {
        return Consistency.READ_YOUR_OWN_WRITES;
    }

    

}

