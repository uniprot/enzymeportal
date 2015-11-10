/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    "title",
    "medlineAbbreviation",
    "essn",
    "issn",
    "isoabbreviation",
    "nlmid"
})
public class Journal {

    @JsonProperty("title")
    private String title;
    @JsonProperty("medlineAbbreviation")
    private String medlineAbbreviation;
    @JsonProperty("essn")
    private String essn;
    @JsonProperty("issn")
    private String issn;
    @JsonProperty("isoabbreviation")
    private String isoabbreviation;
    @JsonProperty("nlmid")
    private String nlmid;
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<>();

    /**
     *
     * @return The title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title The title
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return The medlineAbbreviation
     */
    @JsonProperty("medlineAbbreviation")
    public String getMedlineAbbreviation() {
        return medlineAbbreviation;
    }

    /**
     *
     * @param medlineAbbreviation The medlineAbbreviation
     */
    @JsonProperty("medlineAbbreviation")
    public void setMedlineAbbreviation(String medlineAbbreviation) {
        this.medlineAbbreviation = medlineAbbreviation;
    }

    /**
     *
     * @return The essn
     */
    @JsonProperty("essn")
    public String getEssn() {
        return essn;
    }

    /**
     *
     * @param essn The essn
     */
    @JsonProperty("essn")
    public void setEssn(String essn) {
        this.essn = essn;
    }

    /**
     *
     * @return The issn
     */
    @JsonProperty("issn")
    public String getIssn() {
        return issn;
    }

    /**
     *
     * @param issn The issn
     */
    @JsonProperty("issn")
    public void setIssn(String issn) {
        this.issn = issn;
    }

    /**
     *
     * @return The isoabbreviation
     */
    @JsonProperty("isoabbreviation")
    public String getIsoabbreviation() {
        return isoabbreviation;
    }

    /**
     *
     * @param isoabbreviation The isoabbreviation
     */
    @JsonProperty("isoabbreviation")
    public void setIsoabbreviation(String isoabbreviation) {
        this.isoabbreviation = isoabbreviation;
    }

    /**
     *
     * @return The nlmid
     */
    @JsonProperty("nlmid")
    public String getNlmid() {
        return nlmid;
    }

    /**
     *
     * @param nlmid The nlmid
     */
    @JsonProperty("nlmid")
    public void setNlmid(String nlmid) {
        this.nlmid = nlmid;
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
