/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.search.model;

import org.springframework.util.StringUtils;

/**
 * Taxonomy is a data transfer object for model organisms
 *
 * @author joseph
 */
public class Taxonomy {

    private Long taxId;
    private String scientificName;
    private String commonName;
    private Long num_enzymes;

    public Taxonomy(Long taxId, String scientificName, String commonName) {
        this.taxId = taxId;
        this.scientificName = scientificName;
        this.commonName = commonName;
    }

    public Taxonomy(Long taxId, String scientificName, String commonName, Long num_enzymes) {
        this.taxId = taxId;
        this.scientificName = scientificName;
        this.commonName = commonName;
        this.num_enzymes = num_enzymes;
    }

    public Long getTaxId() {
        return taxId;
    }

    public void setTaxId(Long taxId) {
        this.taxId = taxId;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getCommonName() {
        if (StringUtils.isEmpty(commonName) && "Escherichia coli (strain K12)".equalsIgnoreCase(scientificName)) {
            commonName = "E.Coli";
        }

        if (StringUtils.isEmpty(commonName) && "Bacillus subtilis (strain 168)".equalsIgnoreCase(scientificName)) {
            commonName = "Bacillus subtilis";
        }

        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public Long getNum_enzymes() {
        return num_enzymes;
    }

    public void setNum_enzymes(Long num_enzymes) {
        this.num_enzymes = num_enzymes;
    }

    @Override
    public String toString() {
        return "Taxonomy{" + "taxId=" + taxId + ", scientificName=" + scientificName + ", commonName=" + commonName + ", num_enzymes=" + num_enzymes + '}';
    }

}
