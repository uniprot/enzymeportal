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

@XmlType(name = "Entry")
public class Entry {

    private List<AdditionalFields> additional_fields = new ArrayList<>();
  private List<CrossReference> cross_references = new ArrayList<>();

     @XmlElement (name = "cross_references")
    public List<CrossReference> getCross_references() {
        return cross_references;
    }

    public void setCross_references(List<CrossReference> cross_references) {
        this.cross_references = cross_references;
    }


    @XmlElement (name = "additional_fields")
    public List<AdditionalFields> getAdditional_fields() {
        return additional_fields;
    }

    public void setAdditional_fields(List<AdditionalFields> additional_fields) {
        this.additional_fields = additional_fields;
    }
}
