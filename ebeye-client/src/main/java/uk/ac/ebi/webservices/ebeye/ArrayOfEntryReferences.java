
package uk.ac.ebi.webservices.ebeye;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfEntryReferences complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfEntryReferences">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EntryReferences" type="{http://webservice.ebinocle.ebi.ac.uk}EntryReferences" maxOccurs="unbounded" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfEntryReferences", namespace = "http://webservice.ebinocle.ebi.ac.uk", propOrder = {
    "entryReferences"
})
public class ArrayOfEntryReferences {

    @XmlElement(name = "EntryReferences", nillable = true)
    protected List<EntryReferences> entryReferences;

    /**
     * Gets the value of the entryReferences property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the entryReferences property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEntryReferences().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EntryReferences }
     * 
     * 
     */
    public List<EntryReferences> getEntryReferences() {
        if (entryReferences == null) {
            entryReferences = new ArrayList<EntryReferences>();
        }
        return this.entryReferences;
    }

}
