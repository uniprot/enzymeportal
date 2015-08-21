

package uk.ac.ebi.ep.data.enzyme.model;

import java.io.Serializable;
import java.util.Collection;



/**
 * <p>Java class for Molecule complex type.
 * 
 * 
 * 
 */

public class Molecule
    extends Entity
    implements Serializable
{

  
    protected String formula;

    /**
     * Gets the value of the formula property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormula() {
        return formula;
    }

    /**
     * Sets the value of the formula property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormula(String value) {
        this.formula = value;
    }

   

    public Molecule withFormula(String value) {
        setFormula(value);
        return this;
    }

    @Override
    public Molecule withId(String value) {
        setId(value);
        return this;
    }

    @Override
    public Molecule withName(String value) {
        setName(value);
        return this;
    }

    @Override
    public Molecule withDescription(String value) {
        setDescription(value);
        return this;
    }

    @Override
    public Molecule withUrl(Object value) {
        setUrl(value);
        return this;
    }

    @Override
    public Molecule withXrefs(Object... values) {
        if (values!= null) {
            for (Object value: values) {
                getXrefs().add(value);
            }
        }
        return this;
    }

    @Override
    public Molecule withXrefs(Collection<Object> values) {
        if (values!= null) {
            getXrefs().addAll(values);
        }
        return this;
    }

    @Override
    public Molecule withEvidence(String... values) {
        if (values!= null) {
            for (String value: values) {
                getEvidence().add(value);
            }
        }
        return this;
    }

    @Override
    public Molecule withEvidence(Collection<String> values) {
        if (values!= null) {
            getEvidence().addAll(values);
        }
        return this;
    }
    
}
