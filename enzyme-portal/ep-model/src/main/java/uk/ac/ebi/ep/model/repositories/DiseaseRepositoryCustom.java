
package uk.ac.ebi.ep.model.repositories;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;
import uk.ac.ebi.ep.model.search.model.Disease;
import uk.ac.ebi.ep.model.EnzymePortalDisease;

/**
 * @author joseph
 */
@NoRepositoryBean
public interface DiseaseRepositoryCustom {


    List<EnzymePortalDisease> findDiseasesByAccessions(List<String> accessions);

    List<EnzymePortalDisease> findDiseasesByNamePrefixes(List<String> namePrefixes);

    List<String> findAccessionsByOmimId(String meshId);

    List<EnzymePortalDisease> findDiseases();

    List<Disease> findDiseasesNameLike(String name);
    
    List<Disease> findDiseasesByTaxId(Long taxId);
    
     List<Disease> findDiseasesByAccession(String accession);
     
     List<Disease> findDiseasesByEcNumber(String ecNumber);
     
     List<Disease> findDiseasesInAccessions(List<String> accessions);

}
