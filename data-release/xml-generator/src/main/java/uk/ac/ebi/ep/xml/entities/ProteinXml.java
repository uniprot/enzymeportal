package uk.ac.ebi.ep.xml.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
//@RequiredArgsConstructor
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
@NamedQuery(name = "ProteinXml.findByPdbFlag", query = "SELECT p FROM ProteinXml p WHERE p.pdbFlag = :pdbFlag")
@NamedQuery(name = "ProteinXml.findByRelatedProteinsId", query = "SELECT p FROM ProteinXml p WHERE p.relatedProteinsId = :relatedProteinsId")
@NamedQuery(name = "ProteinXml.findByEntryType", query = "SELECT p FROM ProteinXml p WHERE p.entryType = :entryType")
@NamedQuery(name = "ProteinXml.findByGeneName", query = "SELECT p FROM ProteinXml p WHERE p.geneName = :geneName")
@NamedQuery(name = "ProteinXml.findByPrimaryAccession", query = "SELECT p FROM ProteinXml p WHERE p.primaryAccession = :primaryAccession")
@NamedQuery(name = "ProteinXml.findByPrimaryTaxId", query = "SELECT p FROM ProteinXml p WHERE p.primaryTaxId = :primaryTaxId")
@NamedQuery(name = "ProteinXml.findByPrimaryCommonName", query = "SELECT p FROM ProteinXml p WHERE p.primaryCommonName = :primaryCommonName")
@NamedQuery(name = "ProteinXml.findByPrimaryScientificName", query = "SELECT p FROM ProteinXml p WHERE p.primaryScientificName = :primaryScientificName")
@NamedQuery(name = "ProteinXml.findByPrimaryPdbId", query = "SELECT p FROM ProteinXml p WHERE p.primaryPdbId = :primaryPdbId")
@NamedQuery(name = "ProteinXml.findByPrimaryFunction", query = "SELECT p FROM ProteinXml p WHERE p.primaryFunction = :primaryFunction")
@NamedQuery(name = "ProteinXml.findByPrimaryPdbSpecies", query = "SELECT p FROM ProteinXml p WHERE p.primaryPdbSpecies = :primaryPdbSpecies")
@NamedQuery(name = "ProteinXml.findByPrimaryPdbLinkedAcc", query = "SELECT p FROM ProteinXml p WHERE p.primaryPdbLinkedAcc = :primaryPdbLinkedAcc")
@NamedQuery(name = "ProteinXml.findByPrimaryEntryType", query = "SELECT p FROM ProteinXml p WHERE p.primaryEntryType = :primaryEntryType")
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

    private static final long serialVersionUID = 1L;
    @Column(name = "PROTEIN_GROUP_ID")
    private String proteinGroupId;
    @Column(name = "PROTEIN_NAME")
    private String proteinName;
    @Basic(optional = false)
    @Column(name = "ACCESSION")
    private String accession;
    @Column(name = "TAX_ID")
    private Long taxId;
    @Column(name = "SCIENTIFIC_NAME")
    private String scientificName;
    @Column(name = "COMMON_NAME")
    private String commonName;
    @Column(name = "SYNONYM_NAMES")
    private String synonymNames;
    @Column(name = "EXP_EVIDENCE_FLAG")
    private BigInteger expEvidenceFlag;
    @Column(name = "PDB_FLAG")
    private Character pdbFlag;
    @Column(name = "RELATED_PROTEINS_ID")
    private BigInteger relatedProteinsId;
    @Column(name = "ENTRY_TYPE")
    private Short entryType;
    @Column(name = "GENE_NAME")
    private String geneName;
    @Column(name = "PRIMARY_ACCESSION")
    private String primaryAccession;
    @Column(name = "PRIMARY_TAX_ID")
    private BigInteger primaryTaxId;
    @Column(name = "PRIMARY_COMMON_NAME")
    private String primaryCommonName;
    @Column(name = "PRIMARY_SCIENTIFIC_NAME")
    private String primaryScientificName;
    @Column(name = "PRIMARY_PDB_ID")
    private String primaryPdbId;
    @Column(name = "PRIMARY_FUNCTION")
    private String primaryFunction;
    @Column(name = "PRIMARY_PDB_SPECIES")
    private String primaryPdbSpecies;
    @Column(name = "PRIMARY_PDB_LINKED_ACC")
    private String primaryPdbLinkedAcc;
    @Column(name = "PRIMARY_ENTRY_TYPE")
    private BigInteger primaryEntryType;
    @Column(name = "PRIMARY_RELATED_PROTEINS_ID")
    private BigInteger primaryRelatedProteinsId;
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
    @Column(name = "COMPOUND_SOURCE")
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
    @Column(name = "REACTION_SOURCE")
    private String reactionSource;
    @Column(name = "FAMILY_GROUP_ID")
    private String familyGroupId;
    @Column(name = "FAMILY_NAME")
    private String familyName;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "PROTEIN_XML_ID")
    private BigDecimal proteinXmlId;

//    public ProteinXml() {
//    }
//
//    public ProteinXml(BigDecimal proteinXmlId) {
//        this.proteinXmlId = proteinXmlId;
//    }
//
//    public ProteinXml(BigDecimal proteinXmlId, String accession) {
//        this.proteinXmlId = proteinXmlId;
//        this.accession = accession;
//    }
//
//    public String getProteinGroupId() {
//        return proteinGroupId;
//    }
//
//    public void setProteinGroupId(String proteinGroupId) {
//        this.proteinGroupId = proteinGroupId;
//    }
//
//    public String getProteinName() {
//        return proteinName;
//    }
//
//    public void setProteinName(String proteinName) {
//        this.proteinName = proteinName;
//    }
//
//    public String getAccession() {
//        return accession;
//    }
//
//    public void setAccession(String accession) {
//        this.accession = accession;
//    }
//
//    public Long getTaxId() {
//        return taxId;
//    }
//
//    public void setTaxId(Long taxId) {
//        this.taxId = taxId;
//    }
//
//    public String getScientificName() {
//        return scientificName;
//    }
//
//    public void setScientificName(String scientificName) {
//        this.scientificName = scientificName;
//    }

    public String getCommonName() {
       if(commonName == null){
           commonName = scientificName;
       }
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }
//
//    public String getSynonymNames() {
//        return synonymNames;
//    }
//
//    public void setSynonymNames(String synonymNames) {
//        this.synonymNames = synonymNames;
//    }
//
//    public BigInteger getExpEvidenceFlag() {
//        return expEvidenceFlag;
//    }
//
//    public void setExpEvidenceFlag(BigInteger expEvidenceFlag) {
//        this.expEvidenceFlag = expEvidenceFlag;
//    }
//
//    public Character getPdbFlag() {
//        return pdbFlag;
//    }
//
//    public void setPdbFlag(Character pdbFlag) {
//        this.pdbFlag = pdbFlag;
//    }
//
//    public BigInteger getRelatedProteinsId() {
//        return relatedProteinsId;
//    }
//
//    public void setRelatedProteinsId(BigInteger relatedProteinsId) {
//        this.relatedProteinsId = relatedProteinsId;
//    }
//
//    public Short getEntryType() {
//        return entryType;
//    }
//
//    public void setEntryType(Short entryType) {
//        this.entryType = entryType;
//    }
//
//    public String getGeneName() {
//        return geneName;
//    }
//
//    public void setGeneName(String geneName) {
//        this.geneName = geneName;
//    }
//
//    public String getPrimaryAccession() {
//        return primaryAccession;
//    }
//
//    public void setPrimaryAccession(String primaryAccession) {
//        this.primaryAccession = primaryAccession;
//    }
//
//    public BigInteger getPrimaryTaxId() {
//        return primaryTaxId;
//    }
//
//    public void setPrimaryTaxId(BigInteger primaryTaxId) {
//        this.primaryTaxId = primaryTaxId;
//    }
//
//    public String getPrimaryCommonName() {
//        return primaryCommonName;
//    }
//
//    public void setPrimaryCommonName(String primaryCommonName) {
//        this.primaryCommonName = primaryCommonName;
//    }
//
//    public String getPrimaryScientificName() {
//        return primaryScientificName;
//    }
//
//    public void setPrimaryScientificName(String primaryScientificName) {
//        this.primaryScientificName = primaryScientificName;
//    }
//
//    public String getPrimaryPdbId() {
//        return primaryPdbId;
//    }
//
//    public void setPrimaryPdbId(String primaryPdbId) {
//        this.primaryPdbId = primaryPdbId;
//    }
//
//    public String getPrimaryFunction() {
//        return primaryFunction;
//    }
//
//    public void setPrimaryFunction(String primaryFunction) {
//        this.primaryFunction = primaryFunction;
//    }
//
//    public String getPrimaryPdbSpecies() {
//        return primaryPdbSpecies;
//    }
//
//    public void setPrimaryPdbSpecies(String primaryPdbSpecies) {
//        this.primaryPdbSpecies = primaryPdbSpecies;
//    }
//
//    public String getPrimaryPdbLinkedAcc() {
//        return primaryPdbLinkedAcc;
//    }
//
//    public void setPrimaryPdbLinkedAcc(String primaryPdbLinkedAcc) {
//        this.primaryPdbLinkedAcc = primaryPdbLinkedAcc;
//    }
//
//    public BigInteger getPrimaryEntryType() {
//        return primaryEntryType;
//    }
//
//    public void setPrimaryEntryType(BigInteger primaryEntryType) {
//        this.primaryEntryType = primaryEntryType;
//    }
//
//    public BigInteger getPrimaryRelatedProteinsId() {
//        return primaryRelatedProteinsId;
//    }
//
//    public void setPrimaryRelatedProteinsId(BigInteger primaryRelatedProteinsId) {
//        this.primaryRelatedProteinsId = primaryRelatedProteinsId;
//    }
//
//    public String getOmimNumber() {
//        return omimNumber;
//    }
//
//    public void setOmimNumber(String omimNumber) {
//        this.omimNumber = omimNumber;
//    }
//
//    public String getDiseaseName() {
//        return diseaseName;
//    }
//
//    public void setDiseaseName(String diseaseName) {
//        this.diseaseName = diseaseName;
//    }
//
//    public String getCompoundId() {
//        return compoundId;
//    }
//
//    public void setCompoundId(String compoundId) {
//        this.compoundId = compoundId;
//    }
//
//    public String getCompoundName() {
//        return compoundName;
//    }
//
//    public void setCompoundName(String compoundName) {
//        this.compoundName = compoundName;
//    }
//
//    public String getCompoundRole() {
//        return compoundRole;
//    }
//
//    public void setCompoundRole(String compoundRole) {
//        this.compoundRole = compoundRole;
//    }
//
//    public String getCompoundSource() {
//        return compoundSource;
//    }
//
//    public void setCompoundSource(String compoundSource) {
//        this.compoundSource = compoundSource;
//    }
//
//    public String getReactantId() {
//        return reactantId;
//    }
//
//    public void setReactantId(String reactantId) {
//        this.reactantId = reactantId;
//    }
//
//    public String getReactantName() {
//        return reactantName;
//    }
//
//    public void setReactantName(String reactantName) {
//        this.reactantName = reactantName;
//    }
//
//    public String getReactantSource() {
//        return reactantSource;
//    }
//
//    public void setReactantSource(String reactantSource) {
//        this.reactantSource = reactantSource;
//    }
//
//    public String getEcNumber() {
//        return ecNumber;
//    }
//
//    public void setEcNumber(String ecNumber) {
//        this.ecNumber = ecNumber;
//    }
//
//    public Short getEcFamily() {
//        return ecFamily;
//    }
//
//    public void setEcFamily(Short ecFamily) {
//        this.ecFamily = ecFamily;
//    }
//
//    public String getCatalyticActivity() {
//        return catalyticActivity;
//    }
//
//    public void setCatalyticActivity(String catalyticActivity) {
//        this.catalyticActivity = catalyticActivity;
//    }
//
//    public String getPathwayId() {
//        return pathwayId;
//    }
//
//    public void setPathwayId(String pathwayId) {
//        this.pathwayId = pathwayId;
//    }
//
//    public String getPathwayName() {
//        return pathwayName;
//    }
//
//    public void setPathwayName(String pathwayName) {
//        this.pathwayName = pathwayName;
//    }
//
//    public String getReactionId() {
//        return reactionId;
//    }
//
//    public void setReactionId(String reactionId) {
//        this.reactionId = reactionId;
//    }
//
//    public String getReactionSource() {
//        return reactionSource;
//    }
//
//    public void setReactionSource(String reactionSource) {
//        this.reactionSource = reactionSource;
//    }
//
//    public String getFamilyGroupId() {
//        return familyGroupId;
//    }
//
//    public void setFamilyGroupId(String familyGroupId) {
//        this.familyGroupId = familyGroupId;
//    }
//
//    public String getFamilyName() {
//        return familyName;
//    }
//
//    public void setFamilyName(String familyName) {
//        this.familyName = familyName;
//    }
//
//    public BigDecimal getProteinXmlId() {
//        return proteinXmlId;
//    }
//
//    public void setProteinXmlId(BigDecimal proteinXmlId) {
//        this.proteinXmlId = proteinXmlId;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (proteinXmlId != null ? proteinXmlId.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        if (!(object instanceof ProteinXml)) {
//            return false;
//        }
//        ProteinXml other = (ProteinXml) object;
//        return !((this.proteinXmlId == null && other.proteinXmlId != null) || (this.proteinXmlId != null && !this.proteinXmlId.equals(other.proteinXmlId)));
//    }


}
