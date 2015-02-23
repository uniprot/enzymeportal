/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author joseph
 */
@Entity
@Table(name = "ENZYME_XML_STORE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EnzymeXmlStore.findAll", query = "SELECT e FROM EnzymeXmlStore e"),
    @NamedQuery(name = "EnzymeXmlStore.findByXmlTag", query = "SELECT e FROM EnzymeXmlStore e WHERE e.xmlTag = :xmlTag"),
    @NamedQuery(name = "EnzymeXmlStore.findBySource", query = "SELECT e FROM EnzymeXmlStore e WHERE e.source = :source"),
    @NamedQuery(name = "EnzymeXmlStore.findByEnzymeXmlId", query = "SELECT e FROM EnzymeXmlStore e WHERE e.enzymeXmlId = :enzymeXmlId")})
public class EnzymeXmlStore implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "XML_TAG")
    private String xmlTag;
    @Lob
    @Column(name = "XML_VALUE")
    private String xmlValue;
    @Column(name = "SOURCE")
    private String source;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ENZYME_XML_ID")
    private BigDecimal enzymeXmlId;
    @JoinColumn(name = "ACCESSION", referencedColumnName = "ACCESSION")
    @ManyToOne
    private UniprotEntry accession;

    public EnzymeXmlStore() {
    }

    public EnzymeXmlStore(BigDecimal enzymeXmlId) {
        this.enzymeXmlId = enzymeXmlId;
    }

    public String getXmlTag() {
        return xmlTag;
    }

    public void setXmlTag(String xmlTag) {
        this.xmlTag = xmlTag;
    }

    public String getXmlValue() {
        return xmlValue;
    }

    public void setXmlValue(String xmlValue) {
        this.xmlValue = xmlValue;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public BigDecimal getEnzymeXmlId() {
        return enzymeXmlId;
    }

    public void setEnzymeXmlId(BigDecimal enzymeXmlId) {
        this.enzymeXmlId = enzymeXmlId;
    }

    public UniprotEntry getAccession() {
        return accession;
    }

    public void setAccession(UniprotEntry accession) {
        this.accession = accession;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (enzymeXmlId != null ? enzymeXmlId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EnzymeXmlStore)) {
            return false;
        }
        EnzymeXmlStore other = (EnzymeXmlStore) object;
        if ((this.enzymeXmlId == null && other.enzymeXmlId != null) || (this.enzymeXmlId != null && !this.enzymeXmlId.equals(other.enzymeXmlId))) {
            return false;
        }
        return true;
    }

//    @Override
//    public String toString() {
//        return "uk.ac.ebi.ep.data.domain.EnzymeXmlStore[ enzymeXmlId=" + enzymeXmlId + " ]";
//    }

    @Override
    public String toString() {
        return "EnzymeXmlStore{" + "xmlTag=" + xmlTag + ", xmlValue=" + xmlValue + ", source=" + source + ", enzymeXmlId=" + enzymeXmlId + ", accession=" + accession.getAccession() + '}';
    }
    
}
