
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
 *         &lt;element name="arrayOfDomainNames" type="{http://www.ebi.ac.uk/EBISearchService}ArrayOfString"/>
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
    "arrayOfDomainNames"
})
@XmlRootElement(name = "getDomainsReferencedInEntryResponse")
public class GetDomainsReferencedInEntryResponse {

    @XmlElement(required = true, nillable = true)
    protected ArrayOfString arrayOfDomainNames;

    /**
     * Gets the value of the arrayOfDomainNames property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getArrayOfDomainNames() {
        return arrayOfDomainNames;
    }

    /**
     * Sets the value of the arrayOfDomainNames property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setArrayOfDomainNames(ArrayOfString value) {
        this.arrayOfDomainNames = value;
    }

}
