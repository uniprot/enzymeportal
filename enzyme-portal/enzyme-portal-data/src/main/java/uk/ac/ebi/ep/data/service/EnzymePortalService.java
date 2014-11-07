/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.service;

import com.mysema.query.types.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.data.domain.EnzymePortalCompound;
import uk.ac.ebi.ep.data.domain.EnzymePortalDisease;
import uk.ac.ebi.ep.data.domain.EnzymePortalPathways;
import uk.ac.ebi.ep.data.domain.EnzymePortalReaction;
import uk.ac.ebi.ep.data.domain.EnzymePortalSummary;
import uk.ac.ebi.ep.data.domain.QUniprotEntry;
import uk.ac.ebi.ep.data.domain.RelatedProteins;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.domain.UniprotXref;
import uk.ac.ebi.ep.data.repositories.DiseaseRepository;
import uk.ac.ebi.ep.data.repositories.EnzymePortalCompoundRepository;
import uk.ac.ebi.ep.data.repositories.EnzymePortalEcNumbersRepository;
import uk.ac.ebi.ep.data.repositories.EnzymePortalPathwaysRepository;
import uk.ac.ebi.ep.data.repositories.EnzymePortalReactionRepository;
import uk.ac.ebi.ep.data.repositories.EnzymePortalSummaryRepository;
import uk.ac.ebi.ep.data.repositories.RelatedProteinsRepository;
import uk.ac.ebi.ep.data.repositories.UniprotEntryRepository;
import uk.ac.ebi.ep.data.repositories.UniprotXrefRepository;
import uk.ac.ebi.ep.data.search.model.EnzymeSummary;
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
        
        return uniprotEntryRepository.findByAccession(accession);
    }
    
    @Transactional(readOnly = true)
    public EnzymePortalSummary findEnzymeSummaryByAccession(String accession) {
        
        return enzymeSummaryRepository.findEnzymeSummaryByAccession(accession);
    }
    
    @Transactional(readOnly = true)
    public List<String> findAllUniprotAccessions() {
        
        return uniprotEntryRepository.findAccession();
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
    public List<EnzymePortalCompound> findCompoundsByNamePrefix(List<String> name_prefixes) {
        
        return enzymePortalCompoundRepository.findCompoundsByNameprefixes(name_prefixes);
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
    public List<UniprotEntry> findEnzymesByNamePrefixes(List<String> namePrefixes) {
        
        return uniprotEntryRepository.findEnzymesByNamePrefixes(namePrefixes);
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
    public List<UniprotEntry> findEnzymeByNamePrefixAndProteinName(String namePrefix, String proteinName) {
        
        return uniprotEntryRepository.findEnzymeByNamePrefixAndProteinName(namePrefix, proteinName);
    }
    
    @Transactional(readOnly = true)
    public List<UniprotEntry> findEnzymeByAccessionsAndProteinName(List<String> accessions, String proteinName) {
        
        return uniprotEntryRepository.findEnzymeByAccessionsAndProteinName(accessions, proteinName);
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
    public List<EnzymePortalDisease> findDiseasesByAccession(String accession) {
        
        return diseaseRepository.findDiseasesByAccession(accession);
    }
    
    @Transactional(readOnly = true)
    public List<EnzymePortalDisease> findDiseasesByNamePrefix(List<String> name_prefixes) {
        
        return diseaseRepository.findDiseasesByNamePrefixes(name_prefixes);
    }
    
    @Transactional(readOnly = true)
    public List<UniprotXref> findPDBcodesByAccession(String accession) {
        
        return xrefRepository.findPDBcodesByAccession(accession);
    }
    
    @Transactional(readOnly = true)
    public List<EnzymePortalPathways> findPathwaysByAccession(String accession) {
        
        return pathwaysRepository.findPathwaysByAccession(accession);
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
    public List<String> findEnzymesByCompound(String compound_id) {
        
        return enzymePortalCompoundRepository.findEnzymesByCompound(compound_id);
    }
    
    @Transactional(readOnly = true)
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
    public EnzymePortalReaction findReactionByAccession(String accession) {
        
        return reactionRepository.findReactionByAccession(accession);
    }
    
    @Transactional(readOnly = true)
    public List<String> findAccessionsByPathwayId(String pathwayId) {
        
        return pathwaysRepository.findAccessionsByPathwayId(pathwayId);
    }
    
    @Transactional(readOnly = true)
    public List<String> findAccessionsByTaxId(Long taxId) {
        
        return uniprotEntryRepository.findAccessionsByTaxId(taxId);
    }
    
    public List<Taxonomy> findModelOrganisms() {
        return uniprotEntryRepository.findModelOrganisms();
    }
    
    public List<UniprotEntry> findEnzymesByTaxId(Long taxId) {
        return uniprotEntryRepository.findEnzymesByTaxId(taxId);
    }
    
    public Page<UniprotEntry> findEnzymesByTaxonomy(Long taxId, Pageable pageable) {
        
        return uniprotEntryRepository.findAll(enzymesByTaxId(taxId), pageable);
    }

   
    public Page<UniprotEntry> findEnzymesByAccessionsPageable(List<String> accessions, Pageable pageable) {
        
        return uniprotEntryRepository.findAll(enzymesByAccessions(accessions), pageable);
        
    }
    
    public Page<EnzymeSummary> findEnzymesByAccessions(List<String> accessions, Pageable pageable) {
        
        return uniprotEntryRepository.findEnzymesByAccessions(accessions, pageable);
    }
    
    @Transactional(readOnly = true)
    public List<EnzymeSummary> findSummariesByAccessions(List<String> accessions) {
        
        return enzymeSummaryRepository.findSummariesBYAccessions(accessions);
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

    //delete later
//    private static Predicate enzymesByAccessionsDELETE(List<String> accessions) {
//        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;
//        Predicate predicate = enzyme.accession.in(accessions);
//        CollQueryFactory.from(enzyme).where(predicate).list(enzyme);
//        return predicate;
//    }
    
}
