/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.enzymes;

import java.io.Serializable;

/**
 *
 * @author joseph
 */
public class EnzymeEntry implements Serializable{
    private static final long serialVersionUID = 1L;
     private String ec;
    private String name;
    private String description;
    
   
    
    public EnzymeEntry() {
        //super();
        
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
        return "EnzymeEntry{" + "ec=" + ec + ", entryClassName=" + name + ", description=" + description + '}';
    }






    
}
