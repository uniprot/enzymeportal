/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.service;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.StringExpression;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.data.domain.EnzymePortalCompound;
import uk.ac.ebi.ep.data.domain.EnzymePortalDisease;
import uk.ac.ebi.ep.data.domain.EnzymePortalEcNumbers;
import uk.ac.ebi.ep.data.domain.EnzymePortalPathways;
import uk.ac.ebi.ep.data.domain.EnzymePortalReaction;
import uk.ac.ebi.ep.data.domain.EnzymePortalSummary;
import uk.ac.ebi.ep.data.domain.QUniprotEntry;
import uk.ac.ebi.ep.data.domain.RelatedProteins;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.domain.UniprotXref;
import uk.ac.ebi.ep.data.enzyme.model.EnzymeReaction;
import uk.ac.ebi.ep.data.enzyme.model.Pathway;
import uk.ac.ebi.ep.data.repositories.DiseaseRepository;
import uk.ac.ebi.ep.data.repositories.EnzymePortalCompoundRepository;
import uk.ac.ebi.ep.data.repositories.EnzymePortalEcNumbersRepository;
import uk.ac.ebi.ep.data.repositories.EnzymePortalPathwaysRepository;
import uk.ac.ebi.ep.data.repositories.EnzymePortalReactionRepository;
import uk.ac.ebi.ep.data.repositories.EnzymePortalSummaryRepository;
import uk.ac.ebi.ep.data.repositories.RelatedProteinsRepository;
import uk.ac.ebi.ep.data.repositories.UniprotEntryRepository;
import uk.ac.ebi.ep.data.repositories.UniprotXrefRepository;
import uk.ac.ebi.ep.data.search.model.Compound;
import uk.ac.ebi.ep.data.search.model.Disease;
import uk.ac.ebi.ep.data.search.model.Species;
import uk.ac.ebi.ep.data.search.model.Taxonomy;

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
    private EnzymePortalSummaryRepository enzymeSummaryRepository;

    @Autowired
    private EnzymePortalReactionRepository reactionRepository;

    @Autowired
    private UniprotXrefRepository xrefRepository;

    @Autowired
    private EnzymePortalPathwaysRepository pathwaysRepository;

    @Autowired
    private RelatedProteinsRepository relatedProteinsRepository;

    @Autowired
    private EnzymePortalEcNumbersRepository ecNumbersRepository;

    @Transactional(readOnly = true)
    public UniprotEntry findByAccession(String accession) {

        return uniprotEntryRepository.findEnzymeByAccession(accession);
    }

    @Transactional(readOnly = true)
    public EnzymePortalSummary findEnzymeSummaryByAccession(String accession) {

        return enzymeSummaryRepository.findEnzymeSummaryByAccession(accession);
    }

    @Transactional(readOnly = true)
    public List<String> findAllUniprotAccessions() {

        return uniprotEntryRepository.findAccessions();
    }

    @Transactional(readOnly = true)
    public List<EnzymePortalDisease> findAllDiseases() {

        return diseaseRepository.findAll();
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

    @Deprecated
    @Transactional(readOnly = true)
    public List<String> findAccessionsByCompoundID(String compoundId) {
        List<String> accessions = new ArrayList<>();
        Iterable<UniprotEntry> enzymes = uniprotEntryRepository.findAll(enzymesByCompoundId(compoundId));
        for (UniprotEntry acc : enzymes) {
            accessions.add(acc.getAccession());
        }

        return accessions;
    }

    @Transactional(readOnly = true)
    public List<UniprotEntry> findEnzymesByAccession(String accession) {

        return uniprotEntryRepository.findEnzymesByAccession(accession);
    }

    @Transactional(readOnly = true)
    public List<UniprotEntry> findEnzymesByAccessions(List<String> accessions) {

        return uniprotEntryRepository.findEnzymesByAccessions(accessions);
    }

    @Transactional(readOnly = true)
    public List<EnzymePortalSummary> findEnzymeSummariesByNamePrefixes(List<String> namePrefixes) {

        return enzymeSummaryRepository.findEnzymesByNamePrefixes(namePrefixes);
    }

    @Transactional(readOnly = true)
    public List<EnzymePortalSummary> findEnzymeSumariesByAccessions(List<String> accessions) {

        return enzymeSummaryRepository.findEnzymeSummariesByAccessions(accessions);
    }

    @Transactional(readOnly = true)
    public List<EnzymePortalSummary> findEnzymeSumariesByAccession(String accession) {

        return enzymeSummaryRepository.findEnzymeSummariesByAccession(accession);
    }

    @Transactional(readOnly = true)
    public Page<EnzymePortalSummary> findEnzymeSumariesByAccessions(List<String> accessions, Pageable pageable) {

        return enzymeSummaryRepository.findEnzymeSummariesByAccessions(accessions, pageable);
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

        return xrefRepository.findPDBcodesByAccession(accession);
    }

    @Transactional(readOnly = true)
    public List<Pathway> findPathwaysByAccession(String accession) {

        List<EnzymePortalPathways> enzymePortalPathways = pathwaysRepository.findPathwaysByAccession(accession);
        if (enzymePortalPathways != null) {
            List<Pathway> pathways = new ArrayList<>();

            enzymePortalPathways.stream().map(ep -> new Pathway(ep.getPathwayId(), ep.getPathwayName())).forEach(pathway -> {
                pathways.add(pathway);
            });

            return pathways;

        }

        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<String> filterEnzymesInAccessions(List<String> accessions) {

        return uniprotEntryRepository.filterEnzymesInAccessions(accessions);
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

        return enzymePortalCompoundRepository.findEnzymesByCompound(compoundId);
    }

    @Transactional(readOnly = true)
    @Deprecated
    public List<String> findAccessionsByMeshId(String meshId) {

        return diseaseRepository.findAccessionsByMeshId(meshId);
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
    public List<EnzymePortalEcNumbers> findByEcNumbersByAccession(String accession) {

        return ecNumbersRepository.findByEcNumbersByAccession(accession);
    }

    @Transactional(readOnly = true)
    public List<EnzymePortalReaction> findReactions() {

        return reactionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<EnzymePortalPathways> findPathways() {

        return pathwaysRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<String> findAccessionsByReactionId(String reactionId) {

        return reactionRepository.findAccessionsByReactionId(reactionId);
    }

    @Transactional(readOnly = true)
    public List<EnzymeReaction> findReactionsByAccession(String accession) {

        //return reactionRepository.findReactionsByAccession(accession);
        List<EnzymePortalReaction> enzreactions = reactionRepository.findReactionsByAccession(accession);
        List<EnzymeReaction> reactions = new ArrayList<>();
        if (enzreactions != null) {
            enzreactions.stream().map(r -> new EnzymeReaction(r.getReactionId(), r.getReactionName())).forEach(er -> {
                reactions.add(er);
            });

            return reactions;
        }
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<String> findAccessionsByPathwayId(String pathwayId) {

        return pathwaysRepository.findAccessionsByPathwayId(pathwayId);
    }

    @Transactional(readOnly = true)
    public List<String> findAccessionsByTaxId(Long taxId) {

        return uniprotEntryRepository.findAccessionsByTaxId(taxId);
    }

    @Transactional(readOnly = true)
    public List<Taxonomy> getCountForOrganisms(List<Long> taxids) {
        return uniprotEntryRepository.getCountForOrganisms(taxids);
    }

    public List<UniprotEntry> findEnzymesByTaxId(Long taxId) {
        return uniprotEntryRepository.findEnzymesByTaxId(taxId);
    }

    @Transactional(readOnly = true)
    public Page<UniprotEntry> findEnzymesByTaxonomy(Long taxId, Pageable pageable) {

        return uniprotEntryRepository.findAll(enzymesByTaxId(taxId), pageable);
    }

    @Transactional(readOnly = true)
    public Page<UniprotEntry> findEnzymesByAccessions(List<String> accessions, Pageable pageable) {

        return uniprotEntryRepository.findAll(enzymesByAccessions(accessions), pageable);

    }

    @Transactional(readOnly = true)
    public Page<UniprotEntry> findEnzymesByEcNumbers(List<String> ecNumbers, Pageable pageable) {

        return uniprotEntryRepository.findAll(enzymesByEcNumbers(ecNumbers), pageable);

    }

    @Transactional(readOnly = true)
    public Page<UniprotEntry> findEnzymesByNamePrefixes(List<String> namePrefixes, Pageable pageable) {

        //Pageable pageable = new PageRequest(0, 20);
        return uniprotEntryRepository.findAll(enzymesByNamePrefixes(namePrefixes), pageable);
    }

    @Transactional(readOnly = true)
    public List<UniprotEntry> findEnzymesByNamePrefixes(List<String> namePrefixes) {

        return uniprotEntryRepository.findEnzymesByNamePrefixes(namePrefixes);
    }

    @Transactional(readOnly = true)
    public List<UniprotEntry> findEnzymesByPathwayId(String pathwayId) {

        return uniprotEntryRepository.findEnzymesByPathwayId(pathwayId);
    }

    @Transactional(readOnly = true)
    public List<UniprotEntry> findEnzymesByEc(String ec) {

        return uniprotEntryRepository.findEnzymesByEc(ec);
    }

    private static Predicate enzymesByNamePrefixes(List<String> namePrefixes) {
        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;
        StringExpression idPrefix = enzyme.relatedProteinsId.namePrefix;
        BooleanBuilder builder = new BooleanBuilder();
        namePrefixes.stream().forEach((prefix) -> {

            builder.or(idPrefix.equalsIgnoreCase(prefix));

        });
        Predicate predicate = builder;
        return predicate;
    }

    private static Predicate enzymesByCompoundId(String compoundId) {

        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;

        Predicate predicate = enzyme.enzymePortalCompoundSet.any().compoundId.equalsIgnoreCase(compoundId);

        return predicate;
    }

    private static Predicate enzymesByTaxId(Long taxId) {
        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;
        Predicate predicate = enzyme.taxId.eq(taxId);
        return predicate;
    }

    private static Predicate enzymesByAccessions(List<String> accessions) {
        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;
        Predicate predicate = enzyme.accession.in(accessions);
        return predicate;
    }

    private static Predicate enzymesByEcNumbers(List<String> ecNumbers) {
        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;
        Predicate predicate = enzyme.enzymePortalEcNumbersSet.any().ecNumber.in(ecNumbers);
        return predicate;
    }

    @Transactional(readOnly = true)
    public List<Pathway> findPathwaysByName(String pathwayName) {

        return pathwaysRepository.findPathwaysByName(pathwayName);
    }

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

    public List<Disease> findDiseasesByTaxId(Long taxId) {
        return diseaseRepository.findDiseasesByTaxId(taxId);
    }

    public List<Species> findSpeciesByTaxId(Long taxId) {
        return uniprotEntryRepository.findSpeciesByTaxId(taxId);
    }

    public List<Compound> findCompoundsByTaxId(Long taxId) {
        return enzymePortalCompoundRepository.findCompoundsByTaxId(taxId);
    }

    //filter facets search
    @Transactional(readOnly = true)
    public Page<UniprotEntry> filterBySpecieAndCompoundsAndDiseases(Long taxId, List<String> compoudNames, List<String> diseaseNames, Pageable pageable) {

        return uniprotEntryRepository.findAll(filterBySpecieAndCompoundsAndDiseases(taxId, compoudNames, diseaseNames), pageable);
    }

    @Transactional(readOnly = true)
    public Page<UniprotEntry> filterBySpecieAndCompounds(Long taxId, List<String> compoudNames, Pageable pageable) {

        return uniprotEntryRepository.findAll(filterBySpecieAndCompounds(taxId, compoudNames), pageable);
    }

    @Transactional(readOnly = true)
    public Page<UniprotEntry> filterBySpecieAndDiseases(Long taxId, List<String> diseaseNames, Pageable pageable) {

        return uniprotEntryRepository.findAll(filterBySpecieAndDiseases(taxId, diseaseNames), pageable);
    }

    @Transactional(readOnly = true)
    public Page<UniprotEntry> filterBySpecie(Long taxId, Pageable pageable) {

        return uniprotEntryRepository.findAll(filterBySpecie(taxId), pageable);
    }

    private static Predicate filterBySpecie(Long taxId) {

        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;

        Predicate predicate = enzyme.taxId.eq(taxId);

        return predicate;
    }

    private static Predicate filterBySpecieAndCompounds(Long taxId, List<String> compoudNames) {

        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;

        Predicate compound = enzyme.enzymePortalCompoundSet.any().compoundName.in(compoudNames);

        Predicate predicate = enzyme.taxId.eq(taxId).and(compound);

        return predicate;
    }

    private static Predicate filterBySpecieAndDiseases(Long taxId, List<String> diseaseNames) {

        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;

        Predicate disease = enzyme.enzymePortalDiseaseSet.any().diseaseName.in(diseaseNames);

        Predicate predicate = enzyme.taxId.eq(taxId).and(disease);

        return predicate;
    }

    private static Predicate filterBySpecieAndCompoundsAndDiseases(Long taxId, List<String> compoudNames, List<String> diseaseNames) {

        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;

        Predicate compound = enzyme.enzymePortalCompoundSet.any().compoundName.in(compoudNames);
        Predicate disease = enzyme.enzymePortalDiseaseSet.any().diseaseName.in(diseaseNames);

        Predicate predicate = enzyme.taxId.eq(taxId).and(compound).and(disease);

        return predicate;
    }
}
