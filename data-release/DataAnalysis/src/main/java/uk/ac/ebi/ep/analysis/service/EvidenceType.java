/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.analysis.service;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public enum EvidenceType {

    FUNCTION("FUNCTION"),
    ENZYME_REGULATION("ENZYME_REGULATION"),
    CATALYTIC_ACTIVITY("CATALYTIC_ACTIVITY"),
    COFACTOR("COFACTOR"),
    BIOPHYSICOCHEMICAL_PROPERTIES("BIOPHYSICOCHEMICAL_PROPERTIES");

    private final String evidenceName;

    private EvidenceType(String evidenceName) {
        this.evidenceName = evidenceName;
    }

    public String getEvidenceName() {
        return evidenceName;
    }

}
