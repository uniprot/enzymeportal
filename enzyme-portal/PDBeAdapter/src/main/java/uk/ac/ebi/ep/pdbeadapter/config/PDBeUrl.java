/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.pdbeadapter.config;

/**
 *
 * @author joseph
 */
public class PDBeUrl {
    
    
    private String summaryUrl;
    private String experimentUrl;
    private String publicationsUrl;
    private String moleculesUrl;
    private String structuralDomainUrl;

    public String getSummaryUrl() {
        return summaryUrl;
    }

    public void setSummaryUrl(String summaryUrl) {
        this.summaryUrl = summaryUrl;
    }

    public String getExperimentUrl() {
        return experimentUrl;
    }

    public void setExperimentUrl(String experimentUrl) {
        this.experimentUrl = experimentUrl;
    }

    public String getPublicationsUrl() {
        return publicationsUrl;
    }

    public void setPublicationsUrl(String publicationsUrl) {
        this.publicationsUrl = publicationsUrl;
    }

    public String getMoleculesUrl() {
        return moleculesUrl;
    }

    public void setMoleculesUrl(String moleculesUrl) {
        this.moleculesUrl = moleculesUrl;
    }

    public String getStructuralDomainUrl() {
        return structuralDomainUrl;
    }

    public void setStructuralDomainUrl(String structuralDomainUrl) {
        this.structuralDomainUrl = structuralDomainUrl;
    }
    
    
    
    
}
