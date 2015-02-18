

package uk.ac.ebi.ep.data.enzyme.model;

import java.io.Serializable;
import java.util.Objects;



/**
 * <p>Java class for Sequence complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * 
 * 
 */

public class Sequence
    implements Serializable
{

    protected String sequence;
    protected String weight;
   
    protected String sequenceurl;
    protected Integer length;

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
     * Gets the value of the weight property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWeight() {
        return weight;
    }

    /**
     * Sets the value of the weight property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWeight(String value) {
        this.weight = value;
    }

    /**
     * Gets the value of the sequenceurl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSequenceurl() {
        return sequenceurl;
    }

    /**
     * Sets the value of the sequenceurl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSequenceurl(String value) {
        this.sequenceurl = value;
    }

    /**
     * Gets the value of the length property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getLength() {
        return length;
    }

    /**
     * Sets the value of the length property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLength(Integer value) {
        this.length = value;
    }

    

    public Sequence withSequence(String value) {
        setSequence(value);
        return this;
    }

    public Sequence withWeight(String value) {
        setWeight(value);
        return this;
    }

    public Sequence withSequenceurl(String value) {
        setSequenceurl(value);
        return this;
    }

    public Sequence withLength(Integer value) {
        setLength(value);
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.sequence);
        hash = 29 * hash + Objects.hashCode(this.weight);
        hash = 29 * hash + Objects.hashCode(this.sequenceurl);
        hash = 29 * hash + Objects.hashCode(this.length);
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
        final Sequence other = (Sequence) obj;
        if (!Objects.equals(this.sequence, other.sequence)) {
            return false;
        }
        if (!Objects.equals(this.weight, other.weight)) {
            return false;
        }
        if (!Objects.equals(this.sequenceurl, other.sequenceurl)) {
            return false;
        }
        if (!Objects.equals(this.length, other.length)) {
            return false;
        }
        return true;
    }
    
    

}
