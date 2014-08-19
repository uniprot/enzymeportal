

package uk.ac.ebi.ep.data.enzyme.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;




/**
 * <p>Java class for Enzyme complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * 
 */

public class Enzyme
    implements Serializable
{

   
    protected List<EnzymeHierarchy> echierarchies;
    
    protected List<String> enzymetype;
   
    protected Sequence sequence;
   
    protected List<String> provenance;

    /**
     * Gets the value of the echierarchies property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the echierarchies property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEchierarchies().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EnzymeHierarchy }
     * 
     * 
     */
    public List<EnzymeHierarchy> getEchierarchies() {
        if (echierarchies == null) {
            echierarchies = new ArrayList<EnzymeHierarchy>();
        }
        return this.echierarchies;
    }

    /**
     * Gets the value of the enzymetype property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the enzymetype property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEnzymetype().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getEnzymetype() {
        if (enzymetype == null) {
            enzymetype = new ArrayList<String>();
        }
        return this.enzymetype;
    }

    /**
     * Gets the value of the sequence property.
     * 
     * @return
     *     possible object is
     *     {@link Sequence }
     *     
     */
    public Sequence getSequence() {
        return sequence;
    }

    /**
     * Sets the value of the sequence property.
     * 
     * @param value
     *     allowed object is
     *     {@link Sequence }
     *     
     */
    public void setSequence(Sequence value) {
        this.sequence = value;
    }

    /**
     * Gets the value of the provenance property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the provenance property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProvenance().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getProvenance() {
        if (provenance == null) {
            provenance = new ArrayList<String>();
        }
        return this.provenance;
    }

    

    public Enzyme withEchierarchies(EnzymeHierarchy... values) {
        if (values!= null) {
            for (EnzymeHierarchy value: values) {
                getEchierarchies().add(value);
            }
        }
        return this;
    }

    public Enzyme withEchierarchies(Collection<EnzymeHierarchy> values) {
        if (values!= null) {
            getEchierarchies().addAll(values);
        }
        return this;
    }

    public Enzyme withEnzymetype(String... values) {
        if (values!= null) {
            for (String value: values) {
                getEnzymetype().add(value);
            }
        }
        return this;
    }

    public Enzyme withEnzymetype(Collection<String> values) {
        if (values!= null) {
            getEnzymetype().addAll(values);
        }
        return this;
    }

    public Enzyme withSequence(Sequence value) {
        setSequence(value);
        return this;
    }

    public Enzyme withProvenance(String... values) {
        if (values!= null) {
            for (String value: values) {
                getProvenance().add(value);
            }
        }
        return this;
    }

    public Enzyme withProvenance(Collection<String> values) {
        if (values!= null) {
            getProvenance().addAll(values);
        }
        return this;
    }

    /**
     * Sets the value of the echierarchies property.
     * 
     * @param echierarchies
     *     allowed object is
     *     {@link EnzymeHierarchy }
     *     
     */
    public void setEchierarchies(List<EnzymeHierarchy> echierarchies) {
        this.echierarchies = echierarchies;
    }

    /**
     * Sets the value of the enzymetype property.
     * 
     * @param enzymetype
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnzymetype(List<String> enzymetype) {
        this.enzymetype = enzymetype;
    }

    /**
     * Sets the value of the provenance property.
     * 
     * @param provenance
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvenance(List<String> provenance) {
        this.provenance = provenance;
    }

}
