/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.enzymes;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author joseph
 */
public class EnzymeSubclass  implements Serializable{
    private static final long serialVersionUID = 1L;
    private List<EnzymeSubSubclass> subSubclasses;
    private String ec;
    private String name;
    
    private String description;
   
 

    public EnzymeSubclass() {
        //super();
        subSubclasses = new LinkedList<>();
        
    }

    public List<EnzymeSubSubclass> getSubSubclasses() {
        return subSubclasses;
    }

    public void setSubSubclasses(List<EnzymeSubSubclass> subSubclasses) {
        this.subSubclasses = subSubclasses;
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
        return "EnzymeSubclass{" + "subSubclasses=" + subSubclasses + ", ec=" + ec + ", name=" + name + ", description=" + description + '}';
    }








    
    
}
