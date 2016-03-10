/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
