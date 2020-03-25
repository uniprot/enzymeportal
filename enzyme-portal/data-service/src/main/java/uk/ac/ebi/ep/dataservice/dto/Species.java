package uk.ac.ebi.ep.dataservice.dto;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * @author joseph
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Species implements Comparable<Species>, Serializable {

    private static final long serialVersionUID = 1L;
    @ToString.Include
    private String scientificname;
    @ToString.Include
    private String commonname;
    private boolean selected;
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long taxId;

    public Species() {
    }

    public Species(Long taxId, String scientificname) {
        this.taxId = taxId;
        this.scientificname = scientificname;

    }

    public Species(String scientificname, String commonname, Long taxId) {
        this.scientificname = scientificname;
        this.commonname = commonname;
        this.taxId = taxId;
    }

    @Override
    public int compareTo(Species o) {
        return this.scientificname.compareToIgnoreCase(o.getScientificname());
    }

}
