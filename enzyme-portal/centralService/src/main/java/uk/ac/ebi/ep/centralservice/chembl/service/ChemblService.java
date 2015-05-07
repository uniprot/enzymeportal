/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.centralservice.chembl.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.log4j.Logger;
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

/**
 *
 * @author joseph
 */
public class ChemblService {

    private static final Logger LOGGER = Logger.getLogger(ChemblService.class);
    private final ChemblRestService chemblRestService;

    private final Set<EnzymePortalCompound> chemblCompounds = new HashSet<>();
    //private final  Set<EnzymePortalCompound> drugCompounds = new HashSet<>();

    //@Autowired
    private ChemblServiceUrl chemblServiceUrl;

    public ChemblService(ChemblRestService chemblRestService) {
        this.chemblRestService = chemblRestService;
    }

    public ChemblService(ChemblRestService chemblRestService, ChemblServiceUrl chemblServiceUrl) {
        this.chemblRestService = chemblRestService;
        this.chemblServiceUrl = chemblServiceUrl;
    }

    public Set<EnzymePortalCompound> getChemblCompounds() {
        return chemblCompounds;
    }

//    public Set<EnzymePortalCompound> getDrugCompounds() {
//        return drugCompounds;
//    }
    public void chemblSmallMolecules(String targetId, UniprotEntry protein) {
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

                   
                    computePreferredName(moleculeChemblIds_Inhibitors, moleculeChemblIds_Activators, moleculeChemblIds_bioactive, chemblCompounds, protein);

                }
            }
        }
  
    }

    public void getMoleculesByCuratedMechanism(String targetId, UniprotEntry protein) {

        List<String> moleculeChemblIds_Inhibitors = new ArrayList<>();
        List<String> moleculeChemblIds_Activators = new ArrayList<>();
        List<String> moleculeChemblIds_bioactive = new ArrayList<>();

 
        String mechanismUrl = chemblServiceUrl.getMechanismUrl() + targetId;
        Optional<FdaApproved> fda = chemblRestService.getFdaApprovedDrug(mechanismUrl);

        if(fda.isPresent()){
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
        computePreferredName(moleculeChemblIds_Inhibitors, moleculeChemblIds_Activators, moleculeChemblIds_bioactive, chemblCompounds, protein);


    }

    private void computePreferredName(List<String> moleculeChemblIds_Inhibitors, List<String> moleculeChemblIds_Activators,
            List<String> moleculeChemblIds_bioactive, Set<EnzymePortalCompound> compounds, UniprotEntry protein) {

        if (!moleculeChemblIds_Inhibitors.isEmpty()) {
        
            for (String moleculeId : moleculeChemblIds_Inhibitors) {

                String prefNameUrl = chemblServiceUrl.getMoleculeUrl() + moleculeId;
           
                computeChemblInhibitors(prefNameUrl, protein, compounds);
            }
        }

   
        if (!moleculeChemblIds_Activators.isEmpty()) {
      

            for (String moleculeId : moleculeChemblIds_Activators) {

                String prefNameUrl = chemblServiceUrl.getMoleculeUrl() + moleculeId;
        
                computeChemblActivators(prefNameUrl, protein, compounds);
            }
        }

        if (!moleculeChemblIds_bioactive.isEmpty()) {
            for (String moleculeId : moleculeChemblIds_bioactive) {

                String prefNameUrl = chemblServiceUrl.getMoleculeUrl() + moleculeId;
  
                computeChemblBioactive(prefNameUrl, protein, compounds);
            }
        }
    }

    private void computeChemblInhibitors(String prefNameUrl, UniprotEntry protein, Set<EnzymePortalCompound> compounds) {

        Optional<ChemblMolecule> chemblMolecule = chemblRestService.getChemblMolecule(prefNameUrl);
        if (chemblMolecule.isPresent()) {
            for (Molecule molecule : chemblMolecule.get().getMolecules()) {

                String compoundName = molecule.getPrefName();

                if (compoundName != null && !StringUtils.isEmpty(compoundName)) {
                    EnzymePortalCompound chemblEntry = new EnzymePortalCompound();

                    chemblEntry.setCompoundSource(MmDatabase.ChEMBL.name());
                    chemblEntry.setCompoundId(molecule.getMoleculeChemblId());

                    chemblEntry.setCompoundName(compoundName);
                    chemblEntry.setRelationship(Relationship.is_inhibitor_of.name());

                    chemblEntry.setUniprotAccession(protein);
                    chemblEntry = CompoundUtil.computeRole(chemblEntry, chemblEntry.getRelationship());

                    compounds.add(chemblEntry);
  
                }
            }
        }

    }

    private void computeChemblActivators(String prefNameUrl, UniprotEntry protein, Set<EnzymePortalCompound> compounds) {

   
        Optional<ChemblMolecule> chemblMolecule = chemblRestService.getChemblMolecule(prefNameUrl);
        if (chemblMolecule.isPresent()) {
   
            chemblMolecule.get().getMolecules().stream().forEach((molecule) -> {
                String compoundName = molecule.getPrefName();
      
                if (compoundName != null && !StringUtils.isEmpty(compoundName)) {
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

    private void computeChemblBioactive(String prefNameUrl, UniprotEntry protein, Set<EnzymePortalCompound> compounds) {
        Optional<ChemblMolecule> chemblMolecule = chemblRestService.getChemblMolecule(prefNameUrl);
        if (chemblMolecule.isPresent()) {

            chemblMolecule.get().getMolecules().stream().forEach((molecule) -> {
                String compoundName = molecule.getPrefName();
   
                if (compoundName != null && !StringUtils.isEmpty(compoundName)) {
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
