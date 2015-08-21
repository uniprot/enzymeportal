/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.data.domain.EnzymePortalDisease;

/**
 *
 * @author joseph
 */
public interface DiseaseRepository extends JpaRepository<EnzymePortalDisease, Long>, QueryDslPredicateExecutor<EnzymePortalDisease>, DiseaseRepositoryCustom {

    EnzymePortalDisease findByDiseaseId(BigDecimal id);

    @EntityGraph(value = "DiseaseEntityGraph", type = EntityGraphType.LOAD)
    List<EnzymePortalDisease> findByDiseaseNameLikeIgnoreCase(String diseaseName);

    @Modifying
    @Transactional
    @Query(value = "INSERT /*+ IGNORE_ROW_ON_DUPKEY_INDEX(ENZYME_PORTAL_DISEASE,IX_ENZYME_DISEASE_DUPS) */ INTO ENZYME_PORTAL_DISEASE (UNIPROT_ACCESSION,OMIM_NUMBER,MESH_ID,EFO_ID,DISEASE_NAME,EVIDENCE,DEFINITION,SCORE,URL) VALUES (?1,?2,?3,?4,?5,?6,?7,?8,?9)", nativeQuery = true)
    void createDiseaseIgnoreDup(String accession, String omimNumber, String meshId, String efoId, String diseaseName, String evidence, String definition, String score, String url);

        @Transactional(readOnly = true)
    @Query(value = "SELECT /*+ PARALLEL(auto) */  DISTINCT(UNIPROT_ACCESSION) FROM ENZYME_PORTAL_DISEASE WHERE OMIM_NUMBER= :OMIM_NUMBER", nativeQuery = true)
    List<String> findAccessionsByOmimNumber(@Param("OMIM_NUMBER") String omimNumber);

}
