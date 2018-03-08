///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package uk.ac.ebi.ep.xml.entity;
//
//import java.io.Serializable;
//import java.math.BigInteger;
//import java.util.Set;
//import javax.persistence.Basic;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.NamedQueries;
//import javax.persistence.NamedQuery;
//import javax.persistence.OneToMany;
//import javax.persistence.Table;
//import javax.xml.bind.annotation.XmlRootElement;
//import javax.xml.bind.annotation.XmlTransient;
//
///**
// *
// * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
// */
//@Entity
//@Table(name = "PROTEIN_GROUPS")
//@XmlRootElement
//@NamedQueries({
//    @NamedQuery(name = "ProteinGroups.findAll", query = "SELECT p FROM ProteinGroups p"),
//    @NamedQuery(name = "ProteinGroups.findByProteinName", query = "SELECT p FROM ProteinGroups p WHERE p.proteinName = :proteinName"),
//    @NamedQuery(name = "ProteinGroups.findByProteinGroupId", query = "SELECT p FROM ProteinGroups p WHERE p.proteinGroupId = :proteinGroupId"),
//    @NamedQuery(name = "ProteinGroups.findByEntryType", query = "SELECT p FROM ProteinGroups p WHERE p.entryType = :entryType")})
//public class ProteinGroups implements Serializable {
//    private static final long serialVersionUID = 1L;
//    @Column(name = "PROTEIN_NAME")
//    private String proteinName;
//    @Id
//    @Basic(optional = false)
//    @Column(name = "PROTEIN_GROUP_ID")
//    private String proteinGroupId;
//    @Column(name = "ENTRY_TYPE")
//    private BigInteger entryType;
//    @OneToMany(mappedBy = "proteinGroupId")
//    private Set<UniprotEntry> uniprotEntrySet;
//
//    public ProteinGroups() {
//    }
//
//    public ProteinGroups(String proteinGroupId) {
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
//    public String getProteinGroupId() {
//        return proteinGroupId;
//    }
//
//    public void setProteinGroupId(String proteinGroupId) {
//        this.proteinGroupId = proteinGroupId;
//    }
//
//    public BigInteger getEntryType() {
//        return entryType;
//    }
//
//    public void setEntryType(BigInteger entryType) {
//        this.entryType = entryType;
//    }
//
//    @XmlTransient
//    public Set<UniprotEntry> getUniprotEntrySet() {
//        return uniprotEntrySet;
//    }
//
//    public void setUniprotEntrySet(Set<UniprotEntry> uniprotEntrySet) {
//        this.uniprotEntrySet = uniprotEntrySet;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (proteinGroupId != null ? proteinGroupId.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof ProteinGroups)) {
//            return false;
//        }
//        ProteinGroups other = (ProteinGroups) object;
//        if ((this.proteinGroupId == null && other.proteinGroupId != null) || (this.proteinGroupId != null && !this.proteinGroupId.equals(other.proteinGroupId))) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "uk.ac.ebi.ep.xmlpipeline.model.ProteinGroups[ proteinGroupId=" + proteinGroupId + " ]";
//    }
//    
//}
