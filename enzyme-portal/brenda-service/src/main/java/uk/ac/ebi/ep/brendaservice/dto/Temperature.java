package uk.ac.ebi.ep.brendaservice.dto;

import java.io.Serializable;
import java.util.Objects;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 *
 * @author joseph
 */
@Data
@Builder
@ToString
public class Temperature implements Serializable{

    private String ecNumber;
    private String temperatureRange;
    private String organism;
    private String comment;
    private String accession;

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.organism);
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
        final Temperature other = (Temperature) obj;
        return Objects.equals(this.organism, other.organism);
    }
}
