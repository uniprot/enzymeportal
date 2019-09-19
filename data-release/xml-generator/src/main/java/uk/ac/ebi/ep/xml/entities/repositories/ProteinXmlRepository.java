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

    Stream<Protein> streamProteinByProteinGroupId(@Param("proteinGroupId") String proteinGroupId);

    @Transactional(readOnly = true)
    @Query(value = "SELECT u.accession as accession, u.taxId as taxId,u.proteinName as proteinName,u.scientificName as scientificName, u.commonName as commonName,u.relatedProteinsId as relatedProteinsId, u.entryType as entryType,u.synonymNames as synonymNames,u.expEvidenceFlag as expEvidenceFlag,"
            + "u.geneName as geneName, u.primaryAccession as primaryAccession,"
            + "u.primaryRelatedProteinsId as primaryRelatedProteinsId, u.omimNumber as omimNumber, u.diseaseName as diseaseName, u.compoundId as compoundId, u.compoundName as compoundName, u.compoundRole as compoundRole, u.compoundSource as compoundSource,"
            + "u.reactantId as reactantId, u.reactantName as reactantName,u.reactantSource as reactantSource, u.ecNumber as ecNumber, u.ecFamily as ecFamily,u.catalyticActivity as catalyticActivity, u.pathwayId as pathwayId,u.pathwayName as pathwayName,u.reactionId as reactionId, u.reactionSource as reactionSource, u.familyGroupId as familyGroupId, u.familyName as familyName,"
            + "u.chebiCompoundId as chebiCompoundId, u.chebiCompoundName as chebiCompoundName, u.chebiCompoundRole as chebiCompoundRole, u.chebiSynonyms as chebiSynonyms"
            + " FROM ProteinXml u WHERE u.ecNumber = :ecNumber")
    Stream<Protein> streamProteinByEcNumber(@Param("ecNumber") String ecNumber);

}
