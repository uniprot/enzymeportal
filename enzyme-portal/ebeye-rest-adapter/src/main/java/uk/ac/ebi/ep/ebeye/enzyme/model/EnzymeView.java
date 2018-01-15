package uk.ac.ebi.ep.ebeye.enzyme.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import uk.ac.ebi.ep.ebeye.model.proteinGroup.ProteinGroupEntry;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class EnzymeView {

    private String enzymeName;
    private long numEnzymeHits;
    private String enzymeFamily;
    private String ec;
    private List<String> catalyticActivities;
    //@Deprecated
    //private List<String> species;
    //private List<Protein> proteins;
    private List<ProteinGroupEntry> proteinGroupEntry;
    private int numProteins;
    private Set<String> intenzCofactors;
    private Set<String> altNames;

    public String getEnzymeName() {
        return enzymeName;
    }

    public void setEnzymeName(String enzymeName) {
        this.enzymeName = enzymeName;
    }

    public long getNumEnzymeHits() {
        return numEnzymeHits;
    }

    public void setNumEnzymeHits(long numEnzymeHits) {
        this.numEnzymeHits = numEnzymeHits;
    }

    public String getEnzymeFamily() {
        return enzymeFamily;
    }

    public void setEnzymeFamily(String enzymeFamily) {
        this.enzymeFamily = enzymeFamily;
    }

    public String getEc() {
        return ec;
    }

    public void setEc(String ec) {
        this.ec = ec;
    }

    public List<String> getCatalyticActivities() {
        if (catalyticActivities == null) {
            catalyticActivities = new ArrayList<>();
        }

        return catalyticActivities;
    }

    public void setCatalyticActivities(List<String> catalyticActivities) {
        this.catalyticActivities = catalyticActivities;
    }

//    @Deprecated
//    public List<String> getSpecies() {
//        if (species == null) {
//            species = new ArrayList<>();
//        }
//
//        return species;
//    }
//
//    @Deprecated
//    public void setSpecies(List<String> species) {
//        this.species = species;
//    }

//    public List<Protein> getProteins() {
//        if (proteins == null) {
//            proteins = new ArrayList<>();
//        }
//        return proteins;
//    }
//
//    public void setProteins(List<Protein> proteins) {
//        this.proteins = proteins;
//    }

    public List<ProteinGroupEntry> getProteinGroupEntry() {
        if (proteinGroupEntry == null) {
            proteinGroupEntry = new ArrayList<>();
        }
        return proteinGroupEntry;
    }

    public void setProteinGroupEntry(List<ProteinGroupEntry> proteinGroupEntry) {
        this.proteinGroupEntry = proteinGroupEntry;
    }

    public int getNumProteins() {
        return numProteins;
    }

    public void setNumProteins(int numProteins) {
        this.numProteins = numProteins;
    }

    public Set<String> getIntenzCofactors() {
        return intenzCofactors;
    }

    public void setIntenzCofactors(Set<String> intenzCofactors) {
        this.intenzCofactors = intenzCofactors;
    }

    public Set<String> getAltNames() {
        return altNames;
    }

    public void setAltNames(Set<String> altNames) {
        this.altNames = altNames;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.enzymeName);
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
        final EnzymeView other = (EnzymeView) obj;
        return Objects.equals(this.enzymeName, other.enzymeName);
    }

}
