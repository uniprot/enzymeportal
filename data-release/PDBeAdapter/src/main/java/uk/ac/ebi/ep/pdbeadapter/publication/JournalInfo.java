/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.pdbeadapter.publication;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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
    private String isoAbbreviation;
    @JsonProperty("year")
    private Integer year;
    @JsonProperty("issue")
    private String issue;
    @JsonProperty("pages")
    private String pages;


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
     * @return The isoAbbreviation
     */
    @JsonProperty("ISO_abbreviation")
    public String getIsoAbbreviation() {
        return isoAbbreviation;
    }

        /**
     *
     * @param isoAbbreviation The isoAbbreviation
     */
    @JsonProperty("ISO_abbreviation")
    public void setIsoAbbreviation(String isoAbbreviation) {
        this.isoAbbreviation = isoAbbreviation;
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

   
    
}
