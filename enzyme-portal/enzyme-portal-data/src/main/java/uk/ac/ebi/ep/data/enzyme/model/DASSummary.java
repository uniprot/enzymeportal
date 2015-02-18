

package uk.ac.ebi.ep.data.enzyme.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



/**
 * <p>Java class for DASSummary complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * 
 * 
 */

public class DASSummary
    implements Serializable
{

   
    protected String label;
   
    protected List<String> note;

    /**
     * Gets the value of the label property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the value of the label property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabel(String value) {
        this.label = value;
    }

    /**
     * Gets the value of the note property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the note property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNote().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getNote() {
        if (note == null) {
            note = new ArrayList<String>();
        }
        return this.note;
    }

    
    public DASSummary withLabel(String value) {
        setLabel(value);
        return this;
    }

    public DASSummary withNote(String... values) {
        if (values!= null) {
            for (String value: values) {
                getNote().add(value);
            }
        }
        return this;
    }

    public DASSummary withNote(Collection<String> values) {
        if (values!= null) {
            getNote().addAll(values);
        }
        return this;
    }

    /**
     * Sets the value of the note property.
     * 
     * @param note
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNote(List<String> note) {
        this.note = note;
    }

}
