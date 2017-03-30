/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.data.domain.ChebiCompound;
import uk.ac.ebi.ep.data.domain.TempCompoundCompare;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.domain.UniprotXref;
import uk.ac.ebi.ep.data.repositories.ChebiCompoundRepository;
import uk.ac.ebi.ep.data.repositories.DiseaseRepository;
import uk.ac.ebi.ep.data.repositories.EnzymePortalCompoundRepository;
import uk.ac.ebi.ep.data.repositories.EnzymePortalPathwaysRepository;
import uk.ac.ebi.ep.data.repositories.TempCompoundCompareRepository;
import uk.ac.ebi.ep.data.repositories.UniprotEntryRepository;
import uk.ac.ebi.ep.data.repositories.UniprotXrefRepository;

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
    private DiseaseRepository diseaseRepository;

    @Autowired
    private TempCompoundCompareRepository tempCompoundRepository;

    @Autowired
    private ChebiCompoundRepository chebiCompoundRepository;

    @Transactional(readOnly = true)
    public Optional<UniprotEntry> findByAccession(String accession) {

        return Optional.ofNullable(uniprotEntryRepository.findByAccession(accession));
    }

    @Transactional(readOnly = true)
    public UniprotXref findByPdbId(String pdbId) {

        return xrefRepository.findPdbById(pdbId);
    }

    @Transactional(readOnly = true)
    public List<UniprotXref> findPDBcodes() {
        return xrefRepository.findPDBcodes();
    }

    @Transactional(readOnly = true)
    public List<String> findPdbIdsWithNoNames() {
        return xrefRepository.findPdbCodesWithNoNames();
    }

    @Transactional(readOnly = true)
    public List<UniprotXref> updatePDB(List<UniprotXref> pdb) {

        return xrefRepository.save(pdb);
    }

    public void createCompound(String compoundId, String compoundName, String compoundSource, String relationship, String accession, String url, String compoundRole, String note) {
        compoundRepository.createCompoundIgnoreDup(compoundId, compoundName, compoundSource, relationship, accession, url, compoundRole, note);
    }

    public void createPathway(String accession, String pathwayId, String pathwayUrl, String pathwayName, String status, String species) {

        pathwaysRepository.createPathwayIgnoreDup(accession, pathwayId, pathwayUrl, pathwayName, status, species);
    }

    public void createDisease(String accession, String omimNumber, String meshId, String efoId, String diseaseName, String evidence, String definition, String score, String url) {

        diseaseRepository.createDiseaseIgnoreDup(accession, omimNumber, meshId, efoId, diseaseName, evidence, definition, score, url);
    }

    public void createTempCompound(TempCompoundCompare compound) {
        tempCompoundRepository.save(compound);
    }

    public void createTempCompounds(List<TempCompoundCompare> compounds) {
        tempCompoundRepository.save(compounds);
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
