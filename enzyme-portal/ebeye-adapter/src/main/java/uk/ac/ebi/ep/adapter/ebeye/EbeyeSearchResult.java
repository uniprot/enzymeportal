/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.adapter.ebeye;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author joseph
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EbeyeSearchResult {
    
    @JsonProperty("acc")
    private String acc;
    @JsonProperty("id")
    private String id;
    @JsonProperty("source")
    private String source;

    public String getAcc() {
        return acc;
    }

//    public String getId() {
//        return id.split("_")[0];
//    }
    
     public String getId() {
         return id;
     }

    public String getSource() {
        return source;
    }

//    @Override
//    public String toString() {
//        return "EbeyeSearchResult{" + "acc=" + acc + ", id=" + id + ", source=" + source + '}';
//    }
//    
    @Override
    public String toString() {
        return id;
    }
   
    
}
