package uk.ac.ebi.ep.dataservice.dto;

import java.util.Objects;
import lombok.Data;

/**
 *
 * @author joseph
 */
@Data
public class Pathway {

    private String pathwayId;
    private String pathwayName;
    private String pathwayGroupId;

    public Pathway() {
    }

    public Pathway(String pathwayId, String pathwayName) {
        this.pathwayId = pathwayId;
        this.pathwayName = pathwayName;

    }

    public Pathway(String pathwayGroupId, String pathwayId, String pathwayName) {

        this.pathwayId = pathwayId;
        this.pathwayName = pathwayName;
        this.pathwayGroupId = pathwayGroupId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.pathwayGroupId);
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
        final Pathway other = (Pathway) obj;
        return Objects.equals(this.pathwayGroupId, other.pathwayGroupId);
    }

}
