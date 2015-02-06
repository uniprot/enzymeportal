/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.pdbeadapter;

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
    "water",
    "polypeptide",
    "other",
    "dna",
    "ligand",
    "dna/rna",
    "rna",
    "sugar"
})

public class NumberOfEntities {

    @JsonProperty("water")
    private Integer water;
    @JsonProperty("polypeptide")
    private Integer polypeptide;
    @JsonProperty("other")
    private Integer other;
    @JsonProperty("dna")
    private Integer dna;
    @JsonProperty("ligand")
    private Integer ligand;
    @JsonProperty("dna/rna")
    private Integer dnaRna;
    @JsonProperty("rna")
    private Integer rna;
    @JsonProperty("sugar")
    private Integer sugar;
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<>();

    /**
     *
     * @return The water
     */
    @JsonProperty("water")
    public Integer getWater() {
        return water;
    }

    /**
     *
     * @param water The water
     */
    @JsonProperty("water")
    public void setWater(Integer water) {
        this.water = water;
    }

    /**
     *
     * @return The polypeptide
     */
    @JsonProperty("polypeptide")
    public Integer getPolypeptide() {
        return polypeptide;
    }

    /**
     *
     * @param polypeptide The polypeptide
     */
    @JsonProperty("polypeptide")
    public void setPolypeptide(Integer polypeptide) {
        this.polypeptide = polypeptide;
    }

    /**
     *
     * @return The other
     */
    @JsonProperty("other")
    public Integer getOther() {
        return other;
    }

    /**
     *
     * @param other The other
     */
    @JsonProperty("other")
    public void setOther(Integer other) {
        this.other = other;
    }

    /**
     *
     * @return The dna
     */
    @JsonProperty("dna")
    public Integer getDna() {
        return dna;
    }

    /**
     *
     * @param dna The dna
     */
    @JsonProperty("dna")
    public void setDna(Integer dna) {
        this.dna = dna;
    }

    /**
     *
     * @return The ligand
     */
    @JsonProperty("ligand")
    public Integer getLigand() {
        return ligand;
    }

    /**
     *
     * @param ligand The ligand
     */
    @JsonProperty("ligand")
    public void setLigand(Integer ligand) {
        this.ligand = ligand;
    }

    /**
     *
     * @return The dnaRna
     */
    @JsonProperty("dna/rna")
    public Integer getDnaRna() {
        return dnaRna;
    }

    /**
     *
     * @param dnaRna The dna/rna
     */
    @JsonProperty("dna/rna")
    public void setDnaRna(Integer dnaRna) {
        this.dnaRna = dnaRna;
    }

    /**
     *
     * @return The rna
     */
    @JsonProperty("rna")
    public Integer getRna() {
        return rna;
    }

    /**
     *
     * @param rna The rna
     */
    @JsonProperty("rna")
    public void setRna(Integer rna) {
        this.rna = rna;
    }

    /**
     *
     * @return The sugar
     */
    @JsonProperty("sugar")
    public Integer getSugar() {
        return sugar;
    }

    /**
     *
     * @param sugar The sugar
     */
    @JsonProperty("sugar")
    public void setSugar(Integer sugar) {
        this.sugar = sugar;
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
