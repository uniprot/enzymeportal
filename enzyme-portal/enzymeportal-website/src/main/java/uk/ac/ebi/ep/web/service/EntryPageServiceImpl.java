package uk.ac.ebi.ep.web.service;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ep.dataservice.common.CompoundRole;
import uk.ac.ebi.ep.dataservice.common.ModelOrganisms;
import uk.ac.ebi.ep.dataservice.common.RelatedSpecie;
import uk.ac.ebi.ep.dataservice.dto.CompoundView;
import uk.ac.ebi.ep.dataservice.dto.DiseaseView;
import uk.ac.ebi.ep.dataservice.dto.EnzymeReactionView;
import uk.ac.ebi.ep.dataservice.dto.PdbView;
import uk.ac.ebi.ep.dataservice.dto.ProteinData;
import uk.ac.ebi.ep.dataservice.dto.ProteinView;
import uk.ac.ebi.ep.dataservice.dto.Species;
import uk.ac.ebi.ep.dataservice.service.DataService;
import uk.ac.ebi.ep.enzymeservice.intenz.dto.EcClass;
import uk.ac.ebi.ep.enzymeservice.intenz.dto.EnzymeHierarchy;
import uk.ac.ebi.ep.enzymeservice.intenz.service.IntenzService;
import uk.ac.ebi.ep.enzymeservice.uniprot.model.Comment;
import uk.ac.ebi.ep.enzymeservice.uniprot.service.UniprotService;
import uk.ac.ebi.ep.literatureservice.dto.LabelledCitation;
import uk.ac.ebi.ep.literatureservice.service.LiteratureService;
import uk.ac.ebi.ep.web.model.EnzymeEntryPage;
import uk.ac.ebi.ep.web.model.EnzymeModel;
import uk.ac.ebi.ep.reaction.mechanism.model.MechanismResult;
import uk.ac.ebi.ep.reaction.mechanism.service.ReactionMechanismService;

/**
 *
 * @author joseph
 */
@Slf4j
@Service
class EntryPageServiceImpl implements EntryPageService {

    private final DataService dataService;
    private final IntenzService intenzService;
    private final ReactionMechanismService reactionMechanismService;
    private final LiteratureService literatureService;
    private static final int MCSA_PAGE_SIZE=2;

    @Autowired
    private UniprotService uniprotService;

    @Autowired
    public EntryPageServiceImpl(DataService dataService, IntenzService intenzService, ReactionMechanismService reactionMechanismService, LiteratureService literatureService) {
        this.dataService = dataService;
        this.intenzService = intenzService;
        this.reactionMechanismService = reactionMechanismService;
        this.literatureService = literatureService;

    }

    protected ProteinView findProteinViewByAccession(String accession) {
        return dataService.findProteinViewByAccession(accession);
    }

    @Cacheable(cacheNames = "proteinData", key = "#accession", unless = "#result == null")
    public ProteinData findProteinDataByAccession(String accession) {
        return dataService.findProteinByAccession(accession);
    }

    @Override
    public EnzymeModel getDefaultEnzymeModel(String accession) {
        EnzymeModel model = null;
        ProteinData protein = findProteinDataByAccession(accession);

        if (protein != null) {
            model = new EnzymeModel();
            model.setAccession(protein.getAccession());

            model.setProteinView(protein);
            model.setProteinName(protein.getProteinName());
            model.setEntryType(protein.getEntryType());
            model.setSpecies(protein.getSpecies());
            model.setRelatedspecies(getRelatedspecies(protein));
            return model;
        }
        return model;
    }

    @Override
    public EnzymeModel showEntryEnzymePage(EnzymeModel model) {

        ProteinView uniprotEntry = model.getProteinView();
        EnzymeEntryPage enzyme = new EnzymeEntryPage();

        enzyme.setSequence(uniprotEntry.getSequenceLength());
        enzyme.setAccession(uniprotEntry.getAccession());
        enzyme.setFunction(uniprotEntry.getFunction());
        enzyme.setSynonyms(uniprotEntry.getSynonym());

        List<String> ecNumbers = dataService.findEcNumbersByAccession(model.getAccession());

        List<EnzymeHierarchy> enzymeHierarchies = intenzService.getEnzymeHierarchies(ecNumbers);

        if (!enzymeHierarchies.isEmpty()) {
            enzyme.setEnzymeHierarchies(enzymeHierarchies);
        } else {
            ecNumbers.stream().forEach(ec -> buildEcHierarchy(ec, enzyme));
        }

        model.setEnzyme(enzyme);
        return model;
    }

    @Override
    public EnzymeModel showProteinStructurePage(EnzymeModel model) {

        List<PdbView> pdb = dataService.findPdbViewsByAccession(model.getAccession());
        model.setProteinstructure(pdb);
        return model;
    }

    @Override
    public EnzymeModel showRheaReactionsAndMechanisms(EnzymeModel model) {

        List<String> catalyticActivities = findCatalyticActivitiesByAccession(model.getAccession());
        model.setCatalyticActivities(catalyticActivities);

        MechanismResult mechanismResult = reactionMechanismService.findMechanismResultByAccession(model.getAccession(),MCSA_PAGE_SIZE);
        model.setReactionMechanism(mechanismResult);

        showRheaReactions(model);
        addKineticParameters(model);

        return model;
    }

    private void addKineticParameters(EnzymeModel model) {

        Comment comment = uniprotService.findKinectParamsCommentByAccession(model.getAccession());

        model.setKinetics(comment.getKinetics());
        model.setPhDependences(comment.getPhDependence());
        model.setTemperatureDependences(comment.getTemperatureDependence());

    }

    private EnzymeModel showRheaReactions(EnzymeModel model) {
        List<EnzymeReactionView> reactions = findEnzymeReactionByAccession(model.getAccession());
        if (reactions != null && !reactions.isEmpty()) {
            model.setEnzymeReactions(reactions);

        }
        return model;
    }

    @Override
    public EnzymeModel showCompoundPage(EnzymeModel model) {
        List<CompoundView> compounds = dataService.findCompoundsByAccession(model.getAccession());

        if (!compounds.isEmpty()) {

            String chemblTargetId = dataService.findChemblTargetIdByAccession(model.getAccession())
                    .stream()
                    .filter(Objects::nonNull)
                    .distinct()
                    .findAny()
                    .orElse("");

            model.setChemblTargetId(chemblTargetId);

        }

        List<CompoundView> cofactors = compounds
                .stream()
                .filter(Objects::nonNull)
                .filter(role -> role.getRole().equalsIgnoreCase(CompoundRole.COFACTOR.getName()))
                .collect(Collectors.toList());

        List<CompoundView> inhibitors = compounds
                .stream()
                .filter(Objects::nonNull)
                .filter(role -> role.getRole().equalsIgnoreCase(CompoundRole.INHIBITOR.getName()))
                .collect(Collectors.toList());

        List<CompoundView> activators = compounds
                .stream()
                .filter(Objects::nonNull)
                .filter(role -> role.getRole().equalsIgnoreCase(CompoundRole.ACTIVATOR.getName()))
                .collect(Collectors.toList());
        model.setCofactors(cofactors);
        model.setInhibitors(inhibitors);
        model.setActivators(activators);

        return model;
    }

    @Override
    public EnzymeModel showPathwaysPage(EnzymeModel model) {

        List<String> pathwayIds = dataService.findPathwayIdsByAccession(model.getAccession());
        model.setPathways(pathwayIds);
        return model;
    }

    @Override
    public EnzymeModel showDiseasePage(EnzymeModel model) {

        List<DiseaseView> diseases = dataService.findDiseaseViewByAccession(model.getAccession());
        model.setDisease(diseases);
        return model;
    }

    @Override
    public EnzymeModel showLiteraturePage(EnzymeModel model, int limit) {
        List<LabelledCitation> citations = literatureService.getCitationsByAccession(model.getAccession(), limit);
        model.setLiterature(citations);
        return model;
    }

    private List<String> findCatalyticActivitiesByAccession(String accession) {
        return dataService.findCatalyticActivitiesByAccession(accession);
    }

    private List<EnzymeReactionView> findEnzymeReactionByAccession(String accession) {
        return dataService.findReactionsByAccession(accession);
    }

    private void buildEcHierarchy(String ec, EnzymeEntryPage enzyme) {
        EnzymeHierarchy enzymeHierarchy = new EnzymeHierarchy();
        EcClass ecClass = new EcClass();
        ecClass.setEc(ec);
        enzymeHierarchy.getEcclass().add(ecClass);

        enzyme.getEnzymeHierarchies().add(enzymeHierarchy);

    }

    private List<RelatedSpecie> getRelatedspecies(ProteinView uniprotEntry) {
        List<ProteinView> relatedProteins = new ArrayList<>();
        if (uniprotEntry.getRelatedProteinsId() != null) {
            Long relProtInternalId = uniprotEntry.getRelatedProteinsId();
            String proteinGroupId = uniprotEntry.getProteinGroupId();
             relatedProteins = dataService.findProteinViewByRelatedProteinIdAndProteinGroupId(relProtInternalId, proteinGroupId);

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
