
package uk.ac.ebi.ep.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author joseph
 */
@Entity
@Table(name = "ENZYME_COFACTOR")
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
    @Size(max = 255)
    @Column(name = "COFACTOR_URL")
    private String cofactorUrl;

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

    public String getCofactorUrl() {
        return cofactorUrl;
    }

    public void setCofactorUrl(String cofactorUrl) {
        this.cofactorUrl = cofactorUrl;
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
