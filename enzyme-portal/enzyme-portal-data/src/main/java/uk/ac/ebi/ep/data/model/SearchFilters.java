/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.data.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import uk.ac.ebi.ep.data.domain.EnzymePortalCompound;
import uk.ac.ebi.ep.data.domain.EnzymePortalDisease;

/**
 *
 * @author joseph
 */
public class SearchFilters {
    
    
    protected Set<EnzymePortalDisease> diseases;
    protected Set<EnzymePortalCompound> compounds;
    protected Set<Species> species;

    /**
     * Gets the value of the diseases property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the diseases property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDiseases().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EnzymePortalDisease }
     * 
     * 
     * @return 
     */
    public Set<EnzymePortalDisease> getDiseases() {
        if (diseases == null) {
            diseases = new HashSet<>();
        }
        return this.diseases;
    }

    /**
     * Gets the value of the compounds property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the compounds property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCompounds().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Compound }
     * 
     * 
     * @return 
     */
    public Set<EnzymePortalCompound> getCompounds() {
        if (compounds == null) {
            compounds = new HashSet<>();
        }
        return this.compounds;
    }

    /**
     * Gets the value of the species property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the species property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSpecies().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Species }
     * 
     * 
     * @return 
     */
    public Set<Species> getSpecies() {
        if (species == null) {
            species = new HashSet<>();
        }
        return this.species;
    }






    public SearchFilters withDiseases(EnzymePortalDisease... values) {
        if (values!= null) {
            for (EnzymePortalDisease value: values) {
                getDiseases().add(value);
            }
        }
        return this;
    }

    public SearchFilters withDiseases(Collection<EnzymePortalDisease> values) {
        if (values!= null) {
            getDiseases().addAll(values);
        }
        return this;
    }

    public SearchFilters withCompounds(EnzymePortalCompound... values) {
        if (values!= null) {
            getCompounds().addAll(Arrays.asList(values));
        }
        return this;
    }

    public SearchFilters withCompounds(Collection<EnzymePortalCompound> values) {
        if (values!= null) {
            getCompounds().addAll(values);
        }
        return this;
    }

    public SearchFilters withSpecies(Species... values) {
        if (values!= null) {
            getSpecies().addAll(Arrays.asList(values));
        }
        return this;
    }

    public SearchFilters withSpecies(Collection<Species> values) {
        if (values!= null) {
            getSpecies().addAll(values);
        }
        return this;
    }

    /**
     * Sets the value of the diseases property.
     * 
     * @param diseases
     *     allowed object is
     *     {@link EnzymePortalDisease }
     *     
     */
    public void setDiseases(Set<EnzymePortalDisease> diseases) {
        this.diseases = diseases;
    }

    /**
     * Sets the value of the compounds property.
     * 
     * @param compounds
     *     allowed object is
     *     {@link Compound }
     *     
     */
    public void setCompounds(Set<EnzymePortalCompound> compounds) {
        this.compounds = compounds;
    }

    /**
     * Sets the value of the species property.
     * 
     * @param species
     *     allowed object is
     *     {@link Species }
     *     
     */
    public void setSpecies(Set<Species> species) {
        this.species = species;
    }
}
