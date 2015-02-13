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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author joseph
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PDBePublication {

    @JsonProperty("journal_info")
    private JournalInfo journalInfo;
    @JsonProperty("author_list")
    private List<AuthorList> authorList;
    @JsonProperty("pubmed_id")
    private String pubmedId;
    @JsonProperty("type")
    private String type;
    @JsonProperty("associated_entries")
    private Object associatedEntries;
    @JsonProperty("doi")
    private String doi;
    @JsonProperty("title")
    private String title;
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<>();

    public PDBePublication() {
        this.authorList = new ArrayList<>();
    }

    /**
     *
     * @return The journalInfo
     */
    @JsonProperty("journal_info")
    public JournalInfo getJournalInfo() {
        return journalInfo;
    }

    /**
     *
     * @param journalInfo The journal_info
     */
    @JsonProperty("journal_info")
    public void setJournalInfo(JournalInfo journalInfo) {
        this.journalInfo = journalInfo;
    }

    /**
     *
     * @return The authorList
     */
    @JsonProperty("author_list")
    public List<AuthorList> getAuthorList() {
        return authorList;
    }

    /**
     *
     * @param authorList The author_list
     */
    @JsonProperty("author_list")
    public void setAuthorList(List<AuthorList> authorList) {
        this.authorList = authorList;
    }

    /**
     *
     * @return The pubmedId
     */
    @JsonProperty("pubmed_id")
    public String getPubmedId() {
        return pubmedId;
    }

    /**
     *
     * @param pubmedId The pubmed_id
     */
    @JsonProperty("pubmed_id")
    public void setPubmedId(String pubmedId) {
        this.pubmedId = pubmedId;
    }

    /**
     *
     * @return The type
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     *
     * @param type The type
     */
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return The associatedEntries
     */
    @JsonProperty("associated_entries")
    public Object getAssociatedEntries() {
        return associatedEntries;
    }

    /**
     *
     * @param associatedEntries The associated_entries
     */
    @JsonProperty("associated_entries")
    public void setAssociatedEntries(Object associatedEntries) {
        this.associatedEntries = associatedEntries;
    }

    /**
     *
     * @return The doi
     */
    @JsonProperty("doi")
    public String getDoi() {
        return doi;
    }

    /**
     *
     * @param doi The doi
     */
    @JsonProperty("doi")
    public void setDoi(String doi) {
        this.doi = doi;
    }

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
        return "PDBePublication{" + "journalInfo=" + journalInfo + ", authorList=" + authorList + ", pubmedId=" + pubmedId + ", type=" + type + ", doi=" + doi + ", title=" + title + '}';
    }
    
    
    
}
