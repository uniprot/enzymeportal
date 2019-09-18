package uk.ac.ebi.ep.xml.entities;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author joseph
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "PROTEIN_XML")
@XmlRootElement

@NamedQuery(name = "ProteinXml.findAll", query = "SELECT p FROM ProteinXml p")
@NamedQuery(name = "ProteinXml.findByProteinGroupId", query = "SELECT p FROM ProteinXml p WHERE p.proteinGroupId = :proteinGroupId")
@NamedQuery(name = "ProteinXml.findByProteinName", query = "SELECT p FROM ProteinXml p WHERE p.proteinName = :proteinName")
@NamedQuery(name = "ProteinXml.findByAccession", query = "SELECT p FROM ProteinXml p WHERE p.accession = :accession")
@NamedQuery(name = "ProteinXml.findByTaxId", query = "SELECT p FROM ProteinXml p WHERE p.taxId = :taxId")
@NamedQuery(name = "ProteinXml.findByScientificName", query = "SELECT p FROM ProteinXml p WHERE p.scientificName = :scientificName")
@NamedQuery(name = "ProteinXml.findByCommonName", query = "SELECT p FROM ProteinXml p WHERE p.commonName = :commonName")
@NamedQuery(name = "ProteinXml.findBySynonymNames", query = "SELECT p FROM ProteinXml p WHERE p.synonymNames = :synonymNames")
@NamedQuery(name = "ProteinXml.findByExpEvidenceFlag", query = "SELECT p FROM ProteinXml p WHERE p.expEvidenceFlag = :expEvidenceFlag")
//@NamedQuery(name = "ProteinXml.findByPdbFlag", query = "SELECT p FROM ProteinXml p WHERE p.pdbFlag = :pdbFlag")
@NamedQuery(name = "ProteinXml.findByRelatedProteinsId", query = "SELECT p FROM ProteinXml p WHERE p.relatedProteinsId = :relatedProteinsId")
@NamedQuery(name = "ProteinXml.findByEntryType", query = "SELECT p FROM ProteinXml p WHERE p.entryType = :entryType")
@NamedQuery(name = "ProteinXml.findByGeneName", query = "SELECT p FROM ProteinXml p WHERE p.geneName = :geneName")
@NamedQuery(name = "ProteinXml.findByPrimaryAccession", query = "SELECT p FROM ProteinXml p WHERE p.primaryAccession = :primaryAccession")
//@NamedQuery(name = "ProteinXml.findByPrimaryTaxId", query = "SELECT p FROM ProteinXml p WHERE p.primaryTaxId = :primaryTaxId")
//@NamedQuery(name = "ProteinXml.findByPrimaryCommonName", query = "SELECT p FROM ProteinXml p WHERE p.primaryCommonName = :primaryCommonName")
//@NamedQuery(name = "ProteinXml.findByPrimaryScientificName", query = "SELECT p FROM ProteinXml p WHERE p.primaryScientificName = :primaryScientificName")
//@NamedQuery(name = "ProteinXml.findByPrimaryPdbId", query = "SELECT p FROM ProteinXml p WHERE p.primaryPdbId = :primaryPdbId")
//@NamedQuery(name = "ProteinXml.findByPrimaryFunction", query = "SELECT p FROM ProteinXml p WHERE p.primaryFunction = :primaryFunction")
//@NamedQuery(name = "ProteinXml.findByPrimaryPdbSpecies", query = "SELECT p FROM ProteinXml p WHERE p.primaryPdbSpecies = :primaryPdbSpecies")
//@NamedQuery(name = "ProteinXml.findByPrimaryPdbLinkedAcc", query = "SELECT p FROM ProteinXml p WHERE p.primaryPdbLinkedAcc = :primaryPdbLinkedAcc")
//@NamedQuery(name = "ProteinXml.findByPrimaryEntryType", query = "SELECT p FROM ProteinXml p WHERE p.primaryEntryType = :primaryEntryType")
@NamedQuery(name = "ProteinXml.findByPrimaryRelatedProteinsId", query = "SELECT p FROM ProteinXml p WHERE p.primaryRelatedProteinsId = :primaryRelatedProteinsId")
@NamedQuery(name = "ProteinXml.findByOmimNumber", query = "SELECT p FROM ProteinXml p WHERE p.omimNumber = :omimNumber")
@NamedQuery(name = "ProteinXml.findByDiseaseName", query = "SELECT p FROM ProteinXml p WHERE p.diseaseName = :diseaseName")
@NamedQuery(name = "ProteinXml.findByCompoundId", query = "SELECT p FROM ProteinXml p WHERE p.compoundId = :compoundId")
@NamedQuery(name = "ProteinXml.findByCompoundName", query = "SELECT p FROM ProteinXml p WHERE p.compoundName = :compoundName")
@NamedQuery(name = "ProteinXml.findByCompoundRole", query = "SELECT p FROM ProteinXml p WHERE p.compoundRole = :compoundRole")
@NamedQuery(name = "ProteinXml.findByCompoundSource", query = "SELECT p FROM ProteinXml p WHERE p.compoundSource = :compoundSource")
@NamedQuery(name = "ProteinXml.findByReactantId", query = "SELECT p FROM ProteinXml p WHERE p.reactantId = :reactantId")
@NamedQuery(name = "ProteinXml.findByReactantName", query = "SELECT p FROM ProteinXml p WHERE p.reactantName = :reactantName")
@NamedQuery(name = "ProteinXml.findByReactantSource", query = "SELECT p FROM ProteinXml p WHERE p.reactantSource = :reactantSource")
@NamedQuery(name = "ProteinXml.findByEcNumber", query = "SELECT p FROM ProteinXml p WHERE p.ecNumber = :ecNumber")
@NamedQuery(name = "ProteinXml.findByEcFamily", query = "SELECT p FROM ProteinXml p WHERE p.ecFamily = :ecFamily")
@NamedQuery(name = "ProteinXml.findByCatalyticActivity", query = "SELECT p FROM ProteinXml p WHERE p.catalyticActivity = :catalyticActivity")
@NamedQuery(name = "ProteinXml.findByPathwayId", query = "SELECT p FROM ProteinXml p WHERE p.pathwayId = :pathwayId")
@NamedQuery(name = "ProteinXml.findByPathwayName", query = "SELECT p FROM ProteinXml p WHERE p.pathwayName = :pathwayName")
@NamedQuery(name = "ProteinXml.findByReactionId", query = "SELECT p FROM ProteinXml p WHERE p.reactionId = :reactionId")
@NamedQuery(name = "ProteinXml.findByReactionSource", query = "SELECT p FROM ProteinXml p WHERE p.reactionSource = :reactionSource")
@NamedQuery(name = "ProteinXml.findByFamilyGroupId", query = "SELECT p FROM ProteinXml p WHERE p.familyGroupId = :familyGroupId")
@NamedQuery(name = "ProteinXml.findByFamilyName", query = "SELECT p FROM ProteinXml p WHERE p.familyName = :familyName")
@NamedQuery(name = "ProteinXml.findByProteinXmlId", query = "SELECT p FROM ProteinXml p WHERE p.proteinXmlId = :proteinXmlId")
public class ProteinXml implements Serializable {

    @Column(name = "TAX_ID")
    private Long taxId;
    @Column(name = "EXP_EVIDENCE_FLAG")
    private BigInteger expEvidenceFlag;
    @Column(name = "RELATED_PROTEINS_ID")
    private BigInteger relatedProteinsId;
    @Column(name = "PRIMARY_RELATED_PROTEINS_ID")
    private BigInteger primaryRelatedProteinsId;
    @Column(name = "CHEBI_COMPOUND_ID")
    private String chebiCompoundId;
    @Column(name = "CHEBI_COMPOUND_NAME")
    private String chebiCompoundName;
    @Column(name = "CHEBI_COMPOUND_ROLE")
    private String chebiCompoundRole;
    @Column(name = "CHEBI_SYNONYMS")
    private String chebiSynonyms;

    private static final long serialVersionUID = 1L;
    @Column(name = "PROTEIN_GROUP_ID")
    private String proteinGroupId;
    @Column(name = "PROTEIN_NAME")
    private String proteinName;
    @Basic(optional = false)
    @Column(name = "ACCESSION")
    private String accession;
    @Column(name = "SCIENTIFIC_NAME")
    private String scientificName;
    @Column(name = "COMMON_NAME")
    private String commonName;
    @Column(name = "SYNONYM_NAMES")
    private String synonymNames;
    @Column(name = "ENTRY_TYPE")
    private Short entryType;
    @Column(name = "GENE_NAME")
    private String geneName;
    @Column(name = "PRIMARY_ACCESSION")
    private String primaryAccession;
    @Column(name = "OMIM_NUMBER")
    private String omimNumber;
    @Column(name = "DISEASE_NAME")
    private String diseaseName;
    @Column(name = "COMPOUND_ID")
    private String compoundId;
    @Column(name = "COMPOUND_NAME")
    private String compoundName;
    @Column(name = "COMPOUND_ROLE")
    private String compoundRole;
    @Column(name = "COMPOUND_SOURCE")//
    private String compoundSource;
    @Column(name = "REACTANT_ID")
    private String reactantId;
    @Column(name = "REACTANT_NAME")
    private String reactantName;
    @Column(name = "REACTANT_SOURCE")
    private String reactantSource;
    @Column(name = "EC_NUMBER")
    private String ecNumber;
    @Column(name = "EC_FAMILY")
    private Short ecFamily;
    @Column(name = "CATALYTIC_ACTIVITY")
    private String catalyticActivity;
    @Column(name = "PATHWAY_ID")
    private String pathwayId;
    @Column(name = "PATHWAY_NAME")
    private String pathwayName;
    @Column(name = "REACTION_ID")
    private String reactionId;
    @Column(name = "REACTION_SOURCE")//
    private String reactionSource;
    @Column(name = "FAMILY_GROUP_ID")
    private String familyGroupId;
    @Column(name = "FAMILY_NAME")
    private String familyName;
    @Id
    @Basic(optional = false)
    @Column(name = "PROTEIN_XML_ID")
    private long proteinXmlId;

    public String getCommonName() {
        if (commonName == null) {
            commonName = scientificName;
        }
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

}
