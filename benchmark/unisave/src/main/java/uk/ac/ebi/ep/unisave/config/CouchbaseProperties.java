package uk.ac.ebi.ep.unisave.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Joseph
 */
@Configuration
@ConfigurationProperties(prefix = "couchbase")
@Getter
@Setter
@NoArgsConstructor
public class CouchbaseProperties {

    private String adminUser, adminPassword, host, bucket, password, versionBucket, versionPassword;
}
