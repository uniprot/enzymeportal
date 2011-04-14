
package uk.ac.ebi.webservices.ebeye;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfFieldInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfFieldInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FieldInfo" type="{http://webservice.ebinocle.ebi.ac.uk}FieldInfo" maxOccurs="unbounded" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfFieldInfo", namespace = "http://webservice.ebinocle.ebi.ac.uk", propOrder = {
    "fieldInfo"
})
public class ArrayOfFieldInfo {

    @XmlElement(name = "FieldInfo", nillable = true)
    protected List<FieldInfo> fieldInfo;

    /**
     * Gets the value of the fieldInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fieldInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFieldInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FieldInfo }
     * 
     * 
     */
    public List<FieldInfo> getFieldInfo() {
        if (fieldInfo == null) {
            fieldInfo = new ArrayList<FieldInfo>();
        }
        return this.fieldInfo;
    }

}
