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
import uk.ac.ebi.ep.data.domain.EnzymeXmlStore;



/**
 *
 * @author joseph
 */
public interface EnzymeXmlStoreRepository extends JpaRepository<EnzymeXmlStore, BigDecimal>, QueryDslPredicateExecutor<EnzymeXmlStore> {
   
    
    @Query(value = "  SELECT * FROM ENZYME_XML_STORE where rownum <= 20 order by ENZYME_XML_ID", nativeQuery = true)
    List<EnzymeXmlStore> findEnzymeXmlEnteries();
}
