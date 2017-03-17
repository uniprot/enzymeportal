/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.ebi.ep.data.domain.ChebiCompound;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public interface ChebiCompoundRepository extends JpaRepository<ChebiCompound, Long> {

    ChebiCompound findByChebiAccession(String chebiId);

    ChebiCompound findByCompoundName(String compoundName);
}
