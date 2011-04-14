
package uk.ac.ebi.webservices.ebeye;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DomainDescription complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DomainDescription">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="qualified"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="qualified"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="qualified"/>
 *         &lt;element name="properties" type="{http://www.ebi.ac.uk/EBISearchService}anyType2anyType2anyTypeMapMap" minOccurs="0" form="qualified"/>
 *         &lt;element name="subDomains" type="{http://webservice.ebinocle.ebi.ac.uk}ArrayOfDomainDescription" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DomainDescription", namespace = "http://webservice.ebinocle.ebi.ac.uk", propOrder = {
    "description",
    "id",
    "name",
    "properties",
    "subDomains"
})
public class DomainDescription {

    @XmlElementRef(name = "description", namespace = "http://webservice.ebinocle.ebi.ac.uk", type = JAXBElement.class)
    protected JAXBElement<String> description;
    @XmlElementRef(name = "id", namespace = "http://webservice.ebinocle.ebi.ac.uk", type = JAXBElement.class)
    protected JAXBElement<String> id;
    @XmlElementRef(name = "name", namespace = "http://webservice.ebinocle.ebi.ac.uk", type = JAXBElement.class)
    protected JAXBElement<String> name;
    @XmlElementRef(name = "properties", namespace = "http://webservice.ebinocle.ebi.ac.uk", type = JAXBElement.class)
    protected JAXBElement<AnyType2AnyType2AnyTypeMapMap> properties;
    @XmlElementRef(name = "subDomains", namespace = "http://webservice.ebinocle.ebi.ac.uk", type = JAXBElement.class)
    protected JAXBElement<ArrayOfDomainDescription> subDomains;

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDescription(JAXBElement<String> value) {
        this.description = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setId(JAXBElement<String> value) {
        this.id = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setName(JAXBElement<String> value) {
        this.name = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the properties property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link AnyType2AnyType2AnyTypeMapMap }{@code >}
     *     
     */
    public JAXBElement<AnyType2AnyType2AnyTypeMapMap> getProperties() {
        return properties;
    }

    /**
     * Sets the value of the properties property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link AnyType2AnyType2AnyTypeMapMap }{@code >}
     *     
     */
    public void setProperties(JAXBElement<AnyType2AnyType2AnyTypeMapMap> value) {
        this.properties = ((JAXBElement<AnyType2AnyType2AnyTypeMapMap> ) value);
    }

    /**
     * Gets the value of the subDomains property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfDomainDescription }{@code >}
     *     
     */
    public JAXBElement<ArrayOfDomainDescription> getSubDomains() {
        return subDomains;
    }

    /**
     * Sets the value of the subDomains property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfDomainDescription }{@code >}
     *     
     */
    public void setSubDomains(JAXBElement<ArrayOfDomainDescription> value) {
        this.subDomains = ((JAXBElement<ArrayOfDomainDescription> ) value);
    }

}
