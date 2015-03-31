/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import uk.ac.ebi.ep.data.search.model.EcNumber;

/**
 *
 * @author joseph
 */
@Entity
@Table(name = "ENZYME_PORTAL_EC_NUMBERS")
@XmlRootElement

@NamedEntityGraph(name = "EcNumberEntityGraph", attributeNodes = {  
    @NamedAttributeNode("uniprotAccession")
})
@NamedQueries({
    @NamedQuery(name = "EnzymePortalEcNumbers.findAll", query = "SELECT e FROM EnzymePortalEcNumbers e"),
    @NamedQuery(name = "EnzymePortalEcNumbers.findByEcInternalId", query = "SELECT e FROM EnzymePortalEcNumbers e WHERE e.ecInternalId = :ecInternalId")
    //@NamedQuery(name = "EnzymePortalEcNumbers.findByEcNumber", query = "SELECT e FROM EnzymePortalEcNumbers e WHERE e.ecNumber = :ecNumber")
})
public class EnzymePortalEcNumbers extends EcNumber implements Serializable, Comparable<EnzymePortalEcNumbers> {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "EC_INTERNAL_ID")
    private BigDecimal ecInternalId;
    @Column(name = "EC_NUMBER")
    private String ecNumber;
    @JoinColumn(name = "UNIPROT_ACCESSION", referencedColumnName = "ACCESSION")
    @ManyToOne
    private UniprotEntry uniprotAccession;

    public EnzymePortalEcNumbers() {
    }

    public EnzymePortalEcNumbers(BigDecimal ecInternalId) {
        this.ecInternalId = ecInternalId;
    }

    public BigDecimal getEcInternalId() {
        return ecInternalId;
    }

    public void setEcInternalId(BigDecimal ecInternalId) {
        this.ecInternalId = ecInternalId;
    }

    public String getEcNumber() {
        return ecNumber;
    }

    public void setEcNumber(String ecNumber) {
        this.ecNumber = ecNumber;
    }

    public UniprotEntry getUniprotAccession() {
        return uniprotAccession;
    }

    public void setUniprotAccession(UniprotEntry uniprotAccession) {
        this.uniprotAccession = uniprotAccession;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.getFamily());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EnzymePortalEcNumbers other = (EnzymePortalEcNumbers) obj;
        return Objects.equals(this.getFamily(), other.getFamily());
    }

    @Override
    public String toString() {
        return ecNumber;
    }

    /**
     *
     * @return the enzyme family representation of the ec class
     */
    @Override
    public String getFamily() {
        Optional<String> ec = Optional.ofNullable(this.getEcNumber());
        if (ec.isPresent()) {
            return computeFamily(ec.get());
        }
        return ec.orElse("");
    }

    /**
     *
     * @return ec class
     */
    @Override
    public String getEc() {
        return computeEc(getFamily());
        
    }

    @Override
    public int compareTo(EnzymePortalEcNumbers o) {
        return this.getFamily().compareToIgnoreCase(o.getFamily());

    }

}
