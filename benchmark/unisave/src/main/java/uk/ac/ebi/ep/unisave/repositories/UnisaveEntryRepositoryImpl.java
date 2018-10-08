package uk.ac.ebi.ep.unisave.repositories;

import com.couchbase.client.core.BackpressureException;
import com.couchbase.client.core.time.Delay;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlParams;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.Statement;
import com.couchbase.client.java.query.consistency.ScanConsistency;
import com.couchbase.client.java.query.dsl.Expression;
import static com.couchbase.client.java.query.dsl.Expression.x;
import com.couchbase.client.java.util.retry.RetryBuilder;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.couchbase.core.CouchbaseOperations;
import org.springframework.data.couchbase.repository.config.RepositoryOperationsMapping;
import org.springframework.data.couchbase.repository.query.CouchbaseEntityInformation;
import org.springframework.data.couchbase.repository.query.support.N1qlUtils;
import rx.Observable;
import uk.ac.ebi.ep.unisave.documents.UnisaveEntry;
import uk.ac.ebi.ep.unisave.documents.UnisaveVersion;

/**
 *
 * @author Joseph
 */
public class UnisaveEntryRepositoryImpl extends Helpers implements UnisaveEntryCustomRepository {
//to read https://blog.couchbase.com/json-data-modeling-rdbms-users/

    private final RepositoryOperationsMapping repositoryOperationsMapping;

    @Autowired
    public UnisaveEntryRepositoryImpl(RepositoryOperationsMapping repositoryOperationsMapping) {
        this.repositoryOperationsMapping = repositoryOperationsMapping;
    }

    private CouchbaseOperations getCouchbaseOperations() {

        return repositoryOperationsMapping.resolve(UnisaveEntryCustomRepository.class, UnisaveEntry.class);
    }

    private Bucket getBucket() {
        return getCouchbaseOperations().getCouchbaseBucket();

    }

    @Override
    public List<UnisaveEntry> findEntrieByAccession(String accession) {

        JsonObject json = JsonObject.create()
                .put("accession", accession);
        Expression whereExpression = x(json);

        CouchbaseEntityInformation<UnisaveEntry, String> itemEntityInformation = getCouchbaseEntityInformation(getCouchbaseOperations(), UnisaveEntry.class);

        Statement statement = N1qlUtils.createSelectFromForEntity(getCouchbaseOperations().getCouchbaseBucket().name())
                .where(
                        N1qlUtils.createWhereFilterForEntity(whereExpression, getCouchbaseOperations().getConverter(), itemEntityInformation));
        ScanConsistency consistency = getCouchbaseOperations().getDefaultConsistency().n1qlConsistency();
        N1qlParams queryParams = N1qlParams.build().consistency(consistency);

        N1qlQuery query = N1qlQuery.simple(statement, queryParams);

        return getCouchbaseOperations().findByN1QL(query, UnisaveEntry.class);
    }
    private JsonObject versionToJsonObject(UnisaveVersion version){
               return JsonObject
                    .empty()
                    .put("fullContent", version.getFullContent())
                    .put("md5", version.getMd5())
                    .put("sequenceMd5", version.getSequenceMd5())
                    .put("name", version.getName())
                    .put("sequenceVersion", version.getSequenceVersion())
                    .put("release", version.getRelease())
                    .put("releaseDate", version.getReleaseDate())
                    .put("database", version.getDatabase())
                    .put("type", version.getType())
                    .put("versionNumber", version.getVersionNumber());  
    }
    
    private JsonObject convertEntryToJsonObject(UnisaveEntry entry, List<JsonObject> versions ){
              return JsonObject
                .empty()
                .put("id", entry.getAccesion())
                .put("accesion", entry.getAccesion())
                .put("latest", entry.getLatest())
                .put("type", entry.getType())
                .put("unisaveVersions", versions);
    }

    public JsonDocument uniSaveToDocument(UnisaveEntry entry) {

       
    List<JsonObject> versions = entry
            .getUnisaveVersions()
            .stream()
            .map(version ->versionToJsonObject(version))
            .collect(Collectors.toList());
        
        JsonObject content = convertEntryToJsonObject(entry, versions);

        return JsonDocument.create(entry.getAccesion(), content);

    }

    @Override
    public void loadUnisaveEntries(List<UnisaveEntry> entries) {

        List<JsonDocument> docs = entries
                .stream()
                .map(entry -> uniSaveToDocument(entry))
                //.map(entry -> conversionService.convert(entry, JsonDocument.class))
                .collect(Collectors.toList());

        Observable
                .from(docs)
                .flatMap((final JsonDocument docToInsert) -> getBucket().async()
                .insert(docToInsert))
                .retryWhen(RetryBuilder
                        .anyOf(BackpressureException.class)
                        .delay(Delay.exponential(TimeUnit.MILLISECONDS, 500))
                        .max(10)
                        .build())
                .last()
                .toBlocking()
                .single();

    }

    @Override
    public void loadUnisaveEntries(Set<JsonDocument> jsonDocuments) {

        Observable
                .from(jsonDocuments)
                .flatMap((final JsonDocument docToInsert) -> getBucket().async()
                .insert(docToInsert))
                .retryWhen(RetryBuilder
                        .anyOf(BackpressureException.class)
                        .delay(Delay.exponential(TimeUnit.MILLISECONDS, 100))
                        .max(10)
                        .build())
                .last()
                .toBlocking()
                .single();
    }


}
