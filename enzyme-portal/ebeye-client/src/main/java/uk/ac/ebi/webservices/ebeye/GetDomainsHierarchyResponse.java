
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
 *         &lt;element name="rootDomain" type="{http://webservice.ebinocle.ebi.ac.uk}DomainDescription"/>
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
    "rootDomain"
})
@XmlRootElement(name = "getDomainsHierarchyResponse")
public class GetDomainsHierarchyResponse {

    @XmlElement(required = true, nillable = true)
    protected DomainDescription rootDomain;

    /**
     * Gets the value of the rootDomain property.
     * 
     * @return
     *     possible object is
     *     {@link DomainDescription }
     *     
     */
    public DomainDescription getRootDomain() {
        return rootDomain;
    }

    /**
     * Sets the value of the rootDomain property.
     * 
     * @param value
     *     allowed object is
     *     {@link DomainDescription }
     *     
     */
    public void setRootDomain(DomainDescription value) {
        this.rootDomain = value;
    }

}
