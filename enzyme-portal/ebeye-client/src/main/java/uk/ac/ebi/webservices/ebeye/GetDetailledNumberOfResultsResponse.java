
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
 *         &lt;element name="detailledNumberOfResults" type="{http://webservice.ebinocle.ebi.ac.uk}DomainResult"/>
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
    "detailledNumberOfResults"
})
@XmlRootElement(name = "getDetailledNumberOfResultsResponse")
public class GetDetailledNumberOfResultsResponse {

    @XmlElement(required = true, nillable = true)
    protected DomainResult detailledNumberOfResults;

    /**
     * Gets the value of the detailledNumberOfResults property.
     * 
     * @return
     *     possible object is
     *     {@link DomainResult }
     *     
     */
    public DomainResult getDetailledNumberOfResults() {
        return detailledNumberOfResults;
    }

    /**
     * Sets the value of the detailledNumberOfResults property.
     * 
     * @param value
     *     allowed object is
     *     {@link DomainResult }
     *     
     */
    public void setDetailledNumberOfResults(DomainResult value) {
        this.detailledNumberOfResults = value;
    }

}
