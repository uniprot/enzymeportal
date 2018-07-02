/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;
import uk.ac.ebi.ep.data.domain.UniprotXref;

/**
 *
 * @author joseph
 */
@NoRepositoryBean
public interface UniprotXrefRepositoryCustom {

    //List<UniprotXref> findPDBcodesByAccession(String accession);
    
    List<UniprotXref> findPDBcodes();
    List<String> findPdbCodesWithNoNames();
    UniprotXref findPdbById(String pdbId);
        
    
    
}
