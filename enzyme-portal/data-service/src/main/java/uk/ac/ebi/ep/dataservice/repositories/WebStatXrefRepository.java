package uk.ac.ebi.ep.dataservice.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import uk.ac.ebi.ep.dataservice.dto.WebStatXrefDto;
import uk.ac.ebi.ep.dataservice.dto.WebStatXrefView;
import uk.ac.ebi.ep.dataservice.entities.WebStatXref;

/**
 *
 * @author joseph
 */
public interface WebStatXrefRepository extends StatisticsRepositoryQueries<WebStatXrefDto>, JpaRepository<WebStatXref, Long>, JpaSpecificationExecutor, QuerydslPredicateExecutor<WebStatXref> {

    @Query(value = "SELECT UNIPROT as uniprot, INTENZ as intenz,reactome as reactome,rhea as rhea,kegg as kegg,pdbe as pdbe, chembl as chembl, chebi as chebi,omim as omim,metabolights as metabolights,mcsa as mcsa,europepmc as europepmc,release_Id as releaseId,RELEASE_DATE as releaseDate FROM WEB_STAT_XREF order by release_date asc", nativeQuery = true)
    List<WebStatXrefView> findWebStatXrefView();

    @Query(value = "SELECT UNIPROT as uniprot, INTENZ as intenz,reactome as reactome,rhea as rhea,kegg as kegg,pdbe as pdbe, chembl as chembl, chebi as chebi,omim as omim,metabolights as metabolights,mcsa as mcsa,europepmc as europepmc,release_Id as releaseId,RELEASE_DATE as releaseDate FROM WEB_STAT_XREF WHERE release_Id=:releaseId", nativeQuery = true)
    WebStatXrefView findWebStatXrefViewByReleaseId(@Param("releaseId") String releaseId);

    @Query(value = "select release_id as releaseId from web_stat_xref order by release_date asc", nativeQuery = true)
    List<WebStatXrefView> findWebStatXrefReleaseMonths();

    @Query(value = "select release_id from web_stat_xref order by release_date asc", nativeQuery = true)
    List<String> findReleaseIds();

}
