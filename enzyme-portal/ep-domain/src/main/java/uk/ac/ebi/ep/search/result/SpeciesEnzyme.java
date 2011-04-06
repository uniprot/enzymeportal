//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.04.05 at 05:35:53 PM BST 
//


package uk.ac.ebi.ep.search.result;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SpeciesEnzyme complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SpeciesEnzyme">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="species" type="{http://ebi.ac.uk/enzymeportal/result}Species"/>
 *         &lt;element name="EnzymeSummaryCollection" type="{http://ebi.ac.uk/enzymeportal/result}EnzymeSummaryCollection"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SpeciesEnzyme", propOrder = {
    "species",
    "enzymeSummaryCollection"
})
public class SpeciesEnzyme {

    @XmlElement(required = true)
    protected Species species;
    @XmlElement(name = "EnzymeSummaryCollection", required = true)
    protected EnzymeSummaryCollection enzymeSummaryCollection;

    /**
     * Gets the value of the species property.
     * 
     * @return
     *     possible object is
     *     {@link Species }
     *     
     */
    public Species getSpecies() {
        return species;
    }

    /**
     * Sets the value of the species property.
     * 
     * @param value
     *     allowed object is
     *     {@link Species }
     *     
     */
    public void setSpecies(Species value) {
        this.species = value;
    }

    /**
     * Gets the value of the enzymeSummaryCollection property.
     * 
     * @return
     *     possible object is
     *     {@link EnzymeSummaryCollection }
     *     
     */
    public EnzymeSummaryCollection getEnzymeSummaryCollection() {
        return enzymeSummaryCollection;
    }

    /**
     * Sets the value of the enzymeSummaryCollection property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnzymeSummaryCollection }
     *     
     */
    public void setEnzymeSummaryCollection(EnzymeSummaryCollection value) {
        this.enzymeSummaryCollection = value;
    }

}
