/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.literatureservice.config;

/**
 *
 * @author joseph
 */
public class PmcServiceUrl {
    
    private String specificUrl;
    private String genericUrl;

    public String getSpecificUrl() {
        return specificUrl;
    }

    public void setSpecificUrl(String specificUrl) {
        this.specificUrl = specificUrl;
    }

    public String getGenericUrl() {
        return genericUrl;
    }

    public void setGenericUrl(String genericUrl) {
        this.genericUrl = genericUrl;
    }
    
}
