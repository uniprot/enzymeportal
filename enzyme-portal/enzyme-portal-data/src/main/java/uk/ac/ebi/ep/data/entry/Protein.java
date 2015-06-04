/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.entry;

import java.util.Objects;
import uk.ac.ebi.ep.data.domain.UniprotEntry;

/**
 *
 * @author joseph
 */
public class Protein extends UniprotEntry {

    private String enzymName;


    public Protein(String accession,String proteinName) {
        super(accession);
        this.enzymName = proteinName;
    }

    public String getEnzymName() {
        return enzymName;
    }

    public void setEnzymName(String enzymName) {
        this.enzymName = enzymName;
    }


    
    

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.enzymName);
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
        final Protein other = (Protein) obj;
        if (!Objects.equals(this.enzymName, other.enzymName)) {
            return false;
        }
        return true;
    }
    

    
    
}
