
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
 *         &lt;element name="entryUrlsValues" type="{http://www.ebi.ac.uk/EBISearchService}ArrayOfString"/>
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
    "entryUrlsValues"
})
@XmlRootElement(name = "getEntryFieldUrlsResponse")
public class GetEntryFieldUrlsResponse {

    @XmlElement(required = true, nillable = true)
    protected ArrayOfString entryUrlsValues;

    /**
     * Gets the value of the entryUrlsValues property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getEntryUrlsValues() {
        return entryUrlsValues;
    }

    /**
     * Sets the value of the entryUrlsValues property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setEntryUrlsValues(ArrayOfString value) {
        this.entryUrlsValues = value;
    }

}
