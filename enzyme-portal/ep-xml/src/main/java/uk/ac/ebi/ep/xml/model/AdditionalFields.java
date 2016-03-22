
package uk.ac.ebi.ep.xml.model;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author joseph
 */
@XmlType
public class AdditionalFields {

    private Set<Field> field = new LinkedHashSet<>();
    

    public void setField(Set<Field> field) {
        this.field = field;
    }

     @XmlElement(name = "field")
    public Set<Field> getField() {
        return field;
    }

}
