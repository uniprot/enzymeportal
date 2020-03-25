package uk.ac.ebi.ep.dataservice.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import uk.ac.ebi.ep.dataservice.dto.ProteinData;

/**
 *
 * @author joseph
 */
@NoRepositoryBean
public interface UniprotEntryRepositoryCustom {

    ProteinData findProteinByAccession(String accession);
}
