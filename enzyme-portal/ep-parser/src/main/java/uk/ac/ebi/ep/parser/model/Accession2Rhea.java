
package uk.ac.ebi.ep.parser.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Joseph
 */
public class Accession2Rhea {

    private String accession;
    private Set<Rhea2kegg> rhea;

    public Accession2Rhea() {
         rhea = new HashSet<>();
    }
    
    

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public Set<Rhea2kegg> getRhea() {
        if (rhea == null) {
            rhea = new HashSet<>();
        }
        return rhea;

    }

    public void setRhea(Set<Rhea2kegg> rhea) {
        this.rhea = rhea;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.accession);
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
        final Accession2Rhea other = (Accession2Rhea) obj;
        return Objects.equals(this.accession, other.accession);
    }
    
    

    @Override
    public String toString() {
        return "Accession2Rhea{" + "accession=" + accession + ", rhea=" + getRhea() + '}';
    }

}
