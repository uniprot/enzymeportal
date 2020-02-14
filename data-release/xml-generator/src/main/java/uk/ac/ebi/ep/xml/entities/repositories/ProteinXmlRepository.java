package uk.ac.ebi.ep.xml.entities.repositories;

import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.xml.entities.Protein;
import uk.ac.ebi.ep.xml.entities.ProteinXml;

/**
 *
 * @author joseph
 */
@Repository
public interface ProteinXmlRepository extends JpaRepository<ProteinXml, Long> {

    @Transactional(readOnly = true)
    @Query("SELECT p FROM ProteinXml p WHERE p.proteinGroupId = :proteinGroupId")
    Stream<ProteinXml> streamProteinDataByProteinGroupId(@Param("proteinGroupId") String proteinGroupId);

    @Transactional(readOnly = true)
    @Query("SELECT p FROM ProteinXml p WHERE p.ecNumber = :ecNumber")
    Stream<ProteinXml> streamProteinDataByEcNumber(@Param("ecNumber") String ecNumber);

    /**
     * Note : These are not needed in the protein-centric XML; reactantName,
     * chebiSynonyms, familyName, pathwayName
     *
     * @param proteinGroupId
     * @return Stream of Protein
     */
    @Transactional(readOnly = true)
    @Query(value = "SELECT u.accession as accession, u.taxId as taxId,u.proteinName as proteinName,u.scientificName as scientificName, u.commonName as commonName,u.relatedProteinsId as relatedProteinsId, u.entryType as entryType,u.synonymNames as synonymNames,u.expEvidenceFlag as expEvidenceFlag,"
            + "u.geneName as geneName, u.primaryAccession as primaryAccession,"
            + "u.primaryRelatedProteinsId as primaryRelatedProteinsId, u.omimNumber as omimNumber, u.diseaseName as diseaseName, u.compoundId as compoundId, u.compoundName as compoundName, u.compoundRole as compoundRole, u.compoundSource as compoundSource,"
            + "u.reactantId as reactantId,u.reactantSource as reactantSource, u.ecNumber as ecNumber, u.ecFamily as ecFamily,u.catalyticActivity as catalyticActivity, u.pathwayId as pathwayId,u.reactionId as reactionId, u.reactionSource as reactionSource, u.familyGroupId as familyGroupId,"
            + "u.chebiCompoundId as chebiCompoundId, u.chebiCompoundName as chebiCompoundName, u.chebiCompoundRole as chebiCompoundRole"
            + " FROM ProteinXml u WHERE u.proteinGroupId = :proteinGroupId")
    Stream<Protein> streamProteinByProteinGroupId_JPQL(@Param("proteinGroupId") String proteinGroupId);

    @Transactional(readOnly = true)
    @Query(value = "SELECT u.accession as accession, u.taxId as taxId,u.proteinName as proteinName,u.scientificName as scientificName, u.commonName as commonName,u.relatedProteinsId as relatedProteinsId, u.entryType as entryType,u.synonymNames as synonymNames,u.expEvidenceFlag as expEvidenceFlag,"
            + "u.geneName as geneName, u.primaryAccession as primaryAccession,"
            + "u.primaryRelatedProteinsId as primaryRelatedProteinsId, u.omimNumber as omimNumber, u.diseaseName as diseaseName, u.compoundId as compoundId, u.compoundName as compoundName, u.compoundRole as compoundRole, u.compoundSource as compoundSource,"
            + "u.reactantId as reactantId, u.reactantName as reactantName,u.reactantSource as reactantSource, u.ecNumber as ecNumber, u.ecFamily as ecFamily,u.catalyticActivity as catalyticActivity, u.pathwayId as pathwayId,u.pathwayName as pathwayName,u.reactionId as reactionId, u.reactionSource as reactionSource, u.familyGroupId as familyGroupId, u.familyName as familyName,"
            + "u.chebiCompoundId as chebiCompoundId, u.chebiCompoundName as chebiCompoundName, u.chebiCompoundRole as chebiCompoundRole, u.chebiSynonyms as chebiSynonyms"
            + " FROM ProteinXml u WHERE u.ecNumber = :ecNumber")
    Stream<Protein> streamProteinByEcNumber_JPQL(@Param("ecNumber") String ecNumber);

    @Transactional(readOnly = true)
    @Query(nativeQuery = true, value = "select /*+ INDEX(protein_xml IDX_EDF_PGI) */ * from protein_xml where protein_group_id = :protein_group_id")
    Stream<ProteinXml> streamProteinXmlByProteinGroupId(@Param("protein_group_id") String proteinGroupId);

    @Transactional(readOnly = true)
    @Query(value = "SELECT /*+ INDEX(u IDX_EDF_PGI) */ u.accession as accession, u.tax_id as taxId,u.protein_name as proteinName,u.scientific_name as scientificName, u.common_name as commonName,u.related_proteins_id as relatedProteinsId, u.entry_type as entryType,u.synonym_names as synonymNames,u.exp_evidence_flag as expEvidenceFlag,"
            + "u.gene_name as geneName, u.primary_accession as primaryAccession,"
            + "u.primary_related_proteins_id as primaryRelatedProteinsId, u.omim_number as omimNumber, u.disease_name as diseaseName, u.compound_id as compoundId, u.compound_name as compoundName, u.compound_role as compoundRole, u.compound_source as compoundSource,"
            + "u.reactant_id as reactantId,u.reactant_source as reactantSource, u.ec_number as ecNumber, u.ec_family as ecFamily,u.catalytic_activity as catalyticActivity, u.pathway_id as pathwayId,u.reaction_id as reactionId, u.reaction_source as reactionSource, u.family_group_id as familyGroupId,"
            + "u.chebi_compound_id as chebiCompoundId, u.chebi_compound_name as chebiCompoundName, u.chebi_compound_role as chebiCompoundRole"
            + " FROM protein_xml u WHERE u.protein_group_id = :protein_group_id", nativeQuery = true)
    Stream<Protein> streamProteinByProteinGroupId(@Param("protein_group_id") String proteinGroupId);

    @Transactional(readOnly = true)
    @Query(value = "SELECT /*+ INDEX(u IDX_EDF_PGI) */ u.accession as accession, u.tax_id as taxId,u.protein_name as proteinName,u.scientific_name as scientificName, u.common_name as commonName,u.related_proteins_id as relatedProteinsId, u.entry_type as entryType,u.synonym_names as synonymNames,u.exp_evidence_flag as expEvidenceFlag,"
            + "u.gene_name as geneName, u.primary_accession as primaryAccession,"
            + "u.primary_related_proteins_id as primaryRelatedProteinsId, u.omim_number as omimNumber, u.disease_name as diseaseName, u.compound_id as compoundId, u.compound_name as compoundName, u.compound_role as compoundRole, u.compound_source as compoundSource,"
            + "u.reactant_id as reactantId, u.reactant_name as reactantName,u.reactant_source as reactantSource, u.ec_number as ecNumber, u.ec_family as ecFamily,u.catalytic_activity as catalyticActivity, u.pathway_id as pathwayId,u.pathway_name as pathwayName,u.reaction_id as reactionId, u.reaction_source as reactionSource, u.family_group_id as familyGroupId, u.family_name as familyName,"
            + "u.chebi_compound_id as chebiCompoundId, u.chebi_compound_name as chebiCompoundName, u.chebi_compound_role as chebiCompoundRole, u.chebi_synonyms as chebiSynonyms"
            + " FROM protein_xml u WHERE u.ec_number = :ec_number", nativeQuery = true)
    Stream<Protein> streamProteinByEcNumber(@Param("ec_number") String ecNumber);

}
