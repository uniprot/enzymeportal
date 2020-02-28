package uk.ac.ebi.ep.model.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uk.ac.ebi.ep.model.WebStatXref;
import uk.ac.ebi.ep.model.dao.WebStatXrefView;

/**
 *
 * @author joseph
 */
public interface WebStatXrefRepository extends JpaRepository<WebStatXref, Long> {

    @Query(value = "SELECT UNIPROT as uniprot, INTENZ as intenz,reactome as reactome,rhea as rhea,kegg as kegg,pdbe as pdbe, chembl as chembl, chebi as chebi,omim as omim,metabolights as metabolights,mcsa as mcsa,europepmc as europepmc,release_Id as releaseId,RELEASE_DATE as releaseDate FROM WEB_STAT_XREF order by release_date asc", nativeQuery = true)
    List<WebStatXrefView> findWebStatXrefView();

    @Query(value = "SELECT UNIPROT as uniprot, INTENZ as intenz,reactome as reactome,rhea as rhea,kegg as kegg,pdbe as pdbe, chembl as chembl, chebi as chebi,omim as omim,metabolights as metabolights,mcsa as mcsa,europepmc as europepmc,release_Id as releaseId,RELEASE_DATE as releaseDate FROM WEB_STAT_XREF WHERE release_Id=:releaseId", nativeQuery = true)
    WebStatXrefView findWebStatXrefViewByReleaseId(@Param("releaseId") String releaseId);
    
   @Query(value="select release_id as releaseId from web_stat_xref order by release_date asc",nativeQuery = true)
    List<WebStatXrefView> findWebStatXrefReleaseMonths();

}
