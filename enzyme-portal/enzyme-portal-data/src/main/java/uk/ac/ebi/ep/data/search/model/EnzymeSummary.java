/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.search.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import uk.ac.ebi.ep.data.domain.UniprotEntry;

/**
 *
 * @author joseph
 */
public class EnzymeSummary extends EnzymeAccession  implements Comparable<EnzymeSummary>, Serializable {

    protected List<String> ec;

    private String accession;

    protected String name;
    protected String function;
    protected List<String> synonym;

    protected String uniprotid;
    protected List<EnzymeAccession> relatedspecies;

    private String commentType;
    private String commentText;
    private UniprotEntry uniprotEntry;
    private List<String> pdbId;

    public EnzymeSummary() {
    }

    public EnzymeSummary(UniprotEntry uniprotEntry) {
        this.uniprotEntry = uniprotEntry;
    }


    
//
//    public EnzymeSummary(String commentType, String commentText, UniprotEntry uniprotEntry) {
//
//        this.accession = uniprotEntry.getAccession();
//        this.name = uniprotEntry.getProteinName();
//
//        this.uniprotid = uniprotEntry.getName();
//
//        this.commentType = commentType;
//        this.commentText = commentText;
//        this.uniprotEntry = uniprotEntry;
//
//        computeFunction(commentType, commentText);
//        computePDBcodes(uniprotEntry);
//        computeSynomns(uniprotEntry);
//        
//
//    }
//
//    public EnzymeSummary(String accession, String name, String uniprotid, String commentType, String commentText, UniprotEntry uniprotEntry) {
//        this.accession = accession;
//        this.name = name;
//
//        this.uniprotid = uniprotid;
//
//        this.commentType = commentType;
//        this.commentText = commentText;
//        this.uniprotEntry = uniprotEntry;
//        //computeEc(commentType, commentText);
//
//    }
//
////    public EnzymeSummary(String ecNumber, String accession, String name, String function, String syn, String uniprotid) {
////
////        ec = new ArrayList<>();
////        ec.add(ecNumber);
////
////        this.accession = accession;
////        this.name = name;
////        this.function = function;
////
////        this.synonym = new ArrayList<>();
////        synonym.add(syn);
////
////        this.uniprotid = uniprotid;
////    }
//    public String getCommentType() {
//        return commentType;
//    }
//
//    public String getCommentText() {
//        return commentText;
//    }
//
    public UniprotEntry getUniprotEntry() {
        return uniprotEntry;
    }
//
//    private void computeEc(String commentType, String commentText) {
//        if (ec == null) {
//            ec = new ArrayList<>();
//        }
//
//        if (commentType.equalsIgnoreCase("EC_NUMBER")) {
//
//            ec.add(commentText);
//
//        }
//    }
//
////    public List<String> getEc() {
////        if (ec == null) {
////            ec = new ArrayList<>();
////        }
////        System.out.println("comment type " + commentType);
////        if (this.commentType.equalsIgnoreCase("EC_NUMBER")) {
////
////            ec.add(this.commentText);
////
////        }
////
////        return this.ec;
////    }
////    public String getFunction() {
////        if (this.commentType.equalsIgnoreCase("FUNCTION")) {
////
////            function = this.commentText;
////        }
////
////        return function;
////    }
//    private void computeFunction(String commentType, String commentText) {
//        if (commentType.equalsIgnoreCase("FUNCTION")) {
//
//            function = commentText;
//        }
//    }
//    
//     private void computePDBcodes(UniprotEntry e){
//         if(pdbId != null){
//             pdbId = new ArrayList<>();
//         }   
//         
//         e.getUniprotXrefSet().stream().filter((x) -> (x.getSource().equalsIgnoreCase("PDB"))).limit(2).collect(Collectors.toList()).stream().forEach((xref) -> {
//            pdbId.add(xref.getSourceId());
//        });
// 
//     }
//
//    public List<String> getPdbId() {
//            if(pdbId != null){
//             pdbId = new ArrayList<>();
//         }  
//        
//        return pdbId;
//    }
//     
//     
//
////    @Override
////    public List<String> getPdbeaccession() {
////
////        //return getPdbCodes(this.uniprotEntry);
////        return pdbeaccession;
////    }
//
////    private List<String> getPdbCodes(UniprotEntry e) {
////        List<String> pdbcodes = new ArrayList<>();
////
////        e.getUniprotXrefSet().stream().filter((x) -> (x.getSource().equalsIgnoreCase("PDB"))).limit(2).collect(Collectors.toList()).stream().forEach((xref) -> {
////            pdbcodes.add(xref.getSourceId());
////        });
////
////        return pdbcodes;
////
////    }
//    
//     private void computeSynomns( UniprotEntry e){
//               if (synonym == null) {
//            synonym = new ArrayList<>();
//        }
//
//        String namesColumn = e.getSynonymNames();
//
//        if (namesColumn != null && namesColumn.contains(";")) {
//            String[] syn = namesColumn.split(";");
//            for (String x : syn) {
//
//                synonym.addAll(parseNameSynonyms(x));
//            }
//        } 
//     }
//
////    public List<String> getSynonym() {
////        if (synonym == null) {
////            synonym = new ArrayList<>();
////        }
////
////        String namesColumn = this.uniprotEntry.getSynonymNames();
////
////        if (namesColumn != null && namesColumn.contains(";")) {
////            String[] syn = namesColumn.split(";");
////            for (String x : syn) {
////
////                synonym.addAll(parseNameSynonyms(x));
////            }
////        }
////
////        return this.synonym;
////        
////    }
//
//    private List<String> parseNameSynonyms(String namesColumn) {
//        List<String> nameSynonyms = new ArrayList<>();
//        if (namesColumn != null) {
//            final int sepIndex = namesColumn.indexOf(" (");
//
//            //System.out.println("syn index "+ sepIndex);
//            if (sepIndex == -1) {
//                // no synonyms, just recommended name:
//
//                nameSynonyms.add(namesColumn);
//            } else {
//                // Recommended name:
//                nameSynonyms.add(namesColumn.substring(0, sepIndex));
//                // take out starting and ending parentheses
//                String[] synonyms = namesColumn.substring(sepIndex + 2, namesColumn.length() - 1).split("\\) \\(");
//                nameSynonyms.addAll(Arrays.asList(synonyms));
//            }
//            return nameSynonyms.stream().distinct().collect(Collectors.toList());
//        }
//        return nameSynonyms;
//    }

    /**
     * Gets the value of the ec property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the ec property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEc().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link String }
     *
     *
     * @return
     */
    public List<String> getEc() {
        if (ec == null) {
            ec = new ArrayList<>();
        }
        return this.ec;
    }

    /**
     * Gets the value of the name property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getName() {
        return name;
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the function property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getFunction() {
        return function;
    }

    /**
     * Sets the value of the function property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setFunction(String value) {
        this.function = value;
    }

    /**
     * Gets the value of the synonym property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the synonym property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSynonym().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link String }
     *
     *
     * @return
     */
    public List<String> getSynonym() {
        if (synonym == null) {
            synonym = new ArrayList<>();
        }
        return this.synonym;
        //return synonym;
            }
    /**
     * Sets the value of the synonym property.
     *
     * @param synonym allowed object is {@link String }
     *
     */
    public void setSynonym(List<String> synonym) {
        this.synonym = synonym;
    }

    
//    public String getSynonyms() {
//        return this.synonyms;
//    }
//
//    public void setSynonyms(String synonyms) {
//        this.synonyms = synonyms;
//    }
//    
    /**
     * Gets the value of the uniprotid property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getUniprotid() {
        return uniprotid;
    }

    /**
     * Sets the value of the uniprotid property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setUniprotid(String value) {
        this.uniprotid = value;
    }

    /**
     * Gets the value of the relatedspecies property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the relatedspecies property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRelatedspecies().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EnzymeAccession }
     *
     *
     * @return
     */
    public List<EnzymeAccession> getRelatedspecies() {
        if (relatedspecies == null) {
            relatedspecies = new ArrayList<>();
        }
        return this.relatedspecies;
    }

    /**
     * Sets the value of the relatedspecies property.
     *
     * @param relatedspecies allowed object is {@link EnzymeAccession }
     *
     */
    public void setRelatedspecies(List<EnzymeAccession> relatedspecies) {
        this.relatedspecies = relatedspecies;
    }

    public EnzymeSummary withEc(String... values) {
        if (values != null) {
            getEc().addAll(Arrays.asList(values));
        }
        return this;
    }

    public EnzymeSummary withEc(Collection<String> values) {
        if (values != null) {
            getEc().addAll(values);
        }
        return this;
    }

    public EnzymeSummary withName(String value) {
        setName(value);
        return this;
    }

    public EnzymeSummary withFunction(String value) {
        setFunction(value);
        return this;
    }

    public EnzymeSummary withSynonym(String... values) {
        if (values != null) {
            for (String value : values) {
                getSynonym().add(value);
            }
        }
        return this;
    }

    public EnzymeSummary withSynonym(Collection<String> values) {
        if (values != null) {
            getSynonym().addAll(values);
        }
        return this;
    }

    public EnzymeSummary withUniprotid(String value) {
        setUniprotid(value);
        return this;
    }

    public EnzymeSummary withRelatedspecies(EnzymeAccession... values) {
        if (values != null) {
            getRelatedspecies().addAll(Arrays.asList(values));
        }
        return this;
    }

    public EnzymeSummary withRelatedspecies(Collection<EnzymeAccession> values) {
        if (values != null) {
            getRelatedspecies().addAll(values);
        }
        return this;
    }

    @Override
    public EnzymeSummary withUniprotaccessions(String... values) {
        if (values != null) {
            getUniprotaccessions().addAll(Arrays.asList(values));
        }
        return this;
    }

    @Override
    public EnzymeSummary withUniprotaccessions(Collection<String> values) {
        if (values != null) {
            getUniprotaccessions().addAll(values);
        }
        return this;
    }

    @Override
    public EnzymeSummary withSpecies(Species value) {
        setSpecies(value);
        return this;
    }

    @Override
    public EnzymeSummary withPdbeaccession(String... values) {
        if (values != null) {
            getPdbeaccession().addAll(Arrays.asList(values));
        }
        return this;
    }

    @Override
    public EnzymeSummary withPdbeaccession(Collection<String> values) {
        if (values != null) {
            getPdbeaccession().addAll(values);
        }
        return this;
    }

    @Override
    public EnzymeSummary withCompounds(Compound... values) {
        if (values != null) {
            getCompounds().addAll(Arrays.asList(values));
        }
        return this;
    }

    @Override
    public EnzymeSummary withCompounds(Collection<Compound> values) {
        if (values != null) {
            getCompounds().addAll(values);
        }
        return this;
    }

    @Override
    public EnzymeSummary withDiseases(Disease... values) {
        if (values != null) {
            getDiseases().addAll(Arrays.asList(values));
        }
        return this;
    }

    @Override
    public EnzymeSummary withDiseases(Collection<Disease> values) {
        if (values != null) {
            getDiseases().addAll(values);
        }
        return this;
    }

    @Override
    public EnzymeSummary withScoring(Object value) {
        setScoring(value);
        return this;
    }

    /**
     * Sets the value of the ec property.
     *
     * @param ec allowed object is {@link String }
     *
     */
    public void setEc(List<String> ec) {
        this.ec = ec;
    }



    @Override
    public String toString() {
        return "EnzymeSummary{" + "name=" + name + ", function=" + function + ", synonym=" + synonym + ", uniprotid=" + uniprotid + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.name);
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
        final EnzymeSummary other = (EnzymeSummary) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(EnzymeSummary o) {
        if (super.scoring != null) {
            return super.scoring.toString().compareTo(o.scoring.toString());
        }
        return this.name.compareToIgnoreCase(o.getName());
    }

//    
//    
//        @Override
//    public List<String> getSynonym() {
//        if (synonym == null) {
//            synonym = new ArrayList<>();
//        }
//
//        String namesColumn = this.getUniprotAccession().getSynonymNames();
//
//        if (namesColumn != null && namesColumn.contains(";")) {
//            String[] syn = namesColumn.split(";");
//            for (String x : syn) {
//
//                synonym.addAll(parseNameSynonyms(x));
//            }
//        }
//
//        return this.synonym;
//        //return synonym;
//    }
//    
//        @Override
//    public List<String> getPdbeaccession() {
//
//        return getPdbCodes(this.getUniprotAccession());
//    }
//
//    private List<String> getPdbCodes(UniprotEntry e) {
//        List<String> pdbcodes = new ArrayList<>();
//        
////        e.getUniprotXrefSet().stream().filter((xref) -> (xref.getSource().equalsIgnoreCase("PDB"))).forEach((xref) -> {
////            pdbcodes.add(xref.getSourceId());
////        });
//        
//        
//        
//        e.getUniprotXrefSet().stream().filter((x)->(x.getSource().equalsIgnoreCase("PDB"))).limit(2).collect(Collectors.toList()).stream().forEach((xref) -> {
//            pdbcodes.add(xref.getSourceId());
//        });
//        
//        
//        return pdbcodes;
//        
//
//    }
//
//    private List<String> parseNameSynonyms(String namesColumn) {
//        List<String> nameSynonyms = new ArrayList<>();
//        if (namesColumn != null) {
//            final int sepIndex = namesColumn.indexOf(" (");
//
//            //System.out.println("syn index "+ sepIndex);
//            if (sepIndex == -1) {
//                // no synonyms, just recommended name:
//
//                nameSynonyms.add(namesColumn);
//            } else {
//                // Recommended name:
//                nameSynonyms.add(namesColumn.substring(0, sepIndex));
//                // take out starting and ending parentheses
//                String[] synonyms = namesColumn.substring(sepIndex + 2, namesColumn.length() - 1).split("\\) \\(");
//                nameSynonyms.addAll(Arrays.asList(synonyms));
//            }
//            return nameSynonyms.stream().distinct().collect(Collectors.toList());
//        }
//        return nameSynonyms;
//    }
//
//    @Override
//    public String getName() {
//        return this.uniprotAccession.getProteinName();
//    }
//
//    @Override
//    public String getAccession() {
//        return this.uniprotAccession.getAccession();
//    }
//
//    @Override
//    public String getUniprotid() {
//        return this.uniprotAccession.getName();
//    }
//
//    @Override
//    public List<String> getEc() {
//        if (ec == null) {
//            ec = new ArrayList<>();
//        }
//
//        if (this.getCommentType().equalsIgnoreCase("EC_NUMBER")) {
//
//            ec.add(this.getCommentText());
//
//        }
//
//        return this.ec;
//    }
//
//    @Override
//    public String getFunction() {
//        if (this.getCommentType().equalsIgnoreCase("FUNCTION")) {
//
//            function = this.getCommentText();
//        }
//
//        return function;
//    }
//
//    @Override
//    public Species getSpecies() {
//        return uniprotAccession;
//    }
//
//    @Override
//    public List<Compound> getCompounds() {
//
//        return uniprotAccession.getEnzymePortalCompoundSet().stream().collect(Collectors.toList());
//    }
//
//    @Override
//    public List<Disease> getDiseases() {
//
//        return uniprotAccession.getEnzymePortalDiseaseSet().stream().distinct().collect(Collectors.toList());
//    }
}
