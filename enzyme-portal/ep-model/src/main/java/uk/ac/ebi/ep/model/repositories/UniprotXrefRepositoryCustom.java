package uk.ac.ebi.ep.model.repositories;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;
import uk.ac.ebi.ep.model.UniprotXref;

/**
 *
 * @author joseph
 */
@NoRepositoryBean
public interface UniprotXrefRepositoryCustom {

    List<UniprotXref> findPDBcodesByAccession(String accession);

    List<UniprotXref> findPDBcodes();

    List<String> findPdbCodesWithNoNames();

    UniprotXref findPdbById(String pdbId);

}
