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

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "query",
    "resultType",
    "synonym",
    "page",
    "pageSize"
})
/**
 *
 * @author joseph
 */
public class Request {

    @JsonProperty("query")
    private String query;
    @JsonProperty("resultType")
    private String resultType;
    @JsonProperty("synonym")
    private Boolean synonym;
    @JsonProperty("page")
    private Integer page;
    @JsonProperty("pageSize")
    private Integer pageSize;
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<>();

    /**
     *
     * @return The query
     */
    @JsonProperty("query")
    public String getQuery() {
        return query;
    }

    /**
     *
     * @param query The query
     */
    @JsonProperty("query")
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     *
     * @return The resultType
     */
    @JsonProperty("resultType")
    public String getResultType() {
        return resultType;
    }

    /**
     *
     * @param resultType The resultType
     */
    @JsonProperty("resultType")
    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    /**
     *
     * @return The synonym
     */
    @JsonProperty("synonym")
    public Boolean getSynonym() {
        return synonym;
    }

    /**
     *
     * @param synonym The synonym
     */
    @JsonProperty("synonym")
    public void setSynonym(Boolean synonym) {
        this.synonym = synonym;
    }

    /**
     *
     * @return The page
     */
    @JsonProperty("page")
    public Integer getPage() {
        return page;
    }

    /**
     *
     * @param page The page
     */
    @JsonProperty("page")
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     *
     * @return The pageSize
     */
    @JsonProperty("pageSize")
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     *
     * @param pageSize The pageSize
     */
    @JsonProperty("pageSize")
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
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
