/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye.protein.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class ProteinView {

    private String title;
    private List<String> scientificName;
    protected Protein protein;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getScientificName() {
        if (scientificName == null) {
            scientificName = new ArrayList<>();
        }
        return scientificName;
    }

    public void setScientificName(List<String> scientificName) {
        this.scientificName = scientificName;
    }

    public Protein getProtein() {
        return protein;
    }

    public void setProtein(Protein protein) {
        this.protein = protein;
    }

}
