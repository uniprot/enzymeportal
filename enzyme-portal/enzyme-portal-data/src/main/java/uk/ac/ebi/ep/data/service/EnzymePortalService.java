package uk.ac.ebi.ep.data.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.StringExpression;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.data.domain.EnzymePortalCompound;
import uk.ac.ebi.ep.data.domain.EnzymePortalDisease;
import uk.ac.ebi.ep.data.domain.EnzymePortalEcNumbers;
import uk.ac.ebi.ep.data.domain.EnzymePortalPathways;
import uk.ac.ebi.ep.data.domain.EnzymePortalReaction;
import uk.ac.ebi.ep.data.domain.ProjectedSpecies;
import uk.ac.ebi.ep.data.domain.QUniprotEntry;
import uk.ac.ebi.ep.data.domain.RelatedProteins;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.domain.UniprotXref;
import uk.ac.ebi.ep.data.entry.AssociatedProtein;
import uk.ac.ebi.ep.data.entry.Protein;
import uk.ac.ebi.ep.data.enzyme.model.EnzymeReaction;
import uk.ac.ebi.ep.data.enzyme.model.Pathway;
import uk.ac.ebi.ep.data.repositories.DiseaseRepository;
import uk.ac.ebi.ep.data.repositories.EnzymePortalCompoundRepository;
import uk.ac.ebi.ep.data.repositories.EnzymePortalEcNumbersRepository;
import uk.ac.ebi.ep.data.repositories.EnzymePortalPathwaysRepository;
import uk.ac.ebi.ep.data.repositories.EnzymePortalReactionRepository;
import uk.ac.ebi.ep.data.repositories.EnzymesToTaxonomyRepository;
import uk.ac.ebi.ep.data.repositories.RelatedProteinsRepository;
import uk.ac.ebi.ep.data.repositories.UniprotEntryRepository;
import uk.ac.ebi.ep.data.repositories.UniprotXrefRepository;
import uk.ac.ebi.ep.data.search.model.Compound;
import uk.ac.ebi.ep.data.search.model.Disease;
import uk.ac.ebi.ep.data.search.model.EcNumber;
import uk.ac.ebi.ep.data.search.model.Species;
import uk.ac.ebi.ep.data.search.model.Taxonomy;
import uk.ac.ebi.ep.data.view.DiseaseView;
import uk.ac.ebi.ep.data.view.PathwayView;

/**
 *
 * @author joseph
 */
@Transactional
@Service
public class EnzymePortalService {

    @Autowired
    private DiseaseRepository diseaseRepository;
    @Autowired
    private UniprotEntryRepository uniprotEntryRepository;

    @Autowired
    private EnzymePortalCompoundRepository enzymePortalCompoundRepository;

    @Autowired
    private EnzymePortalReactionRepository enzymePortalReactionRepository;

    @Autowired
    private UniprotXrefRepository xrefRepository;

    @Autowired
    private EnzymePortalPathwaysRepository pathwaysRepository;

    @Autowired
    private RelatedProteinsRepository relatedProteinsRepository;

    @Autowired
    private EnzymePortalEcNumbersRepository ecNumbersRepository;

//    @Autowired
//    private EnzymeCatalyticActivityRepository catalyticActivityRepository;
    @Autowired
    private ProjectionFactory projectionFactory;

    @Autowired
    private EnzymesToTaxonomyRepository enzymesToTaxonomyRepository;
//    @Autowired
//    private IntenzEnzymesRepository intenzEnzymesRepository;

    private static final int ORACLE_IN_CLAUSE_LIMIT = 1000;
    private static final int QUERY_LIMIT = 999;
    private static final int START_INDEX = 0;
//
//    @Transactional(readOnly = true)
//    public IntenzEnzymes findIntenzEnzymesByEc(String ecNumber) {
//        return intenzEnzymesRepository.findByEcNumber(ecNumber);
//    }
//
//    @Transactional(readOnly = true)
//    public List<EnzymeCatalyticActivity> findEnzymeCatalyticActivities() {
//
//        return catalyticActivityRepository.findAllEnzymeCatalyticActivity();
//    }

    @Transactional(readOnly = true)
    public UniprotEntry findByAccession(String accession) {

        return uniprotEntryRepository.findEnzymeByAccession(accession);
    }

    public List<UniprotEntry> findUniprotEntriesByRelatedProteinId(BigDecimal relProtId) {
        return uniprotEntryRepository.findByRelatedProteinId(relProtId);
    }

    @Transactional(readOnly = true)
    public List<String> findAllUniprotAccessions() {

        return uniprotEntryRepository.findAccessions();
    }

    @Transactional(readOnly = true)
    public List<String> findAllSwissProtAccessions() {

        return uniprotEntryRepository.findSwissProtAccessions();
    }

    @Transactional(readOnly = true)
    public List<DiseaseView> findAllDiseases() {

        return diseaseRepository.findAllDiseases();
    }

    //******global methods for the web apps******************
    public List<EnzymePortalCompound> findCompoundsByUniprotAccession(String accession) {

        return enzymePortalCompoundRepository.findCompoundsByUniprotAccession(accession);
    }

    @Transactional(readOnly = true)
    public List<EnzymePortalCompound> findCompoundsByUniprotName(String uniprotName) {

        return enzymePortalCompoundRepository.findCompoundsByUniprotName(uniprotName);
    }

    @Transactional(readOnly = true)
    public List<EnzymePortalCompound> findCompoundsByNamePrefix(List<String> namePrefixes) {

        return enzymePortalCompoundRepository.findCompoundsByNameprefixes(namePrefixes);
    }

    @Transactional(readOnly = true)
    public List<String> findCatalyticActivitiesByAccession(String accession) {

        ///return enzymeSummaryRepository.findCatalyticActivitiesByAccession(accession);
        return uniprotEntryRepository.findCatalyticActivitiesByAccession(accession);
    }

    @Transactional(readOnly = true)
    public List<Disease> findDiseasesByAccession(String accession) {

        return diseaseRepository.findDiseasesByAccession(accession);
    }

    @Transactional(readOnly = true)
    public List<EnzymePortalDisease> findDiseasesByNamePrefix(List<String> namePrefixes) {

        return diseaseRepository.findDiseasesByNamePrefixes(namePrefixes);
    }

    @Transactional(readOnly = true)
    public List<UniprotXref> findPDBcodesByAccession(String accession) {

        return xrefRepository.findPdbCodesByAccession(accession);
    }

    @Transactional(readOnly = true)
    public List<Pathway> findPathwaysByAccession(String accession) {

        List<EnzymePortalPathways> enzymePortalPathways = pathwaysRepository.findPathwaysByAccession(accession);
        if (enzymePortalPathways != null) {
            List<Pathway> pathways = new ArrayList<>();

            enzymePortalPathways.stream().map(ep -> new Pathway(ep.getPathwayGroupId(), ep.getPathwayId(), ep.getPathwayName())).forEach(pathway -> {
                pathways.add(pathway);
            });

            return pathways;

        }

        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<String> filterEnzymesInAccessions(List<String> accessions) {

        return uniprotEntryRepository.filterEnzymesFromAccessions(accessions);
    }

    @Transactional(readOnly = true)
    public RelatedProteins findRelatedProteinsByNamePrefix(String nameprefix) {

        return relatedProteinsRepository.findByNamePrefix(nameprefix);
    }

    @Transactional(readOnly = true)
    public List<RelatedProteins> findRelatedProteinsByNamePrefixes(List<String> nameprefixes) {

        return relatedProteinsRepository.findRelatedProteinsByNamePrefixes(nameprefixes);
    }

    @Transactional(readOnly = true)
    public List<String> findEnzymesByCompound(String compoundId) {

        return uniprotEntryRepository.findEnzymesByCompound(compoundId);
    }

    @Transactional(readOnly = true)
    public List<EnzymePortalDisease> findDiseases() {

        return diseaseRepository.findDiseases();
    }

    @Transactional(readOnly = true)
    public List<String> findAccessionsByEc(String ecNumber) {

        return ecNumbersRepository.findAccessionsByEc(ecNumber);
    }

    @Transactional(readOnly = true)
    public List<String> findAccessionsByEc(String ecNumber, int limit) {

        return ecNumbersRepository.findAccessionsByEc(ecNumber, limit);
    }

    @Transactional(readOnly = true)
    public List<EnzymePortalEcNumbers> findByEcNumbersByAccession(String accession) {

        return ecNumbersRepository.findByEcNumbersByAccession(accession);
    }

    @Transactional(readOnly = true)
    public Set<EnzymePortalEcNumbers> findEcNumbersByAccession(String accession) {

        return ecNumbersRepository.findEcNumbersByAccession(accession);
    }

    @Transactional(readOnly = true)
    public List<PathwayView> findPathways() {

        return pathwaysRepository.findPathways();

    }

//        @Transactional(readOnly = true)
//    public List<EnzymePortalPathways> findAllPathways() {
//        return pathwaysRepository.findAllPathways();
//}
//    @Transactional(readOnly = true)
//    public List<EnzymePortalPathways> findPathways() {
//
//        return pathwaysRepository.findPathways()
//                .stream()
//                .distinct()
//                .parallel()
//                .collect(Collectors.toList());
//    }
//    @Transactional(readOnly = true)
//    public List<String> findAccessionsByReactionId(String reactionId) {
//
//        return reactionRepository.findAccessionsByReactionId(reactionId);
//    }
    @Transactional(readOnly = true)
    public List<EnzymeReaction> findReactionsByAccession(String accession) {

        //return reactionRepository.findReactionsByAccession(accession);
        List<EnzymePortalReaction> enzreactions = enzymePortalReactionRepository.findReactionsByAccession(accession);
        List<EnzymeReaction> reactions = new ArrayList<>();
        if (enzreactions != null) {
            enzreactions.stream().map(r -> new EnzymeReaction("RHEA:"+r.getReactionId(), r.getKeggId()))
                    .forEach(er -> reactions.add(er));

            return reactions;
        }
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<String> findAccessionsByPathwayId(String pathwayId) {

        return pathwaysRepository.findAccessionsByPathwayId(pathwayId);
    }

    @Transactional(readOnly = true)
    public List<String> findAccessionsByPathwayName(String pathwayName) {

        return pathwaysRepository.findAccessionsByPathwayName(pathwayName);
    }

    @Transactional(readOnly = true)
    public List<String> findAccessionsByTaxId(Long taxId) {

        return uniprotEntryRepository.findAccessionsByTaxId(taxId);
    }

    @Transactional(readOnly = true)
    public List<Taxonomy> getCountForOrganisms(List<Long> taxids) {
        return enzymesToTaxonomyRepository.getCountForOrganisms(taxids);
    }

    public List<UniprotEntry> findEnzymesByTaxId(Long taxId) {
        return uniprotEntryRepository.findEnzymesByTaxId(taxId);
    }

    @Transactional(readOnly = true)
    public Page<UniprotEntry> findEnzymesByTaxonomy(Long taxId, Pageable pageable) {

        return uniprotEntryRepository.findAll(enzymesByTaxId(taxId), pageable);
    }

    @Transactional(readOnly = true)
    public UniprotEntry findEnzymeByAccession(String accession) {

        //return uniprotEntryRepository.findByAccession(accession);
        return uniprotEntryRepository.findEnzymeByAccession(accession);

    }

    @Transactional(readOnly = true)
    public List<UniprotEntry> findEnzymesByAcc(List<String> accessions) {

        return uniprotEntryRepository.findSummariesByAcc(accessions);
    }

    @Transactional(readOnly = true)
    public List<UniprotEntry> findEnzymesByAccessions(List<String> accessions) {
        //return uniprotEntryRepository.findSummariesByAccessions(accessions).stream().distinct().filter(Objects::nonNull).collect(Collectors.toList());

        return uniprotEntryRepository.findSummariesByAccessions(accessions);//.stream().map(EnzymePortal::new).distinct().map(EnzymePortal::unwrapProtein).filter(Objects::nonNull).collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public Page<UniprotEntry> findEnzymesByAccessions(List<String> accessions, Pageable pageable) {
        return uniprotEntryRepository.findSummariesByAccessions(accessions, pageable);//.stream().map(EnzymePortal::new).distinct().map(EnzymePortal::unwrapProtein).filter(Objects::nonNull).collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public Stream<List<UniprotEntry>> findStreamedSummariesByAccessions(List<String> accessions, Pageable pageable) {
        //return uniprotEntryRepository.findStreamedSummariesByAccessions(accessions).stream().distinct().filter(Objects::nonNull).collect(Collectors.toList());

        return uniprotEntryRepository.findStreamedSummariesByAccessions(accessions, pageable);
    }

    @Transactional(readOnly = true)
    public Slice<UniprotEntry> findSlicedSummariesByAccessions(List<String> accessions, Pageable pageable) {

        return uniprotEntryRepository.findSlicedSummariesByAccessions(accessions, pageable);

    }

    @Transactional(readOnly = true)
    public Future<List<UniprotEntry>> findFutureSummariesByAccessions(List<String> accessions) {

        return uniprotEntryRepository.findFutureSummariesByAccessions(accessions);

    }

    @Transactional(readOnly = true)
    public List<UniprotEntry> findEnzymePortalByAccessions(String accession) {
        //return uniprotEntryRepository.findSummariesByAccessions(accessions).stream().distinct().filter(Objects::nonNull).collect(Collectors.toList());

        return uniprotEntryRepository.findEnzymePortalByAccession(accession).stream().distinct().collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<EnzymePortalCompound> findCompoundsByAccessions(List<String> accessions) {
        return enzymePortalCompoundRepository.findCompoundsByAccessions(accessions);
    }

    @Transactional(readOnly = true)
    public List<EnzymePortalCompound> findCompoundsByAccession(String accession) {
        return enzymePortalCompoundRepository.findCompoundsByAccession(accession);
    }

    @Deprecated
    @Transactional(readOnly = true)
    public Page<UniprotEntry> findEnzymesByAccessionsDeprecated(List<String> accessions, Pageable pageable) {

        return uniprotEntryRepository.findAll(enzymesByAccessions(accessions), pageable);

    }

    @Transactional(readOnly = true)
    public Page<UniprotEntry> findEnzymesByAccessionsIn(List<String> accessions, Pageable pageable) {

        return uniprotEntryRepository.findDistinctByAccessionIn(accessions, pageable);

    }

    @Transactional(readOnly = true)
    public Page<UniprotEntry> findEnzymesByEcNumbers(List<String> ecNumbers, Pageable pageable) {

        return uniprotEntryRepository.findAll(enzymesByEcNumbers(ecNumbers), pageable);

    }

    @Transactional(readOnly = true)
    public Page<UniprotEntry> findEnzymesByNamePrefixes(List<String> namePrefixes, Pageable pageable) {

        return uniprotEntryRepository.findAll(enzymesByNamePrefixes(namePrefixes), pageable);
    }

    @Transactional(readOnly = true)
    public List<UniprotEntry> findEnzymesByNamePrefixes(List<String> namePrefixes) {

        return uniprotEntryRepository.findEnzymesByNamePrefixes(namePrefixes);
    }

    @Deprecated
    @Transactional(readOnly = true)
    public List<UniprotEntry> findEnzymesByPathwayId(String pathwayId) {

        return uniprotEntryRepository.findEnzymesByPathwayId(pathwayId);
    }

    @Transactional(readOnly = true)
    public List<UniprotEntry> findEnzymesByEc(String ec) {

        return uniprotEntryRepository.findEnzymesByEc(ec);
    }

    @Transactional(readOnly = true)
    public List<Protein> findProteinByEc(String ec) {

        return uniprotEntryRepository.findProteinByEc(ec);
    }

    private static Predicate enzymesByNamePrefixes(List<String> namePrefixes) {
        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;
        StringExpression idPrefix = enzyme.relatedProteinsId.namePrefix;
        BooleanBuilder builder = new BooleanBuilder();
        namePrefixes.stream().forEach(prefix -> {

            builder.or(idPrefix.equalsIgnoreCase(prefix));

        });
        return builder;
    }

    private static Predicate enzymesByTaxId(Long taxId) {
        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;
        return enzyme.taxId.eq(taxId);

    }

    private static Predicate enzymesByAccessions(List<String> accessions) {
        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;
        return enzyme.accession.in(accessions);

    }

    private static Predicate enzymesByEcNumbers(List<String> ecNumbers) {
        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;
        return enzyme.enzymePortalEcNumbersSet.any().ecNumber.in(ecNumbers);

    }

    @Transactional(readOnly = true)
    public List<Pathway> findPathwaysByName(String pathwayName) {

        return pathwaysRepository.findPathwaysByName(pathwayName);
    }

    @Deprecated
    @Transactional(readOnly = true)
    public List<UniprotEntry> findEnzymesByMeshId(String meshId) {

        return uniprotEntryRepository.findEnzymesByMeshId(meshId);
    }

    /**
     *
     * @param diseaseName formated name e.g name =
     * String.format(%%%s%%,diseaseName)
     * @return matched diseases
     */
    @Transactional(readOnly = true)
    public List<Disease> findDiseasesLike(String diseaseName) {
        return diseaseRepository.findDiseasesNameLike(diseaseName);
    }

    @Transactional(readOnly = true)
    public List<Disease> findDiseasesByTaxId(Long taxId) {
        return diseaseRepository.findDiseasesByTaxId(taxId);
    }

    @Transactional(readOnly = true)
    public List<Species> findSpeciesByTaxId(Long taxId) {
        return uniprotEntryRepository.findSpeciesByTaxId(taxId);
    }

    @Transactional(readOnly = true)
    public List<Compound> findCompoundsByTaxId(Long taxId) {
        return enzymePortalCompoundRepository.findCompoundsByTaxId(taxId);
    }

    @Transactional(readOnly = true)
    public List<EcNumber> findEnzymeFamiliesByTaxId(Long taxId) {
        return ecNumbersRepository.findEnzymeFamiliesByTaxId(taxId);

    }

    @Transactional(readOnly = true)
    public List<String> findAccessionsByEcNumber(String ecNumber) {
        return ecNumbersRepository.findAccessionsByEcNumber(ecNumber);

    }

    @Transactional(readOnly = true)
    public List<String> findAccessionsByOmimNumber(String omimNumber) {
        return diseaseRepository.findAccessionsByOmimNumber(omimNumber);

    }

    //filter facets search
    @Transactional(readOnly = true)
    public Page<UniprotEntry> filterBySpecieAndCompoundsAndDiseases(Long taxId, List<String> compoundIds, List<String> omimNumber, Pageable pageable) {

        return uniprotEntryRepository.findAll(filterBySpecieAndCompoundsAndDiseases(taxId, compoundIds, omimNumber), pageable);
    }

    @Transactional(readOnly = true)
    public Page<UniprotEntry> filterBySpecieAndCompounds(Long taxId, List<String> compoundIds, Pageable pageable) {

        return uniprotEntryRepository.findAll(filterBySpecieAndCompounds(taxId, compoundIds), pageable);
    }

    @Transactional(readOnly = true)
    public Page<UniprotEntry> filterBySpecieAndDiseases(Long taxId, List<String> omimNumber, Pageable pageable) {

        return uniprotEntryRepository.findAll(filterBySpecieAndDiseases(taxId, omimNumber), pageable);
    }

    @Transactional(readOnly = true)
    public Page<UniprotEntry> filterBySpecie(Long taxId, Pageable pageable) {

        return uniprotEntryRepository.findAll(filterBySpecie(taxId), pageable);
    }

    @Transactional(readOnly = true)
    public Page<UniprotEntry> filterBySpecieAndEc(Long taxId, List<Integer> ecList, Pageable pageable) {

        return uniprotEntryRepository.findAll(filterBySpecieAndEc(taxId, ecList), pageable);
    }

    @Transactional(readOnly = true)
    public Page<UniprotEntry> filterBySpecieAndCompoundsAndEc(Long taxId, List<String> compoundIds, List<Integer> ecList, Pageable pageable) {

        return uniprotEntryRepository.findAll(filterBySpecieAndCompoundsAndEc(taxId, compoundIds, ecList), pageable);
    }

    @Transactional(readOnly = true)
    public Page<UniprotEntry> filterBySpecieAndDiseasesAndEc(Long taxId, List<String> omimNumber, List<Integer> ecList, Pageable pageable) {

        return uniprotEntryRepository.findAll(filterBySpecieAndDiseasesAndEc(taxId, omimNumber, ecList), pageable);
    }

    @Transactional(readOnly = true)
    public Page<UniprotEntry> filterBySpecieAndCompoundsAndDiseasesAndEc(Long taxId, List<String> compoundIds, List<String> omimNumber, List<Integer> ecList, Pageable pageable) {

        return uniprotEntryRepository.findAll(filterBySpecieAndCompoundsAndDiseasesAndEc(taxId, compoundIds, omimNumber, ecList), pageable);
    }

    private static Predicate filterBySpecie(Long taxId) {

        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;

        Predicate predicate = enzyme.taxId.eq(taxId);

        return predicate;
    }

    private static Predicate filterBySpecieAndCompounds(Long taxId, List<String> compoundIds) {

        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;

        Predicate compound = enzyme.enzymePortalCompoundSet.any().compoundId.in(compoundIds);

        return enzyme.taxId.eq(taxId).and(compound);

    }

    private static Predicate filterBySpecieAndDiseases(Long taxId, List<String> omimNumber) {

        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;

        Predicate disease = enzyme.enzymePortalDiseaseSet.any().omimNumber.in(omimNumber);

        return enzyme.taxId.eq(taxId).and(disease);

    }

    private static Predicate filterBySpecieAndCompoundsAndDiseases(Long taxId, List<String> compoudNames, List<String> omimNumber) {

        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;

        Predicate compound = enzyme.enzymePortalCompoundSet.any().compoundId.in(compoudNames);
        Predicate disease = enzyme.enzymePortalDiseaseSet.any().omimNumber.in(omimNumber);

        return enzyme.taxId.eq(taxId).and(compound).and(disease);

    }

    private static Predicate filterBySpecieAndEc(Long taxId, List<Integer> ecList) {

        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;

        Predicate ec = enzyme.enzymePortalEcNumbersSet.any().ecFamily.in(ecList);

        return enzyme.taxId.eq(taxId).and(ec);

    }

    private static Predicate filterBySpecieAndCompoundsAndDiseasesAndEc(Long taxId, List<String> compoudNames, List<String> omimNumber, List<Integer> ecList) {

        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;

        Predicate compound = enzyme.enzymePortalCompoundSet.any().compoundId.in(compoudNames);
        Predicate disease = enzyme.enzymePortalDiseaseSet.any().omimNumber.in(omimNumber);
        Predicate ec = enzyme.enzymePortalEcNumbersSet.any().ecFamily.in(ecList);

        return enzyme.taxId.eq(taxId).and(compound).and(disease).and(ec);

    }

    private static Predicate filterBySpecieAndCompoundsAndEc(Long taxId, List<String> compoudNames, List<Integer> ecList) {

        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;

        Predicate compound = enzyme.enzymePortalCompoundSet.any().compoundId.in(compoudNames);
        Predicate ec = enzyme.enzymePortalEcNumbersSet.any().ecFamily.in(ecList);
        return enzyme.taxId.eq(taxId).and(compound).and(ec);

    }

    private static Predicate filterBySpecieAndDiseasesAndEc(Long taxId, List<String> omimNumber, List<Integer> ecList) {

        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;

        Predicate disease = enzyme.enzymePortalDiseaseSet.any().omimNumber.in(omimNumber);
        Predicate ec = enzyme.enzymePortalEcNumbersSet.any().ecFamily.in(ecList);

        return enzyme.taxId.eq(taxId).and(disease).and(ec);

    }

    @Transactional(readOnly = true)
    public Page<UniprotEntry> findEnzymesByEcNumber(String ecNumber, Pageable pageable) {
        return uniprotEntryRepository.findAll(enzymesByEcNumber(ecNumber), pageable);
        //return uniprotEntryRepository.findEnzymesByEcNumber(ecNumber, pageable);
    }

    private static Predicate enzymesByEcNumber(String ecNumber) {
        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;
        return enzyme.enzymePortalEcNumbersSet.any().ecNumber.eq(ecNumber);

    }

    @Transactional(readOnly = true)
    public List<Species> findSpeciesByEcNumber(String ecNumber) {
        return uniprotEntryRepository.findSpeciesByEcNumber(ecNumber);
    }

    @Transactional(readOnly = true)
    public List<Species> findSpeciesByEcNumber(String ecNumber, List<String> accessions) {
        return uniprotEntryRepository.findSpeciesByEcNumberViaAccessions(ecNumber, accessions);
    }

    @Transactional(readOnly = true)
    public List<Compound> findCompoundsByEcNumber(String ecNumber) {
        return enzymePortalCompoundRepository.findCompoundsByEcNumber(ecNumber);
    }

    @Transactional(readOnly = true)
    public List<Disease> findDiseasesByEcNumber(String ecNumber) {
        return diseaseRepository.findDiseasesByEcNumber(ecNumber);
    }

    @Transactional(readOnly = true)
    public List<EcNumber> findEnzymeFamiliesByEcNumber(String ecNumber) {

        return ecNumbersRepository.findEnzymeFamiliesByEcNumber(ecNumber);
    }

    public Page<UniprotEntry> filterByEc(String ec, Pageable pageable) {

        return uniprotEntryRepository.findAll(filterByEc(ec), pageable);
    }

    private static Predicate filterByEc(String ec) {

        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;

        Predicate result = enzyme.enzymePortalEcNumbersSet.any().ecNumber.eq(ec);

        return result;

    }

    public Page<UniprotEntry> filterByEcAndSpecies(String ec, List<String> specieFilter, Pageable pageable) {
        return uniprotEntryRepository.findAll(filterByEcAndSpecies(ec, specieFilter), pageable);
    }

    private static Predicate filterByEcAndSpecies(String ec, List<String> specieFilter) {

        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;

        Predicate sp = enzyme.scientificName.in(specieFilter);
        Predicate result = enzyme.enzymePortalEcNumbersSet.any().ecNumber.eq(ec).and(sp);

        return result;

    }

    public Page<UniprotEntry> filterByEcAndCompounds(String ec, List<String> compoundFilter, Pageable pageable) {

        return uniprotEntryRepository.findAll(filterByEcAndCompounds(ec, compoundFilter), pageable);
    }

    private static Predicate filterByEcAndCompounds(String ec, List<String> compoundFilter) {

        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;

        Predicate sp = enzyme.enzymePortalCompoundSet.any().compoundId.in(compoundFilter);
        Predicate result = enzyme.enzymePortalEcNumbersSet.any().ecNumber.eq(ec).and(sp);

        return result;

    }

    public Page<UniprotEntry> filterByEcAndDiseases(String ec, List<String> diseaseFilter, Pageable pageable) {
        return uniprotEntryRepository.findAll(filterByEcAndDiseases(ec, diseaseFilter), pageable);
    }

    private static Predicate filterByEcAndDiseases(String ec, List<String> diseaseFilter) {

        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;

        Predicate dis = enzyme.enzymePortalDiseaseSet.any().omimNumber.in(diseaseFilter);
        Predicate result = enzyme.enzymePortalEcNumbersSet.any().ecNumber.eq(ec).and(dis);

        return result;

    }

    public Page<UniprotEntry> filterByEcAndSpeciesAndCompound(String ec, List<String> specieFilter, List<String> compoundFilter, Pageable pageable) {

        return uniprotEntryRepository.findAll(filterByEcAndSpeciesAndCompound(ec, specieFilter, compoundFilter), pageable);
    }

    private static Predicate filterByEcAndSpeciesAndCompound(String ec, List<String> specieFilter, List<String> compoundFilter) {

        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;

        Predicate sp = enzyme.scientificName.in(specieFilter);
        Predicate compound = enzyme.enzymePortalCompoundSet.any().compoundId.in(compoundFilter);

        Predicate result = enzyme.enzymePortalEcNumbersSet.any().ecNumber.eq(ec).and(sp).and(compound);

        return result;

    }

    public Page<UniprotEntry> filterByEcAndSpeciesAndDiseases(String ec, List<String> specieFilter, List<String> diseaseFilter, Pageable pageable) {

        return uniprotEntryRepository.findAll(filterByEcAndSpeciesAndDiseases(ec, specieFilter, diseaseFilter), pageable);
    }

    private static Predicate filterByEcAndSpeciesAndDiseases(String ec, List<String> specieFilter, List<String> diseaseFilter) {

        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;

        Predicate sp = enzyme.scientificName.in(specieFilter);

        Predicate disease = enzyme.enzymePortalDiseaseSet.any().omimNumber.in(diseaseFilter);

        Predicate result = enzyme.enzymePortalEcNumbersSet.any().ecNumber.eq(ec).and(sp).and(disease);

        return result;

    }

    public Page<UniprotEntry> filterByEcAndSpeciesAndCompoundAndDiseases(String ec, List<String> specieFilter, List<String> compoundFilter, List<String> diseaseFilter, Pageable pageable) {

        return uniprotEntryRepository.findAll(filterByEcAndSpeciesAndCompoundAndDiseases(ec, specieFilter, compoundFilter, diseaseFilter), pageable);
    }

    private static Predicate filterByEcAndSpeciesAndCompoundAndDiseases(String ec, List<String> specieFilter, List<String> compoundFilter, List<String> diseaseFilter) {

        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;

        Predicate sp = enzyme.scientificName.in(specieFilter);
        Predicate compound = enzyme.enzymePortalCompoundSet.any().compoundId.in(compoundFilter);
        Predicate disease = enzyme.enzymePortalDiseaseSet.any().omimNumber.in(diseaseFilter);
        Predicate result = enzyme.enzymePortalEcNumbersSet.any().ecNumber.eq(ec).and(sp).and(compound).and(disease);

        return result;

    }

    public Page<UniprotEntry> filterByEcAndCompoundAndDiseases(String ec, List<String> compoundFilter, List<String> diseaseFilter, Pageable pageable) {

        return uniprotEntryRepository.findAll(filterByEcAndCompoundAndDiseases(ec, compoundFilter, diseaseFilter), pageable);
    }

    private static Predicate filterByEcAndCompoundAndDiseases(String ec, List<String> compoundFilter, List<String> diseaseFilter) {

        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;

        Predicate compound = enzyme.enzymePortalCompoundSet.any().compoundId.in(compoundFilter);
        Predicate disease = enzyme.enzymePortalDiseaseSet.any().omimNumber.in(diseaseFilter);

        Predicate result = enzyme.enzymePortalEcNumbersSet.any().ecNumber.eq(ec).and(compound).and(disease);

        return result;

    }

    public Slice<UniprotEntry> findSlicedEnzymesByEcNumber(String ecNumber, Pageable pageable) {

        return uniprotEntryRepository.findSlicedEnzymesByEcNumber(ecNumber, pageable);
    }

    public Page<ProjectedSpecies> findProjectedSpecies(String ec, Pageable pageable) {
        return uniprotEntryRepository.findEnzymesByEcNumber(ec, pageable).map(sp -> projectionFactory.createProjection(ProjectedSpecies.class, sp));
    }

    public UniprotEntry findByUniprotAccession(String ac) {
        return uniprotEntryRepository.findByUniprotAccession(ac);
    }

    public Page<UniprotEntry> findEnzymeViewByEc(String ec, Pageable pageable) {
        List<String> accessions = uniprotEntryRepository.findAccessionViewByEc(ec);
        if (accessions.size() >= ORACLE_IN_CLAUSE_LIMIT) {
            accessions = accessions.subList(START_INDEX, QUERY_LIMIT);
        }
        Page page = uniprotEntryRepository.findDistinctByAccessionIn(accessions, pageable);
        return page;

    }

    public Page<UniprotEntry> findEnzymeViewByEcAndSpecies(String ec, List<String> species, Pageable pageable) {
        List<String> accessions = uniprotEntryRepository.findAccessionViewByEcAndSpecies(ec, species);
        if (accessions.size() >= ORACLE_IN_CLAUSE_LIMIT) {
            accessions = accessions.subList(START_INDEX, QUERY_LIMIT);
        }
        Page page = uniprotEntryRepository.findDistinctByAccessionIn(accessions, pageable);
        return page;

    }

    @Deprecated
    public void updateExpEvidenceFlag() {
        uniprotEntryRepository.updateExpEvidenceFlag();
    }

    @Transactional(readOnly = true)
    public List<AssociatedProtein> findAssociatedProteinsByEcNumber(String ecNumber, String entryType, int limit) {
        return uniprotEntryRepository.findAssociatedProteinsByEC(ecNumber, entryType, limit);
    }

    @Transactional(readOnly = true)
    public List<AssociatedProtein> findAssociatedProteinsByEcNumber(String ecNumber, int limit) {
        return uniprotEntryRepository.findAssociatedProteinsByEC(ecNumber, limit);
    }

    //updated repository queries
    //species by accession
    @Transactional(readOnly = true)
    public List<Species> findSpeciesInAccessions(List<String> accessions) {
        return uniprotEntryRepository.findSpeciesInAccessions(accessions);
    }

    //compounds by accession
    @Transactional(readOnly = true)
    public List<Compound> findCompoundsInAccessions(List<String> accessions) {
        return enzymePortalCompoundRepository.findCompoundsInAccessions(accessions);
    }
    //diseases by accession

    @Transactional(readOnly = true)
    public List<Disease> findDiseasesInAccessions(List<String> accessions) {
        return diseaseRepository.findDiseasesInAccessions(accessions);
    }

    //ec by accession
    @Transactional(readOnly = true)
    public List<EcNumber> findEcNumberInAccessions(List<String> accessions) {
        return ecNumbersRepository.findEcNumberInAccessions(accessions);
    }

    @Transactional(readOnly = true)
    public Page<UniprotEntry> findEnzymesWithAccessions(List<String> accessions, Pageable pageable) {
        // return uniprotEntryRepository.findSummariesByAccessions(accessions, pageable);
        //return uniprotEntryRepository.findEnzymesWithAccessions(accessions, pageable,uniprotEntryRepository);
        // return uniprotEntryRepository.findEnzymesWithAccessions(accessions, pageable);

        return uniprotEntryRepository.findDistinctByAccessionIn(accessions, pageable);
    }

    @Transactional(readOnly = true)
    public Page<UniprotEntry> findEnzymesWithFilter(List<String> accessions, List<String> taxIds, List<String> compoundIds, List<String> omimNumbers, List<Integer> ecNumbers, Pageable pageable) {

        return uniprotEntryRepository.findAll(filterFacets(accessions, taxIds, compoundIds, omimNumbers, ecNumbers), pageable);
    }

    private static Predicate filterFacets(List<String> accessions, List<String> taxIds, List<String> compoundIds, List<String> omimNumbers, List<Integer> ecNumbers) {

        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;
        // Predicate sp = enzyme.taxId.in(taxIds);

        Predicate sp = enzyme.scientificName.in(taxIds);//use tax id after fixing js

//// 
//      JPAExpressions.selectFrom(enzyme).innerJoin(qepc)
//              .where(qepc.compoundId.in(compoundIds));
        Predicate compound = enzyme.enzymePortalCompoundSet.any().compoundId.in(compoundIds);

        Predicate disease = enzyme.enzymePortalDiseaseSet.any().omimNumber.in(omimNumbers);

        Predicate ec = enzyme.enzymePortalEcNumbersSet.any().ecFamily.in(ecNumbers);

        // Predicate protein = enzyme.accession.in(accessions);
        //Predicate filter = ExpressionUtils.allOf(protein,sp, compound, disease, ec);
        //Predicate filter = ExpressionUtils.anyOf(protein, sp, compound, disease,ec);
        //BooleanBuilder builder = new BooleanBuilder();
        //builder.andAnyOf(ec,sp);
        // Expressions.allOf(ec);
        // return  enzyme.accession.in(accessions).and(builder.andAnyOf(ec,sp).getValue());
        //return filter;
        //return enzyme.accession.in(accessions).andAnyOf(sp,ec,compound, disease);//23.246s
        return enzyme.accession.in(accessions).andAnyOf(ExpressionUtils.anyOf(sp, ec, compound, disease));
    }

    private final Set<String> sortedParams = new HashSet<>(
            Arrays.asList("ENTRY_TYPE"));

    /**
     * Define page request with paging and sorting. *
     * @param sortType
     * @param sortProperty
     * @param page
     * @param pageSize
     * @return 
     */
    public PageRequest getPageRequest(String sortType, String sortProperty, int page, int pageSize) {

        //return new PageRequest(page, pageSize, new Sort(new Sort.Order(sortProperty)));
        return new PageRequest(page, pageSize, Sort.Direction.ASC, sortProperty);
    }
}
