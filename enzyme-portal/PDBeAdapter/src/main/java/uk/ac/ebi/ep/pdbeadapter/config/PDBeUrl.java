package uk.ac.ebi.ep.pdbeadapter.config;

import lombok.Data;

/**
 *
 * @author joseph
 */
@Data
public class PDBeUrl {

    private String summaryUrl;
    private String experimentUrl;
    private String publicationsUrl;
    private String moleculesUrl;
    private String structuralDomainUrl;
    private String ligandUrl;
    private String cofactorUrl;

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

    public String getLigandUrl() {
        return ligandUrl;
    }

    public void setLigandUrl(String ligandUrl) {
        this.ligandUrl = ligandUrl;
    }

    public String getCofactorUrl() {
        return cofactorUrl;
    }

    public void setCofactorUrl(String cofactorUrl) {
        this.cofactorUrl = cofactorUrl;
    }
    
    


}
