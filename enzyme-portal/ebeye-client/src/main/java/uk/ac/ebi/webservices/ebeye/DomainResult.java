
package uk.ac.ebi.webservices.ebeye;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DomainResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DomainResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="domainId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="qualified"/>
 *         &lt;element name="numberOfResults" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0" form="qualified"/>
 *         &lt;element name="subDomainsResults" type="{http://webservice.ebinocle.ebi.ac.uk}ArrayOfDomainResult" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DomainResult", namespace = "http://webservice.ebinocle.ebi.ac.uk", propOrder = {
    "domainId",
    "numberOfResults",
    "subDomainsResults"
})
public class DomainResult {

    @XmlElementRef(name = "domainId", namespace = "http://webservice.ebinocle.ebi.ac.uk", type = JAXBElement.class)
    protected JAXBElement<String> domainId;
    protected Integer numberOfResults;
    @XmlElementRef(name = "subDomainsResults", namespace = "http://webservice.ebinocle.ebi.ac.uk", type = JAXBElement.class)
    protected JAXBElement<ArrayOfDomainResult> subDomainsResults;

    /**
     * Gets the value of the domainId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDomainId() {
        return domainId;
    }

    /**
     * Sets the value of the domainId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDomainId(JAXBElement<String> value) {
        this.domainId = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the numberOfResults property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumberOfResults() {
        return numberOfResults;
    }

    /**
     * Sets the value of the numberOfResults property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumberOfResults(Integer value) {
        this.numberOfResults = value;
    }

    /**
     * Gets the value of the subDomainsResults property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfDomainResult }{@code >}
     *     
     */
    public JAXBElement<ArrayOfDomainResult> getSubDomainsResults() {
        return subDomainsResults;
    }

    /**
     * Sets the value of the subDomainsResults property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfDomainResult }{@code >}
     *     
     */
    public void setSubDomainsResults(JAXBElement<ArrayOfDomainResult> value) {
        this.subDomainsResults = ((JAXBElement<ArrayOfDomainResult> ) value);
    }

}
