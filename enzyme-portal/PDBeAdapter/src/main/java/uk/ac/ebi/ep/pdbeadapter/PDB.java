/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.pdbeadapter;

import java.util.ArrayList;
import java.util.List;
import uk.ac.ebi.ep.pdbeadapter.publication.AuthorList;

/**
 *
 * @author joseph
 */
public class PDB {


    private String id;
    private String title;
    private List<String> experimentMethod;
    private String depositionDate;
    private String revisionDate;
    private String releaseDate;

    private List<AuthorList> entryAuthors;
    private String primaryCitation;
    private String primaryCitationInfo;
    
    
    private String resolution;
    private String rFactor;
    private String rFree;
    
    private List<PdbEntity> pdbEntities;
    
    private String structuralDomain;
    
    private List<String> provenance;
   
    
    
    

//    private String moleculeName;
//    private String chain;
//    private String heterogen;
//    private String ligands;
//    private int residues;
    
    
//    private List<Molecule> pdbMolecules;
//    private List<PDBe> pdbSummaries;
//    private List<PDBexperiment> pdbExperiments;
//    private List<PDBePublication> pdbPublications;
//
//    public PDB() {
//        this.pdbMolecules = new ArrayList<>();
//        this.pdbSummaries = new ArrayList<>();
//        this.pdbExperiments = new ArrayList<>();
//        this.pdbPublications = new ArrayList<>();
//    }
//
//    public List<Molecule> getPdbMolecules() {
//        return pdbMolecules;
//    }
//
//    public void setPdbMolecules(List<Molecule> pdbMolecules) {
//        this.pdbMolecules = pdbMolecules;
//    }
//
//    public List<PDBe> getPdbSummaries() {
//        return pdbSummaries;
//    }
//
//    public void setPdbSummaries(List<PDBe> pdbSummaries) {
//        this.pdbSummaries = pdbSummaries;
//    }
//
//    public List<PDBexperiment> getPdbExperiments() {
//        return pdbExperiments;
//    }
//
//    public void setPdbExperiments(List<PDBexperiment> pdbExperiments) {
//        this.pdbExperiments = pdbExperiments;
//    }
//
//    public List<PDBePublication> getPdbPublications() {
//        return pdbPublications;
//    }
//
//    public void setPdbPublications(List<PDBePublication> pdbPublications) {
//        this.pdbPublications = pdbPublications;
//    }

    @Override
    public String toString() {
        return "PDB{" + "id=" + id + ", title=" + title + ", experimentMethod=" + experimentMethod + ", depositionDate=" + depositionDate + ", revisionDate=" + revisionDate + ", releaseDate=" + releaseDate + ", entryAuthors=" + entryAuthors + ", primaryCitation=" + primaryCitation + ", primaryCitationInfo=" + primaryCitationInfo + ", resolution=" + resolution + ", rFactor=" + rFactor + ", rFree=" + rFree + ", pdbEntities=" + pdbEntities + ", structuralDomain=" + structuralDomain + '}';
    }






    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getExperimentMethod() {
        return experimentMethod;
    }

    public void setExperimentMethod(List<String> experimentMethod) {
        this.experimentMethod = experimentMethod;
    }

    public String getDepositionDate() {
      
        
        return depositionDate;
    }

    public void setDepositionDate(String depositionDate) {
        this.depositionDate = depositionDate;
    }

    public String getRevisionDate() {
        return revisionDate;
    }

    public void setRevisionDate(String revisionDate) {
        this.revisionDate = revisionDate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<AuthorList> getEntryAuthors() {
        return entryAuthors;
    }

    public void setEntryAuthors(List<AuthorList> entryAuthors) {
        this.entryAuthors = entryAuthors;
    }

    public String getPrimaryCitation() {
        return primaryCitation;
    }

    public void setPrimaryCitation(String primaryCitation) {
        this.primaryCitation = primaryCitation;
    }

    public String getPrimaryCitationInfo() {
        return primaryCitationInfo;
    }

    public void setPrimaryCitationInfo(String primaryCitationInfo) {
        this.primaryCitationInfo = primaryCitationInfo;
    }


    public List<PdbEntity> getPdbEntities() {
        return pdbEntities;
    }

    public void setPdbEntities(List<PdbEntity> pdbEntities) {
        this.pdbEntities = pdbEntities;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getrFactor() {
        return rFactor;
    }

    public void setrFactor(String rFactor) {
        this.rFactor = rFactor;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getrFree() {
        return rFree;
    }

    public void setrFree(String rFree) {
        this.rFree = rFree;
    }

    public String getStructuralDomain() {
        return structuralDomain;
    }

    public void setStructuralDomain(String structuralDomain) {
        this.structuralDomain = structuralDomain;
    }

    public List<String> getProvenance() {
        if(provenance == null){
            provenance = new ArrayList<>();
        }
        
        return provenance;
    }

    public void setProvenance(List<String> provenance) {
        this.provenance = provenance;
    }



    
    
    
    

}
