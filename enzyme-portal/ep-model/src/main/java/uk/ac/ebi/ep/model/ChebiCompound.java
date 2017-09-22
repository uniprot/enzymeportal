package uk.ac.ebi.ep.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Entity
@Table(name = "CHEBI_COMPOUND")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChebiCompound.findAll", query = "SELECT c FROM ChebiCompound c"),
    @NamedQuery(name = "ChebiCompound.findByInternalId", query = "SELECT c FROM ChebiCompound c WHERE c.internalId = :internalId"),
    @NamedQuery(name = "ChebiCompound.findByChebiAccession", query = "SELECT c FROM ChebiCompound c WHERE c.chebiAccession = :chebiAccession"),
    @NamedQuery(name = "ChebiCompound.findByCompoundName", query = "SELECT c FROM ChebiCompound c WHERE c.compoundName = :compoundName"),
    @NamedQuery(name = "ChebiCompound.findBySource", query = "SELECT c FROM ChebiCompound c WHERE c.source = :source")})
public class ChebiCompound implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "INTERNAL_ID")
    private BigDecimal internalId;
    @Size(max = 30)
    @Column(name = "CHEBI_ACCESSION")
    private String chebiAccession;
    @Size(max = 4000)
    @Column(name = "COMPOUND_NAME")
    private String compoundName;
    @Size(max = 15)
    @Column(name = "SOURCE")
    private String source;

    public ChebiCompound() {
    }

    public ChebiCompound(BigDecimal internalId) {
        this.internalId = internalId;
    }

    public BigDecimal getInternalId() {
        return internalId;
    }

    public void setInternalId(BigDecimal internalId) {
        this.internalId = internalId;
    }

    public String getChebiAccession() {
        return chebiAccession;
    }

    public void setChebiAccession(String chebiAccession) {
        this.chebiAccession = chebiAccession;
    }

    public String getCompoundName() {
        return compoundName;
    }

    public void setCompoundName(String compoundName) {
        this.compoundName = compoundName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.chebiAccession);
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
        final ChebiCompound other = (ChebiCompound) obj;
        return Objects.equals(this.chebiAccession, other.chebiAccession);
    }



}
