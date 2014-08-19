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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.data.domain.EnzymePortalCompound;
import uk.ac.ebi.ep.data.domain.EnzymePortalDisease;
import uk.ac.ebi.ep.data.domain.EnzymePortalSummary;
import uk.ac.ebi.ep.data.domain.QEnzymePortalCompound;
import uk.ac.ebi.ep.data.domain.QUniprotEntry;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.repositories.DiseaseRepository;
import uk.ac.ebi.ep.data.repositories.EnzymePortalCompoundRepository;
import uk.ac.ebi.ep.data.repositories.EnzymeSummaryRepository;
import uk.ac.ebi.ep.data.repositories.ReactionRepository;
import uk.ac.ebi.ep.data.repositories.UniprotEntryRepository;

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
    private EnzymeSummaryRepository enzymeSummaryRepository;

    @Autowired
    private ReactionRepository reactionRepository;
    
    
        @Transactional(readOnly = true)
    public UniprotEntry findByAccession(String accession) {

        return uniprotEntryRepository.findByAccession(accession);
    }
    
            @Transactional(readOnly = true)
    public EnzymePortalSummary findEnzymeSummaryByAccession(String accession) {

        return enzymeSummaryRepository.findByAccession(accession);
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
    @Transactional(readOnly = true)
    public List<EnzymePortalCompound> findCompoundsByUniprotName(String uniprotName) {

        return enzymePortalCompoundRepository.findCompoundsByUniprotName(uniprotName);
    }

    @Transactional(readOnly = true)
    public List<EnzymePortalCompound> findCompoundsByNamePrefix(List<String> name_prefixes) {

        return enzymePortalCompoundRepository.findByNamePrefixes(name_prefixes);
    }

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
    public List<UniprotEntry> findEnzymesByAccessions(List<String> accessions) {

        return uniprotEntryRepository.findEnzymesByAccessions(accessions);
    }
    
       @Transactional(readOnly = true)
    public List<UniprotEntry> findEnzymeByNamePrefixAndProteinName(String namePrefix,String proteinName) {

        return uniprotEntryRepository.findEnzymeByNamePrefixAndProteinName(namePrefix,proteinName);
    } 
    

    @Transactional(readOnly = true)
    public List<EnzymePortalSummary> findEnzymeSummariesByNamePrefixes(List<String> namePrefixes) {

        return enzymeSummaryRepository.findEnzymesByNamePrefixes(namePrefixes);
    }

    @Transactional(readOnly = true)
    public List<EnzymePortalSummary> findEnzymeSumariesByAccessions(List<String> accessions) {

        return enzymeSummaryRepository.findEnzymesByAccessions(accessions);
    }

    private static Predicate enzymesByCompoundId(String compoundId) {

        QEnzymePortalCompound compound = QEnzymePortalCompound.enzymePortalCompound;
        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;

        Predicate predicate = enzyme.enzymePortalCompoundSet.any().compoundId.equalsIgnoreCase(compoundId);

        return predicate;
    }

}
