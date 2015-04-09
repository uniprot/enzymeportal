/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.pdbeadapter.publication;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 *
 * @author joseph
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AuthorList implements Comparable<AuthorList>{

    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("initials")
    private String initials;

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


    @Override
    public int compareTo(AuthorList o) {
        return this.fullName.compareToIgnoreCase(o.getFullName());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.fullName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AuthorList other = (AuthorList) obj;
        return Objects.equals(this.fullName, other.fullName);
    }

    
    
}
