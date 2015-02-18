/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.pdbeadapter.publication;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author joseph
 */
@JsonPropertyOrder({
    "volume",
    "pdb_abbreviation",
    "ISO_abbreviation",
    "year",
    "issue",
    "pages"
})
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class JournalInfo {

    @JsonProperty("volume")
    private String volume;
    @JsonProperty("pdb_abbreviation")
    private String pdbAbbreviation;
    @JsonProperty("ISO_abbreviation")
    private String ISOAbbreviation;
    @JsonProperty("year")
    private Integer year;
    @JsonProperty("issue")
    private String issue;
    @JsonProperty("pages")
    private String pages;
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<>();

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
     * @return The pdbAbbreviation
     */
    @JsonProperty("pdb_abbreviation")
    public String getPdbAbbreviation() {
        return pdbAbbreviation;
    }

    /**
     *
     * @param pdbAbbreviation The pdb_abbreviation
     */
    @JsonProperty("pdb_abbreviation")
    public void setPdbAbbreviation(String pdbAbbreviation) {
        this.pdbAbbreviation = pdbAbbreviation;
    }

    /**
     *
     * @return The ISOAbbreviation
     */
    @JsonProperty("ISO_abbreviation")
    public String getISOAbbreviation() {
        return ISOAbbreviation;
    }

    /**
     *
     * @param abbreviation The ISO_abbreviation
     */
    @JsonProperty("ISO_abbreviation")
    public void setISOAbbreviation(String abbreviation) {
        this.ISOAbbreviation = abbreviation;
    }

    /**
     *
     * @return The year
     */
    @JsonProperty("year")
    public Integer getYear() {
        return year;
    }

    /**
     *
     * @param year The year
     */
    @JsonProperty("year")
    public void setYear(Integer year) {
        this.year = year;
    }

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
     * @return The pages
     */
    @JsonProperty("pages")
    public String getPages() {
        return pages;
    }

    /**
     *
     * @param pages The pages
     */
    @JsonProperty("pages")
    public void setPages(String pages) {
        this.pages = pages;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "JournalInfo{" + "volume=" + volume + ", pdbAbbreviation=" + pdbAbbreviation + ", ISOAbbreviation=" + ISOAbbreviation + ", year=" + year + ", issue=" + issue + ", pages=" + pages + '}';
    }
    
    
    
}
