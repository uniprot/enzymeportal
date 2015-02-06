/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.pdbeadapter;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "experimental_method",
    "assemblies",
    "title",
    "release_date",
    "split_entry",
    "experimental_method_class",
    "revision_date",
    "entry_authors",
    "deposition_site",
    "number_of_entities",
    "deposition_date",
    "processing_site"
})
public class PDBe {

    @JsonProperty("experimental_method")
    private List<String> experimentalMethod = new ArrayList<>();
    @JsonProperty("assemblies")
    private List<Assembly> assemblies = new ArrayList<>();
    @JsonProperty("title")
    private String title;
    @JsonProperty("release_date")
    private String releaseDate;
    @JsonProperty("split_entry")
    private Object splitEntry;
    @JsonProperty("experimental_method_class")
    private List<String> experimentalMethodClass = new ArrayList<>();
    @JsonProperty("revision_date")
    private String revisionDate;
    @JsonProperty("entry_authors")
    private List<String> entryAuthors = new ArrayList<>();
    @JsonProperty("deposition_site")
    private String depositionSite;
    @JsonProperty("number_of_entities")
    private NumberOfEntities numberOfEntities;
    @JsonProperty("deposition_date")
    private String depositionDate;
    @JsonProperty("processing_site")
    private String processingSite;
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<>();

    public String name;

    public PDBe() {
    }

    @JsonCreator
    public PDBe(String name) {
        this.name = name;
    }
    
    
    /**
     *
     * @return The experimentalMethod
     */
    @JsonProperty("experimental_method")
    public List<String> getExperimentalMethod() {
        return experimentalMethod;
    }

    /**
     *
     * @param experimentalMethod The experimental_method
     */
    @JsonProperty("experimental_method")
    public void setExperimentalMethod(List<String> experimentalMethod) {
        this.experimentalMethod = experimentalMethod;
    }

    /**
     *
     * @return The assemblies
     */
    @JsonProperty("assemblies")
    public List<Assembly> getAssemblies() {
        return assemblies;
    }

    /**
     *
     * @param assemblies The assemblies
     */
    @JsonProperty("assemblies")
    public void setAssemblies(List<Assembly> assemblies) {
        this.assemblies = assemblies;
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

    /**
     *
     * @return The releaseDate
     */
    @JsonProperty("release_date")
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     *
     * @param releaseDate The release_date
     */
    @JsonProperty("release_date")
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     *
     * @return The splitEntry
     */
    @JsonProperty("split_entry")
    public Object getSplitEntry() {
        return splitEntry;
    }

    /**
     *
     * @param splitEntry The split_entry
     */
    @JsonProperty("split_entry")
    public void setSplitEntry(Object splitEntry) {
        this.splitEntry = splitEntry;
    }

    /**
     *
     * @return The experimentalMethodClass
     */
    @JsonProperty("experimental_method_class")
    public List<String> getExperimentalMethodClass() {
        return experimentalMethodClass;
    }

    /**
     *
     * @param experimentalMethodClass The experimental_method_class
     */
    @JsonProperty("experimental_method_class")
    public void setExperimentalMethodClass(List<String> experimentalMethodClass) {
        this.experimentalMethodClass = experimentalMethodClass;
    }

    /**
     *
     * @return The revisionDate
     */
    @JsonProperty("revision_date")
    public String getRevisionDate() {
        return revisionDate;
    }

    /**
     *
     * @param revisionDate The revision_date
     */
    @JsonProperty("revision_date")
    public void setRevisionDate(String revisionDate) {
        this.revisionDate = revisionDate;
    }

    /**
     *
     * @return The entryAuthors
     */
    @JsonProperty("entry_authors")
    public List<String> getEntryAuthors() {
        return entryAuthors;
    }

    /**
     *
     * @param entryAuthors The entry_authors
     */
    @JsonProperty("entry_authors")
    public void setEntryAuthors(List<String> entryAuthors) {
        this.entryAuthors = entryAuthors;
    }

    /**
     *
     * @return The depositionSite
     */
    @JsonProperty("deposition_site")
    public String getDepositionSite() {
        return depositionSite;
    }

    /**
     *
     * @param depositionSite The deposition_site
     */
    @JsonProperty("deposition_site")
    public void setDepositionSite(String depositionSite) {
        this.depositionSite = depositionSite;
    }

    /**
     *
     * @return The numberOfEntities
     */
    @JsonProperty("number_of_entities")
    public NumberOfEntities getNumberOfEntities() {
        return numberOfEntities;
    }

    /**
     *
     * @param numberOfEntities The number_of_entities
     */
    @JsonProperty("number_of_entities")
    public void setNumberOfEntities(NumberOfEntities numberOfEntities) {
        this.numberOfEntities = numberOfEntities;
    }

    /**
     *
     * @return The depositionDate
     */
    @JsonProperty("deposition_date")
    public String getDepositionDate() {
        return depositionDate;
    }

    /**
     *
     * @param depositionDate The deposition_date
     */
    @JsonProperty("deposition_date")
    public void setDepositionDate(String depositionDate) {
        this.depositionDate = depositionDate;
    }

    /**
     *
     * @return The processingSite
     */
    @JsonProperty("processing_site")
    public String getProcessingSite() {
        return processingSite;
    }

    /**
     *
     * @param processingSite The processing_site
     */
    @JsonProperty("processing_site")
    public void setProcessingSite(String processingSite) {
        this.processingSite = processingSite;
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
        return "PDBe{" + "experimentalMethod=" + experimentalMethod + ", assemblies=" + assemblies + ", title=" + title + ", releaseDate=" + releaseDate + ", splitEntry=" + splitEntry + ", experimentalMethodClass=" + experimentalMethodClass + ", revisionDate=" + revisionDate + ", entryAuthors=" + entryAuthors + ", depositionSite=" + depositionSite + ", numberOfEntities=" + numberOfEntities + ", depositionDate=" + depositionDate + ", processingSite=" + processingSite + ", additionalProperties=" + additionalProperties + '}';
    }

}
