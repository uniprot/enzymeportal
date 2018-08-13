
package uk.ac.ebi.ep.parser.model;

import java.util.Objects;

/**
 *
 * @author Joseph
 */
public class Rhea2kegg implements Comparable<Rhea2kegg> {

    private String rheaId;
    private String keggId;

    public Rhea2kegg() {
    }

    public Rhea2kegg(String rheaId, String keggId) {
        this.rheaId = rheaId;
        this.keggId = keggId;
    }

    
    
    public String getRheaId() {
        return rheaId;
    }

    public void setRheaId(String rheaId) {
        this.rheaId = rheaId;
    }

    public String getKeggId() {
        return keggId;
    }

    public void setKeggId(String keggId) {
        this.keggId = keggId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.rheaId);
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
        final Rhea2kegg other = (Rhea2kegg) obj;
        return Objects.equals(this.rheaId, other.rheaId);
    }

    @Override
    public String toString() {
        return "Rhea2kegg{" + "rheaId=" + rheaId + ", keggId=" + keggId + '}';
    }

    @Override
    public int compareTo(Rhea2kegg r) {
        return r.getRheaId().compareToIgnoreCase(this.getRheaId());
    }

}
