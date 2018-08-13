/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.analysis.config;

/**
 *Holds the URL links to Uniprot website
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class ServiceUrl {
    private String functionUrl;
    private String cofactorUrl;
    private String regulationUrl;
    private String activityUrl;
    private String bioPhysioUrl;

    public String getFunctionUrl() {
        return functionUrl;
    }

    public void setFunctionUrl(String functionUrl) {
        this.functionUrl = functionUrl;
    }

    public String getCofactorUrl() {
        return cofactorUrl;
    }

    public void setCofactorUrl(String cofactorUrl) {
        this.cofactorUrl = cofactorUrl;
    }

    public String getRegulationUrl() {
        return regulationUrl;
    }

    public void setRegulationUrl(String regulationUrl) {
        this.regulationUrl = regulationUrl;
    }

    public String getActivityUrl() {
        return activityUrl;
    }

    public void setActivityUrl(String activityUrl) {
        this.activityUrl = activityUrl;
    }

    public String getBioPhysioUrl() {
        return bioPhysioUrl;
    }

    public void setBioPhysioUrl(String bioPhysioUrl) {
        this.bioPhysioUrl = bioPhysioUrl;
    }


    
}
