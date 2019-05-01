/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author joseph
 */
@Entity
@Table(name = "ENZYME_PORTAL_COFACTOR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EnzymePortalCofactor.findAll", query = "SELECT e FROM EnzymePortalCofactor e"),
    @NamedQuery(name = "EnzymePortalCofactor.findByCofactorInternalId", query = "SELECT e FROM EnzymePortalCofactor e WHERE e.cofactorInternalId = :cofactorInternalId"),
    @NamedQuery(name = "EnzymePortalCofactor.findByCofactorId", query = "SELECT e FROM EnzymePortalCofactor e WHERE e.cofactorId = :cofactorId"),
    @NamedQuery(name = "EnzymePortalCofactor.findByCofactorName", query = "SELECT e FROM EnzymePortalCofactor e WHERE e.cofactorName = :cofactorName")})
public class EnzymePortalCofactor implements Serializable {

    private static final long serialVersionUID = 1L;
   @Id
    @Basic(optional = false)
    @NotNull
        @SequenceGenerator(allocationSize = 1, name = "cofactorSeqGenerator", sequenceName = "SEQ_COFACTOR_INTERNAL_ID")
    @GeneratedValue(generator = "cofactorSeqGenerator", strategy = GenerationType.AUTO)
    @Column(name = "COFACTOR_INTERNAL_ID")
    private Long cofactorInternalId;
    @Size(max = 15)
    @Column(name = "COFACTOR_ID")
    private String cofactorId;
    @Size(max = 4000)
    @Column(name = "COFACTOR_NAME")
    private String cofactorName;

    public EnzymePortalCofactor() {
    }

    public EnzymePortalCofactor(Long cofactorInternalId) {
        this.cofactorInternalId = cofactorInternalId;
    }

    public Long getCofactorInternalId() {
        return cofactorInternalId;
    }

    public void setCofactorInternalId(Long cofactorInternalId) {
        this.cofactorInternalId = cofactorInternalId;
    }

    public String getCofactorId() {
        return cofactorId;
    }

    public void setCofactorId(String cofactorId) {
        this.cofactorId = cofactorId;
    }

    public String getCofactorName() {
        return cofactorName;
    }

    public void setCofactorName(String cofactorName) {
        this.cofactorName = cofactorName;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.cofactorId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EnzymePortalCofactor other = (EnzymePortalCofactor) obj;
        return Objects.equals(this.cofactorId, other.cofactorId);
    }



    @Override
    public String toString() {
        return cofactorId;
    }
    
}
