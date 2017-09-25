/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;
import uk.ac.ebi.ep.data.domain.EnzymePortalCompound;
import uk.ac.ebi.ep.data.search.model.Compound;

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
