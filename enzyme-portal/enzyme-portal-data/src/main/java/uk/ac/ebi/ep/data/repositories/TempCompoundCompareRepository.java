/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.ebi.ep.data.domain.TempCompoundCompare;

/**
 *
 * @author joseph
 */
public interface TempCompoundCompareRepository extends JpaRepository<TempCompoundCompare, Long> {
    
}
