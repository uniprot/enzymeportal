

package uk.ac.ebi.ep.data.enzyme.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



/**
 * <p>Java class for EnzymeHierarchy complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * 
 * 
 */

public class EnzymeHierarchy
    implements Serializable
{

   
    protected List<EcClass> ecclass;

    /**
     * Gets the value of the ecclass property.
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EcClass }
     * 
     * 
     */
    public List<EcClass> getEcclass() {
        if (ecclass == null) {
            ecclass = new ArrayList<EcClass>();
        }
        return this.ecclass;
    }

    public EnzymeHierarchy withEcclass(EcClass... values) {
        if (values!= null) {
            for (EcClass value: values) {
                getEcclass().add(value);
            }
        }
        return this;
    }

    public EnzymeHierarchy withEcclass(Collection<EcClass> values) {
        if (values!= null) {
            getEcclass().addAll(values);
        }
        return this;
    }

    /**
     * Sets the value of the ecclass property.
     * 
     * @param ecclass
     *     allowed object is
     *     {@link EcClass }
     *     
     */
    public void setEcclass(List<EcClass> ecclass) {
        this.ecclass = ecclass;
    }

}
