package uk.ac.ebi.ep.dataservice.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author joseph
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "ENZYME_PORTAL_TAXONOMY")
@XmlRootElement
@NamedQuery(name = "EnzymePortalTaxonomy.findAll", query = "SELECT e FROM EnzymePortalTaxonomy e")
@NamedQuery(name = "EnzymePortalTaxonomy.findByTaxId", query = "SELECT e FROM EnzymePortalTaxonomy e WHERE e.taxId = :taxId")
@NamedQuery(name = "EnzymePortalTaxonomy.findByScientificName", query = "SELECT e FROM EnzymePortalTaxonomy e WHERE e.scientificName = :scientificName")
@NamedQuery(name = "EnzymePortalTaxonomy.findByCommonName", query = "SELECT e FROM EnzymePortalTaxonomy e WHERE e.commonName = :commonName")
@NamedQuery(name = "EnzymePortalTaxonomy.findByModelOrganism", query = "SELECT e FROM EnzymePortalTaxonomy e WHERE e.modelOrganism = :modelOrganism")
@NamedQuery(name = "EnzymePortalTaxonomy.findByNumberOfProteins", query = "SELECT e FROM EnzymePortalTaxonomy e WHERE e.numberOfProteins = :numberOfProteins")
public class EnzymePortalTaxonomy implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "TAX_ID")
    private Long taxId;
    @Size(max = 255)
    @Column(name = "SCIENTIFIC_NAME")
    private String scientificName;
    @Size(max = 255)
    @Column(name = "COMMON_NAME")
    private String commonName;
    @Column(name = "MODEL_ORGANISM")
    private BigInteger modelOrganism;
    @Column(name = "NUMBER_OF_PROTEINS")
    private BigInteger numberOfProteins;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.taxId);
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
        final EnzymePortalTaxonomy other = (EnzymePortalTaxonomy) obj;
        return Objects.equals(this.taxId, other.taxId);
    }

}
