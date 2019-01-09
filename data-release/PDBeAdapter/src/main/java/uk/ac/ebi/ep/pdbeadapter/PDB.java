package uk.ac.ebi.ep.pdbeadapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;
import uk.ac.ebi.ep.pdbeadapter.publication.AuthorList;

/**
 *
 * @author joseph
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PDB {

    private String id;
    private String title;
    private List<String> experimentMethod;
    @Singular
    private List<String> cofactors;
    @Singular
    private List<String> ligands;
    private String depositionDate;
    private String revisionDate;
    private String releaseDate;

    private List<AuthorList> entryAuthors;
    private String primaryCitation;
    private String primaryCitationInfo;

    private String resolution;
    private String rFactor;
    private String rFree;
    private String spacegroup;

    private List<Polypeptide> polypeptides;
    private List<SmallMoleculeLigand> smallMoleculeLigands;

    private String structuralDomain;

    private List<String> provenance;

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
        if (entryAuthors != null) {
            return entryAuthors.stream().distinct().sorted().collect(Collectors.toList());
        }
        return new ArrayList<>();
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

    public String getSpacegroup() {
        return spacegroup;
    }

    public void setSpacegroup(String spacegroup) {
        this.spacegroup = spacegroup;
    }

    public String getStructuralDomain() {
        return structuralDomain;
    }

    public void setStructuralDomain(String structuralDomain) {
        this.structuralDomain = structuralDomain;
    }

    public List<String> getProvenance() {
        if (provenance == null) {
            provenance = new ArrayList<>();
        }

        return provenance;
    }

    public void setProvenance(List<String> provenance) {
        this.provenance = provenance;
    }

    public List<Polypeptide> getPolypeptides() {
        return polypeptides;
    }

    public void setPolypeptides(List<Polypeptide> polypeptides) {
        this.polypeptides = polypeptides;
    }

    public List<SmallMoleculeLigand> getSmallMoleculeLigands() {
        return smallMoleculeLigands;
    }

    public void setSmallMoleculeLigands(List<SmallMoleculeLigand> smallMoleculeLigands) {
        this.smallMoleculeLigands = smallMoleculeLigands;
    }

    public List<String> getCofactors() {
        return cofactors;
    }

    public void setCofactors(List<String> cofactors) {
        this.cofactors = cofactors;
    }

    public List<String> getLigands() {
        return ligands;
    }

    public void setLigands(List<String> ligands) {
        this.ligands = ligands;
    }


    
    

}
