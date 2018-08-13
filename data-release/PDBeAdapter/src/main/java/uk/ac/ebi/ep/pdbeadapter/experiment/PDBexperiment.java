/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.pdbeadapter.experiment;

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
public class PDBexperiment {

    @JsonProperty("resolution_low")
    private Double resolutionLow;
    @JsonProperty("r_factor")
    private Double rFactor;
    @JsonProperty("r_free_selection_details")
    private String rFreeSelectionDetails;
    @JsonProperty("resolution_high")
    private Double resolutionHigh;
    @JsonProperty("r_free_percent_reflections")
    private Double rFreePercentReflections;
    @JsonProperty("starting_model")
    private Object startingModel;
    @JsonProperty("refinement_software")
    private String refinementSoftware;
    @JsonProperty("completeness")
    private Integer completeness;
    @JsonProperty("r_free")
    private Double rFree;
    @JsonProperty("percent_reflections_observed")
    private Double percentReflectionsObserved;
    @JsonProperty("experimental_method")
    private String experimentalMethod;
    @JsonProperty("num_reflections")
    private Integer numReflections;
    @JsonProperty("phasing_method")
    private Object phasingMethod;
    @JsonProperty("experiment_data_available")
    private String experimentDataAvailable;
    @JsonProperty("experimental_method_class")
    private String experimentalMethodClass;
    @JsonProperty("r_work")
    private Double rWork;
    @JsonProperty("spacegroup")
    private String spacegroup;
    @JsonProperty("resolution")
    private Double resolution;
    @JsonProperty("structure_determination_method")
    private String structureDeterminationMethod;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    /**
     *
     * @return The resolutionLow
     */
    @JsonProperty("resolution_low")
    public Double getResolutionLow() {
        return resolutionLow;
    }

    /**
     *
     * @param resolutionLow The resolution_low
     */
    @JsonProperty("resolution_low")
    public void setResolutionLow(Double resolutionLow) {
        this.resolutionLow = resolutionLow;
    }

    /**
     *
     * @return The rFactor
     */
    @JsonProperty("r_factor")
    public Double getRFactor() {
        return rFactor;
    }

    /**
     *
     * @param rFactor The r_factor
     */
    @JsonProperty("r_factor")
    public void setRFactor(Double rFactor) {
        this.rFactor = rFactor;
    }

    /**
     *
     * @return The rFreeSelectionDetails
     */
    @JsonProperty("r_free_selection_details")
    public String getRFreeSelectionDetails() {
        return rFreeSelectionDetails;
    }

    /**
     *
     * @param rFreeSelectionDetails The r_free_selection_details
     */
    @JsonProperty("r_free_selection_details")
    public void setRFreeSelectionDetails(String rFreeSelectionDetails) {
        this.rFreeSelectionDetails = rFreeSelectionDetails;
    }

    /**
     *
     * @return The resolutionHigh
     */
    @JsonProperty("resolution_high")
    public Double getResolutionHigh() {
        return resolutionHigh;
    }

    /**
     *
     * @param resolutionHigh The resolution_high
     */
    @JsonProperty("resolution_high")
    public void setResolutionHigh(Double resolutionHigh) {
        this.resolutionHigh = resolutionHigh;
    }

    /**
     *
     * @return The rFreePercentReflections
     */
    @JsonProperty("r_free_percent_reflections")
    public Double getRFreePercentReflections() {
        return rFreePercentReflections;
    }

    /**
     *
     * @param rFreePercentReflections The r_free_percent_reflections
     */
    @JsonProperty("r_free_percent_reflections")
    public void setRFreePercentReflections(Double rFreePercentReflections) {
        this.rFreePercentReflections = rFreePercentReflections;
    }

    /**
     *
     * @return The startingModel
     */
    @JsonProperty("starting_model")
    public Object getStartingModel() {
        return startingModel;
    }

    /**
     *
     * @param startingModel The starting_model
     */
    @JsonProperty("starting_model")
    public void setStartingModel(Object startingModel) {
        this.startingModel = startingModel;
    }

    /**
     *
     * @return The refinementSoftware
     */
    @JsonProperty("refinement_software")
    public String getRefinementSoftware() {
        return refinementSoftware;
    }

    /**
     *
     * @param refinementSoftware The refinement_software
     */
    @JsonProperty("refinement_software")
    public void setRefinementSoftware(String refinementSoftware) {
        this.refinementSoftware = refinementSoftware;
    }

    /**
     *
     * @return The completeness
     */
    @JsonProperty("completeness")
    public Integer getCompleteness() {
        return completeness;
    }

    /**
     *
     * @param completeness The completeness
     */
    @JsonProperty("completeness")
    public void setCompleteness(Integer completeness) {
        this.completeness = completeness;
    }

    /**
     *
     * @return The rFree
     */
    @JsonProperty("r_free")
    public Double getRFree() {
        return rFree;
    }

    /**
     *
     * @param rFree The r_free
     */
    @JsonProperty("r_free")
    public void setRFree(Double rFree) {
        this.rFree = rFree;
    }

    /**
     *
     * @return The percentReflectionsObserved
     */
    @JsonProperty("percent_reflections_observed")
    public Double getPercentReflectionsObserved() {
        return percentReflectionsObserved;
    }

    /**
     *
     * @param percentReflectionsObserved The percent_reflections_observed
     */
    @JsonProperty("percent_reflections_observed")
    public void setPercentReflectionsObserved(Double percentReflectionsObserved) {
        this.percentReflectionsObserved = percentReflectionsObserved;
    }

    /**
     *
     * @return The experimentalMethod
     */
    @JsonProperty("experimental_method")
    public String getExperimentalMethod() {
        return experimentalMethod;
    }

    /**
     *
     * @param experimentalMethod The experimental_method
     */
    @JsonProperty("experimental_method")
    public void setExperimentalMethod(String experimentalMethod) {
        this.experimentalMethod = experimentalMethod;
    }

    /**
     *
     * @return The numReflections
     */
    @JsonProperty("num_reflections")
    public Integer getNumReflections() {
        return numReflections;
    }

    /**
     *
     * @param numReflections The num_reflections
     */
    @JsonProperty("num_reflections")
    public void setNumReflections(Integer numReflections) {
        this.numReflections = numReflections;
    }

    /**
     *
     * @return The phasingMethod
     */
    @JsonProperty("phasing_method")
    public Object getPhasingMethod() {
        return phasingMethod;
    }

    /**
     *
     * @param phasingMethod The phasing_method
     */
    @JsonProperty("phasing_method")
    public void setPhasingMethod(Object phasingMethod) {
        this.phasingMethod = phasingMethod;
    }

    /**
     *
     * @return The experimentDataAvailable
     */
    @JsonProperty("experiment_data_available")
    public String getExperimentDataAvailable() {
        return experimentDataAvailable;
    }

    /**
     *
     * @param experimentDataAvailable The experiment_data_available
     */
    @JsonProperty("experiment_data_available")
    public void setExperimentDataAvailable(String experimentDataAvailable) {
        this.experimentDataAvailable = experimentDataAvailable;
    }

    /**
     *
     * @return The experimentalMethodClass
     */
    @JsonProperty("experimental_method_class")
    public String getExperimentalMethodClass() {
        return experimentalMethodClass;
    }

    /**
     *
     * @param experimentalMethodClass The experimental_method_class
     */
    @JsonProperty("experimental_method_class")
    public void setExperimentalMethodClass(String experimentalMethodClass) {
        this.experimentalMethodClass = experimentalMethodClass;
    }

    /**
     *
     * @return The rWork
     */
    @JsonProperty("r_work")
    public Double getRWork() {
        return rWork;
    }

    /**
     *
     * @param rWork The r_work
     */
    @JsonProperty("r_work")
    public void setRWork(Double rWork) {
        this.rWork = rWork;
    }

    /**
     *
     * @return The spacegroup
     */
    @JsonProperty("spacegroup")
    public String getSpacegroup() {
        return spacegroup;
    }

    /**
     *
     * @param spacegroup The spacegroup
     */
    @JsonProperty("spacegroup")
    public void setSpacegroup(String spacegroup) {
        this.spacegroup = spacegroup;
    }

    /**
     *
     * @return The resolution
     */
    @JsonProperty("resolution")
    public Double getResolution() {
        return resolution;
    }

    /**
     *
     * @param resolution The resolution
     */
    @JsonProperty("resolution")
    public void setResolution(Double resolution) {
        this.resolution = resolution;
    }

    /**
     *
     * @return The structureDeterminationMethod
     */
    @JsonProperty("structure_determination_method")
    public String getStructureDeterminationMethod() {
        return structureDeterminationMethod;
    }

    /**
     *
     * @param structureDeterminationMethod The structure_determination_method
     */
    @JsonProperty("structure_determination_method")
    public void setStructureDeterminationMethod(String structureDeterminationMethod) {
        this.structureDeterminationMethod = structureDeterminationMethod;
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
        return "PDBexperiment{" + "rFactor=" + rFactor + ", rFree=" + rFree + ", experimentalMethod=" + experimentalMethod + '}';
    }


  
}
