
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
 *         &lt;element name="arrayOfFieldInformation" type="{http://webservice.ebinocle.ebi.ac.uk}ArrayOfFieldInfo"/>
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
    "arrayOfFieldInformation"
})
@XmlRootElement(name = "listFieldsInformationResponse")
public class ListFieldsInformationResponse {

    @XmlElement(required = true, nillable = true)
    protected ArrayOfFieldInfo arrayOfFieldInformation;

    /**
     * Gets the value of the arrayOfFieldInformation property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfFieldInfo }
     *     
     */
    public ArrayOfFieldInfo getArrayOfFieldInformation() {
        return arrayOfFieldInformation;
    }

    /**
     * Sets the value of the arrayOfFieldInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfFieldInfo }
     *     
     */
    public void setArrayOfFieldInformation(ArrayOfFieldInfo value) {
        this.arrayOfFieldInformation = value;
    }

}
