/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.data.search.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import uk.ac.ebi.ep.data.domain.EnzymePortalDisease;

/**
 *
 * @author joseph
 */
public class SearchFilters implements Serializable{
    
    
    protected List<Disease> diseases;
    protected List<Compound> compounds;
    protected List<Species> species;
    protected List<EcNumber> ecNumbers;

    public List<EcNumber> getEcNumbers() {
        if(ecNumbers == null){
            ecNumbers = new LinkedList<>();
        }
        
        return ecNumbers;
    }

    public void setEcNumbers(List<EcNumber> ecNumbers) {
        this.ecNumbers = ecNumbers;
    }
    
    
    
    
    
    

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
    public List<Disease> getDiseases() {
        if (diseases == null) {
            diseases = new ArrayList<>();
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
    public List<Compound> getCompounds() {
        if (compounds == null) {
            compounds = new ArrayList<>();
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
    public List<Species> getSpecies() {
        if (species == null) {
            species = new ArrayList<>();
        }
        return this.species;
    }






    public SearchFilters withDiseases(Disease... values) {
        if (values!= null) {
            for (Disease value: values) {
                getDiseases().add(value);
            }
        }
        return this;
    }

    public SearchFilters withDiseases(Collection<Disease> values) {
        if (values!= null) {
            getDiseases().addAll(values);
        }
        return this;
    }

    public SearchFilters withCompounds(Compound... values) {
        if (values!= null) {
            getCompounds().addAll(Arrays.asList(values));
        }
        return this;
    }

    public SearchFilters withCompounds(Collection<Compound> values) {
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
    public void setDiseases(List<Disease> diseases) {
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
    public void setCompounds(List<Compound> compounds) {
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
    public void setSpecies(List<Species> species) {
        this.species = species;
    }
}
