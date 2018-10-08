
package uk.ac.ebi.ep.unisave.repositories;

import org.springframework.data.couchbase.core.query.N1qlPrimaryIndexed;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.CouchbasePagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.ep.unisave.documents.UnisaveVersion;

/**
 *
 * @author Joseph
 */
@N1qlPrimaryIndexed
@ViewIndexed(designDoc = "versions")
@Repository
public interface UnisaveVersionRepository extends CouchbasePagingAndSortingRepository<UnisaveVersion, String> {
    
}

