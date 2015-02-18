

package uk.ac.ebi.ep.data.enzyme.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



/**
 * <p>Java class for Equation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 

 * 
 * 
 */

public class Equation
    implements Serializable
{

 
    protected List<Object> reactantlist;
  
    protected String direction;
   
    protected List<Object> productlist;

    /**
     * Gets the value of the reactantlist property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reactantlist property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReactantlist().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * 
     * 
     */
    public List<Object> getReactantlist() {
        if (reactantlist == null) {
            reactantlist = new ArrayList<Object>();
        }
        return this.reactantlist;
    }

    /**
     * Gets the value of the direction property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Sets the value of the direction property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDirection(String value) {
        this.direction = value;
    }

    /**
     * Gets the value of the productlist property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the productlist property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProductlist().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * 
     * 
     */
    public List<Object> getProductlist() {
        if (productlist == null) {
            productlist = new ArrayList<Object>();
        }
        return this.productlist;
    }

    
    public Equation withReactantlist(Object... values) {
        if (values!= null) {
            for (Object value: values) {
                getReactantlist().add(value);
            }
        }
        return this;
    }

    public Equation withReactantlist(Collection<Object> values) {
        if (values!= null) {
            getReactantlist().addAll(values);
        }
        return this;
    }

    public Equation withDirection(String value) {
        setDirection(value);
        return this;
    }

    public Equation withProductlist(Object... values) {
        if (values!= null) {
            for (Object value: values) {
                getProductlist().add(value);
            }
        }
        return this;
    }

    public Equation withProductlist(Collection<Object> values) {
        if (values!= null) {
            getProductlist().addAll(values);
        }
        return this;
    }

    /**
     * Sets the value of the reactantlist property.
     * 
     * @param reactantlist
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setReactantlist(List<Object> reactantlist) {
        this.reactantlist = reactantlist;
    }

    /**
     * Sets the value of the productlist property.
     * 
     * @param productlist
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setProductlist(List<Object> productlist) {
        this.productlist = productlist;
    }

}
