
package uk.ac.ebi.webservices.ebeye;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EntryReferences complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EntryReferences">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="entry" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="qualified"/>
 *         &lt;element name="references" type="{http://www.ebi.ac.uk/EBISearchService}ArrayOfArrayOfString" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EntryReferences", namespace = "http://webservice.ebinocle.ebi.ac.uk", propOrder = {
    "entry",
    "references"
})
public class EntryReferences {

    @XmlElementRef(name = "entry", namespace = "http://webservice.ebinocle.ebi.ac.uk", type = JAXBElement.class)
    protected JAXBElement<String> entry;
    @XmlElementRef(name = "references", namespace = "http://webservice.ebinocle.ebi.ac.uk", type = JAXBElement.class)
    protected JAXBElement<ArrayOfArrayOfString> references;

    /**
     * Gets the value of the entry property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getEntry() {
        return entry;
    }

    /**
     * Sets the value of the entry property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setEntry(JAXBElement<String> value) {
        this.entry = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the references property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfArrayOfString }{@code >}
     *     
     */
    public JAXBElement<ArrayOfArrayOfString> getReferences() {
        return references;
    }

    /**
     * Sets the value of the references property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfArrayOfString }{@code >}
     *     
     */
    public void setReferences(JAXBElement<ArrayOfArrayOfString> value) {
        this.references = ((JAXBElement<ArrayOfArrayOfString> ) value);
    }

}
