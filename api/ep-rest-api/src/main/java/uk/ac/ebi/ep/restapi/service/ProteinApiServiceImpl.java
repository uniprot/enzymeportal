package uk.ac.ebi.ep.restapi.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import uk.ac.ebi.ep.dataservice.common.CompoundRole;
import uk.ac.ebi.ep.dataservice.dto.CompoundView;
import uk.ac.ebi.ep.dataservice.dto.DiseaseView;
import uk.ac.ebi.ep.dataservice.dto.EnzymeReactionView;
import uk.ac.ebi.ep.dataservice.dto.PdbView;
import uk.ac.ebi.ep.dataservice.dto.ProteinData;
import uk.ac.ebi.ep.dataservice.service.DataService;
import uk.ac.ebi.ep.enzymeservice.reactome.service.ReactomeService;
import uk.ac.ebi.ep.enzymeservice.reactome.view.PathWay;
import uk.ac.ebi.ep.enzymeservice.uniprot.model.Comment;
import uk.ac.ebi.ep.enzymeservice.uniprot.service.UniprotService;
import uk.ac.ebi.ep.literatureservice.dto.LabelledCitation;
import uk.ac.ebi.ep.literatureservice.service.LiteratureService;
import uk.ac.ebi.ep.reaction.mechanism.model.MechanismResult;
import uk.ac.ebi.ep.reaction.mechanism.service.ReactionMechanismService;
import uk.ac.ebi.ep.restapi.dto.Compound;
import uk.ac.ebi.ep.restapi.dto.EnzymeDisease;
import uk.ac.ebi.ep.restapi.dto.PdbInfo;
import uk.ac.ebi.ep.restapi.dto.Reaction;
import uk.ac.ebi.ep.restapi.dto.ReactionParameter;
import uk.ac.ebi.ep.restapi.dto.SmallMolecule;
import uk.ac.ebi.ep.restapi.model.Protein;

/**
 *
 * @author joseph
 */
@Service
class ProteinApiServiceImpl implements ProteinApiService {

    private final DataService dataService;
    private final ReactomeService reactomeService;
    private final ReactionMechanismService reactionMechanismService;
    private final LiteratureService literatureService;
    private final UniprotService uniprotService;

    @Autowired
    public ProteinApiServiceImpl(DataService dataService, ReactomeService reactomeService, ReactionMechanismService reactionMechanismService, LiteratureService literatureService, UniprotService uniprotService) {
        this.dataService = dataService;
        this.reactomeService = reactomeService;
        this.reactionMechanismService = reactionMechanismService;
        this.literatureService = literatureService;
        this.uniprotService = uniprotService;
    }

    @Override
    public List<PdbInfo> pdbByAccession(String accession) {
        List<PdbView> pdbView = dataService.findPdbViewsByAccession(accession);
        return pdbView.stream()
                .map(this::toPdbInfo)
                .collect(Collectors.toList());

    }

    @Override
    public Reaction enzymeReactionByAccession(String accession, int mcsaLimit) {
        List<String> catalyticActivities = dataService.findCatalyticActivitiesByAccession(accession);
        MechanismResult mechanismResult = reactionMechanismService.findMechanismResultByAccession(accession, mcsaLimit);
        List<EnzymeReactionView> rheaReactions = dataService.findReactionsByAccession(accession);
        Comment comment = uniprotService.findKinectParamsCommentByAccession(accession);
        ReactionParameter reactionParameter = ReactionParameter.builder()
                .kinetics(comment.getKinetics())
                .phDependence(comment.getPhDependence())
                .temperatureDependence(comment.getTemperatureDependence())
                .build();

        return Reaction.builder()
                .catalyticActivities(catalyticActivities)
                .reactionMechanism(mechanismResult)
                .rheaReactions(rheaReactions)
                .reactionParameter(reactionParameter)
                .build();
    }

    @Override
    public List<EnzymeDisease> diseasesByAccession(String accession) {

        List<DiseaseView> diseases = dataService.findDiseaseViewByAccession(accession);
        return diseases.stream()
                .map(this::toEnzymeDisease)
                .collect(Collectors.toList());

    }

    @Override
    public List<LabelledCitation> citationsByAccession(String accession, int limit) {
        return literatureService.getCitationsByAccession(accession, limit);
    }

    @Override
    public Flux<PathWay> pathwaysByAccession(String accession) {
        List<String> pathwayIds = dataService.findPathwayIdsByAccession(accession);
        return reactomeService.pathwaysByPathwayIds(pathwayIds);
    }

    @Override
    public Protein proteinByAccession(String accession) {
        List<String> ecNumbers = dataService.findEcNumbersByAccession(accession);
        ProteinData protein = dataService.findProteinByAccession(accession);
        return Protein.builder()
                .accession(protein.getAccession())
                .proteinName(protein.getProteinName())
                .commonName(protein.getCommonName())
                .scientificName(protein.getScientificName())
                .taxid(protein.getTaxId())
                .function(protein.getFunction())
                .ecNumbers(ecNumbers)
                .entryType(shortToString(protein.getEntryType()))
                .otherNames(protein.getSynonym())
                .sequenceLength(protein.getSequenceLength())
                .experimentalEvidence(getCode(protein.getExpEvidenceFlag()))
                .build();
    }

    @Override
    public SmallMolecule smallMoleculeByAccession(String accession) {
        List<CompoundView> compounds = dataService.findCompoundsByAccession(accession);

        List<Compound> cofactors = compounds
                .stream()
                .filter(Objects::nonNull)
                .filter(role -> role.getRole().equalsIgnoreCase(CompoundRole.COFACTOR.getName()))
                .map(this::toCompound)
                .collect(Collectors.toList());

        List<Compound> inhibitors = compounds
                .stream()
                .filter(Objects::nonNull)
                .filter(role -> role.getRole().equalsIgnoreCase(CompoundRole.INHIBITOR.getName()))
                .map(this::toCompound)
                .collect(Collectors.toList());

        List<Compound> activators = compounds
                .stream()
                .filter(Objects::nonNull)
                .filter(role -> role.getRole().equalsIgnoreCase(CompoundRole.ACTIVATOR.getName()))
                .map(this::toCompound)
                .collect(Collectors.toList());

        return SmallMolecule.builder()
                .activators(activators)
                .inhibitors(inhibitors)
                .cofactors(cofactors)
                .build();
    }

    private Compound toCompound(CompoundView compoundView) {

        return Compound.builder().compoundId(compoundView.getId())
                .compoundName(compoundView.getName())
                .compoundSource(compoundView.getSource())
                .build();
    }

    private PdbInfo toPdbInfo(PdbView pdb) {

        return PdbInfo
                .builder()
                .pdbAccession(pdb.getId())
                .pdbName(pdb.getName())
                .build();
    }

    private EnzymeDisease toEnzymeDisease(DiseaseView disease) {

        return EnzymeDisease.builder()
                .omimNumber(disease.getOmimNumber())
                .diseaseName(disease.getDiseaseName())
                .description(disease.getDescription())
                .url(disease.getUrl())
                .diseaseEvidences(disease.getEvidences())
                .build();
    }

    private String shortToString(Short s) {

        if (s == 0) {
            return "Reviewed";
        } else {
            return "Unreviewed";
        }

    }

    private boolean getCode(BigInteger code) {
        return code == BigInteger.ONE;
    }

}
