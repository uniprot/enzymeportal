package uk.ac.ebi.ep.dataservice.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "ENZYME_PORTAL_PATHWAYS")
@XmlRootElement

@NamedQuery(name = "EnzymePortalPathways.findAll", query = "SELECT e FROM EnzymePortalPathways e")
@NamedQuery(name = "EnzymePortalPathways.findByPathwayInternalId", query = "SELECT e FROM EnzymePortalPathways e WHERE e.pathwayInternalId = :pathwayInternalId")
@NamedQuery(name = "EnzymePortalPathways.findByPathwayId", query = "SELECT e FROM EnzymePortalPathways e WHERE e.pathwayId = :pathwayId")
@NamedQuery(name = "EnzymePortalPathways.findByPathwayUrl", query = "SELECT e FROM EnzymePortalPathways e WHERE e.pathwayUrl = :pathwayUrl")
@NamedQuery(name = "EnzymePortalPathways.findByPathwayName", query = "SELECT e FROM EnzymePortalPathways e WHERE e.pathwayName = :pathwayName")
@NamedQuery(name = "EnzymePortalPathways.findByStatus", query = "SELECT e FROM EnzymePortalPathways e WHERE e.status = :status")
@NamedQuery(name = "EnzymePortalPathways.findBySpecies", query = "SELECT e FROM EnzymePortalPathways e WHERE e.species = :species")
@NamedQuery(name = "EnzymePortalPathways.findByPathwayGroupId", query = "SELECT e FROM EnzymePortalPathways e WHERE e.pathwayGroupId = :pathwayGroupId")
public class EnzymePortalPathways implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "PATHWAY_INTERNAL_ID")
    private BigDecimal pathwayInternalId;
    @Size(max = 15)
    @Column(name = "PATHWAY_ID")
    private String pathwayId;
    @Size(max = 255)
    @Column(name = "PATHWAY_URL")
    private String pathwayUrl;
    @Size(max = 4000)
    @Column(name = "PATHWAY_NAME")
    private String pathwayName;
    @Size(max = 5)
    @Column(name = "STATUS")
    private String status;
    @Size(max = 255)
    @Column(name = "SPECIES")
    private String species;
    @Size(max = 15)
    @Column(name = "PATHWAY_GROUP_ID")
    private String pathwayGroupId;
    @JoinColumn(name = "UNIPROT_ACCESSION", referencedColumnName = "ACCESSION")
    @ManyToOne
    private UniprotEntry uniprotAccession;

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + Objects.hashCode(this.pathwayInternalId);
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
        final EnzymePortalPathways other = (EnzymePortalPathways) obj;
        return Objects.equals(this.pathwayInternalId, other.pathwayInternalId);
    }

}
