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
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author joseph
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AuthorList {

    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("initials")
    private String initials;
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<>();

    /**
     *
     * @return The lastName
     */
    @JsonProperty("last_name")
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * @param lastName The last_name
     */
    @JsonProperty("last_name")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     *
     * @return The fullName
     */
    @JsonProperty("full_name")
    public String getFullName() {
        return fullName;
    }

    /**
     *
     * @param fullName The full_name
     */
    @JsonProperty("full_name")
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     *
     * @return The initials
     */
    @JsonProperty("initials")
    public String getInitials() {
        return initials;
    }

    /**
     *
     * @param initials The initials
     */
    @JsonProperty("initials")
    public void setInitials(String initials) {
        this.initials = initials;
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
