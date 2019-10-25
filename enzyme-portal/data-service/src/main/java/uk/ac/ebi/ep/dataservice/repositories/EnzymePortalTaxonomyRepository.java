/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import uk.ac.ebi.ep.data.domain.EnzymesToTaxonomy;


/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public interface EnzymesToTaxonomyRepository extends JpaRepository<EnzymesToTaxonomy, Long>, QueryDslPredicateExecutor<EnzymesToTaxonomy>, EnzymesToTaxonomyRepositoryCustom {
    
}
