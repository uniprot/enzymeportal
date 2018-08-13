package uk.ac.ebi.ep.parser.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Joseph
 */
public class Targets {

    private String accession;
    private List<String> chemblId;
    private String componentType ="PROTEIN";

    public Targets() {
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public List<String> getChemblId() {
        if (chemblId == null) {
            chemblId = new LinkedList<>();
        }
        return chemblId;
    }

    public void setChemblId(List<String> chemblId) {
        this.chemblId = chemblId;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.accession);
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
        final Targets other = (Targets) obj;
        return Objects.equals(this.accession, other.accession);
    }

    @Override
    public String toString() {
        return "Targets{" + "accession=" + accession + ", chemblId=" + chemblId.size() + ", componentType=" + componentType + '}';
    }
    
    

}
