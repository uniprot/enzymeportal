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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author joseph
 */
@Entity
@Table(name = "PREFIX_LIST")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PrefixList.findAll", query = "SELECT p FROM PrefixList p"),
    @NamedQuery(name = "PrefixList.findByPrefixId", query = "SELECT p FROM PrefixList p WHERE p.prefixId = :prefixId"),
    @NamedQuery(name = "PrefixList.findByNamePrefix", query = "SELECT p FROM PrefixList p WHERE p.namePrefix = :namePrefix")})
public class PrefixList implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "PREFIX_ID")
    private BigDecimal prefixId;
    @Column(name = "NAME_PREFIX")
    private String namePrefix;


    public PrefixList() {
    }

    public PrefixList(BigDecimal prefixId) {
        this.prefixId = prefixId;
    }

    public BigDecimal getPrefixId() {
        return prefixId;
    }

    public void setPrefixId(BigDecimal prefixId) {
        this.prefixId = prefixId;
    }

    public String getNamePrefix() {
        return namePrefix;
    }

    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (prefixId != null ? prefixId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrefixList)) {
            return false;
        }
        PrefixList other = (PrefixList) object;
        return !((this.prefixId == null && other.prefixId != null) || (this.prefixId != null && !this.prefixId.equals(other.prefixId)));
    }

    @Override
    public String toString() {
        return "uk.ac.ebi.ep.data.domain.PrefixList[ prefixId=" + prefixId + " ]";
    }
    
}
