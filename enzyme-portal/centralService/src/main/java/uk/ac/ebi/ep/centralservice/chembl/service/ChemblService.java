package uk.ac.ebi.ep.centralservice.chembl.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
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
import uk.ac.ebi.ep.data.domain.TempCompoundCompare;

/**
 *
 * @author joseph
 */
public class ChemblService {

    private final ChemblRestService chemblRestService;

    private final List<TempCompoundCompare> chemblCompounds;
    private final List<TempCompoundCompare> fdaChemblCompounds;

    private ChemblServiceUrl chemblServiceUrl;

    public ChemblService(ChemblRestService chemblRestService) {
        this.chemblRestService = chemblRestService;
        this.chemblCompounds = new ArrayList<>();
        this.fdaChemblCompounds = new ArrayList<>();

    }

    public ChemblService(ChemblRestService chemblRestService, ChemblServiceUrl chemblServiceUrl) {
        this.chemblRestService = chemblRestService;
        this.chemblServiceUrl = chemblServiceUrl;
        this.chemblCompounds = new ArrayList<>();
        this.fdaChemblCompounds = new ArrayList<>();

    }

    public List<TempCompoundCompare> getChemblCompounds() {
        return chemblCompounds;
    }

    public List<TempCompoundCompare> getFdaChemblCompounds() {
        return fdaChemblCompounds;
    }

    public String capitalizeFirstLetter(String original) {
        if (original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    public void chemblSmallMolecules(String targetId, String protein) {
        List<String> moleculeChemblIdsInhibitors = new ArrayList<>();
        List<String> moleculeChemblIdsActivators = new ArrayList<>();

        String assayUrl = chemblServiceUrl.getAssayUrl() + targetId;

        Optional<ChemblAssay> chemblAssay = chemblRestService.getChemblAssay(assayUrl);

        if (chemblAssay.isPresent()) {
            if (!chemblAssay.get().getAssays().isEmpty()) {

                for (Assay assay : chemblAssay.get().getAssays().stream().distinct().collect(Collectors.toList())) {
                    String assayId = assay.getAssayChemblId();
                    //get activities for a given assay id
                    //TODO process in parallel the standard types inhibition and ic50 
                    String activityUrl = chemblServiceUrl.getActivityUrl() + assayId;
                    String ic50ActivityUrl = chemblServiceUrl.getIc50ActivityUrl() + assayId;

                    Optional<ChemblActivity> chemblActivity = chemblRestService.getChemblActivity(activityUrl);

                    if (chemblActivity.isPresent()) {
                        if (!chemblActivity.get().getActivities().isEmpty()) {

                            for (Activity activity : chemblActivity.get().getActivities().stream().distinct().collect(Collectors.toList())) {
                                moleculeChemblIdsInhibitors.add(activity.getMoleculeChemblId());
                            }

                            computePreferredName(moleculeChemblIdsInhibitors, moleculeChemblIdsActivators, chemblCompounds, protein);
                        }
                    }

                    Optional<ChemblActivity> chemblic50Activity = chemblRestService.getChemblActivity(ic50ActivityUrl);

                    if (chemblic50Activity.isPresent()) {
                        if (!chemblic50Activity.get().getActivities().isEmpty()) {

                            chemblic50Activity.get().getActivities().stream().distinct().collect(Collectors.toList()).stream()
                                    .forEach(activity -> moleculeChemblIdsInhibitors.add(activity.getMoleculeChemblId()));

                            computePreferredName(moleculeChemblIdsInhibitors, moleculeChemblIdsActivators, chemblCompounds, protein);
                        }
                    }
                }
            }
        }

    }

    public void getMoleculesByCuratedMechanism(String targetId, String protein) {

        List<String> moleculeChemblIdsInhibitors = new ArrayList<>();
        List<String> moleculeChemblIdsActivators = new ArrayList<>();

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
        computePreferredName(moleculeChemblIdsInhibitors, moleculeChemblIdsActivators, fdaChemblCompounds, protein);

    }

    private void computePreferredName(List<String> moleculeChemblIdsInhibitors, List<String> moleculeChemblIdsActivators,
            List<TempCompoundCompare> compounds, String protein) {

        if (!moleculeChemblIdsInhibitors.isEmpty()) {

            moleculeChemblIdsInhibitors.stream()
                    .map(moleculeId -> chemblServiceUrl.getMoleculeUrl() + moleculeId)
                    .forEach(prefNameUrl -> computeChemblInhibitors(prefNameUrl, protein, compounds));
        }

        if (!moleculeChemblIdsActivators.isEmpty()) {

            for (String moleculeId : moleculeChemblIdsActivators) {

                String prefNameUrl = chemblServiceUrl.getMoleculeUrl() + moleculeId;

                computeChemblActivators(prefNameUrl, protein, compounds);
            }
        }

    }

    private String computeSynonyms(Molecule molecule) {
        String synonyms = molecule.getMoleculeSynonyms()
                .stream()
                .findFirst()
                .get().getSynonyms();
        String result = synonyms;
        String[] parts;
        if (synonyms.contains("|")) {
            parts = synonyms.split(Pattern.quote("|"));
            result = parts[0];
        }
        return result;

    }

    private String computeMoleculeName(String compoundName) {
        String moleculeName = compoundName;
        if (compoundName.contains(" ")) {
            String[] x = compoundName.split(" ");
            String a = x[0];
            String b = x[1];

            String capitalizedCompoundName = WordUtils.capitalize(a) + " " + b;
            moleculeName = capitalizedCompoundName;
        } else if (!compoundName.contains(" ")) {
            String capitalizedCompoundName = WordUtils.capitalize(compoundName);
            moleculeName = capitalizedCompoundName;
        }
        return moleculeName;
    }

    private void computeChemblInhibitors(String prefNameUrl, String protein, List<TempCompoundCompare> compounds) {

        Optional<ChemblMolecule> chemblMolecule = chemblRestService.getChemblMolecule(prefNameUrl.trim());
        if (chemblMolecule.isPresent()) {
            if (!chemblMolecule.get().getMolecules().isEmpty()) {
                for (Molecule molecule : chemblMolecule.get().getMolecules()) {

                    String compoundPrefName = molecule.getPrefName();

                    if (compoundPrefName == null && molecule.getMoleculeSynonyms() != null && !molecule.getMoleculeSynonyms().isEmpty()) {
                        compoundPrefName = computeSynonyms(molecule);

                    }

                    if (compoundPrefName != null && !StringUtils.isEmpty(compoundPrefName)) {
                        String compoundName = compoundPrefName;
                        String moleculeName = compoundName;
                        if (!compoundPrefName.contains("-") && !compoundPrefName.contains("+")) {

                            compoundName = compoundPrefName.replaceAll(",", "").trim().toLowerCase();

                            moleculeName = computeMoleculeName(compoundName);
                        }

                        String compoundId = molecule.getMoleculeChemblId();
                        String compoundSource = MmDatabase.ChEMBL.name();
                        String relationship = Relationship.is_inhibitor_of.name();
                        String compoundRole = CompoundUtil.computeRole(compoundId, relationship);
                        String url = "https://www.ebi.ac.uk/chembl/compound/inspect/" + compoundId;
                        String accession = protein;
                        String note = null;

                        TempCompoundCompare chemblEntry = new TempCompoundCompare();

                        chemblEntry.setCompoundId(molecule.getMoleculeChemblId());
                        chemblEntry.setCompoundSource(compoundSource);
                        chemblEntry.setCompoundName(moleculeName);
                        chemblEntry.setRelationship(relationship);
                        chemblEntry.setCompoundRole(compoundRole);
                        chemblEntry.setUrl(url);
                        chemblEntry.setNote(note);
                        chemblEntry.setUniprotAccession(accession);

                        compounds.add(chemblEntry);

                    }
                }
            }
        }

    }

    private void computeChemblActivators(String prefNameUrl, String protein, List<TempCompoundCompare> compounds) {

        Optional<ChemblMolecule> chemblMolecule = chemblRestService.getChemblMolecule(prefNameUrl.trim());
        if (chemblMolecule.isPresent()) {

            chemblMolecule.get().getMolecules().stream().forEach(molecule -> {
                String compoundPrefName = molecule.getPrefName();

                if (compoundPrefName == null && molecule.getMoleculeSynonyms() != null && !molecule.getMoleculeSynonyms().isEmpty()) {

                    compoundPrefName = computeSynonyms(molecule);
                }

                if (compoundPrefName != null && !StringUtils.isEmpty(compoundPrefName)) {
                    String compoundName = compoundPrefName;
                    String moleculeName = compoundName;
                    if (!compoundPrefName.contains("-") && !compoundPrefName.contains("+")) {

                        compoundName = compoundPrefName.replaceAll(",", "").trim().toLowerCase();

                        moleculeName = computeMoleculeName(compoundName);
                    }
                    String compoundId = molecule.getMoleculeChemblId();
                    String compoundSource = MmDatabase.ChEMBL.name();
                    String relationship = Relationship.is_activator_of.name();
                    String compoundRole = CompoundUtil.computeRole(compoundId, relationship);
                    String url = "https://www.ebi.ac.uk/chembl/compound/inspect/" + compoundId;
                    String accession = protein;
                    String note = null;

                    TempCompoundCompare chemblEntry = new TempCompoundCompare();

                    chemblEntry.setCompoundId(molecule.getMoleculeChemblId());
                    chemblEntry.setCompoundSource(compoundSource);
                    chemblEntry.setCompoundName(moleculeName);
                    chemblEntry.setRelationship(relationship);
                    chemblEntry.setCompoundRole(compoundRole);
                    chemblEntry.setUrl(url);
                    chemblEntry.setNote(note);
                    chemblEntry.setUniprotAccession(accession);

                    compounds.add(chemblEntry);

                }
            });

        }

    }

}
