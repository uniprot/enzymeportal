

package uk.ac.ebi.ep.data.enzyme.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



/**
 * <p>Java class for countableMolecules complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 

 * 
 * 
 */

public class CountableMolecules
    implements Serializable
{

    private static final long serialVersionUID = 1L;
    private List<Molecule> molecule;
   
    protected Integer totalFound;

    /**
     * Gets the value of the molecule property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the molecule property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMolecule().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Molecule }
     * 
     * 
     */
    public List<Molecule> getMolecule() {
        if (molecule == null) {
            molecule = new ArrayList<Molecule>();
        }
        return this.molecule;
    }

    /**
     * Gets the value of the totalFound property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTotalFound() {
        return totalFound;
    }

    /**
     * Sets the value of the totalFound property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTotalFound(Integer value) {
        this.totalFound = value;
    }

    

    public CountableMolecules withMolecule(Molecule... values) {
        if (values!= null) {
            for (Molecule value: values) {
                getMolecule().add(value);
            }
        }
        return this;
    }

    public CountableMolecules withMolecule(Collection<Molecule> values) {
        if (values!= null) {
            getMolecule().addAll(values);
        }
        return this;
    }

    public CountableMolecules withTotalFound(Integer value) {
        setTotalFound(value);
        return this;
    }

    /**
     * Sets the value of the molecule property.
     * 
     * @param molecule
     *     allowed object is
     *     {@link Molecule }
     *     
     */
    public void setMolecule(List<Molecule> molecule) {
        this.molecule = molecule;
    }

}
