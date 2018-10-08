package uk.ac.ebi.ep.unisave.repositories;

import org.springframework.data.couchbase.core.query.N1qlPrimaryIndexed;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.ep.unisave.documents.UnisaveEntry;

/**
 *
 * @author Joseph
 */
@N1qlPrimaryIndexed
//@N1qlSecondaryIndexed(indexName = "entries")
@ViewIndexed(designDoc = "entries")
@Repository
public interface UnisaveEntryRepository extends  ReactiveSortingRepository<UnisaveEntry, String>, UnisaveEntryCustomRepository{//CouchbasePagingAndSortingRepository<UnisaveEntry, String> {

    UnisaveEntry findByAccesion(String accesion);
     
}
