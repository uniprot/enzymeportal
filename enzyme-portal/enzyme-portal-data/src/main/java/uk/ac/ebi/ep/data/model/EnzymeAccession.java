/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.data.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import uk.ac.ebi.ep.data.domain.EnzymePortalCompound;
import uk.ac.ebi.ep.data.domain.EnzymePortalDisease;
import uk.ac.ebi.ep.data.domain.EnzymePortalSummary;

/**
 *
 * @author joseph
 */
public class EnzymeAccession extends EnzymePortalSummary {
   
    
  
    protected List<String> uniprotaccessions;
   
    protected Species species;
    protected List<String> pdbeaccession;
    protected List<EnzymePortalCompound> compounds;
    protected List<EnzymePortalDisease> diseases;
  
    protected Object scoring;

    /**
     * Gets the value of the uniprotaccessions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the uniprotaccessions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUniprotaccessions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     * @return 
     */
    public List<String> getUniprotaccessions() {
        if (uniprotaccessions == null) {
            uniprotaccessions = new ArrayList<>();
        }
        return this.uniprotaccessions;
    }

    /**
     * Gets the value of the species property.
     * 
     * @return
     *     possible object is
     *     {@link Species }
     *     
     */
    public Species getSpecies() {
        return species;
    }

    /**
     * Sets the value of the species property.
     * 
     * @param value
     *     allowed object is
     *     {@link Species }
     *     
     */
    public void setSpecies(Species value) {
        this.species = value;
    }

    /**
     * Gets the value of the pdbeaccession property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pdbeaccession property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPdbeaccession().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getPdbeaccession() {
        if (pdbeaccession == null) {
            pdbeaccession = new ArrayList<String>();
        }
        return this.pdbeaccession;
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
     */
    public List<EnzymePortalCompound> getCompounds() {
        if (compounds == null) {
            compounds = new ArrayList<EnzymePortalCompound>();
        }
        return this.compounds;
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
     */
    public List<EnzymePortalDisease> getDiseases() {
        if (diseases == null) {
            diseases = new ArrayList<EnzymePortalDisease>();
        }
        return this.diseases;
    }

    /**
     * Gets the value of the scoring property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getScoring() {
        return scoring;
    }

    /**
     * Sets the value of the scoring property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setScoring(Object value) {
        this.scoring = value;
    }



    public EnzymeAccession withUniprotaccessions(String... values) {
        if (values!= null) {
            for (String value: values) {
                getUniprotaccessions().add(value);
            }
        }
        return this;
    }

    public EnzymeAccession withUniprotaccessions(Collection<String> values) {
        if (values!= null) {
            getUniprotaccessions().addAll(values);
        }
        return this;
    }

    public EnzymeAccession withSpecies(Species value) {
        setSpecies(value);
        return this;
    }

    public EnzymeAccession withPdbeaccession(String... values) {
        if (values!= null) {
            for (String value: values) {
                getPdbeaccession().add(value);
            }
        }
        return this;
    }

    public EnzymeAccession withPdbeaccession(Collection<String> values) {
        if (values!= null) {
            getPdbeaccession().addAll(values);
        }
        return this;
    }

    public EnzymeAccession withCompounds(EnzymePortalCompound... values) {
        if (values!= null) {
            getCompounds().addAll(Arrays.asList(values));
        }
        return this;
    }

    public EnzymeAccession withCompounds(Collection<EnzymePortalCompound> values) {
        if (values!= null) {
            getCompounds().addAll(values);
        }
        return this;
    }

    public EnzymeAccession withDiseases(EnzymePortalDisease... values) {
        if (values!= null) {
            getDiseases().addAll(Arrays.asList(values));
        }
        return this;
    }

    public EnzymeAccession withDiseases(Collection<EnzymePortalDisease> values) {
        if (values!= null) {
            getDiseases().addAll(values);
        }
        return this;
    }

    public EnzymeAccession withScoring(Object value) {
        setScoring(value);
        return this;
    }

    /**
     * Sets the value of the uniprotaccessions property.
     * 
     * @param uniprotaccessions
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUniprotaccessions(List<String> uniprotaccessions) {
        this.uniprotaccessions = uniprotaccessions;
    }

    /**
     * Sets the value of the pdbeaccession property.
     * 
     * @param pdbeaccession
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPdbeaccession(List<String> pdbeaccession) {
        this.pdbeaccession = pdbeaccession;
    }

    /**
     * Sets the value of the compounds property.
     * 
     * @param compounds
     *     allowed object is
     *     {@link Compound }
     *     
     */
    public void setCompounds(List<EnzymePortalCompound> compounds) {
        this.compounds = compounds;
    }

    /**
     * Sets the value of the diseases property.
     * 
     * @param diseases
     *     allowed object is
     *     {@link EnzymePortalDisease }
     *     
     */
    public void setDiseases(List<EnzymePortalDisease> diseases) {
        this.diseases = diseases;
    }

}
