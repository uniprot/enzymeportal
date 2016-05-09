/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.dummy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import uk.ac.ebi.ep.data.search.model.EcNumber;
import uk.ac.ebi.ep.data.search.model.Species;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class EnzymePortalEnzyme extends EcNumber implements Comparable<EnzymePortalEnzyme>, Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @NotNull
    private Long internalId;
    @Size(max = 15)
    private String ecNumber;
    @Size(max = 300)
    private String enzymeName;
    //@Size(max = 4000)
    private List<String> catalyticActivity;
    private String enzymeFamily;
    private Set<Species> species;
    //@ManyToMany(mappedBy = "enzymes")
    private Set<DummyProtein> dummyProteinSet;

    public EnzymePortalEnzyme() {
    }

    public EnzymePortalEnzyme(Long internalId, String ecNumber, String enzymeName) {
        this.internalId = internalId;
        this.ecNumber = ecNumber;
        this.enzymeName = enzymeName;
        
    }

    public Long getInternalId() {
        return internalId;
    }

    public void setInternalId(Long internalId) {
        this.internalId = internalId;
    }

    public String getEcNumber() {
        return ecNumber;
    }

    public void setEcNumber(String ecNumber) {
        this.ecNumber = ecNumber;
    }

    public String getEnzymeName() {
        return enzymeName;
    }

    public void setEnzymeName(String enzymeName) {
        this.enzymeName = enzymeName;
    }

    public List<String> getCatalyticActivity() {
        if(catalyticActivity == null){
            catalyticActivity = new ArrayList<>();
        }
        return catalyticActivity;
    }

    public void setCatalyticActivity(List<String> catalyticActivity) {
        this.catalyticActivity = catalyticActivity;
    }

    public String getEnzymeFamily() {
       
        return getFamily();
    }

    public void setEnzymeFamily(String enzymeFamily) {
        this.enzymeFamily = enzymeFamily;
    }

    public Set<Species> getSpecies() {
        if (species == null) {
            species = new HashSet<>();
        }
        return species;
    }

    public void setSpecies(Set<Species> species) {
        this.species = species;
    }

    public Set<DummyProtein> getDummyProteinSet() {
        return dummyProteinSet;
    }

    public void setDummyProteinSet(Set<DummyProtein> dummyProteinSet) {
        this.dummyProteinSet = dummyProteinSet;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.ecNumber);
        hash = 29 * hash + Objects.hashCode(this.enzymeName);
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
        final EnzymePortalEnzyme other = (EnzymePortalEnzyme) obj;
        if (!Objects.equals(this.ecNumber, other.ecNumber)) {
            return false;
        }
        return Objects.equals(this.enzymeName, other.enzymeName);
    }

    @Override
    public int compareTo(EnzymePortalEnzyme o) {

        return this.getEcNumber().compareTo(o.getEcNumber());
    }

    @Override
    public String toString() {
        return "EnzymePortalEnzyme{" + "internalId=" + internalId + ", ecNumber=" + ecNumber + ", enzymeName=" + enzymeName + ", catalyticActivity=" + catalyticActivity + ", enzymeFamily=" + getFamily() + '}';
    }

    @Override
    public String getFamily() {
        return this.computeFamily(this.getEcNumber());

    }

}
