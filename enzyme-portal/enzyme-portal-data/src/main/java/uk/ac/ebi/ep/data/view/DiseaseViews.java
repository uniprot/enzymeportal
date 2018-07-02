/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.view;

import java.util.Objects;

public class DiseaseViews {

    private String omimNumber;

    private String diseaseName;

    public DiseaseViews(String omimNumber, String diseaseName) {
        this.omimNumber = omimNumber;
        this.diseaseName = diseaseName;
    }

    public String getOmimNumber() {
        return omimNumber;
    }

    public void setOmimNumber(String omimNumber) {
        this.omimNumber = omimNumber;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.omimNumber);
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
        final DiseaseViews other = (DiseaseViews) obj;
        if (!Objects.equals(this.omimNumber, other.omimNumber)) {
            return false;
        }
        return true;
    }

}
