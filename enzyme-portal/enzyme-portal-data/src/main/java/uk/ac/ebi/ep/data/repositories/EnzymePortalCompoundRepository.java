/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.data.domain.EnzymePortalCompound;

/**
 *
 * @author joseph
 */
public interface EnzymePortalCompoundRepository extends JpaRepository<EnzymePortalCompound, Long>, QueryDslPredicateExecutor<EnzymePortalCompound>, EnzymePortalCompoundRepositoryCustom {

    List<EnzymePortalCompound> findByCompoundName(String name);
//Insert /*+ IGNORE_ROW_ON_DUPKEY_INDEX(ENZYME_PORTAL_COMPOUND,IX_ENZYME_COMPOUND_DUPS) */ INTO ENZYME_PORTAL_COMPOUND (COMPOUND_INTERNAL_ID,COMPOUND_ID,COMPOUND_NAME,COMPOUND_SOURCE,RELATIONSHIP,UNIPROT_ACCESSION,URL,COMPOUND_ROLE) 
    //values (777777,'CHEMBL91','Sildenafil','ChEMBL','is_inhibitor_of','P08183',null,'INHIBITOR');

    @Modifying
    @Transactional
    @Query(value = "Insert /*+ IGNORE_ROW_ON_DUPKEY_INDEX(ENZYME_PORTAL_COMPOUND,IX_ENZYME_COMPOUND_DUPS) */ INTO ENZYME_PORTAL_COMPOUND "
            + "(COMPOUND_ID,COMPOUND_NAME,COMPOUND_SOURCE,RELATIONSHIP,UNIPROT_ACCESSION,URL,COMPOUND_ROLE,NOTE) VALUES (?1,?2,?3,?4,?5,?6,?7,?8)", nativeQuery = true)
    void createCompoundIgnoreDup(String compoundId, String compoundName, String compoundSource, String relationship,String accession, String url,String compoundRole, String note);

  //EnzymePortalCompound e =  new EnzymePortalCompound(String compoundId, String compoundName, String compoundSource, String relationship, String compoundRole, String url, String accession, String note);
    //Insert /*+ IGNORE_ROW_ON_DUPKEY_INDEX(ENZYME_PORTAL_COMPOUND,IX_ENZYME_COMPOUND_DUPS) */ INTO ENZYME_PORTAL_COMPOUND (COMPOUND_ID,COMPOUND_NAME,COMPOUND_SOURCE,RELATIONSHIP,UNIPROT_ACCESSION,URL,COMPOUND_ROLE) 
    //values ('CHEMBL91','Sildenafil','ChEMBL','is_inhibitor_of','P08183',null,'INHIBITOR');
}
