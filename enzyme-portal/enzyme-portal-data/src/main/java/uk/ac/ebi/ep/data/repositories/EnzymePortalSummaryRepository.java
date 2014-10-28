/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import uk.ac.ebi.ep.data.domain.EnzymePortalSummary;

/**
 *
 * @author joseph
 */
public interface EnzymePortalSummaryRepository extends JpaRepository<EnzymePortalSummary, Long>, QueryDslPredicateExecutor<EnzymePortalSummary>, EnzymePortalSummaryRepositoryCustom {

    List<EnzymePortalSummary> findByCommentText(String commentText);

    @Query(value = "SELECT * FROM EnzymePortalSummary WHERE  ROWNUM <= 30000", nativeQuery = true)
    List<EnzymePortalSummary> findEnzymePortalSummaries();

}
