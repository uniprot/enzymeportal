package uk.ac.ebi.ep.web.ec.classification;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import lombok.Data;

/**
 *
 * @author joseph
 */
@Data
public class IntenzEnzyme implements Comparable<IntenzEnzyme>, Serializable {

    private static final long serialVersionUID = 1L;
    private String ec;
    private String name;
    private String description;
    private List<EnzymeSubclass> children;
    private List<EnzymeSubSubclass> subSubclasses;
    private List<EnzymeEntry> entries;
    public String subclassName;
    public String subsubclassName;
    public String entryName;

    private String type;

    public IntenzEnzyme() {
        children = new LinkedList<>();
        subSubclasses = new LinkedList<>();
        entries = new LinkedList<>();

    }

    public List<EnzymeSubclass> getChildren() {
        return children;
    }

    public void setChildren(List<EnzymeSubclass> children) {
        this.children = children;
    }

    public List<EnzymeSubSubclass> getSubSubclasses() {
        return subSubclasses;
    }

    public void setSubSubclasses(List<EnzymeSubSubclass> subSubclasses) {
        this.subSubclasses = subSubclasses;
    }

    public List<EnzymeEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<EnzymeEntry> entries) {
        this.entries = entries;
    }

    @Override
    public String toString() {
        return "IntenzEnzyme{" + "ec=" + ec + ", name=" + name + ", description=" + description + ", children=" + children + ", subSubclasses=" + subSubclasses + ", entries=" + entries + '}';
    }

    @Override
    public int compareTo(IntenzEnzyme e) {

        return name.compareTo(e.getName());

    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.subclassName);
        hash = 29 * hash + Objects.hashCode(this.subsubclassName);
        hash = 29 * hash + Objects.hashCode(this.entryName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IntenzEnzyme other = (IntenzEnzyme) obj;
        if (!Objects.equals(this.subclassName, other.subclassName)) {
            return false;
        }
        if (!Objects.equals(this.subsubclassName, other.subsubclassName)) {
            return false;
        }
        return Objects.equals(this.entryName, other.entryName);
    }
}
