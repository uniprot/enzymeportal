package uk.ac.ebi.ep.web.utils;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import uk.ac.ebi.ep.literatureservice.model.Result;

/**
 * Represents an enzyme view
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Data
@Builder(builderMethodName = "enzymePageBuilder")
public class EnzymePage {

    //@Singular
    private final List<String> accessions;
    private String enzymeName;
    private String ec;
    private String catalyticActivities;
    //@Singular
    private List<Result> citations;

//    public EnzymePage(List<String> accessions, List<Result> citations, String enzymeName, String ec, String catalyticActivities) {
//        this.accessions = accessions;
//        this.enzymeName = enzymeName;
//        this.ec = ec;
//        this.catalyticActivities = catalyticActivities;
//        this.citations = citations;
//    }
//
//    public List<String> getAccessions() {
//        if (accessions == null) {
//            accessions = new ArrayList<>();
//        }
//        return accessions;
//    }
//
//    public void setAccessions(List<String> accessions) {
//        this.accessions = accessions;
//    }
//
//    public String getEnzymeName() {
//        return enzymeName;
//    }
//
//    public void setEnzymeName(String enzymeName) {
//        this.enzymeName = enzymeName;
//    }
//
//    public String getEc() {
//        return ec;
//    }
//
//    public void setEc(String ec) {
//        this.ec = ec;
//    }
//
//    public String getCatalyticActivities() {
//        return catalyticActivities;
//    }
//
//    public void setCatalyticActivities(String catalyticActivities) {
//        this.catalyticActivities = catalyticActivities;
//    }
//
//    public List<Result> getCitations() {
//        if (citations == null) {
//            citations = new ArrayList<>();
//        }
//
//        return citations;
//    }
//
//    public void setCitations(List<Result> citations) {
//        this.citations = citations;
//    }
}
