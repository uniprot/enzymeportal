/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class EnzymeEntry implements Serializable {

    private static final long serialVersionUID = 1L;
    @JsonProperty("id")
    private String ec;

    public EnzymeEntry() {
    }

    public EnzymeEntry(String ec) {
        this.ec = ec;
    }

    public String getEc() {
        return ec;
    }

    public void setEc(String ec) {
        this.ec = ec;
    }

}
