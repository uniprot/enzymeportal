/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.pdbeadapter;

import java.util.List;
import java.util.Objects;
import uk.ac.ebi.ep.pdbeadapter.molecule.Mapping;

/**
 *
 * @author joseph
 */
public class Polypeptide implements Comparable<Polypeptide> {

    private String label;
    private String moleculeName;
    private String organism;
    private boolean protein;
    private List<Mapping> residues;
    
    private String chainId;
    
        public String getChainId() {
        return chainId.replaceAll("\\[", "").replaceAll("]", "");
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
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
    public int compareTo(Polypeptide o) {
        return this.label.compareToIgnoreCase(o.getLabel());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.moleculeName);
        hash = 23 * hash + Objects.hashCode(this.chainId);
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
        final Polypeptide other = (Polypeptide) obj;
        if (!Objects.equals(this.moleculeName, other.moleculeName)) {
            return false;
        }
        return Objects.equals(this.chainId, other.chainId);
    }

    @Override
    public String toString() {
        return "Polypeptide{" + "label=" + label + ", moleculeName=" + moleculeName + ", organism=" + organism + ", protein=" + protein + ", residues=" + residues + ", chainId=" + chainId + '}';
    }



}
