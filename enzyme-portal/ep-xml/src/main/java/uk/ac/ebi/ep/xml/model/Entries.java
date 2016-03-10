/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.xml.model;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author joseph
 */
@XmlType
public class Entries {

    private List<Entry> entry;

    @XmlElement(name = "entry")
    public List<Entry> getEntry() {
        return entry;
    }

    public void setEntry(List<Entry> entry) {
        this.entry = entry;
    }

}
