/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.dummy;

import java.util.Objects;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class Species {
    private String commonName;
    private String scientificName;
    private long taxId;

    public Species() {
    }

    public Species(String commonName, String scientificName) {
        this.commonName = commonName;
        this.scientificName = scientificName;
    }

    public Species(String commonName, String scientificName, long taxId) {
        this.commonName = commonName;
        this.scientificName = scientificName;
        this.taxId = taxId;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public long getTaxId() {
        return taxId;
    }

    public void setTaxId(long taxId) {
        this.taxId = taxId;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.scientificName);
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
        if (!Objects.equals(this.scientificName, other.scientificName)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Species{" + "commonName=" + commonName + ", scientificName=" + scientificName + ", taxId=" + taxId + '}';
    }
    
    
}
