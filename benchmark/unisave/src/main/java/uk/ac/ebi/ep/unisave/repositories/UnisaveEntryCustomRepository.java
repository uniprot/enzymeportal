package uk.ac.ebi.ep.unisave.repositories;

import com.couchbase.client.java.document.JsonDocument;
import java.util.List;
import java.util.Set;
import org.springframework.data.repository.NoRepositoryBean;
import uk.ac.ebi.ep.unisave.documents.UnisaveEntry;

/**
 *
 * @author Joseph
 */
@NoRepositoryBean
public interface UnisaveEntryCustomRepository {

    List<UnisaveEntry> findEntrieByAccession(String accession);

    void loadUnisaveEntries(List<UnisaveEntry> entries);

    void loadUnisaveEntries(Set<JsonDocument> jsonDocuments);
}
