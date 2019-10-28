/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.web.ec.classification;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author joseph
 */
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

    public String getEc() {
        return ec;
    }

    public void setEc(String ec) {
        this.ec = ec;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "EnzymeSubSubclass{" + "entries=" + entries + ", ec=" + ec + ", subsubClassName=" + name + ", description=" + description + '}';
    }

}
