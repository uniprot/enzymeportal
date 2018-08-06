package uk.ac.ebi.ep.xml.entity.enzyme;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import uk.ac.ebi.ep.xml.entity.Protein;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Entity
@Table(name = "UNIPROT_ENTRY")
@XmlRootElement
public class UniprotEntryEnzyme extends Protein implements Serializable {

    private static final long serialVersionUID = 1L;

    @OneToMany(mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
    private Set<EnzymePortalEcNumbers> enzymePortalEcNumbersSet;

    public UniprotEntryEnzyme() {
    }

    @XmlTransient
    public Set<EnzymePortalEcNumbers> getEnzymePortalEcNumbersSet() {
        if (enzymePortalEcNumbersSet == null) {
            enzymePortalEcNumbersSet = new HashSet<>();
        }

        return enzymePortalEcNumbersSet;
    }

    public void setEnzymePortalEcNumbersSet(Set<EnzymePortalEcNumbers> enzymePortalEcNumbersSet) {
        this.enzymePortalEcNumbersSet = enzymePortalEcNumbersSet;
    }

}
