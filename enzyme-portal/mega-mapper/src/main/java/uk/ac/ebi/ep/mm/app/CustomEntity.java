/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.mm.app;

/**
 *the aim of this class if to provide a transfer object ( place holder) for the entities that will be returned from the hibernate query
 * @author joseph
 */

public final class CustomEntity {
    
    private Compounds compounds;
    private Reference reference;
    private String relationship;

    public CustomEntity() {
    }
    
    

    public CustomEntity(Compounds compounds) {
        this.compounds = compounds;
    }

    public CustomEntity(Reference reference) {
        this.reference = reference;
    }
    
    

    public CustomEntity(Compounds compounds, Reference reference) {
        this.compounds = compounds;
        this.reference = reference;
        this.setRelationship(reference.getLocation_in_ref());
    }
    
    

    public CustomEntity(String chebiAccession, String name, String reference_id, String reference_name, String relation) {
      
        compounds = new Compounds();
        compounds.setChebiAccession(chebiAccession);
        compounds.setName(name);
        
        
        reference = new Reference();
        reference.setReference_id(reference_id);
        reference.setReference_name(reference_name);
       reference.setLocation_in_ref(relation);
        this.relationship = relation;
    }

    public Compounds getCompounds() {
        return compounds;
    }

    public Reference getReference() {
        return reference;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CustomEntity other = (CustomEntity) obj;
        if (this.compounds != other.compounds && (this.compounds == null || !this.compounds.equals(other.compounds))) {
            return false;
        }
        if (this.reference != other.reference && (this.reference == null || !this.reference.equals(other.reference))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + (this.compounds != null ? this.compounds.hashCode() : 0);
        hash = 17 * hash + (this.reference != null ? this.reference.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "CustomEntity{" + "compounds=" + compounds + ", reference=" + reference + ", relationship=" + relationship + '}';
    }
    
    
    
   
}
