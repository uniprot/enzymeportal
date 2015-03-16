/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.pdbeadapter;

import java.util.List;
import uk.ac.ebi.ep.pdbeadapter.molecule.Mapping;

/**
 *
 * @author joseph
 */
public class Polypeptide  {
       
        private String label;
    private String moleculeName;
    private String organism;
    private boolean protein;
     private List<Mapping> residues;



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


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }



    public List<Mapping> getResidues() {
        return residues;
    }

    public void setResidues(List<Mapping> residues) {
        this.residues = residues;
    }

    public String getMoleculeName() {
        return moleculeName;
    }

    public void setMoleculeName(String moleculeName) {
        this.moleculeName = moleculeName;
    }

    @Override
    public String toString() {
        return "Polypeptide{" + "label=" + label + ", moleculeName=" + moleculeName + ", organism=" + organism + ", protein=" + protein + ", residues=" + residues + '}';
    }


    
    
}
