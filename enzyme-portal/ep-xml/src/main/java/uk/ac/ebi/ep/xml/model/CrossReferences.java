
package uk.ac.ebi.ep.xml.model;

import java.util.Set;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author joseph
 */
@XmlType
public class CrossReferences {

    private Set<Ref> ref;

    @XmlElement(name = "ref")
    public Set<Ref> getRef() {
        return ref;
    }

    public void setRef(Set<Ref> ref) {
        this.ref = ref;
    }

}
