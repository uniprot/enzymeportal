/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.context.annotation.Import;
import uk.ac.ebi.ep.ebeye.config.EbeyeConfig;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Import({EbeyeConfig.class, })
public class EnzymeView {

    private String enzymeName;
    private int numEnzymeHits;
    private String enzymeFamily;
    private String ec;
    private List<String> catalyticActivities;
    private List<String> species;
    private List<Protein> protein;
    private List<String> proteins;
    private int numProteins;
    
    public String getEnzymeName() {
        return enzymeName;
    }

    public void setEnzymeName(String enzymeName) {
        this.enzymeName = enzymeName;
    }

    public int getNumEnzymeHits() {
        return numEnzymeHits;
    }

    public void setNumEnzymeHits(int numEnzymeHits) {
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
        return catalyticActivities;
    }

    public void setCatalyticActivities(List<String> catalyticActivities) {
        this.catalyticActivities = catalyticActivities;
    }

    public List<String> getSpecies() {
        return species;
    }

    public void setSpecies(List<String> species) {
        this.species = species;
    }

    public List<Protein> getProtein() {
        return protein;
    }

    public void setProtein(List<Protein> proteins) {
        this.protein = proteins;
    }
    
    

    public List<String> getProteins() {
        if(protein == null){
            protein = new ArrayList<>();
        }
        return proteins;
    }

    public void setProteins(List<String> proteins) {
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
