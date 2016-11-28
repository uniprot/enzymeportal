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
public class EnzymePortal {

    private final UniprotEntry protein;

    public EnzymePortal(UniprotEntry p) {
        this.protein = p;
    }

    public UniprotEntry unwrapProtein() {
        return protein;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.protein.getProteinName());
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
        final EnzymePortal other = (EnzymePortal) obj;
        return Objects.equals(this.protein.getProteinName(), other.protein.getProteinName());
    }

    

}
