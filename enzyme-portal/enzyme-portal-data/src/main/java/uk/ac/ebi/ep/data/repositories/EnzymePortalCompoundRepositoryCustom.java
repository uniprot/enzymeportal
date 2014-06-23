/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.data.repositories;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;
import uk.ac.ebi.ep.data.domain.EnzymePortalCompound;

/**
 *
 * @author joseph
 */
@NoRepositoryBean
public interface EnzymePortalCompoundRepositoryCustom {
    List<EnzymePortalCompound> findCompoundsByUniprotName(String uniprotName);
   
    
}
