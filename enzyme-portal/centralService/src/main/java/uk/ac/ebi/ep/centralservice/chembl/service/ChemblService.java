package uk.ac.ebi.ep.centralservice.chembl.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.text.WordUtils;
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
    private final List<EnzymePortalCompound> fdaChemblCompounds = new ArrayList<>();

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

    public List<EnzymePortalCompound> getFdaChemblCompounds() {
        return fdaChemblCompounds;
    }

    public String capitalizeFirstLetter(String original) {
        if (original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    public void chemblSmallMolecules(String targetId, UniprotEntry protein, EnzymePortalParserService parserService) {
        List<String> moleculeChemblIdsInhibitors = new ArrayList<>();
        List<String> moleculeChemblIdsActivators = new ArrayList<>();
        List<String> moleculeChemblIdsBioactive = new ArrayList<>();

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

                            moleculeChemblIdsInhibitors.add(activity.getMoleculeChemblId());
                        }
                        if ("Activity".equalsIgnoreCase(activity.getStandardType())) {
                            moleculeChemblIdsActivators.add(activity.getMoleculeChemblId());

                        }

                        if ((!"Inhibition".equalsIgnoreCase(activity.getStandardType())) && (!"Activity".equalsIgnoreCase(activity.getStandardType()))) {
                            moleculeChemblIdsBioactive.add(activity.getMoleculeChemblId());
                        }
                    }

                    computePreferredName(moleculeChemblIdsInhibitors, moleculeChemblIdsActivators, moleculeChemblIdsBioactive, chemblCompounds, protein, parserService);

                }
            }
        }

    }

    public void getMoleculesByCuratedMechanism(String targetId, UniprotEntry protein, EnzymePortalParserService parserService) {

        List<String> moleculeChemblIdsInhibitors = new ArrayList<>();
        List<String> moleculeChemblIdsActivators = new ArrayList<>();
        List<String> moleculeChemblIdsBioactives = new ArrayList<>();

        String mechanismUrl = chemblServiceUrl.getMechanismUrl() + targetId;

        Optional<FdaApproved> fda = chemblRestService.getFdaApprovedDrug(mechanismUrl);

        if (fda.isPresent()) {

            if (!fda.get().getMechanisms().isEmpty()) {

                for (Mechanism mechanism : fda.get().getMechanisms()) {

                    if ("INHIBITOR".equalsIgnoreCase(mechanism.getActionType())) {
                        //add molecule id to inhibitors
                        moleculeChemblIdsInhibitors.add(mechanism.getMoleculeChemblId());

                    }
                    if ("ACTIVATOR".equalsIgnoreCase(mechanism.getActionType())) {
                        //add molecule id to activator
                        moleculeChemblIdsActivators.add(mechanism.getMoleculeChemblId());
                    }
                }
            }
        }
        computePreferredName(moleculeChemblIdsInhibitors, moleculeChemblIdsActivators, moleculeChemblIdsBioactives, fdaChemblCompounds, protein, parserService);

    }

    private void computePreferredName(List<String> moleculeChemblIds_Inhibitors, List<String> moleculeChemblIds_Activators,
            List<String> moleculeChemblIdsBioactives, List<EnzymePortalCompound> compounds, UniprotEntry protein, EnzymePortalParserService parserService) {

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

        if (!moleculeChemblIdsBioactives.isEmpty()) {
            for (String moleculeId : moleculeChemblIdsBioactives) {

                String prefNameUrl = chemblServiceUrl.getMoleculeUrl() + moleculeId;

                computeChemblBioactive(prefNameUrl, protein, compounds, parserService);
            }
        }
    }

    private String computeSynonyms(Molecule molecule) {
        String synonyms = molecule.getMoleculeSynonyms().stream().findFirst().get().getSynonyms();

        if (synonyms.contains("|")) {
            synonyms = synonyms.split("|")[0];
        }
        return synonyms;

    }

    private void computeChemblInhibitors(String prefNameUrl, UniprotEntry protein, List<EnzymePortalCompound> compounds, EnzymePortalParserService parserService) {

        Optional<ChemblMolecule> chemblMolecule = chemblRestService.getChemblMolecule(prefNameUrl);
        if (chemblMolecule.isPresent()) {
            for (Molecule molecule : chemblMolecule.get().getMolecules()) {

                String compoundname = molecule.getPrefName();

                if (compoundname == null && molecule.getMoleculeSynonyms() != null && !molecule.getMoleculeSynonyms().isEmpty()) {
//                    String synonyms = molecule.getMoleculeSynonyms().stream().findFirst().get().getSynonyms();
//
//                    if (synonyms.contains("|")) {
//                        synonyms = synonyms.split("|")[0];
//                    }
                    compoundname = computeSynonyms(molecule);

                }

                if (compoundname != null && !StringUtils.isEmpty(compoundname)) {
                    String compoundName = compoundname;
                    String moleculeName = compoundName;
                    if (!compoundname.contains("-") && !compoundname.contains("+")) {

                        compoundName = compoundname.replaceAll(",", "").trim().toLowerCase();

                        if (compoundName.contains(" ")) {
                            String[] x = compoundName.split(" ");
                            String a = x[0];
                            String b = x[1];

                            compoundName = WordUtils.capitalize(a) + " " + b;
                            moleculeName = compoundName;
                        } else if (!compoundName.contains(" ")) {
                            compoundName = WordUtils.capitalize(compoundName);
                            moleculeName = compoundName;
                        }
                    }

                    String compoundId = molecule.getMoleculeChemblId();
                    String compoundSource = MmDatabase.ChEMBL.name();
                    String relationship = Relationship.is_inhibitor_of.name();
                    String compoundRole = CompoundUtil.computeRole(compoundId, relationship);
                    String url = "https://www.ebi.ac.uk/chembl/compound/inspect/" + compoundId;
                    String accession = protein.getAccession();
                    String note = null;

                    EnzymePortalCompound chemblEntry = new EnzymePortalCompound();

                    chemblEntry.setCompoundId(molecule.getMoleculeChemblId());
                    chemblEntry.setCompoundSource(compoundSource);
                    chemblEntry.setCompoundName(moleculeName);
                    chemblEntry.setRelationship(relationship);
                    chemblEntry.setCompoundRole(compoundRole);
                    chemblEntry.setUrl(url);
                    chemblEntry.setNote(note);
                    chemblEntry.setUniprotAccession(protein);

                    //chemblEntry = CompoundUtil.computeRole(chemblEntry, chemblEntry.getRelationship());
                    compounds.add(chemblEntry);

                    //parserService.createCompound(compoundId, moleculeName, compoundSource, relationship, accession, url, compoundRole, note);
                }
            }
        }

    }

    private void computeChemblActivators(String prefNameUrl, UniprotEntry protein, List<EnzymePortalCompound> compounds, EnzymePortalParserService parserService) {

        Optional<ChemblMolecule> chemblMolecule = chemblRestService.getChemblMolecule(prefNameUrl);
        if (chemblMolecule.isPresent()) {

            chemblMolecule.get().getMolecules().stream().forEach((molecule) -> {
                String compoundname = molecule.getPrefName();

                if (compoundname == null && molecule.getMoleculeSynonyms() != null && !molecule.getMoleculeSynonyms().isEmpty()) {
                    String synonyms = molecule.getMoleculeSynonyms().stream().findFirst().get().getSynonyms();

                    if (synonyms.contains("|")) {
                        synonyms = synonyms.split("|")[0];
                    }
                    compoundname = synonyms;

                }

                if (compoundname != null && !StringUtils.isEmpty(compoundname)) {
                    String compoundName = compoundname;
                    String moleculeName = compoundName;
                    if (!compoundname.contains("-") && !compoundname.contains("+")) {

                        compoundName = compoundname.replaceAll(",", "").trim().toLowerCase();

                        if (compoundName.contains(" ")) {
                            String[] x = compoundName.split(" ");
                            String a = x[0];
                            String b = x[1];

                            compoundName = WordUtils.capitalize(a) + " " + b;
                            moleculeName = compoundName;
                        } else if (!compoundName.contains(" ")) {
                            compoundName = WordUtils.capitalize(compoundName);
                            moleculeName = compoundName;
                        }
                    }
                    String compoundId = molecule.getMoleculeChemblId();
                    String compoundSource = MmDatabase.ChEMBL.name();
                    String relationship = Relationship.is_activator_of.name();
                    String compoundRole = CompoundUtil.computeRole(compoundId, relationship);
                    String url = "https://www.ebi.ac.uk/chembl/compound/inspect/" + compoundId;
                    String accession = protein.getAccession();
                    String note = null;

                    // parserService.createCompound(compoundId, moleculeName, compoundSource, relationship, accession, url, compoundRole, note);
                    EnzymePortalCompound chemblEntry = new EnzymePortalCompound();

//                    chemblEntry.setCompoundSource(MmDatabase.ChEMBL.name());
//                    chemblEntry.setCompoundId(molecule.getMoleculeChemblId());
//                    
//                    chemblEntry.setCompoundName(moleculeName);
//                    chemblEntry.setRelationship(Relationship.is_activator_of.name());
//                    chemblEntry.setUniprotAccession(protein);
//                    chemblEntry = CompoundUtil.computeRole(chemblEntry, chemblEntry.getRelationship());
                    chemblEntry.setCompoundId(molecule.getMoleculeChemblId());
                    chemblEntry.setCompoundSource(compoundSource);
                    chemblEntry.setCompoundName(moleculeName);
                    chemblEntry.setRelationship(relationship);
                    chemblEntry.setCompoundRole(compoundRole);
                    chemblEntry.setUrl(url);
                    chemblEntry.setNote(note);
                    chemblEntry.setUniprotAccession(protein);

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

                if (compoundname == null && molecule.getMoleculeSynonyms() != null && !molecule.getMoleculeSynonyms().isEmpty()) {
                    String synonyms = molecule.getMoleculeSynonyms().stream().findFirst().get().getSynonyms();

                    if (synonyms.contains("|")) {
                        synonyms = synonyms.split("|")[0];
                    }
                    compoundname = synonyms;

                }

                if (compoundname != null && !StringUtils.isEmpty(compoundname)) {
                    String compoundName = compoundname;
                    String moleculeName = compoundName;
                    if (!compoundname.contains("-") && !compoundname.contains("+")) {

                        compoundName = compoundname.replaceAll(",", "").trim().toLowerCase();

                        if (compoundName.contains(" ")) {
                            String[] x = compoundName.split(" ");
                            String a = x[0];
                            String b = x[1];

                            compoundName = WordUtils.capitalize(a) + " " + b;
                            moleculeName = compoundName;
                        } else if (!compoundName.contains(" ")) {
                            compoundName = WordUtils.capitalize(compoundName);
                            moleculeName = compoundName;
                        }
                    }

                    String compoundId = molecule.getMoleculeChemblId();
                    String compoundSource = MmDatabase.ChEMBL.name();
                    String relationship = Relationship.is_target_of.name();
                    String compoundRole = CompoundUtil.computeRole(compoundId, relationship);
                    String url = "https://www.ebi.ac.uk/chembl/compound/inspect/" + compoundId;
                    String accession = protein.getAccession();
                    String note = null;

                    //parserService.createCompound(compoundId, moleculeName, compoundSource, relationship, accession, url, compoundRole, note);
                    EnzymePortalCompound chemblEntry = new EnzymePortalCompound();

                    chemblEntry.setCompoundId(molecule.getMoleculeChemblId());
                    chemblEntry.setCompoundSource(compoundSource);
                    chemblEntry.setCompoundName(moleculeName);
                    chemblEntry.setRelationship(relationship);
                    chemblEntry.setCompoundRole(compoundRole);
                    chemblEntry.setUrl(url);
                    chemblEntry.setNote(note);
                    chemblEntry.setUniprotAccession(protein);

//                    chemblEntry.setCompoundSource(MmDatabase.ChEMBL.name());
//                    chemblEntry.setCompoundId(molecule.getMoleculeChemblId());
//                    
//                    chemblEntry.setCompoundName(moleculeName);
//                    chemblEntry.setRelationship(Relationship.is_target_of.name());
//                    chemblEntry.setUniprotAccession(protein);
//                    chemblEntry = CompoundUtil.computeRole(chemblEntry, chemblEntry.getRelationship());
                    compounds.add(chemblEntry);
                }
            });
        }

    }

}
