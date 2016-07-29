/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye.enzyme.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import uk.ac.ebi.ep.ebeye.protein.model.Protein;

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
    private List<String> species;
    private List<Protein> proteins;
    //private List<String> proteins;
    private int numProteins;

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

    public List<String> getSpecies() {
        if (species == null) {
            species = new ArrayList<>();
        }

        return species;
    }

    public void setSpecies(List<String> species) {
        this.species = species;
    }

    public List<Protein> getProteins() {
        if (proteins == null) {
            proteins = new ArrayList<>();
        }
        return proteins;
    }

    public void setProteins(List<Protein> proteins) {
        this.proteins = proteins;
    }

    public int getNumProteins() {
        return numProteins;
    }

    public void setNumProteins(int numProteins) {
        this.numProteins = numProteins;
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
