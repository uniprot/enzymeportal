package uk.ac.ebi.ep.dataservice.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
@Table(name = "RELATED_PROTEINS")
@XmlRootElement
@NamedQuery(name = "RelatedProteins.findAll", query = "SELECT r FROM RelatedProteins r")
@NamedQuery(name = "RelatedProteins.findByRelProtInternalId", query = "SELECT r FROM RelatedProteins r WHERE r.relProtInternalId = :relProtInternalId")
@NamedQuery(name = "RelatedProteins.findByNamePrefix", query = "SELECT r FROM RelatedProteins r WHERE r.namePrefix = :namePrefix")
public class RelatedProteins implements Serializable {

    @Size(max = 120)
    @Column(name = "NAME_PREFIX")
    private String namePrefix;

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "REL_PROT_INTERNAL_ID")
    private BigDecimal relProtInternalId;
    @OneToMany(mappedBy = "relatedProteinsId")
    private Set<UniprotEntry> uniprotEntrySet;

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.relProtInternalId);
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
        final RelatedProteins other = (RelatedProteins) obj;
        return Objects.equals(this.relProtInternalId, other.relProtInternalId);
    }

}
