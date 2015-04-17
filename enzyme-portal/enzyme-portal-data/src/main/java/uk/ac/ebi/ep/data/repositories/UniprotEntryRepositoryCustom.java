/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.search.model.EnzymeSummary;
import uk.ac.ebi.ep.data.search.model.Species;
import uk.ac.ebi.ep.data.search.model.Taxonomy;

/**
 *
 * @author joseph
 */
@NoRepositoryBean
public interface UniprotEntryRepositoryCustom {

    List<UniprotEntry> findEnzymesByNamePrefixes(List<String> nameprefixes);

    @Deprecated
    List<UniprotEntry> findEnzymesByAccessions(List<String> accessions);

    List<UniprotEntry> findEnzymesByAccession(String accession);

    List<UniprotEntry> findEnzymeByNamePrefix(String namePrefix);

    //@EntityGraph(value = "UniprotEntryEntityGraph", type = EntityGraph.EntityGraphType.LOAD)
    //UniprotEntry findEnzymeByAccession(String accession);
    List<String> filterEnzymesInAccessions(List<String> accessions);

    List<Taxonomy> getCountForOrganisms(List<Long> taxids);

    List<String> findAccessionsByTaxId(Long taxId);

    List<UniprotEntry> findEnzymesByTaxId(Long taxId);

    Page<EnzymeSummary> findEnzymesByAccessions(List<String> accessions, Pageable pageable);

    List<Species> findSpeciesByTaxId(Long taxId);

    List<Species> findSpeciesByScientificName(String sName);

    List<UniprotEntry> findEnzymesByMeshId(String meshId);

    List<UniprotEntry> findEnzymesByPathwayId(String pathwayId);

}
