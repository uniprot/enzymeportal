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
import uk.ac.ebi.ep.data.entry.Protein;
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

    @Deprecated
    List<UniprotEntry> findEnzymesByAccession(String accession);

    @Deprecated
    List<UniprotEntry> findEnzymeByNamePrefix(String namePrefix);

    @Deprecated
    List<Taxonomy> getCountForOrganisms(List<Long> taxids);

    List<String> findAccessionsByTaxId(Long taxId);

    List<UniprotEntry> findEnzymesByTaxId(Long taxId);

    Page<EnzymeSummary> findEnzymesByAccessions(List<String> accessions, Pageable pageable);

    List<Species> findSpeciesByTaxId(Long taxId);

    List<Species> findSpeciesByScientificName(String sName);

    List<UniprotEntry> findEnzymesByMeshId(String meshId);

    List<UniprotEntry> findEnzymesByPathwayId(String pathwayId);

    List<String> findEnzymesByCompound(String compoundId);

    List<UniprotEntry> findSummariesByAcc(List<String> accession);

    List<Protein> findProteinByEc(String ec);

    List<Species> findSpeciesByEcNumber(String ecNumber);

    List<Species> findSpeciesByEcNumberViaAccessions(String ecNumber, List<String> accessions);

    Page<UniprotEntry> findPageableEntryByEc(String ec, Pageable pageable);
}
