package uk.ac.ebi.ep.dataservice.dto;

import java.io.Serializable;
import java.util.Objects;
import lombok.Data;

/**
 *
 * @author joseph
 */
@Data
public class Species implements Comparable<Species>, Serializable {
 private static final long serialVersionUID = 1L;
    private String scientificname;

    private String commonname;
    private boolean selected;
    private Long taxId;

    public Species() {
    }

    public Species( Long taxId,String scientificname) {
        this.taxId = taxId;
        this.scientificname = scientificname;
        
    }
    
    public Species(String scientificname, String commonname, Long taxId) {
        this.scientificname = scientificname;
        this.commonname = commonname;
        this.taxId = taxId;
    }


    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + Objects.hashCode(this.taxId);
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
        final Species other = (Species) obj;
        return Objects.equals(this.taxId, other.taxId);
    }



    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Species{");
        sb.append("taxId=").append(taxId);
        sb.append("scientificname=").append(scientificname);
        sb.append("commonname=").append(commonname);

        return sb.toString();
    }

    @Override
    public int compareTo(Species o) {
        return this.scientificname.compareToIgnoreCase(o.getScientificname());
    }

}
