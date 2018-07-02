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
import org.springframework.data.repository.query.Param;
import uk.ac.ebi.ep.data.domain.UniprotXref;

/**
 *
 * @author joseph
 */
public interface UniprotXrefRepository extends JpaRepository<UniprotXref, Long>, QueryDslPredicateExecutor<UniprotXref>, UniprotXrefRepositoryCustom {

    @Query(value = "SELECT * FROM UNIPROT_XREF WHERE SOURCE='PDB' AND ACCESSION=:ACCESSION ", nativeQuery = true)
    List<UniprotXref> findPdbCodesByAccession(@Param("ACCESSION") String accession);

}
