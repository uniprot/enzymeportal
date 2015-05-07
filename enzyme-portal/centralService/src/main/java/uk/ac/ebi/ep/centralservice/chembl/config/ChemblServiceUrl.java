/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.centralservice.chembl.config;

/**
 *
 * @author joseph
 */
public class ChemblServiceUrl {
    
    private String mechanismUrl;
    private String moleculeUrl;
    private String assayUrl;
    private String activityUrl;

    public String getMechanismUrl() {
        return mechanismUrl;
    }

    public void setMechanismUrl(String mechanismUrl) {
        this.mechanismUrl = mechanismUrl;
    }

    public String getMoleculeUrl() {
        return moleculeUrl;
    }

    public void setMoleculeUrl(String moleculeUrl) {
        this.moleculeUrl = moleculeUrl;
    }

    public String getAssayUrl() {
        return assayUrl;
    }

    public void setAssayUrl(String assayUrl) {
        this.assayUrl = assayUrl;
    }

    public String getActivityUrl() {
        return activityUrl;
    }

    public void setActivityUrl(String activityUrl) {
        this.activityUrl = activityUrl;
    }

    
    
}
