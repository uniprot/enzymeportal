
package uk.ac.ebi.webservices.ebeye;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="arrayOfEntryValues" type="{http://webservice.ebinocle.ebi.ac.uk}ArrayOfEntryReferences"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "arrayOfEntryValues"
})
@XmlRootElement(name = "getReferencedEntriesSetResponse")
public class GetReferencedEntriesSetResponse {

    @XmlElement(required = true, nillable = true)
    protected ArrayOfEntryReferences arrayOfEntryValues;

    /**
     * Gets the value of the arrayOfEntryValues property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfEntryReferences }
     *     
     */
    public ArrayOfEntryReferences getArrayOfEntryValues() {
        return arrayOfEntryValues;
    }

    /**
     * Sets the value of the arrayOfEntryValues property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfEntryReferences }
     *     
     */
    public void setArrayOfEntryValues(ArrayOfEntryReferences value) {
        this.arrayOfEntryValues = value;
    }

}
