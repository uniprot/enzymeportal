/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.dummy;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Id;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class DummyProtein implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String accession;
    private String proteinName;
    private String scientificName;
    private String commonName;
    //@ManyToMany
    private Set<EnzymePortalEnzyme> enzymePortalEnzymeSet;
    private Set<Species> speciesSet;

    public DummyProtein() {
    }

    public DummyProtein(String accession) {
        this.accession = accession;
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getProteinName() {
        return proteinName;
    }

    public void setProteinName(String proteinName) {
        this.proteinName = proteinName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public Set<EnzymePortalEnzyme> getEnzymePortalEnzymeSet() {
        return enzymePortalEnzymeSet;
    }

    public void setEnzymePortalEnzymeSet(Set<EnzymePortalEnzyme> enzymePortalEnzymeSet) {
        this.enzymePortalEnzymeSet = enzymePortalEnzymeSet;
    }

    public Set<Species> getSpeciesSet() {
        if (speciesSet == null) {
            speciesSet = new HashSet<>();
        }
        return speciesSet;
    }

    public void setSpeciesSet(Set<Species> speciesSet) {
        this.speciesSet = speciesSet;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + Objects.hashCode(this.accession);
        hash = 19 * hash + Objects.hashCode(this.proteinName);
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
        final DummyProtein other = (DummyProtein) obj;
        if (!Objects.equals(this.accession, other.accession)) {
            return false;
        }
        return Objects.equals(this.proteinName, other.proteinName);
    }

    @Override
    public String toString() {
        return "DummyProtein{" + "speciesSet=" + speciesSet + '}';
    }
    
    

}
