package uk.ac.ebi.ep.centralservice.chembl.config;

/**
 *
 * @author joseph
 */
public class ChemblServiceUrl {

    private String mechanismUrl;
    private String moleculeUrl;
    private String assayUrl;
    //private String activityUrl;
    private String inhibitionActivityUrl;
    private String ic50ActivityUrl;
    private String primaryTargetSelectorUrl;
    private String inhibitionIc50Url;

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

//    public String getActivityUrl() {
//        return activityUrl;
//    }
//
//    public void setActivityUrl(String activityUrl) {
//        this.activityUrl = activityUrl;
//    }

    public String getIc50ActivityUrl() {
        return ic50ActivityUrl;
    }

    public void setIc50ActivityUrl(String ic50ActivityUrl) {
        this.ic50ActivityUrl = ic50ActivityUrl;
    }

    public String getPrimaryTargetSelectorUrl() {
        return primaryTargetSelectorUrl;
    }

    public void setPrimaryTargetSelectorUrl(String primaryTargetSelectorUrl) {
        this.primaryTargetSelectorUrl = primaryTargetSelectorUrl;
    }

    public String getInhibitionIc50Url() {
        return inhibitionIc50Url;
    }

    public void setInhibitionIc50Url(String inhibitionIc50Url) {
        this.inhibitionIc50Url = inhibitionIc50Url;
    }

    public String getInhibitionActivityUrl() {
        return inhibitionActivityUrl;
    }

    public void setInhibitionActivityUrl(String inhibitionActivityUrl) {
        this.inhibitionActivityUrl = inhibitionActivityUrl;
    }
    
    

}
