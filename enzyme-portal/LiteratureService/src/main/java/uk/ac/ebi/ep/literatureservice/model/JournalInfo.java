
package uk.ac.ebi.ep.literatureservice.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author joseph
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "issue",
    "volume",
    "journalIssueId",
    "dateOfPublication",
    "monthOfPublication",
    "yearOfPublication",
    "printPublicationDate",
    "journal"
})
public class JournalInfo {

    @JsonProperty("issue")
    private String issue;
    @JsonProperty("volume")
    private String volume;
    @JsonProperty("journalIssueId")
    private Integer journalIssueId;
    @JsonProperty("dateOfPublication")
    private String dateOfPublication;
    @JsonProperty("monthOfPublication")
    private Integer monthOfPublication;
    @JsonProperty("yearOfPublication")
    private Integer yearOfPublication;
    @JsonProperty("printPublicationDate")
    private String printPublicationDate;
    @JsonProperty("journal")
    private Journal journal;
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<>();

    /**
     *
     * @return The issue
     */
    @JsonProperty("issue")
    public String getIssue() {
        return issue;
    }

    /**
     *
     * @param issue The issue
     */
    @JsonProperty("issue")
    public void setIssue(String issue) {
        this.issue = issue;
    }

    /**
     *
     * @return The volume
     */
    @JsonProperty("volume")
    public String getVolume() {
        return volume;
    }

    /**
     *
     * @param volume The volume
     */
    @JsonProperty("volume")
    public void setVolume(String volume) {
        this.volume = volume;
    }

    /**
     *
     * @return The journalIssueId
     */
    @JsonProperty("journalIssueId")
    public Integer getJournalIssueId() {
        return journalIssueId;
    }

    /**
     *
     * @param journalIssueId The journalIssueId
     */
    @JsonProperty("journalIssueId")
    public void setJournalIssueId(Integer journalIssueId) {
        this.journalIssueId = journalIssueId;
    }

    /**
     *
     * @return The dateOfPublication
     */
    @JsonProperty("dateOfPublication")
    public String getDateOfPublication() {
        return dateOfPublication;
    }

    /**
     *
     * @param dateOfPublication The dateOfPublication
     */
    @JsonProperty("dateOfPublication")
    public void setDateOfPublication(String dateOfPublication) {
        this.dateOfPublication = dateOfPublication;
    }

    /**
     *
     * @return The monthOfPublication
     */
    @JsonProperty("monthOfPublication")
    public Integer getMonthOfPublication() {
        return monthOfPublication;
    }

    /**
     *
     * @param monthOfPublication The monthOfPublication
     */
    @JsonProperty("monthOfPublication")
    public void setMonthOfPublication(Integer monthOfPublication) {
        this.monthOfPublication = monthOfPublication;
    }

    /**
     *
     * @return The yearOfPublication
     */
    @JsonProperty("yearOfPublication")
    public Integer getYearOfPublication() {
        return yearOfPublication;
    }

    /**
     *
     * @param yearOfPublication The yearOfPublication
     */
    @JsonProperty("yearOfPublication")
    public void setYearOfPublication(Integer yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }

    /**
     *
     * @return The printPublicationDate
     */
    @JsonProperty("printPublicationDate")
    public String getPrintPublicationDate() {
        return printPublicationDate;
    }

    /**
     *
     * @param printPublicationDate The printPublicationDate
     */
    @JsonProperty("printPublicationDate")
    public void setPrintPublicationDate(String printPublicationDate) {
        this.printPublicationDate = printPublicationDate;
    }

    /**
     *
     * @return The journal
     */
    @JsonProperty("journal")
    public Journal getJournal() {
        return journal;
    }

    /**
     *
     * @param journal The journal
     */
    @JsonProperty("journal")
    public void setJournal(Journal journal) {
        this.journal = journal;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
