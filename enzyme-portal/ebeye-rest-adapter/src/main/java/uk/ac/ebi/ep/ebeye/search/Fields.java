/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author joseph
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Fields {

    @JsonProperty("name")
    private List<String> name;
    @JsonProperty("scientific_name")
    private List<String> scientificName = new ArrayList<>();

    public List<String> getName() {
        return name.stream().distinct().collect(Collectors.toList());
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    /**
     *
     * @return The scientificName
     */
    @JsonProperty("scientific_name")
    public List<String> getScientificName() {
        return scientificName;
    }

    /**
     *
     * @param scientificName The scientific_name
     */
    @JsonProperty("scientific_name")
    public void setScientificName(List<String> scientificName) {
        this.scientificName = scientificName;
    }

}
