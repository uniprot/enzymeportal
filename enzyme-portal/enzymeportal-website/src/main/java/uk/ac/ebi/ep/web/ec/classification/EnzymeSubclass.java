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
public class EnzymeSubclass implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<EnzymeSubSubclass> subSubclasses;
    private String ec;
    private String name;

    private String description;

    public EnzymeSubclass() {
        subSubclasses = new LinkedList<>();

    }

    public List<EnzymeSubSubclass> getSubSubclasses() {
        return subSubclasses;
    }

    public void setSubSubclasses(List<EnzymeSubSubclass> subSubclasses) {
        this.subSubclasses = subSubclasses;
    }

    @Override
    public String toString() {
        return "EnzymeSubclass{" + "subSubclasses=" + subSubclasses + ", ec=" + ec + ", name=" + name + ", description=" + description + '}';
    }

}
