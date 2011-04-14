
package uk.ac.ebi.webservices.ebeye;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfDomainResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfDomainResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DomainResult" type="{http://webservice.ebinocle.ebi.ac.uk}DomainResult" maxOccurs="unbounded" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfDomainResult", namespace = "http://webservice.ebinocle.ebi.ac.uk", propOrder = {
    "domainResult"
})
public class ArrayOfDomainResult {

    @XmlElement(name = "DomainResult", nillable = true)
    protected List<DomainResult> domainResult;

    /**
     * Gets the value of the domainResult property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the domainResult property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDomainResult().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DomainResult }
     * 
     * 
     */
    public List<DomainResult> getDomainResult() {
        if (domainResult == null) {
            domainResult = new ArrayList<DomainResult>();
        }
        return this.domainResult;
    }

}
