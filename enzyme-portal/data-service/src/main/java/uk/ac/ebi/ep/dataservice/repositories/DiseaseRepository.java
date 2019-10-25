package uk.ac.ebi.ep.dataservice.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.ep.dataservice.dto.DiseaseView;
import uk.ac.ebi.ep.dataservice.entities.EnzymePortalDisease;

/**
 *
 * @author joseph
 */
@Repository
public interface DiseaseRepository extends JpaRepository<EnzymePortalDisease, Long>, QuerydslPredicateExecutor<EnzymePortalDisease>, DiseaseRepositoryCustom {

    @Query(value = "select d.omimNumber as omimNumber, d.diseaseName as diseaseName from EnzymePortalDisease d group by d.omimNumber, d.diseaseName")
    List<DiseaseView> findDiseasesView();

    @Query(value = "SELECT OMIM_NUMBER as omimNumber, DISEASE_NAME as diseaseName, DEFINITION as description, EVIDENCE as evidence, URL as url FROM ENZYME_PORTAL_DISEASE WHERE UNIPROT_ACCESSION = :UNIPROT_ACCESSION", nativeQuery = true)
    List<DiseaseView> findDiseasesViewByAccession(@Param("UNIPROT_ACCESSION") String accession);

}
