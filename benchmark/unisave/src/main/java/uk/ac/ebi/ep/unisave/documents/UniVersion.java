package uk.ac.ebi.ep.unisave.documents;

import com.couchbase.client.java.repository.annotation.Field;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;
import org.springframework.data.couchbase.core.mapping.id.IdAttribute;

/**
 *
 * @author Joseph
 */
@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UniVersion {

    @Version
    private long version;
    @Id
    @GeneratedValue(strategy = GenerationStrategy.USE_ATTRIBUTES)
    private String id;
    @Field
    @IdAttribute
    String accession;//entry id
    @Field
    String fullContent;
    @Field
    String md5;
    @Field
    String sequenceMd5;
    @Field
    String name;
    @Field
    Integer sequenceVersion;
    @Field
    String release;
    @Field
    String releaseDate;
    @Field
    String database;
    @Field
    String type; // version
    @Field
    Integer versionNumber;
}
