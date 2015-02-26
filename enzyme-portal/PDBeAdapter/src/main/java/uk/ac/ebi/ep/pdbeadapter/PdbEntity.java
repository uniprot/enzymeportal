/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.pdbeadapter;

import java.util.ArrayList;
import java.util.List;
import uk.ac.ebi.ep.pdbeadapter.molecule.Molecule;

/**
 *
 * @author joseph
 */
public class PdbEntity {
    
    private String label;
    private List<Molecule> molecules;
    private String organism;
     private boolean protein;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Molecule> getMolecules() {
        if(molecules== null){
            molecules = new ArrayList<>();
        }
        
        return molecules;
    }

    public void setMolecules(List<Molecule> molecules) {
        this.molecules = molecules;
    }

    public String getOrganism() {
        return organism;
    }

    public void setOrganism(String organism) {
        this.organism = organism;
    }


    public boolean isProtein() {
        return protein;
    }

    public void setProtein(boolean protein) {
        this.protein = protein;
    }
    
    
}
