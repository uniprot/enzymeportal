package uk.ac.ebi.ep.xml.transformer;

import java.util.Objects;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author joseph
 */
@Data
@NoArgsConstructor
@ToString
public class ProteinMapper {

    private String accession;
    private String organismName;
    private int entryType;
    private int expEvidence;
    private long taxid;

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + Objects.hashCode(this.accession);
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
        final ProteinMapper other = (ProteinMapper) obj;
        return Objects.equals(this.accession, other.accession);
    }

}
