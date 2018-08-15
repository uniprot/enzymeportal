package uk.ac.ebi.ep.model.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.model.ChebiCompound;
import uk.ac.ebi.ep.model.ChemblTargets;
import uk.ac.ebi.ep.model.EnzymePortalReaction;
import uk.ac.ebi.ep.model.TempCompoundCompare;
import uk.ac.ebi.ep.model.UniprotEntry;
import uk.ac.ebi.ep.model.UniprotXref;
import uk.ac.ebi.ep.model.repositories.ChebiCompoundRepository;
import uk.ac.ebi.ep.model.repositories.ChemblTargetsRepository;
import uk.ac.ebi.ep.model.repositories.EnzymePortalCompoundRepository;
import uk.ac.ebi.ep.model.repositories.EnzymePortalPathwaysRepository;
import uk.ac.ebi.ep.model.repositories.EnzymePortalReactionRepository;
import uk.ac.ebi.ep.model.repositories.TempCompoundCompareRepository;
import uk.ac.ebi.ep.model.repositories.UniprotEntryRepository;
import uk.ac.ebi.ep.model.repositories.UniprotXrefRepository;

/**
 *
 * @author joseph
 */
//@Transactional
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
    public void disableAccessionContraints() {
        enzymePortalReactionRepository.disableAccessionContraints();
    }

    @Modifying(clearAutomatically = true)
    @Transactional(readOnly = false)
    public void deleteNonEnzymesReactions() {
        enzymePortalReactionRepository.deleteNonEnzymesReactions();
    }

    @Modifying(clearAutomatically = true)
    @Transactional(readOnly = false)
    public void enableAccessionContraints() {
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

    public void addTempCompound(String compoundId, String compoundName, String compoundSource, String relationship, String accession, String url, String compoundRole, String note) {
        tempCompoundRepository.addTempCompounds(compoundId, compoundName, compoundSource, relationship, accession, url, compoundRole, note);
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
}
