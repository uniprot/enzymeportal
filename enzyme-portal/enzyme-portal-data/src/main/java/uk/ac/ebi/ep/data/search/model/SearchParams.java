/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.data.search.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author joseph
 */
public class SearchParams {
    
      
    protected String text;
    
    protected String sequence;
   
    protected String compound;
   
    protected String previoustext;
    protected int start;
    protected int size;
    protected List<String> compounds;
    protected List<String> diseases;
    protected List<String> species;
      
    protected SearchParams.SearchType type;   

    /**
     * Gets the value of the text property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setText(String value) {
        this.text = value;
    }

    /**
     * Gets the value of the sequence property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSequence() {
        return sequence;
    }

    /**
     * Sets the value of the sequence property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSequence(String value) {
        this.sequence = value;
    }

    /**
     * Gets the value of the compound property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompound() {
        return compound;
    }

    /**
     * Sets the value of the compound property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompound(String value) {
        this.compound = value;
    }

    /**
     * Gets the value of the previoustext property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrevioustext() {
        return previoustext;
    }

    /**
     * Sets the value of the previoustext property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrevioustext(String value) {
        this.previoustext = value;
    }

    /**
     * Gets the value of the start property.
     * 
     * @return 
     */
    public int getStart() {
        return start;
    }

    /**
     * Sets the value of the start property.
     * 
     * @param value
     */
    public void setStart(int value) {
        this.start = value;
    }

    /**
     * Gets the value of the size property.
     * 
     * @return 
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets the value of the size property.
     * 
     * @param value
     */
    public void setSize(int value) {
        this.size = value;
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
     * {@link String }
     * 
     * 
     * @return 
     */
    public List<String> getCompounds() {
        if (compounds == null) {
            compounds = new ArrayList<>();
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
     * {@link String }
     * 
     *
     * @return */
    public List<String> getDiseases() {
        if (diseases == null) {
            diseases = new ArrayList<>();
        }
        return this.diseases;
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
     * {@link String }
     * 
     *
     * @return */
    public List<String> getSpecies() {
        if (species == null) {
            species = new ArrayList<>();
        }
        return this.species;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link SearchParams.SearchType }
     *     
     */
    public SearchParams.SearchType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link SearchParams.SearchType }
     *     
     */
    public void setType(SearchParams.SearchType value) {
        this.type = value;
    }





    public SearchParams withText(String value) {
        setText(value);
        return this;
    }

    public SearchParams withSequence(String value) {
        setSequence(value);
        return this;
    }

    public SearchParams withCompound(String value) {
        setCompound(value);
        return this;
    }

    public SearchParams withPrevioustext(String value) {
        setPrevioustext(value);
        return this;
    }

    public SearchParams withStart(int value) {
        setStart(value);
        return this;
    }

    public SearchParams withSize(int value) {
        setSize(value);
        return this;
    }

    public SearchParams withCompounds(String... values) {
        if (values!= null) {
            getCompounds().addAll(Arrays.asList(values));
        }
        return this;
    }

    public SearchParams withCompounds(Collection<String> values) {
        if (values!= null) {
            getCompounds().addAll(values);
        }
        return this;
    }

    public SearchParams withDiseases(String... values) {
        if (values!= null) {
            getDiseases().addAll(Arrays.asList(values));
        }
        return this;
    }

    public SearchParams withDiseases(Collection<String> values) {
        if (values!= null) {
            getDiseases().addAll(values);
        }
        return this;
    }

    public SearchParams withSpecies(String... values) {
        if (values!= null) {
            getSpecies().addAll(Arrays.asList(values));
        }
        return this;
    }

    public SearchParams withSpecies(Collection<String> values) {
        if (values!= null) {
            getSpecies().addAll(values);
        }
        return this;
    }

    public SearchParams withType(SearchParams.SearchType value) {
        setType(value);
        return this;
    }

    /**
     * Sets the value of the compounds property.
     * 
     * @param compounds
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompounds(List<String> compounds) {
        this.compounds = compounds;
    }

    /**
     * Sets the value of the diseases property.
     * 
     * @param diseases
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDiseases(List<String> diseases) {
        this.diseases = diseases;
    }

    /**
     * Sets the value of the species property.
     * 
     * @param species
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpecies(List<String> species) {
        this.species = species;
    }


    /**
     * <p>Java class for null.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p>
     * <pre>
     * &lt;simpleType>
     *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *     &lt;enumeration value="KEYWORD"/>
     *     &lt;enumeration value="SEQUENCE"/>
     *     &lt;enumeration value="COMPOUND"/>
     *   &lt;/restriction>
     * &lt;/simpleType>
     * </pre>
     * 
     */
 
    public enum SearchType {

        KEYWORD,
        SEQUENCE,
        COMPOUND;

        public String value() {
            return name();
        }

        public static SearchParams.SearchType fromValue(String v) {
            return valueOf(v);
        }

    }
}
