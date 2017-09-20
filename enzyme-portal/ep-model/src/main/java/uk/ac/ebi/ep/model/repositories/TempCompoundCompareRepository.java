/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.model.TempCompoundCompare;

/**
 *
 * @author joseph
 */
public interface TempCompoundCompareRepository extends JpaRepository<TempCompoundCompare, Long> {

    @Modifying
    @Transactional
    @Query(value = "Insert INTO TEMP_COMPOUND_COMPARE "
            + "(COMPOUND_ID,COMPOUND_NAME,COMPOUND_SOURCE,RELATIONSHIP,UNIPROT_ACCESSION,URL,COMPOUND_ROLE,NOTE) VALUES (?1,?2,?3,?4,?5,?6,?7,?8)", nativeQuery = true)
    void addTempCompounds(String compoundId, String compoundName, String compoundSource, String relationship, String accession, String url, String compoundRole, String note);

}
