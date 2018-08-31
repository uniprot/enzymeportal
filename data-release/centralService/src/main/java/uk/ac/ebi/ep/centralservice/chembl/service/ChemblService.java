package uk.ac.ebi.ep.centralservice.chembl.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.text.WordUtils;
import org.springframework.util.StringUtils;
import uk.ac.ebi.ep.centralservice.chembl.activity.Activity;
import uk.ac.ebi.ep.centralservice.chembl.activity.ChemblActivity;
import uk.ac.ebi.ep.centralservice.chembl.config.ChemblServiceUrl;
import uk.ac.ebi.ep.centralservice.chembl.mechanism.FdaApproved;
import uk.ac.ebi.ep.centralservice.chembl.mechanism.Mechanism;
import uk.ac.ebi.ep.centralservice.chembl.molecule.ChemblMolecule;
import uk.ac.ebi.ep.centralservice.chembl.molecule.Molecule;
import uk.ac.ebi.ep.centralservice.helper.CompoundUtil;
import uk.ac.ebi.ep.centralservice.helper.MmDatabase;
import uk.ac.ebi.ep.centralservice.helper.Relationship;
import uk.ac.ebi.ep.centralservice.helper.Role;
import uk.ac.ebi.ep.centralservice.helper.StandardType;
import uk.ac.ebi.ep.model.TempCompoundCompare;

/**
 *
 * @author joseph
 */
@Slf4j
public class ChemblService {

    private final ChemblRestService chemblRestService;

    private final List<TempCompoundCompare> chemblCompounds;
    private final List<TempCompoundCompare> fdaChemblCompounds;

    private final ChemblServiceUrl chemblServiceUrl;

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

    private Optional<ChemblActivity> getPrimaryTargetFromChemblActivity(String targetId) {
        String primaryTargetSelectorUrl = chemblServiceUrl.getPrimaryTargetSelectorUrl() + targetId;

        return chemblRestService.getChemblActivity(primaryTargetSelectorUrl);
    }

    private String computePrimaryTarget(List<String> targets) {
        SortedMap<Integer, String> primaryTargetAggregator = new TreeMap<>();

        targets
                .forEach(targetId -> primaryTargetAggregator.put(getPrimaryTargetFromChemblActivity(targetId)
                .orElse(new ChemblActivity())
                .getPageMeta()
                .getTotalCount(), targetId));

        return primaryTargetAggregator.get(primaryTargetAggregator.lastKey());
    }

    private TempCompoundCompare getTempCompoundCompare(String primaryTargetId, String compoundId, String compoundSource, String moleculeName, String relationship, String compoundRole, String url, String note, String accession) {
        TempCompoundCompare tempCompoundCompare = new TempCompoundCompare();

        tempCompoundCompare.setCompoundId(compoundId);
        tempCompoundCompare.setCompoundSource(compoundSource);
        tempCompoundCompare.setCompoundName(moleculeName);
        tempCompoundCompare.setRelationship(relationship);
        tempCompoundCompare.setCompoundRole(compoundRole);
        tempCompoundCompare.setUrl(url);
        tempCompoundCompare.setNote(note);
        tempCompoundCompare.setUniprotAccession(accession);
        tempCompoundCompare.setPrimaryTargetId(primaryTargetId);

        return tempCompoundCompare;

    }

    private void activitiesToCompounds(Activity data, List<TempCompoundCompare> compounds, String protein, String primaryTargetId) {

        String compoundPrefName = data.getMoleculePrefName();

        if (compoundPrefName != null && !StringUtils.isEmpty(compoundPrefName)) {
            String compoundName = compoundPrefName;
            String moleculeName = compoundName;
            if (!compoundPrefName.contains("-") && !compoundPrefName.contains("+")) {

                compoundName = compoundPrefName.replaceAll(",", "").trim().toLowerCase();

                moleculeName = computeMoleculeName(compoundName);
            }

            String compoundId = data.getMoleculeChemblId();
            String compoundSource = MmDatabase.ChEMBL.name();
            String relationship = Relationship.is_inhibitor_of.name();
            String compoundRole = CompoundUtil.computeRole(compoundId, relationship);
            String url = "https://www.ebi.ac.uk/chembl/compound/inspect/" + compoundId;
            String accession = protein;
            String note = null;
            TempCompoundCompare tempCompoundCompare = getTempCompoundCompare(primaryTargetId, compoundId, compoundSource, moleculeName, relationship, compoundRole, url, note, accession);

            compounds.add(tempCompoundCompare);

        }
    }

    private boolean filterStandardTypeAndValue(Activity activity) {
        String standardValue = activity.getStandardValue();
        String standardType = activity.getStandardType();

        if (standardValue != null && standardType != null) {
            Float sValue = Float.valueOf(standardValue);
            return (standardType.equalsIgnoreCase(StandardType.INHIBITION.name()) && sValue >= 50)
                    || (standardType.equalsIgnoreCase(StandardType.IC50.name()) && sValue <= 1000);
        }
        return false;

    }

    private Optional<ChemblActivity> getChemblActivity(String targetId) {
        String inhibitionIc50Url = chemblServiceUrl.getInhibitionIc50Url() + targetId;

        //TODO delete later
        log.warn("URL SENT " + inhibitionIc50Url);
        return chemblRestService.getChemblActivity(inhibitionIc50Url);
    }

    public void processChemblSmallMolecules(List<String> targets, String protein) {
        String primaryTargetId = computePrimaryTarget(targets);

        String targetIds = targets
                .stream()
                .distinct()
                .collect(Collectors.joining(","));

        //TODO delete later
        if (targets.size() > 1) {
            log.warn("Targets to process  " + targetIds);
        }

        getChemblActivity(targetIds)
                .orElse(new ChemblActivity())
                .getActivities()
                .stream()
                .filter(activity -> filterStandardTypeAndValue(activity))
                .filter(n -> n.getMoleculePrefName() != null)
                .distinct()
                .forEach(data -> activitiesToCompounds(data, chemblCompounds, protein, primaryTargetId));
    }

    public void getMoleculesByCuratedMechanism(List<String> targets, String protein) {

        List<String> moleculeChemblIdsInhibitors = new ArrayList<>();
        List<String> moleculeChemblIdsActivators = new ArrayList<>();
        String primaryTargetId = computePrimaryTarget(targets);
        for (String targetId : targets) {
            String mechanismUrl = chemblServiceUrl.getMechanismUrl() + targetId;

            //log.warn("fda url " + mechanismUrl);
            Optional<FdaApproved> fda = chemblRestService.getFdaApprovedDrug(mechanismUrl);

            if (fda.isPresent() && !fda.get().getMechanisms().isEmpty()) {

                for (Mechanism mechanism : fda.get().getMechanisms()) {

                    if (Role.INHIBITOR.name().equalsIgnoreCase(mechanism.getActionType())) {
                        //add molecule id to inhibitors
                        moleculeChemblIdsInhibitors.add(mechanism.getMoleculeChemblId());

                    }
                    if (Role.ACTIVATOR.name().equalsIgnoreCase(mechanism.getActionType())) {
                        //add molecule id to activator
                        moleculeChemblIdsActivators.add(mechanism.getMoleculeChemblId());
                    }
                }

            }
        }
        computePreferredName(moleculeChemblIdsInhibitors, moleculeChemblIdsActivators, fdaChemblCompounds, protein, primaryTargetId);

    }

    private void computePreferredName(List<String> moleculeChemblIdsInhibitors, List<String> moleculeChemblIdsActivators,
            List<TempCompoundCompare> compounds, String protein, String primaryTargetId) {

        if (!moleculeChemblIdsInhibitors.isEmpty()) {

            moleculeChemblIdsInhibitors
                    .stream()
                    .map((moleculeId) -> chemblServiceUrl.getMoleculeUrl() + moleculeId)
                    .forEach((prefNameUrl) -> computeChemblInhibitors(prefNameUrl, protein, compounds, primaryTargetId));

        }

        if (!moleculeChemblIdsActivators.isEmpty()) {

            for (String moleculeId : moleculeChemblIdsActivators) {

                String prefNameUrl = chemblServiceUrl.getMoleculeUrl() + moleculeId;

                computeChemblActivators(prefNameUrl, protein, compounds, primaryTargetId);
            }
        }

    }

    private String computeSynonyms(Molecule molecule) {
        String synonyms = molecule.getMoleculeSynonyms().stream().findFirst().get().getSynonyms();
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

            String capitalizedSplitCompoundName = WordUtils.capitalize(a) + " " + b;
            moleculeName = capitalizedSplitCompoundName;
        } else if (!compoundName.contains(" ")) {
            String capitalizedCompoundName = WordUtils.capitalize(compoundName);
            moleculeName = capitalizedCompoundName;
        }
        return moleculeName;
    }

    private void computeChemblInhibitors(String prefNameUrl, String protein, List<TempCompoundCompare> compounds, String primaryTargetId) {

        Optional<ChemblMolecule> chemblMolecule = chemblRestService.getChemblMolecule(prefNameUrl.trim());
        if (chemblMolecule.isPresent() && !chemblMolecule.get().getMolecules().isEmpty()) {

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
                    chemblEntry.setPrimaryTargetId(primaryTargetId);

                    compounds.add(chemblEntry);

                }
            }

        }

    }

    private void computeChemblActivators(String prefNameUrl, String protein, List<TempCompoundCompare> compounds, String primaryTargetId) {

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
                    chemblEntry.setPrimaryTargetId(primaryTargetId);

                    compounds.add(chemblEntry);

                }
            });

        }

    }

}
