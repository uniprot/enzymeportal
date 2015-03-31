/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.search.model;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author joseph
 */
public class Species implements Comparable<Species>, Serializable {

    private String scientificname;

    private String commonname ;
    private boolean selected;
     private Long taxId;
        protected transient  Object scoring;


    public Species() {
    }
    
    
    
    public Species(String scientificname, String commonname) {
        this.scientificname = scientificname;
        this.commonname = commonname;
        
    }

    public Species(String scientificname, String commonname, Long taxId) {
        this.scientificname = scientificname;
        this.commonname = commonname;
        this.taxId = taxId;
    }
    
    

    
  

    public Object getScoring() {
        return scoring;
    }

    public void setScoring(Object scoring) {
        this.scoring = scoring;
    }

    public Long getTaxId() {
        return taxId;
    }

    public void setTaxId(Long taxId) {
        this.taxId = taxId;
    }
     
     

    /**
     * Gets the value of the scientificname property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getScientificname() {
        return scientificname;
    }

    /**
     * Sets the value of the scientificname property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setScientificname(String value) {
        this.scientificname = value;
    }

    /**
     * Gets the value of the commonname property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getCommonname() {
        return commonname;
    }

    /**
     * Sets the value of the commonname property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setCommonname(String value) {
        this.commonname = value;
    }

    
    
    
    /**
     * Gets the value of the selected property.
     *
     * @return 
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Sets the value of the selected property.
     *
     */
    public void setSelected(boolean value) {
        this.selected = value;
    }



    public Species withScientificname(String value) {
        setScientificname(value);
        return this;
    }

    public Species withCommonname(String value) {
        setCommonname(value);
        return this;
    }

    public Species withSelected(boolean value) {
        setSelected(value);
        return this;
    }



    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.scientificname);
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
        final Species other = (Species) obj;
        if (!Objects.equals(this.scientificname, other.scientificname)) {
            return false;
        }
        return true;
    }
    
    
    


    
    
    @Override
    public String toString() {
        return "Species{" + "scientificname=" + scientificname + ", commonname=" + commonname + '}';
    }
    
    @Override
    public int compareTo(Species o) {
        return this.scientificname.compareToIgnoreCase(o.getScientificname());
    }
    
    

}
