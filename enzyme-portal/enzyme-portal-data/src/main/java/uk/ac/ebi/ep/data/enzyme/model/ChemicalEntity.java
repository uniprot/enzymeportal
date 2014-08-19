

package uk.ac.ebi.ep.data.enzyme.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * <p>Java class for ChemicalEntity complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * 
 * 
 * 
 */

public class ChemicalEntity
    implements Serializable
{

    
    protected List<EnzymeReaction> reactions;
    protected CountableMolecules inhibitors;
    protected CountableMolecules activators;
    protected CountableMolecules cofactors;
    protected CountableMolecules drugs;
    protected CountableMolecules bioactiveLigands;
   
    protected List<String> provenance;
    protected int totalFound;

    /**
     * Gets the value of the reactions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reactions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReactions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EnzymeReaction }
     * 
     * 
     */
    public List<EnzymeReaction> getReactions() {
        if (reactions == null) {
            reactions = new ArrayList<EnzymeReaction>();
        }
        return this.reactions;
    }

    /**
     * Gets the value of the inhibitors property.
     * 
     * @return
     *     possible object is
     *     {@link CountableMolecules }
     *     
     */
    public CountableMolecules getInhibitors() {
        return inhibitors;
    }

    /**
     * Sets the value of the inhibitors property.
     * 
     * @param value
     *     allowed object is
     *     {@link CountableMolecules }
     *     
     */
    public void setInhibitors(CountableMolecules value) {
        this.inhibitors = value;
    }

    /**
     * Gets the value of the activators property.
     * 
     * @return
     *     possible object is
     *     {@link CountableMolecules }
     *     
     */
    public CountableMolecules getActivators() {
        return activators;
    }

    /**
     * Sets the value of the activators property.
     * 
     * @param value
     *     allowed object is
     *     {@link CountableMolecules }
     *     
     */
    public void setActivators(CountableMolecules value) {
        this.activators = value;
    }

    /**
     * Gets the value of the cofactors property.
     * 
     * @return
     *     possible object is
     *     {@link CountableMolecules }
     *     
     */
    public CountableMolecules getCofactors() {
        return cofactors;
    }

    /**
     * Sets the value of the cofactors property.
     * 
     * @param value
     *     allowed object is
     *     {@link CountableMolecules }
     *     
     */
    public void setCofactors(CountableMolecules value) {
        this.cofactors = value;
    }

    /**
     * Gets the value of the drugs property.
     * 
     * @return
     *     possible object is
     *     {@link CountableMolecules }
     *     
     */
    public CountableMolecules getDrugs() {
        return drugs;
    }

    /**
     * Sets the value of the drugs property.
     * 
     * @param value
     *     allowed object is
     *     {@link CountableMolecules }
     *     
     */
    public void setDrugs(CountableMolecules value) {
        this.drugs = value;
    }

    /**
     * Gets the value of the bioactiveLigands property.
     * 
     * @return
     *     possible object is
     *     {@link CountableMolecules }
     *     
     */
    public CountableMolecules getBioactiveLigands() {
        return bioactiveLigands;
    }

    /**
     * Sets the value of the bioactiveLigands property.
     * 
     * @param value
     *     allowed object is
     *     {@link CountableMolecules }
     *     
     */
    public void setBioactiveLigands(CountableMolecules value) {
        this.bioactiveLigands = value;
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

    /**
     * Gets the value of the totalFound property.
     * 
     */
    public int getTotalFound() {
        return totalFound;
    }

    /**
     * Sets the value of the totalFound property.
     * 
     */
    public void setTotalFound(int value) {
        this.totalFound = value;
    }

   
    public ChemicalEntity withReactions(EnzymeReaction... values) {
        if (values!= null) {
            for (EnzymeReaction value: values) {
                getReactions().add(value);
            }
        }
        return this;
    }

    public ChemicalEntity withReactions(Collection<EnzymeReaction> values) {
        if (values!= null) {
            getReactions().addAll(values);
        }
        return this;
    }

    public ChemicalEntity withInhibitors(CountableMolecules value) {
        setInhibitors(value);
        return this;
    }

    public ChemicalEntity withActivators(CountableMolecules value) {
        setActivators(value);
        return this;
    }

    public ChemicalEntity withCofactors(CountableMolecules value) {
        setCofactors(value);
        return this;
    }

    public ChemicalEntity withDrugs(CountableMolecules value) {
        setDrugs(value);
        return this;
    }

    public ChemicalEntity withBioactiveLigands(CountableMolecules value) {
        setBioactiveLigands(value);
        return this;
    }

    public ChemicalEntity withProvenance(String... values) {
        if (values!= null) {
            for (String value: values) {
                getProvenance().add(value);
            }
        }
        return this;
    }

    public ChemicalEntity withProvenance(Collection<String> values) {
        if (values!= null) {
            getProvenance().addAll(values);
        }
        return this;
    }

    public ChemicalEntity withTotalFound(int value) {
        setTotalFound(value);
        return this;
    }

    /**
     * Sets the value of the reactions property.
     * 
     * @param reactions
     *     allowed object is
     *     {@link EnzymeReaction }
     *     
     */
    public void setReactions(List<EnzymeReaction> reactions) {
        this.reactions = reactions;
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
