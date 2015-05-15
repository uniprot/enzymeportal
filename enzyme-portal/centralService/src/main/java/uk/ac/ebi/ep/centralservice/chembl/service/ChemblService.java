/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.centralservice.chembl.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.util.StringUtils;
import uk.ac.ebi.ep.centralservice.chembl.activity.Activity;
import uk.ac.ebi.ep.centralservice.chembl.activity.ChemblActivity;
import uk.ac.ebi.ep.centralservice.chembl.assay.Assay;
import uk.ac.ebi.ep.centralservice.chembl.assay.ChemblAssay;
import uk.ac.ebi.ep.centralservice.chembl.config.ChemblServiceUrl;
import uk.ac.ebi.ep.centralservice.chembl.mechanism.FdaApproved;
import uk.ac.ebi.ep.centralservice.chembl.mechanism.Mechanism;
import uk.ac.ebi.ep.centralservice.chembl.molecule.ChemblMolecule;
import uk.ac.ebi.ep.centralservice.chembl.molecule.Molecule;
import uk.ac.ebi.ep.centralservice.helper.CompoundUtil;
import uk.ac.ebi.ep.centralservice.helper.MmDatabase;
import uk.ac.ebi.ep.centralservice.helper.Relationship;
import uk.ac.ebi.ep.data.domain.EnzymePortalCompound;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.service.EnzymePortalParserService;

/**
 *
 * @author joseph
 */
public class ChemblService {

    // private static final Logger LOGGER = Logger.getLogger(ChemblService.class);
    private final ChemblRestService chemblRestService;

    private final List<EnzymePortalCompound> chemblCompounds = new ArrayList<>();

    private ChemblServiceUrl chemblServiceUrl;

    public ChemblService(ChemblRestService chemblRestService) {
        this.chemblRestService = chemblRestService;
    }

    public ChemblService(ChemblRestService chemblRestService, ChemblServiceUrl chemblServiceUrl) {
        this.chemblRestService = chemblRestService;
        this.chemblServiceUrl = chemblServiceUrl;
    }

    public List<EnzymePortalCompound> getChemblCompounds() {
        return chemblCompounds;
    }

    public String capitalizeFirstLetter(String original) {
        if (original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    public void chemblSmallMolecules(String targetId, UniprotEntry protein, EnzymePortalParserService parserService) {
        List<String> moleculeChemblIds_Inhibitors = new ArrayList<>();
        List<String> moleculeChemblIds_Activators = new ArrayList<>();
        List<String> moleculeChemblIds_bioactive = new ArrayList<>();

        String assayUrl = chemblServiceUrl.getAssayUrl() + targetId;

        Optional<ChemblAssay> chemblAssay = chemblRestService.getChemblAssay(assayUrl);

        if (chemblAssay.isPresent()) {
            for (Assay assay : chemblAssay.get().getAssays()) {
                String assayId = assay.getAssayChemblId();

                //get activities for a given assay id
                String activityUrl = chemblServiceUrl.getActivityUrl() + assayId;

                Optional<ChemblActivity> chemblActivity = chemblRestService.getChemblActivity(activityUrl);

                if (chemblActivity.isPresent()) {
                    for (Activity activity : chemblActivity.get().getActivities()) {

                        if ("Inhibition".equalsIgnoreCase(activity.getStandardType())) {

                            moleculeChemblIds_Inhibitors.add(activity.getMoleculeChemblId());
                        }
                        if ("Activity".equalsIgnoreCase(activity.getStandardType())) {
                            moleculeChemblIds_Activators.add(activity.getMoleculeChemblId());

                        }

                        if ((!"Inhibition".equalsIgnoreCase(activity.getStandardType())) && (!"Activity".equalsIgnoreCase(activity.getStandardType()))) {
                            moleculeChemblIds_bioactive.add(activity.getMoleculeChemblId());
                        }
                    }

                    computePreferredName(moleculeChemblIds_Inhibitors, moleculeChemblIds_Activators, moleculeChemblIds_bioactive, chemblCompounds, protein, parserService);

                }
            }
        }

    }

    public void getMoleculesByCuratedMechanism(String targetId, UniprotEntry protein, EnzymePortalParserService parserService) {

        List<String> moleculeChemblIds_Inhibitors = new ArrayList<>();
        List<String> moleculeChemblIds_Activators = new ArrayList<>();
        List<String> moleculeChemblIds_bioactive = new ArrayList<>();

        String mechanismUrl = chemblServiceUrl.getMechanismUrl() + targetId;

        Optional<FdaApproved> fda = chemblRestService.getFdaApprovedDrug(mechanismUrl);

        if (fda.isPresent()) {

            if (!fda.get().getMechanisms().isEmpty()) {

                for (Mechanism mechanism : fda.get().getMechanisms()) {

                    if ("INHIBITOR".equalsIgnoreCase(mechanism.getActionType())) {
                        //add molecule id to inhibitors
                        moleculeChemblIds_Inhibitors.add(mechanism.getMoleculeChemblId());

                    }
                    if ("ACTIVATOR".equalsIgnoreCase(mechanism.getActionType())) {
                        //add molecule id to activator
                        moleculeChemblIds_Activators.add(mechanism.getMoleculeChemblId());
                    }
                }
            }
        }
        computePreferredName(moleculeChemblIds_Inhibitors, moleculeChemblIds_Activators, moleculeChemblIds_bioactive, chemblCompounds, protein, parserService);

    }

    private void computePreferredName(List<String> moleculeChemblIds_Inhibitors, List<String> moleculeChemblIds_Activators,
            List<String> moleculeChemblIds_bioactive, List<EnzymePortalCompound> compounds, UniprotEntry protein, EnzymePortalParserService parserService) {

        if (!moleculeChemblIds_Inhibitors.isEmpty()) {

            for (String moleculeId : moleculeChemblIds_Inhibitors) {

                String prefNameUrl = chemblServiceUrl.getMoleculeUrl() + moleculeId;

                computeChemblInhibitors(prefNameUrl, protein, compounds, parserService);
            }
        }

        if (!moleculeChemblIds_Activators.isEmpty()) {

            for (String moleculeId : moleculeChemblIds_Activators) {

                String prefNameUrl = chemblServiceUrl.getMoleculeUrl() + moleculeId;

                computeChemblActivators(prefNameUrl, protein, compounds, parserService);
            }
        }

        if (!moleculeChemblIds_bioactive.isEmpty()) {
            for (String moleculeId : moleculeChemblIds_bioactive) {

                String prefNameUrl = chemblServiceUrl.getMoleculeUrl() + moleculeId;

                computeChemblBioactive(prefNameUrl, protein, compounds, parserService);
            }
        }
    }

    private void computeChemblInhibitors(String prefNameUrl, UniprotEntry protein, List<EnzymePortalCompound> compounds, EnzymePortalParserService parserService) {

        Optional<ChemblMolecule> chemblMolecule = chemblRestService.getChemblMolecule(prefNameUrl);
        if (chemblMolecule.isPresent()) {
            for (Molecule molecule : chemblMolecule.get().getMolecules()) {

                String compoundname = molecule.getPrefName();

                if (compoundname != null && !StringUtils.isEmpty(compoundname)) {
                    //compoundName = compoundName.replaceAll(",", "");

                    String compoundName = StringUtils.capitalize(compoundname.replaceAll(",", ""));
                    if (compoundname.contains("-")) {
                        compoundName = compoundname.replaceAll(",", "");
                    }

                    String compoundId = molecule.getMoleculeChemblId();
                    String compoundSource = MmDatabase.ChEMBL.name();
                    String relationship = Relationship.is_inhibitor_of.name();
                    String compoundRole = CompoundUtil.computeRole(compoundId, relationship);
                    String url = "https://www.ebi.ac.uk/chembl/compound/inspect/" + compoundId;
                    String accession = protein.getAccession();
                    String note = null;

                    EnzymePortalCompound chemblEntry = new EnzymePortalCompound();

                    chemblEntry.setCompoundSource(MmDatabase.ChEMBL.name());
                    chemblEntry.setCompoundId(molecule.getMoleculeChemblId());

                    chemblEntry.setCompoundName(compoundName);
                    chemblEntry.setRelationship(Relationship.is_inhibitor_of.name());

                    chemblEntry.setUniprotAccession(protein);
                    chemblEntry = CompoundUtil.computeRole(chemblEntry, chemblEntry.getRelationship());

                    compounds.add(chemblEntry);

                    parserService.createCompound(compoundId, compoundName, compoundSource, relationship, accession, url, compoundRole, note);

                }
            }
        }

    }

    private void computeChemblActivators(String prefNameUrl, UniprotEntry protein, List<EnzymePortalCompound> compounds, EnzymePortalParserService parserService) {

        Optional<ChemblMolecule> chemblMolecule = chemblRestService.getChemblMolecule(prefNameUrl);
        if (chemblMolecule.isPresent()) {

            chemblMolecule.get().getMolecules().stream().forEach((molecule) -> {
                String compoundname = molecule.getPrefName();

                if (compoundname != null && !StringUtils.isEmpty(compoundname)) {
                    String compoundName = StringUtils.capitalize(compoundname.replaceAll(",", ""));

                    if (compoundname.contains("-")) {
                        compoundName = compoundname.replaceAll(",", "");
                    }
                    String compoundId = molecule.getMoleculeChemblId();
                    String compoundSource = MmDatabase.ChEMBL.name();
                    String relationship = Relationship.is_activator_of.name();
                    String compoundRole = CompoundUtil.computeRole(compoundId, relationship);
                    String url = "https://www.ebi.ac.uk/chembl/compound/inspect/" + compoundId;
                    String accession = protein.getAccession();
                    String note = null;

                    parserService.createCompound(compoundId, compoundName, compoundSource, relationship, accession, url, compoundRole, note);

                    EnzymePortalCompound chemblEntry = new EnzymePortalCompound();

                    chemblEntry.setCompoundSource(MmDatabase.ChEMBL.name());
                    chemblEntry.setCompoundId(molecule.getMoleculeChemblId());

                    chemblEntry.setCompoundName(compoundName);
                    chemblEntry.setRelationship(Relationship.is_activator_of.name());
                    chemblEntry.setUniprotAccession(protein);
                    chemblEntry = CompoundUtil.computeRole(chemblEntry, chemblEntry.getRelationship());

                    compounds.add(chemblEntry);

                }
            });

        }

    }

    private void computeChemblBioactive(String prefNameUrl, UniprotEntry protein, List<EnzymePortalCompound> compounds, EnzymePortalParserService parserService) {
        Optional<ChemblMolecule> chemblMolecule = chemblRestService.getChemblMolecule(prefNameUrl);
        if (chemblMolecule.isPresent()) {

            chemblMolecule.get().getMolecules().stream().forEach((molecule) -> {
                String compoundname = molecule.getPrefName();

                if (compoundname != null && !StringUtils.isEmpty(compoundname)) {
                    //compoundName = compoundName.replaceAll(",", "");
                    String compoundName = StringUtils.capitalize(compoundname.replaceAll(",", ""));
                    if (compoundname.contains("-")) {
                        compoundName = compoundname.replaceAll(",", "");
                    }

                    String compoundId = molecule.getMoleculeChemblId();
                    String compoundSource = MmDatabase.ChEMBL.name();
                    String relationship = Relationship.is_target_of.name();
                    String compoundRole = CompoundUtil.computeRole(compoundId, relationship);
                    String url = "https://www.ebi.ac.uk/chembl/compound/inspect/" + compoundId;
                    String accession = protein.getAccession();
                    String note = null;

                    parserService.createCompound(compoundId, compoundName, compoundSource, relationship, accession, url, compoundRole, note);

                    EnzymePortalCompound chemblEntry = new EnzymePortalCompound();

                    chemblEntry.setCompoundSource(MmDatabase.ChEMBL.name());
                    chemblEntry.setCompoundId(molecule.getMoleculeChemblId());

                    chemblEntry.setCompoundName(compoundName);
                    chemblEntry.setRelationship(Relationship.is_target_of.name());
                    chemblEntry.setUniprotAccession(protein);
                    chemblEntry = CompoundUtil.computeRole(chemblEntry, chemblEntry.getRelationship());

                    compounds.add(chemblEntry);
                }
            });
        }

    }

}
