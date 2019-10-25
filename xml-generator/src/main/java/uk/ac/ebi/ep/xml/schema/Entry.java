
package uk.ac.ebi.ep.xml.schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author joseph
 */
@XmlRootElement
//@XmlType(propOrder = {"id", "acc", "name", "description", "additionalFields", "crossReferences"})
@XmlType(propOrder = {"id", "name", "description", "additionalFields", "crossReferences"})
public class Entry {

    private String id;
   // private String acc;
    private String name;
    private String description;

    private AdditionalFields additionalFields;
    private CrossReferences crossReferences;

//    public String getAcc() {
//        return acc;
//    }
//
//    @XmlAttribute(name = "acc")
//    public void setAcc(String acc) {
//        this.acc = acc;
//    }

    public String getId() {
        return id;
    }

    @XmlAttribute(name = "id")
    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    @XmlElement(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    @XmlElement(name = "description")
    public void setDescription(String description) {
        this.description = description;
    }

    public AdditionalFields getAdditionalFields() {
        return additionalFields;
    }

    @XmlElement(name = "additional_fields")
    public void setAdditionalFields(AdditionalFields additionalFields) {
        this.additionalFields = additionalFields;
    }

    public CrossReferences getCrossReferences() {
        return crossReferences;
    }

    @XmlElement(name = "cross_references")
    public void setCrossReferences(CrossReferences crossReferences) {
        this.crossReferences = crossReferences;
    }
    
    

//    @Override
//    public String toString() {
//        return "Entry{" + "id=" + id + ", acc=" + acc + ", name=" + name + ", description=" + description + ", additionalFields=" + additionalFields + ", crossReferences=" + crossReferences + '}';
//    }

    @Override
    public String toString() {
        return "Entry{" + "id=" + id + ", name=" + name + ", description=" + description + ", additionalFields=" + additionalFields + ", crossReferences=" + crossReferences + '}';
    }

}
