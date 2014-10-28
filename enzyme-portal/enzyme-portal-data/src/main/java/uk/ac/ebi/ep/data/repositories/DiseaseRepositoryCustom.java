/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;
import uk.ac.ebi.ep.data.domain.EnzymePortalDisease;

/**
 *
 * @author joseph
 */
@NoRepositoryBean
public interface DiseaseRepositoryCustom {

  

    List<EnzymePortalDisease> findDiseasesByAccessions(List<String> accessions);

    List<EnzymePortalDisease> findDiseasesByAccession(String accession);

    List<EnzymePortalDisease> findDiseasesByNamePrefixes(List<String> name_prefixes);
     List<String> findAccessionsByDisease(String diseaseId);
     List<EnzymePortalDisease> findDiseases();
}
