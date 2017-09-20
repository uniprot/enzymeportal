
package uk.ac.ebi.ep.model.repositories;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;
import uk.ac.ebi.ep.model.search.model.Compound;
import uk.ac.ebi.ep.model.EnzymePortalCompound;

/**
 *
 * @author joseph
 */
@NoRepositoryBean
public interface EnzymePortalCompoundRepositoryCustom {

    List<EnzymePortalCompound> findCompoundsByUniprotName(String uniprotName);

    List<EnzymePortalCompound> findCompoundsByUniprotAccession(String accession);

    List<EnzymePortalCompound> findCompoundsByNameprefixes(List<String> nameprefixes);
    
//    List<String> findEnzymesByCompound(String compoundId);
    
    List<Compound> findCompoundsByTaxId(Long taxId);
    List<Compound> findCompoundsByEcNumber(String ecNumber);
    List<Compound> findCompoundsInAccessions(List<String> accessions);

}
