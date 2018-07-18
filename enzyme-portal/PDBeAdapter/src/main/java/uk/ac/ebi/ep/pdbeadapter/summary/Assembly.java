/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.pdbeadapter.summary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 *
 * @author joseph
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "assembly_id",
    "form",
    "preferred",
    "name"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Assembly {

    @JsonProperty("assembly_id")
    private String assemblyId;
    @JsonProperty("form")
    private String form;
    @JsonProperty("preferred")
    private Boolean preferred;
    @JsonProperty("name")
    private String name;

    @JsonProperty("assembly_id")
    public String getAssemblyId() {
        return assemblyId;
    }

    @JsonProperty("assembly_id")
    public void setAssemblyId(String assemblyId) {
        this.assemblyId = assemblyId;
    }

    public Assembly withAssemblyId(String assemblyId) {
        this.assemblyId = assemblyId;
        return this;
    }

    @JsonProperty("form")
    public String getForm() {
        return form;
    }

    @JsonProperty("form")
    public void setForm(String form) {
        this.form = form;
    }

    public Assembly withForm(String form) {
        this.form = form;
        return this;
    }

    @JsonProperty("preferred")
    public Boolean getPreferred() {
        return preferred;
    }

    @JsonProperty("preferred")
    public void setPreferred(Boolean preferred) {
        this.preferred = preferred;
    }

    public Assembly withPreferred(Boolean preferred) {
        this.preferred = preferred;
        return this;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public Assembly withName(String name) {
        this.name = name;
        return this;
    }

}
