/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.xml.entity.enzyme;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import uk.ac.ebi.ep.xml.entity.Enzyme;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Entity
@Table(name = "ENZYME_PORTAL_EC_NUMBERS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EnzymePortalEcNumbers.findAll", query = "SELECT e FROM EnzymePortalEcNumbers e"),
    @NamedQuery(name = "EnzymePortalEcNumbers.findByEcInternalId", query = "SELECT e FROM EnzymePortalEcNumbers e WHERE e.ecInternalId = :ecInternalId"),
    @NamedQuery(name = "EnzymePortalEcNumbers.findByEcFamily", query = "SELECT e FROM EnzymePortalEcNumbers e WHERE e.ecFamily = :ecFamily"),
    @NamedQuery(name = "EnzymePortalEcNumbers.findByEnzymeName", query = "SELECT e FROM EnzymePortalEcNumbers e WHERE e.enzymeName = :enzymeName"),
    @NamedQuery(name = "EnzymePortalEcNumbers.findByCatalyticActivity", query = "SELECT e FROM EnzymePortalEcNumbers e WHERE e.catalyticActivity = :catalyticActivity"),
    @NamedQuery(name = "EnzymePortalEcNumbers.findByTransferFlag", query = "SELECT e FROM EnzymePortalEcNumbers e WHERE e.transferFlag = :transferFlag"),
    @NamedQuery(name = "EnzymePortalEcNumbers.findByCofactor", query = "SELECT e FROM EnzymePortalEcNumbers e WHERE e.cofactor = :cofactor")})
public class EnzymePortalEcNumbers extends Enzyme implements Serializable {
    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "EC_NUMBER", referencedColumnName = "EC_NUMBER")
    @ManyToOne
    private EnzymePortalUniqueEc ecNumber;
    @JoinColumn(name = "UNIPROT_ACCESSION", referencedColumnName = "ACCESSION")
    @ManyToOne
    private UniprotEntryEnzyme uniprotAccession;

    public EnzymePortalEcNumbers() {
    }

    public EnzymePortalEcNumbers(BigDecimal ecInternalId) {
        this.ecInternalId = ecInternalId;
    }


    public EnzymePortalUniqueEc getEcNumber() {
        return ecNumber;
    }

    public void setEcNumber(EnzymePortalUniqueEc ecNumber) {
        this.ecNumber = ecNumber;
    }

    public UniprotEntryEnzyme getUniprotAccession() {
        return uniprotAccession;
    }

    public void setUniprotAccession(UniprotEntryEnzyme uniprotAccession) {
        this.uniprotAccession = uniprotAccession;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ecInternalId != null ? ecInternalId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
   
        if (!(object instanceof EnzymePortalEcNumbers)) {
            return false;
        }
        EnzymePortalEcNumbers other = (EnzymePortalEcNumbers) object;
        return !((this.ecInternalId == null && other.ecInternalId != null) || (this.ecInternalId != null && !this.ecInternalId.equals(other.ecInternalId)));
    }

    @Override
    public String toString() {
        return this.getEnzymeName();
    }
    
}
