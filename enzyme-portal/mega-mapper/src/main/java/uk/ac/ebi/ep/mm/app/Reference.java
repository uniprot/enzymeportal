/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.mm.app;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author joseph
 */
@Entity
@Table(name = "REFERENCE")
public class Reference implements Serializable{
   private static final long serialVersionUID = 1L;
    @Id
    private Long id;
    private Long compound_id;
    private String location_in_ref; //LOCATION_IN_REF;
    
    private String reference_name;
    
    private String reference_id;
    
    private String accession_number;

    public Reference() {
    }

    public Reference(String reference_id,String reference_name,String location_in_ref) {
        
        this.reference_id = reference_id;
        this.reference_name = reference_name;
        this.location_in_ref = location_in_ref;
        
    }

    public Long getCompound_id() {
        return compound_id;
    }

    public void setCompound_id(Long compound_id) {
        this.compound_id = compound_id;
    }



    public String getLocation_in_ref() {
        return location_in_ref;
    }

    public void setLocation_in_ref(String location_in_ref) {
        this.location_in_ref = location_in_ref;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference_id() {
        return reference_id;
    }

    public void setReference_id(String reference_id) {
        this.reference_id = reference_id;
    }

    public String getReference_name() {
        return reference_name;
    }

    public void setReference_name(String reference_name) {
        this.reference_name = reference_name;
    }

    public String getAccession_number() {
        return accession_number;
    }

    public void setAccession_number(String accession_number) {
        this.accession_number = accession_number;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Reference other = (Reference) obj;
        if ((this.reference_name == null) ? (other.reference_name != null) : !this.reference_name.equals(other.reference_name)) {
            return false;
        }
        if ((this.reference_id == null) ? (other.reference_id != null) : !this.reference_id.equals(other.reference_id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.reference_name != null ? this.reference_name.hashCode() : 0);
        hash = 29 * hash + (this.reference_id != null ? this.reference_id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Reference{" + "reference_name=" + reference_name + '}';
    }
    
    
    
}
