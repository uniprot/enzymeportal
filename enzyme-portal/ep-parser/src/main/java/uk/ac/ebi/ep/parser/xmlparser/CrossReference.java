/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.parser.xmlparser;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author joseph
 */
@XmlType
public class CrossReference {

    private List<Ref> ref = new ArrayList<>();

    public CrossReference() {
    }

    @XmlElement(name = "ref")
    public List<Ref> getRef() {
        return ref;
    }

    public void setRef(List<Ref> ref) {
        this.ref = ref;
    }

}
