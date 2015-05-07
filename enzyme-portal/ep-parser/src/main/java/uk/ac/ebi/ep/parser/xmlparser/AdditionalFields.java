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
@XmlType(name = "additional_fields")
public class AdditionalFields {

    private List<Field> field = new ArrayList<>();
    public void setField(List<Field> field) {
        this.field = field;
    }

     @XmlElement(name = "field")
    public List<Field> getField() {
        return field;
    }

   

}
