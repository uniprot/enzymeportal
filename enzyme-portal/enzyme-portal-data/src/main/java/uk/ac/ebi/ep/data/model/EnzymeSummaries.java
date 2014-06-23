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


/**
 *
 * @author joseph
 */
public class EnzymeSummaries  extends EnzymeAccession {
    
    protected List<String> ec;
  
    protected String name;
    protected String function;
    protected List<String> synonym;
  
    protected String uniprotid;
    protected List<EnzymeAccession> relatedspecies;

    /**
     * Gets the value of the ec property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ec property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEc().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getEc() {
        if (ec == null) {
            ec = new ArrayList<String>();
        }
        return this.ec;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the function property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFunction() {
        return function;
    }

    /**
     * Sets the value of the function property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFunction(String value) {
        this.function = value;
    }

    /**
     * Gets the value of the synonym property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the synonym property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSynonym().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getSynonym() {
        if (synonym == null) {
            synonym = new ArrayList<String>();
        }
        return this.synonym;
    }

    /**
     * Gets the value of the uniprotid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUniprotid() {
        return uniprotid;
    }

    /**
     * Sets the value of the uniprotid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUniprotid(String value) {
        this.uniprotid = value;
    }

    /**
     * Gets the value of the relatedspecies property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the relatedspecies property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRelatedspecies().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EnzymeAccession }
     * 
     * 
     * @return 
     */
    public List<EnzymeAccession> getRelatedspecies() {
        if (relatedspecies == null) {
            relatedspecies = new ArrayList<>();
        }
        return this.relatedspecies;
    }




    public EnzymeSummaries withEc(String... values) {
        if (values!= null) {
            getEc().addAll(Arrays.asList(values));
        }
        return this;
    }

    public EnzymeSummaries withEc(Collection<String> values) {
        if (values!= null) {
            getEc().addAll(values);
        }
        return this;
    }

    public EnzymeSummaries withName(String value) {
        setName(value);
        return this;
    }

    public EnzymeSummaries withFunction(String value) {
        setFunction(value);
        return this;
    }

    public EnzymeSummaries withSynonym(String... values) {
        if (values!= null) {
            for (String value: values) {
                getSynonym().add(value);
            }
        }
        return this;
    }

    public EnzymeSummaries withSynonym(Collection<String> values) {
        if (values!= null) {
            getSynonym().addAll(values);
        }
        return this;
    }

    public EnzymeSummaries withUniprotid(String value) {
        setUniprotid(value);
        return this;
    }

    public EnzymeSummaries withRelatedspecies(EnzymeAccession... values) {
        if (values!= null) {
            getRelatedspecies().addAll(Arrays.asList(values));
        }
        return this;
    }

    public EnzymeSummaries withRelatedspecies(Collection<EnzymeAccession> values) {
        if (values!= null) {
            getRelatedspecies().addAll(values);
        }
        return this;
    }

    @Override
    public EnzymeSummaries withUniprotaccessions(String... values) {
        if (values!= null) {
            getUniprotaccessions().addAll(Arrays.asList(values));
        }
        return this;
    }

    @Override
    public EnzymeSummaries withUniprotaccessions(Collection<String> values) {
        if (values!= null) {
            getUniprotaccessions().addAll(values);
        }
        return this;
    }

    @Override
    public EnzymeSummaries withSpecies(Species value) {
        setSpecies(value);
        return this;
    }

    @Override
    public EnzymeSummaries withPdbeaccession(String... values) {
        if (values!= null) {
            getPdbeaccession().addAll(Arrays.asList(values));
        }
        return this;
    }

    @Override
    public EnzymeSummaries withPdbeaccession(Collection<String> values) {
        if (values!= null) {
            getPdbeaccession().addAll(values);
        }
        return this;
    }

    @Override
    public EnzymeSummaries withCompounds(EnzymePortalCompound... values) {
        if (values!= null) {
            getCompounds().addAll(Arrays.asList(values));
        }
        return this;
    }

    @Override
    public EnzymeSummaries withCompounds(Collection<EnzymePortalCompound> values) {
        if (values!= null) {
            getCompounds().addAll(values);
        }
        return this;
    }

    @Override
    public EnzymeSummaries withDiseases(EnzymePortalDisease... values) {
        if (values!= null) {
            getDiseases().addAll(Arrays.asList(values));
        }
        return this;
    }

    @Override
    public EnzymeSummaries withDiseases(Collection<EnzymePortalDisease> values) {
        if (values!= null) {
            getDiseases().addAll(values);
        }
        return this;
    }

    @Override
    public EnzymeSummaries withScoring(Object value) {
        setScoring(value);
        return this;
    }

    /**
     * Sets the value of the ec property.
     * 
     * @param ec
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEc(List<String> ec) {
        this.ec = ec;
    }

    /**
     * Sets the value of the synonym property.
     * 
     * @param synonym
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSynonym(List<String> synonym) {
        this.synonym = synonym;
    }

    /**
     * Sets the value of the relatedspecies property.
     * 
     * @param relatedspecies
     *     allowed object is
     *     {@link EnzymeAccession }
     *     
     */
    public void setRelatedspecies(List<EnzymeAccession> relatedspecies) {
        this.relatedspecies = relatedspecies;
    }

}
