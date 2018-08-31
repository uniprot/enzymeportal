
package uk.ac.ebi.ep.centralservice.helper;

/**
 *
 * @author Joseph
 */
public enum StandardType {
    INHIBITION("Inhibition"),IC50("IC50");
    
    private final String standardType;
    private StandardType(String type){
        this.standardType = type;
    }

    public String getStandardType() {
        return standardType;
    }
    
    
}
