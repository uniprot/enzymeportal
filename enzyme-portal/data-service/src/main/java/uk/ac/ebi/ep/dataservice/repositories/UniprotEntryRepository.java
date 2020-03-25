package uk.ac.ebi.ep.dataservice.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.ep.dataservice.dto.ProteinView;
import uk.ac.ebi.ep.dataservice.entities.UniprotEntry;

/**
 *
 * @author joseph
 */
@Repository
public interface UniprotEntryRepository extends JpaRepository<UniprotEntry, Long>, QuerydslPredicateExecutor<UniprotEntry>, UniprotEntryRepositoryCustom {

    default UniprotEntry findByUniprotAccession(String acc) {
        return findByAccession(acc);
    }

    UniprotEntry findByAccession(String accession);

    @Query(value = "SELECT * FROM UNIPROT_ENTRY WHERE ACCESSION = :ACCESSION ", nativeQuery = true)
    UniprotEntry findEnzymeByAccession(@Param("ACCESSION") String accession);

    @Query(value = "SELECT CATALYTIC_ACTIVITY FROM ENZYME_CATALYTIC_ACTIVITY WHERE UNIPROT_ACCESSION = :UNIPROT_ACCESSION", nativeQuery = true)
    List<String> findCatalyticActivitiesByAccession(@Param("UNIPROT_ACCESSION") String accession);

    @Query(value = "select accession as accession, tax_id as taxId,protein_name as proteinName,scientific_name as scientificName, common_name as commonName,sequence_length as sequenceLength,related_proteins_id as relatedProteinsId, function as function, entry_type as entryType,synonym_names as synonymNames,exp_evidence_flag as expEvidenceFlag,protein_group_id as proteinGroupId FROM uniprot_entry WHERE accession = :accession", nativeQuery = true)
    ProteinView findProteinViewByAccession(@Param("accession") String accession);

    @Query(value = "select accession as accession,tax_id as taxId,scientific_name as scientificName, common_name as commonName, entry_type as entryType,exp_evidence_flag as expEvidenceFlag FROM uniprot_entry WHERE related_proteins_id = :RELATED_PROTEINS_ID ", nativeQuery = true)
    List<ProteinView> findProteinViewByRelatedProteinsId(@Param("RELATED_PROTEINS_ID") Long relatedProteinId);

    @Query(value = "select accession as accession,tax_id as taxId,scientific_name as scientificName, common_name as commonName, entry_type as entryType,exp_evidence_flag as expEvidenceFlag,protein_group_id as proteinGroupId FROM uniprot_entry WHERE related_proteins_id = :RELATED_PROTEINS_ID and protein_group_id = :PROTEIN_GROUP_ID", nativeQuery = true)
    List<ProteinView> findProteinViewByRelatedProteinsIdAndProteinGroupId(@Param("RELATED_PROTEINS_ID") Long relatedProteinId, @Param("PROTEIN_GROUP_ID") String proteinGroupId);

    @Query(value = "SELECT accession as accession, tax_id as taxId,protein_name as proteinName,scientific_name as scientificName, common_name as commonName,sequence_length as sequenceLength,related_proteins_id as relatedProteinsId, function as function, entry_type as entryType,synonym_names as synonymNames,exp_evidence_flag as expEvidenceFlag, d.omim_number as diseaseId, d.disease_name as diseaseName FROM uniprot_entry, enzyme_portal_disease d WHERE d.uniprot_accession = uniprot_entry.accession and uniprot_entry.accession = :accession", nativeQuery = true)
    List<ProteinView> findProteinDiseaseViewByAccession(@Param("accession") String accession);

}
