/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents the result object from Enzyme Portal domain in Ebeye Search Index
 *
 * @author joseph
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Entry extends Domain implements Comparable<Entry> {

    @JsonProperty("acc")
    private String uniprot_accession;
    @JsonProperty("id")
    private String uniprot_name;
    @JsonProperty("source")
    private String source;
    
    private String title;

    public Entry() {
     
       
    }

    public Entry(String uniprot_accession, String uniprot_name) {
        this.uniprot_accession = uniprot_accession;
        this.uniprot_name = uniprot_name;
    }

//    @JsonIgnore
//    private final Map<String, Object> additionalProperties = new HashMap<>();
    /**
     *
     * @return The uniprot name
     */
    public String getUniprot_name() {
        return uniprot_name.substring(0, uniprot_name.indexOf("_"));
        //return uniprot_name;
    }

    /**
     *
     * @return uniprot accession
     */
    public String getUniprot_accession() {
        return uniprot_accession;
    }

    /**
     *
     * @return source data index
     */
    public String getSource() {
        return source;
    }

//    @JsonAnyGetter
//    public Map<String, Object> getAdditionalProperties() {
//        return this.additionalProperties;
//    }
//
//    @JsonAnySetter
//    public void setAdditionalProperty(String name, Object value) {
//        this.additionalProperties.put(name, value);
//    }
    @Override
    public String toString() {
        return "Entry{" + "uniprot_accession=" + uniprot_accession + ", uniport_name=" + uniprot_name + ", source=" + source + '}';
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
//        final Entry other = (Entry) obj;
//        if (!Objects.equals(this.uniprot_accession, other.uniprot_accession)) {
//            return false;
//        }
//        return true;
//    }
//    
//    @Override
//    public int hashCode() {
//        int hash = 7;
//        hash = 53 * hash + Objects.hashCode(this.getUniprot_name());
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
//        final Entry other = (Entry) obj;
//        if (!Objects.equals(this.getUniprot_name(), other.getUniprot_name())) {
//            return false;
//        }
//        return true;
//    }
    

//        @Override
//    public int compareTo(Entry obj) {
//       int compare = obj.uniprot_accession.compareToIgnoreCase(this.uniprot_accession);
//         return ((compare == 0) ? obj.uniprot_accession.compareToIgnoreCase(this.uniprot_accession) : compare);
//      
//
//    }
    @Override
    public int compareTo(Entry obj) {
        int compare = obj.getUniprot_name().compareToIgnoreCase(this.getUniprot_name());
        return ((compare == 0) ? obj.getUniprot_name().compareToIgnoreCase(this.getUniprot_name()) : compare);

    }

    @JsonIgnore
    private final Map<String, Fields> fields = new HashMap<>();

    public Fields get(String name) {
        return fields.get(name);
    }

    @JsonAnyGetter
    public Map<String, Fields> any() {
        return fields;
    }

    @JsonAnySetter
    public void set(String name, Fields value) {
        fields.put(name, value);
    }

    public String getTitle() {
        if(get("fields") != null){
        title = get("fields").getName().stream().findFirst().get(); 
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.getTitle());
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
        final Entry other = (Entry) obj;
        if (!Objects.equals(this.getTitle(), other.getTitle())) {
            return false;
        }
        return true;
    }
    
    
    
    
}
