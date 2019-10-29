package uk.ac.ebi.ep.comparisonservice.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ep.comparisonservice.model.ComparisonProteinModel;
import uk.ac.ebi.ep.comparisonservice.model.Compound;
import uk.ac.ebi.ep.comparisonservice.model.Disease;
import uk.ac.ebi.ep.comparisonservice.model.Molecule;
import uk.ac.ebi.ep.comparisonservice.model.Reaction;
import uk.ac.ebi.ep.comparisonservice.model.ReactionPathway;
import uk.ac.ebi.ep.dataservice.common.CompoundRole;
import uk.ac.ebi.ep.dataservice.common.ModelOrganisms;
import uk.ac.ebi.ep.dataservice.common.RelatedSpecie;
import uk.ac.ebi.ep.dataservice.dto.CompoundView;
import uk.ac.ebi.ep.dataservice.dto.DiseaseView;
import uk.ac.ebi.ep.dataservice.dto.EnzymeReactionView;
import uk.ac.ebi.ep.dataservice.dto.PdbView;
import uk.ac.ebi.ep.dataservice.dto.ProteinView;
import uk.ac.ebi.ep.dataservice.dto.Species;
import uk.ac.ebi.ep.dataservice.service.DataService;

/**
 *
 * @author joseph
 */
@Service
class ComparisonServiceImpl implements ComparisonService {

    private final DataService dataService;

    @Autowired
    public ComparisonServiceImpl(DataService dataService) {
        this.dataService = dataService;

    }

    private ProteinView findProteinViewByAccession(String accession) {
        return dataService.findProteinViewByAccession(accession);
    }

    private List<EnzymeReactionView> findEnzymeReactionByAccession(String accession) {
        return dataService.findReactionsByAccession(accession);
    }

    private List<String> findCatalyticActivitiesByAccession(String accession) {
        return dataService.findCatalyticActivitiesByAccession(accession);
    }

    @Override
    public ComparisonProteinModel getComparisonProteinModel(String accession) {
        ComparisonProteinModel model = null;
        ProteinView protein = findProteinViewByAccession(accession);
        List<String> ecNumbers = dataService.findEcNumbersByAccession(accession);
        List<PdbView> pdb = dataService.findPdbViewsByAccession(accession);
        List<String> catalyticActivities = findCatalyticActivitiesByAccession(accession);
        List<DiseaseView> diseases = dataService.findDiseaseViewByAccession(accession);

        if (protein != null) {
            model = new ComparisonProteinModel();
            model.setAccession(protein.getAccession());
            model.setProteinView(protein);
            model.setProteinName(protein.getProteinName());
            model.setEntryType(protein.getEntryType());
            model.setSpecies(protein.getSpecies());
            model.setRelatedspecies(getRelatedspecies(protein));
            model.setProteinstructure(pdb);
            model.setEc(ecNumbers);
            model.setCatalyticActivities(catalyticActivities);
            model.setDisease(diseases);
            return model;
        }
        return model;
    }

    @Override
    public Molecule getCompareEnzymeMolecule(String accession) {
        List<CompoundView> compounds = dataService.findCompoundsByAccession(accession);

        List<Compound> cofactors = compounds.stream()
                .filter(Objects::nonNull)
                .filter(role -> role.getRole().equalsIgnoreCase(CompoundRole.COFACTOR.getName()))
                .map(c -> new Compound(c.getId(), c.getName(), c.getSource(), c.getUrl(), c.getRole()))
                .collect(Collectors.toList());

        List<Compound> inhibitors = compounds.stream()
                .filter(Objects::nonNull)
                .filter(role -> role.getRole().equalsIgnoreCase(CompoundRole.INHIBITOR.getName()))
                .map(c -> new Compound(c.getId(), c.getName(), c.getSource(), c.getUrl(), c.getRole()))
                .collect(Collectors.toList());

        List<Compound> activators = compounds.stream()
                .filter(Objects::nonNull)
                .filter(role -> role.getRole().equalsIgnoreCase(CompoundRole.ACTIVATOR.getName()))
                .map(c -> new Compound(c.getId(), c.getName(), c.getSource(), c.getUrl(), c.getRole()))
                .collect(Collectors.toList());
        Molecule molecule = new Molecule();
        molecule.setCofactors(cofactors);
        molecule.setInhibitors(inhibitors);
        molecule.setActivators(activators);

        return molecule;
    }

    @Override
    public ReactionPathway getCompareEnzymeReactionPathay(String accession) {
        List<EnzymeReactionView> reactions = findEnzymeReactionByAccession(accession);

        List<String> pathwayIds = dataService.findPathwayIdsByAccession(accession);
        ReactionPathway rp = new ReactionPathway();
        rp.setPathways(pathwayIds);
        if (reactions != null && !reactions.isEmpty()) {

            Reaction reaction = reactions
                    .stream()
                    .filter(Objects::nonNull)
                    .map(r -> new Reaction(r.getId(), r.getName()))
                    .findAny()
                    .orElse(new Reaction());

            rp.setReaction(reaction);
        }
        return rp;
    }

    @Override
    public List<Disease> getCompareEnzymeDisease(String accession) {

        List<DiseaseView> diseases = dataService.findDiseaseViewByAccession(accession);

        return diseases
                .stream()
                .filter(Objects::nonNull)
                .map(d -> new Disease(d.getOmimNumber(), d.getDiseaseName(), d.getUrl(), d.getEvidences()))
                .collect(Collectors.toList());

    }

    private List<RelatedSpecie> getRelatedspecies(ProteinView uniprotEntry) {
        List<ProteinView> relatedProteins = new ArrayList<>();
        if (uniprotEntry.getRelatedProteinsId() != null) {
            Long relProtInternalId = uniprotEntry.getRelatedProteinsId();
            relatedProteins = dataService.findProteinViewByRelatedProteinId(relProtInternalId);

        }

        final Map<Integer, ProteinView> priorityMapper = new TreeMap<>();
        AtomicInteger key = new AtomicInteger(50);
        AtomicInteger customKey = new AtomicInteger(6);

        LinkedHashSet<RelatedSpecie> relatedspecies = new LinkedHashSet<>();

        if (relatedProteins != null) {

            return relatedProteins
                    .stream()
                    .map(entry -> sortSpecies(entry.getSpecies(), entry, priorityMapper, customKey, key))
                    .map(Map::entrySet)
                    .flatMap(Set::stream)
                    .map(m -> buildEnzymeAccessionFromProteinView(m.getValue(), relatedspecies))
                    .flatMap(Set::stream)
                    .distinct()
                    .sorted(Comparator.comparing(RelatedSpecie::getExpEvidence)
                            .reversed())
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    private LinkedHashSet<RelatedSpecie> buildEnzymeAccessionFromProteinView(ProteinView protein, LinkedHashSet<RelatedSpecie> relatedspecies) {

        RelatedSpecie ea = new RelatedSpecie();
        ea.setSpecies(protein.getSpecies());
        ea.setExpEvidence(protein.getExpEvidence());
        ea.getUniprotaccessions().add(protein.getAccession());
        ea.setAccession(protein.getAccession());

        relatedspecies.add(ea);
        return relatedspecies;
    }

    private Map<Integer, ProteinView> sortSpecies(Species sp, ProteinView entry, Map<Integer, ProteinView> priorityMapper, AtomicInteger customKey, AtomicInteger key) {
        //Human,Mouse, Mouse-ear cress, fruit fly, yeast, e.coli, Rat,worm
        // "Homo sapiens","Mus musculus","Rattus norvegicus", "Drosophila melanogaster","WORM","Saccharomyces cerevisiae","ECOLI"
        if (sp.getTaxId().equals(ModelOrganisms.HUMAN.getTaxId())) {

            priorityMapper.put(1, entry);
        } else if (sp.getTaxId().equals(ModelOrganisms.MOUSE.getTaxId())) {

            priorityMapper.put(2, entry);
        } else if (sp.getTaxId().equals(ModelOrganisms.MOUSE_EAR_CRESS.getTaxId())) {

            priorityMapper.put(3, entry);
        } else if (sp.getTaxId().equals(ModelOrganisms.FRUIT_FLY.getTaxId())) {

            priorityMapper.put(4, entry);
        } else if (sp.getTaxId().equals(ModelOrganisms.ECOLI.getTaxId())) {

            priorityMapper.put(5, entry);
        } else if (sp.getTaxId().equals(ModelOrganisms.BAKER_YEAST.getTaxId())) {
            priorityMapper.put(6, entry);

        } else if (sp.getTaxId().equals(ModelOrganisms.RAT.getTaxId())) {
            priorityMapper.put(customKey.getAndIncrement(), entry);
        } else {
            priorityMapper.put(key.getAndIncrement(), entry);
        }
        return priorityMapper;
    }

}
