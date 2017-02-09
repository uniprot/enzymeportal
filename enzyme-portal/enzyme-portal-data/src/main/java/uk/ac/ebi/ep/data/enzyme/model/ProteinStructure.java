
package uk.ac.ebi.ep.data.enzyme.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



/**
 * <p>Java class for ProteinStructure complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 

 * 
 * 
 */

public class ProteinStructure
    extends Entity
    implements Serializable{
   

    protected Image image;
  
    transient List<DASSummary> summary;
    
    transient List<String> provenance;

    /**
     * Gets the value of the image property.
     * 
     * @return
     *     possible object is
     *     {@link Image }
     *     
     */
    public Image getImage() {
        return image;
    }

    /**
     * Sets the value of the image property.
     * 
     * @param value
     *     allowed object is
     *     {@link Image }
     *     
     */
    public void setImage(Image value) {
        this.image = value;
    }

    /**
     * Gets the value of the summary property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the summary property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSummary().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DASSummary }
     * 
     * 
     */
    public List<DASSummary> getSummary() {
        if (summary == null) {
            summary = new ArrayList<DASSummary>();
        }
        return this.summary;
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

    
    public ProteinStructure withImage(Image value) {
        setImage(value);
        return this;
    }

    public ProteinStructure withSummary(DASSummary... values) {
        if (values!= null) {
            for (DASSummary value: values) {
                getSummary().add(value);
            }
        }
        return this;
    }

    public ProteinStructure withSummary(Collection<DASSummary> values) {
        if (values!= null) {
            getSummary().addAll(values);
        }
        return this;
    }

    public ProteinStructure withProvenance(String... values) {
        if (values!= null) {
            for (String value: values) {
                getProvenance().add(value);
            }
        }
        return this;
    }

    public ProteinStructure withProvenance(Collection<String> values) {
        if (values!= null) {
            getProvenance().addAll(values);
        }
        return this;
    }

    @Override
    public ProteinStructure withId(String value) {
        setId(value);
        return this;
    }
    
    @Override
    public ProteinStructure withName(String value) {
        setName(value);
        return this;
    }

    @Override
    public ProteinStructure withDescription(String value) {
        setDescription(value);
        return this;
    }

    @Override
    public ProteinStructure withUrl(Object value) {
        setUrl(value);
        return this;
    }

    @Override
    public ProteinStructure withXrefs(Object... values) {
        if (values!= null) {
            for (Object value: values) {
                getXrefs().add(value);
            }
        }
        return this;
    }

    @Override
    public ProteinStructure withXrefs(Collection<Object> values) {
        if (values!= null) {
            getXrefs().addAll(values);
        }
        return this;
    }

    @Override
    public ProteinStructure withEvidence(String... values) {
        if (values!= null) {
            for (String value: values) {
                getEvidence().add(value);
            }
        }
        return this;
    }

    @Override
    public ProteinStructure withEvidence(Collection<String> values) {
        if (values!= null) {
            getEvidence().addAll(values);
        }
        return this;
    }

    /**
     * Sets the value of the summary property.
     * 
     * @param summary
     *     allowed object is
     *     {@link DASSummary }
     *     
     */
    public void setSummary(List<DASSummary> summary) {
        this.summary = summary;
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
