/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.entry;

import java.util.Objects;
import uk.ac.ebi.ep.data.domain.EnzymePortalEcNumbers;

/**
 *
 * @author joseph
 */
public class Family implements Comparable<Family> {

    private final EnzymePortalEcNumbers enzymePortalEcNumbers;

    public Family(EnzymePortalEcNumbers ecNumbers) {
        this.enzymePortalEcNumbers = ecNumbers;
    }

    public EnzymePortalEcNumbers unwrapFamily() {
        return enzymePortalEcNumbers;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.enzymePortalEcNumbers.getEcFamily());
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
        final Family other = (Family) obj;
        if (!Objects.equals(this.enzymePortalEcNumbers.getEcFamily(), other.enzymePortalEcNumbers.getEcFamily())) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Family o) {
      return this.enzymePortalEcNumbers.getEcFamily().compareTo(o.enzymePortalEcNumbers.getEcFamily());
    }



}
