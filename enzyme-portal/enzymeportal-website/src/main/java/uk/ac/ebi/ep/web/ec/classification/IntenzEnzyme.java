package uk.ac.ebi.ep.web.ec.classification;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author joseph
 */
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

    public String getEc() {
        return ec;
    }

    public void setEc(String ec) {
        this.ec = ec;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String className) {
        this.name = className;
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

    public String getSubclassName() {
        return subclassName;
    }

    public void setSubclassName(String subclassName) {
        this.subclassName = subclassName;
    }

    public String getSubsubclassName() {
        return subsubclassName;
    }

    public void setSubsubclassName(String subsubclassName) {
        this.subsubclassName = subsubclassName;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
