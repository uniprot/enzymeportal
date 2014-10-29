/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.ebeye;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author joseph
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EbeyeSearchResult {
    
    @JsonProperty("entries")
    private List<UniProtDomain> uniProtDomains;
    



    public List<UniProtDomain> getUniProtDomains() {
        if(uniProtDomains == null){
            uniProtDomains = new ArrayList<>();
        }
        return uniProtDomains;
    }
    
    //TODO
//            @JsonProperty("entries")
//    private List<IntenzDomain> intenzDomain;
//        
//
//    public List<IntenzDomain> getIntenzDomain() {
//        return intenzDomain;
//    }
//    


    

    
}
