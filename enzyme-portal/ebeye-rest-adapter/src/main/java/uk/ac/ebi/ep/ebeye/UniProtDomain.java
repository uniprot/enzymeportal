/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 *Represents the result object from the domain(uniprot) in Ebeye search
 * @author joseph
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UniProtDomain extends Domain  implements Comparable<UniProtDomain> {

    @JsonProperty("acc")
    private String uniprot_accession;
    @JsonProperty("id")
    private String uniprot_name;
    @JsonProperty("source")
    private String source;

       public String getUniport_name() {
        //return uniprot_name.split("_")[0];
           return uniprot_name.substring(0, uniprot_name.indexOf("_"));
           //return uniprot_name;
    }
    public String getUniprot_accession() {
        return uniprot_accession;
    }


    public String getSource() {
        return source;
    }

    @Override
    public String toString() {
        return "UniProtDomain{" + "uniprot_accession=" + uniprot_accession + ", uniport_name=" + uniprot_name + ", source=" + source + '}';
    }
    
//    @Override
//    public int hashCode() {
//        int hash = 7;
//        hash = 83 * hash + Objects.hashCode(this.uniprot_accession);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final UniProtDomain other = (UniProtDomain) obj;
//        if (!Objects.equals(this.uniprot_accession, other.uniprot_accession)) {
//            return false;
//        }
//        return true;
//    }
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.getUniport_name());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UniProtDomain other = (UniProtDomain) obj;
        if (!Objects.equals(this.getUniport_name(), other.getUniport_name())) {
            return false;
        }
        return true;
    }
    
    
    

//        @Override
//    public int compareTo(UniProtDomain obj) {
//       int compare = obj.uniprot_accession.compareToIgnoreCase(this.uniprot_accession);
//         return ((compare == 0) ? obj.uniprot_accession.compareToIgnoreCase(this.uniprot_accession) : compare);
//      
//
//    }
    
            @Override
    public int compareTo(UniProtDomain obj) {
       int compare = obj.getUniport_name().compareToIgnoreCase(this.getUniport_name());
         return ((compare == 0) ? obj.getUniport_name().compareToIgnoreCase(this.getUniport_name()) : compare);
      

    }
    
}
