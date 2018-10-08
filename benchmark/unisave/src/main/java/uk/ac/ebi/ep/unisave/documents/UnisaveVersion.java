package uk.ac.ebi.ep.unisave.documents;

import com.couchbase.client.java.repository.annotation.Field;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author Joseph
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UnisaveVersion {

  
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
