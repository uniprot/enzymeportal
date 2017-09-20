/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.model.repositories;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;
import uk.ac.ebi.ep.model.search.model.EcNumber;

/**
 *
 * @author joseph
 */
@NoRepositoryBean
public interface EnzymePortalEcNumbersRepositoryCustom {
    
    List<String> findAccessionsByEc(String ecNumber);
    List<String> findAccessionsByEc(String ecNumber, int limit);
    List<EcNumber> findEnzymeFamiliesByTaxId(Long taxId);
   List<EcNumber> findEnzymeFamiliesByEcNumber(String ecNumber);
   List<EcNumber> findEcNumberInAccessions(List<String> accessions);

}
