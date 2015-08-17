/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import uk.ac.ebi.ep.data.search.model.EnzymeAccession;

/**
 *
 * @author joseph
 */
@Entity
@Table(name = "RELATED_PROTEINS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RelatedProteins.findAll", query = "SELECT r FROM RelatedProteins r"),
    @NamedQuery(name = "RelatedProteins.findByRelProtInternalId", query = "SELECT r FROM RelatedProteins r WHERE r.relProtInternalId = :relProtInternalId")
    //@NamedQuery(name = "RelatedProteins.findByNamePrefix", query = "SELECT r FROM RelatedProteins r WHERE r.namePrefix = :namePrefix")
})
public class RelatedProteins extends EnzymeAccession implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "REL_PROT_INTERNAL_ID")
    @SequenceGenerator(allocationSize = 1, name = "seqGenerator", sequenceName = "SEQ_REL_PROT_ID")
    @GeneratedValue(generator = "seqGenerator", strategy = GenerationType.SEQUENCE)
    private BigDecimal relProtInternalId;
    @Column(name = "NAME_PREFIX")
    private String namePrefix;
     //@OneToMany(mappedBy = "relatedProteinsId", fetch = FetchType.EAGER)
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "relatedProteinsId")
    //@BatchSize(size = 100)
    @Fetch(FetchMode.JOIN)
    private Set<UniprotEntry> uniprotEntrySet;

    public RelatedProteins() {
    }

    public RelatedProteins(BigDecimal relProtInternalId) {
        this.relProtInternalId = relProtInternalId;
    }

    public BigDecimal getRelProtInternalId() {
        return relProtInternalId;
    }

    public void setRelProtInternalId(BigDecimal relProtInternalId) {
        this.relProtInternalId = relProtInternalId;
    }

    public String getNamePrefix() {
        return namePrefix;
    }

    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    @XmlTransient
    public Set<UniprotEntry> getUniprotEntrySet() {
        return uniprotEntrySet;
    }

    public void setUniprotEntrySet(Set<UniprotEntry> uniprotEntrySet) {
        this.uniprotEntrySet = uniprotEntrySet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (relProtInternalId != null ? relProtInternalId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RelatedProteins)) {
            return false;
        }
        RelatedProteins other = (RelatedProteins) object;
        if ((this.relProtInternalId == null && other.relProtInternalId != null) || (this.relProtInternalId != null && !this.relProtInternalId.equals(other.relProtInternalId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uk.ac.ebi.ep.data.domain.RelatedProteins[" + relProtInternalId + " ]";
    }

}
