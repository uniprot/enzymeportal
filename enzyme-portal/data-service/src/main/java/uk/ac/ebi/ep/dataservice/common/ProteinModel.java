package uk.ac.ebi.ep.dataservice.common;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import uk.ac.ebi.ep.dataservice.dto.CompoundView;
import uk.ac.ebi.ep.dataservice.dto.DiseaseView;
import uk.ac.ebi.ep.dataservice.dto.EnzymeReactionView;
import uk.ac.ebi.ep.dataservice.dto.PdbView;
import uk.ac.ebi.ep.dataservice.dto.ProteinView;
import uk.ac.ebi.ep.dataservice.dto.Species;

/**
 * @author joseph
 */
@Data
public class ProteinModel {

    private List<PdbView> proteinstructure;
    private List<CompoundView> cofactors;
    private List<CompoundView> inhibitors;
    private List<CompoundView> activators;
    private String chemblTargetId;
    private List<String> catalyticActivities;
    private List<EnzymeReactionView> enzymeReactions;

    private String accession;
    private String proteinName;
    private Species species;
    private ProteinView proteinView;
    private Short entryType;

    private List<RelatedSpecie> relatedspecies;
    List<DiseaseView> disease;

    public List<PdbView> getProteinstructure() {
        if (proteinstructure == null) {
            proteinstructure = new ArrayList<>();
        }
        return proteinstructure;
    }

    public void setProteinstructure(List<PdbView> proteinstructure) {
        this.proteinstructure = proteinstructure;
    }

    public List<String> getCatalyticActivities() {
        if (catalyticActivities == null) {
            catalyticActivities = new ArrayList<>();
        }
        return catalyticActivities;
    }

    public void setCatalyticActivities(List<String> catalyticActivities) {
        this.catalyticActivities = catalyticActivities;
    }

    public List<EnzymeReactionView> getEnzymeReactions() {
        if (enzymeReactions == null) {
            enzymeReactions = new ArrayList<>();
        }
        return enzymeReactions;
    }

    public void setEnzymeReactions(List<EnzymeReactionView> enzymeReactions) {
        this.enzymeReactions = enzymeReactions;
    }

    public List<RelatedSpecie> getRelatedspecies() {
        if (relatedspecies == null) {
            relatedspecies = new ArrayList<>();
        }
        return relatedspecies;
    }

    public void setRelatedspecies(List<RelatedSpecie> relatedspecies) {
        this.relatedspecies = relatedspecies;
    }

    public List<DiseaseView> getDisease() {
        if (disease == null) {
            disease = new ArrayList<>();
        }
        return disease;
    }

    public void setDisease(List<DiseaseView> disease) {
        this.disease = disease;
    }

    public List<CompoundView> getCofactors() {
        if (cofactors == null) {
            cofactors = new ArrayList<>();
        }
        return cofactors;
    }

    public void setCofactors(List<CompoundView> cofactors) {
        this.cofactors = cofactors;
    }

    public List<CompoundView> getInhibitors() {
        if (inhibitors == null) {
            inhibitors = new ArrayList<>();
        }
        return inhibitors;
    }

    public void setInhibitors(List<CompoundView> inhibitors) {
        this.inhibitors = inhibitors;
    }

    public List<CompoundView> getActivators() {
        if (activators == null) {
            activators = new ArrayList<>();
        }
        return activators;
    }

    public void setActivators(List<CompoundView> activators) {
        this.activators = activators;
    }

    public String getChemblTargetId() {
        return chemblTargetId;
    }

    public void setChemblTargetId(String chemblTargetId) {
        this.chemblTargetId = chemblTargetId;
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getProteinName() {
        return proteinName;
    }

    public void setProteinName(String proteinName) {
        this.proteinName = proteinName;
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public ProteinView getProteinView() {
        return proteinView;
    }

    public void setProteinView(ProteinView proteinView) {
        this.proteinView = proteinView;
    }

    public Short getEntryType() {
        return entryType;
    }

    public void setEntryType(Short entryType) {
        this.entryType = entryType;
    }

}
