/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.search.model;

import java.io.Serializable;

/**
 *
 * @author joseph
 */
public class Species implements Comparable<Species>, Serializable {

    private String scientificname;

    private String commonname ;
    private boolean selected;
     private Long taxId;
        protected Object scoring;
//    private List<Compound> compounds;
//    private List<Disease> diseases;

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

    /**
     * Gets the value of the compounds property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the compounds property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCompounds().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Compound }
     *
     *
     */
//    public List<Compound> getCompounds() {
//        if (compounds == null) {
//            compounds = new ArrayList<Compound>();
//        }
//        return this.compounds;
//    }

    /**
     * Gets the value of the diseases property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the diseases property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDiseases().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Disease }
     *
     *
     * @return
     */
//    public List<Disease> getDiseases() {
//        if (diseases == null) {
//            diseases = new ArrayList<>();
//        }
//        return this.diseases;
//    }

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

//    public Species withCompounds(Compound... values) {
//        if (values != null) {
//            for (Compound value : values) {
//                getCompounds().add(value);
//            }
//        }
//        return this;
//    }
//
//    public Species withCompounds(Collection<Compound> values) {
//        if (values != null) {
//            getCompounds().addAll(values);
//        }
//        return this;
//    }
//
//    public Species withDiseases(Disease... values) {
//        if (values != null) {
//            for (Disease value : values) {
//                getDiseases().add(value);
//            }
//        }
//        return this;
//    }
//
//    public Species withDiseases(Collection<Disease> values) {
//        if (values != null) {
//            getDiseases().addAll(values);
//        }
//        return this;
//    }
//
//    /**
//     * Sets the value of the compounds property.
//     *
//     * @param compounds allowed object is {@link Compound }
//     *
//     */
//    public void setCompounds(List<Compound> compounds) {
//        this.compounds = compounds;
//    }
//
//    /**
//     * Sets the value of the diseases property.
//     *
//     * @param diseases allowed object is {@link Disease }
//     *
//     */
//    public void setDiseases(List<Disease> diseases) {
//        this.diseases = diseases;
//    }

//    @Override
//    public int hashCode() {
//        int hash = 5;
//        hash = 23 * hash + Objects.hashCode(this.scientificname);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final Species other = (Species) obj;
//        if (!Objects.equals(this.scientificname, other.scientificname)) {
//            return false;
//        }
//        return true;
//    }
    
    
    

//    @Override
//    public int compareTo(Species other) {
//        if (this.getCommonname() == null & other.getCommonname() == null) {
//            return this.getScientificname().compareTo(other.getScientificname());
//        }
//        if (this.getCommonname() != null & other.getCommonname() == null) {
//            return this.getCommonname().compareTo(other.getScientificname());
//        }
//        if (this.getCommonname() == null & other.getCommonname() != null) {
//            return this.getScientificname().compareTo(other.getCommonname());
//        }
//
//        if (this.getCommonname() != null & this.getScientificname().split("\\(")[0].trim().equalsIgnoreCase(CommonSpecies.Baker_Yeast.getScientificName()) && other.getCommonname() != null & other.getScientificname().split("\\(")[0].trim().equalsIgnoreCase(CommonSpecies.Baker_Yeast.getScientificName())) {
//            return this.getScientificname().compareTo(other.getScientificname());
//        }
//        return this.getCommonname().compareTo(other.getCommonname());
//        //return this.getScientificname().compareTo(other.getScientificname());
//
//    }
//    

//    @Override
//    public int hashCode() {
//        int hash = 7;
//        //hash = 79 * hash + Objects.hashCode(this.scientificname);
//        hash = 79 * hash + (this.getScientificname() != null ? this.getScientificname().hashCode() : 0);
//        return hash;
//    }
    
    
    
//    @Override
//    public int hashCode() {
//        int hash = 5;
//        hash = 67 * hash + Objects.hashCode(this.scientificname);
//        hash = 67 * hash + Objects.hashCode(this.commonname);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final Species other = (Species) obj;
//        if (!Objects.equals(this.scientificname, other.scientificname)) {
//            return false;
//        }
//        if (!Objects.equals(this.commonname, other.commonname)) {
//            return false;
//        }
//        return true;
//    }
    
    
    
    @Override
    public String toString() {
        return "Species{" + "scientificname=" + scientificname + ", commonname=" + commonname + '}';
    }
    
    @Override
    public int compareTo(Species o) {
        return this.scientificname.compareToIgnoreCase(o.getScientificname());
    }
    
    

}
