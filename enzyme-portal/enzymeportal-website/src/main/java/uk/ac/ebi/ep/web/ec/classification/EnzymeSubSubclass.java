/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.web.ec.classification;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author joseph
 */
@Getter
@Setter
public class EnzymeSubSubclass implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<EnzymeEntry> entries;

    private String ec;
    private String name;
    private String description;

    public EnzymeSubSubclass() {
        entries = new LinkedList<>();

    }

    public List<EnzymeEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<EnzymeEntry> entries) {
        this.entries = entries;
    }

    @Override
    public String toString() {
        return "EnzymeSubSubclass{" + "entries=" + entries + ", ec=" + ec + ", subsubClassName=" + name + ", description=" + description + '}';
    }

}
