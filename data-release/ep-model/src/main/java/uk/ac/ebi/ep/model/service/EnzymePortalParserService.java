package uk.ac.ebi.ep.model.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.model.ChebiCompound;
import uk.ac.ebi.ep.model.ChemblTargets;
import uk.ac.ebi.ep.model.EnzymePortalReaction;
import uk.ac.ebi.ep.model.EnzymeReactionInfo;
import uk.ac.ebi.ep.model.TempCompoundCompare;
import uk.ac.ebi.ep.model.UniprotEntry;
import uk.ac.ebi.ep.model.UniprotXref;
import uk.ac.ebi.ep.model.dao.CofactorView;
import uk.ac.ebi.ep.model.dao.MetaboliteView;
import uk.ac.ebi.ep.model.dao.ReactionInfoView;
import uk.ac.ebi.ep.model.dao.Summary;
import uk.ac.ebi.ep.model.repositories.ChebiCompoundRepository;
import uk.ac.ebi.ep.model.repositories.ChemblTargetsRepository;
import uk.ac.ebi.ep.model.repositories.EnzymePortalChebiCompoundRepository;
import uk.ac.ebi.ep.model.repositories.EnzymePortalCompoundRepository;
import uk.ac.ebi.ep.model.repositories.EnzymePortalMetaboliteRepository;
import uk.ac.ebi.ep.model.repositories.EnzymePortalPathwaysRepository;
import uk.ac.ebi.ep.model.repositories.EnzymePortalReactantRepository;
import uk.ac.ebi.ep.model.repositories.EnzymePortalReactionRepository;
import uk.ac.ebi.ep.model.repositories.EnzymePortalSummaryRepository;
import uk.ac.ebi.ep.model.repositories.EnzymeReactionInfoRepository;
import uk.ac.ebi.ep.model.repositories.TempCompoundCompareRepository;
import uk.ac.ebi.ep.model.repositories.UniprotEntryRepository;
import uk.ac.ebi.ep.model.repositories.UniprotXrefRepository;

/**
 *
 * @author joseph
 */
@Transactional
@Service
public class EnzymePortalParserService {

    @Autowired
    private UniprotXrefRepository xrefRepository;
    @Autowired
    private UniprotEntryRepository uniprotEntryRepository;

    @Autowired
    private EnzymePortalCompoundRepository compoundRepository;

    @Autowired
    private EnzymePortalPathwaysRepository pathwaysRepository;

    @Autowired
    private TempCompoundCompareRepository tempCompoundRepository;

    @Autowired
    private ChebiCompoundRepository chebiCompoundRepository;

    @Autowired
    private EnzymePortalReactionRepository enzymePortalReactionRepository;

    @Autowired
    private ChemblTargetsRepository chemblTargetsRepository;

    @Autowired
    private EnzymePortalSummaryRepository enzymePortalSummaryRepository;

    @Autowired
    private EnzymeReactionInfoRepository enzymeReactionInfoRepository;

    @Autowired
    private EnzymePortalReactantRepository enzymePortalReactantRepository;

    @Autowired
    private EnzymePortalChebiCompoundRepository enzymePortalChebiCompoundRepository;

    @Autowired
    private EnzymePortalMetaboliteRepository enzymePortalMetaboliteRepository;

    @Modifying(clearAutomatically = true)
    @Transactional(readOnly = false)
    public void createMetabolite(String metaboliteId, String metaboliteName, String metaboliteUrl) {
        enzymePortalMetaboliteRepository.createMetabolite(metaboliteId, metaboliteName, metaboliteUrl);

    }

    @Transactional(readOnly = true)
    public List<MetaboliteView> findMetabolites() {

        return enzymePortalMetaboliteRepository.findMetabolites();
    }

    @Modifying(clearAutomatically = true)
    @Transactional(readOnly = false)
    public void createChebiCompound(String chebiId, String chebiName, String synonyms, String relationship, String accession, String url, String role, String note) {
        enzymePortalChebiCompoundRepository.createChebiCompoundIgnoreDup(chebiId, chebiName, synonyms, relationship, accession, url, role, note);

    }

    @Modifying(clearAutomatically = true)
    @Transactional(readOnly = false)
    public void createChebiCompound() {
        enzymePortalChebiCompoundRepository.createChebiCompoundIgnoreDup();

    }

    @Modifying(clearAutomatically = true)
    @Transactional(readOnly = false)
    public void createUniqueChebiCompound(String chebiId, String chebiName, String synonyms, String relationship, String url, String role, String note) {
        enzymePortalChebiCompoundRepository.createUniqueChebiCompound(chebiId, chebiName, synonyms, relationship, url, role, note);

    }

    @Modifying(clearAutomatically = true)
    @Transactional(readOnly = false)
    public void createCofactor(String cofactorId, String cofactorName, String cofactorUrl) {
        compoundRepository.createCofactor(cofactorId, cofactorName, cofactorUrl);

    }

    @Transactional
    public List<CofactorView> findCofactors() {

        return compoundRepository.findCofactors();
    }

    @Transactional
    public Stream<EnzymeReactionInfo> findUniqueXrefReactionInfoByXrefTypeAndStream(String xrefType) {

        return enzymeReactionInfoRepository.findUniqueXrefReactionInfoByXrefTypeAndStream(xrefType);
//        try (Stream<EnzymeReactionInfo> reactionInfo = enzymeReactionInfoRepository.findUniqueXrefReactionInfoByXrefTypeAndStream(xrefType)) {
//            reactionInfo.forEach(data -> System.out.println(" unique " + data));
//
//        }
//        return Stream.empty();
    }

    @Transactional
    public Stream<EnzymeReactionInfo> findReactionInfoByChebiAndStream() {

        return enzymeReactionInfoRepository.findReactionInfoByChebiAndStream();
    }

    @Modifying(clearAutomatically = true)
    @Transactional(readOnly = false)
    public void createReactant(String reactantId, String reactantName, String reactantSource, String relationship, String accession, String url, String reactantRole, String reactantDirection) {
        enzymePortalReactantRepository.createReactantIgnoreDup(reactantId, reactantName, reactantSource, relationship, accession, url, reactantRole, reactantDirection);

    }

    public Long countDistinctReactionInfoByXrefType(String xrefType) {
        return enzymeReactionInfoRepository.countDistinctReactionInfoByXrefType(xrefType);
    }

    public Long countReactionInfo(String xrefType) {
        return enzymeReactionInfoRepository.countReactionInfoByXrefType(xrefType);
    }

//    @Transactional
//    public List<EnzymeReactionInfoPart> loadByPartition(String xrefType) {
//        return enzymeReactionInfoRepository.loadByPartition( xrefType);
//    }
//
//    @Transactional
//    public Stream<EnzymeReactionInfoPart> streamReactionInfoByPartitionAndXrefType(String xrefType) {
//        try (Stream<EnzymeReactionInfoPart> stream = enzymeReactionInfoRepository.streamReactionInfoByPartitionAndXrefType(xrefType)) {
//            stream.forEach(data -> System.out.println("DATA " + data));
//        }
//        return Stream.empty();
//        // return enzymeReactionInfoRepository.streamReactionInfoByPartitionAndXrefType(xrefType);
//    }
    @Transactional
    public Stream<EnzymeReactionInfo> findAllReactionInfoByXrefTypeAndStream(String xrefType) {

        return enzymeReactionInfoRepository.findAllReactionInfoByXrefTypeAndStream(xrefType);
    }

    @Transactional(readOnly = true)
    public Stream<EnzymeReactionInfo> streamLimitedReactionInfoByXrefType(String xrefType, Long limit) {

        return enzymeReactionInfoRepository.streamLimitedReactionInfoByXrefType(xrefType, limit);

    }

    @Transactional
    public Stream<ReactionInfoView> findAllReactionInfoViewByXrefTypeAndStream(String xrefType) {

        return enzymeReactionInfoRepository.findAllReactionInfoViewByXrefTypeAndStream(xrefType);
    }

    @Transactional
    public List<EnzymeReactionInfo> findReactionInfoByXrefType(String xrefType) {
        return enzymeReactionInfoRepository.findReactionInfoByXrefType(xrefType);
    }

    @Transactional
    public List<ReactionInfoView> findReactionInfoViewByXrefType(String xrefType) {
        return enzymeReactionInfoRepository.findReactionInfoViewByXrefType(xrefType);
    }

    @Modifying(clearAutomatically = true)
    @Transactional(readOnly = false)
    public void updateRheaReaction(String keggId, String reactionId) {
        enzymePortalReactionRepository.updateRheaReaction(keggId, reactionId);
    }

    @Modifying(clearAutomatically = true)
    @Transactional(readOnly = false)
    public void addRheaReaction(String rheaId, String reactionName, String reactionSource, String relationship, String accession, String url, String keggId) {
        enzymePortalReactionRepository.addRheaReaction(rheaId, reactionName, reactionSource, relationship, accession, url, keggId);
    }

    @Modifying(clearAutomatically = true)
    @Transactional(readOnly = false)
    public void addRheaReaction(EnzymePortalReaction reaction) {
        enzymePortalReactionRepository.save(reaction);
    }

    @Modifying(clearAutomatically = true)
    @Transactional(readOnly = false)
    public void addRheaReaction(List<EnzymePortalReaction> reactions) {
        enzymePortalReactionRepository.saveAll(reactions);
    }

    @Modifying(clearAutomatically = true)
    @Transactional(readOnly = false)
    public void disableAccessionReactionContraints() {
        enzymePortalReactionRepository.disableAccessionContraints();
    }

    @Modifying(clearAutomatically = true)
    @Transactional(readOnly = false)
    public void deleteNonEnzymesReactions() {
        enzymePortalReactionRepository.deleteNonEnzymesReactions();
    }

    @Modifying(clearAutomatically = true)
    @Transactional(readOnly = false)
    public void enableAccessionReactionContraints() {
        enzymePortalReactionRepository.enableAccessionContraints();
    }

    @Transactional(readOnly = true)
    public Optional<UniprotEntry> findByAccession(String accession) {

        return Optional.ofNullable(uniprotEntryRepository.findByAccession(accession));
    }

    @Transactional(readOnly = true)
    public List<UniprotXref> findUniprotXrefBySource(String source) {
        return xrefRepository.findUniprotXrefBySource(source);
    }

    @Transactional(readOnly = true)
    public List<UniprotXref> updatePDB(List<UniprotXref> pdb) {

        return xrefRepository.saveAll(pdb);
    }

    @Modifying(clearAutomatically = true)
    @Transactional(readOnly = false)
    public void disableTargetContraints() {
        chemblTargetsRepository.disableTargetContraints();
    }

    @Modifying(clearAutomatically = true)
    @Transactional(readOnly = false)
    public void addChemblTargets(String chemblId, String componentType, String accession) {
        chemblTargetsRepository.addChemblTargets(chemblId, componentType, accession);
    }

    @Modifying(clearAutomatically = true)
    @Transactional(readOnly = false)
    public void deleteNonEnzymesTargets() {
        chemblTargetsRepository.deleteNonEnzymesTargets();
    }

    @Modifying(clearAutomatically = true)
    @Transactional(readOnly = false)
    public void enableTargetContraints() {
        chemblTargetsRepository.enableTargetContraints();
    }

    @Transactional(readOnly = true)
    public List<ChemblTargets> findChemblTargets() {
        return chemblTargetsRepository.getAllChemblTargets();
    }

    public void createCompound(String compoundId, String compoundName, String compoundSource, String relationship, String accession, String url, String compoundRole, String note) {
        compoundRepository.createCompoundIgnoreDup(compoundId, compoundName, compoundSource, relationship, accession, url, compoundRole, note);
    }

    public void createPathway(String accession, String pathwayId, String pathwayUrl, String pathwayName, String status, String species) {

        pathwaysRepository.createPathwayIgnoreDup(accession, pathwayId, pathwayUrl, pathwayName, status, species);
    }

    public void createTempCompound(TempCompoundCompare compound) {
        tempCompoundRepository.save(compound);
    }

    public void createTempCompounds(List<TempCompoundCompare> compounds) {
        tempCompoundRepository.saveAll(compounds);
    }

    public void addTempCompound(String primaryTargetId, String compoundId, String compoundName, String compoundSource, String relationship, String accession, String url, String compoundRole, String note) {
        tempCompoundRepository.addTempCompounds(primaryTargetId, compoundId, compoundName, compoundSource, relationship, accession, url, compoundRole, note);
    }

    public void insertCompoundsFromTempTable() {
        compoundRepository.insertCompounds();
    }

    @Transactional(readOnly = true)
    public ChebiCompound findChebiCompoundById(String chebiId) {

        return chebiCompoundRepository.findByChebiAccession(chebiId);
    }

    @Transactional(readOnly = true)
    public ChebiCompound findChebiCompoundByName(String compoundName) {

        return chebiCompoundRepository.findByCompoundName(compoundName);
    }

    @Transactional(readOnly = true)
    public List<ChebiCompound> findChebiCompoundById(String chebiId, String source) {

        return chebiCompoundRepository.findByChebiAccession(chebiId, source);
    }

    @Transactional(readOnly = true)
    public List<ChebiCompound> findChebiCompoundByName(String compoundName, String source) {

        return chebiCompoundRepository.findByCompoundName(compoundName, source);
    }

    @Transactional(readOnly = true)
    public List<Summary> findSummariesByCommentType(String commentType) {

        return enzymePortalSummaryRepository.findSummariesByCommentType(commentType);
    }

    @Transactional(readOnly = true)
    public List<String> findUniqueTargetedproteins() {

        return tempCompoundRepository.findUniqueTargetedproteins();
    }

    @Transactional(readOnly = true)
    public List<String> findTargetetsByProtein(String accession) {

        return tempCompoundRepository.findTargetetsByProtein(accession);
    }

}
