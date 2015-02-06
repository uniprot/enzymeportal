/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import uk.ac.ebi.ep.data.domain.EnzymePortalNames;

/**
 *
 * @author joseph
 */
public interface EnzymePortalNamesRepository extends JpaRepository<EnzymePortalNames, BigDecimal>, QueryDslPredicateExecutor<EnzymePortalNames> {

    @Query(value = "select distinct * from ENZYME_PORTAL_NAMES where rownum <= 20 ORDER BY uniprot_accession, DECODE (category_type, 'RecName', 1 ,'AltName', 2,'SubName', 3)", nativeQuery = true)
    List<EnzymePortalNames> findEnzymeNames();
}
