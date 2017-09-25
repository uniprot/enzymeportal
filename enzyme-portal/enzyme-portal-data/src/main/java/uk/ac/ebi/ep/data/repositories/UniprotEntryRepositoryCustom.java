package uk.ac.ebi.ep.data.repositories;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.entry.AssociatedProtein;
import uk.ac.ebi.ep.data.entry.Protein;
import uk.ac.ebi.ep.data.search.model.EnzymeSummary;
import uk.ac.ebi.ep.data.search.model.Species;

/**
 *
 * @author joseph
 */
@NoRepositoryBean
public interface UniprotEntryRepositoryCustom {

    List<UniprotEntry> findEnzymesByNamePrefixes(List<String> nameprefixes);

    /**
     *
     * @param accessions
     * @return
     * @deprecated (no longer needed )
     */
//    @Deprecated
//    List<UniprotEntry> findEnzymesByAccessions(List<String> accessions);
    /**
     *
     * @param accession
     * @return
     * @deprecated (no longer needed )
     */
//    @Deprecated
//    List<UniprotEntry> findEnzymesByAccession(String accession);
    /**
     *
     * @param namePrefix
     * @return
     * @deprecated (no longer needed )
     */
//    @Deprecated
//    List<UniprotEntry> findEnzymeByNamePrefix(String namePrefix);
    /**
     *
     * @param taxids
     * @return
     * @deprecated (no longer needed )
     */
//    @Deprecated
//    List<Taxonomy> getCountForOrganisms(List<Long> taxids);
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

    List<Protein> findProteinByEc(String ec, int limit);

    List<Species> findSpeciesByEcNumber(String ecNumber);

    List<Species> findSpeciesByEcNumberViaAccessions(String ecNumber, List<String> accessions);

    Page<UniprotEntry> findPageableEntryByEc(String ec, Pageable pageable);

    List<AssociatedProtein> findAssociatedProteinsByEC(String ecNumber, String entryType, int limit);

    List<AssociatedProtein> findAssociatedProteinsByEC(String ecNumber, int limit);
     List<Species> findSpeciesInAccessions( List<String> accessions);

    Page<UniprotEntry> findEnzymesWithAccessions(List<String> accessions, Pageable pageable);

    Page<UniprotEntry> findEnzymesWithAccessions(List<String> accessions, Pageable pageable, UniprotEntryRepository uniprotEntryRepository);

}
